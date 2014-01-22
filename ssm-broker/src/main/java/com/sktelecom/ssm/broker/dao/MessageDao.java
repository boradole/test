package com.sktelecom.ssm.broker.dao;

import com.sktelecom.ssm.broker.entity.MessageEntity;

public interface MessageDao {

	/**
	 * Message 저장(MSG , QOS , MSG_ID)
	 * 
	 * @date : 2013. 11. 14.
	 * 
	 * @param messageEntity
	 * @return insert 성공 여부
	 */
	public String insertMessage(MessageEntity messageEntity);

}
