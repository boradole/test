package com.sktelecom.ssm.broker.util;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.entity.PushParamCheckEntity;

@Service
public class SenderParamCheck implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (target.toString().contains("DeviceEntity")) {
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
		} else if (target.toString().contains("PushParamCheckEntity")) {
			PushParamCheckEntity checkEntity = (PushParamCheckEntity) target;
			if (checkEntity == null || "".equals(checkEntity.getServiceId())) {
				errors.rejectValue("serviceId", "serviceId value is null");
			}
			if (checkEntity == null || "".equals(checkEntity.getSendMdn())) {
				errors.rejectValue("sendMdn", "sendMdn value is null");
			}
			if (checkEntity == null || "".equals(checkEntity.getRecvMdns())) {
				errors.rejectValue("recvMdns", "recvMdns value is null");
			}
			if (!"1".equals(checkEntity.getServiceId())) {
				errors.rejectValue("serviceIdEq1", "serviceId != 1");
			}
			if (checkEntity == null || "".equals(checkEntity.getData())) {
				errors.rejectValue("data", "data value is null");
			}
			if (checkEntity == null || "".equals(checkEntity.getQos())) {
				errors.rejectValue("qos", "qos value is null");
			}
		}
	}

}
