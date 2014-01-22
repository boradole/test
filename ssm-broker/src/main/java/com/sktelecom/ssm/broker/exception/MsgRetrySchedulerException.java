package com.sktelecom.ssm.broker.exception;

import com.sktelecom.ssm.broker.config.MsgRetrySchedulerResultCode;

public class MsgRetrySchedulerException extends BaseException {

	private static final long serialVersionUID = -9118655660339615873L;
	private MsgRetrySchedulerResultCode msgRetrySchedulerResultCode;

	public MsgRetrySchedulerException(String who, String where, MsgRetrySchedulerResultCode what, String how, Throwable cause) {
		super(who, where, "CODE:" + what, how, cause);
		this.msgRetrySchedulerResultCode = what;
	}

	public MsgRetrySchedulerException(String who, String where, MsgRetrySchedulerResultCode what, String how) {
		super(who, where, "CODE:" + what, how);
		this.msgRetrySchedulerResultCode = what;
	}

	public MsgRetrySchedulerException(String who, String where, MsgRetrySchedulerResultCode what) {
		super(who, where, "CODE:" + what);
		this.msgRetrySchedulerResultCode = what;
	}

	public MsgRetrySchedulerException(String who, String where, MsgRetrySchedulerResultCode what, Throwable cause) {
		super(who, where, "CODE:" + what, cause);
		this.msgRetrySchedulerResultCode = what;
	}

	public MsgRetrySchedulerResultCode msgRetrySchedulerResultCode() {
		return msgRetrySchedulerResultCode;
	}
}
