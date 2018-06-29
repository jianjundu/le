package com.le.jr.am.profit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.le.jr.am.profit.dao.HoldApartTypeChangeLogMapper;
import com.le.jr.am.profit.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.guohuai.lecurrent.orderservice.OrderInfoRequest;
import com.le.jr.am.assetpool.interfaces.MoneyLxSerfeeInterfaces;
import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.order.domain.OrderLog;
import com.le.jr.am.order.interfaces.OrderInterface;
import com.le.jr.am.order.interfaces.OrderLogInterface;
import com.le.jr.am.product.common.util.PageUtil;
import com.le.jr.am.product.common.util.ProductDecimalFormat;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.domain.ProductIncomeReward;
import com.le.jr.am.product.domain.enums.ProductDateTypeEnum;
import com.le.jr.am.product.domain.enums.ProductStateEnum;
import com.le.jr.am.product.domain.enums.ProductSubTypeEnum;
import com.le.jr.am.product.domain.vo.SearchProductVo;
import com.le.jr.am.product.interfaces.ProductChannelOrderInterfaces;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.common.util.DecimalUtil;
import com.le.jr.am.profit.common.util.RocketMQMessageUtil;
import com.le.jr.am.profit.dao.HoldApartIncomeMapper;
import com.le.jr.am.profit.dao.HoldApartMapper;
import com.le.jr.am.profit.dao.UpdateLockRewardIdLogMapper;
import com.le.jr.am.profit.domain.Hold;
import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.HoldApartIncome;
import com.le.jr.am.profit.domain.UpdateLockRewardIdLog;
import com.le.jr.am.profit.domain.constant.SysConstant;
import com.le.jr.am.profit.domain.enums.HoldApartAccrualStatus;
import com.le.jr.am.profit.domain.enums.HoldApartHoldStatus;
import com.le.jr.am.profit.domain.enums.HoldApartOrderType;
import com.le.jr.am.profit.domain.enums.HoldApartRedeemStatus;
import com.le.jr.am.profit.domain.enums.YesOrNoEnum;
import com.le.jr.am.profit.domain.input.SearchHoldApartVo;
import com.le.jr.am.profit.domain.output.HoldDetail;
import com.le.jr.am.profit.domain.output.HoldDetailResponse;
import com.le.jr.am.profit.domain.output.RedeemAccountMQ;
import com.le.jr.am.profit.service.CallDubboService;
import com.le.jr.am.profit.service.HoldApartCloseDetailService;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.config.ProfitDiamondService;
import com.le.jr.am.profit.service.util.MessageUtils;
import com.le.jr.am.system.interfaces.calendar.TradeCalendarInterface;
import com.le.jr.am.task.domain.JobLock;
import com.le.jr.am.task.domain.enums.JobLockIdEnum;
import com.le.jr.am.task.domain.enums.JobLockStatusEnum;
import com.le.jr.am.task.interfaces.JobLockInterface;
import com.le.jr.platform.redis.RedisClient;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.data.Result;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.le.jr.trade.publictools.util.StringUtil;

/**
 * 持有人分仓信息 xxx
 *
 * @author lining6
 * @date 2016年11月2日 上午11:56:31
 */
@Service("holdApartService")
public class HoldApartServiceImpl implements HoldApartService {

    Logger logger = LoggerFactory.getLogger(HoldApartServiceImpl.class);


    @Value("${mq.amprofit.accountant_topic}")
    private String accountTopic;

    @Value("${mq.amprofit.accountant_redeem}")
    private String redeem;

    @Value("${mq.amprofit.accountant_process}")
    private String process;

    @Value("${lecurrent.batch.size:1000}")
    private int pageSize = 1000;

    @Resource
    private MoneyLxSerfeeInterfaces moneyLxSerfeeInterfaces;

    @Resource
    private JobLockInterface jobLockInterface;

    @Resource
    private ProductInterfaces productInterfaces;

    @Resource
    private ProductChannelOrderInterfaces productChannelOrderInterfaces;

    @Resource
    private HoldApartMapper holdApartMapper;

    @Resource
    private UpdateLockRewardIdLogMapper updateLockRewardIdLogMapper;

    @Resource
    private TradeCalendarInterface tradeCalendarInterface;

    @Resource
    private HoldApartCloseDetailService holdApartCloseDetailService;

    @Resource
    private OrderLogInterface orderLogInterface;

    @Resource
    private OrderInterface orderInterface;

    @Resource
    private ProfitDiamondService profitDiamondService;

    @Resource
    private CallDubboService callDubboService;

    @Resource
    private HoldApartIncomeMapper holdApartIncomeMapper;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private RedisClient redisClient;

    // MQ 生产者
    @Resource
    private DefaultMQProducer producer;
    // 编程式事务
    @Resource
    private PlatformTransactionManager transactionManager;

    @Resource
    private HoldApartTypeChangeLogMapper holdApartTypeChangeLogMapper;


