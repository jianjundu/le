package com.le.jr.am.profit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.input.HoldListRequest;
import com.le.jr.am.profit.domain.input.HoldRequest;
import com.le.jr.am.profit.domain.input.SearchHoldVo;
import com.le.jr.am.profit.domain.output.HoldDetailRep;
import com.le.jr.am.profit.domain.output.HoldQueryRep;
import com.le.jr.am.profit.domain.output.HoldResponse;
import com.le.jr.am.profit.domain.output.InvestResultVo;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;

/**
 * 持有人手册 xxx
 * 
 * @author lining6
 * @date 2016年11月16日 下午8:32:33
 * 
 */
public interface HoldService {

	/**
	 * 根据条件查询总仓信息
	 * 
	 * @param vo
	 * @return
	 */
	List<Hold> selectHolds(SearchHoldVo vo) throws BizException;

	/**
	 * 分页查询总仓信息
	 * 
	 * @param vo
	 * @return
	 */
	Page<Hold> selectHolds4Api(SearchHoldVo vo) throws BizException;

	/**
	 * 根据投资人和产品Id查总仓持仓 唯一信息
	 * 
	 * @param investorOid
	 * @param productOid
	 * @return
	 */
	public Hold findByInvestorOidAndProduct(String investorOid, String productOid) throws BizException;

	/**
	 * 保存持仓
	 * 
	 * @param hold
	 * @return
	 * @throws BizException
	 */
	public Boolean saveEntity(Hold hold) throws BizException;

	/**
	 * 更新持仓
	 * 
	 * @param hold
	 * @return
	 * @throws BizException
	 */
	public Boolean updateEntity(Hold hold) throws BizException;

	/**
	 * 查询通过HoldOid
	 * 
	 * @param holdOid
	 * @return
	 * @throws BizException
	 */
	public Hold findByOid(String holdOid) throws BizException;

	/**
	 * 投资 持有人持有投资额增加
	 * 
	 * @param tradeOrder
	 *            订单信息
	 * @return
	 */
	public InvestResultVo invest(InvestorTradeOrder tradeOrder) throws BizException;

	/**
	 * 赎回操作
	 * @param order
	 * @param submitType 
	 */
	void redeem(InvestorTradeOrder order, String submitType);



	/**
	 * 申购订单作废后，扣除持有人名录和分仓中的锁定份额
	 * 
	 * @param tradeOrder
	 */
	public Boolean abandonInvestOrder(InvestorTradeOrder tradeOrder) throws BizException;

	/**
	 * 解锁赎回份额 进入队列
	 * 
	 * @throws BizException
	 */
	public void unlockRedeem() throws BizException;

	/**
	 * 根据分仓更新合仓可赎回份额 实际执行
	 */
	public void unlockRedeemDo() throws BizException;

	/**
	 * 解锁计息 进入队列
	 */
	public void unlockAccrual() throws BizException;

	/**
	 * 解锁计息 实际执行
	 */
	public void unlockAccrualDo() throws BizException;

	/**
	 * 获取指定产品下面的所有持有人
	 * 
	 * @param product
	 * @return
	 */
	public List<Hold> findByProduct(String productOid) throws BizException;

	/**
	 * 
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	public Page<HoldQueryRep> holdMng(SearchHoldVo vo) throws BizException;

	/**
	 * 活期计息
	 * 
	 * @param interest
	 *            收益金额
	 * @param addHoldVolume
	 *            收益产生的份额
	 * @param netUnitShare
	 *            单位净值
	 * @param incomeDate
	 *            份额确认日期
	 * @param hoid
	 *            持仓Oid
	 */
	public void currentCalc(BigDecimal interest, BigDecimal addHoldVolume, BigDecimal netUnitShare, Date incomeDate, String hoid)
			throws BizException;

	/**
	 * 根据产品和持有状态查询持有人列表
	 * 
	 * @param product
	 * @param holdStatus
	 * @param holdOid
	 * @param incomeDate
	 * @return
	 */
	public List<Hold> findByProductHoldStatus(String productOid, String holdStatus, String holdOid, String incomeDate) throws BizException;

	
	
	public List<Hold> findByProductHoldStatus4lx(String productOid, String holdStatus, String holdOid);
	
