package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.util.Date;

public class Agreement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 产品ID
	 */
	String productOid;
	/**
	 * 订单Id
	 */
	String orderOid;
	/**
	 * 协议编号
	 */
	String agreementCode;
	/**
	 * 协议名称
	 */
	String agreementName;
	/**
	 * 协议地址
	 */
	String agreementUrl;
	/**
	 * 协议类型 
	 */
	String agreementType;
	/**
	 * 更新时间
	 */
	Date updateTime;
	/**
	 * 创建时间
	 */
	Date createTime;
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
	public String getAgreementName() {
		return agreementName;
	}
	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}
	public String getAgreementUrl() {
		return agreementUrl;
	}
	public void setAgreementUrl(String agreementUrl) {
		this.agreementUrl = agreementUrl;
	}
	public String getAgreementType() {
		return agreementType;
	}
	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
