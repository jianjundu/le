package com.le.jr.am.profit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.le.jr.am.assetpool.domain.GamAssetpool;
import com.le.jr.am.assetpool.interfaces.GamAssetpoolInterfaces;
import com.le.jr.am.channel.interfaces.SPVInterfaces;
import com.le.jr.am.channel.vo.SPVResp;
import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.order.domain.OrderLog;
import com.le.jr.am.order.domain.enums.OrderStatusEnum;
import com.le.jr.am.order.domain.enums.OrderTypeEnum;
import com.le.jr.am.order.domain.enums.TaskCodeEnum;
import com.le.jr.am.order.domain.order.vo.OrderBaseVo;
import com.le.jr.am.order.interfaces.OrderInterface;
import com.le.jr.am.product.common.util.CreateFundCodeUtil;
import com.le.jr.am.product.common.util.PageUtil;
import com.le.jr.am.product.common.util.ProductDecimalFormat;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.product.common.util.ValidateUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.product.domain.enums.ProductStateEnum;
import com.le.jr.am.product.domain.enums.ProductSubTypeEnum;
import com.le.jr.am.product.domain.enums.ProductTypeEnum;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.common.util.DecimalUtil;
import com.le.jr.am.profit.common.util.FormulaUtil;
import com.le.jr.am.profit.common.util.RocketMQMessageUtil;
import com.le.jr.am.profit.dao.HoldMapper;
import com.le.jr.am.profit.dao.VolumeConfirmLogMapper;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.VolumeConfirmLog;
import com.le.jr.am.profit.domain.constant.SysConstant;
import com.le.jr.am.profit.domain.enums.ErrorCodeEnum;
import com.le.jr.am.profit.domain.enums.HoldAccountTypeEnum;
import com.le.jr.am.profit.domain.enums.HoldApartAccrualStatus;
import com.le.jr.am.profit.domain.enums.HoldApartRedeemStatus;
import com.le.jr.am.profit.domain.enums.HoldStatusEnum;
import com.le.jr.am.profit.domain.enums.ProfitErrorEnums;
import com.le.jr.am.profit.domain.enums.RedeemTypeEnum;
import com.le.jr.am.profit.domain.enums.YesOrNoEnum;
import com.le.jr.am.profit.domain.input.HoldListRequest;
import com.le.jr.am.profit.domain.input.HoldRequest;
import com.le.jr.am.profit.domain.input.SearchHoldVo;
import com.le.jr.am.profit.domain.output.HoldDetailRep;
import com.le.jr.am.profit.domain.output.HoldOutput;
import com.le.jr.am.profit.domain.output.HoldQueryRep;
import com.le.jr.am.profit.domain.output.HoldResponse;
import com.le.jr.am.profit.domain.output.InvestResultVo;
import com.le.jr.am.profit.service.CallDubboService;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.am.profit.service.config.ProfitDiamondService;
import com.le.jr.am.profit.service.util.MessageUtils;
import com.le.jr.am.task.domain.JobLock;
import com.le.jr.am.task.domain.SerialTaskReq;
import com.le.jr.am.task.domain.enums.JobLockIdEnum;
import com.le.jr.am.task.domain.enums.JobLockStatusEnum;
import com.le.jr.am.task.interfaces.JobLockInterface;
import com.le.jr.am.task.interfaces.SerialTaskInterface;
import com.le.jr.platform.redis.RedisClient;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.data.Result;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.le.jr.trade.publictools.util.StringUtil;
import com.lejr.argus.client.Argus;
import com.lejr.argus.core.metric.Call;
import com.tstd2.log4j.log.LogTransactionIdManager;

@Service("holdService")
public class HoldServiceImpl implements HoldService {

	private static final Logger logger = LoggerFactory.getLogger(HoldServiceImpl.class);

	@Value("${mq.amprofit.accountant_topic}")
	private String accountTopic;

	@Value("${mq.amprofit.accountant_invest}")
	private String invest;
	
	/**
	 * 编程式事务
	 */
	@Resource
	private PlatformTransactionManager transactionManager;

	@Resource
	private HoldMapper holdMapper;
	
	@Resource
	private VolumeConfirmLogMapper volumeConfirmLogMapper;

	@Resource
	private ProductInterfaces productInterfaces;

	@Resource
	private HoldApartService holdApartService;

	@Resource
	private JobLockInterface jobLockInterface;

	@Resource
	private SerialTaskInterface serialTaskInterface;

	@Resource
	private GamAssetpoolInterfaces gamAssetpoolInterfaces;

	@Resource
	private SPVInterfaces spvInterfaces;

	@Resource
	private OrderInterface orderInterface;

	@Resource
	private ProfitDiamondService profitDiamondService;
	
	@Resource
	private CallDubboService callDubboService;
	
	// MQ 生产者
	@Resource
	private DefaultMQProducer producer;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * redis
     */
    @Resource
    private RedisClient redisClient;

	@Value("${lecurrent.batch.size:1000}")
	private int pageSize = 1000;
	
	// 监控
	private static Call holdServiceInvestCall = Argus.call("am.holdService.invest");

	@Override
	public List<Hold> selectHolds(SearchHoldVo searchHoldVo) throws BizException {

		Map<String, Object> map = new HashMap<>();

		if (!StringUtil.isEmpty(searchHoldVo.getInvestorOid())) {
			map.put("investorOid", searchHoldVo.getInvestorOid());
		}
		if (!StringUtil.isEmpty(searchHoldVo.getProductOid())) {
			map.put("productOid", searchHoldVo.getProductOid());
		}

		if (!StringUtil.isEmpty(searchHoldVo.getSpvOid())) {
			map.put("spvOid", searchHoldVo.getSpvOid());
		}
		if (!StringUtil.isEmpty(searchHoldVo.getAssetPoolOid())) {
			map.put("assetPoolOid", searchHoldVo.getAssetPoolOid());
		}

		if (searchHoldVo.getAccountTypeEnum() != null) {
			map.put("accountType", searchHoldVo.getAccountTypeEnum().value);
		}

		if (searchHoldVo.getHoldStatusEnums() != null) {
			map.put("holdStatuss", searchHoldVo.getHoldStatusEnums());
		}

		return this.holdMapper.selectHolds4Api(map);
	}

	@Override
	public Page<Hold> selectHolds4Api(SearchHoldVo searchHoldVo) throws BizException {

		Map<String, Object> map = new HashMap<>();

		List<Hold> result = new ArrayList<Hold>();

		if (searchHoldVo.getCurrentPageNo() <= 0) {
			searchHoldVo.setCurrentPageNo(0);
		}
		if (searchHoldVo.getPageSize() <= 0) {
			searchHoldVo.setPageSize(10);
		}

		PageUtil.setPageParam(searchHoldVo.getCurrentPageNo(), searchHoldVo.getPageSize(), map);

		if (!StringUtil.isEmpty(searchHoldVo.getInvestorOid())) {
			map.put("investorOid", searchHoldVo.getInvestorOid());
		}
		if (!StringUtil.isEmpty(searchHoldVo.getProductOid())) {
			map.put("productOid", searchHoldVo.getProductOid());
		}

		if (null != searchHoldVo.getProductOids() && searchHoldVo.getProductOids().size() > 0) {

			map.put("productOids", searchHoldVo.getProductOids());
		}

		if (searchHoldVo.getAccountTypeEnum() != null) {
			map.put("accountType", searchHoldVo.getAccountTypeEnum().value);
		}

		if (searchHoldVo.getHoldStatusEnums() != null) {
			map.put("holdStatuss", searchHoldVo.getHoldStatusEnums());
		}

		int count = this.holdMapper.selectCountHoldsApi(map);

		if (count > 0) {
			result = this.holdMapper.selectHolds4Api(map);
		}

		Page<Hold> page = new Page<Hold>();
		page.setDataList(result);
		page.setTotalCount(count);

		return page;
	}

	/**
	 * 根据投资人和产品Id查询持有人
	 */
	@Override
	public Hold findByInvestorOidAndProduct(String investorOid, String productOid) throws BizException {

		logger.info("findByInvestorOidAndProduct:investorOid:{} productOid:{}", investorOid, productOid);

		Hold hold = this.holdMapper.findByInvestorOidAndProduct(investorOid, productOid);

		if (null != hold) {
			return hold;
		}
		logger.error("findByInvestorOidAndProduct:investorOid:{} productOid:{}", investorOid, productOid);
		return null;

	}

