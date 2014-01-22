package com.sktelecom.ssm.broker.external.mqttsclient.messages;

/**
 * Doesn't care DUP, QOS and RETAIN flags.
 */
public class PingReqMessage extends AbstractMessage {
	public static final byte MSGTYPE_PINGREQ = 0x16;

	public short getLength() {
		return m_length;
	}
}
