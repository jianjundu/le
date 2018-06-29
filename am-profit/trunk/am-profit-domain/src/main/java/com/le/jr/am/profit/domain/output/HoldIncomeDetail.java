package com.le.jr.am.profit.domain.output;

import java.io.Serializable;


public class HoldIncomeDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8747257946197337103L;
	long holdIncome;
	String productOid;
	public long getHoldIncome() {
		return holdIncome;
	}
	public void setHoldIncome(long holdIncome) {
		this.holdIncome = holdIncome;
	}
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	
	
}
