package com.le.jr.am.profit.domain.enums;
/**
 * 
 * //(待审核: CREATE;发放中: ALLOCATING;发放完成: ALLOCATED;发放失败: ALLOCATEFAIL;驳回: FAIL;已删除: DELETE) 
 *
 * @author lining6
 * @date 2016年11月2日 下午9:00:40
 *
 */
public enum MarketingHoldTypeEnum {


    //平台营销系统持仓
	PLATFORMMARKETING(1),

    //普通持仓
	NORMAL(0);



    public Integer value;

    MarketingHoldTypeEnum(Integer value) {
        this.value = value;
    }

    public static MarketingHoldTypeEnum getInstance(Integer values) {
    	MarketingHoldTypeEnum[] allStatus = MarketingHoldTypeEnum.values();
        for (MarketingHoldTypeEnum ws : allStatus) {
            if (ws.value.equals(values)) {
                return ws;
            }
        }
        return null;
    }

}
