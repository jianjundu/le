package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 收益试算表
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:11:07
 *
 */
public class RewardIncomePractice implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String productOid;

    private String rewardRuleOid;

    private BigDecimal totalHoldVolume = BigDecimal.ZERO;

    private BigDecimal totalRewardIncome = BigDecimal.ZERO;

    private Date tDate;

    private Date updateTime;

    private Date createTime;
    
    //增加字段
    private String assetPoolOid;
    //已受理未确认份额
    private BigDecimal toConfirmVolume = BigDecimal.ZERO;
    //已确认未起息份额
    private BigDecimal toInterestVolume = BigDecimal.ZERO;
    //已起息份额
    private BigDecimal interestedVolume = BigDecimal.ZERO;
    
    //是否已分配收益  yes no
    private String isAllocateInterest; 
    
    

    public String getAssetPoolOid() {
		return assetPoolOid;
	}

	public void setAssetPoolOid(String assetPoolOid) {
		this.assetPoolOid = assetPoolOid;
	}

	public BigDecimal getToConfirmVolume() {
		return toConfirmVolume;
	}

	public void setToConfirmVolume(BigDecimal toConfirmVolume) {
		this.toConfirmVolume = toConfirmVolume;
	}

	public BigDecimal getToInterestVolume() {
		return toInterestVolume;
	}

	public void setToInterestVolume(BigDecimal toInterestVolume) {
		this.toInterestVolume = toInterestVolume;
	}

	public BigDecimal getInterestedVolume() {
		return interestedVolume;
	}

	public void setInterestedVolume(BigDecimal interestedVolume) {
		this.interestedVolume = interestedVolume;
	}

	public String getIsAllocateInterest() {
		return isAllocateInterest;
	}

	public void setIsAllocateInterest(String isAllocateInterest) {
		this.isAllocateInterest = isAllocateInterest;
	}

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

    public String getRewardRuleOid() {
        return rewardRuleOid;
    }

    public void setRewardRuleOid(String rewardRuleOid) {
        this.rewardRuleOid = rewardRuleOid == null ? null : rewardRuleOid.trim();
    }

    public BigDecimal getTotalHoldVolume() {
        return totalHoldVolume;
    }

    public void setTotalHoldVolume(BigDecimal totalHoldVolume) {
        this.totalHoldVolume = totalHoldVolume;
    }

    public BigDecimal getTotalRewardIncome() {
        return totalRewardIncome;
    }

    public void setTotalRewardIncome(BigDecimal totalRewardIncome) {
        this.totalRewardIncome = totalRewardIncome;
    }

    public Date gettDate() {
        return tDate;
    }

    public void settDate(Date tDate) {
        this.tDate = tDate;
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