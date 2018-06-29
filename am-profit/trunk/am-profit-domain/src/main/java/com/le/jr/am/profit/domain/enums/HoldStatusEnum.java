package com.le.jr.am.profit.domain.enums;

public enum HoldStatusEnum {
	
	/** 持仓状态--持有中 */
	HOLDING("holding","持有中"),
	
	/** 持仓状态--已到期 */
	EXPIRED("expired","已到期"),
	
	/** 持仓状态--已结算 */
	CLOSED("closed","已结算");



    public String value;
    
    public String name;

    HoldStatusEnum(String value,String name) {
        this.value = value;
        this.name = name;
    }

    public static HoldStatusEnum getInstance(String projectType) {
    	HoldStatusEnum[] allStatus = HoldStatusEnum.values();
        for (HoldStatusEnum ws : allStatus) {
            if (ws.value.equals(projectType)) {
                return ws;
            }
        }
        return null;
    }

}
