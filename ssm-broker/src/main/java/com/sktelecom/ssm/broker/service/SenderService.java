package com.sktelecom.ssm.broker.service;

import java.util.List;

import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;
import com.sktelecom.ssm.broker.entity.MessageEntity;

public interface SenderService {

	/**
	 * reqPushData에 대한 Message 저장(MSG,QOS,MSG_ID)
	 * 
	 * @date : 2013. 11. 14.
	 * 
	 * @param messageEntity
	 * @return 성공 여부
	 */
	public String addMessage(MessageEntity messageEntity);

	/**
	 * deviceID 조회
	 * 
	 * @date : 2013. 11. 14.
	 * 
	 * @param recvMdns
	 * @return
	 */
	public String getDeviceIds(String recvMdns);

	/**
	 * device 별 Message 저장(MSG_ID , DEVICE_ID)
	 * 
	 * @date : 2013. 11. 14.
	 * 
	 * @param deviceMessageEntity
	 * @return
	 */
	public String addDeviceMsg(DeviceMessageEntity deviceMessageEntity);

	/**
	 * gateway별 push 메세지 조회
	 * 
	 * @date : 2013. 11. 25.
	 * 
	 * @param deviceMsgId
	 * @return
	 */
	public List<DeviceMessageEntity> getPushDataList(String deviceMsgId);

}
