package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品订单收益详情返回对象
 * 
 * @author yinxiao
 *
 */

public class ProductOrderIncomeRespVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5442437969383663944L;

	/**
	 * 订单编号
	 */
	private String orderCode;
	
	/**
	 * 订单金额
	 */
	private BigDecimal orderVolume;
	
	/**
	 * 当前持有
	 */
	private BigDecimal holdVolume;
	
	/**
	 * 产品编号
	 */
	private String productOid;
	
	/**
	 * 计息金额
	 */
	private BigDecimal snapshotVolume; 
	
	/**
	 * 计提收益
	 */
	private BigDecimal provisionInterestVolume;
	
	/**
	 * 结算收益
	 */
	private BigDecimal closeInterestVolume;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public BigDecimal getOrderVolume() {
		return orderVolume;
	}

	public void setOrderVolume(BigDecimal orderVolume) {
		this.orderVolume = orderVolume;
	}

	public BigDecimal getHoldVolume() {
		return holdVolume;
	}

	public void setHoldVolume(BigDecimal holdVolume) {
		this.holdVolume = holdVolume;
	}

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public BigDecimal getSnapshotVolume() {
		return snapshotVolume;
	}

	public void setSnapshotVolume(BigDecimal snapshotVolume) {
		this.snapshotVolume = snapshotVolume;
	}

	public BigDecimal getProvisionInterestVolume() {
		return provisionInterestVolume;
	}

	public void setProvisionInterestVolume(BigDecimal provisionInterestVolume) {
		this.provisionInterestVolume = provisionInterestVolume;
	}

	public BigDecimal getCloseInterestVolume() {
		return closeInterestVolume;
	}

	public void setCloseInterestVolume(BigDecimal closeInterestVolume) {
		this.closeInterestVolume = closeInterestVolume;
	}
}
