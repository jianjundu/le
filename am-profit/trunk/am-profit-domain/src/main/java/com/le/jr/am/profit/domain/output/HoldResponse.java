package com.le.jr.am.profit.domain.output;

import java.util.List;

public class HoldResponse extends ApiResp{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<HoldOutput> holds;
	public List<HoldOutput> getHolds() {
		return holds;
	}
	public void setHolds(List<HoldOutput> holds) {
		this.holds = holds;
	}
	
	
}
