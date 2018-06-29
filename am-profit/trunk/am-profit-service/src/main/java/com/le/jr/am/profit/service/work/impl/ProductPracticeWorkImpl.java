package com.le.jr.am.profit.service.work.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.le.jr.am.profit.service.CallDubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.le.jr.am.order.domain.enums.TaskCodeEnum;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.product.domain.enums.ProductStateEnum;
import com.le.jr.am.product.domain.vo.SearchProductVo;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.common.util.DecimalUtil;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.RewardIncomePractice;
import com.le.jr.am.profit.domain.enums.HoldApartAccrualStatus;
import com.le.jr.am.profit.domain.enums.HoldApartHoldStatus;
import com.le.jr.am.profit.domain.enums.HoldStatusEnum;
import com.le.jr.am.profit.domain.enums.PracticeIsAllocateInterestEnum;
import com.le.jr.am.profit.domain.vo.PracticeParams;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.am.profit.service.RewardIncomePracticeService;
import com.le.jr.am.profit.service.config.ProfitDiamondService;
import com.le.jr.am.profit.service.work.ProductPracticeWork;
import com.le.jr.am.task.domain.JobLock;
import com.le.jr.am.task.domain.SerialTaskReq;
import com.le.jr.am.task.domain.enums.JobLockStatusEnum;
import com.le.jr.am.task.domain.enums.SerialTaskCodeEnum;
import com.le.jr.am.task.interfaces.JobLockInterface;
import com.le.jr.am.task.interfaces.SerialTaskInterface;
import com.le.jr.trade.openapi.common.util.StringUtil;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;

@Service("productPracticeWork")
public class ProductPracticeWorkImpl implements ProductPracticeWork {

	private static final Logger logger = LoggerFactory.getLogger(ProductPracticeWorkImpl.class);

	@Resource
	private JobLockInterface jobLockInterface;

	@Resource
	private SerialTaskInterface serialTaskInterface;

	@Resource
	private ProductInterfaces productInterfaces;

	@Resource
	private ProfitDiamondService profitDiamondService;

	@Resource
	private RewardIncomePracticeService rewardIncomePracticeService;

	@Resource
	private HoldService holdService;

	@Resource
	private HoldApartService holdApartService;

	@Resource
	private PlatformTransactionManager platformTransactionManager;
	
	@Resource
	private CallDubboService callDubboService;

	/**
	 * 判断计息快照是否已经完成了， 2016年10月20日杜建君添加注释
	 * 
	 * @param batchCode
	 *            批次号
	 * @param jobId
	 *            跑批的任务类型关键字
	 */
	@Override
	public void isSnapshotVolume(String batchCode, String jobId) {
		logger.info("isSnapshotVolume开始查询batchCode：{}，jobId：{}", batchCode, jobId);
		Message<JobLock> msg = this.jobLockInterface.findByBatchCodeAndJobId(batchCode, jobId);

		if (!(Messages.isSuccess(msg) && msg.getData() != null)) {
			logger.error("isSnapshotVolume查询失败，batchCode：{}，jobId：{}", batchCode, jobId);
			throw new BizException("isSnapshotVolume查询失败");
		}
		if (!JobLockStatusEnum.DONE.value.equals(msg.getData().getJobStatus())) {
			logger.error("isSnapshotVolume计息快照尚未完成，batchCode：{}，jobId：{}", batchCode, jobId);
			throw new BizException("isSnapshotVolume计息快照尚未完成");
		}
	}

