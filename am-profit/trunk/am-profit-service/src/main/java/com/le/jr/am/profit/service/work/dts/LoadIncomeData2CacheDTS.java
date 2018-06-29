package com.le.jr.am.profit.service.work.dts;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.HoldApartIncomeService;
import com.le.jr.am.profit.service.HoldLevelIncomeService;

public class LoadIncomeData2CacheDTS implements SimpleJobProcessor {
	
	
	

	private static final Logger logger = LoggerFactory.getLogger(LoadIncomeData2CacheDTS.class);

	
	
	@Resource
	private HoldLevelIncomeService holdLevelIncomeService;
	
	@Resource
	private HoldApartIncomeService holdApartIncomeService;
	
	
	@Override
	public ProcessResult process(SimpleJobContext context) {
		
		Boolean flag1 = false;
		Boolean flag2 = false;
		

		logger.info("process 执行开始增量更新缓存数据-----");
		try {

			
			holdApartIncomeService.loadApartIncome2Cache();
			flag1 = true;
		} catch (Exception e) {
			logger.error("process 执行增量更新分仓收益缓存数据失败:", e.getMessage());
			
		}

		logger.info("process 执行结束增量更新分仓收益缓存数据-----");
		logger.info("process 执行开始增量更新阶梯收益缓存数据-----");
		try {
			holdLevelIncomeService.loadLevelIncomeData2Cache();
			flag2 =true;
		} catch (Exception e) {
			logger.error("process 执行增量更新阶梯收益缓存数据失败:", e.getMessage());
			
		}
		
		if(flag2 && flag1 ){
			return new ProcessResult(true);
		}
		
		logger.info("process 执行结束增量更新阶梯收益缓存数据-----");

		return new ProcessResult(false);

	}

}



