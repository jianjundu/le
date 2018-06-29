package com.le.jr.am.profit.domain.input;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品收益详情入参
 * 
 * @author yinxiao
 *
 */

public class ProductIncomeReqVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5442437969383663944L;

	/**
	 * 产品编号
	 */
	private String productOid;
	
	/**
	 * 收益派发基准日;
	 */
	private Date incomeBaseDate;
	
	/**
	 * 发放状态 @see com.le.jr.am.profit.domain.enums.InterestResultStausEnum
	 */
	private String status;

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public Date getIncomeBaseDate() {
		return incomeBaseDate;
	}

	public void setIncomeBaseDate(Date incomeBaseDate) {
		this.incomeBaseDate = incomeBaseDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
