package com.le.jr.am.profit.domain.enums;

public enum YesOrNoEnum {
	
	YES("yes"),
	
	NO("no");
	
	public String value;
	
	YesOrNoEnum(String value) {
		this.value = value;
    }

    public static YesOrNoEnum getInstance(String value) {
    	YesOrNoEnum[] allStatus = YesOrNoEnum.values();
        for (YesOrNoEnum y : allStatus) {
            if (y.value.equals(value)) {
                return y;
            }
        }
        return null;
    }

}
