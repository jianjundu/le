package com.le.jr.am.profit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.le.jr.am.profit.domain.RewardIncomePractice;
import com.le.jr.am.profit.domain.output.PracticeInRep;
import com.le.jr.am.profit.domain.output.RewardIsNullRep;

public interface RewardIncomePracticeService {
	
	
	/**
	 * 杜建君2016年10月20日
	 * 定时任务，奖励收益试算,就是生成产品规模
	 */
	public void practiceDo();
	
	public void practice();
	
	public int insert(RewardIncomePractice entity);
	
	public int saveEntity(RewardIncomePractice entity) ;
	
	public RewardIsNullRep rewardIsNullRep(String product, String tDate) ;
	
	public List<RewardIsNullRep> rewardIsNullReps(String assetPoolOid, String tDate) ;
	
	public List<PracticeInRep> findByProduct(String productOid, String tDate);
	
	public List<PracticeInRep> findByProductLr(String productOid, String tDate);
	
	public void syncRewardData(Map<String, BigDecimal> rewardMap, String productOid,Date tDate);
	
	public int countByProductAndTDate(String productOid,String incomeDate);

	void warnAlloInterestDTS();


}
