package com.sktelecom.ssm.broker.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HttpSetResponse {
	/**
	 * Response 설정
	 * 
	 * @date : 2013. 11. 14.
	 * 
	 * @param str
	 * @param response
	 * @throws Exception
	 */
	public static void sendResponse(String str, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("expires", "0");

		PrintWriter out = response.getWriter();

		out.print(str);
		out.flush();
		out.close();
	}
}
