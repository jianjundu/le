package com.le.jr.am.profit.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.domain.LeZiXuanOrderInfo;
import com.le.jr.am.profit.domain.input.LeZiXuanReqVo;
import com.le.jr.am.profit.domain.vo.LeZiXuanPage;

public interface LeZiXuanOrderInfoMapper {
    int deleteByPrimaryKey(String oid);

    int insert(LeZiXuanOrderInfo record);

    int insertSelective(LeZiXuanOrderInfo record);

    LeZiXuanOrderInfo selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(LeZiXuanOrderInfo record);

    int updateByPrimaryKey(LeZiXuanOrderInfo record);

	LeZiXuanOrderInfo selectByProductOidAndRewardOidAndInvestDate(@Param("productOid")String productOid, @Param("rewardOid")String lockRewardOid,
		@Param("orderTime")Date orderTime);

	LeZiXuanPage<?> selectLeZiXuansCountAndSum(LeZiXuanReqVo reqVo);

	List<LeZiXuanOrderInfo> selectLeZiXuans(PageEntity<LeZiXuanReqVo> pageEntity);
}