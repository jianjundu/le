package com.le.jr.am.profit.domain;

import java.math.BigDecimal;
import java.util.Date;

public class LeZiXuanOrderInfo {
    private String oid;

    private String productOid;

    private String rewardOid;

    private Date investDate;

    private BigDecimal investVolume;

    private Date expireDate;

    private Integer period;

    private BigDecimal expireInvestVolume;

    private BigDecimal expireIncomeVolume;

    private BigDecimal expireTotalVolume;

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

    public String getRewardOid() {
        return rewardOid;
    }

    public void setRewardOid(String rewardOid) {
        this.rewardOid = rewardOid == null ? null : rewardOid.trim();
    }

    public Date getInvestDate() {
        return investDate;
    }

    public void setInvestDate(Date investDate) {
        this.investDate = investDate;
    }

    public BigDecimal getInvestVolume() {
        return investVolume;
    }

    public void setInvestVolume(BigDecimal investVolume) {
        this.investVolume = investVolume;
    }

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public BigDecimal getExpireInvestVolume() {
		return expireInvestVolume;
	}

	public void setExpireInvestVolume(BigDecimal expireInvestVolume) {
		this.expireInvestVolume = expireInvestVolume;
	}

	public BigDecimal getExpireIncomeVolume() {
		return expireIncomeVolume;
	}

	public void setExpireIncomeVolume(BigDecimal expireIncomeVolume) {
		this.expireIncomeVolume = expireIncomeVolume;
	}

	public BigDecimal getExpireTotalVolume() {
		return expireTotalVolume;
	}

	public void setExpireTotalVolume(BigDecimal expireTotalVolume) {
		this.expireTotalVolume = expireTotalVolume;
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