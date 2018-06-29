package com.le.jr.am.profit.domain.output;

import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.profit.domain.HoldApart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dujianjun on 2016/12/14.
 */
public class ProcessAccountMQ implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;




    HoldApart apart;

    BigDecimal bookAmount = BigDecimal.ZERO;
    BigDecimal apartInerestAmount = BigDecimal.ZERO;
    BigDecimal incomeAmount = BigDecimal.ZERO;
    BigDecimal rewardAmount = BigDecimal.ZERO;

    /**
     * 基础收益率
     */
    BigDecimal baseRatio = BigDecimal.ZERO;

    ProductIncomeReward reward;
    Date incomeDate;



/************************************************************下面为理财金专用start ********************************************************/

    /**
     * 营销编码
     */
    private Long marketingId;

    /**
     * 是否加息
     */
    private Boolean addInterest;

    /**
     * 奖励收益率
     */
    private BigDecimal awardRate;

    /**
     * 奖励本金(元)
     */
    private BigDecimal awardAmount;

    /**
     * 奖励利息(元)
     */
    private BigDecimal awardInterest;



    /************************************************************下面为理财金专用end ********************************************************/


    public BigDecimal getBaseRatio() {
        return baseRatio;
    }

    public void setBaseRatio(BigDecimal baseRatio) {
        this.baseRatio = baseRatio;
    }


    public Long getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(Long marketingId) {
        this.marketingId = marketingId;
    }

    public Boolean getAddInterest() {
        return addInterest;
    }

    public void setAddInterest(Boolean addInterest) {
        this.addInterest = addInterest;
    }

    public BigDecimal getAwardRate() {
        return awardRate;
    }

    public void setAwardRate(BigDecimal awardRate) {
        this.awardRate = awardRate;
    }

    public BigDecimal getAwardAmount() {
        return awardAmount;
    }

    public void setAwardAmount(BigDecimal awardAmount) {
        this.awardAmount = awardAmount;
    }

    public BigDecimal getAwardInterest() {
        return awardInterest;
    }

    public void setAwardInterest(BigDecimal awardInterest) {
        this.awardInterest = awardInterest;
    }

    public HoldApart getApart() {
        return apart;
    }

    public void setApart(HoldApart apart) {
        this.apart = apart;
    }

    public BigDecimal getBookAmount() {
        return bookAmount;
    }

    public void setBookAmount(BigDecimal bookAmount) {
        this.bookAmount = bookAmount;
    }

    public BigDecimal getApartInerestAmount() {
        return apartInerestAmount;
    }

    public void setApartInerestAmount(BigDecimal apartInerestAmount) {
        this.apartInerestAmount = apartInerestAmount;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public ProductIncomeReward getReward() {
        return reward;
    }

    public void setReward(ProductIncomeReward reward) {
        this.reward = reward;
    }

    public Date getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(Date incomeDate) {
        this.incomeDate = incomeDate;
    }
}
