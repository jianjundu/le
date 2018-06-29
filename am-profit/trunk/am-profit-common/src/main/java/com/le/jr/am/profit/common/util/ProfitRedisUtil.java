package com.le.jr.am.profit.common.util;

import org.slf4j.Logger;

public class ProfitRedisUtil {

	/**
	 * 根据 projectNameForRedis，projectCode，venderCode，info方法名 构建 Redis 的 Key
	 * 
	 * @param projectNameForRedis : trade_project
	 * @param projectCode
	 * @param info 方法名称简称
	 * @return
	 */
	/**
	 * 前缀
	 */
	public static final String PROJECT ="am_profit";
	//合仓数据
	public static final String TYPE_HOLD="h";
	//分仓数据
	public static final String TYPE_HOLDAPART="ha";
	//协议
	public static final String TYPE_AGREAMENT = "ag";
	//持仓明细
	public static final String TYPE_HOLDDETAIL = "hd";
	//阶梯持仓
	public static final String TYPE_HOLDLEVEL = "hl";
	
	//持仓明细分页用的zset
	public static final String TYPE_HOLDDETAIL_PAGE = "hdp";
	
	
	
	

	/**
	 * 后缀
	 */
	public static final String INFO = "infos";
	public static final String DESC = "desc";
	public static final String LIST = "list";
	public static final String PAGE = "page";
	public static final String COUNT ="count";
	/**
	 * 缓存超时时间
	 */
	public static final int info_ttl = 3600;
	public static final int list_ttl= 15;
	public static final int cache_ttl= 5;
	public static final int method_ttl =5;
	

	public static String createRedisKey(String project, String type, String key, String info) {
		StringBuilder sb = new StringBuilder();
		sb.append(project);
		sb.append("_");
		sb.append(type);
		sb.append("_");
		sb.append(info);
		sb.append("_");
		
		sb.append(key);
		
		return sb.toString();

	}

	public static void printSetKey(Logger logger, String key) {
		logger.info("set redis key : key={} ", new Object[]{key});
	}
	public static void printSetKey(Logger logger, String key,String value) {
		logger.info("set redis key : key={} value={} ", new Object[]{key,value});
	}
	public static void printInfo(Logger logger, String Key, long result) {
		if (result == 1) {
			logger.info("删除redis信息成功 key = {}", Key);
		} else {
			logger.warn("删除Key失败 key={} result={}", new Object[]{Key, result});
		}
	}
}
