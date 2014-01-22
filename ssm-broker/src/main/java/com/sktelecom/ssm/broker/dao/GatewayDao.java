package com.sktelecom.ssm.broker.dao;

import com.sktelecom.ssm.broker.entity.GatewayEntity;

public interface GatewayDao {

	/**
	 * 게이트웨이 접속 추가
	 * 
	 * @date : 2013. 11. 5.
	 * 
	 * @param gateWayDomain
	 * @return 생성된 게이트웨이 ID
	 */
	public String insertGateway(GatewayEntity gatewayEntity);

	/**
	 * 게이트웨이 정보 조회
	 * 
	 * @date : 2013. 11. 6.
	 * 
	 * @param gwIp
	 * @param port
	 * @return DB 존재하는 게이트웨이 ID , 없다면 null
	 */
	public String selectGateway(String gwIp, String port);

}
