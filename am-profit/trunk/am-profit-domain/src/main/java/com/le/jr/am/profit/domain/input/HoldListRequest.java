package com.le.jr.am.profit.domain.input;


public class HoldListRequest extends ApiRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4297239758186023505L;
	String productOid;
	int offset;
	int limit;
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	
}
