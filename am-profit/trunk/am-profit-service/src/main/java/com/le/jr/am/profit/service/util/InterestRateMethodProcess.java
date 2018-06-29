package com.le.jr.am.profit.service.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.order.domain.enums.OrderSubTypeEnum;
import com.le.jr.am.order.domain.order.vo.InvestOrderVo;
import com.le.jr.am.order.interfaces.OrderInterface;
import com.le.jr.am.profit.domain.*;
import com.le.jr.am.profit.domain.enums.MarketingHoldTypeEnum;
import com.lejr.marketing.promotion.rpc.api.PromotionServiceRpc;
import com.lejr.marketing.promotion.rpc.api.param.PromotionCalculatingParam;
import com.lejr.marketing.promotion.rpc.api.vo.AccountingInfo;
import com.lejr.marketing.promotion.rpc.api.vo.PromotionInterestVo;
import com.lejr.platform.common.rpc.RpcResult;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.le.jr.am.order.common.util.JsonUtil;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.product.domain.enums.ProductSubTypeEnum;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.common.util.DecimalUtil;
import com.le.jr.am.profit.common.util.FormulaUtil;
import com.le.jr.am.profit.common.util.RocketMQMessageUtil;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.HoldApartIncome;
import com.le.jr.am.profit.domain.HoldIncome;
import com.le.jr.am.profit.domain.LevelIncome;
import com.le.jr.am.profit.domain.enums.HoldApartRedeemStatus;
import com.le.jr.am.profit.domain.output.InterestRep;
import com.le.jr.am.profit.domain.output.OutInMqVo;
import com.le.jr.am.profit.domain.output.ProcessAccountMQ;
import com.le.jr.am.profit.service.CallDubboService;
import com.le.jr.am.profit.service.HoldApartIncomeService;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.HoldIncomeService;
import com.le.jr.am.profit.service.HoldLevelIncomeService;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;

@Service("interestRateMethodProcess")
public class InterestRateMethodProcess {

	Logger logger = LoggerFactory.getLogger(InterestRateMethodProcess.class);

	@Resource
	private HoldApartService holdApartService;

	@Resource
	private HoldService holdService;

	@Resource
	private ProductInterfaces productInterfaces;

	@Resource
	private HoldApartIncomeService holdApartIncomeService;

	@Resource
	private HoldLevelIncomeService holdLevelIncomeService;

	@Resource
	private CallDubboService callDubboService;

	@Resource
	private HoldIncomeService holdIncomeService;

	@Value("${mq.amprofit.accountant_topic}")
	private String accountTopic;

	@Value("${mq.amprofit.accountant_process}")
	private String process;

	@Value("${mq.amprofit.outInTopic}")
	private String outInTopic;

	@Value("${mq.amprofit.outInTag}")
	private String outInTag;

	@Resource
	private DefaultMQProducer producer;

	@Resource
	private PromotionServiceRpc promotionServiceRpc;


	@Resource
	private OrderInterface orderInterface;

	/**
	 * 编程式事务
	 */
	@Resource
	private PlatformTransactionManager transactionManager;

