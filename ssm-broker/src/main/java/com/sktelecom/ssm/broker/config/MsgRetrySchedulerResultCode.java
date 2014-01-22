package com.sktelecom.ssm.broker.config;

public enum MsgRetrySchedulerResultCode {
	/*
	 * 99 : 서비스 오류 (Broker 내부 오류)
	 */
	FAIL_GENERAL_SERVICE(99),

	/**
	 * 98 : 재시도 횟수 n번 이상인 메세지 DEVICE_MSG에서 조회후 DEVICE_MSG_FAIL에 추가 실패
	 */
	FAIL_INSERT_DEVICE_MSG_FAIL(98),

	/**
	 * 97 : 재시도 횟수 n번 이상인 메세지 DEVICE_MSG에서 삭제 실패
	 */
	FAIL_DELETE_DEVICE_MSG(97),

	/**
	 * 96 : 재전송할 메세지 조회(등록시간이 n시간 경과한 메세지 n1개 조회) 실패
	 */
	FAIL_SELECT_PUSH_DATA_LIST(96),

	/**
	 * 95: retry 업데이트 실패
	 */
	FAIL_UPDATE_DEVICE_MSG_RETRY(95);

	private int resultCode;

	private MsgRetrySchedulerResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public int getResultCode() {
		return resultCode;
	}
}
