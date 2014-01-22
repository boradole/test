package com.sktelecom.ssm.broker.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sktelecom.ssm.broker.config.Constants;
import com.sktelecom.ssm.broker.config.GatewayResultCode;
import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.exception.GatewayException;

@Service
public class CheckDeviceEntity implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		DeviceEntity deviceEntity = (DeviceEntity) target;
		if (deviceEntity == null || "".equals(deviceEntity.getMac())) {
			errors.rejectValue("mac", "mac value is null");
		}
		if (deviceEntity == null || "".equals(deviceEntity.getGwId())) {
			errors.rejectValue("gwId", "gwId value is null");
		}
	}

	/**
	 * Gateway Disconnect 시 Parameter Check
	 * 
	 * @date : 2014. 1. 3.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean hasErrorGatewayDisconnect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mac = (request.getParameter("mac") != null) ? request.getParameter("mac") : "";

		DeviceEntity deviceEntity = new DeviceEntity();
		deviceEntity.setMac(mac);
		BindingResult result = new BeanPropertyBindingResult(deviceEntity, "deviceEntity");

		validate(deviceEntity, result);

		if (result.hasErrors()) {
			if (result.getFieldError("mac") != null) {
				throw new GatewayException(mac, Constants.GATEWAY_DISCONNECT, GatewayResultCode.PARAMETER_VALUE_NULL, "Mac is Null");
			}
			HttpSetResponse.sendResponse("99", response);
			return true;
		}

		return false;
	}

	/**
	 * Gateway Connect 시 Parameter Check
	 * 
	 * @date : 2014. 1. 3.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean hasErrorGatewayConnect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mac = (request.getParameter("mac") != null) ? request.getParameter("mac") : "";
		String gwId = (request.getParameter("gwId") != null) ? request.getParameter("gwId") : "";

		DeviceEntity deviceEntity = new DeviceEntity();
		deviceEntity.setMac(mac);
		deviceEntity.setGwId(gwId);
		BindingResult result = new BeanPropertyBindingResult(deviceEntity, "deviceEntity");

		validate(deviceEntity, result);

		if (result.hasErrors()) {
			if (result.getFieldError("mac") != null) {
				throw new GatewayException(mac, Constants.GATEWAY_CONNECT, GatewayResultCode.PARAMETER_VALUE_NULL, "Mac is Null");
			}
			if (result.getFieldError("gwId") != null) {
				throw new GatewayException(gwId, Constants.GATEWAY_CONNECT, GatewayResultCode.PARAMETER_VALUE_NULL, "gwId is Null");
			}
			HttpSetResponse.sendResponse("99", response);
			return true;
		}

		return false;
	}

	/**
	 * getAuth시 Parameter Check
	 * 
	 * @date : 2014. 1. 3.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean hasErrorGatewayGetAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mac = (request.getParameter("mac") != null) ? request.getParameter("mac") : "";

		DeviceEntity deviceEntity = new DeviceEntity();
		deviceEntity.setMac(mac);
		BindingResult result = new BeanPropertyBindingResult(deviceEntity, "deviceEntity");

		validate(deviceEntity, result);

		if (result.hasErrors()) {
			if (result.getFieldError("mac") != null) {
				throw new GatewayException(mac, Constants.GATEWAY_GETAUTH, GatewayResultCode.PARAMETER_VALUE_NULL, "Mac is Null");
			}
			HttpSetResponse.sendResponse("99", response);
			return true;
		}

		return false;
	}

}
