package com.le.jr.am.profit.service.impl;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.order.domain.enums.OrderContractStatusEnum;
import com.le.jr.am.order.interfaces.OrderInterface;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.product.common.util.ValidateUtil;
import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.product.domain.enums.ProductSubTypeEnum;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.common.util.FileUtil;
import com.le.jr.am.profit.common.util.PdfUtil;
import com.le.jr.am.profit.dao.HoldApartMapper;
import com.le.jr.am.profit.dao.PublisherProductAgreementMapper;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.PDFUploadVo;
import com.le.jr.am.profit.domain.PublisherProductAgreement;
import com.le.jr.am.profit.domain.constant.SysConstant;
import com.le.jr.am.profit.domain.enums.AgreementTypeEnum;
import com.le.jr.am.profit.domain.input.AgreementRequest;
import com.le.jr.am.profit.domain.input.SearchAgreementByVo;
import com.le.jr.am.profit.domain.output.Agreement;
import com.le.jr.am.profit.domain.output.AgreementResponse;
import com.le.jr.am.profit.service.PublisherProductAgreementService;
import com.le.jr.am.profit.service.config.ProfitDiamondService;
import com.le.jr.am.profit.service.util.LeTVUpload;
import com.le.jr.am.system.domain.enums.FileStateEnum;
import com.le.jr.am.system.domain.po.file.File;
import com.le.jr.am.system.interfaces.file.FileInterface;
import com.le.jr.platform.redis.RedisClient;
import com.le.jr.trade.openapi.domain.current.output.InvestorInfoOutput;
import com.le.jr.trade.openapi.interfaces.trading.current.CurrentTradingInterface;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.data.Result;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.le.jr.trade.publictools.util.StringUtil;


@Service("publisherProductAgreementService")
public class PublisherProductAgreementServiceImpl implements PublisherProductAgreementService {
	private static final Logger logger = LoggerFactory.getLogger(PublisherProductAgreementServiceImpl.class);

	@Resource
	private FileInterface fileInterface;

	@Resource
	private OrderInterface orderInterface;

	@Resource
	private HoldApartMapper holdApartMapper;

	@Resource
	private PublisherProductAgreementMapper publisherProductAgreementMapper;

	@Resource
	private CurrentTradingInterface currentTradingInterface;

	@Value("${file.yupload}")
	private String yupload;

	@Value("${file.upload.url}")
	private String fileUploadUrl;

	@Resource
	private RedisClient redisClient;



	@Resource
	private ProfitDiamondService profitDiamondService;

	@Resource
	private ProductInterfaces productInterfaces;


	//产品缓存设置占用带下为10，时间为5秒
	private static Cache<String, String> modelCache = CacheBuilder.newBuilder().maximumSize(10)
			.expireAfterWrite(3600, TimeUnit.SECONDS).build();

	/**
	 * 创建html文件 通过DTS执行
	 */

//	@Override
//	public void makeContract() {
//		JobLock jobLock = null;
//		// 收益快照是否已经成功结束
//		String batchCode = DateUtil.format(DateUtil.getSqlDate(), "yyyyMMdd");
//		try {
//			Message<JobLock> msgJobLock = jobLockInterface.queryAndDealJobLock(batchCode, JobLockIdEnum.UPLOADPDF.value);
//			if (ValidateUtil.interfaceValidate(msgJobLock)) {
//				jobLock = msgJobLock.getData();
//			}else{
//				logger.error("makeContract:未成功创建任务");
//				return ;
//			}
//
//		} catch (Exception e) {
//			logger.error("makeContract:{}",e.getMessage(), e);
//			return;
//		}
//
//		JobLock jobRep = this.makeContractLock();
//		jobLock.setJobStatus(jobRep.getJobStatus());
//		jobLock.setJobMessage(jobRep.getJobMessage());
//		jobLock.setBatchEndTime(DateUtil.getSqlCurrentDate());
//		this.jobLockInterface.updateStatus4Lock(jobLock.getOid(), jobLock.getJobStatus());
//	}

	

//	private JobLock makeContractLock() {
//		JobLock jobRep = new JobLock();
//		jobRep.setJobStatus(JobLockStatusEnum.DONE.value);
//		jobRep.setJobMessage("OK");
//		try {
//			SearchProductVo searchProductVo = new SearchProductVo();
//			List<ProductStateEnum> lstEnum = new ArrayList<ProductStateEnum>();
//			lstEnum.add(ProductStateEnum.CLEARED);
//			searchProductVo.setNotEqualProductStateEnums(lstEnum);
//
//			Message<List<Product>> msgProduct = productInterfaces.selectProductByVo(searchProductVo);
//			List<Product> productList = new ArrayList<Product>();
//			if (msgProduct != null && Messages.isSuccess(msgProduct)) {
//				productList = msgProduct.getData();
//			}
//			if (productList.isEmpty()) {
//				return jobRep;
//			}
//			final CountDownLatch c = new CountDownLatch(productList.size());
//			for (final Product product : productList) {
//				threadPool.execute(new Runnable() {
//					@Override
//					public void run() {
//						try {
//							processProduct(product);
//							c.countDown();
//						} catch (Exception e) {
//							logger.error("productOid:{},协议生成异常", product.getOid(), e);
//							c.countDown();
//						}
//					}
//				});
//			}
//			c.await();
//		} catch (Exception e) {
//			jobRep.setJobMessage(e.getMessage());
//			jobRep.setJobStatus(JobLockStatusEnum.FAIL.value);
//		} finally {
//
//		}
//		return jobRep;
//	}

