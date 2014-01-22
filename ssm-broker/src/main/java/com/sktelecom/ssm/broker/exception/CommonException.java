package com.sktelecom.ssm.broker.exception;

import com.sktelecom.ssm.broker.config.CommonResultCode;

public class CommonException extends BaseException {

	private static final long serialVersionUID = 380205181450598802L;
	private CommonResultCode commonResultCode;

	public CommonException(String who, String where, CommonResultCode what, String how, Throwable cause) {
		super(who, where, "CODE:" + what, how, cause);
		this.commonResultCode = what;
	}

	public CommonException(String who, String where, CommonResultCode what, String how) {
		super(who, where, "CODE:" + what, how);
		this.commonResultCode = what;
	}

	public CommonException(String who, String where, CommonResultCode what) {
		super(who, where, "CODE:" + what);
		this.commonResultCode = what;
	}

	public CommonException(String who, String where, CommonResultCode what, Throwable cause) {
		super(who, where, "CODE:" + what, cause);
		this.commonResultCode = what;
	}

	public CommonResultCode getCommonResultCode() {
		return commonResultCode;
	}
}
