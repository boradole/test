package com.sktelecom.ssm.broker.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sktelecom.ssm.broker.dao.DeviceDao;
import com.sktelecom.ssm.broker.dao.MessageDao;
import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;
import com.sktelecom.ssm.broker.entity.MessageEntity;
import com.sktelecom.ssm.broker.service.SenderService;

@Service
@Transactional
public class SenderServiceImpl implements SenderService {

	private static Logger log = LoggerFactory.getLogger(SenderServiceImpl.class);

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private DeviceDao deviceDao;

	@Override
	public String addMessage(MessageEntity messageEntity) {
		String messageId = messageDao.insertMessage(messageEntity);
		return messageId;
	}

	@Override
	public String getDeviceIds(String recvMdns) {

		String deviceIds = deviceDao.selectDeviceId(recvMdns);

		if (deviceIds != null && deviceIds.contains(",")) {
			deviceIds = deviceIds.substring(0, deviceIds.lastIndexOf(","));
		}

		return deviceIds;
	}

	@Override
	public String addDeviceMsg(DeviceMessageEntity deviceMessageEntity) {
		String id = deviceDao.insertDeviceMsg(deviceMessageEntity);
		return id;
	}

	@Override
	public List<DeviceMessageEntity> getPushDataList(String deviceMsgId) {
		List<DeviceMessageEntity> pushDatalist = deviceDao.selectPushDataList(deviceMsgId);

		for (DeviceMessageEntity deviceMessageEntity : pushDatalist) {
			log.info("MSG_ID :{} ,Mac : {}", deviceMessageEntity.getMsgId(), deviceMessageEntity.getDeviceEntity().getMac());
		}

		return pushDatalist;
	}
}
