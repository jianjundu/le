package com.le.jr.am.profit.interfaces;

import com.le.jr.am.profit.domain.input.AgreementRequest;
import com.le.jr.am.profit.domain.output.AgreementResponse;
import com.le.jr.trade.publictools.data.Message;


/**
 * 持有人产品协议接口
 * xxx
 *
 * @author lining6
 * @date 2016年12月7日 下午3:30:32
 *
 */
public interface PublisherProductAgreementInterface {
	
	/**
	 * 查询持有人产品协议，
	 * 入参：tradeOrderOid  订单ID
	 *     agreementType：AgreementTypeEnum  investing、service
	 * @param request
	 * @return
	 */
	public Message<AgreementResponse> getAgreementInfo(AgreementRequest request) ;
	

}
