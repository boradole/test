package com.sktelecom.ssm.broker.entity;

public class PushParamCheckEntity {

	public String serviceId;
	public String sendMdn;
	public String recvMdns;
	public String data;
	public String qos;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSendMdn() {
		return sendMdn;
	}

	public void setSendMdn(String sendMdn) {
		this.sendMdn = sendMdn;
	}

	public String getRecvMdns() {
		return recvMdns;
	}

	public void setRecvMdns(String recvMdns) {
		this.recvMdns = recvMdns;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getQos() {
		return qos;
	}

	public void setQos(String qos) {
		this.qos = qos;
	}

	public String getServiceIdEq1() {
		return serviceId;
	}

}
