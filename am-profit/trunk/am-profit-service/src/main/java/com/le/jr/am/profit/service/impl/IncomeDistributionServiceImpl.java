package com.le.jr.am.profit.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSONObject;
import com.le.jr.am.accountant.domain.po.acctbook.AccountBook;
import com.le.jr.am.accountant.interfaces.acctbook.AccountBookInterface;
import com.le.jr.am.accountant.interfaces.acctbook.SPVDocumentInterface;
import com.le.jr.am.assetpool.domain.GamAssetpool;
import com.le.jr.am.assetpool.domain.resultForm.SerFeeQueryRep;
import com.le.jr.am.assetpool.interfaces.GamAssetpoolChargefeeSettingInterface;
import com.le.jr.am.assetpool.interfaces.GamAssetpoolEstimateInterfaces;
import com.le.jr.am.assetpool.interfaces.GamAssetpoolInterfaces;
import com.le.jr.am.assetpool.interfaces.MoneyLxSerfeeInterfaces;
import com.le.jr.am.product.common.util.ProductDecimalFormat;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.product.common.util.ValidateUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.vo.SearchProductVo;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.dao.RewardIncomePracticeMapper;
import com.le.jr.am.profit.domain.IncomeAllocate;
import com.le.jr.am.profit.domain.IncomeEvent;
import com.le.jr.am.profit.domain.InvestorInterestResult;
import com.le.jr.am.profit.domain.enums.IncomeEventStausEnum;
import com.le.jr.am.profit.domain.input.IncomeAllocateForm;
import com.le.jr.am.profit.domain.input.SearchAllocateVO;
import com.le.jr.am.profit.domain.output.IncomeAllocateAssetResp;
import com.le.jr.am.profit.domain.output.IncomeAllocateCalcResp;
import com.le.jr.am.profit.domain.output.IncomeAllocateHisResp;
import com.le.jr.am.profit.domain.output.IncomeAllocateProductResp;
import com.le.jr.am.profit.domain.output.IncomeDistributionResp;
import com.le.jr.am.profit.domain.output.RewardIsNullRep;
import com.le.jr.am.profit.service.IncomeAllocateService;
import com.le.jr.am.profit.service.IncomeDistributionService;
import com.le.jr.am.profit.service.IncomeEventService;
import com.le.jr.am.profit.service.InterestRateMethodService;
import com.le.jr.am.profit.service.InvestorInterestResultService;
import com.le.jr.am.profit.service.RewardIncomePracticeService;
import com.le.jr.am.profit.service.util.HttpHelper;
import com.le.jr.am.system.domain.BaseResp;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.le.jr.trade.publictools.util.StringUtil;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service("incomeDistributionService")
public class IncomeDistributionServiceImpl implements IncomeDistributionService {

	private static final Logger logger = LoggerFactory.getLogger(IncomeDistributionServiceImpl.class);

	@Resource
	private ProductInterfaces productInterfaces;

	@Resource
	private IncomeEventService incomeEventService;

	@Resource
	private RewardIncomePracticeService rewardIncomePracticeService;

	@Resource
	private GamAssetpoolEstimateInterfaces gamAssetpoolEstimateInterfaces;

	@Resource
	private MoneyLxSerfeeInterfaces moneyLxSerfeeInterfaces;

	@Resource
	private IncomeAllocateService incomeAllocateService;

	@Resource
	private InvestorInterestResultService interestResultService;

	@Resource
	private GamAssetpoolInterfaces gamAssetpoolInterfaces;
	
	
	@Resource
	private InterestRateMethodService interestRateMethodService;

	@Resource
	private SPVDocumentInterface spvDocumentInterface;

	@Resource
	private AccountBookInterface accountBookInterface;
	
	@Resource
	private RewardIncomePracticeMapper rewardIncomePracticeMapper;

	@Value("${adminUrl}")
	private String adminUrl;

	@Resource
	private PlatformTransactionManager transactionManager;
	/**
	 * 根据资产池和收益分配日获取 产品总规模和奖励收益
	 * 
	 * @param assetPoolOid
	 * @param incomeDate
	 * @return
	 */
	@Override
	public IncomeAllocateProductResp getTotalScaleRewardBenefit(String productOid, String incomeDate)throws BizException {
		
		logger.info("getTotalScaleRewardBenefit productOid:{};incomeDate:{}",productOid,incomeDate);
		IncomeAllocateProductResp resp = new IncomeAllocateProductResp();

		Product p = productInterfaces.selectProductByOid(productOid).getData();
		if(p.getSubType()!=null){
			resp.setSubType(p.getSubType().toString());
		}
		SerFeeQueryRep fee = moneyLxSerfeeInterfaces.findFeeByDate(productOid, incomeDate).getData();// 乐视服务费
		if (fee != null) {
			resp.setLetvServiceFee(fee.getFee());
		}

		RewardIsNullRep practice = rewardIncomePracticeService.rewardIsNullRep(productOid, incomeDate);// 王国
		if (practice != null) {
			if (practice.getTotalHoldVolume() != null) {// 持有人总份额
				resp.setProductTotalScale(practice.getTotalHoldVolume());
			}
			if (practice.getTotalRewardIncome() != null) {// 奖励收益
				resp.setProductRewardBenefit(practice.getTotalRewardIncome());
			}
		}
		
		BigDecimal feeValue = gamAssetpoolEstimateInterfaces.feeCalac(p.getAssetPoolOid(), incomeDate).getData();
        if (feeValue != null) {
			resp.setFeeValue(feeValue);
		}
		return resp;
	}

