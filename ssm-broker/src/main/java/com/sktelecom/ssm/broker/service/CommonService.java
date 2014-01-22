package com.sktelecom.ssm.broker.service;

import java.util.List;
import java.util.Map;

import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;

public interface CommonService {

	/**
	 * 게이트웨이 접속 요청
	 * 
	 * @date : 2013. 11. 5.
	 * 
	 * @param ip
	 * @param port
	 * @return 생성된 게이트웨이 ID
	 */
	public String joinGateway(String ip, String port);

	/**
	 * DEVICE 테이블에 Device 정보 추가
	 * 
	 * @date : 2013. 11. 13.
	 * 
	 * @param deviceEntity
	 */
	public void addDevice(DeviceEntity deviceEntity);

	/**
	 * Device 정보 삭제
	 * 
	 * @date : 2013. 11. 13.
	 * 
	 * @param mac
	 * @return
	 */
	public int delDevice(String mac);

	/**
	 * Message 전달 결과 일괄 입력 (DEVICE_MSG_SUC)
	 * 
	 * @date : 2013. 11. 18.
	 * 
	 * @param insertMsgSucMap
	 */
	public void addDeviceMsgSuc(Map<String, String> insertMsgSucMap);

	/**
	 * MsgSucQueue에 정보 추가
	 * 
	 * @date : 2013. 11. 18.
	 * 
	 * @param msgId
	 * @param deviceId
	 */
	public void putMsgSucQueue(String msgId, String deviceId);

	/**
	 * DeviceId 조회
	 * 
	 * @date : 2013. 11. 18.
	 * 
	 * @param msgId
	 * @param mac
	 * @return
	 */
	public String getDeviceId(String msgId, String mac);

	/**
	 * Publish 전송 성공한 Message 대기 정보 일괄 삭제 (DEVICE_MSG)
	 * 
	 * @date : 2013. 11. 20.
	 * 
	 * @param insertMsgSucMap
	 */
	public void delDeviceMsg(Map<String, String> insertMsgSucMap);

	/**
	 * 재시도 횟수 retry 이상인 메세지들 조회후 저장
	 * 
	 * @date : 2013. 11. 20.
	 * 
	 * @param retryCnt
	 */
	public void addDeviceMsgOverRetryCnt(int retryCnt);

	/**
	 * 재시도 횟수 retry 이상인 메세지 삭제(DEVICE_MSG)
	 * 
	 * @date : 2013. 11. 25.
	 * 
	 * @param retryCnt
	 */
	public void delDeviceMsgOverCnt(int retryCnt);

	/**
	 * 등록 시간이 n시간 경과한 메세지 n1개 조회 (DEVICE_MSG)
	 * 
	 * @date : 2013. 11. 25.
	 * 
	 * @param now
	 * @param selectCnt
	 * @return
	 */
	public List<DeviceMessageEntity> getPushDataListOverTime(String now,
			int selectCnt);

	/**
	 * 재전송할 메세지 조회후, 재시도 횟수 +1 업데이트
	 * 
	 * @date : 2013. 11. 27.
	 * 
	 * @param msgId
	 * @param deviceId
	 * @param retryCnt
	 */
	public void modDeviceMsgRetryCnt(String msgId, String deviceId, int retryCnt);

	/**
	 * MSG 에서 data 조회
	 * 
	 * @date : 2013. 11. 27.
	 * 
	 * @param msgId
	 * @return
	 */
	public String getDeviceMessage(String msgId);

}
