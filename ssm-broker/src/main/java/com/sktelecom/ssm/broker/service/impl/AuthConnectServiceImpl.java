package com.sktelecom.ssm.broker.service.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sktelecom.ssm.broker.dao.DeviceDao;
import com.sktelecom.ssm.broker.dao.GatewayDao;
import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.service.ConnectService;
import com.sktelecom.ssm.broker.util.InsertMsgSucQueue;
import com.sktelecom.ssm.broker.util.UpdateQueue;

@Transactional
public class AuthConnectServiceImpl implements ConnectService {

	private static Logger log = LoggerFactory.getLogger(AuthConnectServiceImpl.class);

	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private GatewayDao gatewayDao;

	@Autowired
	private InsertMsgSucQueue insertMsgSucQueue;

	@Autowired
	private UpdateQueue updateQueue;

	/**
	 * <pre>
	 * Description: 인증 단말 처리
	 * </pre>
	 * 
	 * @date : 2013. 10. 25.
	 * @param deviceDomain
	 * 
	 * @return Auth , MAC
	 */
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public String isAuth(String mac, String gwId) {

		// 인증된 단말일 경우, update Queue 추가
		if (deviceDao.selectDevice(mac) != null) {
			// Queue 삽입
			updateQueue.putData(gwId, mac);
			return "Y";
		} else {
			return "N";
		}

	}

	@Override
	public void modifyAuthForBatch(Map<String, String> updateMap) {
		for (Entry<String, String> entry : updateMap.entrySet()) {
			deviceDao.updateGatewayInfo(entry.getValue(), entry.getKey());
		}
	}

	@Override
	public void addDeviceForBatch(Map<String, String> insertMap) {
	}

	@Override
	public int getExpireTime() {
		return 0;
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
