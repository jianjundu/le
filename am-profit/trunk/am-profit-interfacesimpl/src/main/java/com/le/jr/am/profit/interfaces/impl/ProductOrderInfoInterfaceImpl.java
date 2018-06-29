package com.le.jr.am.profit.interfaces.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.domain.input.LeZiXuanReqVo;
import com.le.jr.am.profit.domain.output.LeZiXuanRespVo;
import com.le.jr.am.profit.domain.vo.LeZiXuanPage;
import com.le.jr.am.profit.interfaces.ProductOrderInfoInterface;
import com.le.jr.am.profit.service.ProductOrderInfoService;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Message;

@Service("productOrderInfoInterface")
public class ProductOrderInfoInterfaceImpl implements ProductOrderInfoInterface{
	
	Logger logger = LoggerFactory.getLogger(ProductOrderInfoInterfaceImpl.class);

	@Resource
	private ProductOrderInfoService productOrderInfoService;
	
	@Override
	public Message<LeZiXuanPage<LeZiXuanRespVo>> getLeZiXuanInfos(PageEntity<LeZiXuanReqVo> pageEntity) {
		return Messages.success(productOrderInfoService.getLeZiXuanInfos(pageEntity));
	}
}
