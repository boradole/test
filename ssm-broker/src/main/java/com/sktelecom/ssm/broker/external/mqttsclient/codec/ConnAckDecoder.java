package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.ConnAckMessage;

public class ConnAckDecoder {

	public Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, ConnAckMessage msg) {
		ConnAckMessage message = msg;

		if (buffer.readableBytes() < 1) {
			return MessageDecoderResult.NEED_DATA;
		}
		buffer.markReaderIndex();

		// returnCode 1 byte
		message.setReturnCode(buffer.readByte());

		return message;
	}

}
