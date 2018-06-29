package com.le.jr.am.profit.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.fastjson.JSONObject;
import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.order.common.util.ValidateUtil;
import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.product.common.util.ProductDecimalFormat;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.dao.HoldApartIncomeMapper;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.HoldApartIncome;
import com.le.jr.am.profit.domain.constant.SysConstant;
import com.le.jr.am.profit.domain.input.ApartIncomeRequest;
import com.le.jr.am.profit.domain.input.HoldDetailRequest;
import com.le.jr.am.profit.domain.input.ProductOrderIncomeReqVo;
import com.le.jr.am.profit.domain.output.ApartIncomeResponse;
import com.le.jr.am.profit.domain.output.CalcSumInterest;
import com.le.jr.am.profit.domain.output.HoldDetail;
import com.le.jr.am.profit.domain.output.HoldDetailResponse;
import com.le.jr.am.profit.domain.output.ProductOrderIncomeRespVo;
import com.le.jr.am.profit.service.CallDubboService;
import com.le.jr.am.profit.service.HoldApartIncomeService;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.IncomeAllocateService;
import com.le.jr.am.profit.service.config.ProfitDiamondService;
import com.le.jr.platform.redis.RedisClient;
import com.le.jr.platform.redis.support.JedisCallback;
import com.le.jr.platform.redis.support.RW;
import com.le.jr.platform.redis.support.ShardedJedisTemplate;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;
import com.le.jr.trade.publictools.util.StringUtil;

import redis.clients.jedis.Jedis;

@Service("holdApartIncomeService")
public class HoldApartIncomeServiceImpl implements HoldApartIncomeService {

	Logger logger = LoggerFactory.getLogger(HoldApartIncomeServiceImpl.class);

	@Resource
	private HoldApartIncomeMapper holdApartIncomeMapper;

	@Resource
	private IncomeAllocateService incomeAllocateService;

	@Resource
	private HoldApartService holdApartService;
	
	@Resource
	private ProfitDiamondService profitDiamondService;
	
	@Resource
	private CallDubboService callDubboService;
	
	/**
	 * 编程式事务
	 */
	@Resource
	private PlatformTransactionManager transactionManager;

	@Resource
	private RedisClient redisClient;
	
	@Resource
	private ShardedJedisTemplate redisTemplate;

	/**
	 * 按计息日，产品统计分仓合计所得总收益，计息总份额
	 * 
	 * @param productOid
	 * @param incomeDate
	 * @return
	 */

	@Override
	public List<CalcSumInterest> calcSumInterest(String productOid, String incomeDate) throws BizException {
		return this.holdApartIncomeMapper.calcSumInterest(productOid, incomeDate);
	}

	/**
	 * 根据产品和确认收益日获取分仓明细列表
	 * 
	 * @param productOid
	 * @param confirmDate
	 * @return
	 */
	@Override
	public List<HoldApartIncome> getByPoidAndConfirmDate(String productOid, String confirmDate) throws BizException {
		return this.holdApartIncomeMapper.getByPoidAndConfirmDate(productOid, confirmDate);
	}
	/**
	 * 批量保存分仓收益
	 */
	@Override
	public int saveBatch(List<HoldApartIncome> partIncomeList)throws BizException  {
		return this.holdApartIncomeMapper.saveList(partIncomeList);

	}

