package com.le.jr.am.profit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.product.common.util.ValidateUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.product.domain.vo.SearchProductRewardVo;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.common.util.DecimalUtil;
import com.le.jr.am.profit.dao.RewardIncomePracticeMapper;
import com.le.jr.am.profit.domain.RewardIncomePractice;
import com.le.jr.am.profit.domain.enums.PracticeIsAllocateInterestEnum;
import com.le.jr.am.profit.domain.output.PracticeInRep;
import com.le.jr.am.profit.domain.output.RewardIsNullRep;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.CallDubboService;
import com.le.jr.am.profit.service.RewardIncomePracticeService;
import com.le.jr.am.profit.service.config.ProfitDiamondService;
import com.le.jr.am.task.interfaces.JobLockInterface;
import com.le.jr.am.task.interfaces.SerialTaskInterface;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.le.jr.trade.touchcenter.domain.dto.touch.TouchExtendParameter;
import com.le.jr.trade.touchcenter.domain.dto.touch.TouchParameter;
import com.le.jr.trade.touchcenter.domain.enums.TouchModeEnum;

@Service("rewardIncomePracticeService")
public class RewardIncomePracticeServiceImpl implements RewardIncomePracticeService {

	Logger logger = LoggerFactory.getLogger(RewardIncomePracticeServiceImpl.class);

	@Resource
	private RewardIncomePracticeMapper rewardIncomePracticeMapper;

	@Resource
	private ProductInterfaces productInterfaces;
	
	@Resource
	private PlatformTransactionManager platformTransactionManager;
	
	@Resource
	private CallDubboService callDubboService;
	
	@Resource
	private ProfitDiamondService profitDiamondService;
	
	
	@Override
	public void practice() {
		logger.info("practice 空");
	}

	/**
	 * 杜建君2016年10月20日 定时任务，奖励收益试算,就是生成产品规模
	 */
	@Override
	public void practiceDo() {
		logger.info("practiceDo 空");
	}

	
	
	@Override
	public int saveEntity(RewardIncomePractice entity) {

		entity.setOid(UUIDUtil.creatUUID());
		return rewardIncomePracticeMapper.insert(entity);
	}

	@Override
	public RewardIsNullRep rewardIsNullRep(String product, String tDate) {

		RewardIsNullRep rep = new RewardIsNullRep();

		RewardIncomePractice entity = null;
		if (null != tDate) {
			entity = this.rewardIncomePracticeMapper.findRewardIsNull(product, tDate);
		} else {
			Date maxDate = this.rewardIncomePracticeMapper.findMaxTDate(product);
			entity = this.rewardIncomePracticeMapper.findRewardIsNull(product, DateUtil.format(maxDate));
		}
		if (null != entity) {
			rep.setProductOid(product);
			rep.setTotalHoldVolume(entity.getTotalHoldVolume());
			rep.setTotalRewardIncome(entity.getTotalRewardIncome());
		}
		return rep;

	}

	@Override
	public List<RewardIsNullRep> rewardIsNullReps(String assetPoolOid, String tDate) {

		logger.info("poolOid={}, tDate={}", assetPoolOid, tDate);
		List<RewardIsNullRep> list = new ArrayList<RewardIsNullRep>();

		List<RewardIncomePractice> entities = null;
		if (null != tDate) {
			entities = this.rewardIncomePracticeMapper.findRewardIsNulls(assetPoolOid, tDate);
		} else {
			Date maxDate = this.rewardIncomePracticeMapper.findMaxTDate(assetPoolOid);
			entities = this.rewardIncomePracticeMapper.findRewardIsNulls(assetPoolOid, DateUtil.format(maxDate));
		}
		for (RewardIncomePractice tmp : entities) {
			RewardIsNullRep rep = new RewardIsNullRep();
			rep.setProductOid(tmp.getProductOid());
			rep.setTotalHoldVolume(tmp.getTotalHoldVolume());
			rep.setTotalRewardIncome(tmp.getTotalRewardIncome());
			list.add(rep);
		}
		return list;

	}

