package com.yuzhi.tender.web.rest;

import static com.yuzhi.tender.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yuzhi.tender.IntegrationTest;
import com.yuzhi.tender.domain.BidInfo;
import com.yuzhi.tender.repository.BidInfoRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BidInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BidInfoResourceIT {

    private static final String DEFAULT_BID_PRJ_ID = "AAAAAAAAAA";
    private static final String UPDATED_BID_PRJ_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BID_PRJ_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BID_PRJ_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BID_SECTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_BID_SECTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BID_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_BID_SECTION = "BBBBBBBBBB";

    private static final String DEFAULT_BIDDER = "AAAAAAAAAA";
    private static final String UPDATED_BIDDER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BID_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BID_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AVERAGE_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVERAGE_VALUE = new BigDecimal(2);

    private static final double DEFAULT_DECLINE_RATIO = 1.00;
    private static final double UPDATED_DECLINE_RATIO = 2.00;

    private static final BigDecimal DEFAULT_BASE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BASE_PRICE = new BigDecimal(2);

    private static final double DEFAULT_BENCHMARK_SCORE = 0.00;
    private static final double UPDATED_BENCHMARK_SCORE = 2.00;

    private static final String ENTITY_API_URL = "/api/bid-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BidInfoRepository bidInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBidInfoMockMvc;

    private BidInfo bidInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BidInfo createEntity(EntityManager em) {
        BidInfo bidInfo = new BidInfo()
            .bidPrjId(DEFAULT_BID_PRJ_ID)
            .bidPrjName(DEFAULT_BID_PRJ_NAME)
            .bidSectionId(DEFAULT_BID_SECTION_ID)
            .bidSection(DEFAULT_BID_SECTION)
            .bidder(DEFAULT_BIDDER)
            .bidPrice(DEFAULT_BID_PRICE)
            .averageValue(DEFAULT_AVERAGE_VALUE)
            .declineRatio(DEFAULT_DECLINE_RATIO)
            .basePrice(DEFAULT_BASE_PRICE)
            .benchmarkScore(DEFAULT_BENCHMARK_SCORE);
        return bidInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BidInfo createUpdatedEntity(EntityManager em) {
        BidInfo bidInfo = new BidInfo()
            .bidPrjId(UPDATED_BID_PRJ_ID)
            .bidPrjName(UPDATED_BID_PRJ_NAME)
            .bidSectionId(UPDATED_BID_SECTION_ID)
            .bidSection(UPDATED_BID_SECTION)
            .bidder(UPDATED_BIDDER)
            .bidPrice(UPDATED_BID_PRICE)
            .averageValue(UPDATED_AVERAGE_VALUE)
            .declineRatio(UPDATED_DECLINE_RATIO)
            .basePrice(UPDATED_BASE_PRICE)
            .benchmarkScore(UPDATED_BENCHMARK_SCORE);
        return bidInfo;
    }

    @BeforeEach
    public void initTest() {
        bidInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createBidInfo() throws Exception {
        int databaseSizeBeforeCreate = bidInfoRepository.findAll().size();
        // Create the BidInfo
        restBidInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bidInfo)))
            .andExpect(status().isCreated());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeCreate + 1);
        BidInfo testBidInfo = bidInfoList.get(bidInfoList.size() - 1);
        assertThat(testBidInfo.getBidPrjId()).isEqualTo(DEFAULT_BID_PRJ_ID);
        assertThat(testBidInfo.getBidPrjName()).isEqualTo(DEFAULT_BID_PRJ_NAME);
        assertThat(testBidInfo.getBidSectionId()).isEqualTo(DEFAULT_BID_SECTION_ID);
        assertThat(testBidInfo.getBidSection()).isEqualTo(DEFAULT_BID_SECTION);
        assertThat(testBidInfo.getBidder()).isEqualTo(DEFAULT_BIDDER);
        assertThat(testBidInfo.getBidPrice()).isEqualByComparingTo(DEFAULT_BID_PRICE);
        assertThat(testBidInfo.getAverageValue()).isEqualByComparingTo(DEFAULT_AVERAGE_VALUE);
        assertThat(testBidInfo.getDeclineRatio()).isEqualByComparingTo(DEFAULT_DECLINE_RATIO);
        assertThat(testBidInfo.getBasePrice()).isEqualByComparingTo(DEFAULT_BASE_PRICE);
        assertThat(testBidInfo.getBenchmarkScore()).isEqualByComparingTo(DEFAULT_BENCHMARK_SCORE);
    }

    @Test
    @Transactional
    void createBidInfoWithExistingId() throws Exception {
        // Create the BidInfo with an existing ID
        bidInfo.setId(1L);

        int databaseSizeBeforeCreate = bidInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBidInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bidInfo)))
            .andExpect(status().isBadRequest());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBidInfos() throws Exception {
        // Initialize the database
        bidInfoRepository.saveAndFlush(bidInfo);

        // Get all the bidInfoList
        restBidInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bidInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].bidPrjId").value(hasItem(DEFAULT_BID_PRJ_ID)))
            .andExpect(jsonPath("$.[*].bidPrjName").value(hasItem(DEFAULT_BID_PRJ_NAME)))
            .andExpect(jsonPath("$.[*].bidSectionId").value(hasItem(DEFAULT_BID_SECTION_ID)))
            .andExpect(jsonPath("$.[*].bidSection").value(hasItem(DEFAULT_BID_SECTION)))
            .andExpect(jsonPath("$.[*].bidder").value(hasItem(DEFAULT_BIDDER)))
            .andExpect(jsonPath("$.[*].bidPrice").value(hasItem(sameNumber(DEFAULT_BID_PRICE))))
            .andExpect(jsonPath("$.[*].averageValue").value(hasItem(sameNumber(DEFAULT_AVERAGE_VALUE))))
            .andExpect(jsonPath("$.[*].declineRatio").value(hasItem(sameNumber(BigDecimal.valueOf(DEFAULT_DECLINE_RATIO)))))
            .andExpect(jsonPath("$.[*].basePrice").value(hasItem(sameNumber(DEFAULT_BASE_PRICE))))
            .andExpect(jsonPath("$.[*].benchmarkScore").value(hasItem(sameNumber(BigDecimal.valueOf(DEFAULT_BENCHMARK_SCORE)))));
    }

    @Test
    @Transactional
    void getBidInfo() throws Exception {
        // Initialize the database
        bidInfoRepository.saveAndFlush(bidInfo);

        // Get the bidInfo
        restBidInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, bidInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bidInfo.getId().intValue()))
            .andExpect(jsonPath("$.bidPrjId").value(DEFAULT_BID_PRJ_ID))
            .andExpect(jsonPath("$.bidPrjName").value(DEFAULT_BID_PRJ_NAME))
            .andExpect(jsonPath("$.bidSectionId").value(DEFAULT_BID_SECTION_ID))
            .andExpect(jsonPath("$.bidSection").value(DEFAULT_BID_SECTION))
            .andExpect(jsonPath("$.bidder").value(DEFAULT_BIDDER))
            .andExpect(jsonPath("$.bidPrice").value(sameNumber(DEFAULT_BID_PRICE)))
            .andExpect(jsonPath("$.averageValue").value(sameNumber(DEFAULT_AVERAGE_VALUE)))
            .andExpect(jsonPath("$.declineRatio").value(equalTo(DEFAULT_DECLINE_RATIO)))
            .andExpect(jsonPath("$.basePrice").value(sameNumber(DEFAULT_BASE_PRICE)))
            .andExpect(jsonPath("$.benchmarkScore").value(equalTo(DEFAULT_BENCHMARK_SCORE)));
    }

    @Test
    @Transactional
    void getNonExistingBidInfo() throws Exception {
        // Get the bidInfo
        restBidInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBidInfo() throws Exception {
        // Initialize the database
        bidInfoRepository.saveAndFlush(bidInfo);

        int databaseSizeBeforeUpdate = bidInfoRepository.findAll().size();

        // Update the bidInfo
        BidInfo updatedBidInfo = bidInfoRepository.findById(bidInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBidInfo are not directly saved in db
        em.detach(updatedBidInfo);
        updatedBidInfo
            .bidPrjId(UPDATED_BID_PRJ_ID)
            .bidPrjName(UPDATED_BID_PRJ_NAME)
            .bidSectionId(UPDATED_BID_SECTION_ID)
            .bidSection(UPDATED_BID_SECTION)
            .bidder(UPDATED_BIDDER)
            .bidPrice(UPDATED_BID_PRICE)
            .averageValue(UPDATED_AVERAGE_VALUE)
            .declineRatio(UPDATED_DECLINE_RATIO)
            .basePrice(UPDATED_BASE_PRICE)
            .benchmarkScore(UPDATED_BENCHMARK_SCORE);

        restBidInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBidInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBidInfo))
            )
            .andExpect(status().isOk());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeUpdate);
        BidInfo testBidInfo = bidInfoList.get(bidInfoList.size() - 1);
        assertThat(testBidInfo.getBidPrjId()).isEqualTo(UPDATED_BID_PRJ_ID);
        assertThat(testBidInfo.getBidPrjName()).isEqualTo(UPDATED_BID_PRJ_NAME);
        assertThat(testBidInfo.getBidSectionId()).isEqualTo(UPDATED_BID_SECTION_ID);
        assertThat(testBidInfo.getBidSection()).isEqualTo(UPDATED_BID_SECTION);
        assertThat(testBidInfo.getBidder()).isEqualTo(UPDATED_BIDDER);
        assertThat(testBidInfo.getBidPrice()).isEqualByComparingTo(UPDATED_BID_PRICE);
        assertThat(testBidInfo.getAverageValue()).isEqualByComparingTo(UPDATED_AVERAGE_VALUE);
        assertThat(testBidInfo.getDeclineRatio()).isEqualByComparingTo(UPDATED_DECLINE_RATIO);
        assertThat(testBidInfo.getBasePrice()).isEqualByComparingTo(UPDATED_BASE_PRICE);
        assertThat(testBidInfo.getBenchmarkScore()).isEqualByComparingTo(UPDATED_BENCHMARK_SCORE);
    }

    @Test
    @Transactional
    void putNonExistingBidInfo() throws Exception {
        int databaseSizeBeforeUpdate = bidInfoRepository.findAll().size();
        bidInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBidInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bidInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bidInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBidInfo() throws Exception {
        int databaseSizeBeforeUpdate = bidInfoRepository.findAll().size();
        bidInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBidInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bidInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBidInfo() throws Exception {
        int databaseSizeBeforeUpdate = bidInfoRepository.findAll().size();
        bidInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBidInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bidInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBidInfoWithPatch() throws Exception {
        // Initialize the database
        bidInfoRepository.saveAndFlush(bidInfo);

        int databaseSizeBeforeUpdate = bidInfoRepository.findAll().size();

        // Update the bidInfo using partial update
        BidInfo partialUpdatedBidInfo = new BidInfo();
        partialUpdatedBidInfo.setId(bidInfo.getId());

        partialUpdatedBidInfo.bidSection(UPDATED_BID_SECTION).bidPrice(UPDATED_BID_PRICE);

        restBidInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBidInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBidInfo))
            )
            .andExpect(status().isOk());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeUpdate);
        BidInfo testBidInfo = bidInfoList.get(bidInfoList.size() - 1);
        assertThat(testBidInfo.getBidPrjId()).isEqualTo(DEFAULT_BID_PRJ_ID);
        assertThat(testBidInfo.getBidPrjName()).isEqualTo(DEFAULT_BID_PRJ_NAME);
        assertThat(testBidInfo.getBidSectionId()).isEqualTo(DEFAULT_BID_SECTION_ID);
        assertThat(testBidInfo.getBidSection()).isEqualTo(UPDATED_BID_SECTION);
        assertThat(testBidInfo.getBidder()).isEqualTo(DEFAULT_BIDDER);
        assertThat(testBidInfo.getBidPrice()).isEqualByComparingTo(UPDATED_BID_PRICE);
        assertThat(testBidInfo.getAverageValue()).isEqualByComparingTo(DEFAULT_AVERAGE_VALUE);
        assertThat(testBidInfo.getDeclineRatio()).isEqualByComparingTo(DEFAULT_DECLINE_RATIO);
        assertThat(testBidInfo.getBasePrice()).isEqualByComparingTo(DEFAULT_BASE_PRICE);
        assertThat(testBidInfo.getBenchmarkScore()).isEqualByComparingTo(DEFAULT_BENCHMARK_SCORE);
    }

    @Test
    @Transactional
    void fullUpdateBidInfoWithPatch() throws Exception {
        // Initialize the database
        bidInfoRepository.saveAndFlush(bidInfo);

        int databaseSizeBeforeUpdate = bidInfoRepository.findAll().size();

        // Update the bidInfo using partial update
        BidInfo partialUpdatedBidInfo = new BidInfo();
        partialUpdatedBidInfo.setId(bidInfo.getId());

        partialUpdatedBidInfo
            .bidPrjId(UPDATED_BID_PRJ_ID)
            .bidPrjName(UPDATED_BID_PRJ_NAME)
            .bidSectionId(UPDATED_BID_SECTION_ID)
            .bidSection(UPDATED_BID_SECTION)
            .bidder(UPDATED_BIDDER)
            .bidPrice(UPDATED_BID_PRICE)
            .averageValue(UPDATED_AVERAGE_VALUE)
            .declineRatio(UPDATED_DECLINE_RATIO)
            .basePrice(UPDATED_BASE_PRICE)
            .benchmarkScore(UPDATED_BENCHMARK_SCORE);

        restBidInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBidInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBidInfo))
            )
            .andExpect(status().isOk());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeUpdate);
        BidInfo testBidInfo = bidInfoList.get(bidInfoList.size() - 1);
        assertThat(testBidInfo.getBidPrjId()).isEqualTo(UPDATED_BID_PRJ_ID);
        assertThat(testBidInfo.getBidPrjName()).isEqualTo(UPDATED_BID_PRJ_NAME);
        assertThat(testBidInfo.getBidSectionId()).isEqualTo(UPDATED_BID_SECTION_ID);
        assertThat(testBidInfo.getBidSection()).isEqualTo(UPDATED_BID_SECTION);
        assertThat(testBidInfo.getBidder()).isEqualTo(UPDATED_BIDDER);
        assertThat(testBidInfo.getBidPrice()).isEqualByComparingTo(UPDATED_BID_PRICE);
        assertThat(testBidInfo.getAverageValue()).isEqualByComparingTo(UPDATED_AVERAGE_VALUE);
        assertThat(testBidInfo.getDeclineRatio()).isEqualByComparingTo(UPDATED_DECLINE_RATIO);
        assertThat(testBidInfo.getBasePrice()).isEqualByComparingTo(UPDATED_BASE_PRICE);
        assertThat(testBidInfo.getBenchmarkScore()).isEqualByComparingTo(UPDATED_BENCHMARK_SCORE);
    }

    @Test
    @Transactional
    void patchNonExistingBidInfo() throws Exception {
        int databaseSizeBeforeUpdate = bidInfoRepository.findAll().size();
        bidInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBidInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bidInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bidInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBidInfo() throws Exception {
        int databaseSizeBeforeUpdate = bidInfoRepository.findAll().size();
        bidInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBidInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bidInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBidInfo() throws Exception {
        int databaseSizeBeforeUpdate = bidInfoRepository.findAll().size();
        bidInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBidInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bidInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BidInfo in the database
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBidInfo() throws Exception {
        // Initialize the database
        bidInfoRepository.saveAndFlush(bidInfo);

        int databaseSizeBeforeDelete = bidInfoRepository.findAll().size();

        // Delete the bidInfo
        restBidInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, bidInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BidInfo> bidInfoList = bidInfoRepository.findAll();
        assertThat(bidInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