	/**
	 * 投资 持有人持有投资额增加
	 * 
	 * @param tradeOrder
	 * @return
	 */
	public InvestResultVo invest(InvestorTradeOrder tradeOrder) throws BizException {
		logger.info("invest orderCode:{}", JsonUtils.writeValue(tradeOrder));
		
		//参数校验
		this.check4Invest(tradeOrder);
		
		//argus 监控
		Call.Context context = holdServiceInvestCall.context();
		
		Hold hold;
		Message<GamAssetpool> assetpoolMessage;
		Product product = tradeOrder.getProduct();

		// 根据投资者Id和产品Id查询持有人
		hold = this.holdMapper.findByInvestorOidAndProduct(tradeOrder.getInvestorOid(), tradeOrder.getProductOid());
				
		//获取产品所属资产池
		assetpoolMessage = callDubboService.callGetAssetpoolByOid(product.getAssetPoolOid());
		
		//判断是否是交易所产品
		String tradeStructure=assetpoolMessage.getData().getTradeStructure();
		boolean flag = GamAssetpool.FIRSTRAISEAFTERVOTE.equals(tradeStructure);
		
		// 事物控制开始，开始添加 增加事务
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(definition);

		try {
			// 校验SPV持仓 内部异常抛出
			this.checkSpvHold4Invest(tradeOrder);
			
			// 校验用户所购产品最大持仓
			this.checkMaxHold4Invest(hold, tradeOrder, product);
			
			if (null == hold) {
				//插入新持仓记录
				hold=this.createHold4Invest(tradeOrder, product,flag);
			} else {
				//更新持仓
				this.updateHold4Invest(hold, tradeOrder, product);
			}
			// 增加分仓记录
			this.holdApartService.createInvestApart(tradeOrder, hold);
			
			// 提交事务
			transactionManager.commit(status);
		} catch (BizException e) {
			// 回滚事务
			logger.error("invest failed", e);
			context.fail(e);
			
			transactionManager.rollback(status);
			throw e;
		} catch (Exception e) {
			// 回滚事务
			logger.error("invest failed", e);
			context.fail(e);
			transactionManager.rollback(status);
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "申购操作异常", e);
		}
		
