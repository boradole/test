package com.sktelecom.ssm.broker.util;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;
import com.sktelecom.ssm.broker.entity.GatewayEntity;

@Service
public class GatewayParamCheck implements Validator {

	/*
	 * 검증 할 수 있는 Object Type인지 확인
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	/*
	 * supports 메소드를 통과한 경우에만 실행
	 */
	@Override
	public void validate(Object target, Errors errors) {
		if (target.toString().contains("GatewayEntity")) {
			GatewayEntity gwEntity = (GatewayEntity) target;
			//FIXME 가독성 좋게.
			if (gwEntity == null || "".equals(gwEntity.getGwIp())) {
				errors.rejectValue("gwIp", "gwIp value is null");
			}
			if (gwEntity == null || "".equals(gwEntity.getGwPort())) {
				errors.rejectValue("gwPort", "gwPort value is null");
			}
		} else if (target.toString().contains("DeviceEntity")) {
			DeviceEntity deviceEntity = (DeviceEntity) target;
			if (deviceEntity == null || "".equals(deviceEntity.getMac())) {
				errors.rejectValue("mac", "mac value is null");
			}
			if (deviceEntity == null || "".equals(deviceEntity.getGwId())) {
				errors.rejectValue("gwId", "gwId value is null");
			}
		} else if (target.toString().contains("DeviceMessageEntity")) {
			DeviceMessageEntity deviceMessageEntity = (DeviceMessageEntity) target;
			if (deviceMessageEntity == null || "".equals(deviceMessageEntity.getMsgId())) {
				errors.rejectValue("msgId", "msgId value is null");
			}
			if (deviceMessageEntity == null || "".equals(deviceMessageEntity.getDeviceEntity().getMac())) {
				errors.rejectValue("mac", "mac value is null");
			}
		} else {
			if (target == null || "".equals(target)) {
				errors.rejectValue("mac", "mac value is null");
			}
		}

	}

}
