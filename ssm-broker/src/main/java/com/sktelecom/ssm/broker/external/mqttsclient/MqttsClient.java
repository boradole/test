/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.sktelecom.ssm.broker.external.mqttsclient;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.AbstractMessage.QOSType;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.ConnectMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.DisconnectMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingReqMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PubAckMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PublishMessage;
import com.sktelecom.ssm.broker.util.MqttsClientProperties;

@Component
public class MqttsClient {
	private static Logger logger = LoggerFactory.getLogger(MqttsClient.class);

	private String host;
	private int port;
	private Channel channel;
	private ClientBootstrap bootstrap;
	private String clientId;
	private static int WRITE_IDLE_SECOND = 0;
	private static int READ_TIMEOUT_SECOND = 0;

	@Autowired
	private MqttsClientProperties msAcc;

	public String getClientId() {
		return clientId;
	}

	public MqttsClient(String host, int port, String clientId) {
		this.host = host;
		this.port = port;
		this.clientId = clientId;
		// initailize();
		run();
	}

	@Autowired
	public MqttsClient(MqttsClientProperties msAcc) {
		MqttsClient.WRITE_IDLE_SECOND = msAcc.getWRITE_IDLE_SECOND();
		MqttsClient.READ_TIMEOUT_SECOND = msAcc.getREAD_TIMEOUT_SECOND();
	}

	public void run() {
		// Initialize the timer that schedules subsequent reconnection attempts.
		final Timer timer = new HashedWheelTimer();

		// Configure the client.
		// newCachedThreadPool 는 동적으로 쓰레드 할당되며 수에 제한없음.
		// 제한 하고 싶은면 fixed 사용.
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new MqttsClientPipelineFactory(bootstrap, timer, clientId, WRITE_IDLE_SECOND, READ_TIMEOUT_SECOND));

		bootstrap.setOption("remoteAddress", new InetSocketAddress(host, port));
		// Make a new connection.
		ChannelFuture connectFuture = bootstrap.connect();

		// Wait until the connection is made successfully.
		channel = connectFuture.awaitUninterruptibly().getChannel();
	}

	public void connect() {
		ConnectMessage message = new ConnectMessage();

		message.setMsgType(ConnectMessage.MSGTYPE_CONNECTION);
		message.setFlags(ConnectMessage.FLAGS);
		message.setProtocolId(ConnectMessage.PROTOCOLID);
		message.setKeepAlive(ConnectMessage.KEEP_ALIVE_SEC);
		message.setClientId(clientId);

		logger.info(
				"Send a ConnectMessage. length : {}, msgType : {}, cleansession : {}, willflag : {}, protocolId : {}, keepAlive : {}, clientId : \"{}\"",
				new Object[] { message.getLength(), message.getMsgType(), message.isCleanSession(), message.isWillFlag(), message.getProtocolId(),
						message.getKeepAlive(), message.getClientId() });

		channel.write(message);
	}

	public void disconnect() throws InterruptedException {
		DisconnectMessage message = new DisconnectMessage();

		message.setLength(DisconnectMessage.TOTAL_LENGTH);
		message.setMsgType(DisconnectMessage.MSGTYPE_DISCONNECT);
		message.setKeepAlive(DisconnectMessage.KEEP_ALIVE_SEC);

		logger.info("Send a DisconnectMessage. length : {}, msgType : {}, duration : {}",
				new Object[] { message.getLength(), message.getMsgType(), message.getKeepAlive() });

		channel.write(message);
		Thread.sleep(1000);
		close();
	}

	public void pingReq() {
		PingReqMessage message = new PingReqMessage();

		message.setMsgType(PingReqMessage.MSGTYPE_PINGREQ);

		logger.info("Send a PingReqMessage. length : {}, msgType : {}\"", new Object[] { message.getLength(), message.getMsgType() });

		channel.write(message);
	}

	public void publishQos0(short topic, short msgId, String payload) {
		PublishMessage message = new PublishMessage();

		message.setMsgType(PublishMessage.MSGTYPE_PUBLISH);
		message.setFlags((byte) 0x00); // Qos0:0b00,Qos1:0b02..(0b00~0b03,0b08~0b0a)
		message.setTopicId(topic);
		message.setMsgId(msgId);
		message.setPayload(payload); // test my payload

		logger.info(
				"Send a PublishMessage Qos0. totalMessageLength : {}, msgType : {}, dop flags : {}, qos : {}, Retain flags : {}, topicId : \"{}\", MsgId : {}, PayLoad : \"{}\"",
				new Object[] { message.getLength(), message.getMsgType(), message.isDupFlag(), QOSType.MOST_ONE, message.isRetainFlag(), message.getTopicId(),
						message.getMsgId(), new String(message.getPayload()) });

		channel.write(message);
	}

	public void publishQos1(short topic, short msgId, String payload) {
		PublishMessage message = new PublishMessage();

		message.setMsgType(PublishMessage.MSGTYPE_PUBLISH);
		message.setFlags((byte) 0x20); // Qos0:0b00,Qos1:0b02..(0b00~0b03,0b08~0b0a)
		message.setTopicId(topic);
		message.setMsgId(msgId);
		message.setPayload(payload); // test my payload

		logger.info(
				"Send a PublishMessage Qos1. totalMessageLength : {}, msgType : {}, dop flags : {}, qos : {}, Retain flags : {}, topicId : \"{}\", MsgId : {}, PayLoad : \"{}\"",
				new Object[] { message.getLength(), message.getMsgType(), message.isDupFlag(), QOSType.LEAST_ONE, message.isRetainFlag(), message.getTopicId(),
						message.getMsgId(), new String(message.getPayload()) });

		channel.write(message);
	}

	public void publishAck(short topic, short msgid) {
		PubAckMessage message = new PubAckMessage();

		message.setLength(PubAckMessage.TOTAL_LENGTH);
		message.setMsgType(PubAckMessage.MSGTYPE_PUBACK);
		message.setTopicId(topic);
		message.setMsgId(msgid);
		message.setReturnCode(PubAckMessage.ACCEPTED);

		logger.info("Send a PubAckMessage. totalMessageLength : {}, msgType : {}, topicId : {}, MsgId : {}, returnCode : {}",
				new Object[] { message.getLength(), message.getMsgType(), message.getTopicId(), message.getMsgId(), message.getReturnCode() });

		channel.write(message);
	}

	public void close() {
		channel.getCloseFuture().getChannel().close().awaitUninterruptibly();

		// Shut down all thread pools to exit.
		bootstrap.releaseExternalResources();
	}

	public boolean isOpen() {
		if (channel.isOpen())
			return true;
		return false;
	}

}
