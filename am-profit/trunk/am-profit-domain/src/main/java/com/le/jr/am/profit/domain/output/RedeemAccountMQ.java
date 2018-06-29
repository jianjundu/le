package com.le.jr.am.profit.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;

import com.le.jr.am.order.domain.InvestorTradeOrder;

/**
 * Created by dujianjun on 2016/12/14.
 */
public class RedeemAccountMQ implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    InvestorTradeOrder order;

    BigDecimal investVolume;


    public InvestorTradeOrder getOrder() {
        return order;
    }

    public void setOrder(InvestorTradeOrder order) {
        this.order = order;
    }

    public BigDecimal getInvestVolume() {
        return investVolume;
    }

    public void setInvestVolume(BigDecimal investVolume) {
        this.investVolume = investVolume;
    }
}
