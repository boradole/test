package com.sktelecom.ssm.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICE")
public class DeviceEntity {

	@Id
	@Column(name = "DEVICE_ID")
	private String deviceId; // 디바이스 ID

	@Column(name = "GW_ID")
	private String gwId; // gateWay ID

	@Column(name = "MAC")
	private String mac; // mac

	@Column(name = "MDN")
	private String mdn; // mdn

	@Column(name = "SERVICE_ID")
	private String serviceId; // 서비스 ID

	@Column(name = "USER_NM")
	private String userNm; // 사용자 이름

	@Column(name = "MAIL_ADDR")
	private String mailAddr; // 메일주소

	@Column(name = "AUTH_YN")
	private Boolean auth; // 인증 유무

	@Column(name = "REG_TM")
	private String regTm; // 등록 일시

	@Column(name = "UP_TM")
	private String upTm; // 수정 일시

	// @OneToMany(targetEntity = DeviceMessageDomain.class)
	// private Set<DeviceMessageDomain> deviceMessageDomain;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getGwId() {
		return gwId;
	}

	public void setGwId(String gwId) {
		this.gwId = gwId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMdn() {
		return mdn;
	}

	public void setMdn(String mdn) {
		this.mdn = mdn;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getMailAddr() {
		return mailAddr;
	}

	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}

	public Boolean getAuth() {
		return auth;
	}

	public void setAuth(Boolean authYn) {
		this.auth = authYn;
	}

	public String getRegTm() {
		return regTm;
	}

	public void setRegTm(String regTm) {
		this.regTm = regTm;
	}

	public String getUpTm() {
		return upTm;
	}

	public void setUpTm(String upTm) {
		this.upTm = upTm;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceIdEq1() {
		return serviceId;
	}

}
