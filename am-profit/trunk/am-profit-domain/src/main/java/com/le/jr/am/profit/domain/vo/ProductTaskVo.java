package com.le.jr.am.profit.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 乐自选乐定活定时任务vo
 * @author yinxiao
 *
 */
public class ProductTaskVo implements Serializable {

	private static final long serialVersionUID = 7146588404046850970L;

	/**
	 * 产品id
	 */
	private String productOid;
	
	/**
	 * 是否全量刷新
	 */
	private String isRefreshAll;
	/**
	 * 订单时间
	 */
	private Date date;
	

	public String getProductOid() {
		return productOid;
	}

	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIsRefreshAll() {
		return isRefreshAll;
	}

	public void setIsRefreshAll(String isRefreshAll) {
		this.isRefreshAll = isRefreshAll;
	}
	
}
