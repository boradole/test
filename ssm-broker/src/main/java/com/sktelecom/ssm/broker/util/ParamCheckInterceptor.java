package com.sktelecom.ssm.broker.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sktelecom.ssm.broker.config.Constants;

@Service("paramCheckInterceptor")
public class ParamCheckInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private CheckGatewayEntity checkGatewayEntity;
	@Autowired
	private CheckDeviceEntity checkDeviceEntity;
	@Autowired
	private CheckDeviceMessageEntity checkDeviceMessageEntity;
	@Autowired
	private checkSenderDeviceEntity checkSnderDeviceEntity;
	@Autowired
	private CheckPushParamEntity checkPushParamEntity;
//	private static Map<String, String> sessionMap = new HashMap<String, String>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String uri = request.getRequestURI();

		if (uri.contains(Constants.CHECK_URI_JOIN)) {
			if (checkGatewayEntity.hasErrorGatewayJoin(request, response))
				return false;
		} else if (uri.contains(Constants.CHECK_URI_DISCONNECT)) {
			if (checkDeviceEntity.hasErrorGatewayDisconnect(request, response))
				return false;
		} else if (uri.contains(Constants.CHECK_URI_CONNECT)) {
			if (checkDeviceEntity.hasErrorGatewayConnect(request, response))
				return false;
		} else if (uri.contains(Constants.CHECK_URI_PUBACK)) {
			if (checkDeviceMessageEntity.hasErrorGatewayPubAck(request, response))
				return false;
		} else if (uri.contains(Constants.CHECK_URI_GETAUTH)) {
			if (checkDeviceEntity.hasErrorGatewayGetAuth(request, response))
				return false;
		} else if (uri.contains(Constants.URI_USERAUTH)) {
			if (checkSnderDeviceEntity.hasErrorSenderUserAuth(request, response))
				return false;
		} else if (uri.contains(Constants.URI_USERINFODELETE)) {
			if (checkSnderDeviceEntity.hasErrorSenderUserInfoDelete(request, response))
				return false;
		} else if (uri.contains(Constants.URI_REQPUSHDATA)) {
			if (checkPushParamEntity.hasErrorReqPushData(request, response))
				return false;
		}
		
		//FIXME 뭐야??... 이부분 함수로 빼기
		String mac = null;
		String sessionId = request.getSession().getId();
		if (uri.contains(Constants.CHECK_URI_CONNECT)) {
			mac = (request.getParameter("mac") != null) ? request.getParameter("mac") : "";

		}

		if (mac != null) {
//			sessionMap.put(sessionId, mac);
			MDC.put("sessionId", mac);
		}
		return super.preHandle(request, response, handler);
	}

}
