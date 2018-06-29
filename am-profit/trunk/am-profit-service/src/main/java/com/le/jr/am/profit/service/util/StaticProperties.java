package com.le.jr.am.profit.service.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.le.jr.am.profit.common.util.DateUtil;
@Service("staticPropertiesProfit")
public class StaticProperties {


	/** 协议存放目录 */
	@Value("${lecurrent.splitby}")
	String splitby;
	
	public static int stSplitby;
	
	static boolean is15;
	static boolean is24;
	/**
	 * 创建文件目录
	 */
	@PostConstruct
	public void init(){
		StaticProperties.stSplitby = Integer.parseInt(splitby);
		if (stSplitby == 0)  {
			is24 = true;
			is15 = false;
		} else {
			is24 = false;
			is15 = true;
		}
	}
	
	public static boolean isIs15() {
		return is15;
	}
	public static boolean isIs24() {
		return is24;
	}
	
	
	/**
	 * 指定时间是否为T日
	 * 
	 * @return
	 */
	public static boolean isT(Timestamp time) {
		if (StaticProperties.stSplitby == 0) {
			return true;
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(time);
			return c.get(Calendar.HOUR_OF_DAY) < 15;
		}
	}
	
	/**
	 * 给指定时间增加T日
	 * 
	 * @param time
	 *            时间
	 * @param plusDays
	 *            增加天数
	 * @param isWorkday
	 *            是否为工作日
	 * @return
	 */
	public static Date plusTDate(Timestamp time, int plusDays, boolean isWorkday) {
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		if (!isT(time)) {
			plusDays++;
		}

		if (isWorkday) {
			return DateUtil.addSQLWorkDays(new Date(time.getTime()), plusDays);
		} else {
			return DateUtil.addSQLDays(new Date(time.getTime()), plusDays);
		}
	}
	
}
