package com.le.jr.am.profit.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.vo.ProductTaskVo;


public interface HoldApartMapper {

    int insert(HoldApart record);
    
    
    int updateList(List<HoldApart> list);
    
    int selectCountHoldApartsApi(Map<String, Object> map);
    
    List<HoldApart> selectHoldAparts4Api(Map<String, Object> map);
    
    
    List<HoldApart> selectHoldAparts(Map<String, Object> map);
    
    
    


    HoldApart selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(HoldApart record);

    
   
    public List<HoldApart> findByBeforerBeginRedeemDateInclusive(@Param("today")String today, @Param("oid")String oid);
	
	/**
	 * 根据可计算状态，持仓状态，开始起息日查找分仓
	 */
	
	public List<HoldApart> findByBeforeBeginAccuralDateInclusive(@Param("date")String date, @Param("oid")String oid);
	
	/**
	 * 查询可平仓的分仓
	 */
	
	public List<HoldApart> findApart(@Param("investorOid")String investorOid, @Param("productOid")String productOid);
	
	/**
	 * 按产品统计计息份额
	 */
	
	public BigDecimal getCountByProduct4Practice(@Param("productOid")String productOid, @Param("incomeDate") String incomeDate);
	
	
	public BigDecimal getTotalCountByProduct4Practice(@Param("productOid")String productOid, @Param("incomeDate") String incomeDate);
	/**
	 * 按产品统计计息份额
	 */
	
	public BigDecimal getCountByProduct4Interest(@Param("productOid")String productOid, @Param("incomeDate")String incomeDate);
	
	/**
	 * 分仓作废处理
	 */
	
	public int abandon(String tradeOrderId);
	
	/**
	 * 更新可赎回状态
	 */
	
	public int unlockRedeem(String holdApardOid);
	/**
	 * 更新可计息状态和计息份额
	 * @param investVolume 
	 */
	
	public int unlockAccrual(@Param("holdApardOid")String holdApardOid, @Param("investVolume")BigDecimal investVolume);
	
	/**
	 * 查询可计息分仓
	 */
	
	public List<HoldApart> findInterestableApart(@Param("holdOid")String holdOid, @Param("curDate")String curDate);
	/**
	 * 分配收益
	 */
	
	/**
	 * 
	 * @param apartOid
	 * @param interestedVolume
	 * @param interestedAmount
	 * @param netUnitAmount
	 * @param incomeDate
	 * @param incomeAmount
	 * @param rewardAmount
	 * @return
	 */
	public int updateHoldApart4Interest(@Param("apartOid")String apartOid, @Param("interestedVolume")BigDecimal interestedVolume, @Param("interestedAmount")BigDecimal interestedAmount,
			@Param("netUnitAmount")BigDecimal netUnitAmount,@Param("incomeDate")String incomeDate, @Param("incomeAmount")BigDecimal incomeAmount,
			@Param("rewardAmount")BigDecimal rewardAmount,@Param("snapshotVolume")BigDecimal snapshotVolume,@Param("marketingAmount")BigDecimal marketingAmount,@Param("marketingHold")Integer marketingHold);
	/**
	 * 查询渠道下可计息份额
	 */
	
	public BigDecimal findInterestableVolumeByChannelOid(@Param("channelOid")String channelOid, @Param("curDate")String curDate);
	
	/**
	 * 份额确认
	 */
	
	/**
	 * 
	 * @param accruableHoldVolume 
	 * @param orderEntity
	 * @param redeemStatus
	 * @param accrualStatus
	 * @return
	 */
	public int update4Confirm(@Param("orderId")String orderId, @Param("accruableHoldVolume")BigDecimal accruableHoldVolume, @Param("redeemStatus")String redeemStatus, @Param("accrualStatus")String accrualStatus);
	
	/**
	 *计息快照 
	 */
	
	public int  snapshotVolume(@Param("productOid")String productOid,@Param("incomeDate") String incomeDate);
	
	/**
	 * 每个产品在渠道下的已确认份额
	 */
	public List<HoldApart> calcSerFee();
	
	
	
	/**
	 * 根据订单查询分仓
	 */
	public HoldApart findByTradeOrder(String orderId);
	
	/**
	 * 加载最近一段时间的数据到缓存
	 * @param startDate
	 * @return
	 */
	public List<HoldApart> loadHoldApartData2Cache(String startDate);


	/**
	 * 根据订单OID查询分仓记录
	 * @param orderOid
	 * @return
	 */
	public HoldApart selectByOrderOid(String orderOid);


	/**
	 * 根据产品id和创建时间查询分仓记录
	 * @param productTaskVo
	 * @return
	 */
	List<HoldApart> selectByProductOidAndMinute(ProductTaskVo productTaskVo);


	/**
	 * 根据产品id和创建时间查询分仓记录
	 * @param productTaskVo
	 * @return
	 */
	List<HoldApart> selectByProductOidAndDate(ProductTaskVo productTaskVo);
	
	
	
}