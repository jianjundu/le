package com.le.jr.am.profit.service.work.dts;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.HoldService;

/**
 * cron.mmp.dayredeemvolume
 */
public class ResetDayRedeemVolumeDTS implements SimpleJobProcessor{
	
	private static final Logger logger = LoggerFactory.getLogger(ResetDayRedeemVolumeDTS.class);

	@Resource
	private HoldService holdService;

	@Override
	public ProcessResult process(SimpleJobContext context) {

		logger.info("process 单人单日赎回份额重置-----");
		try {
			holdService.resetDayRedeemVolume();

		} catch (Exception e) {
			logger.error("process 单人单日赎回份额重置:", e.getMessage());
			return new ProcessResult(false);
		}

		logger.info("process 单人单日赎回份额重置-----");

		return new ProcessResult(true);

	}


}