    /**
     * 计息快照 每日凌晨执行 需要DTS执行
     */
    @Override
    public void snapshot() throws BizException {

        logger.info("snapshot,开始执行计息快照:startTime:{}", new Date().toString());

        JobLock jobLock = null;
        // 收益快照是否已经成功结束，根据批次查询是否已经执行过，涉及到的表为T_MONEY_JOB_LOCK，
        String batchCode = DateUtil.format(DateUtil.getSqlDate(), "yyyyMMdd");

        Message<JobLock> message = jobLockInterface.queryAndDealJobLock(batchCode, JobLockIdEnum.SNAPSHOT.value);
        jobLock = MessageUtils.getJobLock(logger, message, "snapshot");
        // 操作T_MONEY_PUBLISHER_HOLDAPART表，更新发行人持有份额
        JobLock jobRep = this.snapshotLocked();
        Message<Boolean> msgFlat = this.jobLockInterface.updateStatus4Lock(jobLock.getOid(), jobRep.getJobStatus());
        Boolean flag = msgFlat.getData();
        if (!flag) {
            logger.error("snapshot jobLockInterface save failed jobLock={} msg={}", JsonUtils.writeValue(jobLock),
                    JsonUtils.writeValue(message));
            throw new BizException("snapshot jobLockInterface queryAndDealJobLock failed");
        }

        logger.info("queryAndDealJobLock:batchCode{},状态变更为done， ", batchCode);
    }

    /**
     * 计息份额快照
     */
    public JobLock snapshotLocked() throws BizException {
        JobLock jobRep = new JobLock();
        jobRep.setJobStatus(JobLockStatusEnum.DONE.value);
        jobRep.setJobMessage("OK");
        Date curDateD = DateUtil.getBeforeDate();

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

        String curDate = sf.format(curDateD);
        try {
            // 查询所有计息中的项目

            SearchProductVo searchProductVo = new SearchProductVo();
            List<ProductStateEnum> productStateEnums = new ArrayList<ProductStateEnum>();
            productStateEnums.add(ProductStateEnum.RAISING);
            productStateEnums.add(ProductStateEnum.CLEARING);
            searchProductVo.setProductStateEnums(productStateEnums);
            Message<List<Product>> message = productInterfaces.selectProductByVo(searchProductVo);

            if (!(Messages.isSuccess(message) && message.getData() != null)) {
                logger.error("snapshotLocked productInterfaces selectProduct failed searchVo={} msg={}", JsonUtils.writeValue(searchProductVo),
                        JsonUtils.writeValue(message));
                throw new BizException("snapshotLocked productInterfaces selectProduct failed");
            }

            for (Product p : message.getData()) {
                // 需要业务讲解，操作为修改T_MONEY_PUBLISHER_HOLDAPART表的snapshotVolume字段，以及相关状态，更新发行人持有份额
                this.snaphostVolume(p.getOid(), curDate);
            }
        } catch (Exception e) {
            logger.error("snapshotLocked failed..", e);
            jobRep.setJobMessage(e.getMessage());
            jobRep.setJobStatus(JobLockStatusEnum.FAIL.value);
        }
        return jobRep;

    }

    /**
     * 按产品维度进行计息份额份额快照
     *
     * @param productOid 产品快照
     * @param incomeDate 日期
     */
    @Override
    public void snaphostVolume(String productOid, String incomeDate) throws BizException {
        logger.info("snaphostVolume:productOid{},incomeDate{}", productOid, incomeDate);

        int result = this.holdApartMapper.snapshotVolume(productOid, incomeDate);
        if (result == 0) {
            logger.error("snaphostVolume null productOid={} incomeDate={}", productOid, incomeDate);
        }
    }

    /**
     * 新建活期投资订单
     *
     * @param tradeOrder
     * @param hold
     * @return
     */
    @Override
    public int createInvestApart(InvestorTradeOrder tradeOrder, Hold hold) throws BizException {
        HoldApart holdApart = new HoldApart();
        holdApart.setOid(UUIDUtil.creatUUID());
        holdApart.setPublisherOid(tradeOrder.getPublisherOid()); // 所属发行人
        holdApart.setInvestorOid(tradeOrder.getInvestorOid()); // 所属投资人
        holdApart.setProductOid(tradeOrder.getProductOid()); // 所属产品
        holdApart.setHoldOid(hold.getOid()); // 所属持有人手册
        holdApart.setOrderOid(tradeOrder.getOid()); // 所属订单
        holdApart.setChannelOid(tradeOrder.getChannelOid()); // 所属渠道
        holdApart.setInvestVolume(tradeOrder.getOrderVolume());
        holdApart.setOrderVolume(tradeOrder.getOrderVolume());
        holdApart.setValue(tradeOrder.getOrderAmount());
        holdApart.setInvestConfirmDate(tradeOrder.getInvestConfirmDate()); // 申购确认日

        holdApart.setBeginAccuralDate(this.getBeginAccrualDate(tradeOrder));
        holdApart.setBeginRedeemDate(this.getBeginRedeemDate(tradeOrder));

        holdApart.setOrderType(HoldApartOrderType.INVEST.value); // 订单类型
        holdApart.setRedeemStatus(HoldApartRedeemStatus.NO.value);
        holdApart.setAccrualStatus(HoldApartAccrualStatus.NO.value);

        holdApart.setHoldStatus(HoldApartHoldStatus.TOCONFIRM.value); // 待确认状态
        holdApart.setLockRewardOid(tradeOrder.getLockRewardOid());
        //4月10日，增加理财金项目新增的字段
        holdApart.setOrderSubType(tradeOrder.getOrderSubType());
        holdApart.setMarketingHold(tradeOrder.getMarketingOrder());
        holdApart.setOrderTime(tradeOrder.getOrderTime());
        Date currentDate = new Date();
        holdApart.setUpdateTime(currentDate);
        holdApart.setCreateTime(currentDate);
        holdApart.setOrderTime(tradeOrder.getOrderTime());
        int i = this.holdApartMapper.insert(holdApart);

        if (i <= 0) {
            logger.error("invest create holdApart fail or no record orderOid:{}", tradeOrder.getOid());
            throw new BizException(Code.RESULTSETISNULL.getValue(), "创建分仓持有人信息失败");
        }

        return i;
    }

