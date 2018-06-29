package com.le.jr.am.profit.service.work.dts;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.accountant.common.util.DateUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.profit.service.util.StaticProperties;
import com.le.jr.am.profit.service.work.ProductPracticeWork;
import com.le.jr.am.task.domain.enums.JobLockIdEnum;


/**
 * cron.mmp.practice
 * 产品计息规模试算
 */
public class ProfitTryPracticeDTS implements SimpleJobProcessor {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ProfitTryPracticeDTS.class);
	
	@Resource
	private ProductPracticeWork productPracticeWork;

	


	@Override
	public ProcessResult process(SimpleJobContext context) {
		
		logger.info("ProfitTryPracticeDTS practice开始执行");

		// 当前日期
		Date incomeDate = DateUtil.getSqlDate();
		// 24点日切时间
		if (StaticProperties.isIs24()) {
			// 取前一天日期
			incomeDate = DateUtil.getBeforeDate();
		}

		// 批次号
		String batchCode = DateUtil.format(DateUtil.getSqlDate(), "yyyyMMdd");

		logger.info("ProfitTryPracticeDTS:batchCode{},incomeDate{}", batchCode,incomeDate);

		try {
			// 计息快照是分派收益的前提，这里查看是否已经完成计息快照
			//productPracticeWork.isSnapshotVolume(batchCode, JobLockIdEnum.SNAPSHOT.value);

			// 判断是否试算已经执行过

			logger.info("ProfitTryPracticeDTS 计息快照已经执行过 ,开始分发试算任务..");

			List<Product> result = productPracticeWork.queryCanPracticeProduct();

			for (Product product : result) {
				logger.info("ProfitTryPracticeDTS==productOid:{},incomeDate{},batchCode:{}",product.getOid(), incomeDate,batchCode);
				productPracticeWork.createPracticeSerial(product, incomeDate,batchCode);
			}

		} catch (Exception e) {
			logger.error("ProfitTryPracticeDTS : e:{}", e.getMessage());
			return new ProcessResult(false);
		}

		logger.info("process 执行结束产品计息规模试算-----");

		return new ProcessResult(true);
		
	}

}
