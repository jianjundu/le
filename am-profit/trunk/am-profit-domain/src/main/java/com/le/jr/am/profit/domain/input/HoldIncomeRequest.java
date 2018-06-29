package com.le.jr.am.profit.domain.input;



public class HoldIncomeRequest extends ApiRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6826180163614904614L;
	
	String investorOid;
	
	String incomeDate;
	
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
	
	
}
