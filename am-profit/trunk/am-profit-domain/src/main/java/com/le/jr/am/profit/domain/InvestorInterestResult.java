package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 收益分配结果
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:09:21
 *
 */
public class InvestorInterestResult implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String allocateOid;

    private String productOid;

    private Date allocateDate;

    private BigDecimal successAllocateIncome = BigDecimal.ZERO;

    private BigDecimal leftAllocateIncome = BigDecimal.ZERO;

    private Integer successAllocateInvestors =0;

    private BigDecimal successAllocateRewardIncome = BigDecimal.ZERO;

    private Integer failAllocateInvestors = 0;

    private String status;

    /**
     * 备注
     */
    private String anno;

    private Date updateTime;

    private Date createTime;
    
    /**
     * 计息份额
     */
    private BigDecimal snapshotVolume = BigDecimal.ZERO;
    
    /**
     * 计提收益
     */
    private BigDecimal provisionInterestVolume = BigDecimal.ZERO;
    
    /**
     * 结算收益
     */
    private BigDecimal closeInterestVolume = BigDecimal.ZERO;

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

    public String getProductOid() {
        return productOid;
    }

    public void setProductOid(String productOid) {
        this.productOid = productOid == null ? null : productOid.trim();
    }

    public Date getAllocateDate() {
        return allocateDate;
    }

    public void setAllocateDate(Date allocateDate) {
        this.allocateDate = allocateDate;
    }

    public BigDecimal getSuccessAllocateIncome() {
        return successAllocateIncome;
    }

    public void setSuccessAllocateIncome(BigDecimal successAllocateIncome) {
        this.successAllocateIncome = successAllocateIncome;
    }

    public BigDecimal getLeftAllocateIncome() {
        return leftAllocateIncome;
    }

    public void setLeftAllocateIncome(BigDecimal leftAllocateIncome) {
        this.leftAllocateIncome = leftAllocateIncome;
    }

    public Integer getSuccessAllocateInvestors() {
        return successAllocateInvestors;
    }

    public void setSuccessAllocateInvestors(Integer successAllocateInvestors) {
        this.successAllocateInvestors = successAllocateInvestors;
    }

    public BigDecimal getSuccessAllocateRewardIncome() {
        return successAllocateRewardIncome;
    }

    public void setSuccessAllocateRewardIncome(BigDecimal successAllocateRewardIncome) {
        this.successAllocateRewardIncome = successAllocateRewardIncome;
    }

    public Integer getFailAllocateInvestors() {
        return failAllocateInvestors;
    }

    public void setFailAllocateInvestors(Integer failAllocateInvestors) {
        this.failAllocateInvestors = failAllocateInvestors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno == null ? null : anno.trim();
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