    @Override
    public void flatWare4Accept2RedeemByHoldNotInterest(HoldApart apart) {
    	
        apart.setRedeemLockVolume(apart.getInvestVolume());//锁定当前持有
        apart.setSnapshotVolume(BigDecimal.ZERO);//计息份额置为0
        apart.setUpdateTime(new Date());
        this.holdApartMapper.updateByPrimaryKeySelective(apart);
    }

    @Override
    public void flatWare4Accept2RedeemNotInterest(InvestorTradeOrder redeemOrder) {
        BigDecimal volume = redeemOrder.getOrderVolume();

        //查询可赎回分仓
        List<HoldApart> apartList = this.findApart(redeemOrder.getInvestorOid(), redeemOrder.getProduct().getOid());
        for (HoldApart hold : apartList) {
            //剩余可赎回金额
            BigDecimal remainRedeemVolume = hold.getInvestVolume().subtract(hold.getRedeemLockVolume());

            if (remainRedeemVolume.compareTo(volume) >= 0) {
                hold.setRedeemLockVolume(hold.getRedeemLockVolume().add(volume));
                //如果是单利，存在计息份额小于持有份额的情况，如果赎回锁定份额超过计息份额，则计息份额置为0
                BigDecimal remainSnapshotVolume = hold.getSnapshotVolume().subtract(volume);
                hold.setSnapshotVolume(remainSnapshotVolume.compareTo(BigDecimal.ZERO) > 0 ? remainSnapshotVolume : BigDecimal.ZERO);
                volume = BigDecimal.ZERO;
                break;
            }

            if (remainRedeemVolume.compareTo(volume) < 0) {
                //赎回单赎回金额-该分仓可赎回份额
                volume = volume.subtract(remainRedeemVolume);
                //锁定该持仓所有份额
                hold.setRedeemLockVolume(hold.getInvestVolume());
                //此时分仓持有份额已经全部被锁定，计息份额为0
                hold.setSnapshotVolume(BigDecimal.ZERO);
                continue;
            }
        }
        if (volume.compareTo(BigDecimal.ZERO) != 0) {
            logger.error("flatWare orderCode:{}", redeemOrder.getOrderCode());
            throw new BizException(20020, "赎回时分仓份额异常(CODE:20020)");
        }
        logger.info("apartList:{}", JsonUtils.writeValue(apartList));
        this.holdApartMapper.updateList(apartList);
    }


    @Override
    public void flatWare2RedeemByHold(HoldApart apart, InvestorTradeOrder redeemOrder) {
        apart.setInvestVolume(BigDecimal.ZERO);//当前持有置为0
        apart.setValue(BigDecimal.ZERO);//价值置为0
        apart.setRedeemLockVolume(BigDecimal.ZERO);//锁定当前持有
        apart.setHoldStatus(HoldApartHoldStatus.CLOSED.value);
        apart.setAccrualStatus(YesOrNoEnum.NO.value);
        apart.setRedeemStatus(YesOrNoEnum.NO.value);
        apart.setUpdateTime(new Date());
        this.holdApartMapper.updateByPrimaryKeySelective(apart);

        //获取分仓对应投资单
        InvestorTradeOrder order = callDubboService.callGetOrderByOid(apart.getOrderOid()).getData();

        //赎回结算记录
        this.holdApartCloseDetailService.createCloseDetails(apart, redeemOrder.getOrderVolume(), redeemOrder, order.getOrderCode());

        //插入同步平台日志
        this.createRedeemCloseLog(apart.getOrderOid(), redeemOrder, apart.getHoldStatus());

        
        //会计引擎mq(罚息放在赎回对应投资单vo里面，方便会计引擎处理)
        order.setPunishVolume(redeemOrder.getPunishVolume());
        this.sendMq4FlatWare(redeemOrder.getOrderVolume(), order);

    }

    /**
     * 平仓
     *
     * @param tradeOrder
     */
    @Override
    public void flatWare(InvestorTradeOrder redeemOrder) throws BizException {
        logger.info("flatWare:orderCode{}", redeemOrder.getOrderCode());

        BigDecimal volume = redeemOrder.getOrderVolume();
        Product product = redeemOrder.getProduct();
        // 根据投资者和产品Id查询分仓记录
        List<HoldApart> apartList = this.findApart(redeemOrder.getInvestorOid(), redeemOrder.getProduct().getOid());
        for (HoldApart hold : apartList) {
            BigDecimal changeVolume = BigDecimal.ZERO;
            if (hold.getInvestVolume().compareTo(volume) >= 0) {
                changeVolume = volume;
                volume = BigDecimal.ZERO;
                this.updateHoldApart4FlatWareGreater(hold, product, changeVolume);
            } else {
                volume = volume.subtract(hold.getInvestVolume());
                changeVolume = hold.getInvestVolume();
                this.updateHoldApart4FlatWareLess(hold);
            }

            //获取分仓对应投资单
            InvestorTradeOrder order = callDubboService.callGetOrderByOid(hold.getOrderOid()).getData();

            //赎回结算记录
            this.holdApartCloseDetailService.createCloseDetails(hold, changeVolume, redeemOrder, order.getOrderCode());

            //插入同步平台日志
            this.createRedeemCloseLog(hold.getOrderOid(), redeemOrder, hold.getHoldStatus());

            //会计引擎mq
            this.sendMq4FlatWare(changeVolume, order);

            if (volume.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }

        }
        if (volume.compareTo(BigDecimal.ZERO) != 0) {
            logger.error("flatWare orderCode:{}", redeemOrder.getOrderCode());
            throw new BizException(20020, "赎回时分仓份额异常(CODE:20020)");
        }
        logger.info("apartList:{}", JsonUtils.writeValue(apartList));
        this.holdApartMapper.updateList(apartList);

    }

