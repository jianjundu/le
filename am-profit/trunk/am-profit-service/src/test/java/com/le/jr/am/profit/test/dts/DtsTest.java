package com.le.jr.am.profit.test.dts;

import com.le.jr.am.profit.service.*;
import com.le.jr.platform.redis.RedisClient;
import com.le.jr.trade.publictools.util.JsonUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qishang on 2016/11/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config*.xml"})
@ActiveProfiles("test")
public class DtsTest {
    @Resource
    private HoldApartService holdApartService;
    @Resource
    private IncomeEventService incomeEventService;
    @Resource
    private RewardIncomePracticeService rewardIncomePracticeService;
    @Resource
    private PublisherProductAgreementService productAgreementService;
    @Resource
    private HoldService holdService;
    @Resource
    private HoldLevelIncomeService holdLevelIncomeService;
    @Resource
	private RedisClient redisClient;

 /*   *//**
     * 计息份额快照
     *//*
    @Test
    public void snapshot() {
        holdApartService.snapshot();
    }

    *//**
     * 自动派息通知
     *//*
    @Test
    public void notifyPlatformAllotResult() throws ParseException {
        incomeEventService.notifyPlatformAllotResult();
    }

    *//**
     * 自动派息
     *//*
    @Test
    public void interest() {
        incomeEventService.interest();
    }

    
    *//**
     * 产品计息规模试算
     *//*
    @Test
    public void practice() {
        rewardIncomePracticeService.practice();
    }

    *//**
     * 创建投资人html协议
     *//*
    @Test
    public void makeContract() {
        productAgreementService.makeContract();
    }

    *//**
     * 上传投资人pdf协议
     *//*
    @Test
    public void uploadPDF(){
        productAgreementService.uploadPDF();
    }
    *//**
     * 重置每日赎回份额ok
     *//*
    @Test
    public void resetDayRedeemVolume(){
        holdService.resetDayRedeemVolume();
    }
    *//**
     * 解锁计息份额ok
     *//*
    @Test
    public void unlockAccrual(){
        holdService.unlockAccrualDo();
    }

    *//**
     * 解锁赎回份额ok
     *//*
    @Test
    public void UnlockRedeem(){
        holdService.unlockRedeemDo();
    }
    @Test
    public void loadHoldApartData2Cache(){
        holdApartService.loadHoldApartData2Cache();
    }
    @Test
    public void loadHoldData2Cache(){
        holdService.loadHoldData2Cache();
    }
    @Test
    public void loadLevelIncomeData2Cache(){
        holdLevelIncomeService.loadLevelIncomeData2Cache();
    }*/


    @Test
    public  void testnotifyPlatformAllotResult(){
        this.incomeEventService.notifyPlatformAllotResult();
    }
    
    @Test
    public void testHset() {
    	
    	Map<String, String> redisMap = new HashMap<>();

		redisMap.put("agreementCode", "service");
		redisMap.put("agreementName", "ff80808159bbc7e30159df00ab186128");
		redisMap.put("agreementType", "service");
		redisMap.put("agreementUrl", "https://jr.letv.com/a1/M00/11/54/Cm5dyFiYg4qAJuGgAAIV1txuecE688.pdf");
		redisMap.put("orderOid", "ff80808159bbc7e30159df00ab186128");
		
    	for(int i=0;i<100;++i){
    		redisClient.hset("test:lecurrent:A:17012700428420961701"+i/2, i%2==0?"investing":"service", JsonUtils.writeValue(redisMap));
    	}
    }



    @Test
    public void testProfit(){
        incomeEventService.interest();
    }
    
}
