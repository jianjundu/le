package com.le.jr.am.profit.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Result;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.util.StringUtil;
import com.tstd2.log4j.log.LogTransactionIdManager;

public class RocketMQMessageUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(RocketMQMessageUtil.class);
	
	private static final int RECYCLESENDMQCOUNT = 2;
	
	public static  SendResult sendMQChangeInfo(DefaultMQProducer producer,String message,String topic,String subTopic) throws BizException {
		
		
		try {
		
			//调用mq组件发送消息
			
			Message msg = new Message(topic,// topic
					subTopic,// tag
					message.getBytes()// body
					);
			SendResult sendResult = producer.send(msg);
			logger.info("msgId："+sendResult.getMsgId()+"topic:"+topic+"subTopic:"+subTopic+"message:"+message+"result:"+sendResult.getSendStatus());	
			
			// 记录发送MQ的日志
		
			
			return sendResult;
			
			
			
		} catch (MQClientException e) {
			logger.error("调用mq服务失败");
			throw new BizException(Code.CALLSERVICEEXCEPTION.getValue(), "调用mq服务失败", e);
		} catch (RemotingException e) {
			logger.error("调用mq服务失败");
			throw new BizException(Code.CALLSERVICEEXCEPTION.getValue(), "调用mq服务失败", e);
		} catch (MQBrokerException e) {
			logger.error("调用mq服务失败");
			throw new BizException(Code.CALLSERVICEEXCEPTION.getValue(), "调用mq服务失败", e);
		} catch (InterruptedException e) {
			logger.error("调用mq服务失败");
			throw new BizException(Code.CALLSERVICEEXCEPTION.getValue(), "调用mq服务失败", e);
		}
		
	}
	
	
	/**
	 * 说明：处理资金变动并发送异步消息给账户模块
	 * 
	 * @author lining6
	 * @param orderId
	 * @param result
	 * @param sendCount 发送次数
	 * @return 返回
	 * @throws BizException
	 */
	public static Result saveMessage(DefaultMQProducer producer ,String message, String topic, String tag) throws BizException {

		logger.info("saveCallBackMessage:: producer:" + producer + "message:" + message);

		LogTransactionIdManager.resetTransactionId();

		if (StringUtil.isBlank(message)) {
			logger.error("saveCallBackMessage:: 消息体为空");
			return Result.SUCCESS;
		}
		// 发送回调信息到消息中心 保存此消息，订单自动消费此消息，交易模块需要监听此消息，并封装发送给资产变动模块

		SendResult sendResult = null;

		for (int i = 0; i < RECYCLESENDMQCOUNT; i++) {
			sendResult = RocketMQMessageUtil.sendMQChangeInfo(producer, message, topic, tag);
			if (RocketMQMessageUtil.isSuccess(sendResult)) {
				logger.info("saveCallBackMessage:: 调用消息保存方法完成");
				return Result.SUCCESS;
			}

		}

		logger.error("saveCallBackMessage:: 调用消息保存方法失败");

		return Result.FAILED;
	}
	
	
	public static boolean isSuccess(SendResult sendResult){
		
		
		if(sendResult.getSendStatus() == SendStatus.SEND_OK){
			return true;
		}
		return false;
	}
	
	
	

}