    private void updateHoldApart4FlatWareGreater(HoldApart hold, Product product, BigDecimal volume) {
        hold.setInvestVolume(hold.getInvestVolume().subtract(volume));
        hold.setValue(hold.getInvestVolume());
        if (product.getRedeemNeedInterest() == (byte) 0) {
            hold.setRedeemLockVolume(hold.getRedeemLockVolume().subtract(volume));
        } else {
            //如果是单利，存在计息份额小于持有份额的情况，如果赎回锁定份额超过计息份额，则计息份额置为0
            BigDecimal remainSnapshotVolume = hold.getSnapshotVolume().subtract(volume);
            hold.setSnapshotVolume(remainSnapshotVolume.compareTo(BigDecimal.ZERO) > 0 ? remainSnapshotVolume : BigDecimal.ZERO);
        }
        //只要持有本金>0就没close
        if (hold.getInvestVolume().compareTo(BigDecimal.ZERO) == 0) {
            hold.setHoldStatus(HoldApartHoldStatus.CLOSED.value);
        } else {
            hold.setHoldStatus(HoldApartHoldStatus.PARTHOLDING.value);
        }
        hold.setUpdateTime(new Date());
        this.holdApartMapper.updateByPrimaryKeySelective(hold);
    }

    private void updateHoldApart4FlatWareLess(HoldApart hold) {
        hold.setInvestVolume(BigDecimal.ZERO);
        hold.setValue(BigDecimal.ZERO);
        hold.setRedeemLockVolume(BigDecimal.ZERO);
        hold.setSnapshotVolume(BigDecimal.ZERO);
        hold.setHoldStatus(HoldApartHoldStatus.CLOSED.value);
        hold.setUpdateTime(new Date());
        this.holdApartMapper.updateByPrimaryKeySelective(hold);
    }

    private void sendMq4FlatWare(BigDecimal volume, InvestorTradeOrder order) {
        RedeemAccountMQ mes = new RedeemAccountMQ();
        mes.setInvestVolume(volume);
        String productOid = order.getProductOid();
        // TODO 调用产品可本地缓存优化
        Message<Product> msgPro = this.productInterfaces.selectProductByOid(productOid);
        if (msgPro != null && Messages.isSuccess(msgPro)) {
            order.setProduct(msgPro.getData());
        }
        mes.setOrder(order);
        String message = JsonUtils.writeValue(mes);
        logger.info("payCallBack：订单：{}发送MQ信息:{}", message);
        Result result = RocketMQMessageUtil.saveMessage(producer, message, accountTopic, redeem);
    }

    // 记录订单日志
    private Boolean createRedeemCloseLog(String orderId, InvestorTradeOrder redeemOrder, String holdStaus) throws BizException {

        OrderLog orderLog = new OrderLog();
        orderLog.setOid(UUIDUtil.creatUUID());
        orderLog.setCreateTime(new Date());
        orderLog.setOrderStatus(holdStaus);
        orderLog.setOrderType(redeemOrder.getOrderType());
        orderLog.setTradeOrderOid(orderId);
        orderLog.setUpdateTime(new Date());
        orderLog.setReferredOrderAmount(DecimalUtil.changeRMB2FEN4BigDecimal(redeemOrder.getOrderAmount()));
        orderLog.setReferredOrderCode(redeemOrder.getOrderCode());

        return orderLogInterface.addLogAndNotify(orderLog).getData();

    }

    // 分仓废单
    @Override
    public Boolean abandon(String tradeOrderOid) throws BizException {

        logger.info("abandon:tradeOrderOid{}", tradeOrderOid);
        int i = this.holdApartMapper.abandon(tradeOrderOid);

        return i > 0 ? true : false;

    }

    // 解锁分仓可计息份额
    @Override
    public int unlockAccrual(String holdApardOid, BigDecimal investVolume) throws BizException {
        logger.info("unlockAccrual:holdApartOid{}", holdApardOid);
        return this.holdApartMapper.unlockAccrual(holdApardOid, investVolume);
    }

    // 解锁分仓赎回份额
    @Override
    public int unlockRedeem(String holdApardOid) throws BizException {
        logger.info("unlockRedeem:holdApartOid{}", holdApardOid);
        return this.holdApartMapper.unlockRedeem(holdApardOid);
    }

    public List<HoldApart> findInterestableApart(String hold, String incomeDate) throws BizException {
        return this.holdApartMapper.findInterestableApart(hold, incomeDate);
    }

    @Override
    public List<HoldApart> findByBeforeBeginRedeemDateInclusive(String date, String oid) throws BizException {
        return this.holdApartMapper.findByBeforerBeginRedeemDateInclusive(date, oid);
    }

    @Override
    public List<HoldApart> findByBeforeBeginAccuralDateInclusive(String date, String oid) throws BizException {
        logger.info("findByBeforeBeginAccuralDateInclusive:date:{}, oid:{}", date, oid);
        List<HoldApart> list = this.holdApartMapper.findByBeforeBeginAccuralDateInclusive(date, oid);
        return list;
    }

    public List<HoldApart> findApart(String investorOid, String productOid) throws BizException {
        logger.info("findApart:investorOid:{}, productOid:{}", investorOid, productOid);
        List<HoldApart> list = this.holdApartMapper.findApart(investorOid, productOid);
        return list;
    }

