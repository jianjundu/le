package com.le.jr.am.profit.service;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.domain.input.LeZiXuanReqVo;
import com.le.jr.am.profit.domain.output.LeZiXuanRespVo;
import com.le.jr.am.profit.domain.vo.LeZiXuanPage;
import com.le.jr.am.profit.domain.vo.ProductTaskVo;

/**
 *产品订单统计类型接口
 * @author yinxiao
 *
 */
public interface ProductOrderInfoService {

	/**
	 * 统计指定产品的订单
	 * @param productTaskVo
	 */
	void LeZiXuanDTS(ProductTaskVo productTaskVo);
	
	/**
	 * 全量统计指定产品的订单
	 * @param productTaskVo
	 */
	void CompareLeZiXuanDTS(ProductTaskVo productTaskVo);

	/**
	 * 获取乐自选列表
	 * @param pageEntity
	 * @return
	 */
	LeZiXuanPage<LeZiXuanRespVo> getLeZiXuanInfos(PageEntity<LeZiXuanReqVo> pageEntity);

}
