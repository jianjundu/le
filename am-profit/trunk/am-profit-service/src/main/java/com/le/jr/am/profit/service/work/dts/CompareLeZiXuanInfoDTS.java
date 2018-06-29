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
import com.le.jr.am.profit.service.config.ProfitDiamondService;

/**
 * 乐自选投资统计任务(重新校准昨日及以前数据)
 * @author yinxiao
 *
 */
public class CompareLeZiXuanInfoDTS implements ParallelJobProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CompareLeZiXuanInfoDTS.class);

    @Resource
    private CallDubboService callDubboService;
    
    @Resource
    private ProductOrderInfoService productOrderInfoService;
    
    @Resource
	private ProfitDiamondService profitDiamondService;
    
    @Override
    public ProcessResult process(ParallelJobContext parallelJobContext) {

        Object task = parallelJobContext.getTask();

        if (DtsState.START.equals(task)) {
            logger.info("CompareLeZiXuanInfoDTS [一级任务] 开始");
            
            SearchProductVo searchProductVo = new SearchProductVo();
 			searchProductVo.setProductStateEnums(Arrays.asList(ProductStateEnum.RAISING));
 			searchProductVo.setProductTypeEnum(ProductTypeEnum.PRODUCTTYPE_02);
 			searchProductVo.setProductSubTypeEnum(ProductSubTypeEnum.SELECTED);
 			List<Product> products = callDubboService.callSelectProductByVo(searchProductVo).getData();
 			
 			if(products==null || products.isEmpty()){
 				logger.info("CompareLeZiXuanInfoDTS 无待执行任务");
 				return new ProcessResult(true);
 			}
 			
 			Date date = DateUtil.getBeforeDate();
 			String isRefushAll=null; 
 			//是只刷新昨天数据还是刷新昨天及之前所有数据
            Object isRefushAllObject=profitDiamondService.getMessage("lezixuanRefreshAll");
            if(isRefushAllObject!= null){
            	isRefushAll=isRefushAllObject.toString();
            }
            
            List<ProductTaskVo> tasks=new ArrayList<ProductTaskVo>();
            for(Product product:products){
            	ProductTaskVo productTaskVo=new ProductTaskVo();
            	productTaskVo.setProductOid(product.getOid());
            	productTaskVo.setDate(date);
            	productTaskVo.setIsRefreshAll(isRefushAll);
            	tasks.add(productTaskVo);
            }
            logger.info("CompareLeZiXuanInfoDTS 待执行任务:" + JsonUtil.objectToJson(tasks));
            Result<Boolean> result = parallelJobContext.dispatchTaskList(tasks, "二级任务");
            logger.info("CompareLeZiXuanInfoDTS[一级任务]结束");
            return new ProcessResult(result.getData());

        } else if (task instanceof ProductTaskVo) {
            logger.info("CompareLeZiXuanInfoDTS[二级任务]开始:"+JsonUtil.objectToJson(task));
            ProductTaskVo productTaskVo=(ProductTaskVo)task;
            try{
            	productOrderInfoService.CompareLeZiXuanDTS(productTaskVo);
            	logger.info("CompareLeZiXuanInfoDTS[二级任务]执行成功,productOid:"+productTaskVo.getProductOid());
            }catch(Exception e){
            	logger.info("CompareLeZiXuanInfoDTS[二级任务]执行失败,productOid:"+productTaskVo.getProductOid(),e);
            	return new ProcessResult(false);
            }
        }
        return new ProcessResult(true);
    }

}