	/**
	 * 查询普通阶梯数据分布
	 */
	@Override
	public List<PracticeInRep> findByProduct(String productOid, String tDate) {

		return this.findByProduct(productOid, tDate, ProductIncomeReward.REWARD_ISLOCKREWARD_NO);
	}

	/**
	 * 查询锁定阶梯数据分布
	 */
	public List<PracticeInRep> findByProductLr(String productOid, String tDate) {
		return this.findByProduct(productOid, tDate, ProductIncomeReward.REWARD_ISLOCKREWARD_YES);
	}

	public List<PracticeInRep> findByProduct(String productOid, String tDate, String isLockReward) {

		Product product = productInterfaces.selectProductByOid(productOid).getData();

		Map<String, ProductIncomeReward> rewardMap = new HashMap<String, ProductIncomeReward>();
		
		
		SearchProductRewardVo searchVo = new SearchProductRewardVo();
		searchVo.setIsLockReward(isLockReward);
		searchVo.setProductOid(productOid);
		
		//如果产品接口发生改动，需要修改此处  TODO
		Message<Page<ProductIncomeReward>> pageM = productInterfaces.selectProductRewardByVo(searchVo );
		
		if (ValidateUtil.interfaceValidate(pageM)) {

			List<ProductIncomeReward> list = pageM.getData().getDataList();
			for (ProductIncomeReward r : list) {
				rewardMap.put(r.getOid(), r);
			}
		} else {
			logger.error("findByProduct查询奖励收益异常");
			throw new BizException("查询奖励收益异常");
		}

		List<RewardIncomePractice> list = null;
		if (null != tDate) {
			list = this.rewardIncomePracticeMapper.findByProductAndTDate(productOid, tDate);
		} else {
			Date maxDate = this.rewardIncomePracticeMapper.findMaxTDateByProduct(productOid);
			list = this.rewardIncomePracticeMapper.findByProductAndTDate(productOid, DateUtil.format(maxDate));
		}

		List<PracticeInRep> rowsRep = new ArrayList<PracticeInRep>();
		if (null != list && !list.isEmpty()) {
			for (RewardIncomePractice entity : list) {

				if (null != entity.getRewardRuleOid()) {

					if (isLockReward.equals(ProductIncomeReward.REWARD_ISLOCKREWARD_YES)) {
						if(null== rewardMap.get(entity.getRewardRuleOid()) ){
							continue;
						}
						
						if (!rewardMap.get(entity.getRewardRuleOid()).getIsLockReward().equals(ProductIncomeReward.REWARD_ISLOCKREWARD_YES)) {

							continue;
						}
					}
					if (ProductIncomeReward.REWARD_ISLOCKREWARD_NO.equals(isLockReward)) {
						if(null== rewardMap.get(entity.getRewardRuleOid()) ){
							continue;
						}
						if (!rewardMap.get(entity.getRewardRuleOid()).getIsLockReward().equals(ProductIncomeReward.REWARD_ISLOCKREWARD_NO)) {
							continue;
						}
					}

				}else{
					continue;
				}

				PracticeInRep rep = new PracticeInRep();

				rep.setTotalHoldVolume(entity.getTotalHoldVolume());
				rep.setInterestedVolume(entity.getInterestedVolume());
				rep.setToConfirmVolume(entity.getToConfirmVolume());
				rep.setToInterestVolume(entity.getToInterestVolume());
				
				
				
				Long value = Long.valueOf(DecimalUtil.changeRMB2FEN(entity.getTotalHoldVolume().multiply(product.getNetUnitShare())));
				rep.setValue(value);
				rep.setTotalRewardIncome(entity.getTotalRewardIncome());
				rep.settDate(entity.gettDate());
				rep.setCreateTime(entity.getCreateTime());
				rep.setUpdateTime(entity.getUpdateTime());
				rep.setProductOid(productOid);
				if (null != entity.getRewardRuleOid()) {

					ProductIncomeReward reward = rewardMap.get(entity.getRewardRuleOid());
					if(reward !=null){
						rep.setRewardRatio(reward.getRatio());
						rep.setStartDate(reward.getStartDate());
						rep.setEndDate(reward.getEndDate());
						rep.setRewardOid(entity.getRewardRuleOid());
						rep.setLevel(reward.getLevel());
						rep.setTotalIncome(reward.getTotalIncome());
						if (PracticeIsAllocateInterestEnum.YES.value.equals(entity.getIsAllocateInterest())) {
							rep.setYesterdayIncome(reward.getYesterdayIncome());
						}
					}

					
				}

				rowsRep.add(rep);
			}
		}

		logger.info("findByProduct:{}", JsonUtils.writeValue(rowsRep));

		return rowsRep;
	}

