package com.le.jr.am.profit.domain.enums;

public enum HoldApartAccrualStatus {
	/** 计息状态--可以 */
	YES("yes","计息状态可以"),
	
	/** 计息状态--不可以 */
	
	NO("no","计息状态不可以");
	
	 public String value;
	 public String name;

	 HoldApartAccrualStatus(String value,String name) {
	        this.value = value;
	        this.name=name;
	    }

	    public static HoldApartAccrualStatus getInstance(String projectType) {
	    	HoldApartAccrualStatus[] allStatus = HoldApartAccrualStatus.values();
	        for (HoldApartAccrualStatus ws : allStatus) {
	            if (ws.value.equals(projectType)) {
	                return ws;
	            }
	        }
	        return null;
	    }

}
