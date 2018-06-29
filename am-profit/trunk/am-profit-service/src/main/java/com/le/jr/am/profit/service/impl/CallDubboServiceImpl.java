package com.le.jr.am.profit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.assetpool.domain.GamAssetpool;
import com.le.jr.am.assetpool.interfaces.GamAssetpoolInterfaces;
import com.le.jr.am.order.common.util.JsonUtil;
import com.le.jr.am.order.common.util.ValidateUtil;
import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.order.domain.OrderLog;
import com.le.jr.am.order.interfaces.OrderInterface;
import com.le.jr.am.order.interfaces.OrderLogInterface;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.product.domain.vo.SearchProductVo;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.service.CallDubboService;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.touchcenter.domain.dto.touch.TouchParameter;
import com.le.jr.trade.touchcenter.interfaces.touch.QuasiRealtimeTouchInterface;


@Service("callDubboService")
public class CallDubboServiceImpl implements CallDubboService {
	private static final Logger logger = LoggerFactory.getLogger(CallDubboServiceImpl.class);
	
	@Resource
	private GamAssetpoolInterfaces gamAssetpoolInterfaces;
	@Resource
	private OrderInterface orderInterface;
	@Resource
	private OrderLogInterface orderLogInterface;
	@Resource
	private ProductInterfaces productInterfaces;
	@Resource
	private QuasiRealtimeTouchInterface quasiRealtimeTouchInterface;
	
	@Override
	public Message<ProductIncomeReward> callSelectProductRewardByOid(String rewardOid){
		Message<ProductIncomeReward> message;
		try{
			message= productInterfaces.selectProductRewardByOid(rewardOid);
		}catch(Exception e){
			logger.error("调用productInterfaces.selectProductRewardByOid失败,rewardOid:"+rewardOid,e);
			throw new RuntimeException("调用productInterfaces.selectProductRewardByOid失败");
		}
		if(!ValidateUtil.interfaceValidate(message)){
			logger.error("调用productInterfaces.selectProductRewardByOid返回result不正确或结果为空，rewardOid:{}",rewardOid);
			throw new BizException("调用productInterfaces.selectProductRewardByOid返回result不正确或结果为空");
		}
		return message;
	}
	
	@Override
	public Message<ProductIncomeReward> callGetRewardEntity(String productOid, int holdDays, String productIncomeRewardOid,Byte subType){
		Message<ProductIncomeReward> message;
		try{
			message= productInterfaces.getRewardEntity(productOid, holdDays, productIncomeRewardOid,subType);
		}catch(Exception e){
			logger.error("调用productInterfaces.getRewardEntity失败,productOid:"+productOid+",holdDays:"+holdDays+",productIncomeRewardOid:"+productIncomeRewardOid+",subType:"+subType,e);
			throw new RuntimeException("调用productInterfaces.getRewardEntity失败");
		}
		if(!ValidateUtil.interfaceValidateResult(message)){
			logger.error("调用productInterfaces.getRewardEntity返回result不正确或结果为空，productOid:{},holdDays:{},productIncomeRewardOid:{},subType:{}",productOid,holdDays,productIncomeRewardOid,subType);
			throw new BizException("调用productInterfaces.getRewardEntity返回result不正确");
		}
		return message;
	}
			
	@Override
	public Message<Product> callSelectProductByOid(String productOid) {
		Message<Product> message;
		try{
			message=productInterfaces.selectProductByOid(productOid);
		}catch(Exception e){
			logger.error("调用productInterfaces.selectProductByOid失败,assetPoolOid:"+productOid,e);
			throw new RuntimeException("调用productInterfaces.selectProductByOid失败");
		}
		if(!ValidateUtil.interfaceValidate(message)){
			logger.error("调用productInterfaces.selectProductByOid返回result不正确或结果为空，productOid:{},code:{}",productOid,message.getCode());
			throw new BizException("调用productInterfaces.selectProductByOid返回result不正确或结果为空");
		}
		return message;
	}
	
	@Override
	public Message<List<Product>> callSelectProductByVo(SearchProductVo searchProductVo) {
		Message<List<Product>> message;
		try{
			message=productInterfaces.selectProductByVo(searchProductVo);
		}catch(Exception e){
			logger.error("调用productInterfaces.selectProduct失败,searchProductVo:"+JsonUtil.objectToJson(searchProductVo),e);
			throw new RuntimeException("调用productInterfaces.selectProduct失败");
		}
		if(!ValidateUtil.interfaceValidateResult(message)){
			logger.error("调用productInterfaces.selectProduct返回result不正确，searchProductVo:{},code:{}",JsonUtil.objectToJson(searchProductVo),message.getCode());
			throw new BizException("调用productInterfaces.selectProduct返回result不正确");
		}
		return message;
	}
	
