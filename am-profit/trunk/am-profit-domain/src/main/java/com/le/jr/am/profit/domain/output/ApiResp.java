package com.le.jr.am.profit.domain.output;

import java.io.Serializable;



/**
 * API调用返回基类.
 * @author xulizhong
 *
 */

public class ApiResp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 返回码.
	 */
	int errorCode=0;
	
	/**
	 * 错误信息提示.
	 */
	String errorMessage;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
