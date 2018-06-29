package com.le.jr.am.profit.domain.output;

import java.util.List;

public class HoldIncomeResponse extends ApiResp {
	
	private static final long serialVersionUID = -6568484058150097670L;
	
	
	long holdIncome;
	String investorOid;
	String incomeDate;
	List<HoldIncomeDetail> holdDetailList;
	
	public long getHoldIncome() {
		return holdIncome;
	}
	public void setHoldIncome(long holdIncome) {
		this.holdIncome = holdIncome;
	}
	public String getInvestorOid() {
		return investorOid;
	}
	public void setInvestorOid(String investorOid) {
		this.investorOid = investorOid;
	}
	public String getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(String incomeDate) {
		this.incomeDate = incomeDate;
	}
	public List<HoldIncomeDetail> getHoldDetailList() {
		return holdDetailList;
	}
	public void setHoldDetailList(List<HoldIncomeDetail> holdDetailList) {
		this.holdDetailList = holdDetailList;
	}
	
	
	
	
}
