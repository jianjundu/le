package com.le.jr.am.profit.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.fastjson.JSONObject;
import com.le.jr.am.product.common.util.ProductDecimalFormat;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.common.util.DecimalUtil;
import com.le.jr.am.profit.dao.LevelIncomeMapper;
import com.le.jr.am.profit.domain.LevelIncome;
import com.le.jr.am.profit.domain.constant.SysConstant;
import com.le.jr.am.profit.domain.input.LevelHoldRequest;
import com.le.jr.am.profit.domain.output.LevelHold;
import com.le.jr.am.profit.domain.output.LevelHoldResponse;
import com.le.jr.am.profit.service.HoldLevelIncomeService;
import com.le.jr.am.profit.service.IncomeAllocateService;
import com.le.jr.am.profit.service.config.ProfitDiamondService;
import com.le.jr.platform.redis.RedisClient;
import com.le.jr.platform.redis.support.JedisCallback;
import com.le.jr.platform.redis.support.RW;
import com.le.jr.platform.redis.support.ShardedJedisTemplate;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.le.jr.trade.publictools.util.StringUtil;

import redis.clients.jedis.Jedis;

@Service("holdLevelIncomeService")
public class HoldLevelIncomeServiceImpl implements HoldLevelIncomeService {

	@Resource
	private LevelIncomeMapper levelIncomeMapper;

	Logger logger = LoggerFactory.getLogger(HoldLevelIncomeServiceImpl.class);

	@Resource
	private IncomeAllocateService incomeAllocateService;

	@Resource
	private ProfitDiamondService profitDiamondService;

	/**
	 * 编程式事务
	 */
	@Resource
	private PlatformTransactionManager transactionManager;

	@Resource
	private RedisClient redisClient;

	@Resource
	private ShardedJedisTemplate redisTemplate;

	@Override
	public void saveBatch(List<LevelIncome> values) {

		int i = this.levelIncomeMapper.saveList(values);

		logger.info("saveBatch:i=={}", i);

	}

	@Override
	public LevelHoldResponse queryBalanceGroupByLevel(String investorOid, String productOid, String confirmDate) {
		List<LevelHold> levelHolds = new ArrayList<LevelHold>();
		List<LevelIncome> list = null;

		if (null != confirmDate) {

			list = levelIncomeMapper.queryBalanceGroupByLevel(investorOid, productOid, confirmDate);
			if (list.size() == 0) {
				list = this.queryBalanceGroupByLevelInHist(investorOid, productOid, confirmDate);
			}

		} else {
			Date incomeDate = incomeAllocateService.getLatestIncomeDate(productOid);

			list = this.queryBalanceGroupByLevel(investorOid, productOid, incomeDate);
			Date beforeDate = DateUtil.getBeforeDate();
			// 最近一次收益分配日的日期不等于昨日时将分仓的昨日收益置为0
			if (!DateUtil.isEqualDay(incomeDate, beforeDate)) {
				for (LevelIncome entity : list) {
					LevelHold levelHold = new LevelHold();
					levelHold.setProductOid(entity.getProductOid());
					levelHold.setRewardRuleOid(entity.getRewardRuleOid());
					levelHold.setValue(ProductDecimalFormat.format2Cent(entity.getValue()));
					levelHold.setHoldVolume(ProductDecimalFormat.format2Cent(entity.getAccureVolume()));
					levelHold.setHoldYesterdayIncome(0);
					levelHold.setConfirmDate(beforeDate);
					levelHold.setUpdateTime(entity.getUpdateTime());
					levelHolds.add(levelHold);
				}
				LevelHoldResponse response = new LevelHoldResponse();
				response.setLevelHolds(levelHolds);

				return response;
			}
		}

		if (null == list || list.size() == 0) {
			LevelHoldResponse response = new LevelHoldResponse();
			response.setLevelHolds(levelHolds);
			return response;
		}
		for (LevelIncome entity : list) {
			LevelHold levelHold = new LevelHold();
			levelHold.setProductOid(entity.getProductOid());
			levelHold.setRewardRuleOid(entity.getRewardRuleOid());
			levelHold.setValue(ProductDecimalFormat.format2Cent(entity.getValue()));
			levelHold.setHoldVolume(ProductDecimalFormat.format2Cent(entity.getAccureVolume()));
			levelHold.setHoldYesterdayIncome(ProductDecimalFormat.format2Cent(entity.getIncomeAmount()));
			levelHold.setConfirmDate(entity.getConfirmDate());
			levelHold.setUpdateTime(entity.getUpdateTime());
			levelHolds.add(levelHold);
		}

		LevelHoldResponse response = new LevelHoldResponse();
		response.setLevelHolds(levelHolds);

		return response;
	}

