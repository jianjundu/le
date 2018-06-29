package com.le.jr.am.profit.domain.vo;

import java.io.Serializable;

public class PracticeParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productOid;
	
	private String assetPoolOid;
	
	private String incomeDate;
	
	private String batchCode;
	

	

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public String getAssetPoolOid() {
		return assetPoolOid;
	}

	public void setAssetPoolOid(String assetPoolOid) {
		this.assetPoolOid = assetPoolOid;
	}

	public String getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(String incomeDate) {
		this.incomeDate = incomeDate;
	}
	
	

	

}
