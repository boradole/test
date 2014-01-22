package com.sktelecom.ssm.broker.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sktelecom.ssm.broker.dao.MessageDao;
import com.sktelecom.ssm.broker.entity.MessageEntity;

@Repository
public class MessageDaoImpl implements MessageDao {

	private static Logger log = LoggerFactory.getLogger(MessageDaoImpl.class);

	private SessionFactory sessionFactory;

	public MessageDaoImpl() {

	}

	@Autowired
	public MessageDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public String insertMessage(MessageEntity messageEntity) {
		String id = (String) getSession().save(messageEntity);
		log.info("[insertMessage] insert Id : {} ", id);
		return id;
	}

}
