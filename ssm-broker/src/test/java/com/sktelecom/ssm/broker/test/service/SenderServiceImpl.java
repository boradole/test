package com.sktelecom.ssm.broker.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sktelecom.ssm.broker.service.SenderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/com/sktelecom/ssm/broker/config/application-context.xml",
		"classpath:/com/sktelecom/ssm/broker/config/schedulerInfo.xml", "file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml" })
public class SenderServiceImpl {

	@Autowired
	private SenderService senderService;

	@Test
	public void deviceIdTest() {
//		senderService.getDeviceId("B0D09CF2489C");
		senderService.getDeviceIds("B0D09CF2489C,d0578557e499");
	}

}
