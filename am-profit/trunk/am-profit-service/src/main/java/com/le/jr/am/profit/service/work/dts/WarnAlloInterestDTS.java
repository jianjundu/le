package com.le.jr.am.profit.service.work.dts;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.RewardIncomePracticeService;
/**
 * 提醒派息任务
 * @author yinxiao
 *
 */
public class WarnAlloInterestDTS implements SimpleJobProcessor{
	
	private static final Logger logger = LoggerFactory.getLogger(WarnAlloInterestDTS.class);

	@Resource
	private RewardIncomePracticeService rewardIncomePracticeService;
	
	@Override
	public ProcessResult process(SimpleJobContext context) {
		logger.info("process 执行提醒派息任务开始-----");
		try {
			rewardIncomePracticeService.warnAlloInterestDTS();
		} catch (Exception e) {
			logger.error("process 执行提醒派息任务失败:", e.getMessage());
			return new ProcessResult(false);
		}
		logger.info("process 执行提醒派息任务结束-----");
		return new ProcessResult(true);
	}

}
