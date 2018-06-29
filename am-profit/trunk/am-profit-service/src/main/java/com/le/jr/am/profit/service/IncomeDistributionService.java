package com.le.jr.am.profit.service;

import java.math.BigDecimal;
import java.text.ParseException;

import com.le.jr.am.profit.domain.IncomeAllocate;
import com.le.jr.am.profit.domain.InvestorInterestResult;
import com.le.jr.am.profit.domain.input.IncomeAllocateForm;
import com.le.jr.am.profit.domain.input.SearchAllocateVO;
import com.le.jr.am.profit.domain.output.IncomeAllocateAssetResp;
import com.le.jr.am.profit.domain.output.IncomeAllocateProductResp;
import com.le.jr.am.profit.domain.output.IncomeDistributionResp;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;

/**
 * 收益分配
 * 
 * @author wangyan
 *
 */
public interface IncomeDistributionService {

	
	/**
	 * 根据资产池和和产品id查询收益分配信息
	 * @param assetPoolOid
	 * @param productOid
	 * @return
	 */
	public IncomeAllocateProductResp getIncomeAllocateProduct(String assetPoolOid, String productOid)throws BizException;

	/**
	 * 根据资产池和收益分配日获取 产品总规模和奖励收益
	 * 
	 * @param assetPoolOid
	 * @param incomeDate
	 * @return
	 */
	public IncomeAllocateProductResp getTotalScaleRewardBenefit(String assetPoolOid, String incomeDate)throws BizException ;

	

	/**
	 * 保存收益分配事件 杜建君 2015年10月14日，增加注释
	 * @param form
	 * @param operator
	 * @return
	 * @throws ParseException
	 */
	public Boolean saveIncomeAdjust(IncomeAllocateForm form, String operator) throws BizException ;
	/**
	 * 根据ID获得收益分派事件VO
	 * @param oid 为incomeAllocate的主键
	 * @return
	 */
	public IncomeDistributionResp getIncomeAdjust(String oid)throws BizException;
	/**
	 * 资产池 收益分配列表
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<IncomeDistributionResp> getIncomeAdjustList(SearchAllocateVO vo) throws BizException;

	/**
	 * 审核通过收益分配，并执行收益分配
	 * @param oid
	 * @param operator
	 * @return
	 */
	public Boolean auditPassIncomeAdjust(String oid, String operator)throws BizException;


	/**
	 * 发放收益 乐超收益分配完成后调用
	 * 
	 * @param allocateIncomeReturn
	 */
	public void allocateIncome(InvestorInterestResult allocateIncomeReturn,BigDecimal holdInterestMarketingAmount)throws BizException;

	/**
	 * 审核拒绝，收益分配失败
	 * @param oid
	 * @param operator
	 * @return
	 * @throws BizException
	 */
	public Boolean auditFailIncomeAdjust(String oid, String operator)throws BizException;

	/**
	 * 删除收益分配事件
	 * @param oid
	 * @param operator
	 * @return
	 * @throws BizException
	 */
	public IncomeAllocate deleteIncomeAdjust(String oid, String operator)throws BizException;

	/**
	 * 再次分配
	 * @param oid
	 * @param operator
	 * @return
	 * @throws BizException
	 */
	public Boolean allocateIncomeAgain(String oid, String operator)throws BizException;
	
	/**
	 * 根据资产池查询分配收益数据
	 * @param assetPoolOid
	 * @return
	 * @throws BizException
	 */
	public IncomeAllocateAssetResp getIncomeAllocateAsset(String assetPoolOid)throws BizException;

	

}
