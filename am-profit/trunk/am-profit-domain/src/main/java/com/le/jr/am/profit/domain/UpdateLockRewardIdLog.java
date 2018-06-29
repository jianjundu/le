package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.util.Date;

public class UpdateLockRewardIdLog implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String oid;

    private String orderOid;

    private String lockRewardOldId;

    private String lockRewardNewId;

    private Date beginAccuralOldDate;

    private Date beginAccuralNewDate;

    private Date beginRedeemOldDate;

    private Date beginRedeemNewDate;

    private Date createTime;

    private Date updateTime;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getOrderOid() {
        return orderOid;
    }

    public void setOrderOid(String orderOid) {
        this.orderOid = orderOid == null ? null : orderOid.trim();
    }

    public String getLockRewardOldId() {
        return lockRewardOldId;
    }

    public void setLockRewardOldId(String lockRewardOldId) {
        this.lockRewardOldId = lockRewardOldId == null ? null : lockRewardOldId.trim();
    }

    public String getLockRewardNewId() {
        return lockRewardNewId;
    }

    public void setLockRewardNewId(String lockRewardNewId) {
        this.lockRewardNewId = lockRewardNewId == null ? null : lockRewardNewId.trim();
    }

    public Date getBeginAccuralOldDate() {
        return beginAccuralOldDate;
    }

    public void setBeginAccuralOldDate(Date beginAccuralOldDate) {
        this.beginAccuralOldDate = beginAccuralOldDate;
    }

    public Date getBeginAccuralNewDate() {
        return beginAccuralNewDate;
    }

    public void setBeginAccuralNewDate(Date beginAccuralNewDate) {
        this.beginAccuralNewDate = beginAccuralNewDate;
    }

    public Date getBeginRedeemOldDate() {
        return beginRedeemOldDate;
    }

    public void setBeginRedeemOldDate(Date beginRedeemOldDate) {
        this.beginRedeemOldDate = beginRedeemOldDate;
    }

    public Date getBeginRedeemNewDate() {
        return beginRedeemNewDate;
    }

    public void setBeginRedeemNewDate(Date beginRedeemNewDate) {
        this.beginRedeemNewDate = beginRedeemNewDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}