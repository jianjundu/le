package com.le.jr.am.profit.service.work.dts;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.am.profit.service.PublisherProductAgreementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class LoadHoldData2CacheMultiDTS implements SimpleJobProcessor {


    private static final Logger logger = LoggerFactory.getLogger(LoadHoldData2CacheMultiDTS.class);


    @Resource
    private HoldService holdService;

    @Resource
    private HoldApartService holdApartService;


    @Override
    public ProcessResult process(SimpleJobContext context) {

    	Boolean flag1 = false;
    	Boolean flag2 = false;
    

        logger.info("process 执行开始多线程增量加载总仓数据到缓存-----");
        try {
            holdService.loadHoldData2CacheMulti();
            flag1 = true;
        } catch (Exception e) {
            logger.error("process 执行多线程增量加载总仓数据到缓存失败:", e);
            
        }
        logger.info("process 执行多线程完成增量加载总仓数据到缓存-----");

        logger.info("process 执行多线程开始增量加载分仓到缓存-----");
        try {
            holdApartService.loadHoldApartData2CacheMulti();
            flag2 = true;
        } catch (Exception e) {
            logger.error("process 执行多线程增量加载分仓到缓存失败:", e);
           
        }
        logger.info("process 执行多线程结束增量加载分仓到缓存-----");

        

        if(flag1 && flag2){
        	 return new ProcessResult( true); 
        }else{
        	 return new ProcessResult( false); 
        }
       

    }

}