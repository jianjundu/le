package com.le.jr.am.profit.domain.enums;
/**
 * 
 * 
 *
 * @author lining6
 * @date 2016年11月2日 下午9:00:40
 *
 */
public enum InterestResultStausEnum {
	
	ALLOCATING("ALLOCATING","发放中"),
	ALLOCATED("ALLOCATED","发放成功"),
	ALLOCATEFAIL("ALLOCATEFAIL","发放失败");

    public String value;
    
    public String desc;

    InterestResultStausEnum(String value,String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static InterestResultStausEnum getInstance(String value) {
    	InterestResultStausEnum[] allStatus = InterestResultStausEnum.values();
        for (InterestResultStausEnum is : allStatus) {
            if (is.value.equals(value)) {
                return is;
            }
        }
        return null;
    }

}
