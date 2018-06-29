package com.le.jr.am.profit.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.profit.dao.HoldApartCloseDetailsMapper;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.HoldApartCloseDetails;
import com.le.jr.am.profit.domain.enums.CloseChangeDirection;
import com.le.jr.am.profit.service.HoldApartCloseDetailService;


@Service("holdApartCloseDetailService")
public class HoldApartCloseDetailServiceImpl implements HoldApartCloseDetailService {
	
	
	@Resource
	private HoldApartCloseDetailsMapper holdApartCloseDetailsMapper;

	@Override
	public int createCloseDetails(HoldApart part, BigDecimal volume, InvestorTradeOrder redeemOrder,String orderCode) {
		HoldApartCloseDetails entity = new HoldApartCloseDetails();
		entity.setOid(UUIDUtil.creatUUID());
		entity.setHoldApartOid(part.getOid());
		entity.setOrderOid(redeemOrder.getOid());
		entity.setInvestOrderCode(orderCode);
		entity.setChangeVolume(volume);
		entity.setChangeDirection(CloseChangeDirection.OUT.value);
		
		Date date = new Date();
		entity.setCreateTime(date);
		entity.setUpdateTime(date);
		return this.holdApartCloseDetailsMapper.insert(entity);
		
		//return this.saveEntity(entity);
		
	}

	/*@Override
	public int saveEntity(HoldApartCloseDetails entity) {
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		return this.updateEntity(entity);
	}

	@Override
	public int updateEntity(HoldApartCloseDetails entity) {
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		return this.holdApartCloseDetailsMapper.insert(entity);
	}*/

	@Override
	public HoldApartCloseDetails selectByPrimaryKey(String oid) {
		return holdApartCloseDetailsMapper.selectByPrimaryKey(oid);
	}

}
