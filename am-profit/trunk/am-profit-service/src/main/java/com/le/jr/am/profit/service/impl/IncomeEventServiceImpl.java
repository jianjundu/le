package com.le.jr.am.profit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import com.le.jr.am.task.domain.enums.SerialTaskCodeEnum;
import com.le.jr.am.task.domain.po.SerialTaskPO;
import com.le.jr.am.task.domain.vo.SerialTaskVO;
import com.le.jr.am.task.interfaces.SerialTaskInterface;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.enums.ProductStateEnum;
import com.le.jr.am.product.domain.enums.ProductTypeEnum;
import com.le.jr.am.product.domain.vo.SearchProductVo;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.common.util.RocketMQMessageUtil;
import com.le.jr.am.profit.dao.IncomeAllocateMapper;
import com.le.jr.am.profit.dao.IncomeEventMapper;
import com.le.jr.am.profit.domain.IncomeAllocate;
import com.le.jr.am.profit.domain.IncomeEvent;
import com.le.jr.am.profit.domain.constant.SysConstant;
import com.le.jr.am.profit.domain.enums.IncomeEventStausEnum;
import com.le.jr.am.profit.domain.input.IncomeAllocateForm;
import com.le.jr.am.profit.domain.input.SearchEventVo;
import com.le.jr.am.profit.domain.output.NoticeAllotInterestResult;
import com.le.jr.am.profit.service.IncomeDistributionService;
import com.le.jr.am.profit.service.IncomeEventService;
import com.le.jr.am.profit.service.config.ProfitDiamondService;
import com.le.jr.platform.redis.RedisClient;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.le.jr.trade.publictools.util.StringUtil;

@Service("incomeEventService")
public class IncomeEventServiceImpl implements IncomeEventService {

	Logger logger = LoggerFactory.getLogger(IncomeEventServiceImpl.class);

	// MQ 生产者
	@Resource
	private DefaultMQProducer producer;

	@Value("${mq.financingnotice.topic}")
	private String noticeTopic;

	@Value("${mq.financingnotice.tag}")
	private String noticeTag;

	@Resource
	private ProductInterfaces productInterfaces;

	@Resource
	private IncomeEventMapper incomeEventMapper;

	@Resource
	private IncomeAllocateMapper incomeAllocateMapper;

	@Resource
	private IncomeDistributionService incomeDistributionService;
	
	@Resource
	private RedisClient redisClient;
	
	@Resource
	private ProfitDiamondService profitDiamondService;
	
	@Resource
	private  SerialTaskInterface serialTaskInterface;

	@Override
	public List<IncomeEvent> selectEvents(SearchEventVo vo) throws BizException {

		Map<String, Object> map = new HashMap<>();

		if (!StringUtil.isEmpty(vo.getProductOid())) {
			map.put("productOid", vo.getProductOid());
		}
		if (null != vo.getIncomeEventStausEnum()) {
			map.put("status", vo.getIncomeEventStausEnum().value);
		}
		if (null != vo.getNoEventStatus()) {
			map.put("noStatus", vo.getNoEventStatus().value);
		}

		if (!StringUtil.isEmpty(vo.getAssetPoolOid())) {
			map.put("assetPoolOid", vo.getAssetPoolOid());
		}
		return incomeEventMapper.searchIncomeEvents(map);
	}
	
	@Override
	public List<String> searchDistincProductEvents(SearchEventVo vo) throws BizException {

		Map<String, Object> map = new HashMap<>();

		if (!StringUtil.isEmpty(vo.getProductOid())) {
			map.put("productOid", vo.getProductOid());
		}
		if (null != vo.getIncomeEventStausEnum()) {
			map.put("status", vo.getIncomeEventStausEnum().value);
		}
		if (null != vo.getNoEventStatus()) {
			map.put("noStatus", vo.getNoEventStatus().value);
		}

		if (StringUtil.isNotBlank(vo.getAssetPoolOid())) {

			map.put("assetPoolOid", vo.getAssetPoolOid());
		}
		return incomeEventMapper.searchDistincProductEvents(map);
	}

