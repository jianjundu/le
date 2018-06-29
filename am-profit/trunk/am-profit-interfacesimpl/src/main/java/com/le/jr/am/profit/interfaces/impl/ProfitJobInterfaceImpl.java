package com.le.jr.am.profit.interfaces.impl;

import com.le.jr.am.investment.common.utils.CheckUtils;
import com.le.jr.am.profit.domain.input.InterestParams;
import com.le.jr.am.profit.domain.vo.PracticeParams;
import com.le.jr.am.profit.interfaces.ProfitJobInterface;
import com.le.jr.am.profit.service.ProfitJobService;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by qishang on 2016/11/29.
 */
@Service("profitJobInterface")
public class ProfitJobInterfaceImpl implements ProfitJobInterface {
    Logger logger = LoggerFactory.getLogger(ProfitJobInterfaceImpl.class);
    @Resource
    private ProfitJobService profitJobService;

    /**
     * 解锁赎回份额
     *
     * @param taskOid
     * @return
     */
    @Override
    public Message<Boolean> unlockRedeemJob(String taskOid) {
        logger.info("unlockRedeemJob  taskOid={}", taskOid);
        Boolean result = new Boolean(false);
        try {
            CheckUtils.checkParam(logger,taskOid);
            result = profitJobService.unlockRedeemJob(taskOid);
        } catch (BizException e) {
            logger.error("unlockRedeemJob failed.. taskOid=" + taskOid, e);
            Messages.failed(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("unlockRedeemJob failed..  taskOid=" + taskOid, e);
            Messages.failed(Code.FAIL.getValue(), e.getMessage());
        }
        return Messages.success(result);
    }

    /**
     * 解锁计息份额
     *
     * @param taskOid
     * @return
     */
    @Override
    public Message<Boolean> unlockAccrualJob(String taskOid) {
        logger.info("unlockAccrualJob  taskOid={}", taskOid);
        Boolean result = new Boolean(false);
        try {
            CheckUtils.checkParam(logger,taskOid);
            result = profitJobService.unlockAccrualJob(taskOid);
        } catch (BizException e) {
            logger.error("unlockAccrualJob failed.. taskOid=" + taskOid, e);
            Messages.failed(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("unlockAccrualJob failed..  taskOid=" + taskOid, e);
            Messages.failed(Code.FAIL.getValue(), e.getMessage());
        }
        return Messages.success(result);
    }

    /**
     * 收益分配
     *
     * @param params
     * @return
     */
    @Override
    public Message<Boolean> interestJob(InterestParams params, String taskOid) {
        logger.info("interestJob  params={} taskOid={}", params,taskOid);
        Boolean result = new Boolean(false);
        try {
            CheckUtils.checkParam(logger,params,taskOid);
            result = profitJobService.interestJob(params, taskOid);
        } catch (BizException e) {
            logger.error("interestJob failed.. params"+params.toString()+"taskOid=" + taskOid, e);
            Messages.failed(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("interestJob failed.. params"+params.toString()+"taskOid=" + taskOid, e);
            Messages.failed(Code.FAIL.getValue(), e.getMessage());
        }
        return Messages.success(result);
    }

	@Override
	public Message<Boolean> practiceJob(String taskOid) {
		 logger.info("practiceJob  taskOid={}", taskOid);
	        Boolean result = new Boolean(false);
	        try {
	            CheckUtils.checkParam(logger,taskOid);
	            result = profitJobService.practiceJob(taskOid);
	        } catch (BizException e) {
	            logger.error("practiceJob failed.. taskOid=" + taskOid, e);
	            Messages.failed(e.getCode(), e.getMessage());
	        } catch (Exception e) {
	            logger.error("practiceJob failed..  taskOid=" + taskOid, e);
	            Messages.failed(Code.FAIL.getValue(), e.getMessage());
	        }
	        return Messages.success(result);
	}
	
	@Override
	public Message<Boolean> practiceJob(PracticeParams vo ,String taskOid) {
		 logger.info("practiceJob  taskOid={} vo={}", taskOid,JsonUtils.writeValue(vo));
	        Boolean result = new Boolean(false);
	        try {
	            CheckUtils.checkParam(logger,taskOid);
	            result = profitJobService.practiceJob(vo,taskOid);
	        } catch (BizException e) {
	            logger.error("practiceJob failed.. taskOid=" + taskOid, e);
	            Messages.failed(e.getCode(), e.getMessage());
	        } catch (Exception e) {
	            logger.error("practiceJob failed..  taskOid=" + taskOid, e);
	            Messages.failed(Code.FAIL.getValue(), e.getMessage());
	        }
	        return Messages.success(result);
	}
}