	@Override
	public void practiceOneProduct(PracticeParams vo) {
		
		if(null==vo){
			logger.error("=====试算产品参数为空====");
			throw new BizException("试算产品参数为空");
		}
		
		String productOid = vo.getProductOid();
		String incomeDate = vo.getIncomeDate();
		String assetPoolOid = vo.getAssetPoolOid();

		try {
			Product product=callDubboService.callSelectProductByOid(productOid).getData();
			
			Date incomeDate1 = new SimpleDateFormat("yyyyMMdd").parse(incomeDate);
			// 相应产品有无奖励规则
			Map<String, RewardIncomePractice> map = this.getAllRewardEntity(productOid, assetPoolOid, incomeDate);

			logger.info("practiceOneProduct 产品 productOid:{}查询奖励规则完成", productOid);

			if (null == map || map.isEmpty()) {
				logger.info("practiceOneProduct产品.{}.无奖励规则", productOid);
			}
			// 是否已试算奖励规则 ，看看是否已经执行过该跑批任务，根据批次，以及项目ID进行查询，如果查到了，就是已经执行过了
			int count = rewardIncomePracticeService.countByProductAndTDate(productOid, incomeDate);
			if (count != 0) {
				logger.info("practiceOneProduct产品{}--{}奖励规则已经试算", productOid, incomeDate);
				return;
			}
			logger.info("practiceOneProduct 产品 productOid:{}试算开始", productOid);
			String lastHoldOid = "0";
			while (true) {

				List<Hold> holdList = holdService.findByProductHoldStatus4lx(productOid, HoldStatusEnum.HOLDING.value, lastHoldOid);

				if (holdList.isEmpty()) {
					logger.info("practiceOneProduct:holdList为空，productCode{}", productOid);
					break;
				}

				for (Hold hold : holdList) {
					this.processOneItem(incomeDate1, product, map, hold.getOid());
					lastHoldOid = hold.getOid();
				}
				logger.info("practiceOneProduct 产品 productOid:{}试算部分数据处理完成", productOid);
			}

			BigDecimal totalRewardIncome = BigDecimal.ZERO;
			BigDecimal totalHoldVolume = BigDecimal.ZERO;
			BigDecimal interestedVolume = BigDecimal.ZERO;
			BigDecimal toInterestVolume = BigDecimal.ZERO;
			BigDecimal toConfirmVolume = BigDecimal.ZERO;

			logger.info("practiceOneProduct 产品 productOid:{}试算,practiceMap :{}", productOid, JsonUtils.writeValue(map));
			for (RewardIncomePractice practice : map.values()) {
				totalHoldVolume = totalHoldVolume.add(practice.getTotalHoldVolume());
				interestedVolume = interestedVolume.add(practice.getInterestedVolume());
				toInterestVolume = toInterestVolume.add(practice.getToInterestVolume());
				toConfirmVolume = toConfirmVolume.add(practice.getToConfirmVolume());
				totalRewardIncome = totalRewardIncome.add(practice.getTotalRewardIncome());
			}
			// 数据汇总根据分仓记录查询收益日期的总持仓
			totalHoldVolume = this.holdApartService.getTotalCountByProduct4Practice(productOid, incomeDate);
			interestedVolume = this.holdApartService.getCountByProduct4Practice(productOid, incomeDate);

			RewardIncomePractice practice = new RewardIncomePractice();
			practice.setOid(UUIDUtil.creatUUID());
			practice.setProductOid(productOid);
			practice.settDate(incomeDate1);
			practice.setUpdateTime(DateUtil.getSqlCurrentDate());
			practice.setCreateTime(DateUtil.getSqlCurrentDate());
			practice.setTotalHoldVolume(null == totalHoldVolume ? BigDecimal.ZERO : totalHoldVolume);
			practice.setTotalRewardIncome(totalRewardIncome);
			practice.setInterestedVolume(interestedVolume);
			practice.setToInterestVolume(toInterestVolume);
			practice.setToConfirmVolume(toConfirmVolume);
			practice.setTotalRewardIncome(totalRewardIncome);
			practice.setAssetPoolOid(assetPoolOid);

			// 事物控制开始，开始添加 增加事务
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			TransactionStatus status = platformTransactionManager.getTransaction(definition);
			try {

				// 每个阶梯试算结果
				for (RewardIncomePractice practice1 : map.values()) {
					practice1.setUpdateTime(new Date());
					rewardIncomePracticeService.insert(practice1);
				}
				// 总试算结果
				
				rewardIncomePracticeService.insert(practice);
				
			} catch (Exception e) {
				logger.error("更新试算结果失败。。。productOid" + productOid);
				platformTransactionManager.rollback(status);
				throw e;
			}
			logger.info("产品 productOid:{}试算完成", productOid);

			platformTransactionManager.commit(status);
		} catch (Exception e) {
			logger.error("practiceOneProduct:error info :e==={}", e);
			throw new BizException("产品"+productOid+"试算异常");
		}

	}

