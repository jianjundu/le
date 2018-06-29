package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.util.Date;

public class HoldDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 产品id
	 */
	private String productOid;
	
	/**
	 * 订单id
	 */
	private String tradeOrderOid;
	
	/**
	 * 奖励id
	 */
	private String rewardRuleOid;
	
	/**
	 * 当前市值
	 */
	private long value;
	
	/**
	 * 当前金额(和当前市值相同)
	 */
	private long investVolume;
	
	/**
	 * 可赎回金额(不是持有金额)
	 */
	private long holdVolume;
	
	/**
	 * 计息金额
	 */
	private long snapshotVolume;
	
	/**
	 * 赎回状态（yes:可赎回，no:不可赎回）
	 */
	private String redeemStatus;
	
	/**
	 * 总收益
	 */
	private long holdTotalIncome;



	/**
	 * 总营销
	 */
	private long totalMarketingIncome;
	
	/**
	 * 昨日收益
	 */
	private long holdYesterdayIncome;



	/**
	 * 昨日营销收益
	 */
	private long yesterdayMarketingIncome;
	
	/**
	 * 持有状态( @see com.le.jr.am.profit.domain.enums.HoldApartHoldStatus)
	 */
	private String holdStatus;
	
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	/**
	 * 份额确认时间
	 */
	private Date volumeConfirmTime;
	
	/**
	 * 收益确认时间
	 */
	private Date confirmDate;


	/**
	 * 可以赎回时间
	 */
	private Date beginRedeemDate;

	/**
	 * 开始计息时间
	 */
	private Date beginAccuralDate;

	public Date getBeginRedeemDate() {
		return beginRedeemDate;
	}

	public void setBeginRedeemDate(Date beginRedeemDate) {
		this.beginRedeemDate = beginRedeemDate;
	}

	public Date getBeginAccuralDate() {
		return beginAccuralDate;
	}

	public void setBeginAccuralDate(Date beginAccuralDate) {
		this.beginAccuralDate = beginAccuralDate;
	}

	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	public String getTradeOrderOid() {
		return tradeOrderOid;
	}
	public void setTradeOrderOid(String tradeOrderOid) {
		this.tradeOrderOid = tradeOrderOid;
	}
	public String getRewardRuleOid() {
		return rewardRuleOid;
	}
	public void setRewardRuleOid(String rewardRuleOid) {
		this.rewardRuleOid = rewardRuleOid;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public long getInvestVolume() {
		return investVolume;
	}
	public void setInvestVolume(long investVolume) {
		this.investVolume = investVolume;
	}
	public long getHoldVolume() {
		return holdVolume;
	}
	public void setHoldVolume(long holdVolume) {
		this.holdVolume = holdVolume;
	}
	public String getRedeemStatus() {
		return redeemStatus;
	}
	public void setRedeemStatus(String redeemStatus) {
		this.redeemStatus = redeemStatus;
	}
	public long getHoldTotalIncome() {
		return holdTotalIncome;
	}
	public void setHoldTotalIncome(long holdTotalIncome) {
		this.holdTotalIncome = holdTotalIncome;
	}
	public long getHoldYesterdayIncome() {
		return holdYesterdayIncome;
	}
	public void setHoldYesterdayIncome(long holdYesterdayIncome) {
		this.holdYesterdayIncome = holdYesterdayIncome;
	}
	public String getHoldStatus() {
		return holdStatus;
	}
	public void setHoldStatus(String holdStatus) {
		this.holdStatus = holdStatus;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getVolumeConfirmTime() {
		return volumeConfirmTime;
	}
	public void setVolumeConfirmTime(Date volumeConfirmTime) {
		this.volumeConfirmTime = volumeConfirmTime;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public long getSnapshotVolume() {
		return snapshotVolume;
	}
	public void setSnapshotVolume(long snapshotVolume) {
		this.snapshotVolume = snapshotVolume;
	}


	public long getTotalMarketingIncome() {
		return totalMarketingIncome;
	}

	public void setTotalMarketingIncome(long totalMarketingIncome) {
		this.totalMarketingIncome = totalMarketingIncome;
	}

	public long getYesterdayMarketingIncome() {
		return yesterdayMarketingIncome;
	}

	public void setYesterdayMarketingIncome(long yesterdayMarketingIncome) {
		this.yesterdayMarketingIncome = yesterdayMarketingIncome;
	}
}
