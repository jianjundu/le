package com.le.jr.am.profit.domain.enums;

public enum HoldAccountTypeEnum {
	
	/** 类型--发行人  */
	SPV("SPV"),
	
	/** 类型--持有人 */
	INVESTOR("INVESTOR");



    public String value;

    HoldAccountTypeEnum(String value) {
        this.value = value;
    }

    public static HoldAccountTypeEnum getInstance(String projectType) {
    	HoldAccountTypeEnum[] allStatus = HoldAccountTypeEnum.values();
        for (HoldAccountTypeEnum ws : allStatus) {
            if (ws.value.equals(projectType)) {
                return ws;
            }
        }
        return null;
    }

}
