package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.ConnectMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.DisconnectMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingReqMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PublishMessage;

/**
 * 인코더 공통 상위 모듈
 * 
 * @date : 2013. 10. 30. 오전 10:50:51
 */
public class MqttsEncoder extends OneToOneEncoder {
	private static Logger logger = LoggerFactory.getLogger(MqttsEncoder.class);

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {

		if (msg instanceof ConnectMessage) {
			ConnectEncoder connectEncoder = new ConnectEncoder();
			return connectEncoder.encode(ctx, channel, (ConnectMessage) msg);
		} else if (msg instanceof DisconnectMessage) {
			DisconnectEncoder disconnectEncoder = new DisconnectEncoder();
			return disconnectEncoder.encode(ctx, channel, (DisconnectMessage) msg);
		} else if (msg instanceof PingReqMessage) {
			PingReqEncoder pingReqEncoder = new PingReqEncoder();
			return pingReqEncoder.encode(ctx, channel, (PingReqMessage) msg);
		} else if (msg instanceof PublishMessage) {
			PublishEncoder publishEncoder = new PublishEncoder();
			return publishEncoder.encode(ctx, channel, (PublishMessage) msg);
		} else {
			logger.error("[정의되지 않은 메세지] :{}" , msg);
			return null;
		}
	}
}
