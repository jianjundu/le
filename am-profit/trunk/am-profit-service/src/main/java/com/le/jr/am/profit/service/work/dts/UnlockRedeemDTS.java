package com.le.jr.am.profit.service.work.dts;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.HoldService;

/**
 * cron.mmp.unlock_redeem
 */
public class UnlockRedeemDTS implements SimpleJobProcessor{
	
	private static final Logger logger = LoggerFactory.getLogger(UnlockRedeemDTS.class);

	@Resource
	private HoldService holdService;

	@Override
	public ProcessResult process(SimpleJobContext context) {

		logger.info("process根据分仓更新合仓可赎回份额-----");
		try {
			holdService.unlockRedeem();

		} catch (Exception e) {
			logger.error("process 根据分仓更新合仓可赎回份额:", e.getMessage());
			return new ProcessResult(false);
		}

		logger.info("process 根据分仓更新合仓可赎回份额-----");

		return new ProcessResult(true);

	}



}
