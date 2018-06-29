package com.le.jr.am.profit.domain.output;

import java.math.BigDecimal;
import java.util.Date;

import com.le.jr.am.profit.domain.constant.SysConstant;
import com.le.jr.am.system.domain.BaseResp;


public class PracticeInRep extends  BaseResp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 起始天数
	 */
	Integer startDate;
	/**
	 * 截止天数
	 */
	Integer endDate;

	/**
	 * 所属奖励规则
	 */
	BigDecimal rewardRatio;
	
	/**
	 * 奖励规则OID
	 */
	String rewardOid;
	
	/**
	 * 产品OID
	 */
	String productOid;

	/**
	 * 持有人总份额
	 */
	BigDecimal totalHoldVolume;
	
	/**
	 * 奖励收益
	 */
	BigDecimal totalRewardIncome;
	
	/**
	 * 已受理未确认份额
	 */
	BigDecimal toConfirmVolume = SysConstant.BIGDECIMAL_defaultValue;
	
	/**
	 * 已确认未起息份额
	 */
	BigDecimal toInterestVolume = SysConstant.BIGDECIMAL_defaultValue; 
	
	/**
	 * 已起息份额
	 */
	BigDecimal interestedVolume = SysConstant.BIGDECIMAL_defaultValue; 
	
	/**
	 * 市值
	 */
	long value;
	
	/**
	 * 累计派发收益
	 */
	BigDecimal totalIncome;
	
	/**
	 * 昨日收益
	 */
	BigDecimal yesterdayIncome;

	/**
	 * 奖励收益阶段
	 */
	String level;
	/**
	 * t日
	 */
	Date tDate;

	Date updateTime ;
	Date createTime;

	public Integer getStartDate() {
		return startDate;
	}

	public void setStartDate(Integer startDate) {
		this.startDate = startDate;
	}

	public Integer getEndDate() {
		return endDate;
	}

	public void setEndDate(Integer endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getRewardRatio() {
		return rewardRatio;
	}

	public void setRewardRatio(BigDecimal rewardRatio) {
		this.rewardRatio = rewardRatio;
	}

	public String getRewardOid() {
		return rewardOid;
	}

	public void setRewardOid(String rewardOid) {
		this.rewardOid = rewardOid;
	}

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
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

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getYesterdayIncome() {
		return yesterdayIncome;
	}

	public void setYesterdayIncome(BigDecimal yesterdayIncome) {
		this.yesterdayIncome = yesterdayIncome;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
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
