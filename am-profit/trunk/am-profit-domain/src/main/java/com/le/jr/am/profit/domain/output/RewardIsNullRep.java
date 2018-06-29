package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;


public class RewardIsNullRep implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	BigDecimal totalHoldVolume = BigDecimal.ZERO;
	
	BigDecimal totalRewardIncome = BigDecimal.ZERO;
	
	String productOid;
	
	
	public BigDecimal getTotalHoldVolume() {
		return totalHoldVolume;
	}
	public void setTotalHoldVolume(BigDecimal totalHoldVolume) {
		this.totalHoldVolume = totalHoldVolume;
	}
	public BigDecimal getTotalRewardIncome() {
		return totalRewardIncome;
	}
	public void setTotalRewardIncome(BigDecimal totalRewardIncome) {
		this.totalRewardIncome = totalRewardIncome;
	}
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	
	
}
