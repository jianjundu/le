package com.le.jr.am.profit.domain.input;

import java.io.Serializable;
import java.util.List;

import com.le.jr.am.profit.domain.enums.HoldAccountTypeEnum;
import com.le.jr.am.profit.domain.enums.HoldStatusEnum;

/**
 * 业务系统查询Vo
 *
 * @author liuwenjun
 * @version 1.0
 * @date 2016-11-02
 */
public class SearchHoldApartVo implements Serializable {
    private static final long serialVersionUID = 8168542550476181112L;
    
    /**
     * 当前页
     * */
    private int currentPageNo;
    /**
     * 每页大小
     * */
    private int pageSize;
    
   
    
    
    /**
	 * 乐信投资者编号
	 */
    private String investorOid;
	
	/**
	 * 产品OID(资管系统生成)
	 */
    private String productOid;
    
 
    private String holdOid;


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


	public String getHoldOid() {
		return holdOid;
	}


	public void setHoldOid(String holdOid) {
		this.holdOid = holdOid;
	}
    
    
    
	
	
   
}