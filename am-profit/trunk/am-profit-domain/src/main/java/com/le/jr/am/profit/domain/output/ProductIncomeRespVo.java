package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品收益详情返回对象
 * 
 * @author yinxiao
 *
 */

public class ProductIncomeRespVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5442437969383663944L;

	/**
	 * 产品编号
	 */
	private String productOid;
	
	/**
	 * 收益派发基准日;
	 */
	private Date incomeBaseDate;
	
	/**
	 * 计息金额
	 */
	private BigDecimal snapshotVolume; 
	
	/**
	 * 计提收益
	 */
	private BigDecimal provisionInterestVolume;
	
	/**
	 * 结算收益
	 */
	private BigDecimal closeInterestVolume;
	
	/**
	 * 发放状态 @see com.le.jr.am.profit.domain.enums.InterestResultStausEnum
	 */
	private String status;

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public Date getIncomeBaseDate() {
		return incomeBaseDate;
	}

	public void setIncomeBaseDate(Date incomeBaseDate) {
		this.incomeBaseDate = incomeBaseDate;
	}

	public BigDecimal getSnapshotVolume() {
		return snapshotVolume;
	}

	public void setSnapshotVolume(BigDecimal snapshotVolume) {
		this.snapshotVolume = snapshotVolume;
	}

	public BigDecimal getProvisionInterestVolume() {
		return provisionInterestVolume;
	}

	public void setProvisionInterestVolume(BigDecimal provisionInterestVolume) {
		this.provisionInterestVolume = provisionInterestVolume;
	}

	public BigDecimal getCloseInterestVolume() {
		return closeInterestVolume;
	}

	public void setCloseInterestVolume(BigDecimal closeInterestVolume) {
		this.closeInterestVolume = closeInterestVolume;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
