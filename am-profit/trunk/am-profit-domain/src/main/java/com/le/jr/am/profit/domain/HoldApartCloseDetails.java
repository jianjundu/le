package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 持有人平仓，对应分仓表数据
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:06:15
 *
 */
public class HoldApartCloseDetails implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String holdApartOid;

    private String orderOid;

    private String investOrderCode;

    private BigDecimal changeVolume  = BigDecimal.ZERO;

    private String changeDirection;

    private Date updateTime;

    private Date createTime;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getHoldApartOid() {
        return holdApartOid;
    }

    public void setHoldApartOid(String holdApartOid) {
        this.holdApartOid = holdApartOid == null ? null : holdApartOid.trim();
    }

    public String getOrderOid() {
        return orderOid;
    }

    public void setOrderOid(String orderOid) {
        this.orderOid = orderOid == null ? null : orderOid.trim();
    }

    public String getInvestOrderCode() {
        return investOrderCode;
    }

    public void setInvestOrderCode(String investOrderCode) {
        this.investOrderCode = investOrderCode == null ? null : investOrderCode.trim();
    }

    public BigDecimal getChangeVolume() {
        return changeVolume;
    }

    public void setChangeVolume(BigDecimal changeVolume) {
        this.changeVolume = changeVolume;
    }

    public String getChangeDirection() {
        return changeDirection;
    }

    public void setChangeDirection(String changeDirection) {
        this.changeDirection = changeDirection == null ? null : changeDirection.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}