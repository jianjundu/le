package com.le.jr.am.profit.domain.input;

import java.io.Serializable;
import java.util.List;

import com.le.jr.am.profit.domain.enums.HoldStatusEnum;
import com.le.jr.am.profit.domain.enums.IncomeEventStausEnum;

/**
 * 业务系统查询Vo
 *
 * @author liuwenjun
 * @version 1.0
 * @date 2016-11-02
 */
public class SearchIncomeEventVo implements Serializable {
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
     * 审核状态
     * @see HoldStatusEnum
     * */
    private List<IncomeEventStausEnum> incomeEventStatusEnums;
    
    
	
	public List<IncomeEventStausEnum> getIncomeEventStatusEnums() {
		return incomeEventStatusEnums;
	}
	public void setIncomeEventStatusEnums(List<IncomeEventStausEnum> incomeEventStatusEnums) {
		this.incomeEventStatusEnums = incomeEventStatusEnums;
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