    @Override
    public HoldApart findHoldApartByOid(String holdApartOid) throws BizException {

        return this.holdApartMapper.selectByPrimaryKey(holdApartOid);
    }

    @Override
    public BigDecimal getCountByProduct4Practice(String productOid, String incomeDate) throws BizException {

        BigDecimal result = this.holdApartMapper.getCountByProduct4Practice(productOid, incomeDate);
        if (result == null) {
            logger.info("getCountByProduct4Practice:result:{}", result);
            return null;
        }
        logger.info("getCountByProduct4Practice:prductOid:{},incomeDate{},count:{}", productOid, incomeDate, result.toString());
        return result;

    }

    @Override
    public HoldApart findHoldApartByOrderId(String orderOid) throws BizException {

        return this.holdApartMapper.findByTradeOrder(orderOid);
    }


    @Override
    public HoldDetailResponse findHoldApartByOrderCode(String orderCode) throws BizException {
        logger.info("findHoldApartByOrderCode:orderCode:{}", orderCode);
        OrderInfoRequest orderRequest = new OrderInfoRequest();
        orderRequest.setOrderCode(orderCode);
        Message<InvestorTradeOrder> msgOrder = this.orderInterface.getOrderByCode(orderCode);
        if (msgOrder == null || !Messages.isSuccess(msgOrder) || msgOrder.getData() == null) {
            logger.info("findHoldApartByOrderCode:orderCode:{},查询订单失败", orderCode);
            throw new BizException("查询订单失败，没有订单号为" + orderCode + "的订单");
        }
        InvestorTradeOrder order = msgOrder.getData();
        HoldApart holdApart = this.holdApartMapper.findByTradeOrder(msgOrder.getData().getOid());
        if (holdApart == null) {
            logger.info("findHoldApartByOrderCode:orderCode:{},该订单没有查到分仓持仓信息", orderCode);
            return null;
        }

        HoldDetailResponse response = new HoldDetailResponse();
        List<HoldDetail> holdDetails = new ArrayList<HoldDetail>();
        HoldDetail detail = new HoldDetail();
        detail.setProductOid(order.getProductOid());
        detail.setTradeOrderOid(order.getOrderCode());
        HoldApartIncome holdApartIncome = this.holdApartIncomeMapper.queryFirstApartIncomeByOrderOid(order.getOid());
        if (holdApartIncome == null) {
        	detail.setRewardRuleOid(order.getLockRewardOid());
            logger.info("findHoldApartByOrderCode:orderCode:{},该订单没有查询到分仓收益信息，请查证", orderCode);
        } else {
            detail.setRewardRuleOid(holdApartIncome.getRewardRuleOid());
        }
        detail.setRedeemStatus(holdApart.getRedeemStatus());
        detail.setHoldStatus(holdApart.getHoldStatus());
        detail.setSnapshotVolume(ProductDecimalFormat.format2Cent(holdApart.getSnapshotVolume()));
        detail.setValue(ProductDecimalFormat.format2Cent(holdApart.getValue()));
        detail.setInvestVolume(ProductDecimalFormat.format2Cent(holdApart.getInvestVolume()));
        detail.setHoldVolume(detail.getRedeemStatus().equals("yes") ? detail.getInvestVolume()-ProductDecimalFormat.format2Cent(holdApart.getRedeemLockVolume()) : 0);
        detail.setHoldTotalIncome(ProductDecimalFormat.format2Cent(holdApart.getTotalIncome()));
        detail.setHoldYesterdayIncome(ProductDecimalFormat.format2Cent(holdApart.getYesterdayIncome()));
        detail.setUpdateTime(holdApart.getUpdateTime());
        detail.setConfirmDate(holdApart.getIncomeConfirmDate());
        detail.setVolumeConfirmTime(holdApart.getVolumeConfirmTime());
        detail.setBeginAccuralDate(holdApart.getBeginAccuralDate());
        detail.setBeginRedeemDate(holdApart.getBeginRedeemDate());
        holdDetails.add(detail);
        response.setHoldDetails(holdDetails);
        return response;
    }


