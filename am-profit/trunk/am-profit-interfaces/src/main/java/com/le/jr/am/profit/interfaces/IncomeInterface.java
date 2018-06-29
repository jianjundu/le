package com.le.jr.am.profit.interfaces;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.profit.domain.IncomeAllocate;
import com.le.jr.am.profit.domain.input.IncomeAllocateForm;
import com.le.jr.am.profit.domain.input.ProductIncomeReqVo;
import com.le.jr.am.profit.domain.input.ProductOrderIncomeReqVo;
import com.le.jr.am.profit.domain.input.SearchAllocateVO;
import com.le.jr.am.profit.domain.output.IncomeAllocateAssetResp;
import com.le.jr.am.profit.domain.output.IncomeAllocateProductResp;
import com.le.jr.am.profit.domain.output.IncomeDistributionResp;
import com.le.jr.am.profit.domain.output.ProductIncomeRespVo;
import com.le.jr.am.profit.domain.output.ProductOrderIncomeRespVo;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.page.Page;

public interface IncomeInterface {
	
	Message<Page<IncomeDistributionResp>>   getIncomeAdjustList(SearchAllocateVO vo);
	
	Message<IncomeDistributionResp> getIncomeAdjust(String assetPoolOid);
	
	
	/*Message<IncomeAllocateCalcResp> getIncomeAdjustData(String assetPoolOid);*/
	
	
	Message<IncomeAllocateProductResp> getTotalScaleRewardBenefit(String assetPoolOid, String incomeDate);
	
	
	/**
	 * 保存收益分配
	 * @param form
	 * @return
	 */
	public Message<Boolean> saveIncomeAdjust(IncomeAllocateForm form,String operator);
	
	
	public Message<IncomeAllocateAssetResp> getIncomeAllocateAsset(String assetPoolOid);
	
	
	public Message<IncomeAllocateProductResp> getIncomeAllocateProduct(String assetPoolOid, String productOid);
	
	
	public Message<Boolean> auditFailIncomeAdjust(String oid, String operator);
	
	
	public Message<IncomeAllocate> deleteIncomeAdjust(String oid, String operator);

	public Message<Boolean> allocateIncomeAgain(String oid, String operator);
	
	
	public Message<Boolean> auditPassIncomeAdjust(String oid, String operator);
	
	/**
	 * 获取产品收益分配列表
	 * @param pageEntity
	 * @return
	 */
	Message<Page<ProductIncomeRespVo>> getProductIncomes(PageEntity<ProductIncomeReqVo> pageEntity);
	
	/**
	 * 获取产品下订单收益分配列表
	 * @param pageEntity
	 * @return
	 */
	Message<Page<ProductOrderIncomeRespVo>> getProductOrderIncomes(PageEntity<ProductOrderIncomeReqVo> pageEntity);

}