	// Shell脚本，荣后再看
//	public void processProduct(Product product) throws Exception {
//		logger.info("processProduct:productOid:{}",product.getOid());
//		//获取服务协议模板
//		String serviceModel = this.getServiceModel(product);
//		if (null == serviceModel) {
//			logger.error("processProduct:服务协议不存在：productOid{}", product.getOid());
//			throw new BizException("服务协议不存在");
//		}
//		//获取合同协议模板
//		String agreementModel = this.getContractModel(product);
//		if (null == agreementModel) {
//			logger.error("processProduct:投资协议不存在：productOid{}", product.getOid());
//			throw new BizException("投资协议不存在");
//		}
//
//		String lastOid = "0";
//		while (true) {
//
//			Message<List<InvestorTradeOrder>> orderListMsg = this.orderInterface.getOrdersByProductOidAndOffsetOid4Contract(product.getOid(), lastOid);
//
//			if(!ValidateUtil.interfaceValidate(orderListMsg)){
//				logger.info("调用订单接口异常:productOid:{}",product.getOid());
//				break;
//			}
//
//			List<InvestorTradeOrder> orderList = orderListMsg.getData();
//			logger.info("processProduct:共查询到订单：{}条",orderList.size());
//			if (orderList.isEmpty()) {
//				break;
//			}
//			List<PublisherProductAgreement> agreeList = new ArrayList<PublisherProductAgreement>();
//
//			for (InvestorTradeOrder order : orderList) {
//				try {
//					this.processOneItem(product.getSpvOid(), serviceModel, agreementModel, agreeList, order);
//
//				} catch (Exception e) {
//					logger.error("{}生成HTML异常", order.getOrderCode(), e);
//					order.setContractStatus(OrderContractStatusEnum.TOHTML.value);
//				}
//
//				lastOid = order.getOid();
//			}
//
//			// 事物控制开始
//			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
//			definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//			TransactionStatus status = transactionManager.getTransaction(definition);
//			try{
//
//				this.orderInterface.batchUpdate(orderList);
//				logger.info("processProduct:批量修改订单pdf生成状态完成");
//				if(agreeList!=null&&agreeList.size()!=0){
//					this.saveBatch(agreeList);
//					logger.info("processProduct:批量保存用户协议完成");
//				}
//				transactionManager.commit(status);
//			}catch (Exception e){
//				logger.error("processProduct.执行失败,productOid:{}，e：{}",product.getOid(),e);
//				//回滚事务
//				transactionManager.rollback(status);
//			}
//
//
//		}
//
//	}
	
	
	