	/**
	 * 创建试算任务
	 */
	@Override
	public void createPracticeSerial(Product product, Date incomeDate1,String batchCode) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String incomeDate = sf.format(incomeDate1);
		logger.info("createPracticeSerial incomeDate={} batchCode=={}", incomeDate,batchCode);
		SerialTaskReq<PracticeParams> req = new SerialTaskReq<PracticeParams>();
		req.setTaskCode(SerialTaskCodeEnum.PRACTICE.value);

		PracticeParams vo = new PracticeParams();
		vo.setProductOid(product.getOid());
		vo.setAssetPoolOid(product.getAssetPoolOid());
		vo.setIncomeDate(incomeDate);
		vo.setBatchCode(batchCode);
		
		req.setTaskParams(vo);
		
		logger.info("createPracticeSerial params=={}",JsonUtils.writeValue(vo));
		this.serialTaskInterface.createSerialTask(req);

	}

	/**
	 * 查询可以参与试算的产品
	 */
	@Override
	public List<Product> queryCanPracticeProduct() {

		SearchProductVo searchProductVo = new SearchProductVo();
		List<ProductStateEnum> list = new ArrayList<ProductStateEnum>();
		list.add(ProductStateEnum.RAISING);
		list.add(ProductStateEnum.CLEARING);
		searchProductVo.setProductStateEnums(list);
		// 查询计息中的产品列表
		Message<List<Product>> msg1 = productInterfaces.selectProductByVo(searchProductVo);
		if (!(Messages.isSuccess(msg1) && msg1.getData() != null)) {
			logger.error("practice productInterfaces selectProduct failed searchVo={} msg={}", JsonUtils.writeValue(searchProductVo),
					JsonUtils.writeValue(msg1));
			throw new BizException("practice productInterfaces selectProduct failed..");
		}
		// 产品试算
		List<Product> productList = msg1.getData();
		
		//过滤掉配置系统里不需要试算的产品
		String notPracticeProducts = (String) profitDiamondService.getMessage("notPracticeProducts");

		List<Product> result = new ArrayList<Product>();

		for (Product product : productList) {

			if (StringUtil.isNotBlank(notPracticeProducts)) {
				boolean notPraFlag = false;
				String[] nots = notPracticeProducts.split(",");
				for (String notPractice : nots) {
					if (product.getOid().equals(notPractice)) {
						logger.info("practice productOid:{},未试算，无利息派送", notPractice);
						notPraFlag = true;
						break;
					}
				}

				if (notPraFlag) {
					continue;
				}

			}

			result.add(product);

		}

		return result;
	}

	/**
	 * 获取产品所有的奖励阶梯列表（需要做优化，添加内存缓存）
	 * @param productOid
	 * @param assetPoolOid
	 * @param incomeDate
	 * @return
	 */
	private Map<String, RewardIncomePractice> getAllRewardEntity(String productOid, String assetPoolOid, String incomeDate) {
		Map<String, RewardIncomePractice> map = new HashMap<String, RewardIncomePractice>();
		Message<List<ProductIncomeReward>> msgRs = productInterfaces.selectProductRewardByProductOid(productOid);
		if (!Messages.isSuccess(msgRs) || msgRs.getData() == null) {
			logger.info("getAllRewardEntity productInterfaces selectProductDetail failed oid={}", productOid);
			throw new BizException("getAllRewardEntity productInterfaces selectProductDetail failed");
		}
		List<ProductIncomeReward> rs = msgRs.getData();
		for (ProductIncomeReward reward : rs) {
			RewardIncomePractice practice = new RewardIncomePractice();
			practice.setProductOid(productOid);
			practice.setRewardRuleOid(reward.getOid());
			try {
				practice.settDate(new SimpleDateFormat("yyyyMMdd").parse(incomeDate));
			} catch (ParseException e) {

			}
			practice.setOid(UUIDUtil.creatUUID());
			practice.setAssetPoolOid(assetPoolOid);
			practice.setUpdateTime(DateUtil.getSqlCurrentDate());
			practice.setCreateTime(DateUtil.getSqlCurrentDate());
			practice.setTotalHoldVolume(BigDecimal.ZERO);
			practice.setTotalRewardIncome(BigDecimal.ZERO);
			practice.setIsAllocateInterest(PracticeIsAllocateInterestEnum.NO.value);
			map.put(reward.getOid(), practice);

		}

		return map;
	}

	/**
	 * 查询并处理总仓用户下的分仓结果
	 * @param incomeDate
	 * @param productOid
	 * @param map
	 * @param holdOid
	 */
	private void processOneItem(Date incomeDate, Product product, Map<String, RewardIncomePractice> map, String holdOid) {

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String incomeDateStr = sf.format(incomeDate);
		List<HoldApart> apartList = holdApartService.findInterestableApart(holdOid, incomeDateStr);

		for (HoldApart apart : apartList) {
			// 奖励收益
			int holdDays = DateUtil.daysBetween(incomeDate, apart.getBeginAccuralDate()) + 1;
			Message<ProductIncomeReward> rewardMessage = productInterfaces.getRewardEntity(product.getOid(), holdDays, apart.getLockRewardOid(),product.getSubType());

			if (!Messages.isSuccess(rewardMessage)) {
				logger.error("processOneItem:查询奖励信息异常");
				throw new BizException("查询奖励信息异常");
			}
			ProductIncomeReward reward = rewardMessage.getData();

			BigDecimal rewardAmount = BigDecimal.ZERO;

			
			if (null != reward) { // 奖励金额
				 logger.info("processOneItem:apart.oid=={},rewardoid=={},rewardDradio=={}", apart.getOid(),reward.getOid(),reward.getDratio());
				rewardAmount = this.getRewardAmount(reward.getDratio(), apart.getSnapshotVolume());
				RewardIncomePractice practiceEntity = map.get(reward.getOid());
				practiceEntity.setTotalRewardIncome(rewardAmount.add(practiceEntity.getTotalRewardIncome()));

				if (!DateUtil.isEqualDay(apart.getCreateTime(), DateUtil.getSqlDate())) {
					practiceEntity.setTotalHoldVolume(practiceEntity.getTotalHoldVolume().add(apart.getInvestVolume()));
				}

				if (HoldApartAccrualStatus.YES.value.equals(apart.getAccrualStatus())) {
					practiceEntity.setInterestedVolume(practiceEntity.getInterestedVolume().add(apart.getSnapshotVolume()));
				}

				if (HoldApartAccrualStatus.NO.value.equals(apart.getAccrualStatus())
						&& (HoldApartHoldStatus.HOLDING.value.equals(apart.getHoldStatus()) || HoldApartHoldStatus.PARTHOLDING.value
								.equals(apart.getHoldStatus()))) {
					practiceEntity.setToInterestVolume(practiceEntity.getToInterestVolume().add(apart.getInvestVolume()));
				}

				if (HoldApartAccrualStatus.NO.value.equals(apart.getAccrualStatus())
						&& HoldApartHoldStatus.TOCONFIRM.value.equals(apart.getHoldStatus())) {
					if (!DateUtil.isEqualDay(apart.getCreateTime(), DateUtil.getCurrDate())) {
						practiceEntity.setToConfirmVolume(practiceEntity.getToConfirmVolume().add(apart.getInvestVolume()));
					}
				}
			}else{
				 logger.info("processOneItem:apart.oid=={},reward为空", apart.getOid());
			}

		}

	}

	/**
	 * 处理金额
	 * @return BigDecimal
	 * @author yuechao
	 */
	private BigDecimal getRewardAmount(BigDecimal dRatio, BigDecimal volume) {
		return DecimalUtil.setScaleDown(dRatio.multiply(volume));

	}

}
