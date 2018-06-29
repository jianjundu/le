package com.le.jr.am.profit.domain.input;



public class ApartIncomeRequest extends ApiRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6826180163614904614L;
	/**
	 * 投资者ID
	 */
	String investorOid;
	/**
	 * 查询日期
	 */
	String incomeDate;
	/**
	 * 订单ID
	 */
	String tradeOrderOid;
	
	
	
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
	public String getTradeOrderOid() {
		return tradeOrderOid;
	}
	public void setTradeOrderOid(String tradeOrderOid) {
		this.tradeOrderOid = tradeOrderOid;
	}
	
	
}
