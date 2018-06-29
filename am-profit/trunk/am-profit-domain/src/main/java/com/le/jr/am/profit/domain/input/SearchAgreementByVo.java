package com.le.jr.am.profit.domain.input;

import java.io.Serializable;

/**
 * 业务系统查询Vo
 *
 * @author liuwenjun
 * @version 1.0
 * @date 2016-11-02
 */
public class SearchAgreementByVo implements Serializable {
    private static final long serialVersionUID = 8168542550476181112L;
    
    /**
     * 当前页
     * */
    private int currentPageNo;
    /**
     * 每页大小
     * */
    private int pageSize;
    
   
    private String agreementType;
    
    
    private String beginTime;//>=updateTime
    
    private String urlNotNull;//yes not null ，no can null
    
    
    private String productOid;

    private String orderOid;

    private String agreementCode;
    
    

	public String getUrlNotNull() {
		return urlNotNull;
	}

	public void setUrlNotNull(String urlNotNull) {
		this.urlNotNull = urlNotNull;
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

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public String getOrderOid() {
		return orderOid;
	}

	public void setOrderOid(String orderOid) {
		this.orderOid = orderOid;
	}

	public String getAgreementCode() {
		return agreementCode;
	}

	public void setAgreementCode(String agreementCode) {
		this.agreementCode = agreementCode;
	}
    
    
   
    
 

   
}