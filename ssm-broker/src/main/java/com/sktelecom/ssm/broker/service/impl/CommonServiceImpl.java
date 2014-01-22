package com.sktelecom.ssm.broker.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sktelecom.ssm.broker.config.ConfigManager;
import com.sktelecom.ssm.broker.dao.DeviceDao;
import com.sktelecom.ssm.broker.dao.GatewayDao;
import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;
import com.sktelecom.ssm.broker.entity.DeviceMsgSucEntity;
import com.sktelecom.ssm.broker.entity.GatewayEntity;
import com.sktelecom.ssm.broker.service.CommonService;
import com.sktelecom.ssm.broker.util.InsertMsgSucQueue;
import com.sktelecom.ssm.util.CommonUtil;
import com.sktelecom.ssm.util.KeyGenerator;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {

	private static Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private GatewayDao gatewayDao;

	@Autowired
	private ConfigManager configmanager;

	@Autowired
	private InsertMsgSucQueue insertMsgSucQueue;

	@Autowired
	private MessageSourceAccessor msAcc;

	@Override
	public String joinGateway(String ip, String port) {

		String gwId = gatewayDao.selectGateway(ip, port);

		if (gwId != null) {
			log.info("[join] select GateWayId : {} ", gwId);
			return gwId;
		}

		String key = KeyGenerator.getKey();
		int brokerKey = configmanager.getBrokerKey();
		String now = CommonUtil.now();
		String gatewayId = key + "_" + brokerKey;

		GatewayEntity gatewayEntity = new GatewayEntity();
		gatewayEntity.setGwIp(ip);
		gatewayEntity.setGwPort(port);
		gatewayEntity.setGwId(gatewayId);
		gatewayEntity.setRegTm(now);
		gatewayEntity.setUpTm(now);
		gatewayEntity.setStat("Y");

		String id = gatewayDao.insertGateway(gatewayEntity);
		log.info("[join] Create GateWayId : {} ", id);

		return id;
	}

	@Override
	public void addDevice(DeviceEntity deviceEntity) {
		deviceDao.insertDevice(deviceEntity);

	}

	@Override
	public int delDevice(String mac) {
		return deviceDao.deleteDevice(mac);
	}

	@Override
	public void putMsgSucQueue(String msgId, String deviceId) {
		insertMsgSucQueue.putData(msgId, deviceId);
	}

	@Override
	public void addDeviceMsgSuc(Map<String, String> insertMsgSucMap) {
		for (Entry<String, String> entry : insertMsgSucMap.entrySet()) {
			String now = CommonUtil.now();
			DeviceMsgSucEntity deviceMsgSucEntity = new DeviceMsgSucEntity();
			deviceMsgSucEntity.setMsgId(entry.getKey());
			deviceMsgSucEntity.setDeviceId(entry.getValue());
			deviceMsgSucEntity.setDeliveryTm(now);
			deviceDao.insertDeviceMsgSuc(deviceMsgSucEntity);
		}
	}

	@Override
	public String getDeviceId(String msgId, String mac) {
		DeviceEntity deviceEntity = deviceDao.selectDevice(mac);
		// String deviceId = null;
		// if (deviceEntity != null) {
		// deviceId = deviceEntity.getDeviceId();
		// }
		// return deviceId;
		return (deviceEntity != null) ? deviceEntity.getDeviceId() : null;
	}

	@Override
	public void delDeviceMsg(Map<String, String> insertMsgSucMap) {
		for (Entry<String, String> entry : insertMsgSucMap.entrySet()) {
			deviceDao.deleteDeviceMsg(entry.getKey());
		}
	}

	@Override
	public void addDeviceMsgOverRetryCnt(int retryCnt) {
		deviceDao.insertDeviceMsgFail(retryCnt);
	}

	@Override
	public void delDeviceMsgOverCnt(int retryCnt) {
		deviceDao.deleteDeviceMsgOverCnt(retryCnt);
	}

	@Override
	public List<DeviceMessageEntity> getPushDataListOverTime(String now, int selectCnt) {
		List<DeviceMessageEntity> list = deviceDao.selectPushDataListOverTime(now, selectCnt);
		return list;
	}

	@Override
	public void modDeviceMsgRetryCnt(String msgId, String deviceId, int retryCnt) {
		deviceDao.updateDeviceMsgRetryCnt(msgId, deviceId, retryCnt);
	}

	@Override
	public String getDeviceMessage(String msgId) {
		Object msg = deviceDao.selectDeviceMessage(msgId);
		return msg.toString();
	}
}
