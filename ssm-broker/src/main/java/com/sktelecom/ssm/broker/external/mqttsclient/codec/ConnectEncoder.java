package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.ConnectMessage;

public class ConnectEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		ConnectMessage message = (ConnectMessage) msg;
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		// Length 2 bytes
		Utils.writeWord(buffer, message.getLength());

		// msgType 1 byte (value 0x04)
		buffer.writeByte(message.getMsgType());

		// connection flags 1 byte
		byte connectionFlags = message.getFlags();
		if (message.isCleanSession()) {
			connectionFlags |= 0x02;
		}
		if (message.isWillFlag()) {
			connectionFlags |= 0x04;
		}
		buffer.writeByte(connectionFlags);

		// protocol ID 1 byte
		buffer.writeByte(message.getProtocolId());

		// Keep alive timer 2 bytes
		Utils.writeWord(buffer, message.getKeepAlive());

		byte[] clientId = message.getClientId().getBytes();
		buffer.writeBytes(clientId);

		return buffer;
	}
}