package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收益分配
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:07:52
 *
 */
public class IncomeAllocate implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String eventOid;

    private String productOid;

    private Date baseDate;

    private BigDecimal capital = BigDecimal.ZERO;

    private BigDecimal allocateIncome = BigDecimal.ZERO;

    private BigDecimal rewardIncome = BigDecimal.ZERO;

    private BigDecimal ratio = BigDecimal.ZERO;

    private BigDecimal wincome = BigDecimal.ZERO;

    private Integer days;

    private BigDecimal successAllocateIncome = BigDecimal.ZERO;

    private BigDecimal successAllocateRewardIncome = BigDecimal.ZERO;

    private BigDecimal leftAllocateIncome = BigDecimal.ZERO;

    private Integer successAllocateInvestors;

    private Integer failAllocateInvestors;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getEventOid() {
        return eventOid;
    }

    public void setEventOid(String eventOid) {
        this.eventOid = eventOid == null ? null : eventOid.trim();
    }

    public String getProductOid() {
        return productOid;
    }

    public void setProductOid(String productOid) {
        this.productOid = productOid == null ? null : productOid.trim();
    }

    public Date getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getAllocateIncome() {
        return allocateIncome;
    }

    public void setAllocateIncome(BigDecimal allocateIncome) {
        this.allocateIncome = allocateIncome;
    }

    public BigDecimal getRewardIncome() {
        return rewardIncome;
    }

    public void setRewardIncome(BigDecimal rewardIncome) {
        this.rewardIncome = rewardIncome;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public BigDecimal getWincome() {
        return wincome;
    }

    public void setWincome(BigDecimal wincome) {
        this.wincome = wincome;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public BigDecimal getSuccessAllocateIncome() {
        return successAllocateIncome;
    }

    public void setSuccessAllocateIncome(BigDecimal successAllocateIncome) {
        this.successAllocateIncome = successAllocateIncome;
    }

    public BigDecimal getSuccessAllocateRewardIncome() {
        return successAllocateRewardIncome;
    }

    public void setSuccessAllocateRewardIncome(BigDecimal successAllocateRewardIncome) {
        this.successAllocateRewardIncome = successAllocateRewardIncome;
    }

    public BigDecimal getLeftAllocateIncome() {
        return leftAllocateIncome;
    }

    public void setLeftAllocateIncome(BigDecimal leftAllocateIncome) {
        this.leftAllocateIncome = leftAllocateIncome;
    }

    public Integer getSuccessAllocateInvestors() {
        return successAllocateInvestors;
    }

    public void setSuccessAllocateInvestors(Integer successAllocateInvestors) {
        this.successAllocateInvestors = successAllocateInvestors;
    }

    public Integer getFailAllocateInvestors() {
        return failAllocateInvestors;
    }

    public void setFailAllocateInvestors(Integer failAllocateInvestors) {
        this.failAllocateInvestors = failAllocateInvestors;
    }
}