	/**
	 * 更新合仓收益
	 * 
	 * @param holdOid
	 * @param holdInterestVolume
	 * @param holdInterestAmount
	 * @param netUnitAmount
	 * @param incomeDate
	 * @param holdInterestBaseAmount
	 * @param holdInterestRewardAmount
	 * @return
	 */
	public Boolean updateHold4Interest(String holdOid, BigDecimal holdInterestVolume, BigDecimal holdInterestAmount,
			BigDecimal lockHoldInterestVolume, BigDecimal lockHoldInterestAmount,BigDecimal netUnitAmount,
			Date incomeDate, BigDecimal holdInterestBaseAmount, BigDecimal holdInterestRewardAmount,BigDecimal holdInterestMarketingAmount) throws BizException;

	/**
	 * 查询资产池关联主体交易持仓
	 * 
	 * @param assetPool
	 * @param spvOid
	 * @param productOid
	 * @return
	 */
	public Hold getAssetPoolSpvHold(String assetPool, String spvOid, String productOid)throws BizException;

	/**
	 * 更新持有份额  （份额确认）
	 * @param publisherHold
	 * @param redeemableHoldVolume
	 * @param lockRedeemHoldVolume
	 * @param accruableHoldVolume
	 * @return
	 * @throws BizException
	 */
	public Boolean updateHold4Confirm(String publisherHold, BigDecimal redeemableHoldVolume, BigDecimal lockRedeemHoldVolume,
			BigDecimal accruableHoldVolume)throws BizException;

	/**
	 * 重置日赎回份额(跑批实现)
	 * 
	 * @author lining6 2016年10月21日
	 */
	public void resetDayRedeemVolume()throws BizException;

	

	/**
	 * @author lining6 2016年10月21日 更新spv持有投资 减少持有人锁定赎回份额
	 * @param product
	 * @param accountType
	 * @param orderVolume
	 * @return
	 */
	public Boolean updateSpvHold4InvestAbandon(String productOid, String accountType, BigDecimal orderVolume)throws BizException;

	/**
	 * 更新最大持有份额
	 * 
	 * @param investorOid
	 * @param product
	 * @param orderVolume
	 */
	public Boolean updateMaxHold4InvestAbandon(String investorOid, Product product, BigDecimal orderVolume)throws BizException;

	/**
	 * 检查投资者是否存在
	 * 
	 * @param investorOid
	 * @param productOid
	 * @return
	 */
	public Boolean isInvestorExists(String investorOid, String productOid)throws BizException;

	

	/**
	 * spv赎回订单审核确定调整totalHoldVolume
	 * 
	 * @param oid
	 * @param orderAmount
	 * @return
	 */

	public Boolean spvOrderRedeemConfirm(String oid, BigDecimal orderAmount)throws BizException;

	/**
	 * spv申购订单审核确定调整totalHoldVolume
	 * 
	 * @param oid
	 * @param orderAmount
	 * @return
	 */
	public Boolean spvOrderInvestConfirm(String oid, BigDecimal orderAmount)throws BizException;

	/**
	 * 加载持仓信息到缓存中
	 * 
	 * @return
	 */
	Boolean loadHoldData2Cache();

    Boolean loadHoldData2CacheMulti() throws BizException, Exception;

	/**
	 * 检查总仓数据与缓存异同-
	 *
	 * @return
	 */
	Boolean compareHoldDataWithCache()  throws Exception;




	/**
	 * 订单调用产品轧差接口
	 * 
	 * @param list
	 * @return
	 */
	public Boolean volHoldconfirmDetail(String offsetOid)throws BizException;

	
	/**
	 * 查询持有人列表
	 * @param req
	 * @return
	 * @throws BizException
	 */
	public HoldResponse queryHoldList(HoldListRequest req)throws BizException;

	/**
	 * 根据资产池Oid 和产品Oid查询持有人详细信息
	 * @param assetPoolOid
	 * @param productOid
	 * @return
	 * @throws BizException
	 */
	public HoldDetailRep getSPVHoldDetail(String assetPoolOid, String productOid)throws BizException;
	
	

	/**
	 * 根据用户Id和产品Id查询用户持有信息
	 * @param request
	 * @return
	 * @throws BizException
	 */
	public HoldResponse queryUserBalance(HoldRequest request)throws BizException;
	
	
	
}
