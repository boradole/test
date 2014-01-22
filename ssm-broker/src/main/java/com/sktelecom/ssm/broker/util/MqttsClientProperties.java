package com.sktelecom.ssm.broker.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class MqttsClientProperties {

	@Autowired
	private MessageSourceAccessor msAcc;

	private String WRITE_IDLE_SECOND = "WRITE_IDLE_SECOND";

	private String READ_TIMEOUT_SECOND = "READ_TIMEOUT_SECOND";

	public int getREAD_TIMEOUT_SECOND() {
		return Integer.valueOf(msAcc.getMessage(READ_TIMEOUT_SECOND));
	}

	public int getWRITE_IDLE_SECOND() {
		return Integer.valueOf(msAcc.getMessage(WRITE_IDLE_SECOND));
	}

}
