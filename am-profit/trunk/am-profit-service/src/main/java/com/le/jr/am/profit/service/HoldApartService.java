package com.le.jr.am.profit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.guohuai.lecurrent.orderservice.OrderInfoRequest;
import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.HoldApartTypeChangeLog;
import com.le.jr.am.profit.domain.input.SearchHoldApartVo;
import com.le.jr.am.profit.domain.output.HoldDetailResponse;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;

/**
 * 持有人分仓
 * xxx
 *
 * @author lining6
 * @date 2016年11月2日 下午4:23:09
 *
 */
public interface HoldApartService {
	
	/**
	 * 根据查询条件查询所有分仓信息 分页接口
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	Page<HoldApart> selectHoldApart4ApiByVo(SearchHoldApartVo vo )throws BizException ;
	
	/**
	 * 根据查询条件查询所有分仓信息
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	List<HoldApart> selectHoldApartByVo(SearchHoldApartVo vo)throws BizException ;
	
	/**
	 * 根据订单查询分仓记录
	 * @param orderOid
	 * @return
	 */
	HoldApart findHoldApartByOrderId(String orderOid)throws BizException ;


	/**
	 * 根据平台订单code查询分仓记录
	 * @param orderCode 平台订单code
	 * @return
	 */
	HoldDetailResponse findHoldApartByOrderCode(String orderCode)throws BizException ;

	
	/**
	 * 根据分仓Oid查询分仓信息
	 * @param holdApartOid
	 * @return
	 * @throws BizException
	 */
	HoldApart findHoldApartByOid(String holdApartOid)throws BizException ;
	/**
	 * 分仓信息  计息快照
	 */
	void snapshot()throws BizException ;
	
	/**
	 * 按产品维度进行计息份额份额快照
	 * @param product
	 */
	void snaphostVolume(String productOid, String incomeDate)throws BizException ;
	
	/**
	 * 创建分仓信息
	 * @param tradeOrder
	 * @param hold
	 * @return
	 */
	int createInvestApart(InvestorTradeOrder tradeOrder,Hold hold)throws BizException ;
	
	
	/**
	 * 根据订单平仓（赎回订单使用）
	 * 
	 * @param tradeOrder
	 */
	void flatWare(InvestorTradeOrder redeemOrder)throws BizException ;
	
	
	/**
	 * 分仓作废（废单情况下使用）
	 * 
	 * @param orderOid
	 */
	Boolean abandon(String orderOid) throws BizException ;
	
	/**
	 * 解锁可计息份额
	 * @param holdApardOid
	 * @param investVolume 
	 * @return
	 * @throws BizException
	 */
	int unlockAccrual(String holdApardOid, BigDecimal investVolume) throws BizException ;
	
	
	/**
	 * 解锁赎回订单
	 * 
	 * @param tradeOrder
	 * @return
	 */
	int unlockRedeem(String holdApardOid)throws BizException ;
	
	/**
	 * 根据赎回状态，持仓状态，开始赎回日查找分仓（ 根据分仓更新合仓可赎回份额调用）
	 * @param date
	 * @param oid
	 * @return
	 * @throws BizException
	 */
	List<HoldApart> findByBeforeBeginRedeemDateInclusive(String date, String oid)throws BizException ;

	/**
	 * 根据可计息状态，持仓状态，开始起息日查找分仓
	 */
	List<HoldApart> findByBeforeBeginAccuralDateInclusive(String date, String oid)throws BizException ;
	/**
	 * 查询可计息分仓
	 * @param hold
	 * @param incomeDate
	 * @return
	 */
	List<HoldApart> findInterestableApart(String holdOid, String incomeDate)throws BizException ;
	
	/**
	 * 按产品统计试算份额
	 * 
	 * @param productOid
	 * @return
	 */
	BigDecimal getCountByProduct4Practice(String productOid, String incomeDate)throws BizException ;
	
	BigDecimal getTotalCountByProduct4Practice(String productOid, String incomeDate)throws BizException ;
	
	/**
	 * 分配收益,更新分仓收益字段
	 */
	Boolean updateHoldApart4Interest(String apartOid, BigDecimal interestedVolume,
		BigDecimal interestedAmount, BigDecimal netUnitAmount, Date incomeDate,
		 BigDecimal incomeAmount, BigDecimal rewardAmount,BigDecimal snapshotVolume,
		 BigDecimal marketingAmount,Integer marketingHold,HoldApartTypeChangeLog changeLog)throws BizException ;
	
	/**
	 * 加载分仓信息到缓存
	 * @return
	 */
	Boolean loadHoldApartData2Cache();

	Boolean loadHoldApartData2CacheMulti() throws BizException, Exception;

	/**
	 * 检查分仓数据与缓存异同-
	 *
	 * @return
	 */
	Boolean compareApartHoldDataWithCache()  throws Exception;
	
	/**
	 * 确认份额，更新相应字段
	 * @param orderId
	 * @param accruableHoldVolume 
	 * @param redeemStatus
	 * @param accrualStatus
	 * @return
	 * @throws BizException
	 */
	Boolean update4Confirm(String orderId, BigDecimal accruableHoldVolume, String redeemStatus, String accrualStatus)throws BizException ;

	/**
	 * 受理赎回当日不计息单子时分仓处理
	 * @param redeemOrder
	 */
	void flatWare4Accept2RedeemNotInterest(InvestorTradeOrder redeemOrder);

	/**
	 * 受理赎回当日不计息单子时指定分仓处理
	 * @param redeemOrder
	 */
	void flatWare4Accept2RedeemByHoldNotInterest(HoldApart holdApart);

	/**
	 * 确认时指定分仓平仓
	 * @param apart
	 */
	void flatWare2RedeemByHold(HoldApart apart,InvestorTradeOrder redeemOrder);
	
	
	/**
	 * 修改分仓锁定奖励Id ，起息日，可赎回日
	 * @param lockRewardId
	 * @return
	 * @throws BizException
	 */
	public Boolean updateHoldApartRewardInfo(String orderOid,String lockRewardId) throws BizException;
	
}