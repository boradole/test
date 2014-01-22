package com.sktelecom.ssm.broker.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sktelecom.ssm.broker.config.ConfigManager;
import com.sktelecom.ssm.broker.config.Constants;
import com.sktelecom.ssm.broker.config.SenderResultCode;
import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.exception.SenderException;
import com.sktelecom.ssm.util.CommonUtil;
import com.sktelecom.ssm.util.KeyGenerator;

@Service
public class checkSenderDeviceEntity implements Validator {

	@Autowired
	private ConfigManager configManager;

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
		if (deviceEntity == null || "".equals(deviceEntity.getMdn())) {
			errors.rejectValue("mdn", "mdn value is null");
		}
		if (deviceEntity == null || "".equals(deviceEntity.getServiceId())) {
			errors.rejectValue("serviceId", "serviceId value is null");
		}
		if (!"1".equals(deviceEntity.getServiceId())) {
			errors.rejectValue("serviceIdEq1", "serviceId != 1");
		}
	}

	/**
	 * userAuth 시 Parameter Check
	 * 
	 * @date : 2014. 1. 3.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean hasErrorSenderUserAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = KeyGenerator.getKey();
		int brokerKey = configManager.getBrokerKey();
		String deviceId = key + "_" + brokerKey;

		String mac = (request.getParameter("MAC") != null) ? request.getParameter("MAC") : "";
		String mailAddr = (request.getParameter("Mail_Addr") != null) ? request.getParameter("Mail_Addr") : "";
		String mdn = (request.getParameter("MDN") != null) ? request.getParameter("MDN") : "";
		String serviceId = (request.getParameter("ServiceID") != null) ? request.getParameter("ServiceID") : "";
		String userName = (request.getParameter("User_Name") != null) ? request.getParameter("User_Name") : "";
		String now = CommonUtil.now();

		DeviceEntity deviceEntity = new DeviceEntity();
		deviceEntity.setAuth(true);
		deviceEntity.setDeviceId(deviceId);
		deviceEntity.setMac(mac);
		deviceEntity.setMailAddr(mailAddr);
		deviceEntity.setMdn(mdn);
		deviceEntity.setRegTm(now);
		deviceEntity.setServiceId(serviceId);
		deviceEntity.setUpTm(now);
		deviceEntity.setUserNm(userName);
		BindingResult result = new BeanPropertyBindingResult(deviceEntity, "deviceEntity");

		validate(deviceEntity, result);

		if (result.hasErrors()) {
			if (result.getFieldError("mac") != null || result.getFieldError("mdn") != null) {
				throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_PARAM_MDN_MAC, String.format("mdn:{},mac:{}", mdn, mac));
			}
			if (result.getFieldError("serviceIdEq1") != null) {
				throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_SERVICE_ID, String.format("serviceId:{}", serviceId));
			}
			if (result.getFieldError("serviceId") != null) {
				throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_PARAM_SERVICE_ID);
			}
			HttpSetResponse.sendResponse("99", response);
			return true;
		}
		return false;
	}

	/**
	 * UserInfoDelete 시 Parameter Check
	 * 
	 * @date : 2014. 1. 3.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean hasErrorSenderUserInfoDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mac = (request.getParameter("MAC") != null) ? request.getParameter("MAC") : "";
		String mdn = (request.getParameter("MDN") != null) ? request.getParameter("MDN") : "";
		String serviceId = (request.getParameter("ServiceID") != null) ? request.getParameter("ServiceID") : "";

		DeviceEntity deviceEntity = new DeviceEntity();
		deviceEntity.setMdn(mdn);
		deviceEntity.setMac(mac);
		deviceEntity.setServiceId(serviceId);
		BindingResult result = new BeanPropertyBindingResult(deviceEntity, "deviceEntity");

		validate(deviceEntity, result);
		if (result.hasErrors()) {
			if (result.getFieldError("mac") != null || result.getFieldError("mdn") != null) {
				throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_PARAM_MDN_MAC, String.format("mdn:{},mac:{}", mdn, mac));
			}
			if (result.getFieldError("serviceIdEq1") != null) {
				throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_SERVICE_ID, String.format("serviceId:{}", serviceId));
			}
			if (result.getFieldError("serviceId") != null) {
				throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_PARAM_SERVICE_ID);
			}
			HttpSetResponse.sendResponse("99", response);
			return true;
		}
		return false;
	}

}
