package com.le.jr.am.profit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.le.jr.am.product.domain.Product;

/**
 * 持有人手册分仓
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午4:05:43
 *
 */
public class HoldApart implements Serializable{
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oid;

    private String investorOid;

    private String publisherOid;
    
    //private Spv spv;

    private String productOid;
    
    private Product product;

    private String channelOid;

    private String holdOid;
    //private Hold hold;

    private String orderOid;
    

    /**
     * 当前持有份额
     */
    private BigDecimal investVolume  = BigDecimal.ZERO;

    private String redeemStatus;

    private String accrualStatus;

    private Date beginAccuralDate;

    private Date beginRedeemDate;

    private String orderType;

    private BigDecimal totalIncome  = BigDecimal.ZERO;

    private Date investConfirmDate;

    private BigDecimal totalBaseIncome = BigDecimal.ZERO;

    private BigDecimal totalRewardIncome = BigDecimal.ZERO;

    private BigDecimal turnoverVolume = BigDecimal.ZERO;

    private BigDecimal yesterdayIncome = BigDecimal.ZERO;

    private BigDecimal yesterdayBaseIncome = BigDecimal.ZERO;

    private BigDecimal yesterdayRewardIncome = BigDecimal.ZERO;

    private BigDecimal toConfirmIncome = BigDecimal.ZERO;

    /**
     * 计息份额
     */
    private BigDecimal snapshotVolume = BigDecimal.ZERO;

    private BigDecimal redeemableIncome = BigDecimal.ZERO;

    private BigDecimal expectIncome = BigDecimal.ZERO;

    /**
     * 当前价值=当前份额*单位
     */
    private BigDecimal value = BigDecimal.ZERO;

    private Date incomeConfirmDate;

    private String holdStatus;

    private BigDecimal closeLeftBaseVolume = BigDecimal.ZERO;

    private Date volumeConfirmTime;

    private Date updateTime;

    private Date createTime;
    
    //增加字段 所属锁定阶梯
    private String lockRewardOid;
    
    /**
     * 赎回锁定份额
     */
    private BigDecimal redeemLockVolume=BigDecimal.ZERO;
    
    /**
     * 投资单份额
     */
    private BigDecimal orderVolume=BigDecimal.ZERO;

    /**
     * 订单子类型，为了理财金新家的字段，暂时没用，冗余字段
     */
    private String orderSubType ;


    /**
     * 是否营销订单 0：非营销订单，1：营销订单
     */
    private Integer marketingHold;

    /**
     * 总营销收益
     */
    BigDecimal totalMarketingIncome = BigDecimal.ZERO ;
    /**
     * 昨日营销收益
     */
    BigDecimal yesterdayMarketingIncome = BigDecimal.ZERO ;



    /**
     * 订单的下单时间
     */
    private Date orderTime;

    public Integer getMarketingHold() {
        return marketingHold;
    }

    public void setMarketingHold(Integer marketingHold) {
        this.marketingHold = marketingHold;
    }

    public BigDecimal getTotalMarketingIncome() {
        return totalMarketingIncome;
    }

    public void setTotalMarketingIncome(BigDecimal totalMarketingIncome) {
        this.totalMarketingIncome = totalMarketingIncome;
    }

    public BigDecimal getYesterdayMarketingIncome() {
        return yesterdayMarketingIncome;
    }

    public void setYesterdayMarketingIncome(BigDecimal yesterdayMarketingIncome) {
        this.yesterdayMarketingIncome = yesterdayMarketingIncome;
    }

    public String getOrderSubType() {
        return orderSubType;
    }

    public void setOrderSubType(String orderSubType) {
        this.orderSubType = orderSubType;
    }

    public String getLockRewardOid() {
		return lockRewardOid;
	}

	public void setLockRewardOid(String lockRewardOid) {
		this.lockRewardOid = lockRewardOid;
	}

	

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	

	public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getInvestorOid() {
        return investorOid;
    }

    public void setInvestorOid(String investorOid) {
        this.investorOid = investorOid == null ? null : investorOid.trim();
    }

    public String getPublisherOid() {
        return publisherOid;
    }

    public void setPublisherOid(String publisherOid) {
        this.publisherOid = publisherOid == null ? null : publisherOid.trim();
    }

    public String getProductOid() {
        return productOid;
    }

    public void setProductOid(String productOid) {
        this.productOid = productOid == null ? null : productOid.trim();
    }

    public String getChannelOid() {
        return channelOid;
    }

    public void setChannelOid(String channelOid) {
        this.channelOid = channelOid == null ? null : channelOid.trim();
    }

    public String getHoldOid() {
        return holdOid;
    }

    public void setHoldOid(String holdOid) {
        this.holdOid = holdOid == null ? null : holdOid.trim();
    }

    public String getOrderOid() {
        return orderOid;
    }

