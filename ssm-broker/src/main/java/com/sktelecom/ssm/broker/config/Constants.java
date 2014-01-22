package com.sktelecom.ssm.broker.config;

public class Constants {

	/*
	 * sender <-> broker
	 */
	public static final String URL_USER_AUTH = "/UserAuth";
	public static final String URL_USER_INFO_DELETE = "/UserInfoDelete";
	public static final String REQUEST_PUSH_DATA = "/reqPushData";

	/*
	 * Gateway <-> broker
	 */
	public static final String GATEWAY_CONNECT = "/connect";
	public static final String GATEWAY_DISCONNECT = "/disconnect";
	public static final String GATEWAY_PUBACK = "/puback";
	public static final String GATEWAY_GETAUTH = "/getAuth";
	public static final String GATEWAY_JOIN = "/join";

	/*
	 * gateway Validation Check
	 */
	public static final String CHECK_URI_JOIN = "join";
	public static final String CHECK_URI_DISCONNECT = "disconnect";
	public static final String CHECK_URI_CONNECT = "connect";
	public static final String CHECK_URI_PUBACK = "puback";
	public static final String CHECK_URI_GETAUTH = "getAuth";

	/*
	 * sender Validation Check
	 */
	public static final String URI_USERAUTH = "UserAuth";
	public static final String URI_USERINFODELETE = "UserInfoDelete";
	public static final String URI_REQPUSHDATA = "reqPushData";

}
