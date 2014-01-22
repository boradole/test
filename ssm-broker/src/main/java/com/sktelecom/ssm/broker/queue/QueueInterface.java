package com.sktelecom.ssm.broker.queue;

public abstract class QueueInterface {
    private long startTime;
    private String id;
    private String parameter;

    /**
     * @param id
     *            String 로그를 위한 식별자.(예:API규격번호 or 쿼리 네임 => 건 별로 유니크한 값 권장 )
     */
    public QueueInterface(String id) {
        this.id = id;
        this.setStartTime(System.currentTimeMillis());
    }

    /**
     * @param id
     *            String 로그를 위한 식별자.(예:API규격번호 or 쿼리 네임 => 건 별로 유니크한 값 권장 )
     * @param parameter
     *            실제 매개변수들
     */
    public QueueInterface(String id, String parameter) {
        this.id = id;
        this.setParameter(parameter);
        this.setStartTime(System.currentTimeMillis());
    }

    /**
     * 비동기 프로세스 실행
     */
    public abstract void doAsyncProcess();

    public String getId() {
        return id;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
