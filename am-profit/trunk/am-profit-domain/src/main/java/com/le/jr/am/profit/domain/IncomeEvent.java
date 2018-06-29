package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 收益分配事件
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:08:42
 *
 */
public class IncomeEvent implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String assetPoolOid;
    
    private String productOid;

    private Date baseDate;

    private BigDecimal allocateIncome = BigDecimal.ZERO;

    private String creator;

    private Date createTime;

    private String auditor;

    private Date auditTime;

    private Integer days;

    private String status;
    
    

    public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getAssetPoolOid() {
        return assetPoolOid;
    }

    public void setAssetPoolOid(String assetPoolOid) {
        this.assetPoolOid = assetPoolOid == null ? null : assetPoolOid.trim();
    }

    public Date getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
    }

    public BigDecimal getAllocateIncome() {
        return allocateIncome;
    }

    public void setAllocateIncome(BigDecimal allocateIncome) {
        this.allocateIncome = allocateIncome;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor == null ? null : auditor.trim();
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}