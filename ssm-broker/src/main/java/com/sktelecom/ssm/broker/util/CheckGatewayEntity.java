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
import com.sktelecom.ssm.broker.entity.GatewayEntity;
import com.sktelecom.ssm.broker.exception.GatewayException;

@Service
public class CheckGatewayEntity implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		GatewayEntity gwEntity = (GatewayEntity) target;
		// FIXME 가독성 좋게.
		if (gwEntity == null || "".equals(gwEntity.getGwIp())) {
			errors.rejectValue("gwIp", "gwIp value is null");
		}
		if (gwEntity == null || "".equals(gwEntity.getGwPort())) {
			errors.rejectValue("gwPort", "gwPort value is null");
		}
	}
	/**
	 * Gateway Join 시 parameter Check
	 * 
	 * @date : 2014. 1. 3.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean hasErrorGatewayJoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ip = (request.getParameter("ip") != null) ? request.getParameter("ip") : "";
		String port = (request.getParameter("port") != null) ? request.getParameter("port") : "";

		GatewayEntity gwEntity = new GatewayEntity();
		gwEntity.setGwIp(ip);
		gwEntity.setGwPort(port);
		BindingResult result = new BeanPropertyBindingResult(gwEntity, "gwEntity");

		//FIXME validatro 자동 검증(@valid)
		validate(gwEntity, result);

		if (!result.hasErrors()) {
			return false;
		} else {
			// gwIp null
			if (result.getFieldError("gwIp") != null) {
				throw new GatewayException(ip, Constants.GATEWAY_JOIN, GatewayResultCode.PARAMETER_VALUE_NULL, "gwIp is Null");
			}
			// gwPort null
			if (result.getFieldError("gwPort") != null) {
				throw new GatewayException(port, Constants.GATEWAY_JOIN, GatewayResultCode.PARAMETER_VALUE_NULL, "gwPort is Null");
			}
			HttpSetResponse.sendResponse("99", response);
			return true;
		}
	}
}
