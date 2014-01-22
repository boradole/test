package com.sktelecom.ssm.broker.config;

public enum CommonResultCode {
	/*
	 * 99 : 서비스 오류 (Broker 내부 오류)
	 */
	FAIL_GENERAL_SERVICE(99), ;

	private int resultCode;

	private CommonResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public int getResultCode() {
		return resultCode;
	}
}
