package com.sktelecom.ssm.broker.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sktelecom.ssm.broker.config.CommonResultCode;
import com.sktelecom.ssm.broker.config.ConfigManager;
import com.sktelecom.ssm.broker.exception.CommonException;

@Controller
public class CommonControl {

	@Autowired
	private ConfigManager configManager;

	/**
	 * broker 구동 시 DB에 broker 등록
	 * 
	 * @param configManager
	 */
	@Autowired
	public CommonControl(ConfigManager configManager) {
		try {
			configManager.addBroker();
		} catch (Exception e) {
			throw new CommonException(configManager.toString(), "Add Broker", CommonResultCode.FAIL_GENERAL_SERVICE, e);
		}
	}

}
