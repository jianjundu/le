package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 分仓收益表
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:07:11
 *
 */
public class HoldApartIncome implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String investorOid;

    private String holdOid;

    private String productOid;

    private String incomeOid;

    private String holdApartOid;

    private String holdIncomeOid;

    private String rewardRuleOid;

    private String levelIncomeOid;

    private String orderOid;




    /**
     * 基础收益金额
     */
    private BigDecimal incomeAmount  = BigDecimal.ZERO;

    /**
     * 奖励阶梯收益金额
     */
    private BigDecimal rewardAmount = BigDecimal.ZERO;

    /**
     * 计息份额
     */
    private BigDecimal accureVolume = BigDecimal.ZERO;

    private Date confirmDate;

    private Date updateTime;

    private Date createTime;
    
    /**
     * 投资单份额
     */
    private BigDecimal orderVolume  = BigDecimal.ZERO;

    /**
     * 当前持有份额
     */
    private BigDecimal holdVolume = BigDecimal.ZERO;

    /**
     * 计提收益
     */
    private BigDecimal provisionInterestVolume  = BigDecimal.ZERO;

    /**
     * 结算收益
     */
    private BigDecimal closeInterestVolume = BigDecimal.ZERO;


    /**
     * 营销收益金额
     */
    private BigDecimal marketingAmount = BigDecimal.ZERO;

    public BigDecimal getMarketingAmount() {
        return marketingAmount;
    }

    public void setMarketingAmount(BigDecimal marketingAmount) {
        this.marketingAmount = marketingAmount;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getInvestorOid() {
        return investorOid;
    }

    public void setInvestorOid(String investorOid) {
        this.investorOid = investorOid == null ? null : investorOid.trim();
    }

    public String getHoldOid() {
        return holdOid;
    }

    public void setHoldOid(String holdOid) {
        this.holdOid = holdOid == null ? null : holdOid.trim();
    }

    public String getProductOid() {
        return productOid;
    }

    public void setProductOid(String productOid) {
        this.productOid = productOid == null ? null : productOid.trim();
    }

    public String getIncomeOid() {
        return incomeOid;
    }

    public void setIncomeOid(String incomeOid) {
        this.incomeOid = incomeOid == null ? null : incomeOid.trim();
    }

    public String getHoldApartOid() {
        return holdApartOid;
    }

    public void setHoldApartOid(String holdApartOid) {
        this.holdApartOid = holdApartOid == null ? null : holdApartOid.trim();
    }

    public String getHoldIncomeOid() {
        return holdIncomeOid;
    }

    public void setHoldIncomeOid(String holdIncomeOid) {
        this.holdIncomeOid = holdIncomeOid == null ? null : holdIncomeOid.trim();
    }

    public String getRewardRuleOid() {
        return rewardRuleOid;
    }

    public void setRewardRuleOid(String rewardRuleOid) {
        this.rewardRuleOid = rewardRuleOid == null ? null : rewardRuleOid.trim();
    }

    public String getLevelIncomeOid() {
        return levelIncomeOid;
    }

    public void setLevelIncomeOid(String levelIncomeOid) {
        this.levelIncomeOid = levelIncomeOid == null ? null : levelIncomeOid.trim();
    }

    public String getOrderOid() {
        return orderOid;
    }

    public void setOrderOid(String orderOid) {
        this.orderOid = orderOid == null ? null : orderOid.trim();
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public BigDecimal getAccureVolume() {
        return accureVolume;
    }

    public void setAccureVolume(BigDecimal accureVolume) {
        this.accureVolume = accureVolume;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
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

	public BigDecimal getOrderVolume() {
		return orderVolume;
	}

	public void setOrderVolume(BigDecimal orderVolume) {
		this.orderVolume = orderVolume;
	}

	public BigDecimal getHoldVolume() {
		return holdVolume;
	}

	public void setHoldVolume(BigDecimal holdVolume) {
		this.holdVolume = holdVolume;
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