	public String getContractModel(PDFUploadVo pdfUploadVo) {
		if(pdfUploadVo==null){
			logger.info("getContractModel产品投资协议不存在");
			throw new BizException("产品不存在");
		}
		
		String productOid = pdfUploadVo.getProductOid();
		
		FileOutputStream fos = null;
		InputStream is = null;

		try {
			Message<List<File>> files = this.fileInterface.list(pdfUploadVo.getProductInvestFileKey(), FileStateEnum.STATE_VALID.value);
			if (null == files || !Messages.isSuccess(files) || files.getData() == null) {
				logger.error("getContractModel产品投资协议不存在,priductOid:{}", pdfUploadVo.getProductOid());
				throw new BizException("30002");
			}
			if (files.getData().size() != 1) {
				logger.error("getContractModel产品投资存在异常,priductOid:{}", productOid);
				throw new BizException("30003");
			}
			File agreement = files.getData().get(0);
			if (null == agreement.getFurl() || "".equals(agreement.getFurl())) {

				logger.error("getContractModel产品投资协议地址不存在(,priductOid:{}", productOid);
				throw new BizException("30004");
			}
			return FileUtil.urlToString(agreement.getFurl(),"UTF-8");

		
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
		return null;
	}

	public String getServiceModel(PDFUploadVo pdfUploadVo) {
		
		if(pdfUploadVo==null){
			logger.info("getServiceModel产品投资协议不存在");
			throw new BizException("产品不存在");
		}
		String productOid = pdfUploadVo.getProductOid();


		FileOutputStream fos = null;
		InputStream is = null;
		

		try {
			Message<List<File>> files = this.fileInterface.list(pdfUploadVo.getProductServiceFileKey(), FileStateEnum.STATE_VALID.value);

			if (null == files || !Messages.isSuccess(files) || files.getData() == null) {
				logger.error("getContractModel产品服务协议不存在,priductOid:{}", productOid);
				throw new BizException("30027");
			}
			if (files.getData().size() != 1) {
				logger.error("getContractModel产品服务协议异常,priductOid:{}", productOid);
				throw new BizException("30028");
			}
			File agreement = files.getData().get(0);
			if (null == agreement.getFurl() || "".equals(agreement.getFurl())) {

				logger.error("getContractModel产品服务协议地址不存在(,priductOid:{}", productOid);
				throw new BizException("30029");
			}
			return FileUtil.urlToString(agreement.getFurl(),"UTF-8");
		
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
		return null;
	}

	public void processOneItem(PDFUploadVo pdfUploadVo,
		List<PublisherProductAgreement> agreeList, InvestorTradeOrder order)throws Exception {


		logger.info("processOneItem:订单：{}开始生成投资协议",order.getOid());
		// 投资协议
		PublisherProductAgreement  investAgreement = this.createInvestEntity(order);

		//生成产品投资协议
		generateHtmlAgreement(investAgreement, pdfUploadVo,
				AgreementTypeEnum.INVESTING.value);
		// 服务协议
		PublisherProductAgreement serviceAgreement = this.createServiceEntity(order); // service

		logger.info("processOneItem:订单：{}开始生成服务协议",order.getOid());
		//生成信息服务协议
		generateServiceAgreement(serviceAgreement, pdfUploadVo,
				AgreementTypeEnum.SERVICE.value);

		
		if(StringUtil.isNotBlank(investAgreement.getAgreementUrl())){
			agreeList.add(investAgreement);
		}

		
		if(StringUtil.isNotBlank(serviceAgreement.getAgreementUrl())){
			agreeList.add(serviceAgreement);
		}

		if(StringUtil.isNotBlank(investAgreement.getAgreementUrl())&&StringUtil.isNotBlank(serviceAgreement.getAgreementUrl())){
			order.setContractStatus(OrderContractStatusEnum.PDFOK.value);
		}

	}

	@Override
	public PublisherProductAgreement findByInvestorTradeOrderAndAgreementType(InvestorTradeOrder order, String agreementType) {
		return publisherProductAgreementMapper.findByInvestorTradeOrderAndAgreementType(order.getOid(), agreementType);
	}

	@Override
	public List<PublisherProductAgreement> findByOrderOid(String orderOid) {
		return this.publisherProductAgreementMapper.findByOrderOid(orderOid);
	}
	

	/**
	 * 生成服务协议

	 * @return
	 */
	private void generateServiceAgreement(PublisherProductAgreement agree,final PDFUploadVo pdfUploadVo, String type) throws Exception{




		final String serviceKey = SysConstant.AM_PROFIT_PRODUCTCONTRACTMODEL+pdfUploadVo.getProductOid()+"_service";
		String serviceModel ;
		try {
			serviceModel = modelCache.get(serviceKey, new Callable<String>() {
				@Override
				public String call() throws Exception {
					String strModel = getServiceModel(pdfUploadVo);
					if (StringUtil.isNotBlank(strModel)) {
						modelCache.put(serviceKey, strModel);
						return strModel;
					}else{
						logger.error("查询产品模板失败，productOid：{}", pdfUploadVo.getProductOid());
						throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "查询产品服务协议模板失败");
					}
				}
			});
		} catch (Exception e) {
			logger.error("查询产品模板失败，productOid：{}", pdfUploadVo.getProductOid());
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "查询产品失败");
		}

		StringBuffer agreement = new StringBuffer(serviceModel);

		String orderOid = agree.getOrderOid();
		// 尹总提供
		Message<InvestorTradeOrder> msgOrder = orderInterface.getOrderByOid(orderOid);
		InvestorTradeOrder order = msgOrder.getData();

		Message<InvestorInfoOutput> msg = null;
		String investorOid = order.getInvestorOid();

		try {

			msg = currentTradingInterface.getInvestorInfo(investorOid);
		} catch (Exception e) {
			logger.error("getInvestorInfo {} invoked failure", investorOid);
			throw new RuntimeException("getInvestorInfo invoked failure", e);
		}

		if (Result.isSuccess(msg.getResult())) {
			InvestorInfoOutput data = msg.getData();
			if (data != null) {
				agreement = this.replaseStrBuf(agreement,"#investorAccount",data.getInvestorAccount());// 乐视金融用户名：【#investorAccount】
				agreement = this.replaseStrBuf(agreement,"#tradeOrderOid",agree.getAgreementCode()); // 编号:
				if(StringUtil.isNotBlank(data.getInvestorID())){
					agreement = this.replaseStrBuf(agreement,"#investorID",data.getInvestorID()); // 身份证号码：【#investorID】
				}else{
					agreement = this.replaseStrBuf(agreement,"#investorID",""); // 身份证号码：【#investorID】
				}
			}
		} else {
			logger.error("getInvestorInfo {} info failured .", investorOid);
			throw new RuntimeException("getInvestorInfo invoked failure");
		}
		String path = profitDiamondService.getMessage("filePath").toString()+new SimpleDateFormat("yyyyMMdd").format(new Date());

		java.io.File fileDir = new java.io.File(path);
		if(!fileDir.exists()){
			fileDir.mkdir();
		}
		//生成pdf服务协议
		String fileName  = path + java.io.File.separator + order.getOrderCode() + "_"+ type+".pdf";

		PdfUtil.generatePdfFromStr(new java.io.File(fileName),agreement.toString(),profitDiamondService.getMessage("fontPath").toString());

		java.io.File file   = new java.io.File(fileName);
		String returnURL = LeTVUpload.upload(file);
		
		logger.info("generateServiceAgreement==orderCode:{}, type:{},fileUrl:{}",order.getOrderCode(),type,returnURL);
		agree.setAgreementUrl(returnURL);
		
	}

	
	private void generateHtmlAgreement(PublisherProductAgreement agree, final PDFUploadVo pdfUploadVo , String type) throws Exception{

		final String investKey = SysConstant.AM_PROFIT_PRODUCTCONTRACTMODEL+pdfUploadVo.getProductOid()+"_invest";
		String  investModel ;
		try {
			investModel = modelCache.get(investKey, new Callable<String>() {
				@Override
				public String call() throws Exception {
					String strModel = getContractModel(pdfUploadVo);
					if (StringUtil.isNotBlank(strModel)) {
						modelCache.put(investKey, strModel);
						return strModel;
					}else{
						logger.error("查询产品投资模板失败，productOid：{}", pdfUploadVo.getProductOid());
						throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "查询产品投资协议模板失败");
					}
				}
			});
		} catch (Exception e) {
			logger.error("查询产品模板失败，productOid：{}", pdfUploadVo.getProductOid());
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "查询产品模板失败");
		}

		StringBuffer agreement = new StringBuffer(investModel);

		String orderOid = agree.getOrderOid();
		// 尹总提供
		
		Message<InvestorTradeOrder> orderMessage = orderInterface.getOrderByOid(orderOid);
		if(!ValidateUtil.interfaceValidate(orderMessage)){
			logger.error("generateHtmlAgreement:order:{}没有查询到订单",orderOid);
			throw new BizException("调用订单接口异常");
		}
		InvestorTradeOrder order = orderMessage.getData();
		
		BigDecimal orderVolume = order.getOrderVolume();
		String investorOid = order.getInvestorOid();
		Message<InvestorInfoOutput> msg = null;
		try {
			msg = currentTradingInterface.getInvestorInfo(investorOid);
			logger.info("generateHtmlAgreement:msg{}",JsonUtils.writeValue(msg));
		} catch (Exception e) {
			logger.error("getInvestorInfo {} invoked failure", investorOid);
			throw new RuntimeException("getInvestorInfo invoked failure", e);
		}
		if (Result.isSuccess(msg.getResult())) {
			InvestorInfoOutput data = msg.getData();
			if (data != null) {
				//乐自选自有的；
				if(ProductSubTypeEnum.SELECTED.getValue() == pdfUploadVo.getSubType()){
					logger.info("generateHtmlAgreement:orderOid:{},为乐自选项目",order.getOid());
					//订单生成日期
					if(order!=null&&order.getOrderTime()!=null){
						agreement = replaseStrBuf(agreement,"#orderYear",String.valueOf(DateUtil.getYear(order.getOrderTime())));
						agreement = replaseStrBuf(agreement,"#orderMonth",String.valueOf(DateUtil.getMonth(order.getOrderTime())));
						agreement = replaseStrBuf(agreement,"#orderDay",String.valueOf(DateUtil.getDay(order.getOrderTime())));
					}
					//订单起息日
					HoldApart holdApart = this.holdApartMapper.findByTradeOrder(order.getOid());
					if(holdApart!=null&&holdApart.getBeginAccuralDate()!=null){
						agreement = replaseStrBuf(agreement,"#investYear",String.valueOf(DateUtil.getYear(holdApart.getBeginAccuralDate())));
						agreement = replaseStrBuf(agreement,"#investMonth",String.valueOf(DateUtil.getMonth(holdApart.getBeginAccuralDate())));
						agreement = replaseStrBuf(agreement,"#investDay",String.valueOf(DateUtil.getDay(holdApart.getBeginAccuralDate())));
					}


					String rewardOid = order.getLockRewardOid();
					Message<ProductIncomeReward> msgReward = productInterfaces.selectProductRewardByOid(rewardOid);
					//订单转让期限
					if(ValidateUtil.interfaceValidate(msgReward)){
						Integer endDates = msgReward.getData().getEndDate();
						agreement = replaseStrBuf(agreement,"#transferPeriod",String.valueOf(endDates));
						//计算
						if(holdApart!=null&&holdApart.getBeginAccuralDate()!=null){
							Date transferDay = new Date(holdApart.getBeginAccuralDate().getTime() +(endDates-1)*24*60*60*1000);
							agreement = replaseStrBuf(agreement,"#transferYear",String.valueOf(DateUtil.getYear(transferDay)));
							agreement = replaseStrBuf(agreement,"#transferMonth",String.valueOf(DateUtil.getMonth(transferDay)));
							agreement = replaseStrBuf(agreement,"#transferDay",String.valueOf(DateUtil.getDay(transferDay)));
						}
					}
					//最小投资金额，递增投资额，年化收益率
					agreement = replaseStrBuf(agreement,"#startAmount",String.valueOf(pdfUploadVo.getInvestMin()));
					agreement = replaseStrBuf(agreement,"#increasingAmount",String.valueOf(pdfUploadVo.getInvestAdditional()));
					agreement = replaseStrBuf(agreement,"#expAror",String.valueOf(pdfUploadVo.getExpAror().multiply(new BigDecimal(100))));


				}

				agreement = replaseStrBuf(agreement,"#spvName",pdfUploadVo.getSpvCompanyName());
				agreement = replaseStrBuf(agreement,"#spvAddr",null == pdfUploadVo.getSpvAddress() ? "空" : pdfUploadVo.getSpvAddress());
				agreement = replaseStrBuf(agreement,"#busiLicenseNo",null == pdfUploadVo.getSpvLicenceNo() ? "空" : pdfUploadVo.getSpvLicenceNo());
				agreement = replaseStrBuf(agreement,"#tradeOrderOid",agree.getAgreementCode());

				agreement = replaseStrBuf(agreement,"#investorName",data.getInvestorName());
				agreement = replaseStrBuf(agreement,"#investorAccount",data.getInvestorAccount());




				//机构理财用户没有身份证号，这里会报空指针，所以
				if(StringUtil.isNotBlank(data.getInvestorID())){ // 证件编号
					agreement = replaseStrBuf(agreement,"#investorID",data.getInvestorID());
				}else{
					agreement = replaseStrBuf(agreement,"#investorID","");
				}

				agreement = replaseStrBuf(agreement,"#orderDate",DateUtil.format(order.getOrderTime()));
				agreement = replaseStrBuf(agreement,"#orderVolume",orderVolume.toString()); // 单位份数#orderVolume
				agreement = replaseStrBuf(agreement,"#null",""); // 单位份数#orderVolume
			}
		} else {
			logger.error("getInvestorInfo {} info failured .", investorOid);
			throw new RuntimeException("getInvestorInfo invoked failure");
		}


		String path = profitDiamondService.getMessage("filePath").toString()+new SimpleDateFormat("yyyyMMdd").format(new Date());
		java.io.File fileDir = new java.io.File(path);
		if(!fileDir.exists()){
			fileDir.mkdir();
		}
		String fileName  = path + java.io.File.separator + order.getOrderCode() +"_"+ type+".pdf";
		logger.info("generateHtmlAgreement:fontpath:{}",profitDiamondService.getMessage("fontPath").toString());


		PdfUtil.generatePdfFromStr(new java.io.File(fileName),agreement.toString(),profitDiamondService.getMessage("fontPath").toString());
		java.io.File file = new java.io.File(fileName);
		if(!file.isDirectory()){
			file.mkdir();
		}
		String returnURL = LeTVUpload.upload(file);
		logger.info("generateHtmlAgreement==orderCode:{}, type:{},fileUrl:{}",order.getOrderCode(),type,returnURL);
		agree.setAgreementUrl(returnURL);

	}

	//封装一个StringBuffer的替换方法
	public StringBuffer replaseStrBuf(StringBuffer agreement,String resStr,String newStr){
		Integer start = agreement.indexOf(resStr);
		if(start>0){
			Integer end = start+resStr.length();
			agreement = agreement.replace(start,end,newStr);
		}
		return agreement;
	}

	/**
	 * 生成vo，后续批量插入
	 */
	@Override
	public PublisherProductAgreement createInvestEntity(InvestorTradeOrder order) {
		PublisherProductAgreement entity = new PublisherProductAgreement();
		entity.setOid(UUIDUtil.creatUUID());
		entity.setProductOid(order.getProductOid());
		entity.setOrderOid(order.getOid());
		entity.setAgreementCode(order.getOrderCode());
		entity.setAgreementName("invest protocol");
		entity.setAgreementType(AgreementTypeEnum.INVESTING.value);
		Date now = new Date();
		entity.setCreateTime(now);
		entity.setUpdateTime(now);
		return entity;
	}

	/**
	 * 生成vo，后续批量插入
	 */
	@Override
	public PublisherProductAgreement createServiceEntity(InvestorTradeOrder order) {
		PublisherProductAgreement entity = new PublisherProductAgreement();
		entity.setOid(UUIDUtil.creatUUID());
		entity.setProductOid(order.getProductOid());
		entity.setOrderOid(order.getOid());
		entity.setAgreementCode(order.getOrderCode());
		entity.setAgreementName("invest protocol");
		entity.setAgreementType(AgreementTypeEnum.SERVICE.value);
		Date now = new Date();
		entity.setCreateTime(now);
		entity.setUpdateTime(now);
		return entity;
	}

	@Override
	public void saveBatch(List<PublisherProductAgreement> entitys) {
		this.publisherProductAgreementMapper.saveBatch(entitys);
	}

	@Override
	public int updateEntity(PublisherProductAgreement entity) {
		return this.publisherProductAgreementMapper.updateByPrimaryKeySelective(entity);

	}
	

	/**
	 * 查询持有人产品协议 入参：tradeOrderOid 订单ID agreementType：AgreementTypeEnum
	 * investing、service
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public AgreementResponse getAgreementInfo(AgreementRequest request) {
		logger.info("getAgreementInfo, orderOid={} type={}", request.getTradeOrderOid(), request.getAgreementType());
		AgreementResponse rep = new AgreementResponse();
		List<Agreement> agreements = new ArrayList<Agreement>();
		rep.setAgreements(agreements);

		String hkey = SysConstant.LECURRENT_REDIS_AGREEMENT_HASH_PRIFIX + request.getTradeOrderOid();

		logger.info("getAgreementInfo from redis, key={}", hkey);

		if (!StringUtil.isEmpty(request.getAgreementType())) {
			String result = redisClient.hgetString(hkey, request.getAgreementType());

			if(null!=result && !result.equals("null")) {
				agreements.add(JSONObject.parseObject(result, Agreement.class));
			}
			
		} else {
			Map<String, String> map = redisClient.hgetAllString(hkey);
			Iterator it = map.entrySet().iterator();

			while (it.hasNext()) {
				Agreement agreement = JSONObject.parseObject(map.get(it.next().toString()), Agreement.class);
				if(agreement!=null){
					agreements.add(agreement);
				}
			}

		}
		
		if (agreements.size() > 0) {
				rep.setAgreements(agreements);
				return rep;
		}else{
			// 无法连接到redis, 直接查询数据库
			logger.info("getAgreementInfo from DB, OrderOid={}", request.getTradeOrderOid());
			
			SearchAgreementByVo vo = new SearchAgreementByVo();
			
			if (!StringUtil.isEmpty(request.getAgreementType())) {
				vo.setAgreementType(request.getAgreementType());
			}
			vo.setAgreementCode(request.getTradeOrderOid());
			List<PublisherProductAgreement> list = publisherProductAgreementMapper.queryAgreementByVo(vo);
			//存入redis
			for (PublisherProductAgreement agree : list) {
				this.putAgreementData2Cache(agree);

			}
			
			for (PublisherProductAgreement entity : list) {
				Agreement ag = new Agreement();
				ag.setAgreementCode(entity.getAgreementCode()==null?"":entity.getAgreementCode());
				ag.setAgreementName(entity.getAgreementName()==null?"":entity.getAgreementName());
				ag.setAgreementType(entity.getAgreementType()==null?"":entity.getAgreementType());
				ag.setAgreementUrl(entity.getAgreementUrl()==null?"":entity.getAgreementUrl());
				ag.setOrderOid(entity.getOrderOid()==null?"":entity.getOrderOid());
				ag.setProductOid(entity.getProductOid()==null?"":entity.getProductOid());
				ag.setUpdateTime(entity.getUpdateTime()==null?new Date():entity.getUpdateTime());
				ag.setCreateTime(entity.getCreateTime()==null?new Date():entity.getCreateTime());
				agreements.add(ag);
			}
			rep.setAgreements(agreements);
			return rep;
		}
		

		

		
	}
	
	
	

	/**
	 * 查询产品协议通过VO条件
	 */
	public List<PublisherProductAgreement> queryAgreementByVo(SearchAgreementByVo vo) {

		return publisherProductAgreementMapper.queryAgreementByVo(vo);
	}
	
	private Boolean putAgreementData2Cache( PublisherProductAgreement agree){
		String hkey = SysConstant.LECURRENT_REDIS_AGREEMENT_HASH_PRIFIX + agree.getAgreementCode();

		logger.info("getAgreementInfo from redis, key={}", hkey);

		Map<String, String> redisMap = new HashMap<>();

		redisMap.put("agreementCode", agree.getAgreementCode() == null ? "" : agree.getAgreementCode());
		redisMap.put("agreementName", agree.getAgreementName() == null ? "" : agree.getAgreementName());
		redisMap.put("agreementType", agree.getAgreementType() == null ? "" : agree.getAgreementType());
		redisMap.put("agreementUrl", agree.getAgreementUrl() == null ? "" : agree.getAgreementUrl());
		redisMap.put("orderOid", agree.getOrderOid() == null ? "" : agree.getOrderOid());
		redisMap.put("productOid", agree.getProductOid() == null ? "" : agree.getProductOid());
		redisMap.put("updateTime",agree.getUpdateTime()==null?
				String.valueOf(new Date().getTime()): String.valueOf(agree.getUpdateTime().getTime()));
		redisMap.put("createTime", agree.getCreateTime()==null?
				String.valueOf(new Date().getTime()):String.valueOf(agree.getCreateTime().getTime()));

		redisClient.hset(hkey, agree.getAgreementType(), JsonUtils.writeValue(redisMap));
		
		return true;

	}

	/**
	 * 加载协议
	 */
	public Boolean loadAgreement2Cache() {

		String minutesBefore = profitDiamondService.getMessage("holdCacheMinutesBefore").toString();

		SearchAgreementByVo vo = new SearchAgreementByVo();

		Date date = DateUtil.minuteBefor(new Date(), Integer.valueOf(minutesBefore));

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

		vo.setBeginTime(sf.format(date));
		vo.setUrlNotNull("yes");
		List<PublisherProductAgreement> list = this.queryAgreementByVo(vo);

		for (PublisherProductAgreement agree : list) {

			this.putAgreementData2Cache(agree);

		}

		return true;

	}

}
