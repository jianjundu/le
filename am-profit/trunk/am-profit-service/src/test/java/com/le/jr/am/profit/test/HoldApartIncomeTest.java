package com.le.jr.am.profit.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.jr.am.profit.dao.HoldApartIncomeMapper;
import com.le.jr.am.profit.dao.HoldApartMapper;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.service.HoldApartIncomeService;
import com.le.jr.trade.publictools.util.JsonUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class HoldApartIncomeTest {
	
	@Resource
	private HoldApartIncomeService holdApartIncomeService;
	
	@Resource
	private HoldApartIncomeMapper holdApartIncomeMapper;
	
	
	@Test
	public void calcSumInterest(){
	
		System.out.println(JsonUtils.writeValue(holdApartIncomeService.calcSumInterest("01111700", "20160729")));
	}
	@Test
	public void getByPoidAndConfirmDate(){
		
		System.out.println(JsonUtils.writeValue(holdApartIncomeService.getByPoidAndConfirmDate("01111700", "20160729")));
	}
	@Test
	public void findApartIncomeByRewardOid(){
		
		System.out.println(JsonUtils.writeValue(holdApartIncomeMapper.findApartIncomeByRewardOid("165024144", "01111792","0008dbabfba84275b3972910ddc48a89","20160815")));
	}
	@Test
	public void queryApartIncome(){
		
		System.out.println(JsonUtils.writeValue(holdApartIncomeService.queryApartIncome("165024144", "8af0d838566d118901566e6ef33b000d","20160815")));
	}
	@Test
	public void queryApartIncomeInHis(){
		
		System.out.println(JsonUtils.writeValue(holdApartIncomeService.queryApartIncomeInHis("180853113", "8af0d8385658c5a5015658dc7db30024","20160815")));
	}

}
