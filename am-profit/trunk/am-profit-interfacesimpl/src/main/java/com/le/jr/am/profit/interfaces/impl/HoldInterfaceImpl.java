package com.le.jr.am.profit.interfaces.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.input.HoldIncomeRequest;
import com.le.jr.am.profit.domain.input.HoldRequest;
import com.le.jr.am.profit.domain.input.LevelHoldRequest;
import com.le.jr.am.profit.domain.input.SearchHoldVo;
import com.le.jr.am.profit.domain.output.HoldDetailRep;
import com.le.jr.am.profit.domain.output.HoldIncomeResponse;
import com.le.jr.am.profit.domain.output.HoldResponse;
import com.le.jr.am.profit.domain.output.InvestResultVo;
import com.le.jr.am.profit.domain.output.LevelHoldResponse;
import com.le.jr.am.profit.interfaces.HoldInterface;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.HoldIncomeService;
import com.le.jr.am.profit.service.HoldLevelIncomeService;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.tstd2.log4j.log.LogTransactionIdManager;

/**
 * 持有人手册接口-合仓
 * xxx
 *
 * @author lining6
 * @date 2016年11月18日 下午1:11:34
 *
 */
@Service("holdInterface")
public class HoldInterfaceImpl implements HoldInterface{
	
	
	Logger logger = LoggerFactory.getLogger(HoldInterfaceImpl.class);
	
	@Resource
	private HoldService holdService;
	
	@Resource
	private HoldApartService holdApartService;
	
	@Resource
	private HoldLevelIncomeService holdLevelIncomeService;
	
	@Resource
	private HoldIncomeService holdIncomeService;
	
	/**
	 * 编程式事务
	 */
	@Resource
	private PlatformTransactionManager transactionManager;

