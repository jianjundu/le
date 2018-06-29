package com.le.jr.am.profit.domain.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 投资协议
 * 
 * @author xulizhong
 *
 */

public class AgreementRequest extends ApiRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 乐信交易平台唯一订单号.
	 */
	String tradeOrderOid;
	
	/**
	 * 协议类型(可选)
	 */
	String agreementType;

	public String getTradeOrderOid() {
		return tradeOrderOid;
	}

	public void setTradeOrderOid(String tradeOrderOid) {
		this.tradeOrderOid = tradeOrderOid;
	}

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}
	
	
}
