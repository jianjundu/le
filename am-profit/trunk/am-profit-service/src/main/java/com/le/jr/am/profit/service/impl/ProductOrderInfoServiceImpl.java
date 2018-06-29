package com.le.jr.am.profit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.order.common.util.JsonUtil;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.common.util.DecimalUtil;
import com.le.jr.am.profit.dao.HoldApartMapper;
import com.le.jr.am.profit.dao.LeZiXuanOrderInfoMapper;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.LeZiXuanOrderInfo;
import com.le.jr.am.profit.domain.input.LeZiXuanReqVo;
import com.le.jr.am.profit.domain.output.LeZiXuanRespVo;
import com.le.jr.am.profit.domain.vo.LeZiXuanPage;
import com.le.jr.am.profit.domain.vo.ProductTaskVo;
import com.le.jr.am.profit.service.CallDubboService;
import com.le.jr.am.profit.service.ProductOrderInfoService;

@Service("productOrderInfoService")
public class ProductOrderInfoServiceImpl implements ProductOrderInfoService {

	private static final Logger logger = LoggerFactory.getLogger(ProductOrderInfoServiceImpl.class);
	
	@Resource
    private HoldApartMapper holdApartMapper; 
	@Resource
	private LeZiXuanOrderInfoMapper leZiXuanOrderInfoMapper;
	@Resource
	private CallDubboService callDubboService;

	/**
	 * 编程式事务
	 */
	@Resource
	private PlatformTransactionManager transactionManager;
	
	@Override
	public LeZiXuanPage<LeZiXuanRespVo> getLeZiXuanInfos(PageEntity<LeZiXuanReqVo> pageEntity) {
		LeZiXuanPage<LeZiXuanRespVo> page = new LeZiXuanPage<LeZiXuanRespVo>();
		page.setCurrentPageNo(pageEntity.getPageNo());
		page.setPageSize(pageEntity.getSize());
		page.setTotalCount(0);
		List<LeZiXuanRespVo> pVos = new ArrayList<LeZiXuanRespVo>();
		page.setDataList(pVos);
		
		LeZiXuanReqVo reqVo=pageEntity.getParams();
		
		//查询总条数和总金额
		LeZiXuanPage<?> countAndSum = this.selectLeZiXuansCountAndSum(reqVo);
		if (countAndSum==null || countAndSum.getTotalCount()<= 0) {
			return page;
		}
        page.setTotalCount(countAndSum.getTotalCount());
        page.setExpireIncomeTotalVolume(countAndSum.getExpireIncomeTotalVolume());
        page.setExpireInvestTotalVolume(countAndSum.getExpireInvestTotalVolume());
        page.setExpireTotalVolume(countAndSum.getExpireTotalVolume());
        page.setInvestTotalVolume(countAndSum.getInvestTotalVolume());
        
		List<LeZiXuanOrderInfo> leZiXuans=this.selectLeZiXuans(pageEntity);
		
		Map<String,Product> productMap=new HashMap<String,Product>();
		Map<String,ProductIncomeReward> rewardMap=new HashMap<String,ProductIncomeReward>();
		
		for(LeZiXuanOrderInfo lzx:leZiXuans){
			Product product=productMap.get(lzx.getProductOid());
			if(product==null){
				product=this.callDubboService.callSelectProductByOid(lzx.getProductOid()).getData();
				productMap.put(lzx.getProductOid(), product);
			}
			ProductIncomeReward reward =rewardMap.get(lzx.getRewardOid());
			if(reward==null){
				reward=this.callDubboService.callSelectProductRewardByOid(lzx.getRewardOid()).getData();
				rewardMap.put(lzx.getRewardOid(),reward);
			}
			pVos.add(this.createLeZiXuanRespVo(lzx,product,reward));
		}
		return page;
	}
	