	/**
	 * 保存收益分配事件 杜建君 2015年10月14日，增加注释
	 * 
	 * @param form
	 * @param operator
	 * @return
	 */

	@Override
	public Boolean saveIncomeAdjust(IncomeAllocateForm form, String operator) throws BizException {
		logger.info("saveIncomeAdjust:form {}",JsonUtils.writeValue(form));

		// 凌晨0点到1点不允许录入 杜建君2016年10月10日---------开始
		Calendar calendar=Calendar.getInstance();
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour == 0) {
			logger.error("IncomeDistribution.saveIncomeAdjust 在0:00到1:00之间不允许录入收益分配信息!,assetpoolOid:{}", form.getAssetpoolOid());
			
			throw new BizException(100005, "在0:00到1:00之间不允许录入收益分配信息!");
		}
		// 凌晨0点到1点不允许录入 杜建君2016年10月10日---------结束

		// 查询该资产池最近一天的收益分配日 非 IncomeEvent.STATUS_Fail 和 非
		// IncomeEvent.STATUS_Delete
		
		IncomeAllocate lastIncomeAllocate = this.incomeAllocateService.findLastValidIncomeAllocate(form.getProductOid());
		
		if (lastIncomeAllocate != null) {// 非首次分配收益
			
			IncomeEvent lastIncomeEvent = this.incomeEventService.getIncomeEventByOid(lastIncomeAllocate.getEventOid());
			
			if (IncomeEventStausEnum.CREATE.value.equals(lastIncomeEvent.getStatus())) {
				
				throw new BizException(60004, "请先审核" + DateUtil.formatDate(lastIncomeEvent.getBaseDate().getTime()) + "的收益分配!");
			} else if (IncomeEventStausEnum.ALLOCATING.value.equals(lastIncomeEvent.getStatus())) {
			
				throw new BizException(60005, "请先等待" + DateUtil.formatDate(lastIncomeEvent.getBaseDate().getTime()) + "的收益分配完成!");
			} else if (IncomeEventStausEnum.ALLOCATEFAIL.value.equals(lastIncomeEvent.getStatus())) {
				
				throw new BizException(60006, "请先完成" + DateUtil.formatDate(lastIncomeEvent.getBaseDate().getTime()) + "的收益分配!");
			} else if (lastIncomeEvent.getBaseDate().getTime() == DateUtil.getBeforeDate().getTime()) {
				
				throw new BizException(60010, "今日已经申请过昨日收益分配");
			} else if (lastIncomeEvent.getBaseDate().getTime() == DateUtil.getCurrDate().getTime()) {
				
				throw new BizException(60010, "今日已经申请过今日收益分配");
			}
			
			// 2016年10月14日杜建君，修改，可有添加基准日是当天的收益分配事件
			else if (lastIncomeEvent.getBaseDate().getTime() > DateUtil.getCurrDate().getTime()) {
				
				throw new BizException(60012, "今日只能申请今日以及今日之前的收益分配");
			}
		}else{
			//首次分配收益，从试算表查询该产品第一次试算尚未派息的日期
			Date oldPracticeDate = rewardIncomePracticeMapper.findOldDateByProductOid(form.getProductOid());
			Date baseDate =DateUtil.parseDate(form.getIncomeDistrDate(),"yyyy-MM-dd");
			if(oldPracticeDate!=null && baseDate.getTime()>oldPracticeDate.getTime()){
				logger.info("该产品应该第一次派息日应该是:{}",DateUtil.format(oldPracticeDate));
				throw new BizException(60013,"该产品第一次派息日应该是"+DateUtil.format(oldPracticeDate));
			}
		}

		// 查询资产池
		GamAssetpool assetPool = gamAssetpoolInterfaces.getPoolByOid(form.getAssetpoolOid()).getData();
		if (assetPool == null) {

			logger.error("saveIncomeAdjust资产池未查询到 form.getAssetpoolOid(){}", form.getAssetpoolOid());
			throw new BizException(60000, "资产池为空");
		}

		// 查询资产池下所有的产品，因为目前只是一对一，所以产品去第一个
		Product product = productInterfaces.selectProductByOid(form.getProductOid()).getData();
		
