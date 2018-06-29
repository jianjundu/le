package com.le.jr.am.profit.service;

import java.util.List;

import com.le.jr.am.profit.domain.HoldIncome;
import com.le.jr.am.profit.domain.input.HoldIncomeRequest;
import com.le.jr.am.profit.domain.output.HoldIncomeResponse;
import com.le.jr.trade.publictools.exception.BizException;
/**
 * 持有人手册总仓收益
 * xxx
 *
 * @author lining6
 * @date 2016年11月16日 下午8:29:43
 *
 */
public interface HoldIncomeService {
	
	
	
	
	/**
	 * 保存持有人手册总仓收益
	 * @param holdIncome
	 * @return
	 */
	public int saveEntity(HoldIncome holdIncome)throws BizException;

	/**
	 * 更新持有人手册总仓收益
	 * @param holdIncome
	 * @return
	 */
	public int updateEntity(HoldIncome holdIncome)throws BizException ;
	
	/**
	 * 根据投资者Id和确认日期查询持有人手册总仓收益信息
	 * @param investorOid
	 * @param incomeDate
	 * @return
	 */

	public List<HoldIncome> findByInvestorOidAndConfirmDate(String investorOid, String incomeDate)throws BizException ;
	/**
	 * 根据投资者Id和确认日期查询持有人手册总仓收益信息历史，通过分表策略实现，每天备份数据到新的表，通过shell脚本执行，crontab执行
	 * @param investorOid
	 * @param incomeDate
	 * @return
	 */
	public List<HoldIncome> findByInvestorOidAndConfirmDateInHis(String investorOid, String incomeDate)throws BizException ;
	
	/**
	 * 查询持有人总仓收益
	 * @param req
	 * @return
	 * @throws BizException
	 */
	public HoldIncomeResponse queryHoldIncome(HoldIncomeRequest req)throws BizException;
	/**
	 * 备份总仓收益数据到历史表
	 * @return
	 * @throws BizException
	 */
	public Boolean backupHoldIncomeData2His()throws BizException;

}
