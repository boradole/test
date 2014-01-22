package com.sktelecom.ssm.broker.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sktelecom.ssm.broker.config.ConfigManager;
import com.sktelecom.ssm.broker.config.Constants;
import com.sktelecom.ssm.broker.config.SenderResultCode;
import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;
import com.sktelecom.ssm.broker.entity.MessageEntity;
import com.sktelecom.ssm.broker.exception.SenderException;
import com.sktelecom.ssm.broker.external.mqttsclient.MqttsClient;
import com.sktelecom.ssm.broker.service.CommonService;
import com.sktelecom.ssm.broker.service.ConnectService;
import com.sktelecom.ssm.broker.service.SenderService;
import com.sktelecom.ssm.broker.util.ErrorLogUtil;
import com.sktelecom.ssm.broker.util.HttpSetResponse;
import com.sktelecom.ssm.broker.util.ReqPushDataQueue;
import com.sktelecom.ssm.util.CommonUtil;
import com.sktelecom.ssm.util.KeyGenerator;

@Controller
public class SenderControl {

	@Autowired
	private ConnectService connectService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ConfigManager configManager;
	@Autowired
	private SenderService senderService;
	@Autowired
	private GatewayControl gatewayContol;
	@Autowired
	private ReqPushDataQueue reqPushDataQueue;

	private static Logger log = LoggerFactory.getLogger(SenderControl.class);

