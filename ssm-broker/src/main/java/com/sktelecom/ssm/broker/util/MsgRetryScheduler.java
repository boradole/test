package com.sktelecom.ssm.broker.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.sktelecom.ssm.broker.config.MsgRetrySchedulerResultCode;
import com.sktelecom.ssm.broker.control.GatewayControl;
import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;
import com.sktelecom.ssm.broker.exception.MsgRetrySchedulerException;
import com.sktelecom.ssm.broker.external.mqttsclient.MqttsClient;
import com.sktelecom.ssm.broker.service.CommonService;
import com.sktelecom.ssm.util.CommonUtil;

/**
 * 메세지 제전송 스케줄러 
 * 1. 재시도 횟수 3이상인 메세지들을 DEVICE_MSG_FAIL 테이블에 저장 
 * 2. 재시도 횟수 3이상인 메세지들 삭제 DEVICE_MSG 
 * 3. 재전송할 메세지 조회(등록시간이 n시간 경과한 메세지 n1개 조회) 
 * 4. 조회한 메세지 목록 업데이트(재시도 횟수 +1)
 * 
 * @date : 2013. 11. 21. 오후 2:40:38
 */
public class MsgRetryScheduler extends QuartzJobBean {

	private static Logger log = LoggerFactory.getLogger(MsgRetryScheduler.class);

	// key: g/w ID , value : payLoad(MsgId | Mac)
	private static Map<String, String> subMap = new HashMap<String, String>();
	// key : Send Data , value : subMap
	private static Map<String, Map<String, String>> mainMap = new HashMap<String, Map<String, String>>();

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		log.info("=================================");
		log.info("[Message Retry Scheduler] Start");
		log.info("=================================");

		MessageSourceAccessor msAcc = (MessageSourceAccessor) context.getJobDetail().getJobDataMap().get("msAcc");

		int retryCnt = Integer.valueOf(msAcc.getMessage("RETRY_CNT"));
		String now = CommonUtil.now();
		int selectCnt = Integer.valueOf(msAcc.getMessage("SELECT_CNT"));

		CommonService commonService = (CommonService) context.getJobDetail().getJobDataMap().get("commonServiceImpl");

