package com.le.jr.am.profit.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.profit.domain.LevelIncome;
import com.le.jr.am.profit.domain.input.LevelHoldRequest;
import com.le.jr.am.profit.service.HoldLevelIncomeService;
import com.le.jr.trade.publictools.util.JsonUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class LevelIncomeTest {
	
	@Resource
	private HoldLevelIncomeService holdLevelIncomeService;
	
	
/*	@Test
	public void queryBalanceGroupByLevelInHist(){
		
		System.out.println(JsonUtils.writeValue(holdLevelIncomeService.queryBalanceGroupByLevelInHist("125884034", "01111321", "20160713")));
	}
	@Test
	public void loadLevelIncomeData2Cache(){
		
		System.out.println(JsonUtils.writeValue(holdLevelIncomeService.loadLevelIncomeData2Cache()));
	}
	@Test
	public void saveBatch(){
		
		List<LevelIncome> list = new ArrayList<LevelIncome>();
		LevelIncome e = new LevelIncome();
		e.setOid(UUIDUtil.creatUUID());
		e.setAccureVolume(new BigDecimal("0"));
		e.setConfirmDate(new Date());
		list.add(e);
		
		holdLevelIncomeService.saveBatch(list);
	}*/
	@Test
	public void queryBalanceGroupByLevel(){
		LevelHoldRequest r = new LevelHoldRequest();
		r.setInvestorOid("106373572");
		r.setProductOid("01138950");
		r.setConfirmDate("2017-1-17");
		System.out.println(JsonUtils.writeValue(holdLevelIncomeService.queryBalanceGroupByLevel(r)));
	}

}
