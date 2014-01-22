package com.sktelecom.ssm.broker.external.mqttsclient.messages;

public class PublishMessage extends AbstractMessage {

	public static final byte MSGTYPE_PUBLISH = 0x0C;

	private short m_topicId;
	private short m_msgId;
	private String m_payload;

	public short getLength() {
		m_length = (short) (8 + getPayload().length());
		return m_length;
	}

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

	public String getPayload() {
		return m_payload;
	}

	public void setPayload(String payload) {
		this.m_payload = payload;
	}

}
