package com.le.jr.am.profit.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.le.jr.trade.publictools.page.Page;

public class LeZiXuanPage<T> extends Page<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4320690230495214184L;
	
	/**
	 * 申购总金额
	 */
	private BigDecimal investTotalVolume = BigDecimal.ZERO;
	
	/**
	 * 到期应兑付本金合计
	 */
	private BigDecimal expireInvestTotalVolume = BigDecimal.ZERO;

	/**
	 * 到期应兑付利息合计
	 */
	private BigDecimal expireIncomeTotalVolume = BigDecimal.ZERO;

	/**
	 * 到期应兑付本息合计
	 */
	private BigDecimal expireTotalVolume = BigDecimal.ZERO;

	public BigDecimal getInvestTotalVolume() {
		return investTotalVolume;
	}

	public void setInvestTotalVolume(BigDecimal investTotalVolume) {
		this.investTotalVolume = investTotalVolume;
	}

	public BigDecimal getExpireInvestTotalVolume() {
		return expireInvestTotalVolume;
	}

	public void setExpireInvestTotalVolume(BigDecimal expireInvestTotalVolume) {
		this.expireInvestTotalVolume = expireInvestTotalVolume;
	}

	public BigDecimal getExpireIncomeTotalVolume() {
		return expireIncomeTotalVolume;
	}

	public void setExpireIncomeTotalVolume(BigDecimal expireIncomeTotalVolume) {
		this.expireIncomeTotalVolume = expireIncomeTotalVolume;
	}

	public BigDecimal getExpireTotalVolume() {
		return expireTotalVolume;
	}

	public void setExpireTotalVolume(BigDecimal expireTotalVolume) {
		this.expireTotalVolume = expireTotalVolume;
	}
	
}
