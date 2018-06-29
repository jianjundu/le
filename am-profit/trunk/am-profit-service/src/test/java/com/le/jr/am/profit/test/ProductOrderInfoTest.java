package com.le.jr.am.profit.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.domain.input.LeZiXuanReqVo;
import com.le.jr.am.profit.domain.output.LeZiXuanRespVo;
import com.le.jr.am.profit.domain.vo.LeZiXuanPage;
import com.le.jr.am.profit.service.ProductOrderInfoService;
import com.le.jr.am.profit.service.RewardIncomePracticeService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class ProductOrderInfoTest {
	
	@Resource
	private ProductOrderInfoService productOrderInfoService;
	
	@Resource
	private RewardIncomePracticeService rewardIncomePracticeService;
	
/*	@Test
	public void LeZiXuanDTS(){
		try{
			ProductTaskVo productTaskVo=new ProductTaskVo();
			productTaskVo.setProductOid("01341038");
			productTaskVo.setDate(DateUtil.parse("2017-04-12 11:25:22",DateUtil.datetimePattern));
			productOrderInfoService.LeZiXuanDTS(productTaskVo);
		}catch(Exception e){
			System.out.println("hha");
		}
	}*/
	
	/*@Test
	public void getLeZiXuanInfos(){
		PageEntity<LeZiXuanReqVo> pageEntity=new PageEntity<LeZiXuanReqVo>();
		pageEntity.setSize(10);
		pageEntity.setPageNo(1);
		LeZiXuanReqVo lzxVo=new LeZiXuanReqVo();
		pageEntity.setParams(lzxVo);
		LeZiXuanPage<LeZiXuanRespVo>  result=productOrderInfoService.getLeZiXuanInfos(pageEntity);
		System.out.println("haha");
	}*/
	
	@Test
	public void warnAlloInterestDTS(){
		rewardIncomePracticeService.warnAlloInterestDTS();
	}
}
