package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class IncomeAllocateHisResp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String oid;//收益分配表的oid
	private String productOid;
	private String productName;
	private BigDecimal productCapital;//产品总规模（元）
	private BigDecimal rewardIncome;//奖励收益（元）
	private BigDecimal feeValue;//计提费用（元）
	private Date baseDate; // 基准日(上一收益派发日)
	private BigDecimal ratio;//收益率(最近一次基准收益率) 0.01（代表1%）
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getProductCapital() {
		return productCapital;
	}
	public void setProductCapital(BigDecimal productCapital) {
		this.productCapital = productCapital;
	}
	public BigDecimal getRewardIncome() {
		return rewardIncome;
	}
	public void setRewardIncome(BigDecimal rewardIncome) {
		this.rewardIncome = rewardIncome;
	}
	public BigDecimal getFeeValue() {
		return feeValue;
	}
	public void setFeeValue(BigDecimal feeValue) {
		this.feeValue = feeValue;
	}
	public Date getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(Date baseDate) {
		this.baseDate = baseDate;
	}
	public BigDecimal getRatio() {
		return ratio;
	}
	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}
	
	
	
}