		if (GatewayControl.mqttsClientMap.isEmpty()) {
			log.info("Not connected Gateway");
		} else {

			// 1.재시도 횟수 n번 이상인 메세지 DEVICE_MSG에서 조회후 DEVICE_MSG_FAIL에 추가
			addDeviceMsg(retryCnt, commonService);

			// 2.재시도 횟수 n번 이상인 메세지 DEVICE_MSG에서 삭제
			delDeviceMsg(retryCnt, commonService);

			List<DeviceMessageEntity> pushDataList = null;
			// 3.재전송할 메세지 조회(등록시간이 n시간 경과한 메세지 n1개 조회)
			pushDataList = getPushDataList(now, selectCnt, pushDataList, commonService);

			try {
				for (DeviceMessageEntity deviceMessageEntity : pushDataList) {

					// 4.재전송할 메세지의 retryCnt 수 +1
					modDeviceMsgRetryCnt(deviceMessageEntity, commonService);

					String sendData = commonService.getDeviceMessage(deviceMessageEntity.getMsgId());
					String msgId = deviceMessageEntity.getMsgId();
					String mac = deviceMessageEntity.getDeviceEntity().getMac();
					String gwId = deviceMessageEntity.getDeviceEntity().getGwId();

					String subValue = msgId + "|" + mac;

					if (mainMap.isEmpty()) {
						log.debug("[init Map] sendData: {} , MsgId: {}, mac: {}, gwId: {}", new Object[] { sendData, msgId, mac, gwId });
						subMap.put(gwId, subValue);
						mainMap.put(sendData, subMap);
					} else {
						// Key : g/w ID , value : MsgId | mac
						Map<String, String> subTempMap = new HashMap<String, String>();
						for (String mainMapKey : mainMap.keySet()) {
							subTempMap = mainMap.get(mainMapKey);
							// 1. 보낼 Data가 같을 경우
							if (mainMapKey.contains(sendData)) {
								// if (sendData.equals(mainMapKey)) {
								for (String subMapKey : subTempMap.keySet()) {
									// 2. gwID 같을 경우
									if (subMapKey != null && subMapKey.contains(gwId)) {
										/*
										 * 보낼 Data,gwId 가 같고 mac 이 다를경우 subMap
										 * Value 값 (MsgId|mac)에 mac 추가 후
										 * mainMap에 저장
										 */
										if (!subTempMap.get(subMapKey).contains(mac)) {
											log.debug("data,gwId 같고 mac이 다를 경우");
											String subMapValue = subTempMap.get(subMapKey) + "," + mac;
											mainMap.remove(mainMapKey);
											subMap.remove(subMapKey);
											subMap.put(subMapKey, subMapValue);
											mainMap.put(sendData, subMap);
										}
									} else {
										// gwId 다르고 mac도 다를 경우
										/*
										 * 보낼 Data는 같고 gwId ,mac이 다를 경우, subMap
										 * Key(gwId)에 새로운 gwId더하고
										 * subMapValue(MsgId,mac)에 새로운 mac 추가
										 */
										if (!subTempMap.get(subMapKey).contains(mac)) {
											log.debug("data같고 gwid 다르고 mac이 다를 경우");
											String subMapValue = subTempMap.get(subMapKey);
											String newSubMapKey = subMapKey + "," + gwId;
											String[] split = subMapValue.split("\\|");
											split[1] += "," + mac;

											mainMap.remove(mainMapKey);
											subMap.remove(subMapKey);
											subMap.put(newSubMapKey, split[0] + split[1]);
											mainMap.put(sendData, subMap);
										}
									}
								}// end for
							} else {
								// 보낼 Data가 다를 경우

								for (String subMapKey : subTempMap.keySet()) {

									// gwId 같을 경우
									// if (subTempMap.equals(gwId)) {
									if (subMapKey.contains(gwId)) {
										/*
										 * 보낼 Data 다르고 , gwId,mac 같을 경우
										 * mainMapKey(sendData)에 새로운 Data를 더하고
										 * subMapValue(MsgId | mac)에 새로운 MsgId를
										 * 더한다.
										 */
										if (subTempMap.get(subMapKey).contains(mac)) {
											log.debug("data다르고 gwid 같고 mac이 같을 경우");
											String newMainMapKey = mainMapKey + "," + sendData;
											String subTempMapValue = subTempMap.get(subMapKey);
											String[] split = subTempMapValue.split("\\|");
											split[0] += "," + msgId;

											mainMap.remove(mainMapKey);
											subMap.remove(subMapKey);
											subMap.put(subMapKey, split[0] + "|" + split[1]);
											mainMap.put(newMainMapKey, subMap);
										} else {
											/*
											 * 보낼 Data,mac이 다르고 ,gwId 같을 경우
											 * mainMapKey(sendData)에 새로운 Data를
											 * 더하고, subMapValue(MsgId,mac)에
											 * MsgId와 mac을 더한다.
											 */
											log.debug("data다르고 gwid 같고 mac다를 경우");
											String newMainMapKey = mainMapKey + "," + sendData;
											String subTempMapValue = subTempMap.get(subMapKey);
											String[] split = subTempMapValue.split("\\|");
											split[0] += "," + msgId;
											split[1] += "," + mac;
											mainMap.remove(mainMapKey);
											subMap.remove(subMapKey);
											subMap.put(subMapKey, split[0] + "|" + split[1]);
											mainMap.put(newMainMapKey, subMap);
										}
									} else {
										/*
										 * gwId 다르고 mac도 다를 경우,
										 * mainMapKey(sendData)에 새로운 data를 더하고,
										 * subMapKey(gwId)에 새로운
										 * gwId더하고,subMapValue(MsgId | mac)에 새로운
										 * MsgId,mac을 더한다.
										 */
										if (!subTempMap.get(subMapKey).contains(mac)) {
											log.debug("data다르고 gwid 다르고 mac이 다를 경우");
											String newMainMapKey = mainMapKey + "," + sendData;
											String subTempMapValue = subTempMap.get(subMapKey);
											String[] split = subTempMapValue.split("\\|");
											split[0] += "," + msgId;
											split[1] += "," + mac;
											String newSubMapKey = subMapKey + "," + gwId;

											mainMap.remove(mainMapKey);
											subMap.remove(subMapKey);
											subMap.put(newSubMapKey, split[0] + "|" + split[1]);
											mainMap.put(newMainMapKey, subMap);
										}
									}
								}
							}// end else 보낼 data가 다를 경우
						}// end for

					}// end else (map관련 작업)
				}// end for

				Map<String, String> payLoadMap = new HashMap<String, String>();
				String tmpPayLoad = null;

				for (String mainMapKey : mainMap.keySet()) {

					// 보낼 Data (test,test1 ....)가 여러개
					if (mainMapKey.contains(",")) {
						String[] sendDatas = mainMapKey.split(",");

						for (String sendData : sendDatas) {
							for (String gwId : mainMap.get(mainMapKey).keySet()) {
								String msgsId = mainMap.get(mainMapKey).get(gwId).split("\\|")[0];
								String macs = mainMap.get(mainMapKey).get(gwId).split("\\|")[1];

								/*
								 * payLoadMap(msgId,msgId|mac|data) 다른 msgId가 있을
								 * 경우에만 map에 put
								 */
								if (msgsId.contains(",")) {
									String[] msgIds = msgsId.split(",");

									for (String msgId : msgIds) {
										tmpPayLoad = msgId + "|" + macs + "|" + sendData;

										if (payLoadMap.isEmpty()) {
											payLoadMap.put(msgId, tmpPayLoad);
										} else {
											if (!payLoadMap.containsKey(msgId)) {
												for (String key : payLoadMap.keySet()) {
													if (!payLoadMap.get(key).contains(sendData)) {
														payLoadMap.put(msgId, tmpPayLoad);
													}
												}
											}
										}// end else
									} // end for
								} // end else
							}// end for
						}
					} else {
					}
				}

				/*
				 * gw에 연결된 mac에 대해서 publish 진행
				 */
				for (String subMapKey : subMap.keySet()) {
					String mapValue = subMap.get(subMapKey);
					String mac = null;
					if (mapValue != null) {
						mac = (mapValue.split("\\|") != null) ? mapValue.split("\\|")[1] : "";
					} else {
						throw new MsgRetrySchedulerException("payLoad(MsgId | Mac) is Null", "MessageRetryScheduler",
								MsgRetrySchedulerResultCode.FAIL_GENERAL_SERVICE);
					}

					if (mac.contains(",")) {
						mac = mac.substring(0, mac.lastIndexOf(","));
					}

					if (GatewayControl.mqttsClientMap.containsKey(subMapKey)) {
						MqttsClient mqttsClient = GatewayControl.mqttsClientMap.get(subMapKey);
						for (String payload : payLoadMap.keySet()) {
							log.debug("mac:{} , payLoad:{}", mac, payload);
							if (payLoadMap.get(payload).contains(mac)) {
								mqttsClient.publishQos0(Short.parseShort("1"), Short.parseShort("0"), payLoadMap.get(payload));
							}
						}
					}
				}

			} catch (Exception e) {
				throw new MsgRetrySchedulerException("make PayLoadData Fail", "MessageRetryScheduler", MsgRetrySchedulerResultCode.FAIL_GENERAL_SERVICE, e);
			}
		}

