package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.PubAckMessage;

/**
 * PubAckMessage를 Decoder하는 클래스
 * 
 * @date : 2013. 10. 14. 오후 3:55:51
 */
public class PubAckDecoder {

	public Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, PubAckMessage msg) throws Exception {
		PubAckMessage message = msg;

		if (buffer.readableBytes() < 5) {
			return MessageDecoderResult.NEED_DATA;
		}
		buffer.markReaderIndex();

		// TopicId 2 bytes
		short topicId = (short) Utils.readWord(buffer);
		message.setTopicId(topicId);

		// MsgId 2 bytes
		short msgId = (short) Utils.readWord(buffer);
		message.setMsgId(msgId);

		// returnCode 1 byte
		message.setReturnCode(buffer.readByte());

		return message;
	}
}
