package com.sktelecom.ssm.broker.dao;

import com.sktelecom.ssm.broker.entity.BrokerEntity;

public interface BrokerDao {

	/**
	 * Broker 시작시 DB에 Broker 정보 저장
	 * 
	 * @date : 2013. 11. 11.
	 * 
	 * @param brokerEntity
	 * @return
	 */
	public int insertBroker(BrokerEntity brokerEntity);

}
