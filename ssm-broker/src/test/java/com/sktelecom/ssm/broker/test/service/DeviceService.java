package com.sktelecom.ssm.broker.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sktelecom.ssm.broker.config.ConfigManager;
import com.sktelecom.ssm.broker.service.CommonService;
import com.sktelecom.ssm.broker.service.ConnectService;
import com.sktelecom.ssm.util.CommonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/com/sktelecom/ssm/broker/config/application-context.xml",
		"classpath:/com/sktelecom/ssm/broker/config/schedulerInfo.xml", "file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml" })
public class DeviceService {

	@Autowired
	private ConnectService connectService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ConfigManager configManager;

	@Test
	public void notAuthConnect() throws InterruptedException {
		connectService.isAuth("E7-9A-8F-CB-40-D7", "13");
		Thread.sleep(5000);
	}

	// @Test
	public void configManager() {
		// configManager.addBroker();
	}

	// @Test
	public void getPushDataListOverTime() {
		String now = CommonUtil.now();
		int selectCnt = 108;
		commonService.getPushDataListOverTime(now, selectCnt);
	}
}
