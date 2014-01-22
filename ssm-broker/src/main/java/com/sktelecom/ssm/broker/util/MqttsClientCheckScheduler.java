package com.sktelecom.ssm.broker.util;

import java.util.Map;
import java.util.Map.Entry;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.sktelecom.ssm.broker.control.GatewayControl;
import com.sktelecom.ssm.broker.external.mqttsclient.MqttsClient;

/**
 * Gateway가 Broker에 연결이 되면 Broker에서 Map(gwId,MqttsClient)을 생성하고, MqttsClient의
 * 세션이 끊키면 Map에서 지워주는 Scheduler
 * 
 * @date : 2013. 11. 28. 오전 10:42:56
 */
public class MqttsClientCheckScheduler extends QuartzJobBean {

	private static Logger log = LoggerFactory.getLogger(MqttsClientCheckScheduler.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		Map<String, MqttsClient> map = GatewayControl.mqttsClientMap;

		if (!map.isEmpty()) {

			log.info("======================================");
			log.info("[Mqtts Client Check Scheduler] Start");
			log.info("======================================");
			log.info("[MqttsClientMap] key: {}", map.keySet());

			for (Entry<String, MqttsClient> entry : map.entrySet()) {
				if (!entry.getValue().isOpen()) {
					map.remove(entry.getKey());
					log.info("[MqttsClientMap] remove MqttsClient: {}", entry.getKey());
				}
			}
			log.info("======================================");
			log.info("[Mqtts Client Check Scheduler] End");
			log.info("======================================");
		}

	}
}
