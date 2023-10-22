package com.yuzhi.tender.service.impl;

import com.yuzhi.tender.domain.BidInfo;
import com.yuzhi.tender.repository.BidInfoRepository;
import com.yuzhi.tender.service.BidInfoService;
import com.yuzhi.tender.service.dto.BidInfoComputeDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yuzhi.tender.domain.BidInfo}.
 */
@Service
@Transactional
public class BidInfoServiceImpl implements BidInfoService {

    private final Logger log = LoggerFactory.getLogger(BidInfoServiceImpl.class);

    private final BidInfoRepository bidInfoRepository;

    public BidInfoServiceImpl(BidInfoRepository bidInfoRepository) {
        this.bidInfoRepository = bidInfoRepository;
    }

    @Override
    public BidInfo save(BidInfo bidInfo) {
        log.debug("Request to save BidInfo : {}", bidInfo);
        return bidInfoRepository.save(bidInfo);
    }

    @Override
    public BidInfo update(BidInfo bidInfo) {
        log.debug("Request to update BidInfo : {}", bidInfo);
        return bidInfoRepository.save(bidInfo);
    }

    @Override
    public Optional<BidInfo> partialUpdate(BidInfo bidInfo) {
        log.debug("Request to partially update BidInfo : {}", bidInfo);

        return bidInfoRepository
            .findById(bidInfo.getId())
            .map(existingBidInfo -> {
                if (bidInfo.getBidPrjId() != null) {
                    existingBidInfo.setBidPrjId(bidInfo.getBidPrjId());
                }
                if (bidInfo.getBidPrjName() != null) {
                    existingBidInfo.setBidPrjName(bidInfo.getBidPrjName());
                }
                if (bidInfo.getBidSectionId() != null) {
                    existingBidInfo.setBidSectionId(bidInfo.getBidSectionId());
                }
                if (bidInfo.getBidSection() != null) {
                    existingBidInfo.setBidSection(bidInfo.getBidSection());
                }
                if (bidInfo.getBidder() != null) {
                    existingBidInfo.setBidder(bidInfo.getBidder());
                }
                if (bidInfo.getBidPrice() != null) {
                    existingBidInfo.setBidPrice(bidInfo.getBidPrice());
                }
                if (bidInfo.getAverageValue() != null) {
                    existingBidInfo.setAverageValue(bidInfo.getAverageValue());
                }
                if (bidInfo.getDeclineRatio() != 0.0) {
                    existingBidInfo.setDeclineRatio(bidInfo.getDeclineRatio());
                }
                if (bidInfo.getBasePrice() != null) {
                    existingBidInfo.setBasePrice(bidInfo.getBasePrice());
                }
                //if (bidInfo.getBenchmarkScore() != 0.0) {
                existingBidInfo.setBenchmarkScore(bidInfo.getBenchmarkScore());
                //}

                return existingBidInfo;
            })
            .map(bidInfoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BidInfo> findAll(Pageable pageable) {
        log.debug("Request to get all BidInfos");
        return bidInfoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BidInfo> findOne(Long id) {
        log.debug("Request to get BidInfo : {}", id);
        return bidInfoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BidInfo : {}", id);
        bidInfoRepository.deleteById(id);
    }

    /**
     * 根据公式得出计算结果，更新到每一条记录里面的基准评分
     * @return
     */
    @Override
    public List<BidInfo> batchComputing(BidInfoComputeDTO bidInfoComputeDTO) {
        List<BidInfo> updatedBidInfos = bidInfoRepository.findAll();

        //--计算平均值
        BigDecimal sum = updatedBidInfos.stream().map(BidInfo::getBidPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal average = new BigDecimal("0.000000");
        if (!updatedBidInfos.isEmpty()) {
            average = sum.divide(new BigDecimal(updatedBidInfos.size()), 6, RoundingMode.HALF_UP);
        }
        log.debug("*****======  Compute:Average is ======" + average);

        //获取统一的下浮比例,N1值，N2值，同一批次的下浮比例是一样的,按精度小数后6位
        double ratio = bidInfoComputeDTO.getDeclineRatio();
        double n1 = bidInfoComputeDTO.getN1();
        double n2 = bidInfoComputeDTO.getN2();
        log.debug("*****======  Compute:DeclineRatio is ======" + ratio);
        log.debug("*****======  Compute:N1 is ======" + n1);
        log.debug("*****======  Compute:N2 is ======" + n2);

        //计算基准价,公式：设A1=全部有效报价算术均值，a为下浮比例，基准价B=A1×（1-a）
        BigDecimal basePrice;
        BigDecimal ratioBigDecimal = BigDecimal.valueOf(ratio);
        basePrice = average.multiply(BigDecimal.ONE.subtract(ratioBigDecimal)).setScale(6, RoundingMode.HALF_UP);
        log.debug("*****======  Compute:BasePrice is ======" + basePrice);

        //当评标总价P≥B，得分=100-100×n1×(P-B)/B；
        //当评标总价P＜B，得分=100-100×n2×(B-P)/B，当计算出的价格评审得分≤0时，价格部分得分为0分。
        BigDecimal currentBidPrice = new BigDecimal("0.000000");
        for (BidInfo bidInfo : updatedBidInfos) {
            double benchmarkScore = 0.0;
            currentBidPrice = bidInfo.getBidPrice();
            log.debug("*****======  Compute:Base Price is ======" + basePrice);
            if (currentBidPrice.compareTo(basePrice) >= 0) {
                benchmarkScore = 100 - (100 * n1 * (currentBidPrice.subtract(basePrice)).doubleValue()) / basePrice.doubleValue();
            } else {
                benchmarkScore = 100 - (100 * n2 * (basePrice.subtract(currentBidPrice)).doubleValue()) / basePrice.doubleValue();
                if (benchmarkScore <= 0) benchmarkScore = 0.0; // Ensure benchmarkScore doesn't go below 0
            }
            log.debug("*****======  Compute:Score is ======" + benchmarkScore);
            //写入数据库
            bidInfo.setAverageValue(average);
            bidInfo.setDeclineRatio(ratio);
            bidInfo.setBasePrice(basePrice);
            bidInfo.setBenchmarkScore(benchmarkScore);
        }
        return updatedBidInfos;
    }

    /**
     * Delete all data
     */
    @Override
    public void deleteAll() {
        log.debug("Delete all data in servie");
        bidInfoRepository.deleteAll();
    }
}
