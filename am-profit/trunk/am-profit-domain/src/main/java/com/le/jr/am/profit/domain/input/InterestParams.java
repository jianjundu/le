package com.le.jr.am.profit.domain.input;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class InterestParams implements Serializable{
	private static final long serialVersionUID = 1L;
	private String productOid;
	private String incomeAllocateOid; 
	private BigDecimal fpAmount;
	private BigDecimal fpRate;
	private Date incomeDate;
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	public String getIncomeAllocateOid() {
		return incomeAllocateOid;
	}
	public void setIncomeAllocateOid(String incomeAllocateOid) {
		this.incomeAllocateOid = incomeAllocateOid;
	}
	public BigDecimal getFpAmount() {
		return fpAmount;
	}
	public void setFpAmount(BigDecimal fpAmount) {
		this.fpAmount = fpAmount;
	}
	public BigDecimal getFpRate() {
		return fpRate;
	}
	public void setFpRate(BigDecimal fpRate) {
		this.fpRate = fpRate;
	}
	public Date getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(Date incomeDate) {
		this.incomeDate = incomeDate;
	}

	@Override
	public String toString() {
		return "InterestParams{" +
				"productOid='" + productOid + '\'' +
				", incomeAllocateOid='" + incomeAllocateOid + '\'' +
				", fpAmount=" + fpAmount +
				", fpRate=" + fpRate +
				", incomeDate=" + incomeDate +
				'}';
	}
}
