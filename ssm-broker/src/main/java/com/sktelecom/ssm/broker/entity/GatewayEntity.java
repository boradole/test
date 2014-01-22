package com.sktelecom.ssm.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
// @Cache(region = "cache1", usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "GW_INF")
public class GatewayEntity {

	@Id
	@Column(name = "GW_ID")
	private String gwId;

	@Column(name = "GW_PORT")
	private String gwPort;

	@Column(name = "GW_IP")
	private String gwIp;

	@Column(name = "STAT")
	private String stat;

	@Column(name = "REG_TM")
	private String regTm;

	@Column(name = "UP_TM")
	private String upTm;

	public String getGwId() {
		return gwId;
	}

	public void setGwId(String gwId) {
		this.gwId = gwId;
	}

	public String getGwIp() {
		return gwIp;
	}

	public void setGwIp(String gwIp) {
		this.gwIp = gwIp;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
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

	public String getGwPort() {
		return gwPort;
	}

	public void setGwPort(String gwPort) {
		this.gwPort = gwPort;
	}

	@Override
	public String toString() {
		return "gwId:" + getGwId() + ", gwIp:" + getGwIp() + ", gwPort:" + getGwPort() + ", RegTm:" + getRegTm() + ", State:" + getStat() + ", updateTm:"
				+ getUpTm();
	}

}
