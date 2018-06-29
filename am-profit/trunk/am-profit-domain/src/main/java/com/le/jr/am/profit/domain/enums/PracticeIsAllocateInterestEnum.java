package com.le.jr.am.profit.domain.enums;
/**
 * 
 * //(待审核: CREATE;发放中: ALLOCATING;发放完成: ALLOCATED;发放失败: ALLOCATEFAIL;驳回: FAIL;已删除: DELETE) 
 *
 * @author lining6
 * @date 2016年11月2日 下午9:00:40
 *
 */
public enum PracticeIsAllocateInterestEnum {
	
	
	YES("yes"),
	NO("no");
	



    public String value;

    PracticeIsAllocateInterestEnum(String value) {
        this.value = value;
    }

    public static PracticeIsAllocateInterestEnum getInstance(String projectType) {
    	PracticeIsAllocateInterestEnum[] allStatus = PracticeIsAllocateInterestEnum.values();
        for (PracticeIsAllocateInterestEnum ws : allStatus) {
            if (ws.value.equals(projectType)) {
                return ws;
            }
        }
        return null;
    }

}
