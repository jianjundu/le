package com.le.jr.am.profit.interfaces.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.input.ApartIncomeRequest;
import com.le.jr.am.profit.domain.input.HoldDetailRequest;
import com.le.jr.am.profit.domain.input.SearchHoldApartVo;
import com.le.jr.am.profit.domain.output.ApartIncomeResponse;
import com.le.jr.am.profit.domain.output.HoldDetailResponse;
import com.le.jr.am.profit.interfaces.HoldApartInterface;
import com.le.jr.am.profit.service.HoldApartIncomeService;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;
import com.le.jr.trade.publictools.util.JsonUtils;

/**
 * 持有人分仓
 * xxx
 *
 * @author lining6
 * @date 2016年11月18日 下午1:12:01
 *
 */
@Service("holdApartInterface")
public class HoldApartInterfaceImpl implements HoldApartInterface{
	
	Logger logger = LoggerFactory.getLogger(HoldApartInterfaceImpl.class);
	
	@Resource
	private HoldApartService holdApartService;
	
	@Resource
	private HoldApartIncomeService holdApartIncomeService;
	
	

	@Override
	public Message<HoldApart> findHoldApartByOid(String holdApartOid) {
		 logger.info(" findHoldApartByOid{}",holdApartOid);
		 HoldApart result = null;
	        try {

	            result = holdApartService.findHoldApartByOid(holdApartOid);

	        } catch (BizException e) {
	            logger.error("findHoldApartByOid error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("findHoldApartByOid error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
		
	}

	@Override
	public Message<HoldApart> findHoldApartByOrderOid(String orderOid) {
		logger.info(" findHoldApartByOrderOid{}",orderOid);
		 HoldApart result = null;
	        try {

	            result = holdApartService.findHoldApartByOrderId(orderOid);

	        } catch (BizException e) {
	            logger.error("findHoldApartByOrderOid error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("findHoldApartByOrderOid error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	/**
	 * 根据平台订单code查询分仓记录
	 * @param orderCode 平台订单code
	 * @return
	 */
	public Message<HoldDetailResponse> findHoldApartByOrderCode(String orderCode)throws BizException {
		logger.info(" findHoldApartByOrderCode orderCode:{}",orderCode);
		HoldDetailResponse result = null;
		try {

			result = holdApartService.findHoldApartByOrderCode(orderCode);

		} catch (BizException e) {
			logger.error("findHoldApartByOrderCode error...", e);
			return Messages.failed(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.error("findHoldApartByOrderCode error...", e);
			return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
		}
		return Messages.success(result);
	}


	@Override
	public Message<Page<HoldApart>> selectHoldApart4ApiByVo(SearchHoldApartVo vo) {
		logger.info("selectHoldApart4ApiByVo{}",JsonUtils.writeValue(vo));
		Page<HoldApart> result = null;
	        try {

	            result = holdApartService.selectHoldApart4ApiByVo(vo);

	        } catch (BizException e) {
	            logger.error("selectHoldApart4ApiByVo error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("selectHoldApart4ApiByVo error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}

	@Override
	public Message<List<HoldApart>> selectHoldApartByVo(SearchHoldApartVo vo) {
		logger.info("selectHoldApart4ApiByVo{}",JsonUtils.writeValue(vo));
		List<HoldApart> result = null;
	        try {

	            result = holdApartService.selectHoldApartByVo(vo);

	        } catch (BizException e) {
	            logger.error("selectHoldApart4ApiByVo error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("selectHoldApart4ApiByVo error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}

	@Override
	public Message<HoldDetailResponse> queryBalanceByLevel(HoldDetailRequest request) {
		logger.info("queryBalanceByLevel{}",JsonUtils.writeValue(request));
		HoldDetailResponse result = null;
	        try {

	            result = holdApartIncomeService.queryBalanceByLevel(request);

	        } catch (BizException e) {
	            logger.error("queryBalanceByLevel error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("queryBalanceByLevel error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}

	@Override
	public Message<ApartIncomeResponse> queryApartIncome(ApartIncomeRequest request) {
		logger.info("queryApartIncome{}",JsonUtils.writeValue(request));
		ApartIncomeResponse result = null;
	        try {

	            result = holdApartIncomeService.queryApartIncome(request);

	        } catch (BizException e) {
	            logger.error("queryApartIncome error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("queryApartIncome error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}

	@Override
	public Message<Boolean> updateHoldApartRewardInfo(String orderOid,String lockRewardId){
		logger.info("updateHoldApartRewardInfo:orderOid:{},locakRewardId:{}",orderOid,lockRewardId);
		Boolean result = null;
		try {
			result = holdApartService.updateHoldApartRewardInfo(orderOid,lockRewardId);
		} catch (BizException e) {
			logger.error("updateHoldApartRewardInfo error...", e);
			return Messages.failed(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.error("updateHoldApartRewardInfo error...", e);
			return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
		}
		return Messages.success(result);
	}
}
