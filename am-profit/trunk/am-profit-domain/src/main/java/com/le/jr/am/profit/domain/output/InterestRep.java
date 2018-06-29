package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class InterestRep implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 计提收益
	 */
	BigDecimal provisionInterestAmount = BigDecimal.ZERO;
	
	/**
	 * 结算收益
	 */
	BigDecimal closeInterestAmount = BigDecimal.ZERO;
	
	/**
	 * 基础阶梯收益
	 */
	BigDecimal holdInterestBaseAmount = BigDecimal.ZERO;
	
	/**
	 * 奖励阶梯收益
	 */
	BigDecimal holdInterestRewardAmount = BigDecimal.ZERO;

	/**
	 * 营销收益
	 */
	BigDecimal holdInterestMarketingAmount = BigDecimal.ZERO;
	
	boolean result = true;
	
	Map<String, BigDecimal> rewardMap = new HashMap<String, BigDecimal>();

	public BigDecimal getHoldInterestMarketingAmount() {
		return holdInterestMarketingAmount;
	}

	public void setHoldInterestMarketingAmount(BigDecimal holdInterestMarketingAmount) {
		this.holdInterestMarketingAmount = holdInterestMarketingAmount;
	}

	public Map<String, BigDecimal> getRewardMap() {
		return rewardMap;
	}
	public void setRewardMap(Map<String, BigDecimal> rewardMap) {
		this.rewardMap = rewardMap;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public BigDecimal getHoldInterestBaseAmount() {
		return holdInterestBaseAmount;
	}
	public void setHoldInterestBaseAmount(BigDecimal holdInterestBaseAmount) {
		this.holdInterestBaseAmount = holdInterestBaseAmount;
	}
	public BigDecimal getHoldInterestRewardAmount() {
		return holdInterestRewardAmount;
	}
	public void setHoldInterestRewardAmount(BigDecimal holdInterestRewardAmount) {
		this.holdInterestRewardAmount = holdInterestRewardAmount;
	}
	public BigDecimal getProvisionInterestAmount() {
		return provisionInterestAmount;
	}
	public void setProvisionInterestAmount(BigDecimal provisionInterestAmount) {
		this.provisionInterestAmount = provisionInterestAmount;
	}
	public BigDecimal getCloseInterestAmount() {
		return closeInterestAmount;
	}
	public void setCloseInterestAmount(BigDecimal closeInterestAmount) {
		this.closeInterestAmount = closeInterestAmount;
	}
}
