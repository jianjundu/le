package com.le.jr.am.profit.interfaces;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.domain.input.LeZiXuanReqVo;
import com.le.jr.am.profit.domain.output.LeZiXuanRespVo;
import com.le.jr.am.profit.domain.vo.LeZiXuanPage;
import com.le.jr.trade.publictools.data.Message;

/**
 *产品订单统计类型接口
 * @author yinxiao
 *
 */
public interface ProductOrderInfoInterface {

	/**
	 * 获取乐自选列表
	 * @param pageEntity
	 * @return
	 */
	Message<LeZiXuanPage<LeZiXuanRespVo>> getLeZiXuanInfos(PageEntity<LeZiXuanReqVo> pageEntity);
	
	
}
