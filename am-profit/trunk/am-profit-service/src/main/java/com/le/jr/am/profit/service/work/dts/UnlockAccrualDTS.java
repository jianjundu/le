package com.le.jr.am.profit.service.work.dts;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.HoldService;

/**
 * cron.mmp.unlock_accrual
 */
public class UnlockAccrualDTS implements SimpleJobProcessor{
	
	private static final Logger logger = LoggerFactory.getLogger(UnlockAccrualDTS.class);

	@Resource
	private HoldService holdService;

	@Override
	public ProcessResult process(SimpleJobContext context) {

		logger.info("process 解锁计息-----");
		try {
			holdService.unlockAccrual();

		} catch (Exception e) {
			logger.error("process 解锁计息:", e.getMessage());
			return new ProcessResult(false);
		}

		logger.info("process 解锁计息-----");

		return new ProcessResult(true);

	}

}
