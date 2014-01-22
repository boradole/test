package com.sktelecom.ssm.broker.external.mqttsclient.messages;

/**
 * Basic abstract message for all MQTT protocol messages.
 */
public abstract class AbstractMessage {

	public static final byte CONNECT = 0x04; // Client request to connect to
	// Server
	public static final byte CONNACK = 0x05; // Connect Acknowledgment
	public static final byte PUBLISH = 0x0c; // Publish message
	public static final byte PUBACK = 0x0d; // Publish Acknowledgment
	public static final byte PINGREQ = 0x16; // PING Request
	public static final byte PINGRESP = 0x17; // PING Response
	public static final byte DISCONNECT = 0x18; // Client is Disconnecting

	public static enum QOSType {

		MOST_ONE, LEAST_ONE;
	}

	protected short m_length;
	protected byte m_msgType;

	private byte m_flags;
	private boolean m_dupFlag;
	private QOSType m_qos;
	private boolean m_retainFlag;

	public short getLength() {
		return m_length;
	}

	public void setLength(short length) {
		this.m_length = length;
	}

	public byte getMsgType() {
		return m_msgType;
	}

	public void setMsgType(byte msgType) {
		this.m_msgType = msgType;
	}

	public byte getFlags() {

		return m_flags;
	}

	public void setFlags(byte flags) {
		this.m_flags = flags;
	}

	public boolean isDupFlag() {
		return m_dupFlag;
	}

	public void setDupFlag(boolean dupFlag) {
		this.m_dupFlag = dupFlag;
	}

	public QOSType getQos() {
		return m_qos;
	}

	public void setQos(QOSType qos) {
		this.m_qos = qos;
	}

	public boolean isRetainFlag() {
		return m_retainFlag;
	}

	public void setRetainFlag(boolean retainFlag) {
		this.m_retainFlag = retainFlag;
	}

}