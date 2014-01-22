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
import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;
import com.sktelecom.ssm.broker.exception.GatewayException;

@Service
public class CheckDeviceMessageEntity implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		DeviceMessageEntity deviceMessageEntity = (DeviceMessageEntity) target;
		if (deviceMessageEntity == null || "".equals(deviceMessageEntity.getMsgId())) {
			errors.rejectValue("msgId", "msgId value is null");
		}
		if (deviceMessageEntity == null || "".equals(deviceMessageEntity.getDeviceEntity().getMac())) {
			errors.rejectValue("mac", "mac value is null");
		}
	}

	/**
	 * pubAck 시 Parameter Check
	 * 
	 * @date : 2014. 1. 3.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean hasErrorGatewayPubAck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mac = (request.getParameter("mac") != null) ? request.getParameter("mac") : "";
		String msgId = (request.getParameter("msgId") != null) ? request.getParameter("msgId") : "";

		DeviceMessageEntity deviceMessageEntity = new DeviceMessageEntity();
		DeviceEntity deviceEntity = new DeviceEntity();
		deviceEntity.setMac(mac);
		deviceMessageEntity.setMsgId(msgId);
		deviceMessageEntity.setDeviceEntity(deviceEntity);
		BindingResult result = new BeanPropertyBindingResult(deviceMessageEntity, "deviceMessageEntity");

		validate(deviceMessageEntity, result);

		if (result.hasErrors()) {
			if (result.getFieldError("mac") != null) {
				throw new GatewayException(mac, Constants.GATEWAY_PUBACK, GatewayResultCode.PARAMETER_VALUE_NULL, "Mac is Null");
			}
			if (result.getFieldError("msgId") != null) {
				throw new GatewayException(msgId, Constants.GATEWAY_PUBACK, GatewayResultCode.PARAMETER_VALUE_NULL, "msgId is Null");
			}
			HttpSetResponse.sendResponse("99", response);
			return true;
		}
		return false;
	}
}
