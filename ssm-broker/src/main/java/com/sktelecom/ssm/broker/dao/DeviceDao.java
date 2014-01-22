package com.sktelecom.ssm.broker.dao;

import java.util.List;

import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;
import com.sktelecom.ssm.broker.entity.DeviceMsgSucEntity;

public interface DeviceDao {

	/**
	 * 단말 정보 조회
	 * 
	 * @date : 2013. 11. 4.
	 * 
	 * @param mac
	 * @return Auth, Mac
	 */
	public DeviceEntity selectDevice(String mac);

	/**
	 * updateQueue에 쌓인 mac정보를 이용해서 DEVICE 테이블의 GW_ID 값 업데이트.
	 * 
	 * @date : 2013. 11. 5.
	 * 
	 * @param gwInfo
	 * @param mac
	 * @return update 성공 여부
	 */
	public int updateGatewayInfo(String gwInfo, String mac);

	/**
	 * insertQueue에 쌓인 정보를 토대로 DEVICE 테이블에 정보 추가
	 * 
	 * @date : 2013. 11. 6.
	 * 
	 * @param deviceEntity
	 */
	public String insertDevice(DeviceEntity deviceEntity);

	/**
	 * 단말 정보 삭제
	 * 
	 * @date : 2013. 11. 13.
	 * 
	 * @param mac
	 * @return
	 */
	public int deleteDevice(String mac);

	/**
	 * deviceId 조회
	 * 
	 * @date : 2013. 11. 14.
	 * 
	 * @param recvMdns
	 * @return
	 */
	public String selectDeviceId(String recvMdns);

	/**
	 * device 별 Message 저장
	 * 
	 * @date : 2013. 11. 14.
	 * 
	 * @param deviceMessageEntity
	 */
	public String insertDeviceMsg(DeviceMessageEntity deviceMessageEntity);

	/**
	 * Gateway 별 Push Message 조회(MSG_ID)
	 * 
	 * @date : 2013. 11. 14.
	 * 
	 * @param deviceMsgId
	 */
	public List<DeviceMessageEntity> selectPushDataList(String deviceMsgId);

	/**
	 * Message 전달 결과 일괄 입력(DEVICE_MSG_SUC)
	 * 
	 * @date : 2013. 11. 20.
	 * 
	 * @param deviceMsgSucEntity
	 * @return
	 */
	public String insertDeviceMsgSuc(DeviceMsgSucEntity deviceMsgSucEntity);

	/**
	 * Message 대기 정보 일괄 삭제(DEVICE_MSG)
	 * 
	 * @date : 2013. 11. 20.
	 * 
	 * @param key
	 */
	public void deleteDeviceMsg(String msgId);

	/**
	 * 재시도 횟수가 retryCnt 이상인 메제지 저장(DEVICE_MSG_FAIL)
	 * 
	 * @date : 2013. 11. 20.
	 * 
	 * @param retryCnt
	 */
	public void insertDeviceMsgFail(int retryCnt);

	/**
	 * 재시도 횟수 retryCnt 이상인 메세지 삭제(DEVICE_MSG)
	 * 
	 * @date : 2013. 11. 25.
	 * 
	 * @param retryCnt
	 */
	public void deleteDeviceMsgOverCnt(int retryCnt);

	/**
	 * 재전송할 메세지 조회(등록시간이 n시간 경과한 메세지 n1개 조회, DEVICE_MSG)
	 * 
	 * @date : 2013. 11. 25.
	 * 
	 * @param now
	 * @param selectCnt
	 * @return
	 */
	public List<DeviceMessageEntity> selectPushDataListOverTime(String now, int selectCnt);

	/**
	 * 재전송할 메세지 조회후, 재시도 횟수 +1 업데이트
	 * 
	 * @date : 2013. 11. 27.
	 * 
	 * @param msgId
	 * @param deviceId
	 * @param retryCnt
	 */
	public void updateDeviceMsgRetryCnt(String msgId, String deviceId, int retryCnt);

	/**
	 * MSG 에서 data 조회
	 * 
	 * @date : 2013. 11. 27.
	 * 
	 * @param msgId
	 * @return
	 */
	public Object selectDeviceMessage(String msgId);

}
