package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.util.Date;

public class LevelHold implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String productOid;
	String rewardRuleOid;
	long value; //市值
	long holdVolume; //总持有份额
	long holdTotalIncome; //累计总收益
	long holdYesterdayIncome; //昨日收益(包括基础收益和奖励收益)
	long redemptionVolume; //赎回份额
	
	Date updateTime;
	Date confirmDate;
	String rewardInterest; //阶梯利率
	int beginHoldDays;
	int endHoldDays;
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
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
	public long getHoldVolume() {
		return holdVolume;
	}
	public void setHoldVolume(long holdVolume) {
		this.holdVolume = holdVolume;
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
	public long getRedemptionVolume() {
		return redemptionVolume;
	}
	public void setRedemptionVolume(long redemptionVolume) {
		this.redemptionVolume = redemptionVolume;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getRewardInterest() {
		return rewardInterest;
	}
	public void setRewardInterest(String rewardInterest) {
		this.rewardInterest = rewardInterest;
	}
	public int getBeginHoldDays() {
		return beginHoldDays;
	}
	public void setBeginHoldDays(int beginHoldDays) {
		this.beginHoldDays = beginHoldDays;
	}
	public int getEndHoldDays() {
		return endHoldDays;
	}
	public void setEndHoldDays(int endHoldDays) {
		this.endHoldDays = endHoldDays;
	}
	
	
	
}
