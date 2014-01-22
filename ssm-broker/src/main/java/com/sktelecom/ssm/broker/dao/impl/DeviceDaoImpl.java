package com.sktelecom.ssm.broker.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sktelecom.ssm.broker.dao.DeviceDao;
import com.sktelecom.ssm.broker.entity.DeviceEntity;
import com.sktelecom.ssm.broker.entity.DeviceMessageEntity;
import com.sktelecom.ssm.broker.entity.DeviceMsgSucEntity;
import com.sktelecom.ssm.util.CommonUtil;

@Repository
public class DeviceDaoImpl implements DeviceDao {

	private static Logger log = LoggerFactory.getLogger(DeviceDaoImpl.class);

	private SessionFactory sessionFactory;

	public DeviceDaoImpl() {

	}

	@Autowired
	public DeviceDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Description: 단말 접속 요청
	 * 
	 * @date : 2013. 11. 4.
	 * 
	 * @param mac
	 * @return
	 */
	@Override
	public DeviceEntity selectDevice(String mac) {
		Query query = getSession().getNamedQuery("device.selectDevice");
		DeviceEntity deviceEntity = (DeviceEntity) query.setString("searchMac", mac).uniqueResult();
		return deviceEntity;
	}

	@Override
	public String selectDeviceId(String recvMdns) {
		String deviceIds = "";
		Query query = getSession().getNamedQuery("device.selectDeviceId");
		if (recvMdns.contains(",")) {
			String[] macs = recvMdns.split(",");
			for (String mac : macs) {
				DeviceEntity deviceEntity = (DeviceEntity) query.setString("searchMac", mac).uniqueResult();
				if (deviceEntity != null) {
					deviceIds += deviceEntity.getDeviceId() + ",";
				} else {
					log.info("NOT_FOUND_USER : {}", mac);
				}
			}
		} else {
			DeviceEntity deviceEntity = (DeviceEntity) query.setString("searchMac", recvMdns).uniqueResult();
			if (deviceEntity != null) {
				deviceIds = deviceEntity.getDeviceId();
			} else {
				log.info("NOT_FOUND_USER : {}", recvMdns);
			}
		}
		return deviceIds;
	}

	@Override
	public int updateGatewayInfo(String gwInfo, String mac) {
		String now = CommonUtil.now();
		Query query = getSession().getNamedQuery("device.updateDevice");
		return query.setParameter("gwId", gwInfo).setParameter("upTm", now).setParameter("mac", mac).executeUpdate();
	}

	@Override
	public String insertDevice(DeviceEntity deviceEntity) {
		return (String) getSession().save(deviceEntity);
	}

	@Override
	public int deleteDevice(String mac) {
		Query query = getSession().getNamedQuery("device.deleteDevice");
		return query.setParameter("mac", mac).executeUpdate();
	}

	@Override
	public String insertDeviceMsg(DeviceMessageEntity deviceMessageEntity) {
		Object id = getSession().save(deviceMessageEntity);
		deviceMessageEntity = (DeviceMessageEntity) id;
		return deviceMessageEntity == null ? null : deviceMessageEntity.getMsgId();
	}

	@Override
	public List<DeviceMessageEntity> selectPushDataList(String deviceMsgId) {
		Query query = getSession().getNamedQuery("device.selectPushMessageList");
		List<DeviceMessageEntity> list = query.setString("searchMsgId", deviceMsgId).list();
		return list;
	}

	@Override
	public String insertDeviceMsgSuc(DeviceMsgSucEntity deviceMsgSucEntity) {
		Object id = getSession().save(deviceMsgSucEntity);
		deviceMsgSucEntity = (DeviceMsgSucEntity) id;
		return deviceMsgSucEntity == null ? null : deviceMsgSucEntity.getMsgId();
	}

	@Override
	public void deleteDeviceMsg(String msgId) {
		Query query = getSession().getNamedQuery("device.deleteDeviceMsg");
		query.setParameter("msgId", msgId).executeUpdate();
	}

	@Override
	public void insertDeviceMsgFail(int retryCnt) {
		Query query = getSession().getNamedQuery("device.insertDeviceMsgFail");
		query.setParameter("retryCnt", retryCnt).executeUpdate();
	}

	@Override
	public void deleteDeviceMsgOverCnt(int retryCnt) {
		Query query = getSession().getNamedQuery("device.deleteDeviceMsgOverCnt");
		query.setParameter("retryCnt", retryCnt).executeUpdate();
	}

	@Override
	public List<DeviceMessageEntity> selectPushDataListOverTime(String now, int selectCnt) {
		Query query = getSession().getNamedQuery("device.selectPushMessageListOverTime");
		List<DeviceMessageEntity> list = query.setString("now", now).setMaxResults(selectCnt).list();
		return list;
	}

	@Override
	public void updateDeviceMsgRetryCnt(String msgId, String deviceId, int retryCnt) {
		Query query = getSession().getNamedQuery("device.updateDeviceMsgRetryCnt");
		query.setParameter("msgId", msgId).setParameter("deviceId", deviceId).setParameter("retryCnt", retryCnt).executeUpdate();
	}

	@Override
	public Object selectDeviceMessage(String msgId) {
		Query query = getSession().getNamedQuery("device.selectDeviceMessage");
		return query.setString("msgId", msgId).uniqueResult();
	}
}
