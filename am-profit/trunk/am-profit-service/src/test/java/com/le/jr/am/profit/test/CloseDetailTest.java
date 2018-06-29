package com.le.jr.am.profit.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.jr.am.profit.service.HoldApartCloseDetailService;
import com.le.jr.am.profit.service.HoldLevelIncomeService;
import com.le.jr.trade.publictools.util.JsonUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class CloseDetailTest {
	
	@Resource
	private HoldApartCloseDetailService holdApartCloseDetailService;
	
	
	@Test
	public void queryBalanceGroupByLevelInHist(){
		
		System.out.println(JsonUtils.writeValue(holdApartCloseDetailService.selectByPrimaryKey("8af0d8385602b202015602b301ca0000")));
	}

}
