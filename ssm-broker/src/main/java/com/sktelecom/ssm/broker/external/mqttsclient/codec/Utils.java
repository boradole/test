package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import java.io.UnsupportedEncodingException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.AbstractMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.ConnAckMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.ConnectMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.DisconnectMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingReqMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingRespMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PubAckMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PublishMessage;

/**
 * Common utils methodd used in codecs.
 * 
 * @author andrea
 */
public class Utils {

	public static final int MAX_LENGTH_LIMIT = 268435455;

	/**
	 * Read 2 bytes from in buffer first MSB, and then LSB returning as int.
	 */
	static int readWord(ChannelBuffer in) {
		int msb = in.readByte() & 0x00FF; // remove sign extension due to
		// casting
		int lsb = in.readByte() & 0x00FF;
		msb = (msb << 8) | lsb;
		return msb;
	}

	/**
	 * Writes as 2 bytes the int value into buffer first MSB, and then LSB.
	 */
	static void writeWord(ChannelBuffer out, int value) {
		out.writeByte((byte) ((value & 0xFF00) >> 8)); // msb
		out.writeByte((byte) (value & 0x00FF)); // lsb
	}

	/**
	 * Return the ChannelBuffer with string encoded as MSB, LSB and UTF-8
	 * encoded string content.
	 */
	static ChannelBuffer encodeString(String str) {
		ChannelBuffer out = ChannelBuffers.dynamicBuffer(2);
		byte[] raw;
		try {
			raw = str.getBytes("UTF-8");
			// NB every Java platform has got UTF-8 encoding by default, so this
			// exception are never raised.
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
		Utils.writeWord(out, raw.length);
		out.writeBytes(raw);
		out.resetReaderIndex();
		return out;
	}

	/**
	 * Load a string from the given buffer, reading first the two bytes of len
	 * and then the UTF-8 bytes of the string.
	 * 
	 * @return the decoded string or null if NEED_DATA
	 */
	static String decodeString(ChannelBuffer in) throws UnsupportedEncodingException {
		if (in.readableBytes() < 2) {
			return null;
		}
		int strLen = Utils.readWord(in);
		if (in.readableBytes() < strLen) {
			return null;
		}
		byte[] strRaw = new byte[strLen];
		in.readBytes(strRaw);

		return new String(strRaw, "UTF-8");
	}

	public static AbstractMessage createMessage(byte messageType) {
		switch (messageType) {
		case AbstractMessage.CONNECT:
			return new ConnectMessage();
		case AbstractMessage.CONNACK:
			return new ConnAckMessage();
		case AbstractMessage.PUBLISH:
			return new PublishMessage();
		case AbstractMessage.PUBACK:
			return new PubAckMessage();
		case AbstractMessage.PINGREQ:
			return new PingReqMessage();
		case AbstractMessage.PINGRESP:
			return new PingRespMessage();
		case AbstractMessage.DISCONNECT:
			return new DisconnectMessage();
		default:
			return null;
		}
	}
}
