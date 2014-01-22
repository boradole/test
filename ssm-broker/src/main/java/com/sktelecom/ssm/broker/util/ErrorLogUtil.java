package com.sktelecom.ssm.broker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorLogUtil {
	/**
	 * ERROR_TRACE는 log4j.properties에 설정하지 않는다. ERROR_TRACE는 info 로그에는 남기지 않고
	 * error 로그에만 남기기위한 설정이다. log4j.properties에 설정되어 있지 않기 때문에 다른 어느곳에서도 노출되지 않고
	 * log4j.rootLogger 에서만 ERROR 로 노출된다.
	 */
	private static Logger errorLogger = LoggerFactory.getLogger("ERROR_TRACE");

	public static void logging(String layer, String errorMessage, Throwable e, Logger logger) {
		final String msg = "[" + layer + "] " + errorMessage;
		logger.error(msg + (e != null ? ":" + e.getMessage() : ""));
		errorLogger.error(msg, e);
	}

	public static void logging(String layer, String errorMessage, Throwable e) {
		final String msg = "[" + layer + "] " + errorMessage;
		errorLogger.error(msg, e);
	}

	public static void logging(String layer, String errorMessage, Logger logger) {
		final String msg = "[" + layer + "] " + errorMessage;
		logger.error(msg);
	}
}
