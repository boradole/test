package com.sktelecom.ssm.broker.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(InitServlet.class);

	@Override
	public void init(ServletConfig sc) throws ServletException {
		log.info("==========================================");
		log.info(" BROKER SERVER STARTED!!!");
		log.info("==========================================");

	}

	@Override
	public void destroy() {
		log.info("==========================================");
		log.info(" BROKER SERVER STOPPED!!!");
		log.info("==========================================");
	}

}
