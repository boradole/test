package com.sktelecom.ssm.broker.external.mqttsclient.messages;

/**
 * The attributes Qos, Dup and Retain aren't used for Connect message
 */
public class ConnectMessage extends AbstractMessage {
	public static final byte MSGTYPE_CONNECTION = 0x04;
	public static final byte PROTOCOLID = 0x03;
	public static final short KEEP_ALIVE_SEC = 3;
	public static final byte FLAGS = 0x06;

	// flags
	private byte m_flags;
	private boolean m_cleanSession;
	private boolean m_willFlag;
	private String m_willTopic;
	private String m_willMessage;

	private byte m_protocolId;
	private String m_clientId;
	private short m_keepAlive;

	public short getLength() {
		return (short) (7 + getClientId().length());
	}

	public byte getFlags() {
		return m_flags;
	}

	public void setFlags(byte flags) {
		this.m_flags = flags;
	}

	public boolean isCleanSession() {
		return m_cleanSession;
	}

	public void setCleanSession(boolean cleanSession) {
		this.m_cleanSession = cleanSession;
	}

	public boolean isWillFlag() {
		return m_willFlag;
	}

	public void setWillFlag(boolean willFlag) {
		this.m_willFlag = willFlag;
	}

	public String getWillTopic() {
		return m_willTopic;
	}

	public void setWillTopic(String willTopic) {
		this.m_willTopic = willTopic;
	}

	public String getWillMessage() {
		return m_willMessage;
	}

	public void setWillMessage(String willMessage) {
		this.m_willMessage = willMessage;
	}

	public byte getProtocolId() {
		return m_protocolId;
	}

	public void setProtocolId(byte protocolId) {
		this.m_protocolId = protocolId;
	}

	public String getClientId() {
		return m_clientId;
	}

	public void setClientId(String clientId) {
		this.m_clientId = clientId;
	}

	public short getKeepAlive() {
		return m_keepAlive;
	}

	public void setKeepAlive(short keepAlive) {
		this.m_keepAlive = keepAlive;
	}
}
