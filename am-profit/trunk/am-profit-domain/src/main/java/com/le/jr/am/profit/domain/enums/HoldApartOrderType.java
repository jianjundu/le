package com.le.jr.am.profit.domain.enums;

public enum HoldApartOrderType {
	
	/** 变动方向--进 */
	
	
	
	
	
	INVEST("invest"),
	
	/** 变动方向--出 */
	
	BUY("buy");
	
	 public String value;

	 HoldApartOrderType(String value) {
	        this.value = value;
	    }

	    public static HoldApartOrderType getInstance(String projectType) {
	    	HoldApartOrderType[] allStatus = HoldApartOrderType.values();
	        for (HoldApartOrderType ws : allStatus) {
	            if (ws.value.equals(projectType)) {
	                return ws;
	            }
	        }
	        return null;
	    }

}
