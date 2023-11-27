package com.yuzhi.tender.service.impl;

import com.yuzhi.tender.domain.BidInfo;
import com.yuzhi.tender.repository.BidInfoRepository;
import com.yuzhi.tender.service.BidInfoService;
import com.yuzhi.tender.service.dto.BidInfoComputeDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
                if (bidInfo.getValidPrice() != null) {
                    existingBidInfo.setValidPrice(bidInfo.getValidPrice());
                }
                if (bidInfo.getValidAverageValue() != null) {
                    existingBidInfo.setValidAverageValue(bidInfo.getValidAverageValue());
                }
                if (bidInfo.getDeclineRatio() != null) {
                    existingBidInfo.setDeclineRatio(bidInfo.getDeclineRatio());
                }
                if (bidInfo.getBasePrice() != null) {
                    existingBidInfo.setBasePrice(bidInfo.getBasePrice());
                }
                if (bidInfo.getBenchmarkScore() != null) {
                    existingBidInfo.setBenchmarkScore(bidInfo.getBenchmarkScore());
                }
                if (bidInfo.getRanking() != null) {
                    existingBidInfo.setRanking(bidInfo.getRanking());
                }

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
    public List<BidInfo> findAll() {
        return bidInfoRepository.findAll();
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
     * 获取有效报价
     */
    @Override
    public List<BidInfo> getValidPrice(List<BidInfo> bidInfos) {
        //开始计算有效报价,投标报价低于全部合格投标人投标报价平均值30%以上的，不纳入基准价计算。
        //List<BidInfo> updatedBidInfos = bidInfoRepository.findAll();
        List<BidInfo> updatedBidInfos = bidInfos;

        // 过滤出报价不为0的记录
        List<BidInfo> nonZeroBidInfos = updatedBidInfos
            .stream()
            .filter(bidInfo -> bidInfo.getBidPrice().compareTo(BigDecimal.ZERO) > 0)
            .collect(Collectors.toList());

        //--计算平均值
        BigDecimal sum = nonZeroBidInfos.stream().map(BidInfo::getBidPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal originalAveragePrice;
        if (!nonZeroBidInfos.isEmpty()) {
            originalAveragePrice = sum.divide(new BigDecimal(nonZeroBidInfos.size()), 6, RoundingMode.HALF_UP);
        } else {
            originalAveragePrice = new BigDecimal("0.000000");
        }
        log.debug("*****======  Compute:originalAveragePrice is ======" + originalAveragePrice);

        // 计算originalAveragePrice的30%
        BigDecimal threshold = originalAveragePrice.multiply(new BigDecimal("0.30"));

        //更新每条记录的平均价
        //如果原来报价为0，或者投标报价低于全部合格投标人投标报价平均值30%以上的，有效报价都为0
        updatedBidInfos.forEach(bidInfo -> {
            bidInfo.setAverageValue(originalAveragePrice);
            if (bidInfo.getBidPrice().compareTo(threshold) < 0 || bidInfo.getBidPrice().compareTo(BigDecimal.ZERO) == 0) {
                bidInfo.setValidPrice(BigDecimal.ZERO);
            } else {
                bidInfo.setValidPrice(bidInfo.getBidPrice());
            }
        });

        //新的数组里面去除有效报价为0的记录
        updatedBidInfos.removeIf(bidInfo -> bidInfo.getValidPrice().compareTo(BigDecimal.ZERO) == 0);
        return updatedBidInfos;
    }

    /**
     * 根据公式得出计算结果，更新到每一条记录里面的基准评分
     * @return
     */
    @Override
    public List<BidInfo> batchComputing(BidInfoComputeDTO bidInfoComputeDTO) {
        List<BidInfo> allBidInfos = bidInfoRepository.findAll();
        // 按bidSectionId分组
        Map<String, List<BidInfo>> groupedByBidSectionId = allBidInfos.stream().collect(Collectors.groupingBy(BidInfo::getBidSectionId));

        List<BidInfo> sortedBidInfos = null;
        // 针对每个分组进行计算
        for (Map.Entry<String, List<BidInfo>> entry : groupedByBidSectionId.entrySet()) {
            String bidSectionId = entry.getKey();
            List<BidInfo> bidInfosForSection = entry.getValue();

            // 在这里针对当前的bidSectionId进行你的计算...
            // for (BidInfo bidInfo : bidInfosForSection) { }

            //开始计算有效报价,投标报价低于全部合格投标人投标报价平均值30%以上的，不纳入基准价计算。
            //去除报价为0的记录
            List<BidInfo> bidInfos = this.getValidPrice(bidInfosForSection);

            // 过滤出报价不为0的记录
            List<BidInfo> nonZeroBidInfos = bidInfos
                .stream()
                .filter(bidInfo -> bidInfo.getBidPrice().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

            //开始计算有效平均值
            BigDecimal sum = nonZeroBidInfos.stream().map(BidInfo::getValidPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal validAveragePrice = new BigDecimal("0.000000");
            log.debug("*****==== valid sum=" + sum + " valid.size is:" + nonZeroBidInfos.size());
            if (!bidInfos.isEmpty()) {
                validAveragePrice = sum.divide(new BigDecimal(nonZeroBidInfos.size()), 6, RoundingMode.HALF_UP);
            }
            log.debug("*****======  Compute:ValidAveragePrice is ======" + validAveragePrice);

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
            basePrice = validAveragePrice.multiply(BigDecimal.ONE.subtract(ratioBigDecimal)).setScale(6, RoundingMode.HALF_UP);
            log.debug("*****======  Compute:BasePrice is ======" + basePrice);

            //当评标总价P≥B，得分=100-100×n1×(P-B)/B；
            //当评标总价P＜B，得分=100-100×n2×(B-P)/B，当计算出的价格评审得分≤0时，价格部分得分为0分。
            BigDecimal currentBidPrice = new BigDecimal("0.000000");
            for (BidInfo bidInfo : bidInfos) {
                double benchmarkScore = 0.0;
                currentBidPrice = bidInfo.getBidPrice();
                log.debug("*****======  Compute:Base Price is ======" + basePrice);
                if (currentBidPrice.compareTo(basePrice) >= 0) {
                    benchmarkScore = 100 - (100 * n1 * (currentBidPrice.subtract(basePrice)).doubleValue()) / basePrice.doubleValue();
                } else {
                    benchmarkScore = 100 - (100 * n2 * (basePrice.subtract(currentBidPrice)).doubleValue()) / basePrice.doubleValue();
                    if (benchmarkScore <= 0) benchmarkScore = 0.0; // Ensure benchmarkScore doesn't go below 0
                }
                benchmarkScore = Math.round(benchmarkScore * 1000.0) / 1000.0;
                log.debug("*****======  Compute:Score is ======" + benchmarkScore);
                //写入数据库
                bidInfo.setValidAverageValue(validAveragePrice);
                bidInfo.setDeclineRatio(ratio);
                bidInfo.setBasePrice(basePrice);
                bidInfo.setBenchmarkScore(benchmarkScore);
            }

            // 根据benchmarkScore来设置ranking的大小
            // Sort bidInfos based on benchmarkScore in descending order
            //List<BidInfo> sortedBidInfos = bidInfos.stream()
            sortedBidInfos =
                bidInfos.stream().sorted(Comparator.comparingDouble(BidInfo::getBenchmarkScore).reversed()).collect(Collectors.toList());

            // Set ranking based on the order in the sorted list
            for (int i = 0; i < sortedBidInfos.size(); i++) {
                sortedBidInfos.get(i).setRanking(i + 1);
            }
        }
        return sortedBidInfos;
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
