package com.le.jr.am.profit.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.profit.dao.shard.MonthShardingValueStrategy;
import com.le.jr.am.profit.domain.LevelIncome;
import com.lejr.platform.mybatis.spring.sharding.annotation.NonSharding;
import com.lejr.platform.mybatis.spring.sharding.annotation.Sharding;
import com.lejr.platform.mybatis.spring.sharding.annotation.ShardingTable;

@ShardingTable(tablePrefix = "T_MONEY_PUBLISHER_INVESTOR_LEVELINCOME_")
public interface LevelIncomeMapper {

	@NonSharding
    int insert(LevelIncome record);

	@NonSharding
    LevelIncome selectByPrimaryKey(String oid);
	@NonSharding
    int updateByPrimaryKeySelective(LevelIncome record);

    
	@NonSharding
    int saveList(List<LevelIncome> list);
 
	@NonSharding
	List<LevelIncome> queryBalanceGroupByLevel(@Param("investorOid")String investorOid, @Param("productOid")String productOid,@Param("confirmDate") String confirmDate);
	
	@Sharding(property = "confirmDate", shardingValueStrategy = MonthShardingValueStrategy.class)
	List<LevelIncome> queryBalanceGroupByLevelInHist(@Param("investorOid")String investorOid, @Param("productOid")String productOid,@Param("confirmDate") String confirmDate);
	
	@NonSharding
	List<LevelIncome> queryLevelIncomeCache(String start);
	
	@Sharding(property = "newTable", shardingValueStrategy = MonthShardingValueStrategy.class)
	int createAndBackupLevelIncome(@Param("newTable")String newTable,@Param("queryDate")String queryDate);
	
	@Sharding(property = "newTable", shardingValueStrategy = MonthShardingValueStrategy.class)
	int createTableLevelIncome(@Param("newTable")String newTable);
	
	@NonSharding
	int deleteLevelIncomeBackupData(@Param("queryDate")String queryDate);
	
	@NonSharding
	List<Date> queryBackupDate();
	

}