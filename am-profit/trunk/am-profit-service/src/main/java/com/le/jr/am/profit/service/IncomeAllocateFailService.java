package com.le.jr.am.profit.service;

import com.le.jr.am.profit.domain.IncomeAllocateFail;
import com.le.jr.trade.publictools.exception.BizException;

/**
 * 分配收益失败信息
 * xxx
 *
 * @author lining6
 * @date 2016年12月7日 下午5:03:03
 *
 */
public interface IncomeAllocateFailService {

	/**
	 * 保存失败信息
	 * @param result
	 * @return
	 */
	public Boolean saveEntity(IncomeAllocateFail result)throws BizException;

}