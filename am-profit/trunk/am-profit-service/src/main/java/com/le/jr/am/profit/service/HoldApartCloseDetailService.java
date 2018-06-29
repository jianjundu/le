package com.le.jr.am.profit.service;

import java.math.BigDecimal;

import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.HoldApartCloseDetails;
import com.le.jr.trade.publictools.exception.BizException;

public interface HoldApartCloseDetailService {
	
	
	
	/**
	 * 记录交易单关闭记录
	 * @param part
	 * @param volume
	 * @param redeemOrder
	 * @return
	 */
	public  int createCloseDetails(HoldApart part, BigDecimal volume,InvestorTradeOrder redeemOrder,String orderCode)throws BizException;

	/**
	 * 记录分仓结算记录
	 * @param entity
	 * @return
	 * @throws BizException
	 */
	//public int saveEntity(HoldApartCloseDetails entity) throws BizException;
	/**
	 * 更新分仓结算记录
	 * @param entity
	 * @return
	 * @throws BizException
	 */
	//public int updateEntity(HoldApartCloseDetails entity)throws BizException;
	
	/**
	 * 查询分仓结算记录
	 * @param entity
	 * @return
	 * @throws BizException
	 */
	public HoldApartCloseDetails selectByPrimaryKey(String oid)throws BizException;

}
