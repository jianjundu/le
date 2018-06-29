package com.le.jr.am.profit.domain.output;

import java.io.Serializable;

public class LevelCacheVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String investorOid;
	
	private String productOid;
	
	private String scaleTime;

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

	public String getScaleTime() {
		return scaleTime;
	}

	public void setScaleTime(String scaleTime) {
		this.scaleTime = scaleTime;
	}
	
	
	
	
}
