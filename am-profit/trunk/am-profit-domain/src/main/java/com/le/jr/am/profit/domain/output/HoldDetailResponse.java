package com.le.jr.am.profit.domain.output;

import java.util.List;



public class HoldDetailResponse extends ApiResp{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<HoldDetail> holdDetails;
	public List<HoldDetail> getHoldDetails() {
		return holdDetails;
	}
	public void setHoldDetails(List<HoldDetail> holdDetails) {
		this.holdDetails = holdDetails;
	}
	
	
}
