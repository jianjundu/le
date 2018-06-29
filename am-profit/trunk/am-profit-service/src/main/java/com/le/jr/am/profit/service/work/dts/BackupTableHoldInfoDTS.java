package com.le.jr.am.profit.service.work.dts;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.work.BackupIncomeTableWork;

public class BackupTableHoldInfoDTS implements SimpleJobProcessor {

	private static final Logger logger = LoggerFactory.getLogger(BackupTableHoldInfoDTS.class);

	@Resource
	private BackupIncomeTableWork backupIncomeTableWork; 

	@Override
	public ProcessResult process(SimpleJobContext context) {

		logger.info("process 执行开始备份持有收益表信息-----");
		try {
			
			backupIncomeTableWork.backupIncomeData();

		} catch (Exception e) {
			logger.error("process 执行备份持有收益表信息失败:", e.getMessage());
			return new ProcessResult(false);
		}

		logger.info("process 执行结束备份持有收益表信息-----");

		return new ProcessResult(true);

	}

}
