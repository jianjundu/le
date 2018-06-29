package com.le.jr.am.profit.service;

import java.util.List;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.domain.HoldApartIncome;
import com.le.jr.am.profit.domain.input.ApartIncomeRequest;
import com.le.jr.am.profit.domain.input.HoldDetailRequest;
import com.le.jr.am.profit.domain.input.ProductOrderIncomeReqVo;
import com.le.jr.am.profit.domain.output.ApartIncomeResponse;
import com.le.jr.am.profit.domain.output.CalcSumInterest;
import com.le.jr.am.profit.domain.output.HoldDetailResponse;
import com.le.jr.am.profit.domain.output.ProductOrderIncomeRespVo;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;

public interface HoldApartIncomeService {
	
	/**
	 * 按计息日，产品统计分仓合计所得总收益，计息总份额
	 * @param productOid
	 * @param incomeDate
	 * @return
	 */
	public List<CalcSumInterest> calcSumInterest(String productOid, String incomeDate)throws BizException;
	
	/**
	 * 根据产品和确认收益日获取分仓明细列表
	 * @param productOid
	 * @param confirmDate
	 * @return
	 */
	public List<HoldApartIncome> getByPoidAndConfirmDate(String productOid, String confirmDate)throws BizException;

	/**
	 * 批量保存分仓收益
	 * @param partIncomeList
	 * @return
	 * @throws BizException
	 */
	public int saveBatch(List<HoldApartIncome> partIncomeList)throws BizException ;
	
	
	/**
	 * 根据阶梯Id查询阶梯收益
	 * @param
	 * investorOid  投资人
	 * rewardRuleOid 奖励阶梯ID
	 * productOid 产品Id
	 * @return
	 * @throws BizException
	 */
	public HoldDetailResponse findApartIncomeByRewardOid(String investorOid, String productOid, String rewardRuleOid) throws BizException;

	/**
	 * 查询分仓收益信息
	 */
	public HoldApartIncome queryApartIncome(String investorOid, String tradeOrderOid, String incomeDate)throws BizException;

	/**
	 * 查询分仓历史表信息
	 * @param investorOid
	 * @param tradeOrderOid
	 * @param incomeDate
	 * @return
	 * @throws BizException
	 */
	public HoldApartIncome queryApartIncomeInHis(String investorOid, String tradeOrderOid, String incomeDate)throws BizException ;
	
	/**
	 * 查询分仓收益
	 * 入参：ApartIncomeRequest
	 * @param req
	 * @return
	 * @throws BizException
	 */
	public ApartIncomeResponse queryApartIncome(ApartIncomeRequest req)throws BizException  ;
	
	/**
	 * 加载分仓收益信息到缓存
	 * @return
	 * @throws BizException
	 */
	public Boolean loadApartIncome2Cache()throws BizException ;
	
	/**
	 * 查询分仓收益
	 * @param req
	 * @return
	 */
	public HoldDetailResponse queryBalanceByLevel(HoldDetailRequest request)throws BizException  ;
	
	
	public Boolean backupHoldApartIncomeData2His();
	
	/**
	 * 获取产品下订单派发收益分页数据
	 * @param pageEntity
	 * @return
	 */
	Page<ProductOrderIncomeRespVo> getProductOrderIncomes(PageEntity<ProductOrderIncomeReqVo> pageEntity); 
	
	/**
	 * 获取产品收益条数
	 * @param oVo
	 * @return
	 */
	int selectHoldApartIncomesCount(ProductOrderIncomeReqVo oVo);

	/**
	 * 获取产品收益列表
	 * @param pageEntity
	 * @return
	 */
	List<HoldApartIncome> selectHoldApartIncomes(PageEntity<ProductOrderIncomeReqVo> pageEntity);
	
}
