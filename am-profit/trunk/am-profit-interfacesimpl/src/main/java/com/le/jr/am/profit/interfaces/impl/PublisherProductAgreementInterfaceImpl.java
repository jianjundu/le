package com.le.jr.am.profit.interfaces.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.profit.domain.input.AgreementRequest;
import com.le.jr.am.profit.domain.output.AgreementResponse;
import com.le.jr.am.profit.interfaces.PublisherProductAgreementInterface;
import com.le.jr.am.profit.service.PublisherProductAgreementService;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;

/**
 * 持有人产品协议接口 
 * 
 * xxx
 *
 * @author lining6
 * @date 2016年12月7日 下午3:35:05
 *
 */
@Service("publisherProductAgreementInterface")
public class PublisherProductAgreementInterfaceImpl implements PublisherProductAgreementInterface{
	
	
	Logger logger = LoggerFactory.getLogger(PublisherProductAgreementInterfaceImpl.class);
	
	@Resource
	private PublisherProductAgreementService publisherProductAgreementService;

	/**
	 * 订单维度产品协议信息  
	 * 入参  订单Id和协议类型
	 */
	@Override
	public Message<AgreementResponse> getAgreementInfo(AgreementRequest request) {
		
		  logger.info("getAgreementInfo  request={}", JsonUtils.writeValue(request));
		  AgreementResponse result = null;
	        try {
	           
	            result = publisherProductAgreementService.getAgreementInfo(request);
	        } catch (BizException e) {
	            logger.error("getAgreementInfo failed.. ={}", e);
	            Messages.failed(e.getCode(), e.getMessage());
	        } catch (Exception e) {
	            logger.error("getAgreementInfo failed..  ={}", e);
	            Messages.failed(Code.FAIL.getValue(), e.getMessage());
	        }
	        return Messages.success(result);
	        
		
	}


	
	
}