		InvestResultVo resultVo = new InvestResultVo();
		resultVo.setResult(true);
		resultVo.setFundCode(hold.getFundCode());
		//argus监控
		context.success();
		return resultVo;
	}
	
	private void check4Invest(InvestorTradeOrder tradeOrder){
		if (!OrderTypeEnum.INVEST.value.equals(tradeOrder.getOrderType())
				|| !ProductTypeEnum.PRODUCTTYPE_02.getValue().equals(tradeOrder.getProduct().getType())) {
			logger.error("invest 只受理投资单 20001:orderCode{}", tradeOrder.getOrderCode());
			throw new BizException(20001, "只受理投资单(CODE:20001)");
		}
		BigDecimal volume=tradeOrder.getOrderVolume();
		// 投资份额不能小于0
		if (volume==null || volume.compareTo(BigDecimal.ZERO) <= 0) {
			logger.error("flatWare:volume is little than zero:orderCode{},volume{}", tradeOrder.getOrderCode(), volume);
			throw new BizException(20020, "");
		}

	}
	
	private void updateHold4Invest(Hold hold,InvestorTradeOrder tradeOrder,Product product){
		// 直接更新持有人持仓
		int i = this.holdMapper.invest(hold.getOid(), product.getNetUnitShare(), tradeOrder.getOrderVolume());
		if (i <= 0) {
			logger.error("invest update fail or no record orderOid:{}", tradeOrder.getOid());
			throw new BizException(Code.RESULTSETISNULL.getValue(), "更新总仓持有人信息失败");
		}
	}
	
	private Hold createHold4Invest(InvestorTradeOrder tradeOrder,Product product,Boolean flag){
		Hold hold = new Hold();
		// 生成主键
		hold.setOid(UUIDUtil.creatUUID());
		hold.setProductOid(tradeOrder.getProductOid()); // 所属理财产品
		hold.setPublisherOid(tradeOrder.getPublisherOid());// 所属发行人
		hold.setInvestorOid(tradeOrder.getInvestorOid()); // 所属投资人
		hold.setAssetpoolOid(product.getAssetPoolOid());
		hold.setTotalHoldVolume(tradeOrder.getOrderVolume());// 持仓总份额
		hold.setInvestTotalVolume(tradeOrder.getOrderVolume());// 累计投资份额
		hold.setAccountType(HoldAccountTypeEnum.INVESTOR.value);
		hold.setRedeemableHoldVolume(BigDecimal.ZERO);// 可赎回份额
		hold.setLockRedeemHoldVolume(tradeOrder.getOrderVolume());// 赎回锁定份额
		if (null != product.getMaxHold() && product.getMaxHold().compareTo(BigDecimal.ZERO) != 0) { // 等于0，表示无限制
			hold.setMaxHoldVolume(tradeOrder.getOrderVolume());
		}
		hold.setAccruableHoldVolume(BigDecimal.ZERO);
		hold.setLastConfirmDate(tradeOrder.getOrderTime());
		hold.setValue(tradeOrder.getOrderAmount()); // 价值(单位：元)
		hold.setHoldStatus(HoldStatusEnum.HOLDING.value);
		if (flag) {
			String fundCode=this.createFundCode(product);
			hold.setFundCode(fundCode);
		}
		//保存hold
		this.saveEntity(hold);
		return hold;
	}
	
	private String createFundCode(Product product){
		String fundCode = product.getMaxFundCode();
		Message<SPVResp> spvMessage = spvInterfaces.getByOid(product.getSpvOid());
		logger.info("spvMessage:{}", JsonUtils.writeValue(spvMessage));
		if (!ValidateUtil.interfaceValidate(spvMessage)) {
			throw new BizException(Code.FAIL.getValue(), "spvInterfaces.getByOid dubbo error...");
		}
		SPVResp data = spvMessage.getData();
		Integer packageNum = data.getPackageNum();
		if(packageNum==null){
			packageNum=200;
		}
		//初始化为false，当更改一次小包数，改为true，标识生成基金代码从下一个开始
		boolean updatePackNumFlag = data.isUpdatePackNumFlag();
		logger.info("packageNum:" + packageNum);
		Integer holdNum = holdMapper.findHoldNumByPid(product.getOid());
		logger.info("holdNum:" + holdNum);
		if (holdNum % packageNum == 0||updatePackNumFlag) {
			fundCode = CreateFundCodeUtil.createFundCode(fundCode);// B000001
			// 更新产品
			logger.info("productOid:" + product.getOid() + " fundCode:" + fundCode);
			Message<Integer> integerMessage = productInterfaces.updateMaxFundCode(product.getOid(), fundCode);
			logger.info("integerMessage:{}", JsonUtils.writeValue(integerMessage));
			if (!ValidateUtil.interfaceValidateResult(integerMessage) || integerMessage.getData() == 0) {
				logger.error("productInterfaces.updateMaxFundCode dubbo error...");
				throw new BizException(Code.FAIL.getValue(), "productInterfaces.updateMaxFundCode dubbo error...");
			}
			//修改小包数量之后，updatePackNumFlag为true重置为false
			logger.info("updatePackNumFlag:" + updatePackNumFlag);
			if(updatePackNumFlag){
				//更新spv已经更改过小包数量标识
				Message<Boolean> booleanMessage = spvInterfaces.updatePackNumFlag(data.getOid());
				logger.error("booleanMessage：{}",JsonUtils.writeValue(booleanMessage));
				if (!ValidateUtil.interfaceValidateResult(booleanMessage)) {
					logger.error("spvInterfaces.updatePackNumFlag dubbo error...");
					throw new BizException(Code.FAIL.getValue(), "spvInterfaces.updatePackNumFlag dubbo error...");
				}
			}
		}
		logger.info("new fundCode:" + fundCode);
		return fundCode;
	}
	
	@Override
	public void redeem(InvestorTradeOrder order,String submitType) {
		logger.info("redeem orderCode:{},submitType:{}", JsonUtils.writeValue(order),submitType);
		
		//参数校验
		this.check4RedeemParam(order);
		
		Hold hold;
		HoldApart apart=null;
		
		Product product=order.getProduct();
		Byte productType=product.getSubType();
		
		//乐自选或乐定活获取对应分仓
		if(productType==ProductSubTypeEnum.SELECTED.getValue()||productType==ProductSubTypeEnum.FIXED_CURRENT.getValue()){
			apart = this.getHoldApart4Redeem(order);
		}
		
		// 事物控制开始，开始添加 增加事务
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(definition);
		
		try{
			//获取合仓记录(加悲观锁)
			hold=this.getHold4Redeem(order.getInvestorOid(),order.getProductOid());
			
			//单人单日赎回下限校验
			this.checkMin4Redeem(hold,order);
			
			//单人单日赎回上限校验
			this.checkMax4Redeem(hold,order,submitType);
			
			//更新hold
			this.updateHold4Redeem(hold,order);
			
			//赎回当日不计息
			if(product.getRedeemNeedInterest()==(byte)0){
				//赎回对应分仓全部金额
				if(productType==ProductSubTypeEnum.SELECTED.getValue()||productType==ProductSubTypeEnum.FIXED_CURRENT.getValue()){
					this.holdApartService.flatWare4Accept2RedeemByHoldNotInterest(apart);
				}else{//普通单子处理
					this.holdApartService.flatWare4Accept2RedeemNotInterest(order);
				}
			}
			transactionManager.commit(status);
		}catch(Exception e){
			// 回滚事务
			logger.error("redeem failed,orderCode:"+order.getOrderCode(), e);
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	private HoldApart getHoldApart4Redeem(InvestorTradeOrder order){
		String orderOid=order.getOriginOrderOid();
		HoldApart apart = this.holdApartService.findHoldApartByOrderId(orderOid);
		if(apart==null){
			logger.error("没有该持仓记录,orderOid:"+orderOid);
			throw new BizException(ErrorCodeEnum.NO_HOLDAPART.value, ErrorCodeEnum.NO_HOLDAPART.desc);
		}
		if(!apart.getProductOid().equals(order.getProductOid())){
			logger.error("持仓记录非该产品,orderOid:"+orderOid);
			throw new BizException(ErrorCodeEnum.HOLDAPART_PRODUCT_DIFF.value, ErrorCodeEnum.HOLDAPART_PRODUCT_DIFF.desc);
		}
		if(!apart.getRedeemStatus().equals(YesOrNoEnum.YES.value)){
			logger.error("分仓状态为不可赎回,orderOid:"+orderOid);
			throw new BizException(ErrorCodeEnum.HOLDAPART_REDEEMSTATUS_NO.value, ErrorCodeEnum.HOLDAPART_REDEEMSTATUS_NO.desc);
		}
		if(apart.getInvestVolume().compareTo(order.getOrderVolume())!=0){
			logger.error("赎回金额不等于可赎回金额,orderOid:"+orderOid);
			throw new BizException(ErrorCodeEnum.MUST_REDEEM_ALL.value, ErrorCodeEnum.MUST_REDEEM_ALL.desc);
		}
		//乐定活
		if(order.getProduct().getSubType()==ProductSubTypeEnum.FIXED_CURRENT.getValue()){
			ProductIncomeReward reward=callDubboService.callSelectProductRewardByOid(apart.getLockRewardOid()).getData();
			BigDecimal dRadio=ProductDecimalFormat.convertToDratio(reward.getRedeemRatio(), order.getProduct().getIncomeCalcBasis());
			BigDecimal formulaVolume=FormulaUtil.getPunishVolume4FixedCurrent(apart.getSnapshotVolume(), dRadio, reward.getEndDate(), apart.getBeginAccuralDate());
			if(formulaVolume.compareTo(order.getPunishVolume())!=0){
				logger.error("赎回金额不等于可赎回金额,orderOid:{},formulaVolume:{},punishVolume:{}",orderOid,formulaVolume,order.getPunishVolume());
				throw new BizException(ErrorCodeEnum.PUNISHVOLUME_IS_WRONG.value, ErrorCodeEnum.PUNISHVOLUME_IS_WRONG.desc);
			}
		}
		return apart;
	}
	
	private void updateHold4Redeem(Hold hold,InvestorTradeOrder order) {
		
		BigDecimal orderVolume=order.getOrderVolume();
		hold.setLockRedeemHoldVolume(hold.getLockRedeemHoldVolume().add(orderVolume));
		hold.setRedeemableHoldVolume(hold.getRedeemableHoldVolume().subtract(orderVolume));
		hold.setDayRedeemVolume(hold.getDayRedeemVolume().add(orderVolume));
		hold.setUpdateTime(new Date());
		
		holdMapper.updateByPrimaryKeySelective(hold);
	}

	private void checkMax4Redeem(Hold hold,InvestorTradeOrder order,String submitType) {
		Product product = order.getProduct();

		//如果赎回金额大于可赎回金额
        if(hold.getRedeemableHoldVolume().compareTo(order.getOrderVolume())<0){
        	logger.error("redeemLock 20004:orderCode{}", order.getOrderCode());
    		throw new BizException(20004, "可赎回份额不足(CODE:20004)");
        }
        
		//补单且单子不是当天的不做校验
		if(null!=submitType && submitType.equals(RedeemTypeEnum.RESUBMITREDEEM.value)){
			if (!DateUtil.isEqualDay(order.getOrderTime(), new Date())) {
				return;
			}
		}
		// 产品单人单日赎回上限等于0时，表示无上限
		if (null == product.getSingleDailyMaxRedeem() || product.getSingleDailyMaxRedeem().compareTo(BigDecimal.ZERO) == 0
				|| !ProductStateEnum.RAISING.getValue().equals(product.getState())) {
			return;
		}
		if((hold.getDayRedeemVolume().add(order.getOrderVolume())).compareTo(product.getSingleDailyMaxRedeem())>0){
			logger.error("redeem4DayRedeemVolume 超过产品单人单日赎回上限30032:orderCode{}", order.getOrderCode());
			throw new BizException(30032, "超过产品单人单日赎回上限(CODE:30032)");
		}
	}

	private void check4RedeemParam(InvestorTradeOrder tradeOrder){
		if (!OrderTypeEnum.NORMAL_REDEEM.value.equals(tradeOrder.getOrderType())
				|| !ProductTypeEnum.PRODUCTTYPE_02.getValue().equals(tradeOrder.getProduct().getType())) {
			logger.error("redeem 只受理赎回单 20001:orderCode{}", tradeOrder.getOrderCode());
			throw new BizException(20001, "只受理赎回单(CODE:20001)");
		}
		BigDecimal volume=tradeOrder.getOrderVolume();
		// 赎回份额不能小于0
		if (volume==null || volume.compareTo(BigDecimal.ZERO) <= 0) {
			logger.error("redeem:volume is little than zero:orderCode{},volume{}", tradeOrder.getOrderCode(), volume);
			throw new BizException(20020, "");
		}
		Product product=tradeOrder.getProduct();
		
		String originOrderOid=tradeOrder.getOriginOrderOid();
		//乐定活
		if(product.getSubType()==ProductSubTypeEnum.FIXED_CURRENT.getValue()){
			if(originOrderOid==null){
				logger.error("redeem:赎回对应投资单号为空!orderCode:{}",tradeOrder.getOrderCode());
				throw new BizException(ErrorCodeEnum.ORIGINORDEROID_IS_NULL.value,ErrorCodeEnum.ORIGINORDEROID_IS_NULL.desc);
			}
			if(tradeOrder.getPunishVolume()==null){
				logger.error("redeem:赎回对应罚金为null!orderCode:{}",tradeOrder.getOrderCode());
				throw new BizException(ErrorCodeEnum.PUNISHVOLUME_IS_NULL.value, ErrorCodeEnum.PUNISHVOLUME_IS_NULL.desc);
			}
		}else if(product.getSubType()==ProductSubTypeEnum.SELECTED.getValue()){
			if(originOrderOid==null){
				logger.error("redeem:赎回对应投资单号为空!orderCode:{}",tradeOrder.getOrderCode());
				throw new BizException(ErrorCodeEnum.ORIGINORDEROID_IS_NULL.value,ErrorCodeEnum.ORIGINORDEROID_IS_NULL.desc);
			}
		}
		
	}
	

	private Hold getHold4Redeem(String investorOid,String productOid) {
		Hold hold=this.holdMapper.selectByInvestorOidAndProductOid4Update(investorOid, productOid);
		if(hold==null){
			logger.info("合仓记录不存在,investorOid:{},productOid:{}",investorOid,productOid);
			throw new BizException(31001, "合仓记录不存在");
		}
		return hold;
	}

	private void checkMin4Redeem(Hold hold,InvestorTradeOrder order) {
		Product product = order.getProduct();
		
        if (ProductStateEnum.CLEARING.getValue().equals(product.getState())) { //产品清盘中不做以下校验
            return;
        }
        
        //如果赎回金额等于可赎回金额即使低于项目最低可赎回金额，也可以全部赎回，故可不做校验
        if(hold.getRedeemableHoldVolume().compareTo(order.getOrderVolume())==0){
        	return;
        }
		if (null != product.getMinRredeem() && product.getMinRredeem().compareTo(BigDecimal.ZERO) != 0) {
            if (order.getOrderVolume().compareTo(product.getMinRredeem()) < 0) {
            	throw new BizException(30013, "不满足单笔赎回下限(CODE:30013)");
            }
        }
        if (null != product.getAdditionalRredeem() && product.getAdditionalRredeem().compareTo(BigDecimal.ZERO) != 0) {
            if (null != product.getMinRredeem() && product.getMinRredeem().compareTo(BigDecimal.ZERO) != 0) {
                logger.info("order:{},min:{},add:{}",order.getOrderVolume(),product.getMinRredeem(),product.getAdditionalRredeem());
            	if ((order.getOrderVolume().subtract(product.getMinRredeem())).remainder(product.getAdditionalRredeem()).compareTo(BigDecimal.ZERO) != 0) {
                    throw new BizException(30039, "不满足赎回追加份额(CODE:30039)");
                }
            } else {
                if (order.getOrderVolume().remainder(product.getAdditionalRredeem()).compareTo(BigDecimal.ZERO) != 0) {
                    throw new BizException(30039, "不满足赎回追加份额(CODE:30039)");
                }
            }
        }
	}


	/**
	 * 订单作废后，扣除持有人名录和分仓中的锁定份额
	 * 
	 * @param tradeOrder
	 */
	public Boolean abandonInvestOrder(InvestorTradeOrder tradeOrder) throws BizException {

		// 重置日志的线程号
		LogTransactionIdManager.resetTransactionId();

		logger.info("abandonInvestOrder:orderCode{}", tradeOrder.getOrderCode());
		// 事物控制开始，开始添加 增加事务
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(definition);
		try {

			// SPV持仓
			this.updateSpvHold4InvestAbandon(tradeOrder.getProductOid(), HoldAccountTypeEnum.SPV.value, tradeOrder.getOrderVolume());
			// 更新持仓最大持仓份额
			this.updateMaxHold4InvestAbandon(tradeOrder.getInvestorOid(), tradeOrder.getProduct(), tradeOrder.getOrderVolume());

			// 持仓处理
			// this.abandonInvestOrder(tradeOrder);

			Product product = tradeOrder.getProduct();

			// 暂时在份额确认之前不能再赎回，直接从锁定份额里面扣除就可以了
			int i = this.holdMapper.invest4Abandon(tradeOrder.getOrderVolume(), product.getNetUnitShare(), tradeOrder.getInvestorOid(),
					tradeOrder.getProductOid());
			if (i < 1) {
				// error.define[20017]=废单份额异常(CODE:20017)

				logger.error("redeem:orderCode:{}", tradeOrder.getOrderCode());
				throw new BizException(20017, "废单份额异常(CODE:20017)");

			}
			this.holdApartService.abandon(tradeOrder.getOid());

		} catch (BizException e) {

			// 回滚事务
			logger.error("abandonInvestOrder failed", e);
			transactionManager.rollback(status);
			throw e;

		} catch (Exception e) {

			// 回滚事务
			logger.error("abandonInvestOrder failed", e);
			transactionManager.rollback(status);
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "赎回操作异常", e);
		}

		// 提交事务
		transactionManager.commit(status);
		return true;

	}

	/**
	 * 根据分仓更新合仓可计息份额
	 */
	private void unlockAccrualItem(HoldApart entity) throws BizException {

		// 重置日志的线程号
		LogTransactionIdManager.resetTransactionId();

		logger.info("unlockAccrualItem:holdApartId{}", entity.getHoldOid());

		// 事物控制开始，开始添加 增加事务
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(definition);

		try {
			int i = this.holdMapper.unlockAccrual(entity.getHoldOid(), entity.getInvestVolume());
			if (i < 1) {
				logger.error("========根据分仓更新合仓可计息份额" + entity.getOid() + "处理失败");
				throw new BizException(ProfitErrorEnums.UPDATEERROR.code, ProfitErrorEnums.UPDATEERROR.value);
			}
			// 更新分仓可计息
			if (this.holdApartService.unlockAccrual(entity.getOid(),entity.getInvestVolume()) < 1) {
				logger.error("========根据分仓更新分仓可计息份额" + entity.getOid() + "处理失败");
				throw new BizException(ProfitErrorEnums.UPDATEERROR.code, ProfitErrorEnums.UPDATEERROR.value);
			}
			
				// 提交事务
		     transactionManager.commit(status);
		} catch (Exception e) {

			// 回滚事务
			logger.error("unlockAccrualItem failed {}", e.getMessage());
			transactionManager.rollback(status);
			
			return;
			//throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "解锁可计息份额异常", e);
		}

	

	}

	/**
	 * 根据分仓更新合仓可赎回份额
	 * 
	 * @param entity
	 */
	private void unlockRedeemItem(HoldApart entity) throws BizException {

		// 重置日志的线程号
		LogTransactionIdManager.resetTransactionId();

		logger.info("unlockRedeemItem:holdApartId{}", entity.getHoldOid());

		// 事物控制开始，开始添加
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(definition);

		try {

			int i = this.holdMapper.unlockRedeem(entity.getHoldOid(), entity.getInvestVolume());
			if (i < 1) {
				logger.error("unlockRedeemItem 根据分仓更新合仓可赎回份额" + entity.getOid() + "处理失败");
				throw new BizException(ProfitErrorEnums.UPDATEERROR.code, ProfitErrorEnums.UPDATEERROR.value);
			}
			// 解锁分仓赎回份额
			if (this.holdApartService.unlockRedeem(entity.getOid()) < 1) {
				logger.error("unlockRedeemItem 根据分仓更新分仓可赎回份额");
				throw new BizException(ProfitErrorEnums.UPDATEERROR.code, ProfitErrorEnums.UPDATEERROR.value);
			}
			// 提交事务
			transactionManager.commit(status);
		} catch (Exception e) {
			// 回滚事务
			logger.error("unlockRedeemItem failed{}", e.getMessage());
			transactionManager.rollback(status);
			//throw e;
		} 
		
		

	}

	

	/**
	 * 解锁赎回份额
	 */
	@Override
	public void unlockRedeem() throws BizException {

		SerialTaskReq<String> req = new SerialTaskReq<String>();
		req.setTaskCode(TaskCodeEnum.UNLOCK_REDEEM.value);
		req.setTaskParams(DateUtil.getCurrentDate());
		this.serialTaskInterface.createSerialTask(req);

	}

	/**
	 * 解锁分仓赎回份额
	 */
	@Override
	public void unlockRedeemDo() throws BizException {

		// 重置日志的线程号
		LogTransactionIdManager.resetTransactionId();

		logger.info("unlockRedeemDo:date{}", new Date());

		JobLock jobLock = null;
		String jobStatus = JobLockStatusEnum.DONE.value;
		String jobMessage = "OK";
		try {
			Message<JobLock> msgJobLock = jobLockInterface.queryAndDealJobLock(DateUtil.getCurrentDate(), JobLockIdEnum.UNLOCKREDEEM.value);

			if(!ValidateUtil.interfaceValidate(msgJobLock)){
				logger.error("unlockRedeemDo : 创建任务失败");
				return ;
			}
			jobLock = msgJobLock.getData();
			
		} catch (Exception e) {
			logger.error("unlockRedeemDo queryAndDealJobLock exception", e);
			return;
		}
		try {
			String lastOid = "0";
			Date cur = DateUtil.getSqlDate();

			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

			while (true) {
				List<HoldApart> list = this.holdApartService.findByBeforeBeginRedeemDateInclusive(sf.format(cur), lastOid);
				
				for (HoldApart entity : list) {
					logger.info("unlockRedeemDo:holdOid=={},investVolume=={}",entity.getHoldOid(),entity.getInvestVolume());
					lastOid = entity.getOid();
					this.unlockRedeemItem(entity);

				}
				if (list.size() < pageSize) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("unlockRedeem batchCode {}, status {}, jobMsg {} ", jobLock.getBatchCode(), jobLock.getJobStatus(),
					jobLock.getJobMessage());
			jobMessage = e.getMessage();
			jobStatus = JobLockStatusEnum.FAIL.value;
		}

		jobLock.setJobStatus(jobStatus);
		jobLock.setJobMessage(jobMessage);
		jobLock.setBatchEndTime(DateUtil.getSqlCurrentDate());
		this.jobLockInterface.save(jobLock);

	}

	/**
	 * 解锁可计息
	 */

	@Override
	public void unlockAccrual() throws BizException {

		try{
		SerialTaskReq<String> req = new SerialTaskReq<String>();
		req.setTaskCode(TaskCodeEnum.UNLOCK_ACCRUAL.value);
		req.setTaskParams(DateUtil.getCurrentDate());
		this.serialTaskInterface.createSerialTask(req);
		}catch(Exception e){
			logger.error("unlockAccrual add serialTask fail ...");
			
			throw new BizException("解锁可计息失败");
		}

	}

	/**
	 * 解锁可计息 执行taskOid
	 */

	@Override
	public void unlockAccrualDo() throws BizException {

		// 重置日志的线程号
		LogTransactionIdManager.resetTransactionId();

		logger.info("unlockAccrualDo" + new Date());

		JobLock jobLock = null;
		String jobStatus = JobLockStatusEnum.DONE.value;
		String jobMessage = "OK";
		try {
			Message<JobLock> msgJobLock = jobLockInterface.queryAndDealJobLock(DateUtil.getCurrentDate(), JobLockIdEnum.UNLOCKACCRUAL.value);
			
			if(!ValidateUtil.interfaceValidate(msgJobLock)){
				logger.error("unlockAccrualDo : 创建任务失败");
				return ;
			}
			jobLock = msgJobLock.getData();
		} catch (Exception e) {
			logger.error("unlockAccrualDo e: 创建任务失败", e);
			return;
		}
		try {
			String lastOid = "0";
			Date cur = DateUtil.getSqlDate();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

			while (true) {
				List<HoldApart> list = this.holdApartService.findByBeforeBeginAccuralDateInclusive(sf.format(cur), lastOid);
				for (HoldApart entity : list) {
					lastOid = entity.getOid();
					this.unlockAccrualItem(entity);

				}
				if (list.size() < pageSize) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("unlockAccrual batchCode {}, status {}, jobMsg {} ", jobLock.getBatchCode(), jobLock.getJobStatus(),
					jobLock.getJobMessage());
			jobMessage = e.getMessage();
			jobStatus = JobLockStatusEnum.FAIL.value;
		}

		jobLock.setJobStatus(jobStatus);
		jobLock.setJobMessage(jobMessage);
		jobLock.setBatchEndTime(DateUtil.getSqlCurrentDate());
		this.jobLockInterface.updateStatus4Lock(jobLock.getOid(), jobLock.getJobStatus());

	}
	
	@Override
	public Boolean saveEntity(Hold hold){
	Date currentDate = new Date();
		hold.setUpdateTime(currentDate);
		hold.setCreateTime(currentDate);
		int i = holdMapper.insert(hold);
		if(i <=0){
			logger.error("invest:add hold record fail,{}", JsonUtils.writeValue(hold));
			throw new BizException(Code.RESULTSETISNULL.getValue(), "add hold record fail");
		}
		
		return true;
	}

	@Override
	public Boolean updateEntity(Hold hold) throws BizException {

		int i = holdMapper.updateByPrimaryKeySelective(hold);

		return i > 0 ? true : false;

	}

	@Override
	public Page<HoldQueryRep> holdMng(SearchHoldVo searchHoldVo) throws BizException {

		logger.info("holdMng:holdjson=={}", JsonUtils.writeValue(searchHoldVo));

		Page<HoldQueryRep> pagesRep = new Page<HoldQueryRep>();

		List<HoldQueryRep> list = new ArrayList<>();

		try {
			Page<Hold> page = this.selectHolds4Api(searchHoldVo);

			List<Hold> cas = page.getDataList();

			if (cas != null && page.getTotalCount() > 0 && cas.size() > 0) {

				for (Hold hold : cas) {
					HoldQueryRep queryRep = new HoldQueryRep();

					Product product = productInterfaces.selectProductByOid(hold.getProductOid()).getData();

					BigDecimal netUnitShare = product.getNetUnitShare();
					queryRep.setHoldOid(hold.getOid()); // 持仓OID
					queryRep.setProductOid(product.getOid()); // 产品OID
					queryRep.setProductCode(product.getCode()); // 产品编号
					queryRep.setProductName(product.getName()); // 产品名称
					queryRep.setExpAror(product.getExpAror().toString() + product.getExpArorSec().toString()); // 预期收益率
					queryRep.setLockPeriod(product.getLockPeriodDays()); // 锁定期

					queryRep.setTotalHoldVolume(hold.getTotalHoldVolume().multiply(netUnitShare)); // 持仓总份额
					queryRep.setRedeemableHoldVolume(hold.getRedeemableHoldVolume().multiply(netUnitShare)); // 可赎回份额
					queryRep.setLockRedeemHoldVolume(hold.getLockRedeemHoldVolume().multiply(netUnitShare)); // 赎回锁定份额
					queryRep.setValue(hold.getValue()); // 最新市值
					queryRep.setHoldTotalIncome(hold.getHoldTotalIncome()); // 累计收益
					queryRep.setHoldYesterdayIncome(hold.getHoldYesterdayIncome()); // 昨日收益
					queryRep.setExpectIncome(hold.getExpectIncome()); // 预期收益
					queryRep.setLastConfirmDate(hold.getLastConfirmDate()); // 份额确认日期
					queryRep.setHoldStatus(hold.getHoldStatus()); // 持仓状态
					queryRep.setHoldStatusDisp(HoldStatusEnum.getInstance(hold.getHoldStatus()).name); // 持仓状态disp
					list.add(queryRep);
				}
			}
			pagesRep.setTotalCount(page.getTotalCount());
			pagesRep.setDataList(list);
		} catch (Exception e) {
			throw new BizException(Code.RESULTSETISNULL.getValue(), e.getMessage());
		}
		return pagesRep;

	}

	@Override
	public Hold findByOid(String holdOid) throws BizException {

		logger.info("findByOid :holdOid{}", holdOid);

		Hold hold = this.holdMapper.selectByPrimaryKey(holdOid);
		if (null == hold) {
			logger.error("findByOid is not exist :holdOid{}", holdOid);
			throw new BizException(Code.RESULTSETISNULL.getValue(), "持仓不存在");
		}
		return hold;
	}

	@Override
	public List<Hold> findByProduct(String productOid) throws BizException {

		logger.info("findByProduct :productOid{}", productOid);

		return holdMapper.findByProduct(productOid);
	}

	/**
	 * 活期计息
	 * 
	 * @param interest
	 *            收益金额
	 * @param addHoldVolume
	 *            收益产生的份额
	 * @param netUnitShare
	 *            单位净值
	 * @param incomeDate
	 *            份额确认日期
	 * @param hoid
	 *            持仓Oid
	 */

	@Override
	public void currentCalc(BigDecimal interest, BigDecimal addHoldVolume, BigDecimal netUnitShare, Date incomeDate, String hoid)
			throws BizException {

		logger.info("currentCalc interest{}addHoldVolume{}netUnitShare{}incomeDate{}holdOid{}", interest, addHoldVolume, netUnitShare,
				incomeDate, hoid);
		try {
			this.holdMapper.currentCalc(interest, addHoldVolume, netUnitShare, incomeDate, hoid);
		} catch (Exception t) {
			logger.error("<<currentCalc-----发行人-持有人手册：{}活期计息失败----->>", hoid);
		}

	}

	@Override
	public List<Hold> findByProductHoldStatus(String productOid, String holdStatus, String holdOid, String incomeDate) throws BizException {
		logger.info("currentCalc productOid{}, holdStatus{}, holdOid{}, HoldAccountTypeEnum{}, incomeDate{}", productOid, holdStatus, holdOid,
				HoldAccountTypeEnum.INVESTOR.value, incomeDate);
		return this.holdMapper.findByProductAndHoldStatus(productOid, holdStatus, holdOid, HoldAccountTypeEnum.INVESTOR.value, incomeDate);
	}

	@Override
	public Boolean updateHold4Interest(String holdOid, BigDecimal holdInterestVolume, BigDecimal holdInterestAmount,
			BigDecimal lockHoldInterestVolume, BigDecimal lockHoldInterestAmount, BigDecimal netUnitAmount, Date incomeDate,
			BigDecimal holdInterestBaseAmount, BigDecimal holdInterestRewardAmount,BigDecimal holdInterestMarketingAmount) throws BizException {

		logger.info(
				"updateHold4Interest holdOid{}, holdInterestVolume{}, holdInterestAmount{}, netUnitAmount{}, incomeDate{},holdInterestBaseAmount{}, holdInterestRewardAmount{}, holdInterestMarketingAmount：{}",
				holdOid, holdInterestVolume, holdInterestAmount, netUnitAmount, incomeDate, holdInterestBaseAmount, holdInterestRewardAmount,holdInterestMarketingAmount);

		int i = this.holdMapper.updateHold4Interest(holdOid, holdInterestVolume, holdInterestAmount, lockHoldInterestVolume,
				lockHoldInterestAmount, netUnitAmount, incomeDate, holdInterestBaseAmount, holdInterestRewardAmount,holdInterestMarketingAmount);
		if (i < 1) {
			logger.error(
					"updateHold4Interest holdOid{}, holdInterestVolume{}, holdInterestAmount{}, netUnitAmount{}, incomeDate{},holdInterestBaseAmount{}, holdInterestRewardAmount{}, holdInterestMarketingAmount：{}",
					holdOid, holdInterestVolume, holdInterestAmount, netUnitAmount, incomeDate, holdInterestBaseAmount,
					holdInterestRewardAmount,holdInterestMarketingAmount);
			;
			throw new BizException(Code.RESULTSETISNULL.getValue(), "计息失败");
		}
		return true;
	}

	/**
	 * 获取持仓中的spv信息，如果不存在则创建
	 */
	@Override
	public Hold getAssetPoolSpvHold(String assetPoolOid, String spvOid, String productOid) throws BizException {

		SearchHoldVo vo = new SearchHoldVo();
		vo.setProductOid(productOid);
		vo.setSpvOid(spvOid);
		vo.setAccountTypeEnum(HoldAccountTypeEnum.SPV);
		vo.setAssetPoolOid(assetPoolOid);

		List<Hold> list = this.selectHolds(vo);

		if (null != list && list.size() > 0) {
			return list.get(0);
		} else {
			Hold hold = new Hold();
			hold.setOid(UUIDUtil.creatUUID());
			hold.setAssetpoolOid(assetPoolOid);
			hold.setPublisherOid(spvOid);
			hold.setProductOid(productOid);
			hold.setAccountType(HoldAccountTypeEnum.SPV.value);
			hold.setHoldStatus(HoldStatusEnum.HOLDING.value);
			this.saveEntity(hold);
			return hold;
		}
	}

	private Boolean checkSpvHold4Invest(InvestorTradeOrder tradeOrder) throws BizException {
		int i = this.holdMapper.updateSpvHold4Invest(tradeOrder.getProductOid(), HoldAccountTypeEnum.SPV.value, tradeOrder.getOrderVolume());
		if (i < 1) {
			throw new BizException(30007, "SPV仓位不足(CODE:30007)");
		}
		return true;
	}

	/**
	 * 根据资产池查询持仓
	 * 
	 * @author lining6 2016年10月21日
	 * @param assetPoolOid
	 * @param productOid
	 * @return
	 */
	public HoldDetailRep getSPVHoldDetail(String assetPoolOid, String productOid) throws BizException {

		logger.info("getSPVHoldDetail:assetPoolOid:{},productOid:{}", assetPoolOid, productOid);

		HoldDetailRep detailRep = new HoldDetailRep();

		Hold hold = null;

		if (StringUtil.isNotBlank(assetPoolOid) && StringUtil.isNotBlank(productOid)) {

			SearchHoldVo vo = new SearchHoldVo();
			vo.setProductOid(productOid);
			vo.setAccountTypeEnum(HoldAccountTypeEnum.SPV);
			vo.setAssetPoolOid(assetPoolOid);

			List<Hold> holds = this.selectHolds(vo);

			if (holds.size() == 1) {
				hold = holds.get(0);
			} else {
				logger.error("getSPVHoldDetail查询数据不唯一,请检查数据:assetPoolOid:{},productOid:{}", assetPoolOid, productOid);
				throw new BizException("查询数据不唯一,请检查数据");
			}

			if (null != hold) {
				if (hold.getProductOid() != null) {
					Product product = productInterfaces.selectProductByOid(hold.getProductOid()).getData();
					detailRep.setProductOid(product.getOid()); // 产品OID
					detailRep.setProductCode(product.getCode()); // 产品编号
					detailRep.setProductName(product.getName()); // 产品名称
					detailRep.setExpAror(product.getExpAror().toString() + "~" + product.getExpArorSec().toString()); // 预期收益率
					detailRep.setLockPeriod(product.getLockPeriodDays()); // 锁定期
				}

				detailRep.setTotalHoldVolume(hold.getTotalHoldVolume()); // 持仓总份额
																			// 本金余额(持有总份额)
																			// totalHoldVolume
																			// decimal(16,4)
				detailRep.setRedeemableHoldVolume(hold.getRedeemableHoldVolume()); // 可赎回份额
				detailRep.setLockRedeemHoldVolume(hold.getLockRedeemHoldVolume()); // 赎回锁定份额
				detailRep.setValue(hold.getValue()); // 最新市值
				detailRep.setHoldTotalIncome(hold.getHoldTotalIncome()); // 累计收益
				detailRep.setHoldYesterdayIncome(hold.getHoldYesterdayIncome()); // 昨日收益
				detailRep.setExpectIncome(hold.getExpectIncome()); // 预期收益
				detailRep.setLastConfirmDate(hold.getLastConfirmDate()); // 份额确认日期
				detailRep.setHoldStatus(hold.getHoldStatus()); // 持仓状态
				detailRep.setHoldStatusDisp(HoldStatusEnum.getInstance(hold.getHoldStatus()).name); // 持仓状态disp

			}

		}

		return detailRep;
	}


	/**
	 * 校验用户所购产品最大持仓
	 */
	private void checkMaxHold4Invest(Hold hold, InvestorTradeOrder order, Product product){
		try {
			String investorOid = order.getInvestorOid();
			String productOid = product.getOid();
			BigDecimal proMaxHoldVolume = product.getMaxHold();
			BigDecimal orderVolume = order.getOrderVolume();
			
			// 等于0，表示无限制
			if(proMaxHoldVolume == null || proMaxHoldVolume.compareTo(BigDecimal.ZERO) == 0){
				return;
			}
			if (null == hold) {
				if (orderVolume.compareTo(proMaxHoldVolume) > 0) {
					logger.error("checkMaxHold4Invest:份额已超过所购产品最大持仓(CODE:30031):orderVolume{},maxHoldVolume{}", orderVolume,
							proMaxHoldVolume);
					throw new BizException(30031, "份额已超过所购产品最大持仓(CODE:30031)");
				}
			} else {
				int i = this.holdMapper.updateMaxHold4Invest(investorOid, productOid, proMaxHoldVolume, orderVolume);
				if (i < 1) {
					logger.error("checkMaxHold4Invest:份额已超过所购产品最大持仓(CODE:30031):orderVolume{},maxHoldVolume{}", orderVolume,
							proMaxHoldVolume);
					throw new BizException(30031, "份额已超过所购产品最大持仓(CODE:30031)");
				}
			}
		} catch (Exception e) {
			logger.error("checkMaxHold4Invest:{}",e.getMessage());

			throw e;
		}

	}

	@Override
	public Boolean updateHold4Confirm(String holdOid, BigDecimal redeemableHoldVolume, BigDecimal lockRedeemHoldVolume,
			BigDecimal accruableHoldVolume) throws BizException {

		logger.info("updateHold4Confirm: holdOid{},  redeemableHoldVolume{},  lockRedeemHoldVolume{}, accruableHoldVolume{}", holdOid,
				redeemableHoldVolume, lockRedeemHoldVolume, accruableHoldVolume);

		int i = this.holdMapper.updateHold4Confirm(holdOid, redeemableHoldVolume, lockRedeemHoldVolume, accruableHoldVolume);

		return i > 0 ? true : false;

	}

	/**
	 * 重置每日可赎回份额
	 */
	@Override
	public void resetDayRedeemVolume() throws BizException {
		JobLock jobLock = null;
		String batchCode = DateUtil.format(DateUtil.getSqlDate(), "yyyyMMdd");

		Message<JobLock> msg = jobLockInterface.queryAndDealJobLock(batchCode, JobLockIdEnum.DAYREDEEM_VOLUME.value);
		jobLock = MessageUtils.getJobLock(logger, msg, "resetDayRedeemVolume");
		try {

			this.holdMapper.resetDayRedeemVolume();
			jobLock.setJobStatus(JobLockStatusEnum.DONE.value);
			jobLock.setJobMessage("OK");
			logger.info("resetDayRedeemVolumeLock sussess");

		} catch (Exception e) {
			
			jobLock.setJobMessage(e.getMessage());
			jobLock.setJobStatus(JobLockStatusEnum.FAIL.value);
			logger.error("resetDayRedeemVolumeLock {}", e.getMessage());
		}

		
		Message<Boolean> msg2 = this.jobLockInterface.updateStatus4Lock(jobLock.getOid(), jobLock.getJobStatus());
		if (Messages.isSuccess(msg2) && msg2.getData() != null && msg2.getData()) {
			return;
		} else {
			logger.error("resetDayRedeemVolume jobLockInterface updateStatus4Lock failed.. oid={} status={} msg={}", jobLock.getJobId(),
					jobLock.getJobStatus(), JsonUtils.writeValue(msg2));
			throw new BizException("resetDayRedeemVolume jobLockInterface updateStatus4Lock failed");
		}

	}

	
	/**
	 * 废单后更新spv持仓
	 * 
	 * @author lining6 2016年10月21日
	 */
	@Override
	public Boolean updateSpvHold4InvestAbandon(String productOid, String accountType, BigDecimal orderVolume) throws BizException {

		logger.info("updateSpvHold4InvestAbandon:productOid{}, accountType{}, orderVolume{}", productOid, accountType, orderVolume);

		int i = this.holdMapper.updateSpvHold4InvestAbandon(productOid, accountType, orderVolume);
		if (i < 1) {
			// error.define[30035]=废申购单时SPV持仓锁定份额异常(CODE:30035)
			throw new BizException(30035, "废申购单时SPV持仓锁定份额异常(CODE:30035)");
		}
		return true;
	}

	@Override
	public Boolean updateMaxHold4InvestAbandon(String investorOid, Product product, BigDecimal orderVolume) throws BizException {

		if (null != product.getMaxHold() && product.getMaxHold().compareTo(BigDecimal.ZERO) != 0) { // 等于0，表示无限制
			int i = this.holdMapper.updateMaxHold4InvestAbandon(investorOid, product.getOid(), orderVolume);
			if (i < 1) {
				// error.define[30036]=废申购单时最大持仓份额异常(CODE:30036)
				throw new BizException(30036, "废申购单时最大持仓份额异常(CODE:30036)");
			}
			return true;

		}

		return false;

	}

	@Override
	public Boolean isInvestorExists(String investorOid, String productOid) throws BizException {
		int i = 0;
		try {
			i = this.holdMapper.isInvestorExists(investorOid, productOid);
		} catch (Exception e) {
			logger.error("isInvestorExists:investorOid{}, productOid{}", investorOid, productOid);
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "");
		}
		return i > 0 ? true : false;
	}

	/**
	 * 查询用户持仓
	 */
	@Override
	public HoldResponse queryUserBalance(HoldRequest request) throws BizException {
		HoldResponse rep = new HoldResponse();

		List<HoldOutput> holds = new ArrayList<HoldOutput>();

		String hkey = SysConstant.LECURRENT_REDIS_HOLD_PRIFIX + request.getInvestorOid();

		String result = "";
		String productOid = request.getProductOid();
		
		
		if (productOid != null) {
			result = redisClient.hgetString(hkey, productOid);
			logger.info("query redis, key={}, field={}, result={}", hkey, productOid, result);
			if (StringUtil.isNotBlank(result) && !result.equals("null")) {
				holds.add(JSONObject.parseObject(result, HoldOutput.class));
			}

		} else {
			Map<String, String> vals = this.redisClient.hgetAllString(hkey);
			logger.info("query redis, key={}, values={}", hkey, vals);

			Iterator<Map.Entry<String, String>> it = vals.entrySet().iterator();
			while (it.hasNext()) {

				Map.Entry<String, String> map = it.next();

				String valsStr = map.getValue();

				if (StringUtil.isNotBlank(valsStr) && !valsStr.equals("null")) {
					holds.add(JSONObject.parseObject(valsStr, HoldOutput.class));
				}

			}

		}
		
		/*String cacheSwitch="";
		if (profitDiamondService.getMessage("cacheSwitch") != null) {
			cacheSwitch = profitDiamondService.getMessage("cacheSwitch").toString();
		}
		
		if(cacheSwitch.equals("yes")){
			if(holds!=null && holds.size()>0){
				rep.setHolds(holds);
				logger.info("queryUserBalance:缓存模式");
				return rep;
			}
		}else{
			logger.info("queryUserBalance:非缓存模式");
		}*/
		
		if(holds!=null && holds.size()>0){
			rep.setHolds(holds);
			logger.info("queryUserBalance:缓存模式");
			return rep;
		}		
		

		// 如果缓存不存在，则查询数据库
		List<Hold> holdsOri = new ArrayList<Hold>();

		
			SearchHoldVo svo = new SearchHoldVo();
			svo.setInvestorOid(request.getInvestorOid());
			svo.setProductOid(request.getProductOid());
			holdsOri = this.selectHolds(svo);
			
			logger.info("query db, key={}, valuesize={}", hkey, holdsOri.size());
	
			if (holdsOri.size() <= 0) {
				return null;
			}
			rep = this.createHoldResponse(holdsOri, request.getProductOid());
			for(Hold vo :holdsOri){
				// 写入缓存
				this.putHoldData2Redis(hkey, vo);
			}
			

		

		return rep;
	}

	private HoldResponse createHoldResponse(List<Hold> holds, String productOid) throws BizException {
		HoldResponse rep = new HoldResponse();
		List<HoldOutput> holdReps = new ArrayList<HoldOutput>();
		for (Hold entity : holds) {
			HoldOutput hold = new HoldOutput();
			hold.setInvestorOid(entity.getInvestorOid());
			hold.setProductOid(entity.getProductOid());
			hold.setTotalMarketingIncome(ProductDecimalFormat.format2Cent(entity.getTotalMarketingIncome()));
			hold.setYesterdayMarketingIncome(ProductDecimalFormat.format2Cent(entity.getYesterdayMarketingIncome()));
			hold.setValue(ProductDecimalFormat.format2Cent(entity.getValue()));
			hold.setTotalHoldVolume(ProductDecimalFormat.format2Cent(entity.getTotalHoldVolume()));
			hold.setRedeemableHoldVolume(ProductDecimalFormat.format2Cent(entity.getRedeemableHoldVolume()));
			hold.setInvestTotalVolume(ProductDecimalFormat.format2Cent(entity.getInvestTotalVolume()));
			hold.setInvestTotalIncome(ProductDecimalFormat.format2Cent(entity.getHoldTotalIncome()));
			hold.setHoldYesterdayIncome(ProductDecimalFormat.format2Cent(entity.getHoldYesterdayIncome()));
			hold.setUpdateTime(entity.getUpdateTime());
			hold.setConfirmDate(entity.getLastConfirmDate()!=null?entity.getLastConfirmDate():new Date());
			holdReps.add(hold);

		}
		rep.setHolds(holdReps);
		return rep;
	}
	
	private void putHoldData2Redis(String hkey,Hold vo){
		
		
			Map<String, String> redisMap = new HashMap<>();
			redisMap.put("investorOid", vo.getInvestorOid());
			redisMap.put("productOid", vo.getProductOid());
			redisMap.put("value",DecimalUtil.changeRMB2FEN(vo.getValue())); 
			redisMap.put("totalHoldVolume",DecimalUtil.changeRMB2FEN(vo.getTotalHoldVolume())); 
			redisMap.put("redeemableHoldVolume",DecimalUtil.changeRMB2FEN(vo.getRedeemableHoldVolume()));
			redisMap.put("investTotalVolume",DecimalUtil.changeRMB2FEN(vo.getInvestTotalVolume()));
			redisMap.put("investTotalIncome",DecimalUtil.changeRMB2FEN(vo.getHoldTotalIncome()));
			redisMap.put("holdYesterdayIncome",DecimalUtil.changeRMB2FEN(vo.getHoldYesterdayIncome()));
			redisMap.put("totalMarketingIncome",DecimalUtil.changeRMB2FEN(vo.getTotalMarketingIncome()));
			redisMap.put("yesterdayMarketingIncome",DecimalUtil.changeRMB2FEN(vo.getYesterdayMarketingIncome()));
			redisMap.put("confirmDate", vo.getLastConfirmDate()!=null?
					String.valueOf(vo.getLastConfirmDate().getTime()):String.valueOf(new Date().getTime()));
			redisMap.put("updateTime", String.valueOf(vo.getUpdateTime().getTime()));
			logger.info("putHoldData2Redis:hkey:{},productOid:{},redisMap:{}", hkey, vo.getProductOid(), JsonUtils.writeValue(redisMap));

			 this.redisClient.hset(hkey, vo.getProductOid(), JsonUtils.writeValue(redisMap));
		
		
	}

	/**
	 * spv订单赎回确认
	 */

	@Override
	public Boolean spvOrderRedeemConfirm(String oid, BigDecimal orderAmount) throws BizException {

		int i = holdMapper.spvOrderRedeemConfirm(oid, orderAmount);
		return i > 0 ? true : false;
	}

	/**
	 * spv订单申购确认
	 */

	@Override
	public Boolean spvOrderInvestConfirm(String oid, BigDecimal orderAmount) throws BizException {
		int i = holdMapper.spvOrderInvestConfirm(oid, orderAmount);
		return i > 0 ? true : false;
	}

	@Override
	public Boolean loadHoldData2Cache() throws BizException {

		try {

			String minuteBefore = "";
			if (profitDiamondService.getMessage("holdCacheMinutesBefore") != null) {
				minuteBefore = profitDiamondService.getMessage("holdCacheMinutesBefore").toString();
			} else {
				logger.error("loadHoldData2Cache Diamond Is null");
				throw new BizException(Code.PARAMATERSISNULL.getValue(), "holdCacheMinutesBefore");
			}

			Date date = DateUtil.minuteBefor(new Date(), Integer.valueOf(minuteBefore));

			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

			String beginDate = sf.format(date);
			logger.info("loadHoldData2Cache:date :{}", beginDate);

			List<Hold> list = holdMapper.selectHoldByDate(beginDate);
			logger.info("loadHoldData2Cache:  list.size :{}", list.size());


			for(Hold vo :list){
				// 写入缓存
				String hkey = SysConstant.LECURRENT_REDIS_HOLD_PRIFIX + vo.getInvestorOid();
				this.putHoldData2Redis(hkey, vo);
			}

		} catch (BizException e) {
			logger.error("loadHoldData2Cache:Fail:{}", e);
			throw e;
		}

        return true;
    }

	@Override
	public Boolean loadHoldData2CacheMulti() throws Exception {
		try {
			String minuteBefore = "";
			if (profitDiamondService.getMessage("holdCacheMinutesBefore") != null) {
				minuteBefore = profitDiamondService.getMessage("holdCacheMinutesBefore").toString();
			} else {
				logger.error("loadHoldData2CacheMulti Diamond Is null");
				throw new BizException(Code.PARAMATERSISNULL.getValue(), "holdCacheMinutesBefore");
			}

			Date date = DateUtil.minuteBefor(new Date(), Integer.valueOf(minuteBefore));

			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

			String beginDate = sf.format(date);
			logger.info("loadHoldData2CacheMulti:date :{}", beginDate);

			final List<Hold> list = holdMapper.selectHoldByDate(beginDate);
			logger.info("loadHoldData2CacheMulti: list.size = {}", list.size());

			if (list==null || list.isEmpty()){
				logger.info("loadHoldData2CacheMulti 无数据需处理");

				return true;
			}

			int pageNum = new BigDecimal(list.size()).divide(new BigDecimal(pageSize)).setScale(0, BigDecimal.ROUND_CEILING).intValue();

			final CountDownLatch countDownLatch = new CountDownLatch(pageNum);

			for (int i = 1; i <= pageNum; i++) {
				final int pageNo = i;
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							logger.info("loadHoldData2CacheMulti {} pagaNo:{}",Thread.currentThread().getName(),pageNo);
							int start = (pageNo - 1) * pageSize;
							int end = start + pageSize;
							while (start <= end && start < list.size()) {
								Hold vo = list.get(start++);
								// 写入缓存
								String hkey = SysConstant.LECURRENT_REDIS_HOLD_PRIFIX + vo.getInvestorOid();
								try {
									putHoldData2Redis(hkey, vo);
								} catch (Exception e) {
									logger.error("putHoldData2Redis error.. vo="+JsonUtils.writeValue(vo),e);
									continue;
								}
							}
						} catch (Exception e) {
							logger.error("loadHoldData2CacheMulti", e);
						} finally {
							countDownLatch.countDown();
						}
					}
				});
				threadPoolTaskExecutor.execute(thread);
			}

			if (countDownLatch.await(Integer.valueOf(minuteBefore)-1>0?Integer.valueOf(minuteBefore)-1:1, TimeUnit.MINUTES)) {
				logger.info("loadHoldData2CacheMulti done...");
			} else {
				logger.error("loadHoldData2CacheMulti timeOut...");
				throw new BizException("loadHoldData2CacheMulti timeOut...");
			}
		} catch (Exception e) {
			logger.error("loadHoldData2CacheMulti:Fail:{}", e);
			throw e;
		}

		return true;
	}
	
	@Override
	public  Boolean compareHoldDataWithCache() throws Exception{
		try{
			Date date = DateUtil.getDate(new Date());

			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

			String beginDate = sf.format(date);
			logger.info("loadHoldData2Cache:date :{}", beginDate);

			List<Hold> list = holdMapper.selectHoldByDate(beginDate);
			if(list==null||list.size()==0){
				return Boolean.TRUE;
			}
			logger.info("compareHoldDataWithCache共查到数据：{}条",list.size());
			for(Hold vo :list){
				String hkey = SysConstant.LECURRENT_REDIS_HOLD_PRIFIX + vo.getInvestorOid();
				this.putHoldData2Redis(hkey, vo);
			}
		}catch (Exception e){
			logger.error("compareHoldDataWithCache:{}",e);
			throw e;
		}

		return Boolean.TRUE;
	}


	@Override
	public Boolean volHoldconfirmDetail(String offsetOid) throws BizException {
		logger.info("volHoldconfirmDetail:offsetOid:{}", offsetOid);
		Message<List<OrderBaseVo>> message;
		List<OrderBaseVo> orders;
		
		try {
			String lastOid ="0";
			while (true) {
				message = orderInterface.getOrdersByOffsetOidAndLastOid(offsetOid, lastOid, pageSize);
				if(message==null||!Messages.isSuccess(message)){
					logger.error("volHoldconfirmDetail:offsetOid:{}", offsetOid);
					throw new BizException("调用订单接口失败");
				}
				orders = message.getData();
				if(null == orders || orders.isEmpty()){
					break;
				}
				
				for (OrderBaseVo orderBaseVo : orders) {
					logger.info("volHoldconfirmDetail:offsetOid:{}oderType{},orderCode{}", offsetOid, orderBaseVo.getOrderType(),orderBaseVo.getOrderCode());
					
					//查询该订单是否已处理，若已处理跳过
					VolumeConfirmLog volumeConfirmLog=volumeConfirmLogMapper.selectByOrderCode(orderBaseVo.getOrderCode());
					if(volumeConfirmLog!=null){
						continue;
					}
					//单条订单处理
					if (OrderTypeEnum.NORMAL_REDEEM.value.equals(orderBaseVo.getOrderType())) {
						//对象转换
						InvestorTradeOrder order =this.OrderBase2TradeOrder(orderBaseVo);
						//赎回单处理
						this.redeem4Volconfirm(order);
					} else {
						//投资单处理
						this.invest4Volconfirm(orderBaseVo);
						// 向会计发送消息  单条发送
						Result result = RocketMQMessageUtil.saveMessage(producer, JsonUtils.writeValue(Arrays.asList(orderBaseVo)), accountTopic, invest);
						if(!Result.isSuccess(result)){
							// TODO
						}
					}
				}
				
				lastOid = orders.get(orders.size()-1).getOid();
			}
		} catch (Exception e) {// 回滚事务
			logger.error("invest failed", e);
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "份额确认操作异常", e);
		}
		return true;
	}
	
	private InvestorTradeOrder OrderBase2TradeOrder(OrderBaseVo orderBaseVo){
		InvestorTradeOrder order = new InvestorTradeOrder();
		order.setOid(orderBaseVo.getTradeOrderOid());
		order.setOrderVolume(orderBaseVo.getOrderVolume());
		order.setProduct(orderBaseVo.getProduct());
		order.setProductOid(orderBaseVo.getProduct().getOid());
		order.setOrderType(orderBaseVo.getOrderType());
		order.setOrderCode(orderBaseVo.getOrderCode());
		order.setInvestorOid(orderBaseVo.getInvestorOid());
		order.setOriginOrderOid(orderBaseVo.getOriginOrderOid());
		order.setPunishVolume(orderBaseVo.getPunishVolume());
		return order;
	}
	
	/**
	 * 投资单确认份额操作
	 * 
	 * @param tradeOrder
	 */
	private void invest4Volconfirm(OrderBaseVo order){
		logger.info("invest orderId:{}", order.getOid());
		
		HoldApart apart = this.holdApartService.findHoldApartByOrderId(order.getTradeOrderOid());
		logger.info("volHoldconfirmDetail:apart{}", JsonUtils.writeValue(apart));
		
		BigDecimal lockRedeemHoldVolume = BigDecimal.ZERO;
		BigDecimal redeemableHoldVolume=BigDecimal.ZERO;
		BigDecimal accruableHoldVolume = BigDecimal.ZERO;
		String redeemStatus = HoldApartAccrualStatus.NO.value;
		String accrualStatus = HoldApartAccrualStatus.NO.value;
		
		//如果当前日期大于等于可赎回开始日期，则增加可赎回份额
		if (DateUtil.daysBetween(apart.getBeginRedeemDate(), new Date()) <= 0) {
			redeemableHoldVolume = order.getOrderVolume(); // 可赎回份额增加
			lockRedeemHoldVolume = order.getOrderVolume().negate(); // 锁定赎回份额减少
			redeemStatus = HoldApartRedeemStatus.YES.value;
		}
		//如果当前日期大于等于开始计息日期，则增加计息份额
		if (DateUtil.daysBetween(apart.getBeginAccuralDate(), new Date()) <= 0) {
			accruableHoldVolume = order.getOrderVolume();
			accrualStatus = HoldApartAccrualStatus.YES.value;
		}
		
		// 事物控制开始，开始添加 增加事务
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(definition);
		try {
			// 更新合仓
			this.updateHold4Confirm(apart.getHoldOid(), redeemableHoldVolume, lockRedeemHoldVolume, accruableHoldVolume);
			// 更新分仓
			this.holdApartService.update4Confirm(order.getTradeOrderOid(),accruableHoldVolume, redeemStatus, accrualStatus);
			// 更新SPV持仓
			this.update4InvestConfirm(order.getProduct().getOid(), order.getOrderVolume());
			//插入日志表
			this.addVolumeConfirmLog(order.getOrderCode());
			// 提交事务
			transactionManager.commit(status);
			//调用order通知平台
			this.createOrderConfirmLog(order.getOrderCode(), order.getOrderType());
		} catch (Exception e) {
			// 回滚事务
			transactionManager.rollback(status);
			logger.error("invest failed", e);
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "投资操作异常", e);
		}
	}
	
	/**
	 * 赎回单确认份额操作
	 * 
	 * @param tradeOrder
	 */
	private void redeem4Volconfirm(InvestorTradeOrder tradeOrder){
		logger.info("invest orderId:{}", tradeOrder.getOid());
		
		// 事物控制开始，开始添加 增加事务
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(definition);
		try {
			// 合仓处理
			this.hold4Redeem(tradeOrder);
			
			// 分仓处理
			Product product=tradeOrder.getProduct();
			Byte productType=product.getSubType();
			//赎回当日不计息
			if(product.getRedeemNeedInterest()==(byte)0){
				//赎回对应分仓全部金额
				if(productType==ProductSubTypeEnum.SELECTED.getValue()||productType==ProductSubTypeEnum.FIXED_CURRENT.getValue()){
					HoldApart apart=this.holdApartService.findHoldApartByOrderId(tradeOrder.getOriginOrderOid());
					this.holdApartService.flatWare2RedeemByHold(apart,tradeOrder);
				}else{//普通单子处理
					this.holdApartService.flatWare(tradeOrder);
				}
			}else{
				this.holdApartService.flatWare(tradeOrder);
			}
			// 更新SPV持仓
			this.update4RedeemConfirm(tradeOrder);
			//插入日志表
			this.addVolumeConfirmLog(tradeOrder.getOrderCode());
			
			// 提交事务
			transactionManager.commit(status);
			
			//调用order通知平台
			this.createOrderConfirmLog(tradeOrder.getOrderCode(), tradeOrder.getOrderType());
		} catch (Exception e) {
			// 回滚事务
			transactionManager.rollback(status);
			logger.error("invest failed", e);
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "赎回操作异常", e);
		}
	}
	
	// 记录订单日志
	private void createOrderConfirmLog(String orderCode,String orderType){
		OrderLog orderLog = new OrderLog();
		orderLog.setOid(UUIDUtil.creatUUID());
		orderLog.setOrderStatus(OrderStatusEnum.CONFIRMED.value);
		orderLog.setOrderType(orderType);
		orderLog.setTradeOrderOid(orderCode);
		orderLog.setUpdateTime(new Date());
		orderLog.setCreateTime(new Date());
		this.callDubboService.callAddLogAndNotify(orderLog);
	}
	
	private void addVolumeConfirmLog(String orderCode){
		VolumeConfirmLog log=new VolumeConfirmLog();
		log.setOid(UUIDUtil.creatUUID());
		log.setOrderCode(orderCode);
		log.setCreateTime(new Date());
		volumeConfirmLogMapper.insertSelective(log);
	}
	
	private void hold4Redeem(InvestorTradeOrder tradeOrder){
		Product product=tradeOrder.getProduct();
		int i = this.holdMapper.redeem(tradeOrder.getOrderVolume(), product.getNetUnitShare(), tradeOrder.getInvestorOid(),
				tradeOrder.getProductOid());
		if (i < 1) {
			logger.error("redeem: 赎回份额异常getOrderVolume:{},getNetUnitShare:{},getInvestorOid:{},getProductOid{}",
					tradeOrder.getOrderVolume(), product.getNetUnitShare(), tradeOrder.getInvestorOid(), tradeOrder.getProductOid());
			throw new BizException(20005, "赎回份额异常(CODE:20005)");
		}
	}
	
	private void update4InvestConfirm(String productOid, BigDecimal orderVolume){
		logger.info("update4InvestConfirm:productOid{},orderVolume:{}", productOid, orderVolume);
		int i = this.holdMapper.update4InvestConfirm(productOid, HoldAccountTypeEnum.SPV.value, orderVolume);
		if (i < 1) {
			throw new BizException(30024, "针对SPV持仓份额确认失败(CODE:30024)");
		}
	}

	private void update4RedeemConfirm(InvestorTradeOrder tradeOrder){
		int i = this.holdMapper.update4RedeemConfirm(tradeOrder.getProduct().getOid(), HoldAccountTypeEnum.SPV.value, tradeOrder.getOrderVolume());
		if (i < 1) {
			throw new BizException(30024, "针对SPV持仓份额确认失败(CODE:30024)");
		}
	}

	/**
	 * 根据产品查合仓
	 */
	@Override
	public HoldResponse queryHoldList(HoldListRequest req) throws BizException {
		logger.info("query hold list, productOid={}", req.getProductOid());
		SearchHoldVo vo = new SearchHoldVo();
		vo.setProductOid(req.getProductOid());
		vo.setPageSize(req.getLimit());
		vo.setCurrentPageNo(req.getOffset());

		Page<Hold> holds = this.selectHolds4Api(vo);
		HoldResponse rep = this.createHoldResponse(holds.getDataList(), req.getProductOid());
		return rep;
	}

	@Override
	public List<Hold> findByProductHoldStatus4lx(String productOid, String holdStatus, String holdOid) {
		return this.holdMapper.findByProductAndHoldStatus4lx(productOid, holdStatus, holdOid, HoldAccountTypeEnum.INVESTOR.value);
	}

	

	
}
