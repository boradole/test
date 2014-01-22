package com.sktelecom.ssm.broker.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICE_MSG_FAIL")
public class DeviceMsgFailEntity implements Serializable {

	private static final long serialVersionUID = 6015284596581122589L;

	@Id
	@Column(name = "MSG_ID")
	private String msgId;

	@Id
	@Column(name = "DEVICE_ID")
	private String deviceId;

	@Column(name = "JOB_ID")
	private String jobId;

	@Column(name = "DELIVERY_STAT")
	private String deliveryStat;

	@Column(name = "RETRY_CNT")
	private int retryCnt;

	@Column(name = "DELIVERY_TM")
	private String deliveryTm;

	@Column(name = "REG_TM")
	private String regTm;

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

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getDeliveryStat() {
		return deliveryStat;
	}

	public void setDeliveryStat(String deliveryStat) {
		this.deliveryStat = deliveryStat;
	}

	public int getRetryCnt() {
		return retryCnt;
	}

	public void setRetryCnt(int retryCnt) {
		this.retryCnt = retryCnt;
	}

	public String getDeliveryTm() {
		return deliveryTm;
	}

	public void setDeliveryTm(String deliveryTm) {
		this.deliveryTm = deliveryTm;
	}

	public String getRegTm() {
		return regTm;
	}

	public void setRegTm(String regTm) {
		this.regTm = regTm;
	}

}
