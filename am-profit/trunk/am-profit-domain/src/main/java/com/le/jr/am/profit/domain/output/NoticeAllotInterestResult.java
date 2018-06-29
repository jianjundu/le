package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.util.Date;

public class NoticeAllotInterestResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productOid ;
	
	private Date allotDate;
	
	private Date notifyTime;
	
	private String notifyId;
	

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public Date getAllotDate() {
		return allotDate;
	}

	public void setAllotDate(Date allotDate) {
		this.allotDate = allotDate;
	}

	public Date getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	
	

}
