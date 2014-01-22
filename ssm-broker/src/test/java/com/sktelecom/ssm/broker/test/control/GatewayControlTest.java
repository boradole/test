package com.sktelecom.ssm.broker.test.control;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sktelecom.ssm.broker.control.GatewayControl;
import com.sktelecom.ssm.broker.dao.DeviceDao;
import com.sktelecom.ssm.broker.service.ConnectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/com/sktelecom/ssm/broker/config/application-context.xml",
		"classpath:/com/sktelecom/ssm/broker/config/schedulerInfo.xml", "file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml" })
public class GatewayControlTest {

	@Autowired
	private GatewayControl gatewayControl;

	@Autowired
	private ConnectService connectService;

	@Autowired
	DeviceDao deviceDao;

	@Test
	public void ConnectTest() throws InterruptedException {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		// MockHttpServletRequest mockHttpServletRequest = new
		// MockHttpServletRequest()
		try {
			gatewayControl.connect(mockHttpServletResponse, "B0D09CF2489C", "20131216102753674000_1023");
			// gatewayControl.connect(mockHttpServletResponse, "B0D09CF2489C",
			// "66", deviceEntity, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// gatewayControl.connect("E7-9A-8F-CB-40-D5", "55");
		Thread.sleep(2000);
	}

	//
	// @Test
	public void batchUpdateTest() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("E7-9A-8F-CB-40-D8", "11");
		map.put("E7-9A-8F-CB-40-D9", "22");
		// connectService.modAuthBatch(map);
	}

	// @Test
	public void gateJoin() {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		try {
			gatewayControl.join(mockHttpServletResponse, "", "10010");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void deleteDevice() {
		// gatewayControl.disconnect("TEST-MAC1");
	}

	// @Test
	public void getAuth() {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		try {
			gatewayControl.getAuth(mockHttpServletResponse, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void pubAckTest() {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		try {
			gatewayControl.pubAck(mockHttpServletResponse, "", "20131118171936017000");
			// gatewayControl.pubAck(mockHttpServletResponse, "B0D09CF2489C",
			// "20131118171936017000", deviceMessageEntity, result);
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void disconnect() {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		try {
			gatewayControl.disconnect(mockHttpServletResponse, "");
		} catch (Exception e) {
		}

	}

};
