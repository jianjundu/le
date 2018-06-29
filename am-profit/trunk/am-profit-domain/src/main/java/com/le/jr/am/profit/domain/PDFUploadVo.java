package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dujianjun on 2017/3/13.
 */
public class PDFUploadVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 产品Oid
     */
    private String productOid;

    private String orderOid;

    private String spvOid;

    private String spvCompanyName;

    private String spvAddress;

    private String spvLicenceNo;

    /**
     * 预期年化收益率
     */
    private BigDecimal expAror;


    /**
     * 最低投资份额
     */
    private BigDecimal investMin;

    /**
     * 单笔投资追加份额
     */
    private BigDecimal investAdditional;


    /**
     * 产品类型，乐自选的模板是特定的
     */
    private Byte subType;


    public BigDecimal getExpAror() {
        return expAror;
    }

    public void setExpAror(BigDecimal expAror) {
        this.expAror = expAror;
    }

    public BigDecimal getInvestMin() {
        return investMin;
    }

    public void setInvestMin(BigDecimal investMin) {
        this.investMin = investMin;
    }

    public BigDecimal getInvestAdditional() {
        return investAdditional;
    }

    public void setInvestAdditional(BigDecimal investAdditional) {
        this.investAdditional = investAdditional;
    }

    public Byte getSubType() {
        return subType;
    }

    public void setSubType(Byte subType) {
        this.subType = subType;
    }

    /**
    * 信息服务协议模板链接Oid
    */
    private String productServiceFileKey;


    /**
     * 投资协议模板链接Oid
     */
    private String productInvestFileKey;


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

    public String getSpvOid() {
        return spvOid;
    }

    public void setSpvOid(String spvOid) {
        this.spvOid = spvOid;
    }

    public String getSpvCompanyName() {
        return spvCompanyName;
    }

    public void setSpvCompanyName(String spvCompanyName) {
        this.spvCompanyName = spvCompanyName;
    }

    public String getSpvAddress() {
        return spvAddress;
    }

    public void setSpvAddress(String spvAddress) {
        this.spvAddress = spvAddress;
    }

    public String getSpvLicenceNo() {
        return spvLicenceNo;
    }

    public void setSpvLicenceNo(String spvLicenceNo) {
        this.spvLicenceNo = spvLicenceNo;
    }

    public String getProductServiceFileKey() {
        return productServiceFileKey;
    }

    public void setProductServiceFileKey(String productServiceFileKey) {
        this.productServiceFileKey = productServiceFileKey;
    }

    public String getProductInvestFileKey() {
        return productInvestFileKey;
    }

    public void setProductInvestFileKey(String productInvestFileKey) {
        this.productInvestFileKey = productInvestFileKey;
    }
}
