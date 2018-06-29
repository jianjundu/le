package com.le.jr.am.profit.domain.input;




/**
 * 废弃订单请求.
 * 
 * @author xulizhong
 *
 */

public class LevelHoldRequest extends ApiRequest{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 乐信投资者编号
	 */
	String investorOid;
	
	/**
	 * 产品OID(资管系统生成)
	 */
	String productOid;
	
	/**
	 * 收益确认日期（可选）
	 */
	String confirmDate;

	public String getInvestorOid() {
		return investorOid;
	}

	public void setInvestorOid(String investorOid) {
		this.investorOid = investorOid;
	}

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public String getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	
	
	
}
