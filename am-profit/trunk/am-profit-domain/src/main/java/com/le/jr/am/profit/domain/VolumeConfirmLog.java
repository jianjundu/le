package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.util.Date;

public class VolumeConfirmLog  implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String oid;

    private String orderCode;

    private Date createTime;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}