package com.le.jr.am.profit.service.work.dts;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.IncomeEventService;

public class AutoDistributeProfitDTS implements SimpleJobProcessor{
	
	private static final Logger logger = LoggerFactory.getLogger(AutoDistributeProfitDTS.class);

	@Resource
	private IncomeEventService incomeEventService;

    

	@Override
	public ProcessResult process(SimpleJobContext context) {
		logger.info("process 执行开始自动派息-----");
		try {
			incomeEventService.interest();
		} catch (Exception e) {
			logger.error("process 执行业务自动派息失败:", e.getMessage());
			return new ProcessResult(false);
		}
		logger.info("process 执行结束自动派息-----");
		return new ProcessResult(true);
	}

}