	/**
	 * 가입자 Insert 요청
	 * 
	 * @param request
	 * @param response
	 * @param mdn
	 *            신규가입자 전화번호
	 * @param mac
	 *            MAC address 번호
	 * @param serviceId
	 *            서비스 및 Group 식별 번호
	 * @param userName
	 *            사용자 이름
	 * @param userPasswd
	 *            사용자 패스워드
	 * @param mailAddr
	 *            사용자 메일 주소
	 * @param manu
	 *            단말 제조사
	 * @param os
	 *            단말 OS 명
	 * @param osVer
	 *            단말 OS 버전
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = Constants.URL_USER_AUTH)
	public void userAuth(HttpServletResponse response, @RequestParam(value = "MDN", required = false, defaultValue = "") String mdn,
			@RequestParam(value = "MAC", required = false, defaultValue = "") String mac,
			@RequestParam(value = "ServiceID", required = false, defaultValue = "") String serviceId,
			@RequestParam(value = "User_Name", required = false, defaultValue = "") String userName,
			@RequestParam(value = "User_Passwd", required = false, defaultValue = "") String userPasswd,
			@RequestParam(value = "Mail_Addr", required = false, defaultValue = "") String mailAddr,
			@RequestParam(value = "Manu", required = false, defaultValue = "") String manu,
			@RequestParam(value = "OS", required = false, defaultValue = "") String os,
			@RequestParam(value = "OS_Ver", required = false, defaultValue = "") String osVer) throws Exception {

		String key = KeyGenerator.getKey();
		int brokerKey = configManager.getBrokerKey();
		String deviceId = key + "_" + brokerKey;

		String now = CommonUtil.now();
		DeviceEntity deviceEntity = new DeviceEntity();
		deviceEntity.setAuth(true);
		deviceEntity.setDeviceId(deviceId);
		deviceEntity.setMac(mac);
		deviceEntity.setMailAddr(mailAddr);
		deviceEntity.setMdn(mdn);
		deviceEntity.setRegTm(now);
		deviceEntity.setServiceId(serviceId);
		deviceEntity.setUpTm(now);
		deviceEntity.setUserNm(userName);

		try {
			commonService.addDevice(deviceEntity);

		} catch (DataIntegrityViolationException e) {
			throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_AREADY_USER, e);
		} catch (Exception e) {
			throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_GENERAL_SERVICE, e);
		}

		HttpSetResponse.sendResponse(String.valueOf(SenderResultCode.SUCCESS.getResultCode()), response);
	}

	/**
	 * 가입자 Delete 요청
	 * 
	 * @param request
	 * @param response
	 * @param mdn
	 *            신규가입자 전화번호
	 * @param mac
	 *            MAC address 번호
	 * @param serviceId
	 *            서비스 및 Group 식별 번호
	 * @param userName
	 *            사용자 이름
	 * @param userPasswd
	 *            사용자 패스워드
	 * @param mailAddr
	 *            사용자 메일 주소
	 * @param manu
	 *            단말 제조사
	 * @param os
	 *            단말 OS 명
	 * @param osVer
	 *            단말 OS 버전
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = Constants.URL_USER_INFO_DELETE)
	public void userInfoDelete(HttpServletResponse response, @RequestParam(value = "MDN", required = false, defaultValue = "") String mdn,
			@RequestParam(value = "MAC", required = false, defaultValue = "") String mac,
			@RequestParam(value = "ServiceID", required = false, defaultValue = "") String serviceId,
			@RequestParam(value = "User_Name", required = false, defaultValue = "") String userName,
			@RequestParam(value = "User_Passwd", required = false, defaultValue = "") String userPasswd,
			@RequestParam(value = "Mail_Addr", required = false, defaultValue = "") String mailAddr,
			@RequestParam(value = "Manu", required = false, defaultValue = "") String manu,
			@RequestParam(value = "OS", required = false, defaultValue = "") String os,
			@RequestParam(value = "OS_Ver", required = false, defaultValue = "") String osVer) throws Exception {

		try {
			int result = commonService.delDevice(mac);
			if (result != 1) {
				throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_NOT_FOUND_USER);
			}
		} catch (Exception e) {
			throw new SenderException(mac, Constants.URL_USER_AUTH, SenderResultCode.FAIL_GENERAL_SERVICE, e);
		}
		HttpSetResponse.sendResponse(String.valueOf(SenderResultCode.SUCCESS.getResultCode()), response);
	}

	/**
	 * 데이터 Push 요청
	 * 
	 * @date : 2013. 11. 14.
	 * 
	 * @param response
	 * @param serviceId
	 *            서비스 및 Group 식별 ID
	 * @param sendMdn
	 *            발신전화번호
	 * @param recvMdns
	 *            수신전화번호 또는 MAC Address번호
	 * @param data
	 *            Push 메시지
	 * @param qos
	 *            메시지 전송 Type
	 * @throws Exception
	 */
	@RequestMapping(value = Constants.REQUEST_PUSH_DATA)
	public void requestPushData(HttpServletResponse response, @RequestParam(value = "ServiceID", required = false, defaultValue = "") String serviceId,
			@RequestParam(value = "Send_mdn", required = false, defaultValue = "") String sendMdn,
			@RequestParam(value = "Recv_mdn", required = false, defaultValue = "") String recvMdns,
			@RequestParam(value = "Data", required = false, defaultValue = "") String data,
			@RequestParam(value = "Qos", required = false, defaultValue = "") String qos) throws Exception {

		try {

			String now = CommonUtil.now();

			MessageEntity messageEntity = new MessageEntity();
			messageEntity.setMsg(data);
			messageEntity.setQos(qos);
			messageEntity.setMsgId(KeyGenerator.getKey());
			messageEntity.setRegTm(now);

			String messageId = senderService.addMessage(messageEntity);
			if (messageId == null) {
				throw new SenderException(serviceId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_GENERAL_SERVICE, String.format("messageId:{}",
						messageId));
			}

			// g/w 연결 확인 후, publish 수행
			if (GatewayControl.mqttsClientMap.isEmpty()) {
				log.info("Message Save Complete. Gateway Not Connect");
				// FIXME 나중에.... 상황보고.. response 날리는부분 순서 정하기
				HttpSetResponse.sendResponse(String.valueOf(SenderResultCode.SUCCESS.getResultCode()), response);
			} else {
				HttpSetResponse.sendResponse(String.valueOf(SenderResultCode.SUCCESS.getResultCode()), response);

				if (messageEntity.isQos()) {
					// 비동기로 처리
					reqPushDataQueue.putData(recvMdns, messageEntity);
				} else {

					String deviceIds = senderService.getDeviceIds(recvMdns);
					if (deviceIds == null) {
						throw new SenderException(serviceId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_NOT_FOUND_USER, String.format("deviceId:{}",
								deviceIds));
					} else {

						String deviceId[] = deviceIds.split(",");
						String deviceMsgId = null;
						for (String id : deviceId) {
							DeviceMessageEntity deviceMessageEntity = new DeviceMessageEntity();
							deviceMessageEntity.setMsgId(messageId);
							deviceMessageEntity.setDeviceId(id);
							deviceMessageEntity.setDeliveryStat("y");
							deviceMessageEntity.setRetryCnt(0);
							// FIXME 아직 publish 안된건데 now적용해도 되는가? pubAck올때 업데이트?
							deviceMessageEntity.setDeliveryTm(now);
							deviceMessageEntity.setRegTm(now);

							deviceMsgId = (String) senderService.addDeviceMsg(deviceMessageEntity);

							if (deviceMsgId == null) {
								throw new SenderException(deviceMsgId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_GENERAL_SERVICE);
							} else {
								List<DeviceMessageEntity> pushDataList = senderService.getPushDataList(deviceMsgId);

								ArrayList<String> macArrayList = new ArrayList<String>();
								ArrayList<String> gwIdArrayList = new ArrayList<String>();

								String payLoad = getPayload(data, messageId, pushDataList, macArrayList, gwIdArrayList);
								log.info("Payload :{}", payLoad);

								for (String gwId : gwIdArrayList) {
									if (GatewayControl.mqttsClientMap.containsKey(gwId)) {
										MqttsClient mqttsClient = GatewayControl.mqttsClientMap.get(gwId);
										mqttsClient.publishQos0(Short.parseShort("1"), Short.parseShort("0"), payLoad);
									}
								}
							}
						}
					}
				}// end else
			}
		} catch (Exception e) {
			throw new SenderException(serviceId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_GENERAL_SERVICE, e);
		}
	}

