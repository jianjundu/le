package com.le.jr.am.profit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.le.jr.am.accountant.interfaces.liabilities.AccountingNotifyEntityInterface;
import com.le.jr.am.order.domain.enums.TaskCodeEnum;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.InvestorInterestResult;
import com.le.jr.am.profit.domain.enums.HoldStatusEnum;
import com.le.jr.am.profit.domain.enums.InterestResultStausEnum;
import com.le.jr.am.profit.domain.output.InterestRep;
import com.le.jr.am.profit.service.CallDubboService;
import com.le.jr.am.profit.service.HoldApartIncomeService;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.HoldIncomeService;
import com.le.jr.am.profit.service.HoldLevelIncomeService;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.am.profit.service.IncomeAllocateService;
import com.le.jr.am.profit.service.IncomeDistributionService;
import com.le.jr.am.profit.service.InterestRateMethodService;
import com.le.jr.am.profit.service.InvestorInterestResultService;
import com.le.jr.am.profit.service.RewardIncomePracticeService;
import com.le.jr.am.profit.service.util.InterestRateMethodProcess;
import com.le.jr.am.task.domain.SerialTaskReq;
import com.le.jr.am.task.domain.param.InterestParams;
import com.le.jr.am.task.interfaces.SerialTaskInterface;


@Service("interestRateMethodService")
public class InterestRateMethodServiceImpl implements InterestRateMethodService{
	
	
	Logger logger = LoggerFactory.getLogger(InterestRateMethodServiceImpl.class);
	
	
	@Resource
	HoldService holdService;
	@Resource
	HoldIncomeService holdIncomeService;
	@Resource
	RewardIncomePracticeService rewardIncomePracticeService;
	@Resource
	HoldLevelIncomeService holdLevelIncomeService;
	@Resource
	ProductInterfaces productInterfaces;
	
	@Resource
	AccountingNotifyEntityInterface accountingNotifyEntityInterface;
	
	@Resource
	InvestorInterestResultService investorInterestResultService;
	@Resource
	IncomeDistributionService incomeDistributionService;
	@Resource
	IncomeAllocateService incomeAllocateService;
	@Resource
	HoldApartService holdApartService;
	
	@Resource
	HoldApartIncomeService holdApartIncomeService;
	
	@Resource
	InterestRateMethodProcess interestRateMethodProcess;
	
	@Resource
	CallDubboService callDubboService;
	
	
	
	@Value("${lecurrent.batch.size:1000}")
	private int pageSize = 1000;
	

	@Resource
	private SerialTaskInterface serialTaskInterface;
	

	@Override
	public void interest( String productOid, String incomeAllocateOid,  BigDecimal fpAmount,  BigDecimal fpRate,  Date incomeDate) {
		InterestParams params = new InterestParams();
		params.setProductOid(productOid);
		params.setIncomeAllocateOid(incomeAllocateOid);
		params.setFpAmount(fpAmount);
		params.setFpRate(fpRate);
		params.setIncomeDate(incomeDate);
		SerialTaskReq<InterestParams> req = new SerialTaskReq<InterestParams>();
		req.setTaskCode(TaskCodeEnum.INTEREST.value);
		req.setTaskParams(params);
		this.serialTaskInterface.createSerialTask(req)  ;      
	}
	
