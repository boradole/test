package com.sktelecom.ssm.broker.external.mqttsclient.messages;

/**
 * Placeholder for PUBACK message.
 */
public class PubAckMessage extends AbstractMessage {

	public static final byte MSGTYPE_PUBACK = 0x0D;
	public static final short TOTAL_LENGTH = 8;

	public static final byte ACCEPTED = 0x00;
	public static final byte CONGESTION = 0x01;
	public static final byte INVALID_TOPIC_ID = 0x02;
	public static final byte AUTHENTICATION_FAILD = 0x03;
	public static final byte RESPONSE_TIMEOUT = 0x04;
	public static final byte EXPIRE_CUSTOMER = 0x05;
	public static final byte BADREQUEST = 0x06;

	private short m_topicId;
	private short m_msgId;
	private byte m_returnCode;

	public short getTopicId() {
		return m_topicId;
	}

	public void setTopicId(short topicId) {
		this.m_topicId = topicId;
	}

	public short getMsgId() {
		return m_msgId;
	}

	public void setMsgId(short msgId) {
		this.m_msgId = msgId;
	}

	public byte getReturnCode() {
		return m_returnCode;
	}

	public void setReturnCode(byte returnCode) {
		this.m_returnCode = returnCode;
	}
}
