package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.util.List;

import com.le.jr.am.assetpool.domain.resultForm.SerFeeQueryRep;
import com.le.jr.am.system.domain.BaseResp;



public class IncomeAllocateCalcResp  extends BaseResp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String oid;
	public String assetpoolOid;
	public String productOid;
	public String investmentAssets;//投资资产
	public String apUndisIncome;//资产池未分配收益
	public String apReceiveIncome;//应收投资收益
	public String lastIncomeDate;//上一收益分配日
	public String incomeDate;//收益分配日
	public String productTotalScale;//产品总规模
	public String productRewardBenefit;//奖励收益
	public String productDistributionIncome;//分配收益
	public String productAnnualYield;//年化收益率
	public String undisIncome;//未分配收益
	public String receiveIncome;//应收投资收益
	public String totalScale;//产品总规模
	public String annualYield;//产品年化收益率
	public String millionCopiesIncome;//万份收益
	private String incomeCalcBasis;//收益计算基础
	private String feeValue;//计提费用
	private String feeValueStr;//计提费用
	public String investmentAssetsStr;//投资资产
	public String apUndisIncomeStr;//资产池未分配收益
	public String apReceiveIncomeStr;//应收投资收益
	public String productTotalScaleStr;//产品总规模
	public String productRewardBenefitStr;//奖励收益
	
	private List<SerFeeQueryRep> fees;//乐视服务费
	
	
	
	public List<SerFeeQueryRep> getFees() {
		return fees;
	}
	public void setFees(List<SerFeeQueryRep> fees) {
		this.fees = fees;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getAssetpoolOid() {
		return assetpoolOid;
	}
	public void setAssetpoolOid(String assetpoolOid) {
		this.assetpoolOid = assetpoolOid;
	}
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	public String getInvestmentAssets() {
		return investmentAssets;
	}
	public void setInvestmentAssets(String investmentAssets) {
		this.investmentAssets = investmentAssets;
	}
	public String getApUndisIncome() {
		return apUndisIncome;
	}
	public void setApUndisIncome(String apUndisIncome) {
		this.apUndisIncome = apUndisIncome;
	}
	public String getApReceiveIncome() {
		return apReceiveIncome;
	}
	public void setApReceiveIncome(String apReceiveIncome) {
		this.apReceiveIncome = apReceiveIncome;
	}
	public String getLastIncomeDate() {
		return lastIncomeDate;
	}
	public void setLastIncomeDate(String lastIncomeDate) {
		this.lastIncomeDate = lastIncomeDate;
	}
	public String getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(String incomeDate) {
		this.incomeDate = incomeDate;
	}
	public String getProductTotalScale() {
		return productTotalScale;
	}
	public void setProductTotalScale(String productTotalScale) {
		this.productTotalScale = productTotalScale;
	}
	public String getProductRewardBenefit() {
		return productRewardBenefit;
	}
	public void setProductRewardBenefit(String productRewardBenefit) {
		this.productRewardBenefit = productRewardBenefit;
	}
	public String getProductDistributionIncome() {
		return productDistributionIncome;
	}
	public void setProductDistributionIncome(String productDistributionIncome) {
		this.productDistributionIncome = productDistributionIncome;
	}
	public String getProductAnnualYield() {
		return productAnnualYield;
	}
	public void setProductAnnualYield(String productAnnualYield) {
		this.productAnnualYield = productAnnualYield;
	}
	public String getUndisIncome() {
		return undisIncome;
	}
	public void setUndisIncome(String undisIncome) {
		this.undisIncome = undisIncome;
	}
	public String getReceiveIncome() {
		return receiveIncome;
	}
	public void setReceiveIncome(String receiveIncome) {
		this.receiveIncome = receiveIncome;
	}
	public String getTotalScale() {
		return totalScale;
	}
	public void setTotalScale(String totalScale) {
		this.totalScale = totalScale;
	}
	public String getAnnualYield() {
		return annualYield;
	}
	public void setAnnualYield(String annualYield) {
		this.annualYield = annualYield;
	}
	public String getMillionCopiesIncome() {
		return millionCopiesIncome;
	}
	public void setMillionCopiesIncome(String millionCopiesIncome) {
		this.millionCopiesIncome = millionCopiesIncome;
	}
	public String getIncomeCalcBasis() {
		return incomeCalcBasis;
	}
	public void setIncomeCalcBasis(String incomeCalcBasis) {
		this.incomeCalcBasis = incomeCalcBasis;
	}
	public String getFeeValue() {
		return feeValue;
	}
	public void setFeeValue(String feeValue) {
		this.feeValue = feeValue;
	}
	public String getFeeValueStr() {
		return feeValueStr;
	}
	public void setFeeValueStr(String feeValueStr) {
		this.feeValueStr = feeValueStr;
	}
	public String getInvestmentAssetsStr() {
		return investmentAssetsStr;
	}
	public void setInvestmentAssetsStr(String investmentAssetsStr) {
		this.investmentAssetsStr = investmentAssetsStr;
	}
	public String getApUndisIncomeStr() {
		return apUndisIncomeStr;
	}
	public void setApUndisIncomeStr(String apUndisIncomeStr) {
		this.apUndisIncomeStr = apUndisIncomeStr;
	}
	public String getApReceiveIncomeStr() {
		return apReceiveIncomeStr;
	}
	public void setApReceiveIncomeStr(String apReceiveIncomeStr) {
		this.apReceiveIncomeStr = apReceiveIncomeStr;
	}
	public String getProductTotalScaleStr() {
		return productTotalScaleStr;
	}
	public void setProductTotalScaleStr(String productTotalScaleStr) {
		this.productTotalScaleStr = productTotalScaleStr;
	}
	public String getProductRewardBenefitStr() {
		return productRewardBenefitStr;
	}
	public void setProductRewardBenefitStr(String productRewardBenefitStr) {
		this.productRewardBenefitStr = productRewardBenefitStr;
	}
	
	//private List<SerFeeQueryRep> fees;//乐视服务费
	
	
	
	
}
