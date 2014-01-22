package com.sktelecom.ssm.broker.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sktelecom.ssm.broker.dao.GatewayDao;
import com.sktelecom.ssm.broker.entity.GatewayEntity;

@Repository
public class GatewayDaoImpl implements GatewayDao {

	private static Logger log = LoggerFactory.getLogger(GatewayDaoImpl.class);

	private SessionFactory sessionFactory;

	public GatewayDaoImpl() {

	}

	@Autowired
	public GatewayDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public String insertGateway(GatewayEntity gatewayEntity) {
		String id = (String) getSession().save(gatewayEntity);
		log.info("[join] insert Id : {}", id);
		return id;
	}

	@Override
	public String selectGateway(String gwIp, String port) {
		Query query = getSession().getNamedQuery("gateway.selectGatewayInfo");
		GatewayEntity gatewayEntity = (GatewayEntity) query.setString("gwIp", gwIp).setString("gwPort", port).uniqueResult();
		return gatewayEntity == null ? null : gatewayEntity.getGwId();
	}

}
