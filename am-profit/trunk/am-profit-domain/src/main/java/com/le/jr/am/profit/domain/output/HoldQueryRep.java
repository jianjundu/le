package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


public class HoldQueryRep implements Serializable {
	
	private static final long serialVersionUID = 1L;


	private String investorOid;
	
	/**
	 * 持仓OID
	 */
	private String holdOid;
	
	/**
	 * 产品OID
	 */
	private String productOid;
	
	/**
	 * 产品编号
	 */
	private String productCode;
	/**
	 * 产品名称
	 */
	private String productName;
	/**
	 * 预期年化收益率
	 */
	private String expAror;
	/**
	 * 锁定期
	 */
	private int lockPeriod;
	/**
	 * 持仓总份额
	 */
	private BigDecimal totalHoldVolume;
	
	/**
	 * 可计息份额
	 */
	private BigDecimal accruableHoldVolume;
	/**
	 * 计息锁定份额
	 */
	private BigDecimal accrueLockHoldVolume;
	/**
	 * 可赎回份额
	 */
	private BigDecimal redeemableHoldVolume;
	/**
	 * 赎回锁定份额
	 */
	private BigDecimal lockRedeemHoldVolume;
	
	/**
	 * 最新市值
	 */
	private BigDecimal value;
	
	/**
	 * 累计收益
	 */
	private BigDecimal holdTotalIncome;
	/**
	 * 昨日收益
	 */
	private BigDecimal holdYesterdayIncome;
	
	/**
	 * 待结转收益
	 */
	private BigDecimal toConfirmIncome;
	/**
	 * 总收益
	 */
	private BigDecimal incomeAmount;
	
	/**
	 * 可赎回收益
	 */
	private BigDecimal redeemableIncome;
	/**
	 * 锁定收益
	 */
	private BigDecimal lockIncome;
	/**
	 * 预期收益
	 */
	private BigDecimal expectIncome;
	
	/**
	 * 份额确认日期
	 */
	private Date lastConfirmDate;
	
	/**
	 * 开仓时间
	 */
	private Timestamp openTime;
	
	/**
	 * 持仓状态
	 */
	private String holdStatus;
	private String holdStatusDisp;
	
	/**
	 * spv 名称
	 */
	private String spvName;
	
	/**
	 * spv oid
	 */
	private String spvOid;
	
	/**
	 * 类型
	 */
	private String accountType;
	
	/**
	 * 累计基础收益
	 */
	private BigDecimal totalBaseIncome;
	
	/**
	 * 累计奖励收益
	 */
	private BigDecimal totalRewardIncome;
	
	/**
	 * 资产池OID
	 */
	private String assetPoolOid;
	/**
	 * 资产池名称
	 */
	private String assetPoolName;
	
	
	

	public String getAssetPoolOid() {
		return assetPoolOid;
	}

	public void setAssetPoolOid(String assetPoolOid) {
		this.assetPoolOid = assetPoolOid;
	}

	public String getAssetPoolName() {
		return assetPoolName;
	}

	public void setAssetPoolName(String assetPoolName) {
		this.assetPoolName = assetPoolName;
	}

	public String getInvestorOid() {
		return investorOid;
	}

	public void setInvestorOid(String investorOid) {
		this.investorOid = investorOid;
	}

	public String getHoldOid() {
		return holdOid;
	}

	public void setHoldOid(String holdOid) {
		this.holdOid = holdOid;
	}

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getExpAror() {
		return expAror;
	}

	public void setExpAror(String expAror) {
		this.expAror = expAror;
	}

	public int getLockPeriod() {
		return lockPeriod;
	}

	public void setLockPeriod(int lockPeriod) {
		this.lockPeriod = lockPeriod;
	}

	public BigDecimal getTotalHoldVolume() {
		return totalHoldVolume;
	}

	public void setTotalHoldVolume(BigDecimal totalHoldVolume) {
		this.totalHoldVolume = totalHoldVolume;
	}

	public BigDecimal getAccruableHoldVolume() {
		return accruableHoldVolume;
	}

	public void setAccruableHoldVolume(BigDecimal accruableHoldVolume) {
		this.accruableHoldVolume = accruableHoldVolume;
	}

	public BigDecimal getAccrueLockHoldVolume() {
		return accrueLockHoldVolume;
	}

	public void setAccrueLockHoldVolume(BigDecimal accrueLockHoldVolume) {
		this.accrueLockHoldVolume = accrueLockHoldVolume;
	}

	public BigDecimal getRedeemableHoldVolume() {
		return redeemableHoldVolume;
	}

	public void setRedeemableHoldVolume(BigDecimal redeemableHoldVolume) {
		this.redeemableHoldVolume = redeemableHoldVolume;
	}

	public BigDecimal getLockRedeemHoldVolume() {
		return lockRedeemHoldVolume;
	}

	public void setLockRedeemHoldVolume(BigDecimal lockRedeemHoldVolume) {
		this.lockRedeemHoldVolume = lockRedeemHoldVolume;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getHoldTotalIncome() {
		return holdTotalIncome;
	}

	public void setHoldTotalIncome(BigDecimal holdTotalIncome) {
		this.holdTotalIncome = holdTotalIncome;
	}

	public BigDecimal getHoldYesterdayIncome() {
		return holdYesterdayIncome;
	}

	public void setHoldYesterdayIncome(BigDecimal holdYesterdayIncome) {
		this.holdYesterdayIncome = holdYesterdayIncome;
	}

	public BigDecimal getToConfirmIncome() {
		return toConfirmIncome;
	}

	public void setToConfirmIncome(BigDecimal toConfirmIncome) {
		this.toConfirmIncome = toConfirmIncome;
	}

	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	public BigDecimal getRedeemableIncome() {
		return redeemableIncome;
	}

	public void setRedeemableIncome(BigDecimal redeemableIncome) {
		this.redeemableIncome = redeemableIncome;
	}

	public BigDecimal getLockIncome() {
		return lockIncome;
	}

	public void setLockIncome(BigDecimal lockIncome) {
		this.lockIncome = lockIncome;
	}

	public BigDecimal getExpectIncome() {
		return expectIncome;
	}

	public void setExpectIncome(BigDecimal expectIncome) {
		this.expectIncome = expectIncome;
	}

	public Date getLastConfirmDate() {
		return lastConfirmDate;
	}

	public void setLastConfirmDate(Date lastConfirmDate) {
		this.lastConfirmDate = lastConfirmDate;
	}

	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

	public String getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(String holdStatus) {
		this.holdStatus = holdStatus;
	}

	public String getHoldStatusDisp() {
		return holdStatusDisp;
	}

	public void setHoldStatusDisp(String holdStatusDisp) {
		this.holdStatusDisp = holdStatusDisp;
	}

	public String getSpvName() {
		return spvName;
	}

	public void setSpvName(String spvName) {
		this.spvName = spvName;
	}

	public String getSpvOid() {
		return spvOid;
	}

	public void setSpvOid(String spvOid) {
		this.spvOid = spvOid;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getTotalBaseIncome() {
		return totalBaseIncome;
	}

	public void setTotalBaseIncome(BigDecimal totalBaseIncome) {
		this.totalBaseIncome = totalBaseIncome;
	}

	public BigDecimal getTotalRewardIncome() {
		return totalRewardIncome;
	}

	public void setTotalRewardIncome(BigDecimal totalRewardIncome) {
		this.totalRewardIncome = totalRewardIncome;
	}
	
	
}