		log.info("=================================");
		log.info("[Message Retry Scheduler] End");
		log.info("=================================");
	}

	private void modDeviceMsgRetryCnt(DeviceMessageEntity deviceMessageEntity, CommonService commonService) {
		try {
			// retry +1
			commonService.modDeviceMsgRetryCnt(deviceMessageEntity.getMsgId(), deviceMessageEntity.getDeviceId(), deviceMessageEntity.getRetryCnt() + 1);
		} catch (Exception e) {
			throw new MsgRetrySchedulerException("modDeviceMsgRetryCnt", "MessageRetryScheduler", MsgRetrySchedulerResultCode.FAIL_UPDATE_DEVICE_MSG_RETRY, e);
		}
	}

	private List<DeviceMessageEntity> getPushDataList(String now, int selectCnt, List<DeviceMessageEntity> pushDataList, CommonService commonService) {
		try {
			pushDataList = commonService.getPushDataListOverTime(now, selectCnt);
		} catch (Exception e) {
			throw new MsgRetrySchedulerException("getPushDataListOverTime", "MessageRetryScheduler", MsgRetrySchedulerResultCode.FAIL_SELECT_PUSH_DATA_LIST, e);
		}
		return pushDataList;
	}

	private void delDeviceMsg(int retryCnt, CommonService commonService) {
		try {
			commonService.delDeviceMsgOverCnt(retryCnt);
		} catch (Exception e) {
			throw new MsgRetrySchedulerException("delDeviceMsgOverCnt", "MessageRetryScheduler", MsgRetrySchedulerResultCode.FAIL_DELETE_DEVICE_MSG, e);
		}
	}

	private void addDeviceMsg(int retryCnt, CommonService commonService) {
		try {
			commonService.addDeviceMsgOverRetryCnt(retryCnt);
		} catch (Exception e) {
			throw new MsgRetrySchedulerException("addDeviceMsgOverRetryCnt", "MessageRetryScheduler", MsgRetrySchedulerResultCode.FAIL_INSERT_DEVICE_MSG_FAIL,
					e);
		}
	}
}
