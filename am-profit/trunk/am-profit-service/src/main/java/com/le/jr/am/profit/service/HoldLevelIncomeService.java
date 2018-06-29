package com.le.jr.am.profit.service;

import java.util.Date;
import java.util.List;

import com.le.jr.am.profit.domain.LevelIncome;
import com.le.jr.am.profit.domain.input.LevelHoldRequest;
import com.le.jr.am.profit.domain.output.LevelHoldResponse;
import com.le.jr.trade.publictools.exception.BizException;

public interface HoldLevelIncomeService {
	
	/**
	 * 批量持久化<<阶段收益>>
	 * @param values
	 */
	public void saveBatch(List<LevelIncome> values) throws BizException;
	
	/**
	 * 缓存读取数据
	 * @param request
	 * @return
	 */
	public LevelHoldResponse queryBalanceGroupByLevel(LevelHoldRequest request) throws BizException;

	/**
	 * 查询阶梯收益（提供平台接口）
	 * @param investorOid
	 * @param productOid
	 * @param confirmDate
	 * @return
	 * @throws BizException
	 */
	public LevelHoldResponse queryBalanceGroupByLevel(String investorOid, String productOid, String confirmDate)throws BizException;

	/**
	 * 查询阶梯收益信息
	 * @param investorOid
	 * @param productOid
	 * @param incomeDate
	 * @return
	 * @throws BizException
	 */
	public List<LevelIncome> queryBalanceGroupByLevel(String investorOid, String productOid,Date incomeDate )throws BizException;
	

	/**
	 * 查询阶梯收益历史表
	 * @param investorOid
	 * @param productOid
	 * @param confirmDate
	 * @return
	 * @throws BizException
	 */
	public List<LevelIncome> queryBalanceGroupByLevelInHist(String investorOid, String productOid, String confirmDate)throws BizException;
	
	/**
	 * 加载阶梯收益信息到缓存
	 * @return
	 * @throws BizException
	 */
	public Boolean loadLevelIncomeData2Cache()throws BizException;
	/**
	 * 备份阶梯收益数据到历史表
	 * @return
	 * @throws BizException
	 */
	public Boolean backupLevelIncomeData2His()throws BizException;
	
	
	
	

}
