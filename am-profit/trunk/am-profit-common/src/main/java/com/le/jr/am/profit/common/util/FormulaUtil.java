package com.le.jr.am.profit.common.util;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author yinxiao
 *
 */
public class FormulaUtil {

	/**
	 * 计算乐定活罚息
	 * @param snapshotVolume 计息金额
	 * @param dratio 日利率
	 * @param cycleDays 乐定活一个周期天数
	 * @param beginAccuralDate 开始计息日期
	 * @return
	 */
	public static BigDecimal getPunishVolume4FixedCurrent(BigDecimal snapshotVolume,BigDecimal dratio,int cycleDays,Date beginAccuralDate){
		int holdDays=DateUtil.getDaysBetweenTwoDate(beginAccuralDate, new Date());
		if(holdDays==0){
			return BigDecimal.ZERO;
		}
		int day= holdDays%cycleDays;
		if(day==0){
			return BigDecimal.ZERO;
		}
		return DecimalUtil.setScaleDown(snapshotVolume.multiply(dratio).multiply(new BigDecimal(day)));
	}
	
	/**
	 * 乐定活是否一个周期结束
	 * @param cycleDays
	 * @param beginAccuralDate
	 * @return
	 */
	public static boolean isCycleEnd(int cycleDays,Date beginAccuralDate,Date incomeDate){
		int holdDays=DateUtil.getDaysBetweenTwoDate(beginAccuralDate, incomeDate)+1;
		if(holdDays<cycleDays){
			return false;
		}
		int day= holdDays%cycleDays;
		if(day==0){
			return true;
		}
		return false;
	}
	
	public static int holdDaysInCycle(int cycleDays,int holdDays){
		if(holdDays<cycleDays){
			return holdDays;
		}
		int day= holdDays%cycleDays;
		if(day==0){
			return cycleDays;
		}
		return day;
	}
	
}
