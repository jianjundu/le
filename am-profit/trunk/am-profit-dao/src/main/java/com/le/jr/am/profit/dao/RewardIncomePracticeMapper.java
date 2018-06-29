package com.le.jr.am.profit.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.profit.domain.RewardIncomePractice;


public interface RewardIncomePracticeMapper {

    int insert(RewardIncomePractice record);


    RewardIncomePractice selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(RewardIncomePractice record);

    
    
    
    /*@Query(value = "select count(*) from PracticeEntity where product = ?1 and tDate = ?2")*/
	int countByProductAndTDate(@Param("productOid")String productOid, @Param("sqlDate")String sqlDate);

	
	
	
	/*@Query("from PracticeEntity  where product = ?1 and tDate = ?2 and reward is not null order by reward.startDate asc")*/
	List<RewardIncomePractice> findByProductAndTDate(@Param("productOid")String product,@Param("tDate")String tDate);
	
	
	/*@Query("from PracticeEntity  where product = ?1 and tDate = ?2 and reward is null")*/
	RewardIncomePractice findRewardIsNull(@Param("productOid")String productOid,@Param("tDate") String tDate);
	
	List<RewardIncomePractice> findRewardIsNulls(@Param("assetPoolOid")String assetPoolOid,@Param("tDate") String tDate);


	/*@Query("select max(tDate) from PracticeEntity  where product = ?1")*/
	Date findMaxTDate(String  assetPoolOid);
	
	
	int saveBatch(List<RewardIncomePractice> list);
	
	
	/*@Query("select max(tDate) from PracticeEntity  where product = ?1")*/
	Date findMaxTDateByProduct(String productOid);
	
	
	RewardIncomePractice findByRewardAndTDate(@Param("rewardOid")String rewardOid,@Param("tDate")String tDate);
	
	int updateIsAllocateInterestByRewardAndTDate(@Param("rewardOid")String rewardOid,@Param("tDate")String tDate);
	
	/**
	 *
	 * @param product
	 * @param tDate
	 * @return
	 */
	Date findOldDateByProductOid(@Param("productOid")String productOid);

	/**
	 * 
	 * @param tDate
	 * @return
	 */
	List<String> getNotAlloInterestProducts(@Param("tDate")String tDate);
}