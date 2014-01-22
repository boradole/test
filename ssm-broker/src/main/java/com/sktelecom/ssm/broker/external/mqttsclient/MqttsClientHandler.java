package com.sktelecom.ssm.broker.external.mqttsclient;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sktelecom.ssm.broker.config.MqttsMessageManagement;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.AbstractMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.ConnAckMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.DisconnectMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingReqMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingRespMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PubAckMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PublishMessage;

public class MqttsClientHandler extends SimpleChannelHandler {

	private static Logger logger = LoggerFactory.getLogger(MqttsClientHandler.class);
	private static Logger errorLog = LoggerFactory.getLogger("ERROR_TRACE");

	final ClientBootstrap bootstrap;
	private final String clientId; // pingReq보낼 때 clientID 값

	public MqttsClientHandler(ClientBootstrap bootstrap, String clientId) {
		this.bootstrap = bootstrap;
		this.clientId = clientId;
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelOpen(ctx, e);
		logger.info("[생성] : {} ", getRemoteAddress());
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
		super.channelConnected(ctx, e);
		logger.info("[연결] : {}", getRemoteAddress());
	}

	private InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) bootstrap.getOption("remoteAddress");
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		e.getChannel().setAttachment(e.getMessage());
		super.writeRequested(ctx, e);
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		if (e.getChannel().getAttachment() instanceof DisconnectMessage) {
			logger.info("[종료] : {}", getRemoteAddress());
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		logger.debug("[메세지받음] ");
		MqttsMessageManagement mqttsMessage = new MqttsMessageManagement();
		AbstractMessage message = (AbstractMessage) e.getMessage();

		try {
			switch (message.getMsgType()) {
			case AbstractMessage.CONNACK:
				mqttsMessage.receviedConnAck((ConnAckMessage) message, clientId);
				break;
			case AbstractMessage.PUBLISH:
				PubAckMessage pubAckMsg = mqttsMessage.receviedPublish((PublishMessage) message, clientId);
				// QOS_1 일 경우 not null. pubAckMessage 보냄
				if (pubAckMsg != null) {
					e.getChannel().write(pubAckMsg);
				}
				break;
			case AbstractMessage.PUBACK:
				mqttsMessage.receviedPubAck((PubAckMessage) message, clientId);
				break;
			case AbstractMessage.PINGRESP:
				mqttsMessage.receviedPingResp((PingRespMessage) message, clientId);
				break;
			}
		} catch (Exception ex) {
			logger.error("[정의되지 않은 메세지] : {}", e);
		}
	}

	private PingReqMessage sendPingReq() {
		PingReqMessage message = new PingReqMessage();
		message.setMsgType(PingReqMessage.MSGTYPE_PINGREQ);

		logger.info("Send a PingReqMessage. length : {}, msgType : {} ", new Object[] { message.getLength(), message.getMsgType() });

		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		errorLog.error("[예외사항] : {}", e.getCause().toString());
		logger.error("[예외사항]");

		e.getChannel().close();
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

		if (e instanceof IdleStateEvent) {
			if (((IdleStateEvent) e).getState().equals(IdleState.WRITER_IDLE)) {
				e.getChannel().write(sendPingReq());
			}
		}
		super.handleUpstream(ctx, e);
	}
}
