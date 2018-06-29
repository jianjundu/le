package com.le.jr.am.profit.domain.enums;

public enum AgreementTypeEnum {


	/**
	 * 投资类
	 */
	INVESTING("investing"),

	/**
	 * 服务类
	 */
	SERVICE("service");

	 public String value;

	 AgreementTypeEnum(String value) {
	        this.value = value;
	    }

	    public static AgreementTypeEnum getInstance(String projectType) {
	    	AgreementTypeEnum[] allStatus = AgreementTypeEnum.values();
	        for (AgreementTypeEnum ws : allStatus) {
	            if (ws.value.equals(projectType)) {
	                return ws;
	            }
	        }
	        return null;
	    }

}
