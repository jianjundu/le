package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 阶梯收益信息  持有人，产品，阶梯
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:09:37
 *
 */
public class LevelIncome implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String investorOid;

    private String holdOid;

    private String productOid;

    private String rewardRuleOid;

    private String holdIncomeOid;

    private BigDecimal incomeAmount = BigDecimal.ZERO;

    private BigDecimal accureVolume = BigDecimal.ZERO;

    private BigDecimal value = BigDecimal.ZERO;

    private Date confirmDate;

    private Date updateTime;

    private Date createTime;

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

    public String getRewardRuleOid() {
        return rewardRuleOid;
    }

    public void setRewardRuleOid(String rewardRuleOid) {
        this.rewardRuleOid = rewardRuleOid == null ? null : rewardRuleOid.trim();
    }

    public String getHoldIncomeOid() {
        return holdIncomeOid;
    }

    public void setHoldIncomeOid(String holdIncomeOid) {
        this.holdIncomeOid = holdIncomeOid == null ? null : holdIncomeOid.trim();
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getAccureVolume() {
        return accureVolume;
    }

    public void setAccureVolume(BigDecimal accureVolume) {
        this.accureVolume = accureVolume;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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
}