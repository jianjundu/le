package com.le.jr.am.profit.interfaces;

import com.le.jr.am.profit.domain.input.InterestParams;
import com.le.jr.am.profit.domain.vo.PracticeParams;
import com.le.jr.trade.publictools.data.Message;

/**
 * Created by qishang on 2016/11/29.
 */
public interface ProfitJobInterface {
    /**
     * 解锁赎回份额
     * @param taskOid
     * @return
     */
    public Message<Boolean> unlockRedeemJob(String taskOid);
    /**
     * 试算
     * @param taskOid
     * @return
     */
    public Message<Boolean> practiceJob(String taskOid);
    
    
    public Message<Boolean> practiceJob(PracticeParams vo ,String taskOid);

    /**
     * 解锁计息份额
     * @param taskOid
     * @return
     */
    public Message<Boolean> unlockAccrualJob(String taskOid);

    /**
     * 收益分配
     * @param params
     * @return
     */
    public Message<Boolean> interestJob(InterestParams params,String taskOid);
}
