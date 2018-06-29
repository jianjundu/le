package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 持有人手册（合仓）
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:05:57
 *
 */
public class Hold implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String investorOid;

    private String productOid;

    private String assetpoolOid;

    private String publisherOid;

    private String accountType;

    private BigDecimal totalHoldVolume = BigDecimal.ZERO;

    private BigDecimal lockRedeemHoldVolume = BigDecimal.ZERO;

    private BigDecimal redeemableHoldVolume = BigDecimal.ZERO;

    private BigDecimal accruableHoldVolume = BigDecimal.ZERO;

    private BigDecimal value = BigDecimal.ZERO;

    private BigDecimal holdTotalIncome = BigDecimal.ZERO;

    private BigDecimal totalBaseIncome  = BigDecimal.ZERO;

    private BigDecimal totalRewardIncome  = BigDecimal.ZERO;

    private BigDecimal investTotalVolume = BigDecimal.ZERO;

    private BigDecimal turnoverVolume = BigDecimal.ZERO;

    private BigDecimal holdYesterdayIncome = BigDecimal.ZERO;

    private BigDecimal yesterdayBaseIncome = BigDecimal.ZERO;

    private BigDecimal yesterdayRewardIncome = BigDecimal.ZERO;

    private BigDecimal redeemableIncome = BigDecimal.ZERO;

    private BigDecimal dayRedeemVolume = BigDecimal.ZERO;

    private BigDecimal lockIncome = BigDecimal.ZERO;

    private BigDecimal expectIncome = BigDecimal.ZERO;

    private Date lastConfirmDate;

    private BigDecimal maxHoldVolume = BigDecimal.ZERO;

    private BigDecimal totalMarketingIncome = BigDecimal.ZERO;


    private BigDecimal yesterdayMarketingIncome = BigDecimal.ZERO;



    private String holdStatus;

    private Date updateTime;

    private Date createTime;

    private String fundCode;


    public BigDecimal getTotalMarketingIncome() {
        return totalMarketingIncome;
    }

    public void setTotalMarketingIncome(BigDecimal totalMarketingIncome) {
        this.totalMarketingIncome = totalMarketingIncome;
    }

    public BigDecimal getYesterdayMarketingIncome() {
        return yesterdayMarketingIncome;
    }

    public void setYesterdayMarketingIncome(BigDecimal yesterdayMarketingIncome) {
        this.yesterdayMarketingIncome = yesterdayMarketingIncome;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getInvestorOid() {
        return investorOid;
    }

    public void setInvestorOid(String investorOid) {
        this.investorOid = investorOid == null ? null : investorOid.trim();
    }

    public String getProductOid() {
        return productOid;
    }

    public void setProductOid(String productOid) {
        this.productOid = productOid == null ? null : productOid.trim();
    }

    public String getAssetpoolOid() {
        return assetpoolOid;
    }

    public void setAssetpoolOid(String assetpoolOid) {
        this.assetpoolOid = assetpoolOid == null ? null : assetpoolOid.trim();
    }

    public String getPublisherOid() {
        return publisherOid;
    }

    public void setPublisherOid(String publisherOid) {
        this.publisherOid = publisherOid == null ? null : publisherOid.trim();
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType == null ? null : accountType.trim();
    }

    public BigDecimal getTotalHoldVolume() {
        return totalHoldVolume;
    }

    public void setTotalHoldVolume(BigDecimal totalHoldVolume) {
        this.totalHoldVolume = totalHoldVolume;
    }

    public BigDecimal getLockRedeemHoldVolume() {
        return lockRedeemHoldVolume;
    }

    public void setLockRedeemHoldVolume(BigDecimal lockRedeemHoldVolume) {
        this.lockRedeemHoldVolume = lockRedeemHoldVolume;
    }

    public BigDecimal getRedeemableHoldVolume() {
        return redeemableHoldVolume;
    }

    public void setRedeemableHoldVolume(BigDecimal redeemableHoldVolume) {
        this.redeemableHoldVolume = redeemableHoldVolume;
    }

    public BigDecimal getAccruableHoldVolume() {
        return accruableHoldVolume;
    }

    public void setAccruableHoldVolume(BigDecimal accruableHoldVolume) {
        this.accruableHoldVolume = accruableHoldVolume;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getHoldTotalIncome() {
        return holdTotalIncome;
    }

    public void setHoldTotalIncome(BigDecimal holdTotalIncome) {
        this.holdTotalIncome = holdTotalIncome;
    }

    public BigDecimal getTotalBaseIncome() {
        return totalBaseIncome;
    }

    public void setTotalBaseIncome(BigDecimal totalBaseIncome) {
        this.totalBaseIncome = totalBaseIncome;
    }

    public BigDecimal getTotalRewardIncome() {
        return totalRewardIncome;
    }

    public void setTotalRewardIncome(BigDecimal totalRewardIncome) {
        this.totalRewardIncome = totalRewardIncome;
    }

    public BigDecimal getInvestTotalVolume() {
        return investTotalVolume;
    }

    public void setInvestTotalVolume(BigDecimal investTotalVolume) {
        this.investTotalVolume = investTotalVolume;
    }

    public BigDecimal getTurnoverVolume() {
        return turnoverVolume;
    }

    public void setTurnoverVolume(BigDecimal turnoverVolume) {
        this.turnoverVolume = turnoverVolume;
    }

    public BigDecimal getHoldYesterdayIncome() {
        return holdYesterdayIncome;
    }

    public void setHoldYesterdayIncome(BigDecimal holdYesterdayIncome) {
        this.holdYesterdayIncome = holdYesterdayIncome;
    }

    public BigDecimal getYesterdayBaseIncome() {
        return yesterdayBaseIncome;
    }

    public void setYesterdayBaseIncome(BigDecimal yesterdayBaseIncome) {
        this.yesterdayBaseIncome = yesterdayBaseIncome;
    }

    public BigDecimal getYesterdayRewardIncome() {
        return yesterdayRewardIncome;
    }

    public void setYesterdayRewardIncome(BigDecimal yesterdayRewardIncome) {
        this.yesterdayRewardIncome = yesterdayRewardIncome;
    }

    public BigDecimal getRedeemableIncome() {
        return redeemableIncome;
    }

    public void setRedeemableIncome(BigDecimal redeemableIncome) {
        this.redeemableIncome = redeemableIncome;
    }

    public BigDecimal getDayRedeemVolume() {
        return dayRedeemVolume;
    }

    public void setDayRedeemVolume(BigDecimal dayRedeemVolume) {
        this.dayRedeemVolume = dayRedeemVolume;
    }

    public BigDecimal getLockIncome() {
        return lockIncome;
    }

    public void setLockIncome(BigDecimal lockIncome) {
        this.lockIncome = lockIncome;
    }

    public BigDecimal getExpectIncome() {
        return expectIncome;
    }

    public void setExpectIncome(BigDecimal expectIncome) {
        this.expectIncome = expectIncome;
    }

    public Date getLastConfirmDate() {
        return lastConfirmDate;
    }

    public void setLastConfirmDate(Date lastConfirmDate) {
        this.lastConfirmDate = lastConfirmDate;
    }

    public BigDecimal getMaxHoldVolume() {
        return maxHoldVolume;
    }

    public void setMaxHoldVolume(BigDecimal maxHoldVolume) {
        this.maxHoldVolume = maxHoldVolume;
    }

    public String getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(String holdStatus) {
        this.holdStatus = holdStatus == null ? null : holdStatus.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}