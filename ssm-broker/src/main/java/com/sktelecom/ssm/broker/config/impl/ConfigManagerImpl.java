package com.sktelecom.ssm.broker.config.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sktelecom.ssm.broker.config.ConfigManager;
import com.sktelecom.ssm.broker.dao.BrokerDao;
import com.sktelecom.ssm.broker.entity.BrokerEntity;
import com.sktelecom.ssm.util.CommonUtil;

@Service
@Transactional
public class ConfigManagerImpl implements ConfigManager {

	private static Map<String, Integer> map = new HashMap<String, Integer>();

	@Autowired
	private BrokerDao brokerDao;

	@Override
	public void addBroker() {
		String regTm = CommonUtil.now();
		BrokerEntity brokerEntity = new BrokerEntity();
		brokerEntity.setRegTm(regTm);

		int brokerKey = brokerDao.insertBroker(brokerEntity);
		map.put("brokerKey", brokerKey);
	}

	@Override
	public int getBrokerKey() {
		return map.get("brokerKey");
	}

}
