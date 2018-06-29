package com.le.jr.am.profit.service.util;

import org.slf4j.Logger;

import com.le.jr.am.task.domain.JobLock;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;

/**
 * Created by qishang on 2016/11/17.
 */
public class MessageUtils {
    public static JobLock getJobLock(Logger logger,Message<JobLock> msg,String methodName){
        JobLock result = new JobLock();
        if (msg!=null&&result!=null){
            if (!(Messages.isSuccess(msg) && msg.getData() != null)) {
                logger.error(methodName+" jobLockInterface findByBatchCodeAndJobId failed msg={}", JsonUtils.writeValue(msg));
                throw new BizException(methodName+" jobLockInterface findByBatchCodeAndJobId failed");
            } else {
                result = msg.getData();
            }
            return result;
        }
        return null;
    }
}
