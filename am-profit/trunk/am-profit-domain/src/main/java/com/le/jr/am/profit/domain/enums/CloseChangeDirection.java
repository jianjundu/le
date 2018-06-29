package com.le.jr.am.profit.domain.enums;

public enum CloseChangeDirection {
	
	/** 变动方向--进 */
	
	
	
	
	
	IN("in"),
	
	/** 变动方向--出 */
	
	OUT("out");
	
	 public String value;

	 CloseChangeDirection(String value) {
	        this.value = value;
	    }

	    public static CloseChangeDirection getInstance(String projectType) {
	    	CloseChangeDirection[] allStatus = CloseChangeDirection.values();
	        for (CloseChangeDirection ws : allStatus) {
	            if (ws.value.equals(projectType)) {
	                return ws;
	            }
	        }
	        return null;
	    }

}
