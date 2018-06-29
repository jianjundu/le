package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;

public class CalcSumInterest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal totalAmount;
	
	private String holdOid;

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getHoldOid() {
		return holdOid;
	}

	public void setHoldOid(String holdOid) {
		this.holdOid = holdOid;
	}
	
	

}
