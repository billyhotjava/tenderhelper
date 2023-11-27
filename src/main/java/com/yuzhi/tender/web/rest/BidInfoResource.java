package com.yuzhi.tender.web.rest;

import com.yuzhi.tender.domain.BidInfo;
import com.yuzhi.tender.repository.BidInfoRepository;
import com.yuzhi.tender.service.BidInfoService;
import com.yuzhi.tender.service.dto.BidInfoComputeDTO;
import com.yuzhi.tender.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yuzhi.tender.domain.BidInfo}.
 */
@RestController
@RequestMapping("/api")
public class BidInfoResource {

    private final Logger log = LoggerFactory.getLogger(BidInfoResource.class);

    private static final String ENTITY_NAME = "bidInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BidInfoService bidInfoService;

    private final BidInfoRepository bidInfoRepository;

    public BidInfoResource(BidInfoService bidInfoService, BidInfoRepository bidInfoRepository) {
        this.bidInfoService = bidInfoService;
        this.bidInfoRepository = bidInfoRepository;
    }

    /**
     * {@code POST  /bid-infos} : Create a new bidInfo.
     *
     * @param bidInfo the bidInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bidInfo, or with status {@code 400 (Bad Request)} if the bidInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bid-infos")
    public ResponseEntity<BidInfo> createBidInfo(@RequestBody BidInfo bidInfo) throws URISyntaxException {
        log.debug("REST request to save BidInfo : {}", bidInfo);
        if (bidInfo.getId() != null) {
            throw new BadRequestAlertException("A new bidInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BidInfo result = bidInfoService.save(bidInfo);
        return ResponseEntity
            .created(new URI("/api/bid-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bid-infos/:id} : Updates an existing bidInfo.
     *
     * @param id the id of the bidInfo to save.
     * @param bidInfo the bidInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bidInfo,
     * or with status {@code 400 (Bad Request)} if the bidInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bidInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bid-infos/{id}")
    public ResponseEntity<BidInfo> updateBidInfo(@PathVariable(value = "id", required = false) final Long id, @RequestBody BidInfo bidInfo)
        throws URISyntaxException {
        log.debug("REST request to update BidInfo : {}, {}", id, bidInfo);
        if (bidInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bidInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bidInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BidInfo result = bidInfoService.update(bidInfo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bidInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bid-infos/:id} : Partial updates given fields of an existing bidInfo, field will ignore if it is null
     *
     * @param id the id of the bidInfo to save.
     * @param bidInfo the bidInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bidInfo,
     * or with status {@code 400 (Bad Request)} if the bidInfo is not valid,
     * or with status {@code 404 (Not Found)} if the bidInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the bidInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bid-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BidInfo> partialUpdateBidInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BidInfo bidInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update BidInfo partially : {}, {}", id, bidInfo);
        if (bidInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bidInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bidInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BidInfo> result = bidInfoService.partialUpdate(bidInfo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bidInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /bid-infos} : get all the bidInfos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bidInfos in body.
     */
    @GetMapping("/bid-infos")
    public ResponseEntity<List<BidInfo>> getAllBidInfos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of BidInfos");
        Page<BidInfo> page = bidInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/bid-infos/all")
    public ResponseEntity<List<BidInfo>> getAllBidInfos() {
        log.debug("REST request to get all BidInfos");
        List<BidInfo> list = bidInfoService.findAll();
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /bid-infos/:id} : get the "id" bidInfo.
     *
     * @param id the id of the bidInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bidInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bid-infos/{id}")
    public ResponseEntity<BidInfo> getBidInfo(@PathVariable Long id) {
        log.debug("REST request to get BidInfo : {}", id);
        Optional<BidInfo> bidInfo = bidInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bidInfo);
    }

    /**
     * {@code DELETE  /bid-infos/:id} : delete the "id" bidInfo.
     *
     * @param id the id of the bidInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bid-infos/{id}")
    public ResponseEntity<Void> deleteBidInfo(@PathVariable Long id) {
        log.debug("REST request to delete BidInfo : {}", id);
        bidInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * 统一计算基准评分
     */
    @PostMapping("/bid-infos/batch-computing")
    public ResponseEntity<List<BidInfo>> batchUpdateBidInfos(
        @RequestBody BidInfoComputeDTO computeData,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("========REST request to batch computing BidInfos ===========");
        log.debug(computeData.toString());
        List<BidInfo> result = bidInfoService.batchComputing(computeData);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 删除表中所有数据
     */
    @DeleteMapping("/bid-infos/deleteAllData")
    public ResponseEntity<String> deleteAllData() {
        log.debug("=====REST request to delete all bidinfos");
        try {
            bidInfoService.deleteAll(); // 假设DataService中有这样一个方法
            return ResponseEntity.ok("All data deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting data: " + e.getMessage());
        }
    }
}
