package com.le.jr.am.profit.interfaces.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.profit.domain.output.PracticeInRep;
import com.le.jr.am.profit.interfaces.PracticeInterface;
import com.le.jr.am.profit.service.RewardIncomePracticeService;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;


@Service("practiceInterface")
public class PracticeInterfaceImpl implements PracticeInterface {
	
	Logger logger = LoggerFactory.getLogger(PracticeInterfaceImpl.class);
	
	@Resource
	private RewardIncomePracticeService rewardIncomePracticeService;

	public Message<List<PracticeInRep>> findByProduct(String productOid, String tDate){
		
		
		 logger.info(" findByProduct,productOid{},tDate{}",productOid,tDate);
		 List<PracticeInRep> result = null;
	        try {

	            result = rewardIncomePracticeService.findByProduct(productOid, tDate);

	        } catch (BizException e) {
	            logger.error("findByProduct error...", e);
	            return Messages.failed(e.getCode(),e.getMessage());
	        } catch (Exception e) {
	            logger.error("findByProduct error...", e);
	            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
	        }
	        return Messages.success(result);
	        
		
		
		
	}
	public Message<List<PracticeInRep>> findByProductLr(String productOid, String tDate){
		
		
		logger.info(" findByProductLr,productOid{},tDate{}",productOid,tDate);
		List<PracticeInRep> result = null;
		try {
			
			result = rewardIncomePracticeService.findByProductLr(productOid, tDate);
			
		} catch (BizException e) {
			logger.error("findByProduct error...", e);
			return Messages.failed(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.error("findByProduct error...", e);
			return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
		}
		return Messages.success(result);
		
		
		
		
	}

}