	/**
	 *
	 * @param product
	 * @param incomeDate
	 * @param hold
	 * @param netUnitAmount
	 * @param fpRate
	 *            年化利率 0.05 已经除以100
	 * @return
	 */
	public InterestRep process(Product product, Date incomeDate, Hold hold, BigDecimal netUnitAmount, BigDecimal fpRate) {
		logger.info("productOid:{},fpRate:{}",product.getOid(),fpRate);
		InterestRep iRep = new InterestRep();

		BigDecimal holdInterestVolume = BigDecimal.ZERO; // 合仓利息份额
		BigDecimal holdInterestAmount = BigDecimal.ZERO; // 合仓利息金额
		BigDecimal holdInterestBaseAmount = BigDecimal.ZERO; // 合仓基础利息金额
		BigDecimal holdInterestRewardAmount = BigDecimal.ZERO; // 合仓奖励利息金额
		BigDecimal holdInterestMarketingAmount = BigDecimal.ZERO;//合仓营销利息金额
		BigDecimal holdAccuralVolume = BigDecimal.ZERO; // 合仓计息份额

		BigDecimal lockHoldInterestVolume = BigDecimal.ZERO; // 合仓锁定利息份额
		BigDecimal lockHoldInterestAmount = BigDecimal.ZERO; // 合仓锁定利息金额

		HoldIncome investorIncomeEntity = new HoldIncome(); // 发行人-投资人-合仓收益明细
		List<HoldApartIncome> partIncomeList = new ArrayList<HoldApartIncome>();
		Map<String, LevelIncome> rulesLevelMap = new HashMap<String, LevelIncome>();

		//查询此人 基准日的可派息分仓列表（ 查找可分配收益的分仓列表 起息日，状态，份额 ，总仓ID）
		List<HoldApart> apartList = holdApartService.findInterestableApart(hold.getOid(), DateUtil.format(incomeDate));

		List<ProcessAccountMQ> mqList = new ArrayList<ProcessAccountMQ>();
		List<OutInMqVo> outInMqList=new ArrayList<OutInMqVo>();
		// 事物控制开始，开始添加 增加事务
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(definition);




		try {
			//记录总仓收益记录（只添加oid，如果失败则回滚删除）
			investorIncomeEntity.setOid(UUIDUtil.creatUUID());
			holdIncomeService.saveEntity(investorIncomeEntity);

			for (HoldApart apart : apartList) {
				//是否营销单的持仓
				Integer marketingHold  =apart.getMarketingHold();
				// 奖励阶梯 计息日开始到基准日一共多少天，查询奖励阶梯
				logger.info("apartOid:{},orderOid:{}",apart.getOid(),apart.getOrderOid());
				int holdDays=DateUtil.daysBetween(incomeDate, apart.getBeginAccuralDate()) + 1;
				logger.info("apartOid:{},holdDays:{}",apart.getOid(),holdDays);
				Message<ProductIncomeReward> rewardmsg = callDubboService.callGetRewardEntity(product.getOid(),holdDays,apart.getLockRewardOid(),product.getSubType());
				if (!Messages.isSuccess(rewardmsg)) {
					logger.error("process派息失败，查询产品阶梯失败");
					throw new BizException("process派息失败，查询产品阶梯失败");
				}
				ProductIncomeReward reward = rewardmsg.getData();
				logger.info("apartOid:{},rewardmsg:{}",apart.getOid(),JsonUtil.objectToJson(rewardmsg));
				//自动赎回的赎回当日不计息
				if(!this.checkExpiredAutoRedeem(product,apart,reward)){
					logger.info("apartOid:{},check:false",apart.getOid());
					continue;
				}

				//奖励收益
				BigDecimal rewardAmount = BigDecimal.ZERO;
				//营销收益
				BigDecimal marketingAmount = BigDecimal.ZERO;

				//阶梯收益实体
				LevelIncome levelIncomeEntity = null;
				// 基础收益利息金额
				BigDecimal incomeAmount = BigDecimal.ZERO;
				PromotionInterestVo rpcRes = null;
				//当分仓为理财金的时候,目前的虚拟单和普通单分开的模式，所以这里按独立的订单来设计

				HoldApartTypeChangeLog changeLog = null;
				if(MarketingHoldTypeEnum.PLATFORMMARKETING.value==apart.getMarketingHold()){
					try{
						InvestorTradeOrder order = orderInterface.getOrderByOid(apart.getOrderOid()).getData();
						rpcRes =this.interestLcjpRrocess(order,incomeDate,product,apart,reward,fpRate);
						Long interest = rpcRes.getInterest();
						//理财金都是营销收益，没有基础利息和奖励收益
						if(interest!=null&&interest!=0l){
							marketingAmount = BigDecimal.valueOf(interest).divide(BigDecimal.valueOf(100));
						}
						//理财金的生效时间已经结束
						if(rpcRes.getLastPromotion()){
							logger.info("process:holdOid:{},理财金收益时间结束",apart.getOid());
							//设置变量为普通持仓，之后不再调用营销系统

							changeLog = new HoldApartTypeChangeLog();
							changeLog.setOid(UUIDUtil.creatUUID());
							changeLog.setCreateTime(new Date());
							changeLog.setHoldApartOid(apart.getOid());
							changeLog.setOriginalHoldType(marketingHold);
							changeLog.setNewHoldType(MarketingHoldTypeEnum.NORMAL.value);

							marketingHold = MarketingHoldTypeEnum.NORMAL.value;

						}
					}catch (Exception e){
						logger.info("process ,分仓apartOid:"+apart.getOid()+"调用营销接口失败",e);
					}

				}

				if (null != reward) {
					if(product.getSubType()==ProductSubTypeEnum.SELECTED.getValue()){
						BigDecimal rewardAmountTotal = DecimalUtil.setScaleDown(reward.getDratio().multiply(apart.getSnapshotVolume()).multiply(new BigDecimal(holdDays)));
						BigDecimal rewardAmountBefore = DecimalUtil.setScaleDown(reward.getDratio().multiply(apart.getSnapshotVolume()).multiply(new BigDecimal(holdDays-1)));
						rewardAmount = rewardAmountTotal.subtract(rewardAmountBefore);
					}else if(product.getSubType()==ProductSubTypeEnum.FIXED_CURRENT.getValue()){
						int holdDaysInCycle=FormulaUtil.holdDaysInCycle(reward.getEndDate(), holdDays);
						BigDecimal rewardAmountTotal = DecimalUtil.setScaleDown(reward.getDratio().multiply(apart.getSnapshotVolume()).multiply(new BigDecimal(holdDaysInCycle)));
						BigDecimal rewardAmountBefore = DecimalUtil.setScaleDown(reward.getDratio().multiply(apart.getSnapshotVolume()).multiply(new BigDecimal(holdDaysInCycle-1)));
						rewardAmount = rewardAmountTotal.subtract(rewardAmountBefore);
					}else{
						rewardAmount = DecimalUtil.setScaleDown(reward.getDratio().multiply(apart.getSnapshotVolume()));
					}
					logger.info("apartOid:{},rewardAmount:{}",apart.getOid(),rewardAmount);
				}
				// <<本金>>基础收益利息金额
				incomeAmount= DecimalUtil.setScaleDown(apart.getSnapshotVolume().multiply(netUnitAmount).multiply(fpRate)
						.divide(new BigDecimal(product.getIncomeCalcBasis()), 10, DecimalUtil.roundMode));

				logger.info("apartOid:{},incomeAmount:{},rewardAmount:{}",apart.getOid(),incomeAmount);
				// <<本金基础收益利息+奖励利息>>分仓单笔总利息金额
				BigDecimal apartInerestAmount = incomeAmount.add(rewardAmount).add(marketingAmount);
				// <<本金+奖励>>利息份额
				BigDecimal apartInterestVolume = apartInerestAmount.divide(netUnitAmount, DecimalUtil.scale, DecimalUtil.roundMode);

				if (null != reward) {
					// 创建<<发行人-投资人-阶段收益明细>>
					levelIncomeEntity = rulesLevelMap.get(reward.getOid());

					if (null != levelIncomeEntity) {
						levelIncomeEntity.setIncomeAmount(levelIncomeEntity.getIncomeAmount().add(apartInerestAmount)); // 收益金额
						levelIncomeEntity.setAccureVolume(levelIncomeEntity.getAccureVolume().add(apart.getSnapshotVolume())); // 计息份额
						levelIncomeEntity.setValue(levelIncomeEntity.getValue().add(apart.getInvestVolume().multiply(netUnitAmount))
								.add(apartInerestAmount)); // 市值
					} else {
						levelIncomeEntity = new LevelIncome();
						levelIncomeEntity.setOid(UUIDUtil.creatUUID());
						levelIncomeEntity.setHoldOid(hold.getOid()); // 所属持有人手册
						levelIncomeEntity.setProductOid(product.getOid()); // 所属产品
						levelIncomeEntity.setRewardRuleOid(reward.getOid()); // 所属奖励规则
						levelIncomeEntity.setInvestorOid(apart.getInvestorOid()); // 所属投资人
						levelIncomeEntity.setHoldIncomeOid(investorIncomeEntity.getOid()); // 所属合仓明细
						levelIncomeEntity.setIncomeAmount(apartInerestAmount); // 收益金额
						levelIncomeEntity.setAccureVolume(apart.getSnapshotVolume()); // 计息份额
						levelIncomeEntity.setValue(apart.getInvestVolume().multiply(netUnitAmount).add(apartInerestAmount));
						levelIncomeEntity.setConfirmDate(incomeDate); // 确认日期
						rulesLevelMap.put(reward.getOid(), levelIncomeEntity);
					}
				}

				// 更新<<发行人-持有人分仓>>
				BigDecimal newSnapshotVolume=BigDecimal.ZERO;
				//复利时下一天的计息份额需要加上昨日利息
				if (null != reward && reward.getIncomeCalculationMethod()==(byte)2) {
					logger.info("apartOid:{},复利",apart.getOid());
					newSnapshotVolume=apartInterestVolume;
				}
				//乐定活周期结束计息份额需要置为持有金额
				if(null != reward && product.getSubType()==ProductSubTypeEnum.FIXED_CURRENT.getValue()){
					if(FormulaUtil.isCycleEnd(reward.getEndDate(),apart.getBeginAccuralDate(),incomeDate)){
						String orderCode=callDubboService.callGetOrderByOid(apart.getOrderOid()).getData().getOrderCode();
						logger.info("apartOid:{},乐定活周期结束,orderCode:{}",apart.getOid(),orderCode);
						newSnapshotVolume = DecimalUtil.setScaleDown(reward.getDratio().multiply(apart.getSnapshotVolume()).multiply(new BigDecimal(reward.getEndDate())));
						//发送mq同步转出转入单 
						OutInMqVo outInMqVo=new OutInMqVo();
						outInMqVo.setInvestorOid(apart.getInvestorOid());
						outInMqVo.setProductOid(apart.getProductOid());
						outInMqVo.setInterstAmout((apart.getInvestVolume().add(apartInterestVolume)).multiply(new BigDecimal("100")).longValue());
						outInMqVo.setOrderCode(orderCode);
						outInMqList.add(outInMqVo);
					}
				}
				holdApartService.updateHoldApart4Interest(apart.getOid(), apartInterestVolume, apartInerestAmount, netUnitAmount, incomeDate,
						incomeAmount, rewardAmount,newSnapshotVolume,marketingAmount, marketingHold,changeLog);
				BigDecimal bookAmount = apart.getSnapshotVolume().multiply(netUnitAmount);

				ProcessAccountMQ mes = new ProcessAccountMQ();
				apart.setProduct(product);
				mes.setApart(apart);
				mes.setBookAmount(bookAmount);
				mes.setApartInerestAmount(apartInerestAmount);
				mes.setIncomeAmount(incomeAmount);
				mes.setRewardAmount(rewardAmount);
				mes.setReward(reward);
				mes.setIncomeDate(incomeDate);
				mes.setBaseRatio(fpRate);
				//当营销分仓的时候，发送的mq有变化
				if(MarketingHoldTypeEnum.PLATFORMMARKETING.value==apart.getMarketingHold()&&rpcRes!=null){
					List<AccountingInfo> accountings = rpcRes.getAccountings();
					if(accountings!=null&&accountings.size()!=0){
						AccountingInfo accountingInfo = accountings.get(0);
						mes.setMarketingId(accountingInfo.getMarketingId());
						mes.setAddInterest(accountingInfo.getAddInterest());
						if(accountingInfo.getAwardRate()!=null){
							mes.setAwardRate(new BigDecimal(accountingInfo.getAwardRate()));
						}
						if(accountingInfo.getAwardInterest()!=null){
							mes.setAwardInterest(BigDecimal.valueOf(accountingInfo.getAwardInterest()).divide(new BigDecimal(100)));
						}
						if(accountingInfo.getAwardAmount()!=null){
							mes.setAwardAmount(BigDecimal.valueOf(accountingInfo.getAwardAmount()).divide(new BigDecimal(100)));
						}
					}
				}

				mqList.add(mes);


				// 创建<<发行人-投资人-分仓收益明细>>
				HoldApartIncome partIncomeEntity = new HoldApartIncome();
				partIncomeEntity.setOid(UUIDUtil.creatUUID());
				partIncomeEntity.setHoldOid(hold.getOid()); // 所属持有人手册
				partIncomeEntity.setProductOid(product.getOid()); // 所属产品
				partIncomeEntity.setInvestorOid(apart.getInvestorOid()); // 所属投资人
				partIncomeEntity.setHoldApartOid(apart.getOid()); // 所属分仓
				partIncomeEntity.setHoldIncomeOid(investorIncomeEntity.getOid()); // 所属合仓收益

				if (reward != null) {
					partIncomeEntity.setRewardRuleOid(reward.getOid()); // 所属奖励规则
				}

				if (levelIncomeEntity != null) {
					partIncomeEntity.setLevelIncomeOid(levelIncomeEntity.getOid()); // 所属阶段收益
				}
				partIncomeEntity.setMarketingAmount(marketingAmount);
				partIncomeEntity.setIncomeAmount(incomeAmount); // 收益金额
				partIncomeEntity.setOrderOid(apart.getOrderOid()); // 所属订单
				partIncomeEntity.setRewardAmount(rewardAmount); // 奖励金额
				partIncomeEntity.setAccureVolume(apart.getSnapshotVolume()); // 计息份额
				partIncomeEntity.setConfirmDate(incomeDate); // 确认日期
				partIncomeEntity.setHoldVolume(apart.getInvestVolume());//当前持有
				partIncomeEntity.setOrderVolume(apart.getOrderVolume());//投资单份额
				partIncomeEntity.setMarketingAmount(marketingAmount);//营销收益
				partIncomeEntity.setProvisionInterestVolume(apartInterestVolume);//计提份额
				partIncomeEntity.setCloseInterestVolume(apartInterestVolume);//结算份额
				partIncomeList.add(partIncomeEntity);

				// 累计合仓
				if (HoldApartRedeemStatus.YES.value.equals(apart.getRedeemStatus())) {
					holdInterestVolume = holdInterestVolume.add(apartInterestVolume);
					holdInterestAmount = holdInterestAmount.add(apartInerestAmount);
				} else {
					lockHoldInterestVolume = lockHoldInterestVolume.add(apartInterestVolume);
					lockHoldInterestAmount = lockHoldInterestAmount.add(apartInerestAmount);
				}

				holdAccuralVolume = holdAccuralVolume.add(apart.getSnapshotVolume());
				holdInterestBaseAmount = holdInterestBaseAmount.add(incomeAmount);
				holdInterestRewardAmount = holdInterestRewardAmount.add(rewardAmount);
				holdInterestMarketingAmount = holdInterestMarketingAmount.add(marketingAmount);

				if (null != reward) {
					if (null == iRep.getRewardMap().get(reward.getOid())) {
						iRep.getRewardMap().put(reward.getOid(), apartInerestAmount);
					} else {
						iRep.getRewardMap().put(reward.getOid(), iRep.getRewardMap().get(reward.getOid()).add(apartInerestAmount));
					}
				}

			}
			// 更新<<发行人-持有人手册>>
			holdService.updateHold4Interest(hold.getOid(), holdInterestVolume, holdInterestAmount, lockHoldInterestVolume,
					lockHoldInterestAmount, netUnitAmount, incomeDate, holdInterestBaseAmount, holdInterestRewardAmount, holdInterestMarketingAmount);

			// 创建<<发行人-投资人-合仓收益明细>>
			investorIncomeEntity.setHoldOid(hold.getOid()); // 所属持有人手册
			investorIncomeEntity.setProductOid(product.getOid()); // 所属产品
			investorIncomeEntity.setInvestorOid(hold.getInvestorOid()); // 所属投资人
			investorIncomeEntity.setIncomeAmount(holdInterestAmount.add(lockHoldInterestAmount));
			investorIncomeEntity.setAccureVolume(holdAccuralVolume);
			investorIncomeEntity.setConfirmDate(incomeDate);


			holdIncomeService.updateEntity(investorIncomeEntity);

			List<LevelIncome> listValue = new ArrayList<LevelIncome>();
			Iterator<String> it = rulesLevelMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();

				listValue.add(rulesLevelMap.get(key));
			}

			if(listValue.size()>0){
				// 保存<<发行人-投资人-阶段收益明细>>
				holdLevelIncomeService.saveBatch(listValue);
			}

			// 保存<<发行人-投资人-分仓收益明细>>
			if(partIncomeList!=null&&partIncomeList.size()!=0){
				holdApartIncomeService.saveBatch(partIncomeList);
			}

			//返回基础收益，奖励收益，计提收益，结算收益
			iRep.setHoldInterestBaseAmount(holdInterestBaseAmount);
			iRep.setHoldInterestRewardAmount(holdInterestRewardAmount);
			iRep.setHoldInterestMarketingAmount(holdInterestMarketingAmount);
			iRep.setProvisionInterestAmount(holdInterestAmount);
			iRep.setCloseInterestAmount(holdInterestAmount);


			// 提交事务
			transactionManager.commit(status);
		} catch (Exception e) {
			// 回滚事务
			logger.error("process failed", e);
			transactionManager.rollback(status);
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "派息异常", e);
		}

		//发送会计引擎
		for(ProcessAccountMQ mes:mqList){
			String message = JsonUtils.writeValue(mes);
			logger.info("process：分仓:{},发送MQ信息:{}",mes.getApart().getOid(), message);
			RocketMQMessageUtil.saveMessage(producer, message, accountTopic, process);
		}

		//发送转入转出单mq
		for(OutInMqVo outInMqVo:outInMqList){
			String outInMessage = JsonUtils.writeValue(outInMqVo);
			logger.info("process：订单：{}发送转入转出单MQ信息:{}",outInMqVo.getOrderCode(),outInMessage);
			RocketMQMessageUtil.saveMessage(producer, outInMessage, outInTopic, outInTag);
		}

		return iRep;

	}

	private Boolean checkExpiredAutoRedeem(Product product,HoldApart apart,ProductIncomeReward reward) {
		if(reward ==null){
			return true;
		}
		//到期自动赎回且赎回当日不计息的不派息
		if(product.getRedeemNeedInterest()==(byte)0 &&reward.getExpiredAutoRedeem()==(byte)1
				&& DateUtil.compare_current_(apart.getBeginRedeemDate())){
			return false;
		}
		return true;
	}


	private PromotionInterestVo interestLcjpRrocess(InvestorTradeOrder order,Date incomeDate,Product product,HoldApart holdApart,ProductIncomeReward reward,BigDecimal fpRate){
		PromotionCalculatingParam calculatingParam = new PromotionCalculatingParam();
		calculatingParam.setOrderId(order.getOrderCode());
		calculatingParam.setOrderTime(order.getOrderTime());
		calculatingParam.setLetvUserId(Long.valueOf(order.getInvestorOid()));
		calculatingParam.setCalculatingDate(incomeDate);
		calculatingParam.setProjectCode(product.getOid());
		calculatingParam.setBaseDays(Integer.parseInt(product.getIncomeCalcBasis()));
		//元转分
		BigDecimal investVolume = holdApart.getSnapshotVolume().multiply(new BigDecimal(100));
		calculatingParam.setInterestAmount(Long.valueOf(investVolume.intValue()));
		BigDecimal rewardRatio=BigDecimal.ZERO;
		if(reward!=null){
			rewardRatio = reward.getRatio();
		}
		BigDecimal currentRate = fpRate.add(rewardRatio);
		calculatingParam.setCurrentRate(currentRate.multiply(new BigDecimal(100)).doubleValue());
		calculatingParam.setStartInterestDate(holdApart.getBeginAccuralDate());
		logger.info("interestLcj:param:{}开始调用营销接口", JsonUtils.writeValue(calculatingParam));
		RpcResult<PromotionInterestVo> rcpRes = promotionServiceRpc.calculatePromotionInterest(calculatingParam);
		if(rcpRes==null||rcpRes.getResult()==0){
			logger.error("interestLcj,apartOid:{}调用营销接口失败",holdApart.getOid());
			throw new BizException("interestLcj,apartOid:{}调用营销接口失败");
		}
		logger.info("interestLcj:rcpRes:{}调用营销接口返回", JsonUtils.writeValue(rcpRes));
		return rcpRes.getData();
	}





}
