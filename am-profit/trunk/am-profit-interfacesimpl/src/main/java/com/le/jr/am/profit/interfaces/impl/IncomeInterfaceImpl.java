package com.le.jr.am.profit.interfaces.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
import com.le.jr.am.profit.interfaces.IncomeInterface;
import com.le.jr.am.profit.service.HoldApartIncomeService;
import com.le.jr.am.profit.service.IncomeDistributionService;
import com.le.jr.am.profit.service.InvestorInterestResultService;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;
import com.le.jr.trade.publictools.util.JsonUtils;

/**
 * 持有人手册接口-合仓
 * xxx
 *
 * @author lining6
 * @date 2016年11月18日 下午1:11:34
 *
 */
@Service("incomeInterface")
public class IncomeInterfaceImpl implements IncomeInterface{
	
	
	Logger logger = LoggerFactory.getLogger(IncomeInterfaceImpl.class);
	
	
    @Resource
    private IncomeDistributionService incomeDistributionService;
    
    @Resource
    private InvestorInterestResultService investorInterestResultService;
    
    @Resource
    private HoldApartIncomeService holdApartIncomeService;
	


	@Override
	public Message<Page<IncomeDistributionResp>> getIncomeAdjustList(SearchAllocateVO vo) {
		logger.info(" getIncomeAdjustList{} ",JsonUtils.writeValue(vo));
		Page<IncomeDistributionResp> result = null;
	        try {

	            result = incomeDistributionService.getIncomeAdjustList(vo);

	        } catch (BizException e) {
	            logger.error("getIncomeAdjustList error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("getIncomeAdjustList error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<IncomeDistributionResp> getIncomeAdjust(String assetPoolOid) {
		logger.info(" getIncomeAdjust{} ",assetPoolOid);
		IncomeDistributionResp result = null;
	        try {

	            result = incomeDistributionService.getIncomeAdjust(assetPoolOid);

	        } catch (BizException e) {
	            logger.error("getIncomeAdjust error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("getIncomeAdjust error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}







	@Override
	public Message<IncomeAllocateProductResp> getTotalScaleRewardBenefit(String productOid, String incomeDate) {
		logger.info(" getTotalScaleRewardBenefit productOid{},incomeDate{}  ",productOid,  incomeDate);
		IncomeAllocateProductResp result = null;
	        try {

	            result = incomeDistributionService.getTotalScaleRewardBenefit(productOid, incomeDate);

	        } catch (BizException e) {
	            logger.error("getTotalScaleRewardBenefit error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("getTotalScaleRewardBenefit error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Boolean> saveIncomeAdjust(IncomeAllocateForm form, String operator) {
		logger.info(" saveIncomeAdjust form{}  ",JsonUtils.writeValue(form));
		Boolean result = null;
	        try {

	            result = incomeDistributionService.saveIncomeAdjust(form, operator);

	        } catch (BizException e) {
	            logger.error("saveIncomeAdjust error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("saveIncomeAdjust error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<IncomeAllocateAssetResp> getIncomeAllocateAsset(String assetPoolOid) {
		logger.info(" getIncomeAllocateAsset{} ",assetPoolOid);
		IncomeAllocateAssetResp result = null;
	        try {

	            result = incomeDistributionService.getIncomeAllocateAsset(assetPoolOid);

	        } catch (BizException e) {
	            logger.error("getIncomeAllocateAsset error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("getIncomeAllocateAsset error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<IncomeAllocateProductResp> getIncomeAllocateProduct(String assetPoolOid, String productOid) {
		logger.info(" getIncomeAllocateProduct{} ",assetPoolOid);
		IncomeAllocateProductResp result = null;
	        try {

	            result = incomeDistributionService.getIncomeAllocateProduct(assetPoolOid, productOid);

	        } catch (BizException e) {
	            logger.error("getIncomeAllocateProduct error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("getIncomeAllocateProduct error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Boolean> auditFailIncomeAdjust(String oid, String operator) {
		logger.info(" auditFailIncomeAdjust{} ",oid);
		Boolean result = null;
	        try {

	            result = incomeDistributionService.auditFailIncomeAdjust( oid,  operator);

	        } catch (BizException e) {
	            logger.error("auditFailIncomeAdjust error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("auditFailIncomeAdjust error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<IncomeAllocate> deleteIncomeAdjust(String oid, String operator) {
		logger.info(" deleteIncomeAdjust{} ",oid);
		IncomeAllocate result = null;
	        try {

	            result = incomeDistributionService.deleteIncomeAdjust( oid,  operator);

	        } catch (BizException e) {
	            logger.error("deleteIncomeAdjust error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("deleteIncomeAdjust error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Boolean> allocateIncomeAgain(String oid, String operator) {
		logger.info(" allocateIncomeAgain{} ",oid);
		Boolean result = null;
	        try {

	            result = incomeDistributionService.allocateIncomeAgain( oid,  operator);

	        } catch (BizException e) {
	            logger.error("allocateIncomeAgain error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("allocateIncomeAgain error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Boolean> auditPassIncomeAdjust(String oid, String operator) {
		logger.info(" auditPassIncomeAdjust{} ",oid);
		Boolean result = null;
	        try {

	            result = incomeDistributionService.auditPassIncomeAdjust( oid,  operator);

	        } catch (BizException e) {
	            logger.error("auditPassIncomeAdjust error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("auditPassIncomeAdjust error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	}



	@Override
	public Message<Page<ProductIncomeRespVo>> getProductIncomes(PageEntity<ProductIncomeReqVo> pageEntity) {
		return Messages.success(investorInterestResultService.getProductIncomes(pageEntity));
	}



	@Override
	public Message<Page<ProductOrderIncomeRespVo>> getProductOrderIncomes(
			PageEntity<ProductOrderIncomeReqVo> pageEntity) {
		return Messages.success(holdApartIncomeService.getProductOrderIncomes(pageEntity));
	}


}
