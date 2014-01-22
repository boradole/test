package com.sktelecom.ssm.broker.external.mqttsclient.messages;

/**
 * Doesn't care DUP, QOS and RETAIN flags.
 */
public class DisconnectMessage extends AbstractMessage {
	public static final byte MSGTYPE_DISCONNECT = 0x18;
	public static final int KEEP_ALIVE_SEC = 3;
	public static final short TOTAL_LENGTH = 5;

	private int m_keepAlive;

	public int getKeepAlive() {
		return m_keepAlive;
	}

	public void setKeepAlive(int keepAlive) {
		this.m_keepAlive = keepAlive;
	}
}
