package com.le.jr.am.profit.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.jr.am.profit.service.HoldLevelIncomeService;
import com.le.jr.am.profit.service.IncomeDistributionService;
import com.le.jr.am.profit.service.RewardIncomePracticeService;
import com.le.jr.trade.publictools.util.JsonUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class PracticeTest {
	
	@Resource
	private RewardIncomePracticeService rewardIncomePracticeService;
	
	@Resource
	private IncomeDistributionService incomeDistributionService;
	
	
	@Test
	public void  getIncomeAllocateAsset(){
		
		incomeDistributionService.getIncomeAllocateAsset("c4caa7a727ff45ea81d5f4b014be4ef2");
		
	/*	System.out.println(JsonUtils.writeValue(rewardIncomePracticeService.);*/
	}
	@Test
	public void  practice(){
		
		//rewardIncomePracticeService.practiceDo();
		
		/*	System.out.println(JsonUtils.writeValue(rewardIncomePracticeService.);*/
	}
	
	

}
