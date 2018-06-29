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
public class SearchHoldVo implements Serializable {
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
     * 持有状态
     * @see HoldStatusEnum
     * */
    private List<HoldStatusEnum> holdStatusEnums;
    /**
     * 账户类型
     * @see HoldAccountTypeEnum
     * */
    private HoldAccountTypeEnum accountTypeEnum;
    
    
    /**
	 * 乐信投资者编号
	 */
    private String investorOid;
	
	/**
	 * 产品OID(资管系统生成)
	 */
    private String productOid;
    
    private List<String> productOids;
    
    
    private String productName;
    
    private String spvOid;
    
    private String assetPoolOid;
    
    
    
    
	
	public String getAssetPoolOid() {
		return assetPoolOid;
	}
	public void setAssetPoolOid(String assetPoolOid) {
		this.assetPoolOid = assetPoolOid;
	}
	public String getSpvOid() {
		return spvOid;
	}
	public void setSpvOid(String spvOid) {
		this.spvOid = spvOid;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<String> getProductOids() {
		return productOids;
	}
	public void setProductOids(List<String> productOids) {
		this.productOids = productOids;
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
	public List<HoldStatusEnum> getHoldStatusEnums() {
		return holdStatusEnums;
	}
	public void setHoldStatusEnums(List<HoldStatusEnum> holdStatusEnums) {
		this.holdStatusEnums = holdStatusEnums;
	}
	public HoldAccountTypeEnum getAccountTypeEnum() {
		return accountTypeEnum;
	}
	public void setAccountTypeEnum(HoldAccountTypeEnum accountTypeEnum) {
		this.accountTypeEnum = accountTypeEnum;
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