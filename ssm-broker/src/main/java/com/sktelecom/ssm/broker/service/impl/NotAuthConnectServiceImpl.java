package com.sktelecom.ssm.broker.service.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.transaction.annotation.Transactional;

import com.sktelecom.ssm.broker.config.ConfigManager;
import com.sktelecom.ssm.broker.dao.DeviceDao;
import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.service.ConnectService;
import com.sktelecom.ssm.broker.util.InsertMsgSucQueue;
import com.sktelecom.ssm.broker.util.InsertQueue;
import com.sktelecom.ssm.broker.util.UpdateQueue;
import com.sktelecom.ssm.util.CommonUtil;
import com.sktelecom.ssm.util.KeyGenerator;

@Transactional
public class NotAuthConnectServiceImpl implements ConnectService {

	private static Logger log = LoggerFactory.getLogger(NotAuthConnectServiceImpl.class);

	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private InsertQueue insertQueue;

	@Autowired
	private UpdateQueue updateQueue;

	@Autowired
	private ConfigManager configManager;

	@Autowired
	private InsertMsgSucQueue insertMsgSucQueue;

	@Autowired
	private MessageSourceAccessor msAcc;

	@Override
	public String isAuth(String mac, String gwId) {
		updateQueue.putData(gwId, mac);
		return "N";
	}

	@Override
	public void modifyAuthForBatch(Map<String, String> updateMap) {
		for (Entry<String, String> entry : updateMap.entrySet()) {
			if (deviceDao.updateGatewayInfo(entry.getValue(), entry.getKey()) < 1) {
				insertQueue.putData(entry.getValue(), entry.getKey());
			}
		}
	}

	@Override
	public void addDeviceForBatch(Map<String, String> insertMap) {
		String now = CommonUtil.now();

		for (Entry<String, String> entry : insertMap.entrySet()) {

			String key = KeyGenerator.getKey();
			int brokerKey = configManager.getBrokerKey();
			String gwId = key + "_" + brokerKey;

			DeviceEntity deviceEntity = new DeviceEntity();
			deviceEntity.setAuth(false);
			deviceEntity.setDeviceId(gwId);
			deviceEntity.setGwId(entry.getValue());
			deviceEntity.setMac(entry.getKey());
			deviceEntity.setServiceId("1");
			deviceEntity.setUpTm(now);
			deviceEntity.setRegTm(now);

			String deviceId = deviceDao.insertDevice(deviceEntity);

			if (log.isDebugEnabled()) {
				log.debug("Insert Device Id : {}", deviceId);
			}
		}
	}

	@Override
	public int getExpireTime() {
		// 분단위 24 * 60;
		return Integer.valueOf(msAcc.getMessage("EXPIRE_TIME"));
	}

	@Override
	public void deleteDevice(String mac) {
		deviceDao.deleteDevice(mac);
	}

	@Override
	public String isAuth(String mac) {
		DeviceEntity deviceEntity = deviceDao.selectDevice(mac);
		boolean auth = false;
		if (deviceEntity != null) {
			auth = deviceEntity.getAuth();
		}
		return (auth == true) ? "Y" : "N";
	}

}
