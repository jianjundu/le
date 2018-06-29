package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 乐自选统计入参
 * 
 * @author yinxiao
 *
 */

public class LeZiXuanRespVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5442437969383663944L;

	/**
	 * 产品oid
	 */
	private String productOid;
	
	/**
	 * 产品名称
	 */
    private String productName;

    /**
     * 奖励阶梯名称
     */
    private String rewardName;

    /**
     * 申购日期
     */
    private Date investDate;

    /**
     * 申购金额
     */
    private BigDecimal investVolume;
    
    /**
     * 利率
     */
    private BigDecimal rate;

    /**
     * 到期日期
     */
    private Date expireDate;

    /**
     * 存续期
     */
    private int period;

    /**
     * 到期应兑付本金
     */
    private BigDecimal expireInvestVolume;

    /**
     * 到期应兑付利息
     */
    private BigDecimal expireIncomeVolume;

    /**
     * 到期应兑付本息
     */
    private BigDecimal expireTotalVolume;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getRewardName() {
		return rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
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


	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
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

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	
}
