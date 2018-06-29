package com.le.jr.am.profit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.domain.InvestorInterestResult;
import com.le.jr.am.profit.domain.input.ProductIncomeReqVo;
import com.le.jr.am.profit.domain.output.ProductIncomeRespVo;
import com.le.jr.trade.publictools.page.Page;

public interface InvestorInterestResultService {
	
	/**
	 * 记录分配收益结果
	 * @param productOid
	 * @param incomeAllocateOid
	 * @param incomeDate
	 * @param totalVolume 
	 * @return
	 */
	InvestorInterestResult createEntity(String productOid, String incomeAllocateOid, Date incomeDate, BigDecimal totalVolume);

	InvestorInterestResult updateEntity(InvestorInterestResult result) ;

	/**
	 *
	 * @param result 派息结果VO
	 * @param holdInterestMarketingAmount 营销收益（后期优化到result中）
	 */
	void send(InvestorInterestResult result,BigDecimal holdInterestMarketingAmount) ;
	
	/**
	 * 获取产品派发收益分页数据
	 * @param pageEntity
	 * @return
	 */
	Page<ProductIncomeRespVo> getProductIncomes(PageEntity<ProductIncomeReqVo> pageEntity);

	/**
	 * 获取产品收益条数
	 * @param oVo
	 * @return
	 */
	int selectInterestsCount(ProductIncomeReqVo oVo);

	/**
	 * 获取产品收益列表
	 * @param pageEntity
	 * @return
	 */
	List<InvestorInterestResult> selectInterests(PageEntity<ProductIncomeReqVo> pageEntity);

}