	@Override
	public void LeZiXuanDTS(ProductTaskVo productTaskVo) {
		List<HoldApart> holdAparts=holdApartMapper.selectByProductOidAndMinute(productTaskVo);
		
		if(holdAparts.isEmpty()){
			logger.info("product:{}没有新增分仓",productTaskVo.getProductOid());
			return;
		}
		
		Map<String,ProductIncomeReward> rewardMap=new HashMap<>();
		
		for(HoldApart holdApart : holdAparts){
			ProductIncomeReward reward=rewardMap.get(holdApart.getLockRewardOid());
			if(reward==null){
				reward=callDubboService.callSelectProductRewardByOid(holdApart.getLockRewardOid()).getData();
				rewardMap.put(holdApart.getLockRewardOid(), reward);
			}
			LeZiXuanOrderInfo orderInfo=leZiXuanOrderInfoMapper.selectByProductOidAndRewardOidAndInvestDate(holdApart.getProductOid(),holdApart.getLockRewardOid(),holdApart.getOrderTime());
			if(orderInfo==null){
				orderInfo=this.createLeZiXuanOrderInfo(holdApart, reward);
				leZiXuanOrderInfoMapper.insert(orderInfo);
			}else{
				this.updateLeZiXuanOrderInfo(orderInfo,holdApart,reward);
				leZiXuanOrderInfoMapper.updateByPrimaryKey(orderInfo);
			}
		}
		
	}
	
	@Override
	public void CompareLeZiXuanDTS(ProductTaskVo productTaskVo) {
		List<HoldApart> holdAparts=holdApartMapper.selectByProductOidAndDate(productTaskVo);
		
		if(holdAparts.isEmpty()){
			logger.info("product:{}没有分仓",productTaskVo.getProductOid());
			return;
		}
		//所有乐自选统计记录
		Map<String,LeZiXuanOrderInfo> leZiXuanOrderMap=new HashMap<>();
		
		Map<String,ProductIncomeReward> rewardMap=new HashMap<>();
		
		for(HoldApart holdApart : holdAparts){
			ProductIncomeReward reward=rewardMap.get(holdApart.getLockRewardOid());
			if(reward==null){
				reward=callDubboService.callSelectProductRewardByOid(holdApart.getLockRewardOid()).getData();
				rewardMap.put(holdApart.getLockRewardOid(), reward);
			}
			String key=holdApart.getLockRewardOid()+"_"+DateUtil.format(holdApart.getOrderTime());
			LeZiXuanOrderInfo lezixuan=leZiXuanOrderMap.get(key);
			
			if(lezixuan==null){
				lezixuan=this.createLeZiXuanOrderInfo(holdApart, reward);
				leZiXuanOrderMap.put(key, lezixuan);
			}else{
				this.updateLeZiXuanOrderInfo(lezixuan,holdApart,reward);
			}
		}
		
		//遍历所有map
		 for(Map.Entry<String, LeZiXuanOrderInfo> entry : leZiXuanOrderMap.entrySet()) {
			 LeZiXuanOrderInfo leZiXuanResult=entry.getValue();
			 LeZiXuanOrderInfo leZiXuanQuery=leZiXuanOrderInfoMapper.selectByProductOidAndRewardOidAndInvestDate(leZiXuanResult.getProductOid(),leZiXuanResult.getRewardOid(),leZiXuanResult.getInvestDate());
			 if(leZiXuanQuery==null){
				 logger.info("校验新增记录,{}",JsonUtil.objectToJson(leZiXuanResult));
				 leZiXuanOrderInfoMapper.insert(leZiXuanResult);
			 }else{
				 if(leZiXuanResult.getInvestVolume().compareTo(leZiXuanQuery.getInvestVolume())!=0){
					 logger.info("校验更新记录,old:{},new:{}",JsonUtil.objectToJson(leZiXuanQuery),JsonUtil.objectToJson(leZiXuanResult));
					 leZiXuanResult.setOid(leZiXuanQuery.getOid());
					 leZiXuanOrderInfoMapper.updateByPrimaryKey(leZiXuanResult);
				 }
			 }
		 }
	}
	
