package com.le.jr.am.profit.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.dao.shard.MonthShardingValueStrategy;
import com.le.jr.am.profit.domain.HoldApartIncome;
import com.le.jr.am.profit.domain.input.ProductOrderIncomeReqVo;
import com.le.jr.am.profit.domain.output.CalcSumInterest;
import com.lejr.platform.mybatis.spring.sharding.annotation.NonSharding;
import com.lejr.platform.mybatis.spring.sharding.annotation.Sharding;
import com.lejr.platform.mybatis.spring.sharding.annotation.ShardingTable;

@ShardingTable(tablePrefix = "T_MONEY_PUBLISHER_INVESTOR_HOLDAPARTINCOME_")
public interface HoldApartIncomeMapper {

	@NonSharding
    int insert(HoldApartIncome record);


	@NonSharding
    HoldApartIncome selectByPrimaryKey(String oid);

	@NonSharding
    int updateByPrimaryKeySelective(HoldApartIncome record);

    
    
    /**
	 * 按计息日，产品统计分仓合计所得总收益，计息总份额
	 * @param productOid 产品oid
	 * @param incomeDate 收益日 
	 * @return
	 */
	@NonSharding
	public List<CalcSumInterest> calcSumInterest(@Param("productOid")String productOid, @Param("incomeDate")String incomeDate);
	@NonSharding
	public List<HoldApartIncome> getByPoidAndConfirmDate(@Param("productOid")String productOid, @Param("confirmDate")String confirmDate);
	@NonSharding
	public List<HoldApartIncome> findApartIncomeByRewardOid(@Param("investorOid")String investorOid,@Param("productOid") String productOid, @Param("rewardRuleOid")String rewardRuleOid,@Param("incomeDate") String incomeDate);
	
	@NonSharding
	public List<HoldApartIncome> queryApartIncome(@Param("investorOid")String investorOid,@Param("tradeOrderOid") String tradeOrderOid,@Param("incomeDate") String incomeDate);

	@NonSharding
	public HoldApartIncome queryFirstApartIncomeByOrderOid(@Param("tradeOrderOid") String tradeOrderOid);


	@Sharding(property = "incomeDate", shardingValueStrategy = MonthShardingValueStrategy.class)
	public List<HoldApartIncome> queryApartIncomeInHis(@Param("investorOid")String investorOid,@Param("tradeOrderOid") String tradeOrderOid,@Param("incomeDate") String incomeDate);
	
	@NonSharding
	public int saveList(List<HoldApartIncome> list);
	
	/**
	 * 根据更新时间查询
	 * @param startDate
	 * @return
	 */
	@NonSharding
	public List<HoldApartIncome> queryApartIncomeByUpdateTime(String startDate);
	
	
	
	@Sharding(property = "newTable", shardingValueStrategy = MonthShardingValueStrategy.class)
	int createAndBackupHoldApartIncome(@Param("newTable")String newTable,@Param("queryDate")String queryDate);
	
	@Sharding(property = "newTable", shardingValueStrategy = MonthShardingValueStrategy.class)
	int createTableHoldApartIncome(@Param("newTable")String newTable);
	
	@NonSharding
	List<Date> queryBackupDate();
	
	@NonSharding
	int deleteHoldApartBackupData(@Param("queryDate")String queryDate);

	@NonSharding
	int selectHoldApartIncomesCount(ProductOrderIncomeReqVo oVo);

	@NonSharding
	List<HoldApartIncome> selectHoldApartIncomes(PageEntity<ProductOrderIncomeReqVo> pageEntity);
	
	
	
	
	
}