	@Override
	public Message<GamAssetpool> callGetAssetpoolByOid(String assetPoolOid) {
		Message<GamAssetpool> message;
		try{
			message=gamAssetpoolInterfaces.getAssetpoolByOid(assetPoolOid);
		}catch(Exception e){
			logger.error("调用gamAssetpoolInterfaces.getAssetpoolByOid失败,assetPoolOid:"+assetPoolOid,e);
			throw new RuntimeException("调用gamAssetpoolInterfaces.getAssetpoolByOid失败");
		}
		if(!ValidateUtil.interfaceValidate(message)){
			logger.error("调用gamAssetpoolInterfaces.getAssetpoolByOid返回result不正确或结果为空，assetPoolOid:{},code:{}",assetPoolOid,message.getCode());
			throw new BizException("调用gamAssetpoolInterfaces.getAssetpoolByOid返回result不正确或结果为空");
		}
		return message;
	}
	
	@Override
	public Message<InvestorTradeOrder> callGetOrderByOid(String orderOid) {
		Message<InvestorTradeOrder> message;
		try{
			message=orderInterface.getOrderByOid(orderOid);
		}catch(Exception e){
			logger.error("调用orderInterface.getOrderByOid失败,orderOid:"+orderOid,e);
			throw new RuntimeException("调用orderInterface.getOrderByOid失败");
		}
		if(!ValidateUtil.interfaceValidate(message)){
			logger.error("调用orderInterface.getOrderByOid返回result不正确或结果为空，orderOid:{},code:{}",orderOid,message.getCode());
			throw new BizException("调用orderInterface.getOrderByOid返回result不正确或结果为空");
		}
		return message;
	}
	
	@Override
	public Message<InvestorTradeOrder> callGetOrderByOrderCode(String orderCode) {
		Message<InvestorTradeOrder> message;
		try{
			message=orderInterface.getOrderByCode(orderCode);
		}catch(Exception e){
			logger.error("调用orderInterface.getOrderByCode失败,orderCode:"+orderCode,e);
			throw new RuntimeException("调用orderInterface.getOrderByCode失败");
		}
		if(!ValidateUtil.interfaceValidateResult(message)){
			logger.error("调用orderInterface.getOrderByCode返回result不正确或结果为空，orderCode:{},code:{}",orderCode,message.getCode());
			throw new BizException("调用orderInterface.getOrderByCode返回result不正确或结果为空");
		}
		return message;
	}

	@Override
	public Message<Boolean> callAddLogAndNotify(OrderLog orderLog) {
		Message<Boolean> message;
		try{
			message=orderLogInterface.addLogAndNotify(orderLog);
		}catch(Exception e){
			logger.error("调用orderLogInterface.addLogAndNotify失败,orderOid:"+orderLog.getTradeOrderOid(),e);
			throw new RuntimeException("调用orderLogInterface.addLogAndNotify失败");
		}
		if(!ValidateUtil.interfaceValidateResult(message)|| !message.getData()){
			logger.error("调用orderLogInterface.addLogAndNotify返回result不正确或结果为空，orderOid:{},code:{}",orderLog.getTradeOrderOid(),message.getCode());
			throw new BizException("调用orderLogInterface.addLogAndNotify返回result不正确或结果为空");
		}
		return message;
	}
	
	@Override
	public Message<Boolean> callTouch(TouchParameter touchParameter) {
		Message<Boolean> message;
		try{
			message=quasiRealtimeTouchInterface.touch(touchParameter);
		}catch(Exception e){
			logger.error("调用quasiRealtimeTouchInterface.touch失败,touchParameter:"+JsonUtil.objectToJson(touchParameter),e);
			throw new RuntimeException("调用quasiRealtimeTouchInterface.touch失败");
		}
		if(!ValidateUtil.interfaceValidateResult(message)|| !message.getData()){
			logger.error("调用quasiRealtimeTouchInterface.touch返回result不正确，touchParameter:"+JsonUtil.objectToJson(touchParameter));
			throw new BizException("调用quasiRealtimeTouchInterface.touch返回result不正确");
		}
		return message;
	}

}
