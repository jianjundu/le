package com.le.jr.am.profit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.enums.HoldAccountTypeEnum;

public interface HoldMapper {

	int insert(Hold record);

	Hold selectByPrimaryKey(String oid);

	int updateByPrimaryKeySelective(Hold record);

	/**
	 * 提供api接口查询
	 * @param map
	 * @return
	 */
	public List<Hold> selectHolds4Api(Map<String, Object> map);
	/**
	 * 提供api接口查询数量
	 * @param map
	 * @return
	 */
	public int selectCountHoldsApi(Map<String, Object> map);
	/**
	 * 提供hold接口查询
	 * @param map
	 * @return
	 */
	public List<Hold> selectHolds(Map<String, Object> map);
	
	//根据时间查询持有列表
	public List<Hold> selectHoldByDate(String startDate);

	/**
	 * 根据产品ID和用户ID查询持仓信息
	 * @param investorOid
	 * @param productOid
	 * @return
	 */
	Hold findByInvestorOidAndProduct(@Param("investorOid") String investorOid, @Param("productOid") String productOid);

	/**
	 * 投资后更新持有人
	 * 
	 * @param oid
	 *            持有人名录oid
	 * @param netUnitShare
	 *            单位净值
	 * @param volume
	 *            交易份额
	 * @param lockRedeemHoldVolume
	 *            赎回锁定份额
	 * @param redeemableHoldVolume
	 *            可赎回份额
	 * @return
	 */
	int invest(@Param("holdOid") String holdOid, @Param("netUnitShare") BigDecimal netUnitShare, @Param("volume") BigDecimal volume);

	/**
	 * 赎回，持仓人处理份额
	 * @param volume
	 * @param netUnitShare
	 * @param investorOid
	 * @param productOid
	 * @return
	 */
	public int redeem(@Param("volume") BigDecimal volume, @Param("netUnitShare") BigDecimal netUnitShare,
			@Param("investorOid") String investorOid, @Param("productOid") String productOid);

	public int invest4Abandon(@Param("volume") BigDecimal volume, @Param("netUnitShare") BigDecimal netUnitShare,
			@Param("investorOid") String investorOid, @Param("productOid") String productOid);

	/**
	 * 通过注册表重新检测，增加可计息份额，减少计息锁定份额
	 * 
	 * @param oid
	 *            持有人主键
	 * @param volume
	 *            份额
	 * @return
	 */

	public int unlockAccrual(@Param("holdOid") String holdOid, @Param("volume") BigDecimal volume);

	/**
	 * 赎回锁定期到了 增加可赎回份额，减少赎回锁定份额
	 * 
	 * @param oid
	 *            持有人主键
	 * @param volume
	 *            份额
	 * @return
	 */
	public int unlockRedeem(@Param("holdOid") String holdOid, @Param("volume") BigDecimal volume);

	public int redeem4Abandon(@Param("volume") BigDecimal volume, @Param("investorOid") String investorOid,
			@Param("productOid") String productOid);

	public int redeem4AbandonOfDayRedeemVolume(@Param("orderVolume") BigDecimal orderVolume, @Param("investorOid") String investorOid,
			@Param("productOid") String productOid);

	public List<Hold> findByProduct(String productOid);

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
	 * @return
	 */

	public int currentCalc(@Param("interest") BigDecimal interest, @Param("addHoldVolume") BigDecimal addHoldVolume,
			@Param("netUnitShare") BigDecimal netUnitShare, @Param("incomeDate") Date incomeDate, @Param("hoid") String hoid);

	public List<Hold> findByProductAndHoldStatus(@Param("productOid") String productOid, @Param("holdStatus") String holdStatus,
			@Param("holdOid") String holdOid, @Param("accountType") String accountType, @Param("incomeDate") String incomeDate);

