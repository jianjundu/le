package com.le.jr.am.profit.service;

import java.util.List;

import com.le.jr.am.assetpool.domain.GamAssetpool;
import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.order.domain.OrderLog;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.product.domain.vo.SearchProductVo;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.touchcenter.domain.dto.touch.TouchParameter;

public interface CallDubboService {
	
	/**
	 * 获取资产池对象byOid
	 * gamAssetpoolInterfaces.getAssetpoolByOid
	 * @param assetPoolOid
	 * @return
	 */
	Message<GamAssetpool> callGetAssetpoolByOid(String assetPoolOid);

	/**
	 * 获取订单byOid
	 * @param orderOid
	 * @return
	 */
	Message<InvestorTradeOrder> callGetOrderByOid(String orderOid);
	
	/**
	 * 获取订单byOrderCode
	 * @param OrderCode
	 * @return
	 */
	Message<InvestorTradeOrder> callGetOrderByOrderCode(String orderCode);

	/**
	 * 获取产品byOid
	 * @param productOid
	 * @return
	 */
	Message<Product> callSelectProductByOid(String productOid);
	
	/**
	 * 获取产品byVo
	 * @param searchProductVo
	 * @return
	 */
	Message<List<Product>> callSelectProductByVo(SearchProductVo searchProductVo);

	/**
	 * 获取奖励阶梯
	 * @param productOid
	 * @param holdDays
	 * @param productIncomeRewardOid
	 * @return
	 */
	Message<ProductIncomeReward> callGetRewardEntity(String productOid, int holdDays, String productIncomeRewardOid,Byte subType);

	/**
	 * 获取奖励阶梯byOid
	 * @param rewardOid
	 * @return
	 */
	Message<ProductIncomeReward> callSelectProductRewardByOid(String rewardOid);
	
	/**
	 * 插入订单日志
	 * @param orderLog
	 * @return
	 */
	Message<Boolean> callAddLogAndNotify(OrderLog orderLog);

	/**
	 * 短信接口
	 * @param touchParameter
	 * @return
	 */
	Message<Boolean> callTouch(TouchParameter touchParameter);

}
