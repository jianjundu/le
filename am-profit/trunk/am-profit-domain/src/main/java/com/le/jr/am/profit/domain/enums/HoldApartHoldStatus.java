package com.le.jr.am.profit.domain.enums;

public enum HoldApartHoldStatus {
	
	TOCONFIRM("toConfirm"),
	HOLDING("holding"),
	PARTHOLDING("partHolding"),
	EXPIRED("expired"),
	CLOSED("closed"),
	ABADONED("abadoned");
	
	
	
	 public String value;

	 HoldApartHoldStatus(String value) {
	        this.value = value;
	    }

	    public static HoldApartHoldStatus getInstance(String projectType) {
	    	HoldApartHoldStatus[] allStatus = HoldApartHoldStatus.values();
	        for (HoldApartHoldStatus ws : allStatus) {
	            if (ws.value.equals(projectType)) {
	                return ws;
	            }
	        }
	        return null;
	    }

}
