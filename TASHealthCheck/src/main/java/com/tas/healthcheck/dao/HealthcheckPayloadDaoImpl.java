package com.tas.healthcheck.dao;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.models.HealthcheckPayload;

@Repository
@Transactional
public class HealthcheckPayloadDaoImpl implements HealthcheckPayloadDao {

	private static final Logger logger = LoggerFactory.getLogger(HealthcheckPayloadDaoImpl.class);
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public boolean removeAllByAppId(int appId) {
		sessionFactory.getCurrentSession().createQuery("DELETE from HealthcheckPayload where app_id = :givenid")
		.setParameter("givenid", appId).executeUpdate();
		
		return true;
	}

	@Override
	public HealthcheckPayload savePayload(HealthcheckPayload saveP) {
		logger.info("Adding {} to database", saveP.toString());
		
		HealthcheckPayload savedPayload = null;
		
		try{
			savedPayload = (HealthcheckPayload) sessionFactory.getCurrentSession().merge(saveP);
			
			Date date = new Date();
			
			sessionFactory.getCurrentSession().createQuery("update HealthcheckPayload set uppdated_date = :update"
					+ " where health_id = :id").setParameter("update", date).setParameter("id", savedPayload.getHealthId()).executeUpdate();
			
			return savedPayload;
		}catch(HibernateException exc){
			return null;
		}		
	}

	@Override
	public HealthcheckPayload getOneByAppId(int appID) {
		HealthcheckPayload payload;
		
		try{
		payload = this.sessionFactory.getCurrentSession()
				.createQuery("from HealthcheckPayload where app_id = :applicationidentity", HealthcheckPayload.class)
				.setParameter("applicationidentity", appID)
				.getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
		
		return payload;
	}
}
