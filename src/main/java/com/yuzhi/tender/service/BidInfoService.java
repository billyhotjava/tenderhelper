package com.yuzhi.tender.service;

import com.yuzhi.tender.domain.BidInfo;
import com.yuzhi.tender.service.dto.BidInfoComputeDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.yuzhi.tender.domain.BidInfo}.
 */
public interface BidInfoService {
    /**
     * Save a bidInfo.
     *
     * @param bidInfo the entity to save.
     * @return the persisted entity.
     */
    BidInfo save(BidInfo bidInfo);

    /**
     * Updates a bidInfo.
     *
     * @param bidInfo the entity to update.
     * @return the persisted entity.
     */
    BidInfo update(BidInfo bidInfo);

    /**
     * Partially updates a bidInfo.
     *
     * @param bidInfo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BidInfo> partialUpdate(BidInfo bidInfo);

    /**
     * Get all the bidInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BidInfo> findAll(Pageable pageable);

    /**
     * Get the "id" bidInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BidInfo> findOne(Long id);

    /**
     * Delete the "id" bidInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Delete all bidInfos
     */
    void deleteAll();
    /**
     * 计算基准评分
     */
    List<BidInfo> batchComputing(BidInfoComputeDTO bidInfoComputeDTO);
}
