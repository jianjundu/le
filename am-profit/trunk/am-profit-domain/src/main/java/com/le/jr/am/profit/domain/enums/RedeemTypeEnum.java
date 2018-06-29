package com.le.jr.am.profit.domain.enums;

public enum RedeemTypeEnum {


	/**
	 * 正常赎回单
	 */
	NOMALREDEEM("nomalRedeem"),

	/**
	 * 补单
	 */
	RESUBMITREDEEM("resubmitRedeem");

	 public String value;

	 RedeemTypeEnum(String value) {
	        this.value = value;
	    }

	    public static RedeemTypeEnum getInstance(String projectType) {
	    	RedeemTypeEnum[] allStatus = RedeemTypeEnum.values();
	        for (RedeemTypeEnum ws : allStatus) {
	            if (ws.value.equals(projectType)) {
	                return ws;
	            }
	        }
	        return null;
	    }

}
