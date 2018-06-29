package com.le.jr.am.profit.domain.enums;
/**
 * 
 * //(待审核: CREATE;发放中: ALLOCATING;发放完成: ALLOCATED;发放失败: ALLOCATEFAIL;驳回: FAIL;已删除: DELETE) 
 *
 * @author lining6
 * @date 2016年11月2日 下午9:00:40
 *
 */
public enum IncomeEventStausEnum {
	
	
	CREATE("CREATE"),
	ALLOCATED("ALLOCATED"),
	ALLOCATEFAIL("ALLOCATEFAIL"),
	FAIL("FAIL"),
	DELETE("DELETE"),
	ALLOCATING("ALLOCATING");



    public String value;

    IncomeEventStausEnum(String value) {
        this.value = value;
    }

    public static IncomeEventStausEnum getInstance(String projectType) {
    	IncomeEventStausEnum[] allStatus = IncomeEventStausEnum.values();
        for (IncomeEventStausEnum ws : allStatus) {
            if (ws.value.equals(projectType)) {
                return ws;
            }
        }
        return null;
    }

}
