package com.sktelecom.ssm.broker.config;

public enum GatewayResultCode {
	/*
	 * 99 : 서비스 오류 (Broker 내부 오류)
	 */
	FAIL_GENERAL_SERVICE(99),
	/*
	 * 98 : parameter 값 불충분
	 */
	PARAMETER_VALUE_NULL(98);

	private int resultCode;

	private GatewayResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public int getResultCode() {
		return resultCode;
	}
}
