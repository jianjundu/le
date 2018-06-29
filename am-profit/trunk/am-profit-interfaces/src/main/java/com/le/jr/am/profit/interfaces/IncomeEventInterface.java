package com.le.jr.am.profit.interfaces;

import com.le.jr.am.profit.domain.IncomeEvent;
import com.le.jr.am.profit.domain.input.SearchEventVo;
import com.le.jr.trade.publictools.data.Message;

import java.util.List;

public interface IncomeEventInterface {

	
	
	Message<IncomeEvent> getLastIncomeEventByAssetPoolId(String assetPoolOid,String productOid);

	/**
	 * 获取收益分配列表
	 * @param searchEventVo
	 * @return
	 */
	Message<List<IncomeEvent>> selectEvents(SearchEventVo searchEventVo);


}
