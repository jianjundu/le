package com.le.jr.am.profit.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.jr.am.profit.domain.input.AgreementRequest;
import com.le.jr.am.profit.domain.input.HoldRequest;
import com.le.jr.am.profit.domain.input.LevelHoldRequest;
import com.le.jr.am.profit.service.HoldLevelIncomeService;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.am.profit.service.PublisherProductAgreementService;
import com.le.jr.trade.publictools.util.JsonUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class HoldTest {
	
	@Resource
	private HoldService holdService;
	
	@Resource
	private HoldLevelIncomeService holdLevelIncomeService;
	
	@Resource
	private PublisherProductAgreementService publisherProductAgreementService;
	
	
	@Test
	public void findByOid(){
		
		System.out.println(JsonUtils.writeValue(holdService.findByOid("4028ee81559b94d201559bbc26600037")));
	}
	
	
	
	@Test
	public void queryUserBalance(){
		
		HoldRequest request = new HoldRequest();
		request.setInvestorOid("120854327");
		request.setProductOid("01138937");
		
		System.out.println(JsonUtils.writeValue(holdService.queryUserBalance(request)));
	}
	@Test
	public void queryBalanceGroupByLevel(){
		
		LevelHoldRequest request = new LevelHoldRequest();
		request.setInvestorOid("152039799812");
		request.setProductOid("01136338");
		
		System.out.println(JsonUtils.writeValue(holdLevelIncomeService.queryBalanceGroupByLevel(request)));
	}
	@Test
	public void findByInvestorOidAndProduct(){
		
		System.out.println(JsonUtils.writeValue(holdService.findByInvestorOidAndProduct("198782449","01137841")));
		System.out.println();
	}
	@Test
	public void isInvestorExists(){
		
		System.out.println(JsonUtils.writeValue(holdService.isInvestorExists("4028ee81559b94d201559bbc26600037","sss")));
	}
	@Test
	public void getAssetPoolSpvHold(){
		
		System.out.println(JsonUtils.writeValue(holdService.getAssetPoolSpvHold("b3d2e826440b41d98118c51a8e53c5e5","56e519c8fa1b4df494a8552971baac97","01137841")));
	}


	@Test
	public void volHoldconfirmDetail(){
		
		System.out.println(JsonUtils.writeValue(holdService.volHoldconfirmDetail("6fab642e68ab4d1e8d50bdc4f98cf980")));
	}
	@Test
	public void getAgreementInfo(){
		
		AgreementRequest r = new AgreementRequest();
		r.setAgreementType("service");
		r.setTradeOrderOid("937201701170009");
		
		System.out.println(JsonUtils.writeValue(publisherProductAgreementService.getAgreementInfo(r)));
	}


	@Test
	public void testCompareHoldDataWithCache() throws Exception{
		this.holdService.compareHoldDataWithCache();
	}

}
