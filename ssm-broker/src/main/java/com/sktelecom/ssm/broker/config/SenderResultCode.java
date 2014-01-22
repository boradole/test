package com.sktelecom.ssm.broker.config;

public enum SenderResultCode {
    /**
     * 0 : 전송 성공
     */
    SUCCESS(0),
    /**
     * 10 : 전송 실패 (입력 parameter 오류 - service id)
     */
    FAIL_PARAM_SERVICE_ID(10),
    /**
     * 11 : 전송 실패 (입력 parameter 오류 - send_address)
     */
    FAIL_PARAM_SEND_ADDRESS(11),
    /**
     * 12 : 전송 실패 (입력 parameter 오류 - Recv_adress)
     */
    FAIL_PARAM_RECV_ADDRESS(12),
    /**
     * 13 : 전송 실패 (입력 parameter 오류 - Data)
     */
    FAIL_PARAM_DATA(13),
    /**
     * 14 : 전송 실패 (입력 parameter 오류 - QOS)
     */
    FAIL_PARAM_QOS(14),
    /**
     * 15 : 전송 실패 (사용자 없음)
     */
    FAIL_NOT_FOUND_USER(15),
    /**
     * 16 : 서비스 오류 (Broker 내부 오류)
     */
    FAIL_GENERAL_SERVICE(16),
    /**
     * 20 : MDN 또는 MAC 정보 없음
     */
    FAIL_PARAM_MDN_MAC(20),
    /**
     * 21 : 기존에 기등록 되어 있는 사용자를 다시 등록 하려고 할 경우
     */
    FAIL_AREADY_USER(21),
    /**
     * 17 : 접속 오류 (Broker 접속 오류)
     */
    FAIL_GENERAL(17),
    /**
     * 22 : Service id 가 sender 에 맞는 id(1) 이 아닌 경우
     */
    FAIL_SERVICE_ID(22), ;
    private int resultCode;

    private SenderResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }
}
