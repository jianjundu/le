package com.le.jr.am.profit.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.jr.am.profit.service.IncomeEventService;
import com.le.jr.platform.redis.RedisClient;
import com.le.jr.trade.publictools.util.JsonUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class IncomeEventTest {
	
	@Resource
	private IncomeEventService incomeEventService;
	
	@Resource
	private RedisClient redisClient;
	
	
	@Test
	public void getLastIncomeEventByAssetPoolId(){
		
		System.out.println(JsonUtils.writeValue(incomeEventService.getLastIncomeEventByAssetPoolId("908bc85291f540d38457a15a7a10047a","")));
	}
	@Test
	public void redisTest(){
		
		this.redisClient.zrem("lecurrent:HL:208062829:01137674", "1480003677000");
	}
	
	
	

}
