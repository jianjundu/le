package com.le.jr.am.profit.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收益分配工具类
 * xxx
 *
 * @author lining6
 * @date 2016年12月7日 下午7:45:53
 *
 */
public interface InterestRateMethodService {
	
	

	/**
	 * 收益分配实际执行方法 加入队列
	 * @param productOid
	 * @param incomeAllocateOid
	 * @param fpAmount
	 * @param fpRate
	 * @param incomeDate
	 */
	 void interest( String productOid, String incomeAllocateOid,  BigDecimal fpAmount,  BigDecimal fpRate,  Date incomeDate) ;
	
	 /**
	  * 收益分配实际执行
	  * @param productOid
	  * @param incomeAllocateOid
	  * @param fpAmount
	  * @param fpRate
	  * @param incomeDate
	  */
	 void interestDo( String productOid, String incomeAllocateOid, 
			 BigDecimal fpAmount,  BigDecimal fpRate,  Date incomeDate) ;
	



	 public void interestThread(String productOid, String incomeAllocateOid,
										BigDecimal fpAmount, BigDecimal fpRate, Date incomeDate);
	
}
