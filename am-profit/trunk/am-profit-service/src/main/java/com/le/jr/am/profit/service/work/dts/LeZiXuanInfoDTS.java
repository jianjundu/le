package com.le.jr.am.profit.service.work.dts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.ParallelJobProcessor;
import com.le.dts.client.executor.parallel.processor.ParallelJobContext;
import com.le.dts.common.domain.DtsState;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.dts.common.domain.result.Result;
import com.le.jr.am.order.common.util.JsonUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.enums.ProductStateEnum;
import com.le.jr.am.product.domain.enums.ProductSubTypeEnum;
import com.le.jr.am.product.domain.enums.ProductTypeEnum;
import com.le.jr.am.product.domain.vo.SearchProductVo;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.domain.vo.ProductTaskVo;
import com.le.jr.am.profit.service.CallDubboService;
import com.le.jr.am.profit.service.ProductOrderInfoService;

/**
 * 乐自选投资统计任务(处理上一分钟的数据)
 * @author yinxiao
 *
 */
public class LeZiXuanInfoDTS implements ParallelJobProcessor {
    private static final Logger logger = LoggerFactory.getLogger(LeZiXuanInfoDTS.class);

    @Resource
    private CallDubboService callDubboService;
    
    @Resource
    private ProductOrderInfoService productOrderInfoService;
    
    @Override
    public ProcessResult process(ParallelJobContext parallelJobContext) {

        Object task = parallelJobContext.getTask();

        if (DtsState.START.equals(task)) {
            logger.info("LeZiXuanInfoDTS [一级任务] 开始");
            
            SearchProductVo searchProductVo = new SearchProductVo();
			searchProductVo.setProductStateEnums(Arrays.asList(ProductStateEnum.RAISING));
			searchProductVo.setProductTypeEnum(ProductTypeEnum.PRODUCTTYPE_02);
			searchProductVo.setProductSubTypeEnum(ProductSubTypeEnum.SELECTED);
			List<Product> products = callDubboService.callSelectProductByVo(searchProductVo).getData();
			
			if(products==null || products.isEmpty()){
				logger.info("LeZiXuanInfoDTS 无待执行任务");
				return new ProcessResult(true);
			}
            
            Date date = DateUtil.minuteBefor(new Date(),1);
            List<ProductTaskVo> tasks=new ArrayList<ProductTaskVo>();
            for(Product product:products){
            	ProductTaskVo productTaskVo=new ProductTaskVo();
            	productTaskVo.setProductOid(product.getOid());
            	productTaskVo.setDate(date);
            	tasks.add(productTaskVo);
            }
            logger.info("LeZiXuanInfoDTS 待执行任务:" + JsonUtil.objectToJson(tasks));
            Result<Boolean> result = parallelJobContext.dispatchTaskList(tasks, "二级任务");
            logger.info("LeZiXuanInfoDTS[一级任务]结束");
            return new ProcessResult(result.getData());

        } else if (task instanceof ProductTaskVo) {
            logger.info("LeZiXuanInfoDTS[二级任务]开始:"+JsonUtil.objectToJson(task));
            ProductTaskVo productTaskVo=(ProductTaskVo)task;
            try{
            	productOrderInfoService.LeZiXuanDTS(productTaskVo);
            	logger.info("LeZiXuanInfoDTS[二级任务]执行成功,productOid:"+productTaskVo.getProductOid());
            }catch(Exception e){
            	logger.info("LeZiXuanInfoDTS[二级任务]执行失败,productOid:"+productTaskVo.getProductOid(),e);
            	return new ProcessResult(false);
            }
        }
        return new ProcessResult(true);
    }

}
