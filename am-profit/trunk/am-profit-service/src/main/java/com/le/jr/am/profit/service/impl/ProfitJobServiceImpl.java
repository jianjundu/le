package com.le.jr.am.profit.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.le.jr.am.investment.common.utils.CheckUtils;
import com.le.jr.am.order.domain.enums.TaskCodeEnum;
import com.le.jr.am.profit.domain.input.InterestParams;
import com.le.jr.am.profit.domain.vo.PracticeParams;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.am.profit.service.InterestRateMethodService;
import com.le.jr.am.profit.service.ProfitJobService;
import com.le.jr.am.profit.service.work.ProductPracticeWork;
import com.le.jr.am.task.domain.enums.SerialTaskCodeEnum;
import com.le.jr.am.task.domain.enums.SerialTaskStatusEnum;
import com.le.jr.am.task.domain.vo.SerialTaskVO;
import com.le.jr.am.task.interfaces.SerialTaskInterface;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;

/**
 * Created by qishang on 2016/11/29.
 */
@Service("profitJobService")
public class ProfitJobServiceImpl implements ProfitJobService {
    Logger logger = LoggerFactory.getLogger(ProfitJobServiceImpl.class);
    @Resource
    private HoldService holdService;
    @Resource
    private InterestRateMethodService interestRateMethodService;
    @Resource
    private SerialTaskInterface serialTaskInterface;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    
   /* @Resource
    private RewardIncomePracticeService rewardIncomePracticeService;*/
    
    
    @Resource
    private ProductPracticeWork productPracticeWork;

    /**
     * 解锁赎回份额
     *
     * @param taskOid
     * @return
     */
    @Override
    public boolean unlockRedeemJob(String taskOid) {
        logger.info("unlockRedeemJob  taskOid={}", taskOid);

        CheckUtils.checkParam(logger, taskOid);

        JobThread thread = new JobThread();
        thread.taskOid = taskOid;
        thread.taskCode = SerialTaskCodeEnum.UNLOCK_REDEEM.value;

        threadPoolTaskExecutor.execute(thread);

        return true;
    }
    
    @Override
   	public boolean practiceJob(String taskOid) {
       	logger.info("practiceJob  taskOid={}", taskOid);

           CheckUtils.checkParam(logger, taskOid);

           JobThread thread = new JobThread();
           thread.taskOid = taskOid;
           thread.taskCode = SerialTaskCodeEnum.PRACTICE.value;

           threadPoolTaskExecutor.execute(thread);

           return true;
   	}
    
    
    @Override
	public boolean practiceJob(PracticeParams params ,String taskOid) {
    	logger.info("practiceJob  taskOid={}", taskOid);

        CheckUtils.checkParam(logger, taskOid);

        JobThread thread = new JobThread();
        thread.taskOid = taskOid;
        thread.taskCode = SerialTaskCodeEnum.PRACTICE.value;
        thread.paramMap.put("PracticeParams",params);

        threadPoolTaskExecutor.execute(thread);

        return true;
	}

    /**
     * 解锁计息份额
     *
     * @param taskOid
     * @return
     */
    @Override
    public boolean unlockAccrualJob(String taskOid) {
        logger.info("unlockAccrualJob  taskOid={}", taskOid);

        CheckUtils.checkParam(logger, taskOid);

        JobThread thread = new JobThread();
        thread.taskOid = taskOid;
        thread.taskCode = SerialTaskCodeEnum.UNLOCK_ACCRUAL.value;

        threadPoolTaskExecutor.execute(thread);

        return true;
    }

    /**
     * 收益分配
     *
     * @param params
     * @return
     */
    @Override
    public boolean interestJob(InterestParams params, String taskOid) {
        logger.info("interestJob  taskOid={}", taskOid);

        CheckUtils.checkParam(logger, taskOid);

        JobThread thread = new JobThread();
        thread.taskOid = taskOid;
        thread.taskCode = TaskCodeEnum.INTEREST.value;
        thread.paramMap.put("InterestParams",params);
        threadPoolTaskExecutor.execute(thread);

        return true;
    }

    /**
     * Job运行线程
     */
    private class JobThread implements Runnable {
        private Map<String, Object> paramMap =new HashMap<>();
        private String taskOid;
        private String taskCode;

        @Override
        public void run() {
            SerialTaskVO vo = new SerialTaskVO();
            vo.setOid(taskOid);
            //将任务启动
            vo.setTaskStatus(SerialTaskStatusEnum.RUNNING.value);
            Message<SerialTaskVO> msg = serialTaskInterface.updateSerialTaskStatus(vo);
            if (!Messages.isSuccess(msg)) {
                logger.error("JobThread run  error paramMap={} taskOid={} task={}", JsonUtils.writeValue(paramMap), taskOid, JsonUtils.writeValue(vo));
                throw new BizException("JobThread run  error updateSerialTask error vo = " + JsonUtils.writeValue(vo));
            }
            //执行业务方法
            boolean flag = false;

            try {
                if (taskCode.equalsIgnoreCase(SerialTaskCodeEnum.UNLOCK_ACCRUAL.value)) {
                    //解锁计息份额
                    holdService.unlockAccrualDo();
                    flag = true;
                } else if (taskCode.equalsIgnoreCase(SerialTaskCodeEnum.UNLOCK_REDEEM.value)) {
                    //解锁赎回份额
                    holdService.unlockRedeemDo();
                    flag = true;
                } else if (taskCode.equalsIgnoreCase(SerialTaskCodeEnum.INTEREST.value)) {
                    //收益分配
                    InterestParams req = (InterestParams) paramMap.get("InterestParams");

                    interestRateMethodService.interestDo(
                            req.getProductOid(),
                            req.getIncomeAllocateOid(),
                            req.getFpAmount(),
                            req.getFpRate(),
                            req.getIncomeDate());
                    flag = true;
                }else if(taskCode.equalsIgnoreCase(SerialTaskCodeEnum.PRACTICE.value)){
                	
                	PracticeParams req = (PracticeParams) paramMap.get("PracticeParams");
                	
                	productPracticeWork.practiceOneProduct(req);
                	//rewardIncomePracticeService.practiceDo();
                	 flag = true;
                } else {
                    //未识别的任务类型
                	 
                    logger.error("undefined taskCode {}", taskCode);
                    throw new BizException("undefined taskCode = " + taskCode);
                }
            } catch (Exception e) {
                logger.error("JobThread run  error clear failed..", e);
                vo.setTaskStatus(SerialTaskStatusEnum.FAILED.value);
                vo.setTaskError(e.getMessage());
                
            }
            if (flag) {
                //任务执行成功
                vo.setTaskStatus(SerialTaskStatusEnum.DONE.value);
            } else {
                vo.setTaskStatus(SerialTaskStatusEnum.FAILED.value);
            }
            msg = serialTaskInterface.updateSerialTaskStatus(vo);
            if (!Messages.isSuccess(msg)) {
                logger.error("JobThread run  error  paramMap={} taskOid={} task={}", JsonUtils.writeValue(paramMap), taskOid, JsonUtils.writeValue(vo));
                throw new BizException("JobThread run serialTaskInterface updateSerialTask error vo = " + JsonUtils.writeValue(vo));
            }
        }
    }

	
}