		if(product == null){
			logger.error("saveIncomeAdjust:productOid:{}",form.getProductOid());
			throw new BizException(Code.RESULTSETISNULL.getValue(), "产品为空");
		}

		// 包装收益分配事件主体
		IncomeEvent incomeEvent = new IncomeEvent();
		incomeEvent.setOid(UUIDUtil.creatUUID());
		incomeEvent.setAssetPoolOid(form.getAssetpoolOid());
		incomeEvent.setProductOid(form.getProductOid());
		incomeEvent.setBaseDate(DateUtil.parseDate(form.getIncomeDistrDate(),"yyyy-MM-dd"));
		incomeEvent.setAllocateIncome(new BigDecimal(form.getProductRewardBenefit()).add(new BigDecimal(form.getProductDistributionIncome())));// 总分配收益
		incomeEvent.setCreator(operator);
		incomeEvent.setCreateTime(new Date());
		incomeEvent.setDays(1);
		incomeEvent.setStatus(IncomeEventStausEnum.CREATE.value);


		// 包装收益分配表
		IncomeAllocate incomeAllocate = new IncomeAllocate();
		incomeAllocate.setOid(UUIDUtil.creatUUID());
		incomeAllocate.setEventOid(incomeEvent.getOid());
		incomeAllocate.setProductOid(product.getOid());
		incomeAllocate.setBaseDate(incomeEvent.getBaseDate());
		incomeAllocate.setCapital(new BigDecimal(form.getProductTotalScale()));// 产品总规模
		incomeAllocate.setAllocateIncome(new BigDecimal(form.getProductDistributionIncome()));// 分配收益
		incomeAllocate.setRewardIncome(new BigDecimal(form.getProductRewardBenefit()));// 奖励收益
		incomeAllocate.setRatio(ProductDecimalFormat.divide(new BigDecimal(form.getProductAnnualYield())));// 收益率(年化)
																											// form.getProductAnnualYield()单位为%

		/**
		 * 万份收益=年化收益率/365*10000
		 */
		BigDecimal productAnnualYield = ProductDecimalFormat.divide(new BigDecimal(form.getProductAnnualYield())); // 产品范畴
																													// 年化收益率
		BigDecimal incomeCalcBasis = new BigDecimal(product.getIncomeCalcBasis());// 计算基础
		BigDecimal millionCopiesIncome = productAnnualYield.multiply(new BigDecimal("10000")).divide(incomeCalcBasis, 4,
				RoundingMode.HALF_UP);// 万份收益 试算结果
		incomeAllocate.setWincome(millionCopiesIncome);

