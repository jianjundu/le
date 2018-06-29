package com.le.jr.am.profit.domain.enums;
/**
* @Description: 错误码
* @author yinxiao
* @date 2016年11月16日
* @version V1.0
//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖保佑             永无BUG
 */
public enum ErrorCodeEnum {
	
	/**
	 * 没有对应分仓
	 */
	NO_HOLDAPART(95279001,"没有对应持仓"),
	
	/**
	 * 分仓对应产品不一致
	 */
	HOLDAPART_PRODUCT_DIFF(95279002,"分仓对应产品不一致"),
	
	/**
	 * 分仓状态为不可赎回
	 */
	HOLDAPART_REDEEMSTATUS_NO(95279003,"分仓状态为不可赎回"),
	
	/**
	 * 必须全部赎回
	 */
	MUST_REDEEM_ALL(95279004,"必须全部赎回"),
	
	/**
	 * 原始投资单号为空
	 */
	ORIGINORDEROID_IS_NULL(95279005,"原始投资单号为空"),
	
	/**
	 * 赎回罚金为空
	 */
	PUNISHVOLUME_IS_NULL(95279006,"赎回罚金为空"),
	
	/**
	 * 赎回罚金不正确
	 */
	PUNISHVOLUME_IS_WRONG(95279007,"赎回罚金不正确"),
	
	/**
	 * 请选择锁定期阶梯收益
	 */
	HHHHH(00000,"####");
	
	public int value;
	
	public String desc;
	
	
	ErrorCodeEnum(int value,String desc) {
		this.value = value;
		this.desc = desc;
	}
    
    /**
     * 根据value获取ErrorCodeEnum
     */
    public static ErrorCodeEnum getInstanceByValue(int value) {
    	ErrorCodeEnum[] allStatus = ErrorCodeEnum.values();
        for (ErrorCodeEnum ele : allStatus) {
            if (ele.value==value) {
                return ele;
            }
        }
        return null;
    }
}