	/**
	 * 更新合仓持有收益
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

	public int updateHold4Interest(@Param("holdOid") String holdOid, @Param("holdInterestVolume") BigDecimal holdInterestVolume,
			@Param("holdInterestAmount") BigDecimal holdInterestAmount, @Param("lockHoldInterestVolume")BigDecimal lockHoldInterestVolume,
			@Param("lockHoldInterestAmount") BigDecimal lockHoldInterestAmount,@Param("netUnitAmount") BigDecimal netUnitAmount,
			@Param("incomeDate") Date incomeDate, @Param("holdInterestBaseAmount") BigDecimal holdInterestBaseAmount,
			@Param("holdInterestRewardAmount") BigDecimal holdInterestRewardAmount,@Param("holdInterestMarketingAmount") BigDecimal holdInterestMarketingAmount);


	/**
	 * 减少持有人锁定赎回份额
	 * 
	 * @param product
	 * @param accountType
	 * @param orderVolume
	 * @return
	 */

	public int updateSpvHold4InvestAbandon(@Param("productOid") String productOid, @Param("accountType") String accountType,
			@Param("orderVolume") BigDecimal orderVolume);

	public int updateMaxHold4InvestAbandon(@Param("investorOid") String investorOid, @Param("productOid") String productOid,
			@Param("orderVolume") BigDecimal orderVolume);

	public int isInvestorExists(@Param("investorOid") String investorOid, @Param("productOid") String productOid);

	public int resetDayRedeemVolume();

	public int updateHold4Confirm(@Param("holdOid") String holdOid, @Param("redeemableHoldVolume") BigDecimal redeemableHoldVolume,
			@Param("lockRedeemHoldVolume")BigDecimal lockRedeemHoldVolume, @Param("accruableHoldVolume") BigDecimal accruableHoldVolume);

	int updateMaxHold4Invest(@Param("investorOid") String investorOid, @Param("productOid") String productOid,
			@Param("proMaxHoldVolume")BigDecimal proMaxHoldVolume, @Param("orderVolume") BigDecimal orderVolume);

	/**
	 * 赎回份额确认
	 * 
	 * @param product
	 * @param publisherAccounttypeSpv
	 * @param orderVolume
	 * @return
	 */

	public int update4RedeemConfirm(@Param("product") String product, @Param("accountType") String accountType,
			@Param("orderVolume") BigDecimal orderVolume);

	/**
	 * 投资确认
	 * 
	 * @param product
	 * @param publisherAccounttypeSpv
	 * @param orderVolume
	 * @return
	 */
	/*
	 * 
	 * 
	 * @Modifying
	 */
	public int update4InvestConfirm(@Param("productOid") String productOid,
			@Param("publisherAccounttypeSpv") String publisherAccounttypeSpv, @Param("orderVolume") BigDecimal orderVolume);

	/**
	 * 投资 增加SPV锁定赎回金额
	 * 
	 * @param product
	 * @param accountType
	 * @param orderVolume
	 * @return
	 */
	int updateSpvHold4Invest(@Param("productOid")String productOid, @Param("accountType")String accountType,
			@Param("orderVolume") BigDecimal orderVolume);
	
	/**
	 * spv赎回订单审核确定调整totalHoldVolume
	 * @param oid
	 * @param orderAmount
	 * @return
	 */
	
	public int spvOrderRedeemConfirm(@Param("oid")String oid,@Param("orderAmount")BigDecimal orderAmount);
	
	
	/**
	 * spv申购订单审核确定调整totalHoldVolume
	 * @param oid
	 * @param orderAmount
	 * @return
	 */
	
	public int spvOrderInvestConfirm(@Param("oid")String oid,@Param("orderAmount")BigDecimal orderAmount);

	/**
	 * Description:根据产品id查询持仓数量
	 *
	 * @Author:huangqilin@le.com
	 * @date: 2016/12/2 15:33
	 */
	Integer findHoldNumByPid(String oid);
	
	
	List<Hold> findByProductAndHoldStatus4lx(@Param("productOid")String productOid,@Param("holdStatus")String  holdStatus,
			@Param("holdOid")String holdOid, @Param("holdAccountType")String holdAccountType);

	/**
	 * 根据investorOid和productOid查询持仓4Update
	 * @param investorOid
	 * @param productOid
	 */
	Hold selectByInvestorOidAndProductOid4Update(@Param("investorOid")String investorOid, @Param("productOid")String productOid);
}