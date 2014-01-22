package com.sktelecom.ssm.broker.external.mqttsclient.messages;

/**
 * The attributes Qos, Dup and Retain aren't used.
 * 
 * @author andrea
 */
public class ConnAckMessage extends AbstractMessage {
	public static final short TOTAL_LENGTH = 4;
	public static final byte MSGTYPE_CONNACK = 0x05;

	public static final byte ACCEPTED = 0x00;
	public static final byte CONGESTION = 0x01;
	public static final byte INVALID_TOPIC_ID = 0x02;
	public static final byte AUTHENTICATION_FAILD = 0x03;
	public static final byte RESPONSE_TIMEOUT = 0x04;
	public static final byte EXPIRE_CUSTOMER = 0x05;
	public static final byte BADREQUEST = 0x06;

	private byte m_returnCode;

	public byte getReturnCode() {
		return m_returnCode;
	}

	public void setReturnCode(byte returnCode) {
		this.m_returnCode = returnCode;
	}

}
