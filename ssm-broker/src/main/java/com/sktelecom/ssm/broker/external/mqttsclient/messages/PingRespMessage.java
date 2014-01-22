package com.sktelecom.ssm.broker.external.mqttsclient.messages;

/**
 * Doesn't care DUP, QOS and RETAIN flags.
 */
public class PingRespMessage extends AbstractMessage {
	public static final short TOTAL_LENGTH = 3;
	public static final byte MSGTYPE_PINGRESP = 0x17;

}
