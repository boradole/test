package com.sktelecom.ssm.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MSG")
public class MessageEntity {

	@Id
	@Column(name = "MSG_ID")
	private String msgId;

	@Column(name = "MSG")
	private String msg;

	@Column(name = "REG_TM")
	private String regTm;

	@Column(name = "QOS")
	private String qos;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRegTm() {
		return regTm;
	}

	public void setRegTm(String regTm) {
		this.regTm = regTm;
	}

	public String getQos() {
		return qos;
	}

	public void setQos(String qos) {
		this.qos = qos;
	}

	public boolean isQos() {
		return (this.qos.equals("1"));
	}

	@Override
	public String toString() {
		return "Msg:" + getMsg() + ", MsgId:" + getMsgId() + ", Qos:" + getQos() + ", RegTm:" + getRegTm();
	}
}
