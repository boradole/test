package com.sktelecom.ssm.broker.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "DEVICE_MSG")
@DynamicInsert(value = true)
// default : false , true면 값이 null이 아닌 프로퍼티에 대해서 동적 쿼리 생성 (insert 할 때
// DeviceDomain이 null이기 때문)
public class DeviceMessageEntity implements Serializable {
	private static final long serialVersionUID = -636510999568609780L;

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

	@ManyToOne(targetEntity = DeviceEntity.class)
	@JoinColumn(name = "DEVICE_ID")
	// @ForeignKey(name="deviceId")
	private DeviceEntity deviceEntity;

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

	public DeviceEntity getDeviceEntity() {
		return deviceEntity;
	}

	public void setDeviceEntity(DeviceEntity deviceEntity) {
		this.deviceEntity = deviceEntity;
	}

	public String getDeliveryStat() {
		return deliveryStat;
	}

	public void setDeliveryStat(String deliveryStat) {
		this.deliveryStat = deliveryStat;
	}

	public String getMac() {
		return deviceEntity.getMac();
	}

}
