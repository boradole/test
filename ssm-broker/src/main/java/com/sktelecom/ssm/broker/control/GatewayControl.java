package com.sktelecom.ssm.broker.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sktelecom.ssm.broker.config.ConfigManager;
import com.sktelecom.ssm.broker.config.Constants;
import com.sktelecom.ssm.broker.config.GatewayResultCode;
import com.sktelecom.ssm.broker.exception.GatewayException;
import com.sktelecom.ssm.broker.external.mqttsclient.MqttsClient;
import com.sktelecom.ssm.broker.service.CommonService;
import com.sktelecom.ssm.broker.service.ConnectService;
import com.sktelecom.ssm.broker.util.ErrorLogUtil;
import com.sktelecom.ssm.broker.util.HttpSetResponse;
import com.sktelecom.ssm.util.StopWatch;

@Controller
public class GatewayControl {

	@Autowired
	private ConnectService connectService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ConfigManager configManager;

	private static Logger log = LoggerFactory.getLogger(GatewayControl.class);

	public static Map<String, MqttsClient> mqttsClientMap = new HashMap<String, MqttsClient>();

	/**
	 * 단말 접속 요청 인증
	 * 
	 * @date : 2013. 10. 21.
	 * 
	 * @param request
	 * @param response
	 * @param mac
	 *            Mac Address 번호 Ex)E7-9A-8F-CB-40-D8
	 * @param gwId
	 *            게이트웨이 ID Ex)1
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = Constants.GATEWAY_CONNECT)
	public void connect(HttpServletResponse response, @RequestParam(value = "mac", required = true, defaultValue = "") String mac,
			@RequestParam(value = "gwId", required = true, defaultValue = "") String gwId) throws Exception {

		String auth = connectService.isAuth(mac, gwId);

		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.element("regAuthorized", auth);
			jsonObj.element("expireTime", connectService.getExpireTime());
			log.info("regAuthorized:{}, ExpireTime:{}", auth, connectService.getExpireTime());
			HttpSetResponse.sendResponse(jsonObj.toString(), response);
		} catch (Exception e) {
			throw new GatewayException(mac, Constants.GATEWAY_CONNECT, GatewayResultCode.FAIL_GENERAL_SERVICE, e);
		}

	}

	/**
	 * 단말 접속해제 요청
	 * 
	 * @date : 2013. 10. 21.
	 * 
	 * @param request
	 * @param response
	 * @param mac
	 *            Mac Address 번호 Ex)E79A8FCB40D8
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = Constants.GATEWAY_DISCONNECT)
	public void disconnect(HttpServletResponse response, @RequestParam(value = "mac", required = false, defaultValue = "") String mac) throws Exception {
		try {
			connectService.deleteDevice(mac);
			HttpSetResponse.sendResponse("{}", response);
		} catch (Exception e) {
			throw new GatewayException(mac, Constants.GATEWAY_DISCONNECT, GatewayResultCode.FAIL_GENERAL_SERVICE, e);
		}
	}

	/**
	 * 메시지 수신 확인 요청
	 * 
	 * @date : 2013. 10. 21.
	 * 
	 * @param request
	 * @param response
	 * @param mac
	 *            Mac Address 번호 Ex)E7-9A-8F-CB-40-D8
	 * @param msgId
	 *            메시지 고유 식별자 Ex)20131017114358059000
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = Constants.GATEWAY_PUBACK)
	public void pubAck(HttpServletResponse response, @RequestParam(value = "mac", required = false, defaultValue = "") String mac,
			@RequestParam(value = "msgId", required = false, defaultValue = "") String msgId) throws Exception {

		try {
			String deviceId = commonService.getDeviceId(msgId, mac);
			if (deviceId != null) {
				commonService.putMsgSucQueue(msgId, deviceId);
			} else {
				throw new GatewayException(mac, Constants.GATEWAY_PUBACK, GatewayResultCode.FAIL_GENERAL_SERVICE, "getDeviceId Fail");
			}
			HttpSetResponse.sendResponse("{}", response);
		} catch (Exception e) {
			throw new GatewayException(mac, Constants.GATEWAY_PUBACK, GatewayResultCode.FAIL_GENERAL_SERVICE, e);
		}

	}

	/**
	 * 단말 접속 요청
	 * 
	 * @date : 2013. 10. 21.
	 * 
	 * @param request
	 * @param response
	 * @param mac
	 *            Mac Address 번호 Ex)E7-9A-8F-CB-40-D8
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = Constants.GATEWAY_GETAUTH)
	public void getAuth(HttpServletResponse response, @RequestParam(value = "mac", required = false, defaultValue = "") String mac) throws Exception {

		try {
			String auth = connectService.isAuth(mac);

			JSONObject jsonObj = new JSONObject();
			jsonObj.element("regAuthorized", auth);
			HttpSetResponse.sendResponse(jsonObj.toString(), response);
		} catch (Exception e) {
			throw new GatewayException(mac, Constants.GATEWAY_GETAUTH, GatewayResultCode.FAIL_GENERAL_SERVICE, e);
		}
	}

	/**
	 * 게이트웨이 접속 요청
	 * 
	 * @date : 2013. 11. 5.
	 * 
	 * @param request
	 * @param response
	 * @param gwIp
	 * @param gwPort
	 * @return 생성된 게이트웨이 ID
	 * @throws Exception
	 */
	@RequestMapping(value = Constants.GATEWAY_JOIN)
	public void join(HttpServletResponse response, @RequestParam(value = "ip", required = false, defaultValue = "") String gwIp,
			@RequestParam(value = "port", required = false, defaultValue = "") String gwPort) throws Exception {

		StopWatch sw = new StopWatch("join");
		sw.start();
		try {
			String gateWayId = commonService.joinGateway(gwIp, gwPort);
			sw.mark("joinGateway");

			MqttsClient mqttsClient = new MqttsClient(gwIp, Integer.valueOf(gwPort), "_broker_" + configManager.getBrokerKey());
			mqttsClient.connect();
			sw.mark("MqttsClient Connect");

			mqttsClientMap.put(gateWayId, mqttsClient);

			JSONObject jsonObj = new JSONObject();
			jsonObj.element("gwId", gateWayId);
			log.info("Set Response gwId Value : {} ", gateWayId);
			HttpSetResponse.sendResponse(jsonObj.toString(), response);

		} catch (Exception e) {
			throw new GatewayException(gwIp, Constants.GATEWAY_JOIN, GatewayResultCode.FAIL_GENERAL_SERVICE, e);
		}
		log.info(sw.toString());
	}

	@ExceptionHandler(GatewayException.class)
	public void handleCustomException(GatewayException ex, HttpServletResponse response) {
		try {
			ErrorLogUtil.logging(this.getClass().getName(), ex.getMessage(), ex, log);
			HttpSetResponse.sendResponse(String.valueOf(ex.getGatewayResultCode().getResultCode()), response);
		} catch (Exception e) {
			ErrorLogUtil.logging(this.getClass().getName(), e.getMessage(), e, log);
		}
	}

}
