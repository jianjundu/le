package com.le.jr.am.profit.domain.input;

import java.io.Serializable;

/**
 * 废弃订单请求.
 * 
 * @author xulizhong
 *
 */

public class HoldDetailRequest  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 乐信投资者编号
	 */
	String investorOid;
	/**
	 * 奖励阶段
	 */
	String rewardRuleOid;
	
	/**
	 * 产品OID(资管系统生成)
	 */
	String productOid;
	
	
	int offset;
	
	int limit;
	
	public String getInvestorOid() {
		return investorOid;
	}
	public void setInvestorOid(String investorOid) {
		this.investorOid = investorOid;
	}
	public String getRewardRuleOid() {
		return rewardRuleOid;
	}
	public void setRewardRuleOid(String rewardRuleOid) {
		this.rewardRuleOid = rewardRuleOid;
	}
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	
}
