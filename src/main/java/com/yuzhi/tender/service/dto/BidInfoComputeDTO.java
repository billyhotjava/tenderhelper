package com.yuzhi.tender.service.dto;

public class BidInfoComputeDTO {

    private static final long serialVersionUID = 1L;

    private String sectionType;
    private double declineRatio;
    private double n1;
    private double n2;

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public double getDeclineRatio() {
        return declineRatio;
    }

    public void setDeclineRatio(double declineRatio) {
        this.declineRatio = declineRatio;
    }

    public double getN1() {
        return n1;
    }

    public void setN1(double n1) {
        this.n1 = n1;
    }

    public double getN2() {
        return n2;
    }

    public void setN2(double n2) {
        this.n2 = n2;
    }

    @Override
    public String toString() {
        return (
            "BidInfoComputeDTO{" +
            "sectionType='" +
            sectionType +
            '\'' +
            ", declineRatio='" +
            declineRatio +
            '\'' +
            ", n1='" +
            n1 +
            '\'' +
            ", n2='" +
            n2 +
            '\'' +
            '}'
        );
    }
}
