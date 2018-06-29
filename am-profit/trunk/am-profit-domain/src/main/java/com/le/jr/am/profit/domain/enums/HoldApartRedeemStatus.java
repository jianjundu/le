package com.le.jr.am.profit.domain.enums;

public enum HoldApartRedeemStatus {
	
	/** 变动方向--进 */
	
	
	
	
	
	YES("yes"),
	
	/** 变动方向--出 */
	
	NO("no");
	
	 public String value;

	 HoldApartRedeemStatus(String value) {
	        this.value = value;
	    }

	    public static HoldApartRedeemStatus getInstance(String projectType) {
	    	HoldApartRedeemStatus[] allStatus = HoldApartRedeemStatus.values();
	        for (HoldApartRedeemStatus ws : allStatus) {
	            if (ws.value.equals(projectType)) {
	                return ws;
	            }
	        }
	        return null;
	    }

}
