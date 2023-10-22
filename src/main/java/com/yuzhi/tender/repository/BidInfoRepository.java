package com.yuzhi.tender.repository;

import com.yuzhi.tender.domain.BidInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BidInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BidInfoRepository extends JpaRepository<BidInfo, Long> {}