	public void interestDo( String productOid, String incomeAllocateOid, 
			 BigDecimal fpAmount,  BigDecimal fpRate,  Date incomeDate) {
	
		this.interestThread(productOid, incomeAllocateOid, fpAmount, fpRate, incomeDate);
		
	}
	
	
	public void interestThread(String productOid, String incomeAllocateOid,
			BigDecimal fpAmount, BigDecimal fpRate, Date incomeDate) {
		
		logger.info("interest_productOid:{},incomeDate:{}",productOid,incomeDate);
		
		Product product = productInterfaces.selectProductByOid(productOid).getData();
		
		//待计息份额 = 本金份额 + 最后计息基数
		BigDecimal totalVolume = holdApartService.getCountByProduct4Practice(productOid, DateUtil.format(incomeDate));


		/**
		 * 应该在result中增加这个字段，营销收益，但是还需要该表，目前已经上线，带下次优化
		 */
		BigDecimal holdInterestMarketingAmount = BigDecimal.ZERO;
		
		//收益分配日志
		InvestorInterestResult  result = investorInterestResultService.createEntity(productOid, incomeAllocateOid, incomeDate,totalVolume);

		if (null == totalVolume || BigDecimal.ZERO.compareTo(totalVolume) == 0) {
			logger.info("{}===待计算从份额为零", product.getCode());
			result.setLeftAllocateIncome(fpAmount);
			result.setStatus(InterestResultStausEnum.ALLOCATED.value);
			result.setAnno("待计息份额为零");
			this.investorInterestResultService.updateEntity(result);
			this.investorInterestResultService.send(result,holdInterestMarketingAmount);
			return;
		}
		//单位净值
		BigDecimal netUnitAmount = product.getNetUnitShare();
		
		String lastOid = "0";

		Map<String, BigDecimal> rewardMap = new HashMap<String, BigDecimal>();
		
		while(true) {
			
			List<Hold> holdList = holdService.findByProductHoldStatus(product.getOid(), HoldStatusEnum.HOLDING.value, lastOid, DateUtil.format(incomeDate));
			if (holdList.isEmpty()) {
				break;
			}
			for (Hold hold : holdList) {
				//记录最后一个oid
				lastOid = hold.getOid();
				try {
					InterestRep iRep = this.processOneItem(product, incomeDate, hold, netUnitAmount, fpRate);
					if (iRep.isResult()) {
						result.setSuccessAllocateIncome(result.getSuccessAllocateIncome().add(iRep.getHoldInterestBaseAmount()));
						result.setSuccessAllocateInvestors(result.getSuccessAllocateInvestors() + 1);
						result.setSuccessAllocateRewardIncome(result.getSuccessAllocateRewardIncome().add(iRep.getHoldInterestRewardAmount()));
						result.setProvisionInterestVolume(result.getProvisionInterestVolume().add(iRep.getProvisionInterestAmount()));
						result.setCloseInterestVolume(result.getCloseInterestVolume().add(iRep.getCloseInterestAmount()));
						holdInterestMarketingAmount = iRep.getHoldInterestMarketingAmount();
						/** 统计每个阶梯收益 */
						Iterator<String> it = iRep.getRewardMap().keySet().iterator();
						while (it.hasNext()) {
							String rewardOid = it.next();
							BigDecimal incomeAmount = iRep.getRewardMap().get(rewardOid);
							
							if (null == rewardMap.get(rewardOid)) {
								rewardMap.put(rewardOid, incomeAmount);
							} else {
								rewardMap.put(rewardOid, rewardMap.get(rewardOid).add(incomeAmount));
							}
						}
					} else {
						result.setFailAllocateInvestors(result.getFailAllocateInvestors() + 1);
					}
				} catch(Exception e) {
					logger.error(e.getMessage(), e);
					logger.warn("interest error: {}", e.getMessage());
				}
				
			}
		}
		//剩余可分配收益
		result.setLeftAllocateIncome(fpAmount.subtract(result.getSuccessAllocateIncome()));
		if (0 == result.getFailAllocateInvestors().intValue()) {
			result.setStatus(InterestResultStausEnum.ALLOCATED.value);
		} else {
			result.setStatus(InterestResultStausEnum.ALLOCATEFAIL.value);
		}
		
		
		//通知收益试算
		rewardIncomePracticeService.syncRewardData(rewardMap,productOid,incomeDate);
		
		this.investorInterestResultService.updateEntity(result);
		this.investorInterestResultService.send(result,holdInterestMarketingAmount);
	}
	
	
	
	/**
	 * 
	 * @param product
	 * @param incomeDate
	 * @param hold
	 * @param netUnitAmount
	 * @param fpRate 年化利率 0.05 已经除以100
	 * @return
	 */
	public InterestRep processOneItem(Product product, Date incomeDate,
			Hold hold, BigDecimal netUnitAmount,
			BigDecimal fpRate) {
		
		InterestRep iRep = new InterestRep();
		try {
			 iRep = interestRateMethodProcess.process(product, incomeDate, hold, netUnitAmount, fpRate);
		} catch (Exception e) {
			logger.error("processOneItem " );
			iRep.setResult(false);
		}
		
		return iRep;
	}

	

	

	
}
