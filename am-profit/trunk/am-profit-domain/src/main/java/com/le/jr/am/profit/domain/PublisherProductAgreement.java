package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.util.Date;
/**
 * T_MONEY_PUBLISHER_PRODUCT_AGREEMENT
 * 持有人-产品-协议
 *
 * @author lining6
 * @date 2016年11月17日 下午3:47:59
 *
 */
public class PublisherProductAgreement implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String productOid;

    private String orderOid;

    private String agreementCode;

    private String agreementName;

    private String agreementUrl;

    private String agreementType;

    private Date updateTime;

    private Date createTime;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getProductOid() {
        return productOid;
    }

    public void setProductOid(String productOid) {
        this.productOid = productOid == null ? null : productOid.trim();
    }

    public String getOrderOid() {
        return orderOid;
    }

    public void setOrderOid(String orderOid) {
        this.orderOid = orderOid == null ? null : orderOid.trim();
    }

    public String getAgreementCode() {
        return agreementCode;
    }

    public void setAgreementCode(String agreementCode) {
        this.agreementCode = agreementCode == null ? null : agreementCode.trim();
    }

    public String getAgreementName() {
        return agreementName;
    }

    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName == null ? null : agreementName.trim();
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl == null ? null : agreementUrl.trim();
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType == null ? null : agreementType.trim();
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