	/**
	 * DataList로 부터 payLoad 조합하기
	 * 
	 * @date : 2014. 1. 15.
	 * 
	 * @param data
	 * @param messageId
	 * @param pushDataList
	 * @param macArrayList
	 * @param gwIdArrayList
	 * @return
	 */
	public String getPayload(String data, String messageId, List<DeviceMessageEntity> pushDataList, ArrayList<String> macArrayList,
			ArrayList<String> gwIdArrayList) {
		for (DeviceMessageEntity deviceMsgEntity : pushDataList) {
			if (pushDataList.size() == 1) {
				macArrayList.add(deviceMsgEntity.getDeviceEntity().getMac());
				gwIdArrayList.add(deviceMsgEntity.getDeviceEntity().getGwId());
			} else {
				macArrayList.add(deviceMsgEntity.getDeviceEntity().getMac());
				if (!gwIdArrayList.contains(deviceMsgEntity.getDeviceEntity().getGwId())) {
					gwIdArrayList.add(deviceMsgEntity.getDeviceEntity().getGwId());
				}
			}
		}

		String mac = new String();
		mac = StringUtils.join(macArrayList, ",");

		log.info("MSG_ID:{} , MSG:{} , MAC:{}", new Object[] { messageId, data, mac });

		String payLoad = messageId + "|" + mac + "|" + data;
		return payLoad;
	}

	/**
	 * Qo1 1인경우, publish할 때 비동기로 처리(ReqPushDataQueue에서 호출함)
	 * 
	 * @date : 2013. 12. 5.
	 * 
	 * @param map
	 *            (recvMdn,MessageEntity)
	 */
	public void requestPushDataQos1(Map<String, MessageEntity> map) {
		for (Entry<String, MessageEntity> entry : map.entrySet()) {

			String deviceIds = senderService.getDeviceIds(entry.getKey());
			if (deviceIds == null) {
				throw new SenderException(entry.getKey(), Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_NOT_FOUND_USER, String.format("deviceId:{}",
						deviceIds));
			} else {

				String deviceId[] = deviceIds.split(",");
				String deviceMsgId = null;
				for (String id : deviceId) {
					DeviceMessageEntity deviceMessageEntity = new DeviceMessageEntity();
					deviceMessageEntity.setMsgId(entry.getValue().getMsgId());
					deviceMessageEntity.setDeviceId(id);
					deviceMessageEntity.setDeliveryStat("y");
					deviceMessageEntity.setRetryCnt(0);
					deviceMessageEntity.setDeliveryTm(CommonUtil.now());
					deviceMessageEntity.setRegTm(CommonUtil.now());

					deviceMsgId = (String) senderService.addDeviceMsg(deviceMessageEntity);

					if (deviceMsgId == null) {
						throw new SenderException(deviceMsgId, Constants.REQUEST_PUSH_DATA, SenderResultCode.FAIL_GENERAL_SERVICE);
					} else {
						List<DeviceMessageEntity> pushDatalist = senderService.getPushDataList(deviceMsgId);

						ArrayList<String> macArrayList = new ArrayList<String>();
						ArrayList<String> gwIdArrayList = new ArrayList<String>();

						String payLoad = getPayload(entry.getValue().getMsg(), entry.getValue().getMsgId(), pushDatalist, macArrayList, gwIdArrayList);

						log.info("Payload :{}", payLoad);

						for (String gwId : gwIdArrayList) {
							if (GatewayControl.mqttsClientMap.containsKey(gwId)) {
								MqttsClient mqttsClient = GatewayControl.mqttsClientMap.get(gwId);
								mqttsClient.publishQos1(Short.parseShort("1"), Short.parseShort("1"), payLoad);
							}
						}
					}// end else
				}// end for
			}
		}
	}

	@ExceptionHandler(SenderException.class)
	public void handleCustomException(SenderException ex, HttpServletResponse response) {

		try {
			ErrorLogUtil.logging(this.getClass().getName(), ex.getMessage(), ex, log);
			HttpSetResponse.sendResponse(String.valueOf(ex.getSenderResultCode().getResultCode()), response);
		} catch (Exception e) {
			ErrorLogUtil.logging(this.getClass().getName(), e.getMessage(), e, log);
		}
	}
}
