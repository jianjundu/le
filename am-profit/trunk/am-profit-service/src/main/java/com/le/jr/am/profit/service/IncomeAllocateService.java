package com.le.jr.am.profit.service;

import java.util.Date;
import java.util.List;

import com.le.jr.am.profit.domain.IncomeAllocate;
import com.le.jr.am.profit.domain.input.SearchAllocateVO;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;

public interface IncomeAllocateService {

	/**
	 * 根据条件查询收益分配记录
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	List<IncomeAllocate> selectIncomeAllocates(SearchAllocateVO vo)throws BizException;
	/**
	 * 根据条件查询收益分配记录 分页
	 * @param vo
	 * @return
	 * @throws BizException
	 */
    Page<IncomeAllocate> selectIncomeAllocates4Api(SearchAllocateVO vo)throws BizException;
	
    
    /**
	 * 根据产品Id查询最新的收益分配日期
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	public Date getLatestIncomeDate(String productOid)throws BizException;
	/**
	 * 查询最后一次分配记录 ，根据产品列表
	 * @param productOids
	 * @return
	 * @throws BizException
	 */
	public List<IncomeAllocate> findLatestIncomeAllocates(List<String> productOids)throws BizException;
	
	/**
	 * 根据分配事件查询分配记录
	 * @param eventOid
	 * @return
	 * @throws BizException
	 */
	IncomeAllocate selectByEventOid(String eventOid)throws BizException;
	/**
	 * 根据分配记录Oid查询记录vo
	 * @param oid
	 * @return
	 * @throws BizException
	 */
	IncomeAllocate selectByOid(String oid)throws BizException;
	/**
	 * 根据产品Oid和基准日期查询是否分配记录
	 * @param productOid
	 * @param baseDate
	 * @return
	 * @throws BizException
	 */
	Boolean isIncomeAllocated(String productOid, Date baseDate)throws BizException;
	/**
	 * 保存记录
	 * @param ia
	 * @return
	 * @throws BizException
	 */
	Boolean insert(IncomeAllocate ia)throws BizException;
	
	/**
	 * 查询最后一次有效分配记录
	 * @param productOid
	 * @return
	 * @throws BizException
	 */
	IncomeAllocate findLastValidIncomeAllocate(String productOid)throws BizException;
	
	
	Boolean updateIncomeAllocate(IncomeAllocate vo)throws BizException;
	

}
