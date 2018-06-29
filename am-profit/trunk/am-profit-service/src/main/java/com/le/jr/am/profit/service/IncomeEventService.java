package com.le.jr.am.profit.service;

import java.text.ParseException;
import java.util.List;

import com.le.jr.am.profit.domain.IncomeEvent;
import com.le.jr.am.profit.domain.input.SearchEventVo;
import com.le.jr.trade.publictools.exception.BizException;

public interface IncomeEventService {

	
	/**
	 * 更具资产池Id查询最后一次分配事件
	 * @param assetPoolOid
	 * @return
	 * @throws BizException
	 */
	IncomeEvent getLastIncomeEventByAssetPoolId(String assetPoolOid,String productOid)throws BizException;

	/**
	 * 收益分配执行  加入队列
	 * @throws BizException
	 */
	public void interest()throws BizException;

	/**
	 * 通知分配结果给交易平台
	 * @return
	 * @throws ParseException
	 */
	public Boolean notifyPlatformAllotResult()throws BizException;
	
	/**
	 * 根据分配事件Id查询分配事件记录
	 * @param oid
	 * @return
	 */
	IncomeEvent getIncomeEventByOid(String oid)throws BizException;
	/**
	 * 保存收益分配事件
	 * @param event
	 * @return
	 */
	Boolean insert(IncomeEvent event)throws BizException;
	
	/**
	 * 根据查询条件查询分配事件
	 * @param vo
	 * @return
	 */
	List<IncomeEvent> selectEvents(SearchEventVo vo)throws BizException;
	
	/**
	 * 查询分配事件产品
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	List<String> searchDistincProductEvents(SearchEventVo vo)throws BizException;
	
	
	Boolean updateIncomeEvent(IncomeEvent event);
	
	

}
