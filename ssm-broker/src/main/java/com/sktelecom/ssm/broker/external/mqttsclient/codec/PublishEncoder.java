package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.PublishMessage;

/**
 * PublishMessage를 Encode 처리하여 buffer에 담는 클래스
 * 
 * @date : 2013. 10. 14. 오후 3:56:23
 */
public class PublishEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {

		PublishMessage message = (PublishMessage) msg;

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		// Length 2 bytes
		Utils.writeWord(buffer, message.getLength());

		// msgType 1 byte (value 0x0c)
		buffer.writeByte(message.getMsgType());

		// Flags
		buffer.writeByte(message.getFlags());

		// topicId 2 bytes, msgId 2 bytes
		Utils.writeWord(buffer, message.getTopicId());
		Utils.writeWord(buffer, message.getMsgId());

		// Data n bytes
		byte[] payload = message.getPayload().getBytes();
		buffer.writeBytes(payload);

		return buffer;
	}

}
