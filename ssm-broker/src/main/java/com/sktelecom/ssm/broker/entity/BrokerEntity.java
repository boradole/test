package com.sktelecom.ssm.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BROKER_INF")
public class BrokerEntity {

	@Id
	@Column(name = "BROKER_IDX")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int brokerIdx;

	@Column(name = "REG_TM")
	private String regTm;

	public int getBrokerIdx() {
		return brokerIdx;
	}

	public void setBrokerIdx(int brokerIdx) {
		this.brokerIdx = brokerIdx;
	}

	public String getRegTm() {
		return regTm;
	}

	public void setRegTm(String regTm) {
		this.regTm = regTm;
	}

	@Override
	public String toString() {
		return "brokerId :" + getBrokerIdx() + ", RegTm:" + getRegTm();
	}

}
