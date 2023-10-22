package com.yuzhi.tender.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.tender.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BidInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BidInfo.class);
        BidInfo bidInfo1 = new BidInfo();
        bidInfo1.setId(1L);
        BidInfo bidInfo2 = new BidInfo();
        bidInfo2.setId(bidInfo1.getId());
        assertThat(bidInfo1).isEqualTo(bidInfo2);
        bidInfo2.setId(2L);
        assertThat(bidInfo1).isNotEqualTo(bidInfo2);
        bidInfo1.setId(null);
        assertThat(bidInfo1).isNotEqualTo(bidInfo2);
    }
}