    public void setOrderOid(String orderOid) {
        this.orderOid = orderOid == null ? null : orderOid.trim();
    }

    public BigDecimal getInvestVolume() {
        return investVolume;
    }

    public void setInvestVolume(BigDecimal investVolume) {
        this.investVolume = investVolume;
    }

    public String getRedeemStatus() {
        return redeemStatus;
    }

    public void setRedeemStatus(String redeemStatus) {
        this.redeemStatus = redeemStatus == null ? null : redeemStatus.trim();
    }

    public String getAccrualStatus() {
        return accrualStatus;
    }

    public void setAccrualStatus(String accrualStatus) {
        this.accrualStatus = accrualStatus == null ? null : accrualStatus.trim();
    }

    public Date getBeginAccuralDate() {
        return beginAccuralDate;
    }

    public void setBeginAccuralDate(Date beginAccuralDate) {
        this.beginAccuralDate = beginAccuralDate;
    }

    public Date getBeginRedeemDate() {
        return beginRedeemDate;
    }

    public void setBeginRedeemDate(Date beginRedeemDate) {
        this.beginRedeemDate = beginRedeemDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Date getInvestConfirmDate() {
        return investConfirmDate;
    }

    public void setInvestConfirmDate(Date investConfirmDate) {
        this.investConfirmDate = investConfirmDate;
    }

    public BigDecimal getTotalBaseIncome() {
        return totalBaseIncome;
    }

    public void setTotalBaseIncome(BigDecimal totalBaseIncome) {
        this.totalBaseIncome = totalBaseIncome;
    }

    public BigDecimal getTotalRewardIncome() {
        return totalRewardIncome;
    }

    public void setTotalRewardIncome(BigDecimal totalRewardIncome) {
        this.totalRewardIncome = totalRewardIncome;
    }

    public BigDecimal getTurnoverVolume() {
        return turnoverVolume;
    }

    public void setTurnoverVolume(BigDecimal turnoverVolume) {
        this.turnoverVolume = turnoverVolume;
    }

    public BigDecimal getYesterdayIncome() {
        return yesterdayIncome;
    }

    public void setYesterdayIncome(BigDecimal yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }

    public BigDecimal getYesterdayBaseIncome() {
        return yesterdayBaseIncome;
    }

    public void setYesterdayBaseIncome(BigDecimal yesterdayBaseIncome) {
        this.yesterdayBaseIncome = yesterdayBaseIncome;
    }

    public BigDecimal getYesterdayRewardIncome() {
        return yesterdayRewardIncome;
    }

    public void setYesterdayRewardIncome(BigDecimal yesterdayRewardIncome) {
        this.yesterdayRewardIncome = yesterdayRewardIncome;
    }

    public BigDecimal getToConfirmIncome() {
        return toConfirmIncome;
    }

    public void setToConfirmIncome(BigDecimal toConfirmIncome) {
        this.toConfirmIncome = toConfirmIncome;
    }

    public BigDecimal getSnapshotVolume() {
        return snapshotVolume;
    }

    public void setSnapshotVolume(BigDecimal snapshotVolume) {
        this.snapshotVolume = snapshotVolume;
    }

    public BigDecimal getRedeemableIncome() {
        return redeemableIncome;
    }

    public void setRedeemableIncome(BigDecimal redeemableIncome) {
        this.redeemableIncome = redeemableIncome;
    }

    public BigDecimal getExpectIncome() {
        return expectIncome;
    }

    public void setExpectIncome(BigDecimal expectIncome) {
        this.expectIncome = expectIncome;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getIncomeConfirmDate() {
        return incomeConfirmDate;
    }

    public void setIncomeConfirmDate(Date incomeConfirmDate) {
        this.incomeConfirmDate = incomeConfirmDate;
    }

    public String getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(String holdStatus) {
        this.holdStatus = holdStatus == null ? null : holdStatus.trim();
    }

    public BigDecimal getCloseLeftBaseVolume() {
        return closeLeftBaseVolume;
    }

    public void setCloseLeftBaseVolume(BigDecimal closeLeftBaseVolume) {
        this.closeLeftBaseVolume = closeLeftBaseVolume;
    }

    public Date getVolumeConfirmTime() {
        return volumeConfirmTime;
    }

    public void setVolumeConfirmTime(Date volumeConfirmTime) {
        this.volumeConfirmTime = volumeConfirmTime;
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

	public BigDecimal getRedeemLockVolume() {
		return redeemLockVolume;
	}

	public void setRedeemLockVolume(BigDecimal redeemLockVolume) {
		this.redeemLockVolume = redeemLockVolume;
	}

	public BigDecimal getOrderVolume() {
		return orderVolume;
	}

	public void setOrderVolume(BigDecimal orderVolume) {
		this.orderVolume = orderVolume;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
}