	@Override
	public HoldDetailResponse findApartIncomeByRewardOid(String investorOid, String productOid, String rewardRuleOid)throws BizException  {
		Date incomeDate = incomeAllocateService.getLatestIncomeDate(productOid);
		HoldDetailResponse response = new HoldDetailResponse();
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		
		List<HoldApartIncome> list = this.holdApartIncomeMapper.findApartIncomeByRewardOid(investorOid, productOid, rewardRuleOid,
				sf.format(incomeDate));
		List<HoldDetail> holdDetails = new ArrayList<HoldDetail>();
		for (HoldApartIncome entity : list) {
			HoldApart apart = holdApartService.findHoldApartByOid(entity.getHoldApartOid());
			InvestorTradeOrder order=callDubboService.callGetOrderByOid(apart.getOrderOid()).getData();
			HoldDetail detail = new HoldDetail();
			detail.setProductOid(entity.getProductOid());
			detail.setTradeOrderOid(order.getOrderCode());
			detail.setRewardRuleOid(entity.getRewardRuleOid());
			detail.setRedeemStatus(apart.getRedeemStatus());
			detail.setHoldStatus(apart.getHoldStatus());
			detail.setSnapshotVolume(ProductDecimalFormat.format2Cent(apart.getSnapshotVolume()));
			detail.setValue(ProductDecimalFormat.format2Cent(apart.getValue()));
			detail.setInvestVolume(ProductDecimalFormat.format2Cent(apart.getInvestVolume()));
			detail.setHoldVolume(apart.getRedeemStatus().equals("yes") ?detail.getInvestVolume()-ProductDecimalFormat.format2Cent(apart.getRedeemLockVolume()):0);
			detail.setHoldTotalIncome(ProductDecimalFormat.format2Cent(apart.getTotalIncome()));
			detail.setTotalMarketingIncome(ProductDecimalFormat.format2Cent(apart.getTotalMarketingIncome()));
			detail.setYesterdayMarketingIncome(ProductDecimalFormat.format2Cent(apart.getYesterdayMarketingIncome()));
			detail.setHoldYesterdayIncome(ProductDecimalFormat.format2Cent(apart.getYesterdayIncome()));
			detail.setUpdateTime(apart.getUpdateTime());
			detail.setConfirmDate(apart.getIncomeConfirmDate());
			detail.setVolumeConfirmTime(apart.getVolumeConfirmTime());
			detail.setBeginRedeemDate(apart.getBeginRedeemDate());
			detail.setBeginAccuralDate(apart.getBeginAccuralDate());
			holdDetails.add(detail);
		}

		response.setHoldDetails(holdDetails);
		return response;
	}

	/**
	 * 查询分仓收益主表信息
	 */
	@Override
	public HoldApartIncome queryApartIncome(String investorOid, String tradeOrderOid, String incomeDate) throws BizException {
		List<HoldApartIncome> list = this.holdApartIncomeMapper.queryApartIncome(investorOid, tradeOrderOid, incomeDate);
		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;

	}

	/**
	 * 查询分仓收益历史信息
	 */

