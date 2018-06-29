package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 收益分配失败表
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:08:19
 *
 */
public class IncomeAllocateFail implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String allocateOid;

    private String holdOid;

    private Date failTime;

    private Date successTime;

    private Date allocateDate;

    private Date updateTime;

    private Date createTime;

    private String failComment;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getAllocateOid() {
        return allocateOid;
    }

    public void setAllocateOid(String allocateOid) {
        this.allocateOid = allocateOid == null ? null : allocateOid.trim();
    }

    public String getHoldOid() {
        return holdOid;
    }

    public void setHoldOid(String holdOid) {
        this.holdOid = holdOid == null ? null : holdOid.trim();
    }

    public Date getFailTime() {
        return failTime;
    }

    public void setFailTime(Date failTime) {
        this.failTime = failTime;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public Date getAllocateDate() {
        return allocateDate;
    }

    public void setAllocateDate(Date allocateDate) {
        this.allocateDate = allocateDate;
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

    public String getFailComment() {
        return failComment;
    }

    public void setFailComment(String failComment) {
        this.failComment = failComment == null ? null : failComment.trim();
    }
}