	private void updateLeZiXuanOrderInfo(LeZiXuanOrderInfo leZiXuanOrderInfo, HoldApart holdApart,ProductIncomeReward reward) {
		leZiXuanOrderInfo.setInvestVolume(leZiXuanOrderInfo.getInvestVolume().add(holdApart.getOrderVolume()));
		leZiXuanOrderInfo.setExpireInvestVolume(leZiXuanOrderInfo.getInvestVolume());
		//利息
		BigDecimal expireRewardAmount = DecimalUtil.setScaleDown(reward.getDratio().multiply(holdApart.getOrderVolume()).multiply(new BigDecimal(reward.getEndDate())));
		leZiXuanOrderInfo.setExpireIncomeVolume(leZiXuanOrderInfo.getExpireIncomeVolume().add(expireRewardAmount));
		leZiXuanOrderInfo.setExpireTotalVolume(leZiXuanOrderInfo.getExpireIncomeVolume().add(leZiXuanOrderInfo.getExpireInvestVolume()));
		leZiXuanOrderInfo.setUpdateTime(new Date());
	}

	private LeZiXuanOrderInfo createLeZiXuanOrderInfo(HoldApart holdApart,ProductIncomeReward reward){
		LeZiXuanOrderInfo leZiXuanOrderInfo=new LeZiXuanOrderInfo();
		leZiXuanOrderInfo.setOid(UUIDUtil.creatUUID());
		leZiXuanOrderInfo.setProductOid(holdApart.getProductOid());
		leZiXuanOrderInfo.setRewardOid(holdApart.getLockRewardOid());
		leZiXuanOrderInfo.setInvestDate(holdApart.getOrderTime());
		leZiXuanOrderInfo.setInvestVolume(holdApart.getOrderVolume());
		leZiXuanOrderInfo.setExpireDate(holdApart.getBeginRedeemDate());
		leZiXuanOrderInfo.setPeriod(reward.getEndDate());
		leZiXuanOrderInfo.setExpireInvestVolume(holdApart.getOrderVolume());
		//利息
		BigDecimal expireRewardAmount = DecimalUtil.setScaleDown(reward.getDratio().multiply(holdApart.getOrderVolume()).multiply(new BigDecimal(reward.getEndDate())));
		leZiXuanOrderInfo.setExpireIncomeVolume(expireRewardAmount);
		leZiXuanOrderInfo.setExpireTotalVolume(holdApart.getOrderVolume().add(expireRewardAmount));
		leZiXuanOrderInfo.setUpdateTime(new Date());
		leZiXuanOrderInfo.setCreateTime(new Date());
		return leZiXuanOrderInfo;
	}
	
	private LeZiXuanRespVo createLeZiXuanRespVo(LeZiXuanOrderInfo lzx, Product product, ProductIncomeReward reward) {
		LeZiXuanRespVo respVo=new LeZiXuanRespVo();
		respVo.setProductOid(product.getOid());
		respVo.setProductName(product.getName());
		respVo.setRewardName(reward.getLevel());
		respVo.setRate(reward.getRatio());
		respVo.setInvestVolume(lzx.getInvestVolume());
		respVo.setPeriod(lzx.getPeriod());
		respVo.setInvestDate(lzx.getInvestDate());
		respVo.setExpireDate(lzx.getExpireDate());
		respVo.setExpireInvestVolume(lzx.getExpireInvestVolume());
		respVo.setExpireIncomeVolume(lzx.getExpireIncomeVolume());
		respVo.setExpireTotalVolume(lzx.getExpireTotalVolume());
		return respVo;
	}

	private LeZiXuanPage<?> selectLeZiXuansCountAndSum(LeZiXuanReqVo reqVo) {
		return leZiXuanOrderInfoMapper.selectLeZiXuansCountAndSum(reqVo);
	}
	
	private List<LeZiXuanOrderInfo> selectLeZiXuans(PageEntity<LeZiXuanReqVo> pageEntity) {
		return leZiXuanOrderInfoMapper.selectLeZiXuans(pageEntity);
	}



	
}