		incomeAllocate.setDays(1);// 收益分配天数
		incomeAllocate.setSuccessAllocateIncome(new BigDecimal("0"));// 成功分配收益
		incomeAllocate.setLeftAllocateIncome(new BigDecimal(form.getProductRewardBenefit()).add(new BigDecimal(form
				.getProductDistributionIncome())));// 剩余收益
		incomeAllocate.setSuccessAllocateInvestors(0);// 成功分配投资者数
		incomeAllocate.setFailAllocateInvestors(0);// 失败分配投资者数
		// 事物控制开始 //增加事务
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(definition);
		try {
			this.incomeEventService.insert(incomeEvent);
			logger.info("IncomeDistribution.saveIncomeAdjust 添加收益事件主体完成!,assetpoolOid:{}", form.getAssetpoolOid());
			incomeAllocateService.insert(incomeAllocate);
			logger.info("IncomeDistribution.saveIncomeAdjust 添加收益分配表完成!,assetpoolOid:{}", form.getAssetpoolOid());
			transactionManager.commit(status);
		}catch(Exception e){
			logger.error("saveIncomeAdjust.新增失败，e：{}",e.getMessage());
			transactionManager.rollback(status);
			return  Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	private IncomeDistributionResp configIncomeDistributionResp(String oid) throws BizException{

		IncomeAllocate incomeAllocate = incomeAllocateService.selectByOid(oid);

		IncomeEvent event = this.incomeEventService.getIncomeEventByOid(incomeAllocate.getEventOid());
		if(event == null){
			return null;
		}
		GamAssetpool assetPool = gamAssetpoolInterfaces.getPoolByOid(event.getAssetPoolOid()).getData();
		Message<Product> productMsg = this.productInterfaces.selectProductByOid(incomeAllocate.getProductOid());
		
		if(!ValidateUtil.interfaceValidate(productMsg)){
			logger.error("未查询到此产品 productOid:{}",incomeAllocate.getProductOid());
			throw new BizException("未查询到此产品");
		}
		Product product = productMsg.getData();

		// 从新包装一个返回到前端的VO
		IncomeDistributionResp idr = new IncomeDistributionResp();
		idr.setOid(oid);
		idr.setAssetPoolName(assetPool.getName());
		idr.setAssetpoolOid(event.getAssetPoolOid());
		idr.setProductName(product.getName());
		idr.setProductOid(incomeAllocate.getProductOid());

		idr.setAllocateIncome(ProductDecimalFormat.format(incomeAllocate.getAllocateIncome(), "0.##"));// 分配收益);
		idr.setAuditor(event.getAuditor());// 审批人

		idr.setAuditTime(event.getAuditTime() != null ? DateUtil.formatDatetime(event.getAuditTime().getTime()) : "");// 审批时间
		idr.setBaseDate(DateUtil.formatDate(incomeAllocate.getBaseDate().getTime())); // 基准日
		idr.setCapital(ProductDecimalFormat.format(incomeAllocate.getCapital(), "0.##"));// 产品总规模
		idr.setStatus(event.getStatus());// 状态 (待审核: CREATE;通过: PASS;驳回:
											// FAIL;已删除: DELETE));
		idr.setCreateTime(DateUtil.formatDatetime(event.getCreateTime().getTime())); // 申请时间
		idr.setCreator(event.getCreator());// 申请人
		idr.setRewardIncome(ProductDecimalFormat.format(incomeAllocate.getRewardIncome(), "0.##"));// 奖励收益);
		idr.setSuccessAllocateIncome(incomeAllocate.getSuccessAllocateIncome() != null ? ProductDecimalFormat.format(
				incomeAllocate.getSuccessAllocateIncome(), "0.##") : "");// 成功分配收益
		idr.setSuccessAllocateInvestors(incomeAllocate.getSuccessAllocateInvestors() != null ? incomeAllocate.getSuccessAllocateInvestors()
				.toString() : "");// 成功分配投资者数);
		idr.setSuccessAllocateRewardIncome(incomeAllocate.getSuccessAllocateRewardIncome() != null ? ProductDecimalFormat.format(
				incomeAllocate.getSuccessAllocateRewardIncome(), "0.##") : "");// 成功分配奖励收益金额
		idr.setTotalAllocateIncome(ProductDecimalFormat.format(event.getAllocateIncome(), "0.##"));// 总分配收益
		idr.setRatio(ProductDecimalFormat.format(ProductDecimalFormat.multiply(incomeAllocate.getRatio()), "0.##"));// 收益率
		idr.setLeftAllocateIncome(incomeAllocate.getLeftAllocateIncome() != null ? ProductDecimalFormat.format(
				incomeAllocate.getLeftAllocateIncome(), "0.##") : "");// 剩余分配收益
		idr.setFailAllocateInvestors(incomeAllocate.getFailAllocateInvestors() != null ? incomeAllocate.getFailAllocateInvestors()
				.toString() : "");// 失败分配投资者数

		return idr;

	}

	@Override
	public IncomeDistributionResp getIncomeAdjust(String allocateOid)throws BizException {
		
		logger.info("getIncomeAdjust:allocateOid{}", allocateOid);

		IncomeDistributionResp idr = this.configIncomeDistributionResp(allocateOid);
		
		if(idr == null){
			return null;
		}
		
		logger.info("getIncomeAdjust:idr{}", JsonUtils.writeValue(idr));

		if (!StringUtil.isEmpty(idr.getCreator())) {

			try {

				MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
				multiValueMap.set("oid", idr.getCreator());
				Map<String, Object> admin = HttpHelper.postRequestResult(adminUrl, multiValueMap, Map.class);// adminsSdk.getAdmin(entity.getChecker());

				idr.setCreator((String) admin.get("name"));

			} catch (Exception e) {
				logger.error("getIncomeAdjust:oid{}", allocateOid, e);
			}

		}
		if (!StringUtil.isEmpty(idr.getAuditor())) {

			try {
				MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
				multiValueMap.set("oid", idr.getAuditor());
				Map<String, Object> admin = HttpHelper.postRequestResult(adminUrl, multiValueMap, Map.class);// adminsSdk.getAdmin(entity.getChecker());
				idr.setAuditor((String) admin.get("name"));
			} catch (Exception e) {
				logger.error("getIncomeAdjust:oid{}", allocateOid, e);
			}

		}

		return idr;
	}

	@Override
	public Page<IncomeDistributionResp> getIncomeAdjustList(SearchAllocateVO vo)throws BizException {
		logger.info("getIncomeAdjustList,获取收益分配列表");

		
		Page<IncomeAllocate> cas = this.incomeAllocateService.selectIncomeAllocates4Api(vo);

		Page<IncomeDistributionResp> pagesRep = new Page<IncomeDistributionResp>();
		
		List<IncomeDistributionResp> rows = new ArrayList<IncomeDistributionResp>();

		if (cas != null && cas.getDataList() != null && cas.getTotalCount() > 0) {
			

			for (IncomeAllocate ia : cas.getDataList()) {
				
				IncomeDistributionResp r = this.getIncomeAdjust(ia.getOid());
				if(r !=null){
					rows.add(r);
				}

				
			}
			
		}
		
		pagesRep.setDataList(rows);
		pagesRep.setTotalCount(cas.getTotalCount());

		return pagesRep;
	}

	@Override
	public Boolean auditPassIncomeAdjust(String allocateOid, String operator)throws BizException {
		logger.info("auditPassIncomeAdjust,allocateOid:{},operator:{}", allocateOid, operator);
		IncomeAllocate incomeAllocate = incomeAllocateService.selectByOid(allocateOid);
		IncomeEvent ie = this.incomeEventService.getIncomeEventByOid(incomeAllocate.getEventOid());
		if (ie != null && !IncomeEventStausEnum.CREATE.value.equals(ie.getStatus())) {

			logger.error("auditPassIncomeAdjust 待审核状态才能审核!oid{},operator{}", allocateOid, operator);
			throw new BizException(60007, "待审核状态才能审核!");
		}
		// 如果审核日期与基准日相同，则不允许审核
		Date today = DateUtil.getCurrDate();

		if (ie != null && ie.getBaseDate().getTime() == today.getTime()) {
			logger.error("auditPassIncomeAdjust:{},oid:{}","60013审核日期与基准日相同，不允许审核!",allocateOid);
			throw new BizException(60013, "审核日期与基准日相同，不允许审核!");
		}

		ie.setAuditor(operator);
		ie.setAuditTime(new Date());
		ie.setStatus(IncomeEventStausEnum.ALLOCATING.value);// 发放中
		
		if(!incomeEventService.updateIncomeEvent(ie)){
			logger.error("auditPassIncomeAdjust:更新事件分配状态失败,ie:{}",JsonUtils.writeValue(ie));
			throw new BizException("更新事件分配状态失败");
		}

		logger.info("IncomeDistributionService.auditPassIncomeAdjust,oid:{},状态修改为发放中", allocateOid);
		
		this.interestRateMethodService.interest(incomeAllocate.getProductOid(), incomeAllocate.getOid(),
				incomeAllocate.getAllocateIncome(), incomeAllocate.getRatio(), incomeAllocate.getBaseDate());

		logger.info("IncomeDistributionService.auditPassIncomeAdjust,oid:{},派息完毕", allocateOid);
		return true;
	}

	/**
	 * 发放收益 乐超收益分配完成后调用
	 * 
	 * @param allocateIncomeReturn
	 */
	@Override
	public void allocateIncome(InvestorInterestResult allocateIncomeReturn,BigDecimal holdInterestMarketingAmount) throws BizException{

		Product p = productInterfaces.selectProductByOid(allocateIncomeReturn.getProductOid()).getData();

		IncomeAllocate im = incomeAllocateService.selectByOid(allocateIncomeReturn.getAllocateOid());
		IncomeEvent ie = incomeEventService.getIncomeEventByOid(im.getEventOid());

		/**
		 * 成功分配基础收益金额
		 */
		BigDecimal successAllocateIncome = BigDecimal.ZERO;
		if (allocateIncomeReturn.getSuccessAllocateIncome() != null) {
			successAllocateIncome = allocateIncomeReturn.getSuccessAllocateIncome();
		}

		/**
		 * 成功分配奖励收益金额
		 */
		BigDecimal successAllocateRewardIncome = BigDecimal.ZERO;
		if (allocateIncomeReturn.getSuccessAllocateRewardIncome() != null) {
			successAllocateRewardIncome = allocateIncomeReturn.getSuccessAllocateRewardIncome();
		}

		/**
		 * 成功分配投资者数
		 */
		Integer successAllocateInvestors = 0;
		if (allocateIncomeReturn.getSuccessAllocateInvestors() != null) {
			successAllocateInvestors = allocateIncomeReturn.getSuccessAllocateInvestors();
		}

		im.setSuccessAllocateIncome(im.getSuccessAllocateIncome().add(successAllocateIncome));// 成功分配收益金额
		im.setSuccessAllocateRewardIncome(im.getSuccessAllocateRewardIncome().add(successAllocateRewardIncome));// 成功分配奖励收益金额
		im.setLeftAllocateIncome(allocateIncomeReturn.getLeftAllocateIncome());// 未分配金额
		im.setSuccessAllocateInvestors(im.getSuccessAllocateInvestors() + successAllocateInvestors);// 成功分配投资者数
		im.setFailAllocateInvestors(allocateIncomeReturn.getFailAllocateInvestors());// 失败分配投资者数
		this.incomeAllocateService.updateIncomeAllocate(im);

		ie.setStatus(allocateIncomeReturn.getStatus());// 分配状态
		this.incomeEventService.updateIncomeEvent(ie);

		/**
		 * 成功分配基础收益金额+成功分配奖励收益金额
		 */
		BigDecimal successAllIncome = successAllocateIncome.add(successAllocateRewardIncome).add(holdInterestMarketingAmount);
		
		// 资产池收益分配成功发送更新产品currentVolume
		try{
			this.productInterfaces.incomeAllocateAdjustCurrentVolume(p.getOid(), successAllIncome, im.getRatio());
		}catch(Exception e){
			logger.info("调用productInterfaces.incomeAllocateAdjustCurrentVolume失败,productOid:"+p.getOid(),e);
		}
		if (successAllIncome.compareTo(new BigDecimal("0")) != 0) {// 成功分配收益金额
			// 会计分录接口
			spvDocumentInterface.incomeAllocate(ie.getAssetPoolOid(), ie.getOid(), successAllIncome);
		}

	}


	@Override
	public Boolean auditFailIncomeAdjust(String oid, String operator) throws BizException{

		IncomeAllocate incomeAllocate = incomeAllocateService.selectByOid(oid);
		IncomeEvent ie = this.incomeEventService.getIncomeEventByOid(incomeAllocate.getEventOid());

		if (ie != null && !IncomeEventStausEnum.CREATE.value.equals(ie.getStatus())) {
			logger.error("auditFailIncomeAdjust,待审核状态才能审核!oid{},operator{}", oid, operator);
			throw new BizException(60007, "待审核状态才能审核!");
		}

		ie.setAuditor(operator);
		ie.setAuditTime(new Date());
		ie.setStatus(IncomeEventStausEnum.FAIL.value);
		this.incomeEventService.insert(ie);

		return true;
	}

	@Override
	public IncomeAllocate deleteIncomeAdjust(String oid, String operator) throws BizException{

		IncomeAllocate incomeAllocate = incomeAllocateService.selectByOid(oid);

		IncomeEvent ie = this.incomeEventService.getIncomeEventByOid(incomeAllocate.getEventOid());

		if (ie != null && !IncomeEventStausEnum.CREATE.value.equals(ie.getStatus())
				&& !IncomeEventStausEnum.FAIL.value.equals(ie.getStatus())) {
			
			throw new BizException(60008, "");
		}

		ie.setAuditor(operator);
		ie.setAuditTime(new Date());
		ie.setStatus(IncomeEventStausEnum.DELETE.value);
		incomeEventService.updateIncomeEvent(ie);
		return incomeAllocate;
	}

	@Override
	public Boolean allocateIncomeAgain(String oid, String operator)throws BizException {
		logger.info("allocateIncomeAgain:oid:{},operator:{}",oid,operator);
		IncomeAllocate incomeAllocate = incomeAllocateService.selectByOid(oid);
		IncomeEvent ie = this.incomeEventService.getIncomeEventByOid(incomeAllocate.getEventOid());

		if (ie != null && !IncomeEventStausEnum.ALLOCATEFAIL.value.equals(ie.getStatus())) {
			logger.error("allocateIncomeAgain,只有分配失败的收益分配才可以再次发送!oid{},operator{}", oid, operator);
			throw new BizException(60009, "只有分配失败的收益分配才可以再次发送!");
		}

		ie.setStatus(IncomeEventStausEnum.ALLOCATING.value);// 发放中

		this.incomeEventService.updateIncomeEvent(ie);

		// 发放收益
		this.interestRateMethodService.interest(incomeAllocate.getProductOid(), incomeAllocate.getOid(),
				incomeAllocate.getAllocateIncome(), incomeAllocate.getRatio(), incomeAllocate.getBaseDate());

		return true;
	}

	@Override
	public IncomeAllocateAssetResp getIncomeAllocateAsset(String assetPoolOid) throws BizException{
		logger.info("getIncomeAllocateAsset assetPoolOid:{}", assetPoolOid);
		IncomeAllocateAssetResp resp = new IncomeAllocateAssetResp();

		resp.setAssetpoolOid(assetPoolOid);

		List<String> accounts = new ArrayList<String>();
		accounts.add("1111");
		accounts.add("1201");
		accounts.add("2201");
		Map<String, AccountBook> accountBookMap = accountBookInterface.find(assetPoolOid, accounts).getData();

		if (accountBookMap != null && accountBookMap.size() > 0) {
			// 资产池
			AccountBook investmentAssets = accountBookMap.get("1111");// 资产池
																		// 投资资产
			if (investmentAssets != null) {
				resp.setInvestmentAssets(ProductDecimalFormat.format(investmentAssets.getBalance(), "0.##"));
			}
			AccountBook apUndisIncome = accountBookMap.get("2201");// 资产池 未分配收益
			if (apUndisIncome != null) {
				resp.setApUndisIncome(ProductDecimalFormat.format(apUndisIncome.getBalance(), "0.##"));
			}
			AccountBook apReceiveIncome = accountBookMap.get("1201");// 资产池
																		// 应收投资收益
			if (apReceiveIncome != null) {
				resp.setApReceiveIncome(ProductDecimalFormat.format(apReceiveIncome.getBalance(), "0.##"));
			}
		}

		List<IncomeAllocateHisResp> incomeAllocateHis = new ArrayList<IncomeAllocateHisResp>();// 该资产池下产品最新一条历史收益分配记录
		List<JSONObject> products = new ArrayList<JSONObject>();// 该资产池下需要收益分配的产品列表

		List<RewardIsNullRep> reps = rewardIncomePracticeService.rewardIsNullReps(assetPoolOid, null);
		logger.info("getIncomeAllocateAsset:返回{} 条",reps.size());
		if (reps != null && reps.size() > 0) {
			JSONObject obj = null;
			List<String> productOids = new ArrayList<String>();
			
			for (RewardIsNullRep rep : reps) {
				if (rep.getProductOid() != null) {
					productOids.add(rep.getProductOid());
					
					String productName = productInterfaces.selectProductByOid(rep.getProductOid()).getData().getName();
					
					obj = new JSONObject();
					obj.put("oid", rep.getProductOid());
					 obj.put("name", productName);
					products.add(obj);

					IncomeAllocateHisResp idr = new IncomeAllocateHisResp();
					idr.setProductOid(rep.getProductOid());
					 idr.setProductName(productName);
					incomeAllocateHis.add(idr);
				}
			}
			if (productOids.size() > 0) {
				List<IncomeAllocate> ims = this.incomeAllocateService.findLatestIncomeAllocates(productOids);
				if (ims != null && ims.size() > 0) {
					Map<String, IncomeAllocate> incomeAllocateMap = new HashMap<String, IncomeAllocate>();
					for (IncomeAllocate iam : ims) {
						incomeAllocateMap.put(iam.getProductOid(), iam);
					}
					IncomeAllocate ia = null;
					for (IncomeAllocateHisResp r : incomeAllocateHis) {
						ia = incomeAllocateMap.get(r.getProductOid());
						if (ia != null) {
							r.setOid(ia.getOid());// 收益分配表的oid
							r.setProductCapital(ia.getCapital());// 产品总规模（元）
							r.setRewardIncome(ia.getSuccessAllocateRewardIncome());// 奖励收益（元）
							r.setBaseDate(ia.getBaseDate()); // 基准日(上一收益派发日))
							r.setRatio(ia.getRatio());// 收益率(最近一次基准收益率)
														// 0.01（代表1%）
						}
					}
				}

			}
		}

		resp.setIncomeAllocateHis(incomeAllocateHis);
		resp.setProducts(products);

		return resp;
	}

	@Override
	public IncomeAllocateProductResp getIncomeAllocateProduct(String assetPoolOid, String productOid)throws BizException {
		logger.info("getIncomeAllocateProduct assetPoolOid:{};productOid:{}",assetPoolOid,productOid);
		IncomeAllocateProductResp resp = new IncomeAllocateProductResp();

		Product p = productInterfaces.selectProductByOid(productOid).getData();
		resp.setProductOid(p.getOid());
		resp.setIncomeCalcBasis(p.getIncomeCalcBasis());
		resp.setAssetPoolOid(assetPoolOid);
		if(p.getSubType()!=null){
			resp.setSubType(p.getSubType().toString());
		}
		//查询收益分派事件表，t_Gam_assetpool_event表，最近一天的有效信息，根据资产池，
		// 查询该产品以及该产品的资产池最近一天的收益分配日 非 IncomeEvent.STATUS_Fail 和 非 IncomeEvent.STATUS_Delete
		IncomeAllocate lastIncomeAllocate = this.incomeAllocateService.findLastValidIncomeAllocate(productOid);

		
		logger.info("getIncomeAllocateProduct lastIncomeAllocate:{}",JsonUtils.writeValue(lastIncomeAllocate));
		//当为null的时候表示还没有分配过，为首次分配
		if (lastIncomeAllocate == null) {// 首次分配收益
			resp.setIncomeDate("");// 收益分配日
			resp.setLastIncomeDate("");// 上一收益分配日
			resp.setPromptCode(IncomeAllocateProductResp.PROMPT_CODE_FIRST);
			resp.setPromptMessage(IncomeAllocateProductResp.PROMPT_MESSAGE_FIRST);
		} else {// 非首次分配收益
			IncomeEvent lastIncomeEvent = this.incomeEventService.getIncomeEventByOid(lastIncomeAllocate.getEventOid());
			
			
			
			resp.setLastIncomeDate(DateUtil.format(lastIncomeEvent.getBaseDate()));// 上一收益分配日
			resp.setIncomeDate(DateUtil.format(lastIncomeEvent.getBaseDate()));// 收益分配日
			if (IncomeEventStausEnum.CREATE.value.equals(lastIncomeEvent.getStatus())) {
				resp.setPromptCode(IncomeAllocateProductResp.PROMPT_CODE_Create);
				resp.setPromptMessage(IncomeAllocateProductResp.PROMPT_MESSAGE_Create);
			} else if (IncomeEventStausEnum.ALLOCATING.value.equals(lastIncomeEvent.getStatus())) {
				resp.setPromptCode(IncomeAllocateProductResp.PROMPT_CODE_Allocating);
				resp.setPromptMessage(IncomeAllocateProductResp.PROMPT_MESSAGE_Allocating);
			} else if (IncomeEventStausEnum.ALLOCATEFAIL.value.equals(lastIncomeEvent.getStatus())) {
				resp.setPromptCode(IncomeAllocateProductResp.PROMPT_CODE_AllocateFail);
				resp.setPromptMessage(IncomeAllocateProductResp.PROMPT_MESSAGE_AllocateFail);
			} else {
				if (lastIncomeEvent.getBaseDate().getTime() == DateUtil.getCurrDate().getTime()) {
					resp.setPromptCode(IncomeAllocateProductResp.PROMPT_CODE_Allocated);
					resp.setPromptMessage(IncomeAllocateProductResp.PROMPT_MESSAGE_Allocated);
				} else {
					Date incomeDate = DateUtil.addSQLDays(lastIncomeEvent.getBaseDate(), 1);

					Date today = DateUtil.getCurrDate();
					if (incomeDate.getTime() > today.getTime()) {
						resp.setPromptCode(IncomeAllocateProductResp.PROMPT_CODE_Nonapply);
						resp.setPromptMessage(IncomeAllocateProductResp.PROMPT_MESSAGE_Nonapply);
					} else {
						resp.setPromptCode(IncomeAllocateProductResp.PROMPT_CODE_Apply);
						resp.setPromptMessage(IncomeAllocateProductResp.PROMPT_MESSAGE_Apply);
					}
					//计算收益分配日，在上一个分配日的基础上加一天
					resp.setIncomeDate(DateUtil.format(incomeDate));// 收益分配日
					
        			RewardIsNullRep practice = null;//产品总规模 王国处获取 奖励收益 王国处获取
					SerFeeQueryRep fee = null;//某一天的乐视服务费
					BigDecimal feeValue = null;// 计提费用
					
					String feeDate ="";
					  if (incomeDate.getTime() == today.getTime()) {
						  Date yesterday =DateUtil.getBeforeDate();
						  
						  feeDate=DateUtil.format(yesterday);
					  }else{
						  feeDate= DateUtil.format( incomeDate);
					  }
					//根据产品ID，批次，查询服务费 T_MONEY_LX_SERFEE表
					  Message<SerFeeQueryRep> feeMsg = moneyLxSerfeeInterfaces.findFeeByDate(p.getOid(),  feeDate);// 乐视服务费
					  
					  if(!Messages.isSuccess(feeMsg)){
						  logger.error("getIncomeAllocateProduct：查询findFeeByDate乐视服务费异常,productOid:{},feeDate:{}",p.getOid(),  feeDate);
						  throw new BizException("查询乐视服务费异常");
					  }
					  fee=feeMsg.getData();
						logger.info("getIncomeAllocateProduct 乐视服务费用 productId:{},yesterday:{},feeValue:{}",p.getOid(),feeDate,JsonUtils.writeValue(fee));
						
					  practice = rewardIncomePracticeService.rewardIsNullRep(productOid, feeDate);// 查询产品规模（持有人总份额），以及奖励收益等信息T_MONEY_PUBLISHER_PRODUCT_REWARDINCOMEPRACTICE，
						if (practice != null) {
							logger.info("getIncomeAllocateProduct,productId:{},yesterday:{},持有人总分额:{},奖励收益:{}",p.getOid(),feeDate, practice.getTotalHoldVolume(),practice.getTotalRewardIncome());
						}
						
						 Message<BigDecimal> feeValueMsg = gamAssetpoolEstimateInterfaces.feeCalac(assetPoolOid, feeDate);
						 if(!Messages.isSuccess(feeValueMsg)){
							 logger.error("getIncomeAllocateProduct：查询feeCalac异常,assetPoolOid:{},feeDate:{}",assetPoolOid,  feeDate);
							  throw new BizException("查询FeeValue乐视服务费异常");
						 }
						 
						 feeValue = feeValueMsg.getData();
						logger.info("getIncomeAllocateProduct 计提费用 assetPoolOid:{},yesterday:{},feeValue:{}",assetPoolOid,feeDate,feeValue);
					
                    if (fee != null) {
						resp.setLetvServiceFee(fee.getFee());
					}
                    if (practice != null) {
						if (practice.getTotalHoldVolume() != null) {// 持有人总份额 产品总规模
							resp.setProductTotalScale(practice.getTotalHoldVolume());
						}
						if (practice.getTotalRewardIncome() != null) {// 奖励收益
							resp.setProductRewardBenefit(practice.getTotalRewardIncome());
						}
					}
                    if (feeValue != null) {
						resp.setFeeValue(feeValue);
					}
				}
			}
		}
		return resp;
	}
	
	
	

}