	/**
	 * 从数据库查询 根据投资者Id 产品Id 查询日期
	 */
	@Override
	public List<LevelIncome> queryBalanceGroupByLevel(String investorOid, String productOid, Date incomeDate) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sf.format(incomeDate);

		List<LevelIncome> list = levelIncomeMapper.queryBalanceGroupByLevel(investorOid, productOid, str);
		return list;
	}

	/**
	 * 分表查询历史阶梯收益数据
	 */
	@Override
	public List<LevelIncome> queryBalanceGroupByLevelInHist(String investorOid, String productOid, String confirmDate) {

		if (StringUtil.isNotBlank(confirmDate)) {
			confirmDate = confirmDate.replaceAll("-", "");
		}

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String today = sf.format(new Date());
		String yesterday = sf.format(DateUtil.getBeforeDate());
		if (today.equals(confirmDate) || yesterday.equals(confirmDate)) {
			logger.error(
					"queryBalanceGroupByLevelInHist ：历史表不能查询今日或昨日的记录,investorOid:{}, productOid:{}, confirmDate:{}",
					investorOid, productOid, confirmDate);

			return null;
		}
		return levelIncomeMapper.queryBalanceGroupByLevelInHist(investorOid, productOid, confirmDate);

	}

	@Override
	public LevelHoldResponse queryBalanceGroupByLevel(LevelHoldRequest request) {
		logger.info("query user balance by level, investorOid={}, productOid={}, confirmDate={}",
				request.getInvestorOid(), request.getProductOid(), request.getConfirmDate());

		LevelHoldResponse rep = new LevelHoldResponse();

		final String hkey = SysConstant.LECURRENT_REDIS_HOLDLEVEL_PRIFIX + request.getInvestorOid() + ":"
				+ request.getProductOid();

		/*
		 * String cacheSwitch =""; if
		 * (profitDiamondService.getMessage("cacheSwitch") != null) {
		 * cacheSwitch =
		 * profitDiamondService.getMessage("cacheSwitch").toString(); }
		 * 
		 * if(!cacheSwitch.equals("yes")){ rep =
		 * this.queryBalanceGroupByLevel(request.getInvestorOid(),
		 * request.getProductOid(), request.getConfirmDate());
		 * logger.info("queryBalanceGroupByLevel:使用非缓存模式"); return rep; }else{
		 * logger.info("queryBalanceGroupByLevel:使用缓存模式"); }
		 */

		Set<String> resultSet = null;
		if (!StringUtil.isEmpty(request.getConfirmDate())) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				final String score = format.parse(request.getConfirmDate()).getTime() + "";

				resultSet = redisTemplate.execute(hkey, new JedisCallback<Set<String>>() {
					@Override
					public Set<String> call(Jedis jedis) {
						Set<String> result = jedis.zrangeByScore(hkey, score, score);
						return result;
					}
				}, RW.READ);
			} catch (ParseException e) {
				logger.error("queryBalanceGroupByLevel:hkey:{}", hkey);
			}

		} else {
			// 没有日期，取最新的
			resultSet = redisTemplate.execute(hkey, new JedisCallback<Set<String>>() {
				@Override
				public Set<String> call(Jedis jedis) {
					Set<String> result = jedis.zrevrange(hkey, 0, 0);
					return result;
				}
			}, RW.READ);
		}
		String result = "[]";
		if (resultSet != null && !resultSet.isEmpty()) {
			result = resultSet.toString();
		}
		logger.debug("query redis using key: {}, result={}", hkey, result);

		List<LevelHold> levelList = JSONObject.parseArray(result, LevelHold.class);
		if(levelList!=null && !levelList.isEmpty()){
			Map<String,LevelHold> map=new HashMap<String,LevelHold>();
			for(LevelHold level:levelList){
				if(map.get(level.getRewardRuleOid())==null){
					map.put(level.getRewardRuleOid(), level);
				}
			}
			levelList=new ArrayList<LevelHold>();
			Iterator<String> it = map.keySet().iterator();  
		       while (it.hasNext()) {  
		           String key = it.next().toString();  
		           levelList.add(map.get(key));  
		    }  
		}
		if (levelList != null && levelList.size() > 0 && StringUtil.isEmpty(request.getConfirmDate())) {
			Date incomeDate = incomeAllocateService.getLatestIncomeDate(request.getProductOid());
			Date beforeDate = DateUtil.getBeforeDate(); // 最近一次收益分配日的日期不等于昨日时将分仓的昨日收益置为0
			if (null == incomeDate || !DateUtil.isEqualDay(incomeDate, beforeDate)) {
				for (LevelHold lh : levelList) {
					lh.setHoldYesterdayIncome(0);
				}
			}
		}

		if (levelList.size() > 0) {
			rep.setLevelHolds(levelList);
			return rep;

		} else {
			rep = this.queryBalanceGroupByLevel(request.getInvestorOid(), request.getProductOid(),
					request.getConfirmDate());

			if (rep.getLevelHolds() != null && rep.getLevelHolds().size() > 0) {

				Date date = null;
				if (request.getConfirmDate() != null) {
					try {
						date = DateUtil.parse(request.getConfirmDate());
					} catch (ParseException e) {
						logger.error("queryBalanceGroupByLevel解析异常");
					}
				}
				long score = date != null ? date.getTime() : 0;
				for (LevelHold vo : rep.getLevelHolds()) {
					redisClient.zadd(hkey, JSONObject.toJSONString(vo), score);
				}

			}
		}

		return rep;

	}

	@Override
	public Boolean loadLevelIncomeData2Cache() {

		String minuteBefore = profitDiamondService.getMessage("holdIncomeCacheMinutesBefore").toString();

		Date date = DateUtil.minuteBefor(new Date(), Integer.valueOf(minuteBefore));

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

		List<LevelIncome> list = levelIncomeMapper.queryLevelIncomeCache(sf.format(date));

		for (LevelIncome vo : list) {

			final String hkey = SysConstant.LECURRENT_REDIS_HOLDLEVEL_PRIFIX + vo.getInvestorOid() + ":"
					+ vo.getProductOid();
			
			final long confirmDateTime = vo.getConfirmDate().getTime();

			// 删除重复的key 使用jedis回调方法
			redisTemplate.execute(hkey, new JedisCallback<Object>() {
				@Override
				public Object call(Jedis jedis) {
					jedis.zremrangeByScore(hkey, 0, confirmDateTime - 1000 * 3600 * 24);
					return null;
				}
			}, RW.WRITE);

			Map<String, String> redisMap = new HashMap<>();
			redisMap.put("rewardRuleOid", vo.getRewardRuleOid() != null ? vo.getRewardRuleOid() : "");
			redisMap.put("productOid", vo.getProductOid() != null ? vo.getProductOid() : "0");
			redisMap.put("value", DecimalUtil.changeRMB2FEN(vo.getValue()));
			redisMap.put("holdVolume", DecimalUtil.changeRMB2FEN(vo.getAccureVolume()));
			redisMap.put("holdYesterdayIncome", DecimalUtil.changeRMB2FEN(vo.getIncomeAmount()));
			redisMap.put("confirmDate", String.valueOf(vo.getCreateTime().getTime()));
			redisMap.put("updateTime", String.valueOf(vo.getUpdateTime().getTime()));

			logger.info("loadHoldData2Cache:hkey:{},productOid:{},redisMap:{}", hkey, vo.getProductOid(),
					JsonUtils.writeValue(redisMap));

			this.redisClient.zadd(hkey, JSONObject.toJSONString(redisMap), confirmDateTime);

		}

		return null;
	}

	public void main(String[] args) {

		this.redisClient.zrem("lecurrent:HL:208062829:01137674", "1480003677000");

	}

	@Override
	public Boolean backupLevelIncomeData2His() throws BizException {
		/**
		 * 备份任务表信息到历史表，并删除原表相应数据
		 */

		try {
			List<Date> dateList = null;

			dateList = levelIncomeMapper.queryBackupDate();

			for (Date date : dateList) {
				if (date != null) {
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");

					String newTable = sf1.format(date);

					try {
						levelIncomeMapper.createTableLevelIncome(newTable);
					} catch (Exception e) {
						logger.error("backupLevelIncomeData2His:创建表失败或者已经存在表：{}", newTable);
					}

					int i = levelIncomeMapper.createAndBackupLevelIncome(newTable, sf.format(date));

					if (i > 0) {
						if (levelIncomeMapper.deleteLevelIncomeBackupData(sf.format(date)) < 1) {
							logger.error("backupLevelIncomeData2His删除备份数据失败或者数据为空");
							throw new BizException("删除备份数据失败或者数据为空");
						}
					}

				}
			}

		} catch (Exception e) {

			// 回滚事务
			logger.error("backupLevelIncomeData2His failed", e);
			throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "备份阶梯收益数据操作异常", e);
		}

		// 提交事务
		return true;
	}

}
