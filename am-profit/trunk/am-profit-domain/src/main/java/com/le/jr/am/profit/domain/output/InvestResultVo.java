package com.le.jr.am.profit.domain.output;

import java.io.Serializable;

/*
* Description:投资结果实体
* Author:huangqilin@le.com
* Time:2016/12/13 10:34
*/
public class InvestResultVo implements Serializable {

    private static final long serialVersionUID = -8184143312283529108L;

    private boolean result;
    private String fundCode;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }
}
