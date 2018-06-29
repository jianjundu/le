package com.le.jr.am.profit.domain.output;

import java.io.Serializable;

/**
 * 转出转入单消息vo
 * @author Administrator
 *
 */
public class OutInMqVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 订单code
	 */
	private String orderCode;
	
	/**
	 * 投资者code
	 */
	private String investorOid;
	
	/**
	 * 项目code
	 */
	private String productOid;
	
	/**
	 * 当前计息金额
	 */
	private Long interstAmout;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public Long getInterstAmout() {
		return interstAmout;
	}

	public void setInterstAmout(Long interstAmout) {
		this.interstAmout = interstAmout;
	}

}