    @Override
    public Boolean updateHoldApart4Interest(String apartOid, BigDecimal interestedVolume, BigDecimal interestedAmount,
                                            BigDecimal netUnitAmount, Date incomeDate, BigDecimal incomeAmount,
                                            BigDecimal rewardAmount, BigDecimal snapshotVolume,
                                            BigDecimal marketingAmount,Integer marketingHold,HoldApartTypeChangeLog changeLog) throws BizException {
        logger.info("updateHoldApart4Interest:apartOid {}", apartOid);
        // 事物控制开始，开始添加 增加事务
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            //增加营销类别变化日志
            if(changeLog!=null&&StringUtil.isNotBlank(changeLog.getOid())){
                holdApartTypeChangeLogMapper.insert(changeLog);
            }

            int i = holdApartMapper.updateHoldApart4Interest(apartOid, interestedVolume, interestedAmount, netUnitAmount,
                    DateUtil.format(incomeDate), incomeAmount, rewardAmount, snapshotVolume,marketingAmount,marketingHold);
            if (i < 1) {
                logger.error("updateHoldApart4Interest:apartOid {}分仓保存失败", apartOid);
                transactionManager.rollback(status);
                throw new BizException("计息失败");
            }
            transactionManager.commit(status);
        }catch (Exception e){
            logger.error("updateHoldApart4Interest:apartOid {},e:{}，分仓保存失败", apartOid,e.getMessage());
            transactionManager.rollback(status);
            throw new BizException("计息失败");
        }
        return true;
    }

    private Date getBeginRedeemDate(InvestorTradeOrder tradeOrder) throws BizException {

        Date beginRedeemDate = null;
        try {
            if (tradeOrder.getProduct().getLockPeriodDays() == 0 || tradeOrder.getProduct().getSubType() == ProductSubTypeEnum.FIXED_CURRENT.getValue()) {
                beginRedeemDate = tradeOrder.getInvestConfirmDate();
            } else {
                if (ProductDateTypeEnum.T.getValue().equals(tradeOrder.getProduct().getRredeemDateType())) {
                    beginRedeemDate = this.tradeCalendarInterface.nextTrade(tradeOrder.getInvestConfirmDate(),
                            tradeOrder.getProduct().getLockPeriodDays()).getData();
                } else {
                    beginRedeemDate = DateUtil.addSQLDays(tradeOrder.getInvestConfirmDate(), tradeOrder.getProduct().getLockPeriodDays());
                }
            }

            if (null != tradeOrder.getLockRewardOid() && tradeOrder.getProduct().getSubType() != ProductSubTypeEnum.FIXED_CURRENT.getValue()) {

                Message<ProductIncomeReward> message = productInterfaces.selectProductRewardByOid(tradeOrder.getLockRewardOid());
                if (Messages.isSuccess(message)) {
                    int endDate = message.getData().getEndDate();
                    Date lockRedeemDate = DateUtil.addSQLDays(tradeOrder.getInvestConfirmDate(), endDate);
                    if (lockRedeemDate.after(beginRedeemDate)) {
                        beginRedeemDate = lockRedeemDate;
                    }
                }

            }

        } catch (Exception e) {
            logger.error("getBeginRedeemDate 获取赎回日失败");
            throw e;
        }

        return beginRedeemDate;
    }

    private Date getBeginAccrualDate(InvestorTradeOrder tradeOrder) throws BizException {
        Date beginAccuralDate = null;
        try {

            if (ProductDateTypeEnum.T.getValue().equals(tradeOrder.getProduct().getInvestDateType())) {
                boolean isTrade = this.tradeCalendarInterface.isTrade(tradeOrder.getOrderTime()).getData();
                if (isTrade) {
                    beginAccuralDate = tradeCalendarInterface.nextTrade(new Date(tradeOrder.getOrderTime().getTime()),
                            tradeOrder.getProduct().getInterestsFirstDays()).getData();
                } else {
                    beginAccuralDate = tradeCalendarInterface.nextTrade(tradeOrder.getOrderTime(),
                            tradeOrder.getProduct().getInterestsFirstDays() + 1).getData();
                }
            } else {
                beginAccuralDate = DateUtil.addSQLDays(tradeOrder.getOrderTime(), tradeOrder.getProduct().getInterestsFirstDays());
            }
        } catch (Exception e) {
            logger.error("getBeginAccrualDate 获取起息日失败");
            throw e;
        }
        return beginAccuralDate;
    }

    @Override
    public Boolean loadHoldApartData2Cache() throws BizException {

        logger.info("loadHoldApartData2Cache...");

        String minuteBefore = profitDiamondService.getMessage("holdCacheMinutesBefore").toString();

        Date startDate = DateUtil.minuteBefor(new Date(), Integer.valueOf(minuteBefore));

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

        List<HoldApart> list = holdApartMapper.loadHoldApartData2Cache(sf.format(startDate));
        logger.info("loadHoldApartData2Cache:  list.size:{}", list.size());

        for (HoldApart vo : list) {

            String hkey = SysConstant.LECURRENT_REDIS_HOLDDETIAILS_HASH_PRIFIX + vo.getOrderOid();

            Map<String, String> redisMap = new HashMap<>();
            redisMap.put("holdStatus", vo.getHoldStatus() == null ? "0" : vo.getHoldStatus());
            redisMap.put("redeemStatus", vo.getRedeemStatus() == null ? "0" : vo.getRedeemStatus());
            redisMap.put("snapshotVolume", DecimalUtil.changeRMB2FEN(vo.getSnapshotVolume()));
            redisMap.put("value", DecimalUtil.changeRMB2FEN(vo.getValue()));
            redisMap.put("holdVolume", redisMap.get("redeemStatus").equals("yes") ? redisMap.get("value") : "0");//可赎回份额
            redisMap.put("holdTotalIncome", DecimalUtil.changeRMB2FEN(vo.getTotalIncome()));
            redisMap.put("holdYesterdayIncome", DecimalUtil.changeRMB2FEN(vo.getYesterdayIncome()));
            redisMap.put("redeemableIncome", DecimalUtil.changeRMB2FEN(vo.getRedeemableIncome()));
            redisMap.put("totalMarketingIncome",DecimalUtil.changeRMB2FEN(vo.getTotalMarketingIncome()));
            redisMap.put("yesterdayMarketingIncome",DecimalUtil.changeRMB2FEN(vo.getYesterdayMarketingIncome()));
            redisMap.put("updateTime", String.valueOf(vo.getUpdateTime().getTime()));
            redisMap.put("beginRedeemDate", String.valueOf(vo.getBeginRedeemDate().getTime()));
            redisMap.put("beginAccuralDate", String.valueOf(vo.getBeginAccuralDate().getTime()));

            this.redisClient.hmsetString(hkey, redisMap);

        }

        return true;

    }

    @Override
    public Boolean loadHoldApartData2CacheMulti() throws Exception {

        logger.info("loadHoldApartData2CacheMulti...");

        String minuteBefore = profitDiamondService.getMessage("holdCacheMinutesBefore").toString();

        Date startDate = DateUtil.minuteBefor(new Date(), Integer.valueOf(minuteBefore));

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

        final List<HoldApart> list = holdApartMapper.loadHoldApartData2Cache(sf.format(startDate));
        logger.info("loadHoldApartData2CacheMulti: list.size = {}", list.size());

        if (list == null || list.isEmpty()) {
            logger.info("loadHoldApartData2CacheMulti 无数据需处理");
            return true;
        }

        int pageNum = new BigDecimal(list.size()).divide(new BigDecimal(pageSize)).setScale(0, BigDecimal.ROUND_CEILING).intValue();

        final CountDownLatch countDownLatch = new CountDownLatch(pageNum);

        for (int i = 1; i <= pageNum; i++) {
            final int pageNo = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("loadHoldApartData2CacheMulti {} pagaNo:{}", Thread.currentThread().getName(), pageNo);
                        int start = (pageNo - 1) * pageSize;
                        int end = start + pageSize;
                        while (start <= end && start < list.size()) {
                            HoldApart vo = list.get(start++);
                            try {
                                // 写入缓存
                                String hkey = SysConstant.LECURRENT_REDIS_HOLDDETIAILS_HASH_PRIFIX + vo.getOrderOid();

                                Map<String, String> redisMap = new HashMap<>();
                                redisMap.put("holdStatus", vo.getHoldStatus() == null ? "0" : vo.getHoldStatus());
                                redisMap.put("redeemStatus", vo.getRedeemStatus() == null ? "0" : vo.getRedeemStatus());
                                redisMap.put("snapshotVolume", DecimalUtil.changeRMB2FEN(vo.getSnapshotVolume()));
                                redisMap.put("value", DecimalUtil.changeRMB2FEN(vo.getValue()));
                                redisMap.put("holdVolume", vo.getRedeemStatus().equals("yes") ?DecimalUtil.changeRMB2FEN(vo.getInvestVolume().subtract(vo.getRedeemLockVolume())):"0");//可赎回份额
                                redisMap.put("holdTotalIncome", DecimalUtil.changeRMB2FEN(vo.getTotalIncome()));
                                redisMap.put("holdYesterdayIncome", DecimalUtil.changeRMB2FEN(vo.getYesterdayIncome()));
                                redisMap.put("redeemableIncome", DecimalUtil.changeRMB2FEN(vo.getRedeemableIncome()));
                                redisMap.put("totalMarketingIncome",DecimalUtil.changeRMB2FEN(vo.getTotalMarketingIncome()));
                                redisMap.put("yesterdayMarketingIncome",DecimalUtil.changeRMB2FEN(vo.getYesterdayMarketingIncome()));
                                redisMap.put("updateTime", String.valueOf(vo.getUpdateTime().getTime()));
                                redisMap.put("beginRedeemDate", String.valueOf(vo.getBeginRedeemDate().getTime()));
                                redisMap.put("beginAccuralDate", String.valueOf(vo.getBeginAccuralDate().getTime()));

                                logger.info("redis hmsetString key:{}", hkey);
                                redisClient.hmsetString(hkey, redisMap);

                            } catch (Exception e) {
                                logger.error("loadHoldApartData2CacheMulti error.. vo=" + JsonUtils.writeValue(vo), e);
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        logger.error("loadHoldApartData2CacheMulti", e);
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
            threadPoolTaskExecutor.execute(thread);
        }

        if (countDownLatch.await(Integer.valueOf(minuteBefore)-1>0?Integer.valueOf(minuteBefore)-1:1, TimeUnit.MINUTES)) {
            logger.info("loadHoldApartData2CacheMulti done...");
        } else {
            logger.error("loadHoldApartData2CacheMulti timeOut...");
            throw new BizException("loadHoldApartData2CacheMulti timeOut...");
        }
        return true;

    }
	
	public Boolean compareApartHoldDataWithCache()  throws Exception{
		logger.info("compareApartHoldDataWithCache...");
		Date date = DateUtil.getDate(new Date());
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<HoldApart> list = holdApartMapper.loadHoldApartData2Cache(sf.format(date));
		if(list==null||list.size()==0){
			return Boolean.TRUE;
		}
		logger.info("compareApartHoldDataWithCache:共查到数据：{}",list.size());
		for (HoldApart vo : list) {
			String hkey = SysConstant.LECURRENT_REDIS_HOLDDETIAILS_HASH_PRIFIX + vo.getOrderOid();
			Map<String, String> redisMap = new HashMap<>();
			redisMap.put("holdVolume",DecimalUtil.changeRMB2FEN( vo.getInvestVolume()));
			redisMap.put("value", DecimalUtil.changeRMB2FEN(vo.getValue()));
			redisMap.put("holdTotalIncome", DecimalUtil.changeRMB2FEN(vo.getTotalIncome()));
			redisMap.put("holdYesterdayIncome",DecimalUtil.changeRMB2FEN(vo.getYesterdayIncome()));
			redisMap.put("redeemableIncome",DecimalUtil.changeRMB2FEN(vo.getRedeemableIncome()));
			redisMap.put("holdStatus", vo.getHoldStatus() == null ? "0" : vo.getHoldStatus());
			redisMap.put("redeemStatus", vo.getRedeemStatus() == null ? "0" : vo.getRedeemStatus());
			redisMap.put("updateTime", String.valueOf(vo.getUpdateTime().getTime()));
			redisMap.put("beginRedeemDate", String.valueOf(vo.getBeginRedeemDate().getTime()));
			redisMap.put("beginAccuralDate", String.valueOf(vo.getBeginAccuralDate().getTime()));
			this.redisClient.hmsetString(hkey, redisMap);
		}
		return true;
	}



    @Override
    public Boolean update4Confirm(String orderId, BigDecimal accruableHoldVolume,String redeemStatus, String accrualStatus) throws BizException {
        logger.info("update4Confirm:: orderId{},  redeemStatus{},accrualStatus{}", orderId, redeemStatus, accrualStatus);

        try {
            int i = this.holdApartMapper.update4Confirm(orderId, accruableHoldVolume,redeemStatus, accrualStatus);

            return i > 0 ? true : false;
        } catch (Exception e) {
            logger.error("update4Confirm:更新失败：orderId:{}", orderId);
            throw e;
        }
    }

    /**
     * 按条件查询list
     */
    @Override
    public Page<HoldApart> selectHoldApart4ApiByVo(SearchHoldApartVo vo) throws BizException {
        Map<String, Object> map = new HashMap<>();

        List<HoldApart> result = new ArrayList<HoldApart>();

        if (vo.getCurrentPageNo() <= 0) {
            vo.setCurrentPageNo(0);
        }
        if (vo.getPageSize() <= 0) {
            vo.setPageSize(10);
        }

        PageUtil.setPageParam(vo.getCurrentPageNo(), vo.getPageSize(), map);

        if (!StringUtil.isEmpty(vo.getInvestorOid())) {
            map.put("investorOid", vo.getInvestorOid());
        }
        if (!StringUtil.isEmpty(vo.getProductOid())) {
            map.put("productOid", vo.getProductOid());
        }
        if (!StringUtil.isEmpty(vo.getHoldOid())) {
            map.put("holdOid", vo.getHoldOid());
        }

        int count = this.holdApartMapper.selectCountHoldApartsApi(map);

        if (count > 0) {
            result = this.holdApartMapper.selectHoldAparts4Api(map);
        }

        Page<HoldApart> page = new Page<HoldApart>();
        page.setDataList(result);
        page.setTotalCount(count);

        return page;
    }

    @Override
    public List<HoldApart> selectHoldApartByVo(SearchHoldApartVo vo) throws BizException {
        Map<String, Object> map = new HashMap<>();

        List<HoldApart> result = new ArrayList<HoldApart>();

        if (!StringUtil.isEmpty(vo.getInvestorOid())) {
            map.put("investorOid", vo.getInvestorOid());
        }
        if (!StringUtil.isEmpty(vo.getProductOid())) {
            map.put("productOid", vo.getProductOid());
        }
        if (!StringUtil.isEmpty(vo.getHoldOid())) {
            map.put("holdOid", vo.getHoldOid());
        }

        result = this.holdApartMapper.selectHoldAparts(map);

        return result;
    }

    @Override
    public BigDecimal getTotalCountByProduct4Practice(String productOid, String incomeDate) throws BizException {
        logger.info("getTotalCountByProduct4Practice:productOid {}, incomeDate {} ", productOid, incomeDate);
        BigDecimal result = this.holdApartMapper.getTotalCountByProduct4Practice(productOid, incomeDate);
        return result;
    }

    @Override
    public Boolean updateHoldApartRewardInfo(String orderOid, String lockRewardId) throws BizException {
        logger.info("updateHoldApartRewardInfo:orderOid:{},locakRewardId:{}", orderOid, lockRewardId);
        HoldApart apartVo = this.holdApartMapper.selectByOrderOid(orderOid);
        UpdateLockRewardIdLog logVo = new UpdateLockRewardIdLog();
        logVo.setOid(UUIDUtil.creatUUID());
        logVo.setOrderOid(orderOid);
        logVo.setBeginAccuralOldDate(apartVo.getBeginAccuralDate());
        logVo.setBeginRedeemOldDate(apartVo.getBeginRedeemDate());
        logVo.setLockRewardOldId(apartVo.getLockRewardOid());
        logVo.setLockRewardNewId(lockRewardId);

        Date investConFirmDate = apartVo.getInvestConfirmDate();
        Date baseDate = new Date();
        //当确认日期大于等于当前日期,则起息日按照确认日计算，如果小于的话，则按当前日期算
        if (DateUtil.ge(investConFirmDate, new Date())) {
            baseDate = investConFirmDate;
        }
        apartVo.setBeginAccuralDate(baseDate);
        logVo.setBeginAccuralNewDate(baseDate);
        Message<ProductIncomeReward> message = productInterfaces.selectProductRewardByOid(lockRewardId);
        logger.info("updateHoldApartRewardInfo:ProductIncomeReward:{}", JsonUtils.writeValue(message));
        Date beginRedeemDate = null;
        if (Messages.isSuccess(message)) {
            int endDate = message.getData().getEndDate();
            beginRedeemDate = DateUtil.addSQLDays(baseDate, endDate);
        }
        apartVo.setBeginRedeemDate(beginRedeemDate);
        logVo.setBeginAccuralNewDate(beginRedeemDate);
        apartVo.setLockRewardOid(lockRewardId);
        apartVo.setUpdateTime(new Date());

        // 事物控制开始
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            this.holdApartMapper.updateByPrimaryKeySelective(apartVo);
            logger.info("updateHoldApartRewardInfo,修改分仓完成：apartVo：{}", JsonUtils.writeValue(apartVo));
            this.updateLockRewardIdLogMapper.insert(logVo);
            logger.info("updateHoldApartRewardInfo,插入log表：logVo：{}", JsonUtils.writeValue(logVo));
            // 提交事务
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("updateHoldApartRewardInfo.修改锁定期ID失败，e：{}", e);
            //回滚事务
            transactionManager.rollback(status);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}