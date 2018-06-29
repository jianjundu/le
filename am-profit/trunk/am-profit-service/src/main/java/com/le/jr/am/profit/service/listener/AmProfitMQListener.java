package com.le.jr.am.profit.service.listener;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.order.domain.enums.OrderTypeEnum;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.trade.publictools.data.Result;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.JsonUtils;

public class AmProfitMQListener implements MessageListenerConcurrently {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmProfitMQListener.class);

	@Resource
	private HoldService holdService;
	
	@Value("${mq.amprofit.investOrRedeemTopic}")
	private String investOrRedeemTopic;
	
	@Value("${mq.amprofit.investOrRedeemTag}")
	private String investOrRedeemTag;

	@SuppressWarnings("unused")
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		/*for (MessageExt msg : msgs) {

			String msgStr = new String(msg.getBody());
			String tags = msg.getTags();
			String topic = msg.getTopic();
			String msgId = msg.getMsgId();
			LOGGER.info("消息主题：{}，消息tag：{}  接收到mq消息：{}  msgId :{}", topic, tags, msgStr, msgId);

			try {

				if (msgStr == null) {
					LOGGER.error("msg解析异常...{}", msgStr);
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}

				if (topic.equals(investOrRedeemTopic) && tags.equals(investOrRedeemTag)) {
					InvestorTradeOrder order = JsonUtils.readValue(msgStr, InvestorTradeOrder.class);

					if (order.getOrderType() != null) {
						if (order.getOrderType().equals(OrderTypeEnum.INVEST)) {
							 holdService.invest(order);
						}
						if (order.getOrderType().equals(OrderTypeEnum.NORMAL_REDEEM)) {
							 holdService.redeem(order);
						}

					}
				}

				Result result = Result.SUCCESS;

				if (!result.equals(Result.SUCCESS)) {
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}

			} catch (BizException e) {
				LOGGER.error("consumeMessage 处理回调消息失败", e);
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			}

		}*/
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
