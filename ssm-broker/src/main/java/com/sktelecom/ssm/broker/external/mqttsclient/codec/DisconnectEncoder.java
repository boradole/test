package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.DisconnectMessage;

public class DisconnectEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		DisconnectMessage message = (DisconnectMessage) msg;

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		// Length 2 bytes
		Utils.writeWord(buffer, message.getLength());

		// msgType 1 byte (0x18)
		buffer.writeByte(message.getMsgType());

		// Keep alive timer 2 bytes
		Utils.writeWord(buffer, message.getKeepAlive());

		return buffer;
	}

}