	@Override
	public Message<Hold> getAssetPoolSpvHold(String assetPool, String spvOid,String productOid) {
		logger.info("getAssetPoolSpvHold:assetPool{},spvOid{},productOid{}",assetPool,spvOid,productOid);
		Hold result = null;
	        try {

	            result = holdService.getAssetPoolSpvHold(assetPool,spvOid,productOid);

	        } catch (BizException e) {
	            logger.error("getAssetPoolSpvHold error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("getAssetPoolSpvHold error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}

	

	@Override
	public Message<Boolean> isInvestorExists(String investorOid, String productOid) {
		 logger.info(" isInvestorExists:investorOid{},productOid{}",investorOid,productOid);
		 Boolean result = false;
	        try {

	            result = holdService.isInvestorExists(investorOid,productOid);

	        } catch (BizException e) {
	            logger.error("isInvestorExists error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("isInvestorExists error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}

	@Override
	public Message<Hold> findByOid(String holdOid) {
		logger.info(" findByOid{}",holdOid);
		Hold result = null;
	        try {

	            result = holdService.findByOid(holdOid);

	        } catch (BizException e) {
	            logger.error("findByOid error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("findByOid error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}


	@Override
	public Message<Boolean> spvOrderRedeemConfirm(String oid, BigDecimal orderAmount) {
		logger.info(" spvOrderRedeemConfirm{} amount:{}",oid,orderAmount);
		Boolean result = false;
	        try {

	            result = holdService.spvOrderRedeemConfirm(oid, orderAmount);

	        } catch (BizException e) {
	            logger.error("spvOrderRedeemConfirm error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("spvOrderRedeemConfirm error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Boolean> spvOrderInvestConfirm(String oid, BigDecimal orderAmount) {
		logger.info(" spvOrderInvestConfirm{} amount{}",oid,orderAmount);
		Boolean result = false;
	        try {

	            result = holdService.spvOrderInvestConfirm(oid, orderAmount);

	        } catch (BizException e) {
	            logger.error("spvOrderInvestConfirm error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("spvOrderInvestConfirm error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Page<Hold>> selectHolds4Api(SearchHoldVo vo) {
		logger.info(" selectHolds4Api{} ",JsonUtils.writeValue(vo));
		Page<Hold> result = null;
	        try {

	            result = holdService.selectHolds4Api(vo);

	        } catch (BizException e) {
	            logger.error("selectHolds4Api error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("selectHolds4Api error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}

	@Override
	public Message<Boolean> saveAndFlush(Hold hold) {
		 logger.info(" saveAndFlush{}",JsonUtils.writeValue(hold));
	        try {
	            holdService.saveEntity(hold);

	        } catch (BizException e) {
	            logger.error("isInvestorExists error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("isInvestorExists error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(true);
	}

	@Override
	public Message<Boolean> updateEntity(Hold hold) {
		logger.info(" updateEntity{} ",JsonUtils.writeValue(hold));
		Boolean result = null;
	        try {

	            result = holdService.updateEntity(hold);

	        } catch (BizException e) {
	            logger.error("updateEntity error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("updateEntity error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Hold> findByInvestorOidAndProduct(String investorOid, String productOid) {
		logger.info(" findByInvestorOidAndProduct:investorOid{},productOid{} ",investorOid,productOid);
		Hold result = null;
	        try {
	            result = holdService.findByInvestorOidAndProduct(investorOid, productOid);
	        } catch (BizException e) {
	            logger.error("findByInvestorOidAndProduct error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("findByInvestorOidAndProduct error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<InvestResultVo> invest(InvestorTradeOrder order) {
		// 重置日志的线程号
		LogTransactionIdManager.resetTransactionId();
		logger.info("invest:orderCode{} ",order.getOrderCode());
		InvestResultVo result;
	        try {
	        	result =holdService.invest(order);
				logger.info("result:{}",JsonUtils.writeValue(result));
	        } catch (BizException e) {
	            logger.error("invest error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("invest error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	



	/**
	 * 提供给订单的赎回
	 */
	@Override
	public Message<Boolean> redeem4Order(InvestorTradeOrder order,String submitType) {
		// 重置日志的线程号
		LogTransactionIdManager.resetTransactionId();
		logger.info("redeem:orderCode:{} ,submitType:{}",order.getOrderCode(),submitType);
	    try {
	    	holdService.redeem(order,submitType);
	    } catch (BizException e) {
	        logger.error("redeem error...", e);
	        return Messages.failed(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        logger.error("redeem error...", e);
	        return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	    }
	    return Messages.success(true);
	}



	@Override
	public Message<Boolean> abandonInvestOrder(InvestorTradeOrder tradeOrder) {
		logger.info("abandonInvestOrder:order:{} ",JsonUtils.writeValue(tradeOrder));
		Boolean result = false;
	        try {

	         result = holdService.abandonInvestOrder(tradeOrder);
	            

	        } catch (BizException e) {
	            logger.error("abandonInvestOrder error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("abandonInvestOrder error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Boolean> volHoldconfirmDetail(String offsetOid) {
		logger.info("volHoldconfirmDetail:offsetOid:{},lastOid:{} ", offsetOid);
		Boolean result = false;
	        try {

	           result =  holdService.volHoldconfirmDetail(offsetOid);
	            

	        } catch (BizException e) {
	            logger.error("volHoldconfirmDetail error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("volHoldconfirmDetail error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<LevelHoldResponse> queryBalanceGroupByLevel(LevelHoldRequest request) {
		logger.info("queryBalanceGroupByLevel:request{} ", JsonUtils.writeValue(request));
		LevelHoldResponse result = null;
	        try {

	           result =  holdLevelIncomeService.queryBalanceGroupByLevel(request);
	            

	        } catch (BizException e) {
	            logger.error("queryBalanceGroupByLevel error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("queryBalanceGroupByLevel error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<HoldDetailRep> getSPVHoldDetail(String assetPoolOid, String productOid) {
		logger.info("getSPVHoldDetail assetPoolOid{}productOid{} ", assetPoolOid,  productOid);
		HoldDetailRep result = null;
	        try {

	           result =  holdService.getSPVHoldDetail( assetPoolOid,  productOid);
	            

	        } catch (BizException e) {
	            logger.error("getSPVHoldDetail error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("getSPVHoldDetail error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<HoldResponse> queryUserBalance(HoldRequest request) {
		logger.info("queryUserBalance request:{} ", JsonUtils.writeValue(request));
		HoldResponse result = null;
	        try {

	           result =  holdService.queryUserBalance(request);
	           
	        } catch (BizException e) {
	            logger.error("queryUserBalance error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("queryUserBalance error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<HoldIncomeResponse> queryHoldIncome(HoldIncomeRequest request) {
		logger.info("queryHoldIncome request:{} ", JsonUtils.writeValue(request));
		HoldIncomeResponse result = null;
	        try {

	           result =  holdIncomeService.queryHoldIncome(request);
	            

	        } catch (BizException e) {
	            logger.error("queryHoldIncome error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("queryHoldIncome error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Boolean> loadHoldAndApartRedis() {
		try{
			 holdService.loadHoldData2Cache();
			 holdApartService.loadHoldApartData2Cache();
			 return Messages.success(true);
		}catch (Exception e) {
            logger.error("刷新缓存失败...", e);
            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
        }
	}

	
}