	// 自动派息
	@Override
	public void interest() throws BizException {

		try {
			// 查询可派息产品
			SearchProductVo searchProductVo = new SearchProductVo();
			List<ProductStateEnum> list = new ArrayList<>();
			list.add(ProductStateEnum.RAISING);
			searchProductVo.setProductStateEnums(list);
			searchProductVo.setProductTypeEnum(ProductTypeEnum.PRODUCTTYPE_02);
			List<Product> lstProduct = productInterfaces.selectProductByVo(searchProductVo).getData();

			//diamond读取派息产品,如果配置为空，暂时不派息
			//Object productsObject=profitDiamondService.getMessage("interestProducts");
			Object notProductsObj = profitDiamondService.getMessage("notInterestProducts");
			
			List<String> notproductOids=new ArrayList<String>();
			if(notProductsObj!=null&& (!"".equals(notProductsObj))){
				String productsString=notProductsObj.toString();
				notproductOids=Arrays.asList(productsString.split(","));
			}
			
			
			
			for (int i = 0; i < lstProduct.size(); i++) {

				Product gamProduct = lstProduct.get(i);
				
				//如果不是diamond配置的产品，跳过
				if(null!=notproductOids && notproductOids.contains(gamProduct.getOid())){
					continue;
				}

				// 查询最后一次分配的记录
				IncomeEvent lastIncomeEvent = incomeEventMapper.getLastIncomeEventByAssetPoolId(gamProduct.getAssetPoolOid(),gamProduct.getOid());
				if (lastIncomeEvent == null || StringUtil.isBlank(lastIncomeEvent.getOid())) {
					continue;
					// 如果没有分配记录，则需要手动创建分配记录，手动审核或者不审核等待自动跑任务

				}
				String eventOid = lastIncomeEvent.getOid();
				IncomeAllocate gamAssetpoolIncomeAllocate = incomeAllocateMapper.selectByEventOid(eventOid);

				if (null == gamAssetpoolIncomeAllocate || gamAssetpoolIncomeAllocate.getOid() == null) {
					logger.error("interest::eventOid==={} is null in event", eventOid);
					continue;
				}

				Date baseDate = lastIncomeEvent.getBaseDate();
				Date yesterday = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);

				// 当昨天已经添加
				if (DateUtil.isSameDay(baseDate, yesterday)) {

					// 已经添加单没有审核,如果已经被审核过，则不再做操作
					if (IncomeEventStausEnum.CREATE.value.equals(lastIncomeEvent.getStatus())) {
						logger.info("interest:hasRecorde:已经分配利息待审核:productOid:" + gamProduct.getOid() + " assetpoolId:"
								+ gamProduct.getAssetPoolOid());

						this.incomeDistributionService.auditPassIncomeAdjust(gamAssetpoolIncomeAllocate.getOid(), lastIncomeEvent.getAuditor());

					} else {
						logger.info("interest:hasRecorde:已经分配利息并且已经审核:productOid:" + gamProduct.getOid() + " assetpoolId:"
								+ gamProduct.getAssetPoolOid());

					}

				}
				// 当昨天没有添加
				else {
					// 历史待审核
					if (IncomeEventStausEnum.CREATE.value.equals(lastIncomeEvent.getStatus())) {
						logger.info("interest:昨日无收益记录，存在历史分配记录直接通过审核并分配:productOid:" + gamProduct.getOid() + " assetpoolId:"
								+ gamProduct.getAssetPoolOid() + "  auditor:" + lastIncomeEvent.getAuditor());

						if (StringUtil.isNotBlank(gamAssetpoolIncomeAllocate.getOid())) {
							this.incomeDistributionService.auditPassIncomeAdjust(gamAssetpoolIncomeAllocate.getOid(),
									lastIncomeEvent.getAuditor());
						} else {
							continue;
						}

						// 历史收益分配完成 ，需要手动补上昨日的收益 TODO

						//

					} else {
						logger.info("interest:无历史待审核记录 需要创建利息分配审核记录(与历史审核后的记录最后一条相同)并分配收益:productOid:" + gamProduct.getOid() + " assetpoolId:"
								+ gamProduct.getAssetPoolOid());
						Boolean saveResult = this.saveIncomeAdjust(lastIncomeEvent, gamProduct, gamAssetpoolIncomeAllocate,
								lastIncomeEvent.getCreator());
						if (saveResult) {
							// 新增完成之后要重新查一下今天的数据
							IncomeEvent todayGamAssetpoolIncomeEvent = incomeEventMapper.getLastIncomeEventByAssetPoolId(gamProduct.getAssetPoolOid(),gamProduct.getOid());
							IncomeAllocate todayIncomeAllocate = incomeAllocateMapper.selectByEventOid(todayGamAssetpoolIncomeEvent.getOid());

							this.incomeDistributionService.auditPassIncomeAdjust(todayIncomeAllocate.getOid(), lastIncomeEvent.getAuditor());

						}
					}

				}

				logger.info("interest,productCode:{}结束调用分配", gamProduct.getCode());

			}
		} catch (Exception e) {
			logger.error("interest", e);
		}

	}

	@Override
	public Boolean notifyPlatformAllotResult() throws BizException {
		SearchProductVo searchProductVo = new SearchProductVo();
		List<ProductStateEnum> list = new ArrayList<>();
		list.add(ProductStateEnum.RAISING);
		searchProductVo.setProductStateEnums(list);
		searchProductVo.setProductTypeEnum(ProductTypeEnum.PRODUCTTYPE_02);

		List<Product> lstProduct = productInterfaces.selectProductByVo(searchProductVo).getData();
		
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String notifyKey = SysConstant.AM_PROFIT_NOTIFYALLOTINTEREST_SUCCESS+"_"+sf.format(new Date());
		String notifyFlag =redisClient.getString(notifyKey);
		if(StringUtil.isNotBlank(notifyFlag) && Boolean.valueOf(notifyFlag)){
			logger.info("notifyPlatformAllotResult notifyKey:{} has notified message",notifyKey);
			return true;
		}

		/**
		 * 查询当天的派息情况
		 */
		Message<List<SerialTaskPO>> msgSerialVo = this.serialTaskInterface.getListByKeyAndDate(SerialTaskCodeEnum.INTEREST.value,new Date());
		if(msgSerialVo==null|| !Messages.isSuccess(msgSerialVo)){
			logger.error("notifyPlatformAllotResult:查询task任务列表失败");
			throw new BizException("notifyPlatformAllotResult:查询task任务列表失败");
		}
		if(msgSerialVo.getData()==null||msgSerialVo.getData().size()==0){
			logger.error("notifyPlatformAllotResult:尚未派息完成");
			return true;
		}

		//必须派息完成1分钟后才能通知，因为1分钟后才能上传到缓存中
		SerialTaskPO lastOne = msgSerialVo.getData().get(0);
		if(lastOne==null||lastOne.getExecuteEndTime()==null){
			logger.error("notifyPlatformAllotResult最后一条为空或者最后一条执行完成时间为空");
			return true;
		}
		//当前时间减去执行结束时间，小于1分钟，则不执行
		if(new Date().getTime()-lastOne.getExecuteEndTime().getTime()<60000){
			logger.error("notifyPlatformAllotResult，最后一条与当前时间相差不足1分钟");
			return  true;
		}


		// 发送派息通知
		NoticeAllotInterestResult result=null;

		List<Product> lstProductEnd = new ArrayList<>();

		Date baseDate = null;

		for (Product product : lstProduct) {
			IncomeEvent lastGamAssetpoolIncomeEvent = incomeEventMapper.getLastIncomeEventByAssetPoolId(product.getAssetPoolOid(),product.getOid());
			if (lastGamAssetpoolIncomeEvent == null || null==lastGamAssetpoolIncomeEvent.getBaseDate()) {
				continue;
			}
			// 基准日+1=派息日
			if (lastGamAssetpoolIncomeEvent.getStatus().equals(IncomeEventStausEnum.ALLOCATED.value)) {

				String auditTime = sf.format(lastGamAssetpoolIncomeEvent.getAuditTime());

				if(!sf.format(new Date()).equals(auditTime)){
					logger.info("notifyPlatformAllotResult notifyKey:{} not today",notifyKey);
					continue;
				}
				lstProductEnd.add(product);
				baseDate = lastGamAssetpoolIncomeEvent.getBaseDate();
				logger.info("notifyPlatformAllotResult Oid为：{}的产品派息完成,基准日为：{}",  product.getOid(),lastGamAssetpoolIncomeEvent.getBaseDate());
			}
		}
		logger.info("notifyPlatformAllotResult:派息任务数量为：{}",msgSerialVo.getData().size());
		logger.info("notifyPlatformAllotResult:需要派息的产品数量为：{}",lstProductEnd.size());
		if(lstProductEnd.size()==msgSerialVo.getData().size()){
			// 发送派息通知
			result = new NoticeAllotInterestResult();
			result.setAllotDate(baseDate);
			result.setNotifyTime(new Date());
			result.setNotifyId(new Date().getTime() + "" + new Random().nextInt(100));
			logger.info("notifyPlatformAllotResult:包装完所需message主体{}",JsonUtils.writeValue(result));
		}
		if(result!=null){
			String message = JsonUtils.writeValue(result);
			logger.info("notifyPlatformAllotResult message:{}", message);
			SendResult sr =RocketMQMessageUtil.sendMQChangeInfo(producer, message, noticeTopic, noticeTag);
		    if(RocketMQMessageUtil.isSuccess(sr)){
				redisClient.set(notifyKey, "true");
		    }
		}
		return true;

	}

	/**
	 * 保存派息事件 杜建君
	 * 
	 * @param lastGamAssetpoolIncomeEvent
	 * @param gamProduct
	 * @param gamAssetpoolIncomeAllocate
	 * @param operator
	 * @return
	 */
	public Boolean saveIncomeAdjust(IncomeEvent lastGamAssetpoolIncomeEvent, Product gamProduct, IncomeAllocate gamAssetpoolIncomeAllocate,
			String operator) throws BizException {

		try {
			String assetpoolOid = gamProduct.getAssetPoolOid();
			String incomeDistrDate = DateUtil.formatDate(new Date(new Date().getTime() - 24 * 60 * 60 * 1000));
			String productTotalScale = String.valueOf(gamAssetpoolIncomeAllocate.getCapital());
			String productRewardBenefit = String.valueOf(gamAssetpoolIncomeAllocate.getRewardIncome());
			String productDistributionIncome = String.valueOf(gamAssetpoolIncomeAllocate.getAllocateIncome());
			String productAnnualYield = String.valueOf(gamAssetpoolIncomeAllocate.getRatio().multiply(new BigDecimal(100)));

			IncomeAllocateForm form = new IncomeAllocateForm();
			form.setAssetpoolOid(assetpoolOid);
			form.setProductOid(gamProduct.getOid());
			form.setIncomeDistrDate(incomeDistrDate);
			form.setProductAnnualYield(productAnnualYield);
			form.setProductDistributionIncome(productDistributionIncome);
			form.setProductTotalScale(productTotalScale);
			form.setProductRewardBenefit(productRewardBenefit);
			return incomeDistributionService.saveIncomeAdjust(form, operator);

		} catch (Exception e) {
			logger.error("saveIncomeAdjust :{}", e);
			return false;
		}

	}

	@Override
	public IncomeEvent getLastIncomeEventByAssetPoolId(String assetPoolOid,String productOid) throws BizException {
		return incomeEventMapper.getLastIncomeEventByAssetPoolId(assetPoolOid,productOid);
	}

	@Override
	public IncomeEvent getIncomeEventByOid(String oid) throws BizException {

		return incomeEventMapper.selectByPrimaryKey(oid);

	}

	@Override
	public Boolean insert(IncomeEvent event) throws BizException {

		int i = incomeEventMapper.insert(event);

		return i > 0 ? true : false;
	}

	@Override
	public Boolean updateIncomeEvent(IncomeEvent event) {
		
		
		int i = incomeEventMapper.updateByPrimaryKeySelective(event);

		return i > 0 ? true : false;
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.daysBetween(DateUtil.getBeforeDate(), new Date()));
	}

}