	@Override
	public void syncRewardData(Map<String, BigDecimal> rewardMap,String productOid, Date tDate) {
		
		Iterator<String> it = rewardMap.keySet().iterator();
		while (it.hasNext()) {
			String rewardOid = it.next();
			BigDecimal totalIncome=rewardMap.get(rewardOid);
			String dateFormat=DateUtil.format(tDate);
			try {
				// 调用产品接口
				this.productInterfaces.interestProductIncomeReward(rewardOid,totalIncome,totalIncome);
				
				//更新试算表
				int flag= this.rewardIncomePracticeMapper.updateIsAllocateInterestByRewardAndTDate(rewardOid, dateFormat);
				if(flag<=0){
					logger.error("更新阶梯试算记录失败,productOid:{},rewardOid:{} date:{}",productOid,rewardOid, dateFormat);
				}
			} catch (Exception e) {
				logger.error("同步奖励阶梯收益失败,productOid:"+productOid+",rewardOid:"+rewardOid+",date:"+dateFormat,e);
			}
		}
	}

	@Override
	public int countByProductAndTDate(String productOid, String incomeDate) {
		return this.rewardIncomePracticeMapper.countByProductAndTDate(productOid, incomeDate);
	}

	@Override
	public int insert(RewardIncomePractice entity) {
		return this.rewardIncomePracticeMapper.insert(entity);
	}

	@Override
	public void warnAlloInterestDTS() {
		Date tDate=DateUtil.getBeforeDate();
		List<String> productOids=rewardIncomePracticeMapper.getNotAlloInterestProducts(DateUtil.format(tDate));
		if(productOids.isEmpty()){
			logger.info("没有待提醒派息产品");
			return;
		}
		
		String serviceId=profitDiamondService.getMessage("warnInterestMsgServiceId").toString();
		String mobileString=profitDiamondService.getMessage("warnInterestMsgMobile").toString();
		String[] mobiles=mobileString.split(",");
		
		for(String productOid:productOids){
			Product product = this.callDubboService.callSelectProductByOid(productOid).getData();
			Map<String,String> contentMap=new HashMap<String,String>();
			contentMap.put("productOid", productOid);
			contentMap.put("productName", product.getName());
			
			for(String mobile :mobiles){
				try{
					callDubboService.callTouch(this.createTouchParameter(serviceId,12345678L, mobile, contentMap));
				}catch(Exception e){
					logger.info("通知派息短信发送失败,productOid:"+productOid+",mobile:"+mobile,e);
				}
			}
		}
	}
	
	private TouchParameter createTouchParameter(String serviceId,Long letvUserId,String mobile,Map<String, String> contentMap){
		TouchParameter touchParameter = new TouchParameter();
		touchParameter.setServiceId(serviceId);
		touchParameter.setLetvUserId(letvUserId);
		touchParameter.setContentMap(contentMap);
		
		TouchExtendParameter  touchExtendParameter = new TouchExtendParameter();
		touchExtendParameter.setMsisdn(mobile);
		touchExtendParameter.setPriority(9);
		touchExtendParameter.setTouchMode(TouchModeEnum.NORMAL.value);
		touchParameter.setExtendParameter(touchExtendParameter);
		
		return touchParameter;
	}

}
