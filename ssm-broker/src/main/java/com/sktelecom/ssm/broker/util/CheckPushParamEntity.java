package com.sktelecom.ssm.broker.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sktelecom.ssm.broker.config.Constants;
import com.sktelecom.ssm.broker.config.SenderResultCode;
import com.sktelecom.ssm.broker.entity.PushParamCheckEntity;
import com.sktelecom.ssm.broker.exception.SenderException;

@Service
public class CheckPushParamEntity implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
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

	/**
	 * reqPushData 시 Parameter Check
	 * 
	 * @date : 2014. 1. 3.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean hasErrorReqPushData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serviceId = (request.getParameter("ServiceID") != null) ? request.getParameter("ServiceID") : "";
		String sendMdn = (request.getParameter("Send_mdn") != null) ? request.getParameter("Send_mdn") : "";
		String recvMdns = (request.getParameter("Recv_mdn") != null) ? request.getParameter("Recv_mdn") : "";
		String data = (request.getParameter("Data") != null) ? request.getParameter("Data") : "";
		String qos = (request.getParameter("Qos") != null) ? request.getParameter("Qos") : "";

		PushParamCheckEntity checkEntity = new PushParamCheckEntity();
		checkEntity.setServiceId(serviceId);
		checkEntity.setSendMdn(sendMdn);
		checkEntity.setRecvMdns(recvMdns);
		checkEntity.setData(data);
		checkEntity.setQos(qos);
		BindingResult result = new BeanPropertyBindingResult(checkEntity, "deviceEntity");

		validate(checkEntity, result);

		if (result.hasErrors()) {
			if (result.getFieldError("serviceId") != null) {
				throw new SenderException(serviceId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_PARAM_SERVICE_ID);
			}
			if (result.getFieldError("serviceIdEq1") != null) {
				throw new SenderException(serviceId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_SERVICE_ID, String.format("serviceId:{}", serviceId));
			}
			if (result.getFieldError("recvMdns") != null) {
				throw new SenderException(serviceId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_PARAM_RECV_ADDRESS);
			}
			if (result.getFieldError("sendMdn") != null) {
				throw new SenderException(serviceId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_PARAM_SEND_ADDRESS);
			}
			if (result.getFieldError("qos") != null) {
				throw new SenderException(serviceId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_PARAM_QOS);
			}
			if (result.getFieldError("data") != null) {
				throw new SenderException(serviceId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_PARAM_DATA);
			}
			HttpSetResponse.sendResponse(String.valueOf(SenderResultCode.SUCCESS.getResultCode()), response);
			return true;
		}
		return false;
	}
}
