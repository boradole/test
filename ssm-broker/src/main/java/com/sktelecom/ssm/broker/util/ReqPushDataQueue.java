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

import com.sktelecom.ssm.broker.control.SenderControl;
import com.sktelecom.ssm.broker.entity.MessageEntity;
import com.sktelecom.ssm.broker.queue.QueueInterface;
import com.sktelecom.ssm.broker.queue.QueueService;

@Service
public class ReqPushDataQueue {
	@Autowired
	private MessageSourceAccessor msAcc;
	// private static final int WAIT_TIME = 500;
	// private static final int LIMIT = 1;

	private static Logger log = LoggerFactory.getLogger(ReqPushDataQueue.class);

	@Autowired
	private QueueService queueSercie;
	@Autowired
	private SenderControl senderControl;

	static final BlockingQueue<Map<String, MessageEntity>> answer = new LinkedBlockingQueue<Map<String, MessageEntity>>();

	@Autowired
	public ReqPushDataQueue(final MessageSourceAccessor msAcc) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean interrupted = false;
				int wait_Time = Integer.valueOf(msAcc.getMessage("WAIT_TIME"));
				int limit = Integer.valueOf(msAcc.getMessage("DB_LIMIT"));

				for (;;) {
					try {
						final Map<String, MessageEntity> reqPushDataMap = new HashMap<String, MessageEntity>();
						reqPushDataMap.putAll(answer.take());// queue에서 가져오기

						int i = 0;

						for (; i < limit; i++) {
							// poll : 지정된 시간까지 queue사용 못하도록 wait

							// Queue에 값이 없으면 0.5초 기다리고 있으면 바로 값 가져오기
							Map<String, MessageEntity> val = answer.poll(wait_Time, TimeUnit.MILLISECONDS);

							if (val == null) {
								queueSercie.add(new QueueInterface("ReqPushDataQueue") {
									@Override
									public void doAsyncProcess() {
										senderControl.requestPushDataQos1(reqPushDataMap);
									}
								});
								break;
							}
							reqPushDataMap.putAll(val);

						}
						if (i > 0)
							log.debug("[ReqPushDataQueue] poll Count : {} ", i + 1);
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

	public void putData(String recvMdns, MessageEntity messageEntity) {
		Map<String, MessageEntity> updateMap = new HashMap<String, MessageEntity>(1);
		updateMap.put(recvMdns, messageEntity);
		answer.offer(updateMap);
	}
}
