package com.le.jr.am.profit.test;

import javax.annotation.Resource;

import com.le.jr.am.profit.domain.input.HoldIncomeRequest;
import com.le.jr.am.profit.domain.output.HoldIncomeResponse;
import com.le.jr.trade.openapi.domain.current.input.QueryIncomeByDateInput;
import com.le.jr.trade.openapi.domain.current.output.HoldIncomeByDateOutput;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.jr.am.profit.dao.HoldApartIncomeMapper;
import com.le.jr.am.profit.dao.HoldIncomeMapper;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.HoldIncomeService;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.trade.publictools.util.JsonUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class HoldIncomeTest {
	
	@Resource
	private HoldIncomeService holdIncomeService;
	
	@Resource
	private HoldIncomeMapper holdIncomeMapper;
	
	
	@Test
	public void findByInvestorOidAndConfirmDate(){
		
		System.out.println(JsonUtils.writeValue(holdIncomeService.findByInvestorOidAndConfirmDate("165247427", "20160729")));
	}
	@Test
	public void findByInvestorOidAndConfirmDateInHis(){
		
		System.out.println(JsonUtils.writeValue(holdIncomeService.findByInvestorOidAndConfirmDateInHis("125884034", "20160729")));
	}
	@Test
	public void createAndBackupHoldIncome(){
		
		holdIncomeMapper.createHoldIncomeTable("20161121");
		holdIncomeMapper.backupHoldIncomeData("20161121","2016-11-21");
	}
	@Test
	public void queryBackupDate(){
		
		System.out.println(holdIncomeMapper.queryBackupDate());
	}




	@Test
	public void queryHoldIncomeByDate(){

		HoldIncomeRequest input = new HoldIncomeRequest();
		input.setIncomeDate("2017-02-26");
		input.setInvestorOid("229025037");
		HoldIncomeResponse result= this.holdIncomeService.queryHoldIncome(input);
		System.out.print("111111111111111");

	}


}
