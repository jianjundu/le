package com.le.jr.am.profit.domain.input;

import java.io.Serializable;

import com.le.jr.am.profit.domain.enums.IncomeEventStausEnum;

/**
 * 业务系统查询Vo
 *
 * @author liuwenjun
 * @version 1.0
 * @date 2016-11-02
 */
public class SearchEventVo   implements Serializable {
    private static final long serialVersionUID = 8168542550476181112L;
    
    /**
     * 当前页
     * */
    private int currentPageNo;
    /**
     * 每页大小
     * */
    private int pageSize;
    
    private String assetPoolOid;
    
    private IncomeEventStausEnum incomeEventStausEnum;
    
    private IncomeEventStausEnum noEventStatus;
	
	/**
	 * 产品OID(资管系统生成)
	 */
    private String productOid;
    
    
    
    

	public IncomeEventStausEnum getNoEventStatus() {
		return noEventStatus;
	}
	public void setNoEventStatus(IncomeEventStausEnum noEventStatus) {
		this.noEventStatus = noEventStatus;
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
	public IncomeEventStausEnum getIncomeEventStausEnum() {
		return incomeEventStausEnum;
	}
	public void setIncomeEventStausEnum(IncomeEventStausEnum incomeEventStausEnum) {
		this.incomeEventStausEnum = incomeEventStausEnum;
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

   
}