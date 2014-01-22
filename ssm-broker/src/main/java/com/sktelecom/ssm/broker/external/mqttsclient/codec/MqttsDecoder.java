package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.AbstractMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.ConnAckMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingRespMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PubAckMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PublishMessage;

public class MqttsDecoder extends FrameDecoder {
	private static Logger logger = LoggerFactory.getLogger(MqttsDecoder.class);

	protected Object decodeCommonHeader(ChannelBuffer in) throws Exception {
		// Common decoding part
		if (in.readableBytes() < 3) {
			return MessageDecoderResult.NEED_DATA;
		}

		in.markReaderIndex();

		// length 2 byte
		short length = (short) Utils.readWord(in);

		// msgType 1 byte
		byte msgType = in.readByte();

		AbstractMessage message = Utils.createMessage(msgType);
		if (message == null) {
			in.skipBytes(in.readableBytes());
			throw new CorruptedFrameException("Bad messageType : " + msgType);
		}

		message.setMsgType(msgType);
		message.setLength(length);

		if (message.getMsgType() == PublishMessage.MSGTYPE_PUBLISH) {
			byte flags = in.readByte();
			boolean dupFlag = ((byte) ((flags & 0x0008) >> 3) == 1);
			byte qosLevel = (byte) ((flags & 0x0006) >> 1);
			boolean retainFlag = ((byte) (flags & 0x0001) == 1);

			message.setDupFlag(dupFlag);
			message.setQos(AbstractMessage.QOSType.values()[qosLevel]);
			message.setRetainFlag(retainFlag);
		}
		return message;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		AbstractMessage abstraceMessage = null;
		try {
			abstraceMessage = (AbstractMessage) decodeCommonHeader(buffer);
		} catch (ClassCastException e) {
			logger.error("Decode 에러. MessageDecoderResult : {}, channel : {}", new Object[] { decodeCommonHeader(buffer), channel });
			throw new ClassCastException("NEED_DATA");
		}
		if (abstraceMessage instanceof ConnAckMessage) {
			ConnAckDecoder connAckDecoder = new ConnAckDecoder();
			return connAckDecoder.decode(ctx, channel, buffer, (ConnAckMessage) abstraceMessage);
		} else if (abstraceMessage instanceof PingRespMessage) {
			PingRespDecoder pingRespDecoder = new PingRespDecoder();
			return pingRespDecoder.decode(ctx, channel, buffer, (PingRespMessage) abstraceMessage);
		} else if (abstraceMessage instanceof PubAckMessage) {
			PubAckDecoder pubAckDecoder = new PubAckDecoder();
			return pubAckDecoder.decode(ctx, channel, buffer, (PubAckMessage) abstraceMessage);
		} else {
			buffer.skipBytes(buffer.readableBytes());
			throw new CorruptedFrameException("messageType: " + abstraceMessage.getMsgType());
		}
	}
}
