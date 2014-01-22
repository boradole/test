package com.sktelecom.ssm.broker.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sktelecom.ssm.broker.dao.BrokerDao;
import com.sktelecom.ssm.broker.entity.BrokerEntity;

@Repository
public class BrokerDaoImpl implements BrokerDao {

	private static Logger log = LoggerFactory.getLogger(BrokerDaoImpl.class);

	private SessionFactory sessionFactory;

	public BrokerDaoImpl() {

	}

	@Autowired
	public BrokerDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public int insertBroker(BrokerEntity brokerEntity) {
		int id = (Integer) getSession().save(brokerEntity);
		log.info("[insertBroker] Result Value : {} ", id);
		return id;
	}

}
