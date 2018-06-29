package com.le.jr.am.profit.domain.input;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品订单收益详情入参
 * 
 * @author yinxiao
 *
 */

public class ProductOrderIncomeReqVo implements Serializable {

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
	 * 平台订单号
	 */
	private String orderCode;
	
	/**
	 * 资管订单号
	 */
	private String orderOid;

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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderOid() {
		return orderOid;
	}

	public void setOrderOid(String orderOid) {
		this.orderOid = orderOid;
	}
}
