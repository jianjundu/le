package com.le.jr.am.profit.domain;

import java.util.Date;

public class HoldApartTypeChangeLog {
    private String oid;

    private Integer originalHoldType;

    private Integer newHoldType;

    private String holdApartOid;

    private Date createTime;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public Integer getOriginalHoldType() {
        return originalHoldType;
    }

    public void setOriginalHoldType(Integer originalHoldType) {
        this.originalHoldType = originalHoldType;
    }

    public Integer getNewHoldType() {
        return newHoldType;
    }

    public void setNewHoldType(Integer newHoldType) {
        this.newHoldType = newHoldType;
    }

    public String getHoldApartOid() {
        return holdApartOid;
    }

    public void setHoldApartOid(String holdApartOid) {
        this.holdApartOid = holdApartOid == null ? null : holdApartOid.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}