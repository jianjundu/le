package com.le.jr.am.profit.domain.input;

import java.io.Serializable;
import java.util.Date;

/**
 * 乐自选统计出参
 * 
 * @author yinxiao
 *
 */

public class LeZiXuanReqVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5442437969383663944L;

	/**
	 * 产品编号
	 */
	private String productOid;
	
	/**
	 * 奖励阶梯编号
	 */
	private String rewardOid;
	
	/**
	 * 申购日期起
	 */
	private Date investDateBegin;
	
	/**
	 * 申购日期止
	 */
	private Date investDateEnd;
	
	/**
	 * 到期日期起
	 */
	private Date expireDateBegin;
	
	/**
	 * 到期日期止
	 */
	private Date expireDateEnd;
	
	/**
	 * 存续期天数起
	 */
	private Integer periodBegin;
	
	/**
	 * 存续期天数止
	 */
	private Integer periodEnd;

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public String getRewardOid() {
		return rewardOid;
	}

	public void setRewardOid(String rewardOid) {
		this.rewardOid = rewardOid;
	}

	public Date getInvestDateBegin() {
		return investDateBegin;
	}

	public void setInvestDateBegin(Date investDateBegin) {
		this.investDateBegin = investDateBegin;
	}

	public Date getInvestDateEnd() {
		return investDateEnd;
	}

	public void setInvestDateEnd(Date investDateEnd) {
		this.investDateEnd = investDateEnd;
	}

	public Date getExpireDateBegin() {
		return expireDateBegin;
	}

	public void setExpireDateBegin(Date expireDateBegin) {
		this.expireDateBegin = expireDateBegin;
	}

	public Date getExpireDateEnd() {
		return expireDateEnd;
	}

	public void setExpireDateEnd(Date expireDateEnd) {
		this.expireDateEnd = expireDateEnd;
	}

	public Integer getPeriodBegin() {
		return periodBegin;
	}

	public void setPeriodBegin(Integer periodBegin) {
		this.periodBegin = periodBegin;
	}

	public Integer getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(Integer periodEnd) {
		this.periodEnd = periodEnd;
	}

}
