package com.sktelecom.ssm.broker.exception;

import com.sktelecom.ssm.broker.config.GatewayResultCode;

public class GatewayException extends BaseException {

	private static final long serialVersionUID = -185714071735188159L;
	private GatewayResultCode gatewayResultCode;

	public GatewayException(String who, String where, GatewayResultCode what, String how, Throwable cause) {
		super(who, where, "CODE:" + what, how, cause);
		this.gatewayResultCode = what;
	}

	public GatewayException(String who, String where, GatewayResultCode what, String how) {
		super(who, where, "CODE:" + what, how);
		this.gatewayResultCode = what;
	}

	public GatewayException(String who, String where, GatewayResultCode what) {
		super(who, where, "CODE:" + what);
		this.gatewayResultCode = what;
	}

	public GatewayException(String who, String where, GatewayResultCode what, Throwable cause) {
		super(who, where, "CODE:" + what, cause);
		this.gatewayResultCode = what;
	}

	public GatewayResultCode getGatewayResultCode() {
		return gatewayResultCode;
	}
}
