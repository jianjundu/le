package com.le.jr.am.profit.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.profit.dao.IncomeAllocateFailMapper;
import com.le.jr.am.profit.domain.IncomeAllocateFail;
import com.le.jr.am.profit.service.IncomeAllocateFailService;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;

@Service("incomeAllocateFailService")
public class IncomeAllocateFailServiceImpl implements IncomeAllocateFailService{
	
	
	private static final Logger logger = LoggerFactory.getLogger(IncomeAllocateFailServiceImpl.class);
	
	@Resource
	private IncomeAllocateFailMapper incomeAllocateFailMapper;

	public Boolean saveEntity(IncomeAllocateFail result) throws BizException{
		
		logger.info("saveEntity:{}",JsonUtils.writeValue(result));
		
		result.setOid(UUIDUtil.creatUUID());
		
		int i = incomeAllocateFailMapper.insert(result);
		
		
		return i > 0?true:false;
		
	}
   
}