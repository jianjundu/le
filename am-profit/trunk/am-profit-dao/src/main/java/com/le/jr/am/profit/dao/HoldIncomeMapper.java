package com.le.jr.am.profit.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.profit.dao.shard.MonthShardingValueStrategy;
import com.le.jr.am.profit.domain.HoldIncome;
import com.lejr.platform.mybatis.spring.sharding.annotation.NonSharding;
import com.lejr.platform.mybatis.spring.sharding.annotation.Sharding;
import com.lejr.platform.mybatis.spring.sharding.annotation.ShardingTable;

@ShardingTable(tablePrefix = "T_MONEY_PUBLISHER_INVESTOR_HOLDINCOME_")
public interface HoldIncomeMapper {
    @NonSharding
    int insert(HoldIncome record);
    
    @NonSharding
    HoldIncome selectByPrimaryKey(String oid);
    @NonSharding
    int updateByPrimaryKeySelective(HoldIncome record);
    
    @NonSharding
	List<HoldIncome> findByInvestorOidAndConfirmDate(@Param("investorOid")String investorOid,@Param("incomeDate") String incomeDate);
	
	@Sharding(property = "incomeDate", shardingValueStrategy = MonthShardingValueStrategy.class)
    List<HoldIncome> findByInvestorOidAndConfirmDateInHis(@Param("investorOid")String investorOid,@Param("incomeDate") String incomeDate) ;
    
	@Sharding(property = "newTable", shardingValueStrategy = MonthShardingValueStrategy.class)
	int createHoldIncomeTable(@Param("newTable")String newTable);
	
	@Sharding(property = "newTable", shardingValueStrategy = MonthShardingValueStrategy.class)
	int backupHoldIncomeData(@Param("newTable")String newTable,@Param("queryDate")String queryDate);
	 
	
	
	@NonSharding
	int deleteBackupHoldIncome(@Param("queryDate")String queryDate);
	
	@NonSharding
	List<Date> queryBackupDate();
	
	

}