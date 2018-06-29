package com.le.jr.am.profit.domain.input;

import java.io.Serializable;
import java.util.Date;

import com.le.jr.am.profit.domain.enums.IncomeEventStausEnum;

public class SearchAllocateVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productOid;
	
	private String assetPoolOid;
	
	private Date baseDate;
	
	private IncomeEventStausEnum incomeEventStausEnum;
	
	private IncomeEventStausEnum noStatus;
	
	
	  /**
     * 当前页
     * */
    private int currentPageNo;
    /**
     * 每页大小
     * */
    private int pageSize;
    
    
    
    
    

	public IncomeEventStausEnum getIncomeEventStausEnum() {
		return incomeEventStausEnum;
	}

	public void setIncomeEventStausEnum(IncomeEventStausEnum incomeEventStausEnum) {
		this.incomeEventStausEnum = incomeEventStausEnum;
	}

	public IncomeEventStausEnum getNoStatus() {
		return noStatus;
	}

	public void setNoStatus(IncomeEventStausEnum noStatus) {
		this.noStatus = noStatus;
	}

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public Date getBaseDate() {
		return baseDate;
	}

	public void setBaseDate(Date baseDate) {
		this.baseDate = baseDate;
	}

	public String getAssetPoolOid() {
		return assetPoolOid;
	}

	public void setAssetPoolOid(String assetPoolOid) {
		this.assetPoolOid = assetPoolOid;
	}
	
	
	
	

	

}
