package com.sktelecom.ssm.broker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.AbstractMessage.QOSType;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.ConnAckMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingRespMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PubAckMessage;
import com.sktelecom.ssm.broker.external.mqttsclient.messages.PublishMessage;

public class MqttsMessageManagement {
	private static Logger logger = LoggerFactory.getLogger(MqttsMessageManagement.class);

	public PubAckMessage receviedPublish(PublishMessage msg, String clientId) {
		logger.info(
				"received a PublishMessage. clientId : \"{}\", totalMessageLength : {}, msgType : {}, dopflags : {}, qos : {}, Retainflags : {}, topicId : \"{}\", MsgId : {}, PayLoad : \"{}\"",
				new Object[] { clientId, msg.getLength(), msg.getMsgType(), msg.isDupFlag(), msg.getQos(), msg.isRetainFlag(), msg.getTopicId(),
						msg.getMsgId(), new String(msg.getPayload()) });

		if (msg.getQos().toString().equals(QOSType.LEAST_ONE.toString())) {

			PubAckMessage message = new PubAckMessage();
			message.setLength(PubAckMessage.TOTAL_LENGTH);
			message.setMsgType(PubAckMessage.MSGTYPE_PUBACK);
			message.setTopicId((short) msg.getTopicId());
			message.setMsgId(msg.getMsgId());
			message.setReturnCode(PubAckMessage.ACCEPTED);

			logger.info("Send a PubAckMessage. clientId : \"{}\", totalMessageLength : {}, msgType : {}, topicId : {}, MsgId : {}, returnCode : {}",
					new Object[] { clientId, message.getLength(), message.getMsgType(), message.getTopicId(), message.getMsgId(), message.getReturnCode() });

			return message;
		}
		return null;
	}

	public void receviedPubAck(PubAckMessage msg, String clientId) {
		logger.info("received a PubAckMessage. clientId : {}, length : {}, msgType : {}, topId : \"{}\", msgId : {}, returnCode : {}", new Object[] { clientId,
				msg.getLength(), msg.getMsgType(), msg.getTopicId(), msg.getMsgId(), msg.getReturnCode() });
	}

	public void receviedPingResp(PingRespMessage msg, String clientId) {
		logger.info("received a PingRespMessage. clientId : {}, totalMessageLength : {}, messageType : {}",
				new Object[] { clientId, msg.getLength(), msg.getMsgType() });
	}

	public void receviedConnAck(ConnAckMessage msg, String clientId) {
		logger.info("received a ConnAckMessage. clientId : {}, totalMessageLength : {}, messageType : {}, returnCode : {}",
				new Object[] { clientId, msg.getLength(), msg.getMsgType(), msg.getReturnCode() });
	}

}
