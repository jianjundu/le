package com.le.jr.am.profit.service;


import com.le.jr.am.profit.domain.input.InterestParams;
import com.le.jr.am.profit.domain.vo.PracticeParams;

/**
 * Created by qishang on 2016/11/29.
 */
public interface ProfitJobService {
    /**
     * 解锁赎回份额
     * @param taskOid
     * @return
     */
    public boolean unlockRedeemJob(String taskOid);
    
    /**
     * 试算
     * @param taskOid
     * @return
     */
    public boolean practiceJob(String taskOid);
    /**
     * 试算
     * @param taskOid
     * @return
     */
    public boolean practiceJob(PracticeParams vo,String taskOid);

    /**
     * 解锁计息份额
     * @param taskOid
     * @return
     */
    public boolean unlockAccrualJob(String taskOid);

    /**
     * 收益分配
     * @param params
     * @return
     */
    public boolean interestJob(InterestParams params, String taskOid);
}
