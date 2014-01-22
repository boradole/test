package com.sktelecom.ssm.broker.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.sktelecom.ssm.broker.queue.QueueInterface;
import com.sktelecom.ssm.broker.queue.QueueService;
import com.sktelecom.ssm.broker.service.CommonService;

@Service
public class InsertMsgSucQueue {
	@Autowired
	private MessageSourceAccessor msAcc;

	// private static final int WAIT_TIME = 500;
	// private static final int INSERT_LIMIT = 100;

	private static Logger log = LoggerFactory.getLogger(InsertMsgSucQueue.class);

	@Autowired
	private QueueService queueSercie;

	@Autowired
	private CommonService commonService;

	static final BlockingQueue<Map<String, String>> answer = new LinkedBlockingQueue<Map<String, String>>();

	@Autowired
	public InsertMsgSucQueue(final MessageSourceAccessor msAcc) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean interrupted = false;
				int wait_Time = Integer.valueOf(msAcc.getMessage("WAIT_TIME"));
				int insert_Limit = Integer.valueOf(msAcc.getMessage("DB_LIMIT"));

				for (;;) {
					try {
						final Map<String, String> insertMsgSucMap = new HashMap<String, String>();
						insertMsgSucMap.putAll(answer.take());// queue에서 가져오기

						int i = 0;

						for (; i < insert_Limit; i++) {
							// poll : 지정된 시간까지 queue사용 못하도록 wait

							// Queue에 값이 없으면 0.5초 기다리고 있으면 바로 값 가져오기
							Map<String, String> val = answer.poll(wait_Time, TimeUnit.MILLISECONDS);

							if (val == null) {
								queueSercie.add(new QueueInterface("insertMsgSucQueue") {
									@Override
									public void doAsyncProcess() {
										commonService.addDeviceMsgSuc(insertMsgSucMap);
										commonService.delDeviceMsg(insertMsgSucMap);
									}
								});
								break;
							}
							insertMsgSucMap.putAll(val);

						}
						if (i > 0)
							log.debug("[InsertMsgSucQueue] poll Count : {} ", i + 1);
						if (interrupted) {
							Thread.currentThread().interrupt();
						}
					} catch (InterruptedException e) {
						interrupted = true;
						log.error("{}", e.getStackTrace());
					}
				}
			}
		}).start();
	}

	public void putData(String msgId, String deviceId) {
		Map<String, String> insertMap = new HashMap<String, String>(1);
		insertMap.put(msgId, deviceId);
		answer.offer(insertMap);
	}
}
