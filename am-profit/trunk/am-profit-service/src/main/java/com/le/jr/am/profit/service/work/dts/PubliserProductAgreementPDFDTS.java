package com.le.jr.am.profit.service.work.dts;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.le.dts.client.executor.job.processor.ParallelJobProcessor;
import com.le.dts.client.executor.parallel.processor.ParallelJobContext;
import com.le.dts.common.domain.DtsState;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.channel.domain.Spv;
import com.le.jr.am.channel.interfaces.SPVInterfaces;
import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.order.domain.enums.OrderContractStatusEnum;
import com.le.jr.am.order.interfaces.OrderInterface;
import com.le.jr.am.product.common.util.ValidateUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.enums.ProductStateEnum;
import com.le.jr.am.product.domain.vo.SearchProductVo;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.domain.PDFUploadVo;
import com.le.jr.am.profit.domain.PublisherProductAgreement;
import com.le.jr.am.profit.domain.constant.SysConstant;
import com.le.jr.am.profit.service.PublisherProductAgreementService;
import com.le.jr.platform.redis.RedisClient;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.StringUtil;
import com.tstd2.log4j.log.LogTransactionIdManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class PubliserProductAgreementPDFDTS implements ParallelJobProcessor {

	private static final Logger logger = LoggerFactory.getLogger(PubliserProductAgreementPDFDTS.class);

	@Resource
	private PublisherProductAgreementService publisherProductAgreementService;


	@Resource
	private ProductInterfaces productInterfaces;

	@Resource
	private OrderInterface orderInterface;


	

	@Resource
	private SPVInterfaces spvInterfaces;

	@Resource
	private RedisClient redisClient;
	
	//设置redis超时时间为3600秒 即1小时
	private int redisTimeout =60*60;





	@Override
	public ProcessResult process(ParallelJobContext context) {

		//重置线程号
		LogTransactionIdManager.resetTransactionId();
		logger.info("process makePDFandupload begin-----");
		Object task = context.getTask();
		if (DtsState.START.equals(task)) {
			logger.info("process:一级任务开始执行");
			SearchProductVo searchProductVo = new SearchProductVo();
			List<ProductStateEnum> lstEnum = new ArrayList<ProductStateEnum>();
			lstEnum.add(ProductStateEnum.CLEARED);
			searchProductVo.setNotEqualProductStateEnums(lstEnum);

			Message<List<Product>> msgProduct = productInterfaces.selectProductByVo(searchProductVo);
			List<Product> lstProduct = new ArrayList<Product>();
			if (msgProduct != null && Messages.isSuccess(msgProduct)&&msgProduct.getData().size()!=0) {
				logger.info("process:查询到产品：{}条",msgProduct.getData().size());
				lstProduct = msgProduct.getData();
				com.le.dts.common.domain.result.Result<Boolean> result = context.dispatchTaskList(lstProduct, "二级任务");
				return new ProcessResult(result.getData());
			}
		} else if (task instanceof Product) {

			//重置线程号
			LogTransactionIdManager.resetTransactionId();
			logger.info("process:二级任务开始执行");
			// 二级任务，拿到一个产品后，去执行
			Product product = (Product) task;
			Message<Spv> spvMsg = spvInterfaces.findOne(product.getSpvOid());

			if(!ValidateUtil.interfaceValidate(spvMsg)){
				logger.error("processOneItem:spvOid:{}发行人spv接口调用失败",product.getSpvOid());
				throw new BizException("processOneItem:spvOid:发行人spv接口调用失败");
			}
			Spv spv = spvMsg.getData();

			String lastOrderOid = "0";

			while(true){
				Message<List<InvestorTradeOrder>> msgOrderList = this.orderInterface.getOrdersByProductOidAndOffsetOid4Contract(product.getOid(), lastOrderOid);
				if(!ValidateUtil.interfaceValidate(msgOrderList)){
					logger.error("调用订单接口异常:productOid:{}", product.getOid());
					break;
				}
				List<InvestorTradeOrder> lstOrder = msgOrderList.getData();
				
				logger.info("调用订单接口:productOid:{}查询到订单：{}条",product.getOid(),lstOrder==null?"0":lstOrder.size());

				List<PDFUploadVo> lstPDFUploadVo = new ArrayList<PDFUploadVo>();
				String tempLastOrderOid ="";
				
				if(null==lstOrder ||lstOrder.size()<=0){
					break;
				}
				for (InvestorTradeOrder order:lstOrder){
					PDFUploadVo pdfVo = new PDFUploadVo();
					pdfVo.setSubType(product.getSubType());
					pdfVo.setOrderOid(order.getOid());
					pdfVo.setProductInvestFileKey(product.getInvestFileKey());
					pdfVo.setProductOid(product.getOid());
					pdfVo.setProductServiceFileKey(product.getServiceFileKey());
					pdfVo.setSpvAddress(spv.getAddress());
					pdfVo.setSpvOid(spv.getOid());
					pdfVo.setSpvCompanyName(spv.getCompanyName());
					pdfVo.setSpvLicenceNo(spv.getLicenceNo());
					pdfVo.setInvestMin(product.getInvestMin());
					pdfVo.setInvestAdditional(product.getInvestAdditional());
					pdfVo.setExpAror(product.getExpAror());
					lstPDFUploadVo.add(pdfVo);
					tempLastOrderOid = order.getOid();
				}
				if(lstPDFUploadVo==null||lstPDFUploadVo.size()==0){
					logger.info("process:执行二级任务product：{}没有查到订单",product.getOid());
				}
				com.le.dts.common.domain.result.Result<Boolean> result = context.dispatchTaskList(lstPDFUploadVo, "三级任务");
				if(result.getData()){
					if(!tempLastOrderOid.equals("")){
						lastOrderOid = tempLastOrderOid;
					}
					
				}else{
					return new ProcessResult(false);
				}
			}
			return new ProcessResult(true);
		} else if (task instanceof PDFUploadVo) {

			//重置线程号
			LogTransactionIdManager.resetTransactionId();
			logger.info("PubliserProductAgreementPDFDTS:三级任务开始执行");
			// 二级任务，拿到一个产品后，去执行
			final PDFUploadVo pdfUploadVo = (PDFUploadVo) task;
			logger.info("process三级任务：orderOid：{}",pdfUploadVo.getOrderOid());
			List<PublisherProductAgreement> agreeList = new ArrayList<PublisherProductAgreement>();

			final String serviceKey = SysConstant.AM_PROFIT_PRODUCTCONTRACTMODEL+pdfUploadVo.getProductOid()+"_service";




//			String serviceModel =redisClient.getString(serviceKey);
//			if(StringUtil.isBlank(serviceModel)){
//				serviceModel = this.productAgreementService.getServiceModel(pdfUploadVo);
//				if (null == serviceModel) {
//					logger.error("processProduct:服务协议不存在：productOid{}", pdfUploadVo.getProductOid());
//					throw new BizException("服务协议不存在");
//				}
//				redisClient.set(serviceKey, serviceModel,redisTimeout);
//			}



//			final String investKey = SysConstant.AM_PROFIT_PRODUCTCONTRACTMODEL+pdfUploadVo.getProductOid()+"_invest";
//			StringBuffer agreementModel ;
//			try {
//				agreementModel = modelCache.get(investKey, new Callable<StringBuffer>() {
//					@Override
//					public StringBuffer call() throws Exception {
//						String strModel = productAgreementService.getContractModel(pdfUploadVo);
//						if (StringUtil.isNotBlank(strModel)) {
//							modelCache.put(investKey, new StringBuffer(strModel));
//							return new StringBuffer(strModel);
//						}else{
//							logger.error("查询产品投资模板失败，productOid：{}", pdfUploadVo.getProductOid());
//							throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "查询产品投资协议模板失败");
//						}
//					}
//				});
//			} catch (Exception e) {
//				logger.error("查询产品模板失败，productOid：{}", pdfUploadVo.getProductOid());
//				throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "查询产品模板失败");
//			}



//			String agreementModel =redisClient.getString(investKey);
//			if(StringUtil.isBlank(agreementModel)){
//				agreementModel = this.productAgreementService.getContractModel(pdfUploadVo);
//				if (null == agreementModel) {
//					logger.error("processProduct:投资协议不存在：productOid{}", pdfUploadVo.getProductOid());
//					throw new BizException("投资协议不存在");
//				}
//				redisClient.set(investKey, agreementModel,redisTimeout);
//			}
			logger.info("PubliserProductAgreementPDFDTS:三级任务订单：{}查询模板完成",pdfUploadVo.getOrderOid());
			Message<InvestorTradeOrder> msgOrder = this.orderInterface.getOrderByOid(pdfUploadVo.getOrderOid());
			if(!ValidateUtil.interfaceValidate(msgOrder)){
				logger.error("processOneItem:orderOid:{}发行人order接口调用失败",pdfUploadVo.getOrderOid());
				throw new BizException("processOneItem:orderOid:order接口调用失败");
			}
			InvestorTradeOrder order = msgOrder.getData();
			try {
				this.publisherProductAgreementService.processOneItem(pdfUploadVo, agreeList, order);
				logger.info("PubliserProductAgreementPDFDTS:三级任务订单：{}生成协议完成",pdfUploadVo.getOrderOid());
			} catch (Exception e) {
				logger.error("{}生成HTML异常", pdfUploadVo.getOrderOid(), e);
				order.setContractStatus(OrderContractStatusEnum.TOHTML.value);
			}
			
			try{
				List<InvestorTradeOrder> lstOrder = new ArrayList<InvestorTradeOrder>();
				lstOrder.add(order);
				Message<Boolean> msgBoolean =this.orderInterface.batchUpdate(lstOrder);
				if(null!= msgBoolean && null !=msgBoolean.getData() && msgBoolean.getData()){
					logger.info("process:处理订单完成 orderOid:{}", pdfUploadVo.getOrderOid());
					if(order.getContractStatus().equals(OrderContractStatusEnum.PDFOK.value)){
						if(agreeList!=null&&agreeList.size()!=0){
							this.publisherProductAgreementService.saveBatch(agreeList);
							logger.info("process:保存订单协议完成 orderOid:{}", pdfUploadVo.getOrderOid());
						}
					}
					
				}
				
				
			}catch (Exception e){
				logger.error("processProduct.执行失败,productOid:{}，e：{}",pdfUploadVo.getProductOid(),e);
				
			}

		}

		logger.info("process uploadPDF end-----");

		return new ProcessResult(true);

	}

}
