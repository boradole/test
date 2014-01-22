package com.sktelecom.ssm.broker.service;

import java.util.Map;

public interface ConnectService {
	/**
	 * 단말정보 조회 인증,비인증
	 * 
	 * @date : 2013. 11. 4.
	 * 
	 * @param mac
	 * @param gwId
	 * 
	 * @return 단말 인증 정보
	 */
	public String isAuth(String mac, String gwId);

	/**
	 * Queue에 있는 인증된 Mac에 대한 Gateway Id 업데이트
	 * 
	 * @date : 2013. 11. 4.
	 * 
	 * @param updateMap
	 *            (mac,gwInfo)
	 */
	public void modifyAuthForBatch(Map<String, String> updateMap);

	/**
	 * 단말정보 DB Insert
	 * 
	 * @date : 2013. 11. 6.
	 * 
	 * @param insertMap
	 *            (mac,gwId)
	 */
	public void addDeviceForBatch(Map<String, String> insertMap);

	/**
	 * 단말 접속 만료 기간
	 * 
	 * @date : 2013. 11. 6.
	 * @param authYn
	 * 
	 * @return 분단위
	 */
	public int getExpireTime();

	/**
	 * 단말 정보 삭제
	 * 
	 * @date : 2013. 11. 13.
	 * 
	 * @param mac
	 */
	// TODO 백M 다시 얘기
	public void deleteDevice(String mac);

	/**
	 * 단말 Auth 조회
	 * 
	 * @date : 2013. 11. 13.
	 * 
	 * @param mac
	 * @return auth
	 */
	public String isAuth(String mac);

}
