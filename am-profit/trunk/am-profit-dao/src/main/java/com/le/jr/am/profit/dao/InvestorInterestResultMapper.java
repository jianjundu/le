package com.le.jr.am.profit.dao;

import java.util.List;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.domain.InvestorInterestResult;
import com.le.jr.am.profit.domain.input.ProductIncomeReqVo;


public interface InvestorInterestResultMapper {

    int insert(InvestorInterestResult record);


    InvestorInterestResult selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(InvestorInterestResult record);

	List<InvestorInterestResult> selectInterests(PageEntity<ProductIncomeReqVo> pageEntity);

	int selectInterestsCount(ProductIncomeReqVo oVo);

}