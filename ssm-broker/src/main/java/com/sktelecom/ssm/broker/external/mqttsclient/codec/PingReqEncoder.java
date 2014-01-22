package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingReqMessage;

public class PingReqEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		PingReqMessage message = (PingReqMessage) msg;
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		buffer.markReaderIndex();

		// Length 2 bytes
		Utils.writeWord(buffer, message.getLength());

		// msgType 1 byte (value 0x16)
		buffer.writeByte(message.getMsgType());

		return buffer;
	}
}
