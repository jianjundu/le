package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.le.jr.am.system.domain.BaseResp;


public class IncomeAllocateAssetResp extends BaseResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String assetpoolOid;
	public String investmentAssets;//投资资产（元）
	public String apUndisIncome;//资产池未分配收益（元）
	public String apReceiveIncome;//应收投资收益（元）
	
	private List<IncomeAllocateHisResp> incomeAllocateHis;//该资产池下产品最新一条历史收益分配记录
	
	private List<JSONObject> products;//该资产池下需要收益分配的产品列表

	public String getAssetpoolOid() {
		return assetpoolOid;
	}

	public void setAssetpoolOid(String assetpoolOid) {
		this.assetpoolOid = assetpoolOid;
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

	public List<IncomeAllocateHisResp> getIncomeAllocateHis() {
		return incomeAllocateHis;
	}

	public void setIncomeAllocateHis(List<IncomeAllocateHisResp> incomeAllocateHis) {
		this.incomeAllocateHis = incomeAllocateHis;
	}

	public List<JSONObject> getProducts() {
		return products;
	}

	public void setProducts(List<JSONObject> products) {
		this.products = products;
	}
	
	
	
	
}

