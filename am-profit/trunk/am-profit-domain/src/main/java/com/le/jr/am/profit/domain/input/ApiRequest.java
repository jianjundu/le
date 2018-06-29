package com.le.jr.am.profit.domain.input;

import java.io.Serializable;

/**
 * API服务调用请求基类.
 * @author xulizhong
 *
 */
public class ApiRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String cid;
	String ckey;
	String reqId;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCkey() {
		return ckey;
	}
	public void setCkey(String ckey) {
		this.ckey = ckey;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	
	
	
}
