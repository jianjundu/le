package com.le.jr.am.profit.domain.output;

import java.util.List;

public class AgreementResponse extends ApiResp{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 协议列表
	 */
	List<Agreement> agreements;
	
	public List<Agreement> getAgreements() {
		return agreements;
	}
	public void setAgreements(List<Agreement> agreements) {
		this.agreements = agreements;
	}
	
	
}
