package com.le.jr.am.profit.service.work.dts;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.HoldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by dujianjun on 2017/3/21.
 */
public class CompareHoldDataWithCacheDTS implements SimpleJobProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CompareHoldDataWithCacheDTS.class);



    @Resource
    private HoldService holdService;

    @Resource
    private HoldApartService holdApartService;

    @Override
    public ProcessResult process(SimpleJobContext context) {


        logger.info("CompareHoldDataWithCacheDTS 检查总仓数据与缓存异同-----");
        try {
            holdService.compareHoldDataWithCache();
        } catch (Exception e) {
            logger.error("CompareHoldDataWithCacheDTS 检查总仓数据与缓存异同失败:", e.getMessage());
        }


        logger.info("CompareHoldDataWithCacheDTS 检查分仓数据与缓存异同-----");
        try {
            holdApartService.compareApartHoldDataWithCache();
        } catch (Exception e) {
            logger.error("CompareHoldDataWithCacheDTS 检查分仓数据与缓存异同失败:", e.getMessage());
        }

        return new ProcessResult( true);
    }
}
