package com.le.jr.am.profit.domain.input;

import java.io.Serializable;

/**
 * 新增收益分配事件的方法参数主体
 * Date:2016年10月20日
 * 杜建君
 */

public class IncomeAllocateForm implements Serializable {

	private static final long serialVersionUID = 1539925102915297056L;
	
	
	public String assetpoolOid;
	
	public String productOid;
	
	public String incomeDistrDate;//收益分配日
	
	public String productTotalScale;//产品总规模
	
	public String productRewardBenefit;//奖励收益
	
	public String productDistributionIncome;//分配收益
	
	public String productAnnualYield;//年化收益率

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

	public String getIncomeDistrDate() {
		return incomeDistrDate;
	}

	public void setIncomeDistrDate(String incomeDistrDate) {
		this.incomeDistrDate = incomeDistrDate;
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
	
	
	

}
