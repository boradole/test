package com.sktelecom.ssm.broker.test.control;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sktelecom.ssm.broker.control.SenderControl;
import com.sktelecom.ssm.broker.exception.SenderException;
import com.sktelecom.ssm.broker.service.SenderService;
import com.sktelecom.ssm.broker.util.MsgRetryScheduler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/com/sktelecom/ssm/broker/config/application-context.xml",
		"classpath:/com/sktelecom/ssm/broker/config/schedulerInfo.xml", "file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml" })
public class SenderControlTest {

	@Autowired
	private SenderControl senderControl;
	@Autowired
	private SenderService senderService;

	 @Test
	public void userAuthCorrect() throws Exception {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		String mdn = "123";
		String mailAddr = "";
		String manu = "";
		String userPasswd = "";
		String mac = "B0D09CF2489C";
//		String mac = "d0578557e499";
		String serviceId = "1";
		String userName = "";
		String os = "";
		String osVer = "";
		try {
			senderControl.userAuth(mockHttpServletResponse, mdn, mac, serviceId, userName, userPasswd, mailAddr, manu, os, osVer);
		} catch (SenderException e) {
			senderControl.handleCustomException(e, mockHttpServletResponse);
		} finally {
			// DEVIE 삭제
			// MockHttpServletResponse delResponse = new
			// MockHttpServletResponse();
			// senderControl.userInfoDelete(delResponse, mdn, mac, serviceId,
			// userName, userPasswd, mailAddr, manu, os, osVer);
			// Assert.isTrue(delResponse.getContentAsString().equals(String.valueOf(SenderResultCode.SUCCESS.getResultCode())));
		}
	}

//	@Test
	public void requestPushData() throws Exception {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		String serviceId = "1";
		String sendMdn = "2222";
		String recvMdn = "d0578557e499";
		String data = "test1";
		String qos = "0";
		senderControl.requestPushData(mockHttpServletResponse, serviceId, sendMdn, recvMdn, data, qos);
//		Thread.sleep(300000);
	}

	// @Test
	public void getPushDataList() {
		senderService.getPushDataList("20131114161809807000");
	}

	// @Test
	public void userDelete() {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		String mdn = "123";
		String mailAddr = "";
		String manu = "";
		String userPasswd = "";
		String mac = "d0578557e490";
		String serviceId = "1";
		String userName = "";
		String os = "";
		String osVer = "";
		try {
			senderControl.userInfoDelete(mockHttpServletResponse, mdn, mac, serviceId, userName, userPasswd, mailAddr, manu, os, osVer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

};
