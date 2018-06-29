package com.le.jr.am.profit.service.work.dts;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.IncomeEventService;

public class AutoAllotInterestNoticeWorkDTS implements SimpleJobProcessor{
	
	private static final Logger logger = LoggerFactory.getLogger(AutoAllotInterestNoticeWorkDTS.class);
	
	
	@Resource
	private IncomeEventService incomeEventService;
	
	

	@Override
	public ProcessResult process(SimpleJobContext context) {
		
		logger.info("process 执行开始自动派息通知-----");
		
		
		
		 try {
			 incomeEventService.notifyPlatformAllotResult();
	        } catch (Exception e) {
	            logger.error("process 执行派息自动通知任务失败:", e.getMessage());
	            return new ProcessResult(false);
	        }
	       
		
		logger.info("process 执行结束自动派息通知-----");
		
		 return new ProcessResult(true);
	}

}