	@Override
	public HoldApartIncome queryApartIncomeInHis(String investorOid, String tradeOrderOid, String incomeDate)throws BizException  {

		if(StringUtil.isNotBlank(incomeDate)){
			incomeDate = incomeDate.replaceAll("-", "");
		}
		
		//进行时间转换
	    String yesterday = DateUtil.format(DateUtil.getBeforeDate());
		String now = DateUtil.format(DateUtil.getCurrDate());
		
		if(incomeDate.equals(now) || yesterday.equals(incomeDate)){
			logger.error("findByInvestorOidAndConfirmDateInHis:当日和昨日不会创建备份数据，不存在查询记录");
			return null;
		}
		
		List<HoldApartIncome> list = this.holdApartIncomeMapper.queryApartIncomeInHis(investorOid, tradeOrderOid, incomeDate);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	

	/**
	 * 查询分仓信息
	 */
	@Override
	public ApartIncomeResponse queryApartIncome(ApartIncomeRequest req)throws BizException  {
		logger.info("queryApartIncome===investorOid:{},tradeOrderOid:{},incomeDate:{}", req.getInvestorOid(), req.getTradeOrderOid(),
				req.getIncomeDate());
		ApartIncomeResponse rep = new ApartIncomeResponse();
		rep.setInvestorOid(req.getInvestorOid());
		rep.setTradeOrderOid(req.getTradeOrderOid());
		rep.setIncomeDate(req.getIncomeDate());
		try {

			HoldApartIncome entity = this.queryApartIncome(req.getInvestorOid(), req.getTradeOrderOid(), req.getIncomeDate());
			if (null == entity) {
				entity = this.queryApartIncomeInHis(req.getInvestorOid(), req.getTradeOrderOid(), req.getIncomeDate());
			}
			if (null != entity) {
				rep.setHoldRewardIncome(ProductDecimalFormat.format2Cent(entity.getRewardAmount()));
				rep.setHoldBaseIncome(ProductDecimalFormat.format2Cent(entity.getIncomeAmount()));
				rep.setHoldIncome(rep.getHoldBaseIncome() + rep.getHoldRewardIncome());
				rep.setHoldAccureVolume(ProductDecimalFormat.format2Cent(entity.getAccureVolume()));
				rep.setHoldMarketingIncome(ProductDecimalFormat.format2Cent(entity.getMarketingAmount()));
			} else {
				rep.setErrorCode(-1);
				rep.setErrorMessage("no Data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			rep.setErrorCode(-1);
			rep.setErrorMessage(e.getMessage());
		}
		return rep;
	}

	/**
	 * 加载分仓收益到缓存
	 */
	@Override
	public Boolean loadApartIncome2Cache() throws BizException {
		
		
		
		String minuteBefore = profitDiamondService.getMessage("holdIncomeCacheMinutesBefore").toString();
		
		
		Date date = DateUtil.minuteBefor(new Date(),Integer.valueOf( minuteBefore));

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		logger.info("loadApartIncome2Cache:date=={}",sf.format(date));

		List<HoldApartIncome> list = holdApartIncomeMapper.queryApartIncomeByUpdateTime(sf.format(date));
		
		logger.info("loadApartIncome2Cache:list.size=={}",list.size());
		
		try{

			for (HoldApartIncome vo : list) {
	
				String hkey = SysConstant.LECURRENT_REDIS_HOLDDETIAILS_HASH_PRIFIX + vo.getOrderOid();
				
				logger.info("loadApartIncome2Cache:hkey:{}",hkey);
	
				Map<String, String> redisMap = new HashMap<>();
				
				redisMap.put("rewardRuleOid", vo.getRewardRuleOid()!=null?vo.getRewardRuleOid():"0");
				
				String confirmDate = String.valueOf(new Date().getTime());
				if(vo.getConfirmDate()!=null){
					confirmDate =String.valueOf(vo.getConfirmDate().getTime());
				}
				redisMap.put("confirmDate", confirmDate);
	
				try{
				this.redisClient.hmsetString(hkey, redisMap);
				
				logger.info("loadApartIncome2Cache:hkey1 succ");
				}catch(Exception e){
					logger.error("loadApartIncome2Cache:hmsetString exception :{}",e.getMessage());
					throw e ;
				}
				
				String hkey2 = SysConstant.LECURRENT_REDIS_HOLDDETIAILS_ZSET_PRIFIX + vo.getInvestorOid()
						+":"+vo.getProductOid()+":"+(vo.getRewardRuleOid()==null?"-1":vo.getRewardRuleOid());
				
				logger.info("loadApartIncome2Cache:hkey2:{}",hkey2);
				this.redisClient.zadd(hkey2, vo.getOrderOid(), vo.getCreateTime().getTime());
				logger.info("loadApartIncome2Cache:hkey2 succ");
			}
		}catch(Exception e){
			logger.error("loadApartIncome2Cache:exception :{}",e.getMessage());
			throw e ;
		}

		return true;
	}
	
	
	
	/**
	 * 根据投资者、产品、奖励规则查询分仓
	 */
	@Override
	public HoldDetailResponse queryBalanceByLevel(HoldDetailRequest request)throws BizException  {
		logger.info("query balance by level, investorOid={},productOid={},rewardRuleOid={}", request.getInvestorOid(),
				request.getProductOid(), request.getRewardRuleOid());
		HoldDetailResponse rep = new HoldDetailResponse();

		final String zkey = SysConstant.LECURRENT_REDIS_HOLDDETIAILS_ZSET_PRIFIX + request.getInvestorOid() + ":" + request.getProductOid() + ":"
				+ request.getRewardRuleOid();
		/*String cacheSwitch ="";
		if (profitDiamondService.getMessage("cacheSwitch") != null) {
			cacheSwitch = profitDiamondService.getMessage("cacheSwitch").toString();
		}
		
		if(!cacheSwitch.equals("yes")){
			rep = this.findApartIncomeByRewardOid(request.getInvestorOid(), request.getProductOid(), request.getRewardRuleOid());
			logger.info("queryBalanceByLevel:使用非缓存模式");
			return rep;
		}else{
			logger.info("queryBalanceByLevel:使用缓存模式");
		}*/
		
		logger.debug("query redis key={}", zkey);
		/*int limit = -1;
		if (request.getLimit() > 0) {
			limit = request.getLimit();
		}*/

		List<HoldDetail> list = new ArrayList<HoldDetail>();
		final String score = DateUtil.getBeforeDate().getTime() + "";
		Set<String> sets = redisTemplate.execute(zkey, new JedisCallback<Set<String>>() {
			@Override
			public Set<String> call(Jedis jedis) {
				Set<String> result = jedis.zrangeByScore(zkey, score, score);
				return result;
			}
		}, RW.READ);
		
		for (String bs : sets) {
			String oKey=SysConstant.LECURRENT_REDIS_HOLDDETIAILS_HASH_PRIFIX+bs;
			Map<String, String> map=redisClient.hgetAllString(oKey);
			JSONObject jObj=new JSONObject();
			for (String k : map.keySet()) {
				jObj.put(new String(k),new String(map.get(k)) );
			}
			list.add(JSONObject.toJavaObject(jObj, HoldDetail.class));
		}
		if (list.size() > 0) {
			rep.setHoldDetails(list);
			return rep;
		} else {
			rep = this.findApartIncomeByRewardOid(request.getInvestorOid(), request.getProductOid(), request.getRewardRuleOid());
		}

		logger.info("query result: {}", rep.getHoldDetails());

		return rep;
	}
	
	@Override
	public Boolean backupHoldApartIncomeData2His() {
		 /**
	     * 备份任务表信息到历史表，并删除原表相应数据
	     */
			
			
			try{
				List<Date> dateList =null;
				
				dateList = holdApartIncomeMapper.queryBackupDate();
				
				for(Date date:dateList){
					if(date !=null){
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
						
						
						String newTable =sf1.format(date);
						try{
							holdApartIncomeMapper.createTableHoldApartIncome(newTable);
						}catch(Exception e){
							logger.error("backupHoldApartIncomeData2His:创建表失败或者已经存在:{}",newTable);
						}
						
						int i =holdApartIncomeMapper.createAndBackupHoldApartIncome(newTable,sf.format(date));
							
						if(i>0){
							if(holdApartIncomeMapper.deleteHoldApartBackupData(sf.format(date))<1){
								logger.error("backupHoldApartIncomeData2His删除备份数据失败或者数据为空");
								throw new BizException("删除备份数据失败或者数据为空");
							}
						}
						
					}
				}
			}catch (Exception e) {

				// 回滚事务
				logger.error("backupHoldApartIncomeData2His failed", e);
				
				throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "备份分仓数据库数据异常", e);
			}

			
			return true;
	}

	@Override
	public Page<ProductOrderIncomeRespVo> getProductOrderIncomes(PageEntity<ProductOrderIncomeReqVo> pageEntity) {
		Page<ProductOrderIncomeRespVo> page = new Page<ProductOrderIncomeRespVo>();
		page.setCurrentPageNo(pageEntity.getPageNo());
		page.setPageSize(pageEntity.getSize());
		page.setTotalCount(0);
		List<ProductOrderIncomeRespVo> pVos = new ArrayList<ProductOrderIncomeRespVo>();
		page.setDataList(pVos);
		
		ProductOrderIncomeReqVo reqVo=pageEntity.getParams();
		String orderCode=reqVo.getOrderCode();
		if(ValidateUtil.isNotEmpty(orderCode)){
            reqVo.setOrderOid(callDubboService.callGetOrderByOrderCode(orderCode).getData().getOid());
		}
		int count = this.selectHoldApartIncomesCount(reqVo);
		if (count <= 0) {
			return page;
		}
        page.setTotalCount(count);
		List<HoldApartIncome> HoldApartIncomes=this.selectHoldApartIncomes(pageEntity);
		for(HoldApartIncome hi:HoldApartIncomes){
			pVos.add(this.createProductOrderIncomeRespVo(hi,reqVo));
		}
		return page;
	}

	@Override
	public int selectHoldApartIncomesCount(ProductOrderIncomeReqVo oVo) {
		
		return holdApartIncomeMapper.selectHoldApartIncomesCount(oVo);
	}

	@Override
	public List<HoldApartIncome> selectHoldApartIncomes(PageEntity<ProductOrderIncomeReqVo> pageEntity) {
		return  holdApartIncomeMapper.selectHoldApartIncomes(pageEntity);
		
	}
	
	private ProductOrderIncomeRespVo createProductOrderIncomeRespVo(HoldApartIncome holdApartIncome,ProductOrderIncomeReqVo reqVo){
		ProductOrderIncomeRespVo respVo=new ProductOrderIncomeRespVo();
		respVo.setHoldVolume(holdApartIncome.getHoldVolume());
		respVo.setSnapshotVolume(holdApartIncome.getAccureVolume());
		respVo.setCloseInterestVolume(holdApartIncome.getCloseInterestVolume());
		respVo.setProvisionInterestVolume(holdApartIncome.getProvisionInterestVolume());
		respVo.setProductOid(holdApartIncome.getProductOid());
        respVo.setOrderVolume(holdApartIncome.getOrderVolume());
		if(ValidateUtil.isNotEmpty(reqVo.getOrderCode())){
			respVo.setOrderCode(reqVo.getOrderCode());
		}else{
			respVo.setOrderCode(callDubboService.callGetOrderByOid(holdApartIncome.getOrderOid()).getData().getOrderCode());
		}
		return respVo;
	}

}
