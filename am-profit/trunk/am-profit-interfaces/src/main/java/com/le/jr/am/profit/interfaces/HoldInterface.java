package com.le.jr.am.profit.interfaces;

import java.math.BigDecimal;

import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.input.HoldIncomeRequest;
import com.le.jr.am.profit.domain.input.HoldRequest;
import com.le.jr.am.profit.domain.input.LevelHoldRequest;
import com.le.jr.am.profit.domain.input.SearchHoldVo;
import com.le.jr.am.profit.domain.output.*;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.page.Page;

public interface HoldInterface {
	
	
	Message<Hold> getAssetPoolSpvHold(String assetPool, String spvOid,String productOid) ;
	
	Message<Boolean> saveAndFlush(Hold hold);
	
	Message<Boolean> updateEntity(Hold hold);
	
	/**
	 * 根据投资人和产品Id查总仓持仓
	 * @param investorOid
	 * @param productOid
	 * @return
	 */
	Message<Hold> findByInvestorOidAndProduct(String investorOid,String productOid);
	
	/**
	 * 判断投资者是否存在
	 * @param investorOid
	 * @param productOid
	 * @return
	 */
	Message<Boolean> isInvestorExists(String investorOid,String productOid);
	
	Message<Hold> findByOid(String holdOid);
	
	
	/**
	 * spv赎回订单审核确定调整totalHoldVolume
	 * @param oid
	 * @param orderAmount
	 * @return
	 */
	Message<Boolean> spvOrderRedeemConfirm(String oid,BigDecimal orderAmount);
	
	/**
	 * spv申购订单审核确定调整totalHoldVolume
	 * @param oid
	 * @param orderAmount
	 * @return
	 */
	Message<Boolean> spvOrderInvestConfirm(String oid,BigDecimal orderAmount);
	
	/**
	 * 根据条件查询持有人手册
	 */
	
	Message<Page<Hold>> selectHolds4Api(SearchHoldVo vo);
	
	/**
	 * 投资单
	 * @param order
	 * @return
	 */
	Message<InvestResultVo>  invest(InvestorTradeOrder order);
	
	/**
	 * 赎回和补单赎回
	 * @param order
	 * @param orderSubmitType
	 * @return
	 */
	Message<Boolean>  redeem4Order(InvestorTradeOrder order,String submitType);
	
	
	
	/**
	 * 申购废单
	 * @param tradeOrder
	 * @return
	 */
	Message<Boolean> abandonInvestOrder(InvestorTradeOrder tradeOrder) ;
	/**
	 * 份额确认持有人份额更新
	 * @param list
	 * @return
	 */
	Message<Boolean>  volHoldconfirmDetail(String offsetOid);
	
	/**
	 * 根据阶梯查询阶梯收益信息
	 * @param request
	 * @return
	 */
	Message<LevelHoldResponse> queryBalanceGroupByLevel(LevelHoldRequest request) ;
	
	/**
	 * 获取spv持仓详细信息
	 * @param assetPoolOid
	 * @param productOid
	 * @return
	 */
	Message<HoldDetailRep> getSPVHoldDetail(String assetPoolOid,String productOid);
	
	
	/**
	 * 查询用户持仓
	 */
	
	Message<HoldResponse> queryUserBalance(HoldRequest request) ;
	
	/**
	 * 查询总仓收益信息
	 * @param request
	 * @return
	 */
	Message<HoldIncomeResponse> queryHoldIncome(HoldIncomeRequest request);
	
	/**
	 * 刷新持仓缓存
	 * @return
	 */
	Message<Boolean> loadHoldAndApartRedis();
	
	
	

}
