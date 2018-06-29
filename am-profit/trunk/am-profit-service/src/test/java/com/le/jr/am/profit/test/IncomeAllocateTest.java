package com.le.jr.am.profit.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.dao.HoldMapper;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.input.SearchAllocateVO;
import com.le.jr.am.profit.service.IncomeDistributionService;
import com.le.jr.trade.publictools.data.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.jr.am.profit.service.IncomeAllocateService;
import com.le.jr.am.profit.service.InterestRateMethodService;
import com.le.jr.trade.publictools.util.JsonUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class IncomeAllocateTest {
	
	@Resource
	private IncomeAllocateService incomeAllocateService;

	@Resource
	private ProductInterfaces productInterfaces;
	
	@Resource
	InterestRateMethodService  interestRateMethodService;

	@Resource
	private HoldMapper holdMapper;


	@Resource
	private IncomeDistributionService incomeDistributionService;
	
	
	@Test
	public void getLastIncomeEventByAssetPoolId(){
		
		System.out.println(JsonUtils.writeValue(incomeAllocateService.getLatestIncomeDate("01111783")));
	}
	@Test
	public void interestDo(){
		
		
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-24");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		interestRateMethodService.interestDo("01138418", "e30d4d4c3d4343e9895124380dde3842", new BigDecimal("0.7100"), new BigDecimal("9000"),date);
	}


	@Test
	public void testgetIncomeAdjustList(){
		SearchAllocateVO vo = new SearchAllocateVO();
		vo.setAssetPoolOid("d4b86336066f44ca868a77e40c14628c");
		vo.setPageSize(10);
		vo.setCurrentPageNo(1);
		this.incomeDistributionService.getIncomeAdjustList(vo);
	}



	@Test
	public void testinterest()throws ParseException{
		String productOid= "01440980";
		String incomeAllocateOid = "d7796b602cc54ca898b664e6d1bc63a9";
		BigDecimal fpAmount = BigDecimal.ZERO;
		BigDecimal fpRate=BigDecimal.ZERO;
		Date incomeDate = DateUtil.getYesterday();


		this.interestRateMethodService.interestThread(productOid,incomeAllocateOid,fpAmount,fpRate,incomeDate);
	}

}
