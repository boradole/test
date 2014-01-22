package com.sktelecom.ssm.broker.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICE_MSG_SUC")
public class DeviceMsgSucEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8957804748563440360L;

	@Id
	@Column(name = "MSG_ID")
	private String msgId;

	@Id
	@Column(name = "DEVICE_ID")
	private String deviceId;

	@Column(name = "DELIVERY_TM")
	private String deliveryTm;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeliveryTm() {
		return deliveryTm;
	}

	public void setDeliveryTm(String deliveryTm) {
		this.deliveryTm = deliveryTm;
	}

}
