package com.yuzhi.tender.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A BidInfo.
 */
@Entity
@Table(name = "bid_info")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BidInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bid_prj_id")
    private String bidPrjId;

    @Column(name = "bid_prj_name")
    private String bidPrjName;

    @Column(name = "bid_section_id")
    private String bidSectionId;

    @Column(name = "bid_section")
    private String bidSection;

    @Column(name = "bidder")
    private String bidder;

    @Column(name = "bid_price", precision = 21, scale = 6)
    private BigDecimal bidPrice;

    @Column(name = "average_value", precision = 21, scale = 6)
    private BigDecimal averageValue;

    @Column(name = "valid_price", precision = 21, scale = 6)
    private BigDecimal validPrice;

    @Column(name = "valid_average_value", precision = 21, scale = 6)
    private BigDecimal validAverageValue;

    @Column(name = "decline_ratio")
    private Double declineRatio;

    @Column(name = "base_price", precision = 21, scale = 6)
    private BigDecimal basePrice;

    @Column(name = "benchmark_score")
    private Double benchmarkScore;

    @Column(name = "ranking")
    private Integer ranking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BidInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBidPrjId() {
        return this.bidPrjId;
    }

    public BidInfo bidPrjId(String bidPrjId) {
        this.setBidPrjId(bidPrjId);
        return this;
    }

    public void setBidPrjId(String bidPrjId) {
        this.bidPrjId = bidPrjId;
    }

    public String getBidPrjName() {
        return this.bidPrjName;
    }

    public BidInfo bidPrjName(String bidPrjName) {
        this.setBidPrjName(bidPrjName);
        return this;
    }

    public void setBidPrjName(String bidPrjName) {
        this.bidPrjName = bidPrjName;
    }

    public String getBidSectionId() {
        return this.bidSectionId;
    }

    public BidInfo bidSectionId(String bidSectionId) {
        this.setBidSectionId(bidSectionId);
        return this;
    }

    public void setBidSectionId(String bidSectionId) {
        this.bidSectionId = bidSectionId;
    }

    public String getBidSection() {
        return this.bidSection;
    }

    public BidInfo bidSection(String bidSection) {
        this.setBidSection(bidSection);
        return this;
    }

    public void setBidSection(String bidSection) {
        this.bidSection = bidSection;
    }

    public String getBidder() {
        return this.bidder;
    }

    public BidInfo bidder(String bidder) {
        this.setBidder(bidder);
        return this;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    public BigDecimal getBidPrice() {
        return this.bidPrice;
    }

    public BidInfo bidPrice(BigDecimal bidPrice) {
        this.setBidPrice(bidPrice);
        return this;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getAverageValue() {
        return this.averageValue;
    }

    public BidInfo averageValue(BigDecimal averageValue) {
        this.setAverageValue(averageValue);
        return this;
    }

    public void setAverageValue(BigDecimal averageValue) {
        this.averageValue = averageValue;
    }

    public BigDecimal getValidPrice() {
        return this.validPrice;
    }

    public BidInfo validPrice(BigDecimal validPrice) {
        this.setValidPrice(validPrice);
        return this;
    }

    public void setValidPrice(BigDecimal validPrice) {
        this.validPrice = validPrice;
    }

    public BigDecimal getValidAverageValue() {
        return this.validAverageValue;
    }

    public BidInfo validAverageValue(BigDecimal validAverageValue) {
        this.setValidAverageValue(validAverageValue);
        return this;
    }

    public void setValidAverageValue(BigDecimal validAverageValue) {
        this.validAverageValue = validAverageValue;
    }

    public Double getDeclineRatio() {
        return this.declineRatio;
    }

    public BidInfo declineRatio(Double declineRatio) {
        this.setDeclineRatio(declineRatio);
        return this;
    }

    public void setDeclineRatio(Double declineRatio) {
        this.declineRatio = declineRatio;
    }

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public BidInfo basePrice(BigDecimal basePrice) {
        this.setBasePrice(basePrice);
        return this;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Double getBenchmarkScore() {
        return this.benchmarkScore;
    }

    public BidInfo benchmarkScore(Double benchmarkScore) {
        this.setBenchmarkScore(benchmarkScore);
        return this;
    }

    public void setBenchmarkScore(Double benchmarkScore) {
        this.benchmarkScore = benchmarkScore;
    }

    public Integer getRanking() {
        return this.ranking;
    }

    public BidInfo ranking(Integer ranking) {
        this.setRanking(ranking);
        return this;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BidInfo)) {
            return false;
        }
        return getId() != null && getId().equals(((BidInfo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BidInfo{" +
            "id=" + getId() +
            ", bidPrjId='" + getBidPrjId() + "'" +
            ", bidPrjName='" + getBidPrjName() + "'" +
            ", bidSectionId='" + getBidSectionId() + "'" +
            ", bidSection='" + getBidSection() + "'" +
            ", bidder='" + getBidder() + "'" +
            ", bidPrice=" + getBidPrice() +
            ", averageValue=" + getAverageValue() +
            ", validPrice=" + getValidPrice() +
            ", validAverageValue=" + getValidAverageValue() +
            ", declineRatio=" + getDeclineRatio() +
            ", basePrice=" + getBasePrice() +
            ", benchmarkScore=" + getBenchmarkScore() +
            ", ranking=" + getRanking() +
            "}";
    }
}
