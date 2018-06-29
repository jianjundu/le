package com.le.jr.am.profit.domain.output;



public class ApartIncomeResponse extends ApiResp {
	/**
	 *
	 */
	private static final long serialVersionUID = -6826180163614904614L;
	long holdIncome;
	long holdBaseIncome;
	long holdRewardIncome;
	long holdAccureVolume;

	/**
	 * 营销收益金额
	 */
	long holdMarketingIncome;

	String investorOid;
	String tradeOrderOid;
	String incomeDate;


	public long getHoldMarketingIncome() {
		return holdMarketingIncome;
	}

	public void setHoldMarketingIncome(long holdMarketingIncome) {
		this.holdMarketingIncome = holdMarketingIncome;
	}

	public long getHoldIncome() {
		return holdIncome;
	}
	public void setHoldIncome(long holdIncome) {
		this.holdIncome = holdIncome;
	}
	public long getHoldBaseIncome() {
		return holdBaseIncome;
	}
	public void setHoldBaseIncome(long holdBaseIncome) {
		this.holdBaseIncome = holdBaseIncome;
	}
	public long getHoldRewardIncome() {
		return holdRewardIncome;
	}
	public void setHoldRewardIncome(long holdRewardIncome) {
		this.holdRewardIncome = holdRewardIncome;
	}
	public long getHoldAccureVolume() {
		return holdAccureVolume;
	}
	public void setHoldAccureVolume(long holdAccureVolume) {
		this.holdAccureVolume = holdAccureVolume;
	}
	public String getInvestorOid() {
		return investorOid;
	}
	public void setInvestorOid(String investorOid) {
		this.investorOid = investorOid;
	}
	public String getTradeOrderOid() {
		return tradeOrderOid;
	}
	public void setTradeOrderOid(String tradeOrderOid) {
		this.tradeOrderOid = tradeOrderOid;
	}
	public String getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(String incomeDate) {
		this.incomeDate = incomeDate;
	}


}
