package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.util.Date;

public class HoldOutput implements Serializable{

	private static final long serialVersionUID = 1L;
	String investorOid;
	String productOid;
	Long value;
	Long redeemableHoldVolume;
	Long totalHoldVolume;
	Long investTotalVolume;
	Long investTotalIncome;
	Long holdYesterdayIncome;
	Date updateTime;
	Date confirmDate;

	/**
	 * 总营销收益
	 */
	Long totalMarketingIncome;

	/**
	 * 昨日营销收益
	 */
	Long yesterdayMarketingIncome;

	public Long getTotalMarketingIncome() {
		return totalMarketingIncome;
	}

	public void setTotalMarketingIncome(Long totalMarketingIncome) {
		this.totalMarketingIncome = totalMarketingIncome;
	}

	public Long getYesterdayMarketingIncome() {
		return yesterdayMarketingIncome;
	}

	public void setYesterdayMarketingIncome(Long yesterdayMarketingIncome) {
		this.yesterdayMarketingIncome = yesterdayMarketingIncome;
	}

	public String getInvestorOid() {
		return investorOid;
	}
	public void setInvestorOid(String investorOid) {
		this.investorOid = investorOid;
	}
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	public Long getRedeemableHoldVolume() {
		return redeemableHoldVolume;
	}
	public void setRedeemableHoldVolume(Long redeemableHoldVolume) {
		this.redeemableHoldVolume = redeemableHoldVolume;
	}
	public Long getInvestTotalVolume() {
		return investTotalVolume;
	}
	public void setInvestTotalVolume(Long investTotalVolume) {
		this.investTotalVolume = investTotalVolume;
	}
	public Long getInvestTotalIncome() {
		return investTotalIncome;
	}
	public void setInvestTotalIncome(Long investTotalIncome) {
		this.investTotalIncome = investTotalIncome;
	}
	public Long getHoldYesterdayIncome() {
		return holdYesterdayIncome;
	}
	public void setHoldYesterdayIncome(Long holdYesterdayIncome) {
		this.holdYesterdayIncome = holdYesterdayIncome;
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
	public Long getTotalHoldVolume() {
		return totalHoldVolume;
	}
	public void setTotalHoldVolume(Long totalHoldVolume) {
		this.totalHoldVolume = totalHoldVolume;
	}





}
