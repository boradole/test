package com.sktelecom.ssm.broker.config;

public interface ConfigManager {

	/**
	 * broker 구동 시 BROKER_INF에 저장(BROKER_IDX는 AutoIncreament)
	 * 
	 * @date : 2013. 11. 13.
	 */
	public void addBroker();

	/**
	 * broker 구동 시 저장했던 BrokerIdx 값을 Map에서 얻기
	 * 
	 * @date : 2013. 11. 13.
	 * 
	 * @return BrokerIdx
	 */
	public int getBrokerKey();

}
