package com.sktelecom.ssm.broker.exception;

import com.sktelecom.ssm.broker.config.SenderResultCode;

public class SenderException extends BaseException {

	private static final long serialVersionUID = -244727704568196242L;
	private SenderResultCode senderResultCode;

	public SenderException(String who, String where, SenderResultCode what, String how, Throwable cause) {
		super(who, where, "CODE:" + what, how, cause);
		this.senderResultCode = what;
	}

	public SenderException(String who, String where, SenderResultCode what, String how) {
		super(who, where, "CODE:" + what, how);
		this.senderResultCode = what;
	}

	public SenderException(String who, String where, SenderResultCode what) {
		super(who, where, "CODE:" + what);
		this.senderResultCode = what;
	}

	public SenderException(String who, String where, SenderResultCode what, Throwable cause) {
		super(who, where, "CODE:" + what, cause);
		this.senderResultCode = what;
	}

	public SenderResultCode getSenderResultCode() {
		return senderResultCode;
	}
}
