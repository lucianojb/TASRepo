package com.tas.healthcheck.dao;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.tas.healthcheck.models.Application;

@Repository
@Transactional
public class TASApplicationDaoImpl implements TASApplicationDao{

private static final Logger logger = LoggerFactory.getLogger(TASApplicationDaoImpl.class);
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Application saveApplication(Application app) {
		logger.info("Adding {} to database", app.toString());
		
		Application savedApp = null;
		
		try{
			savedApp = (Application)sessionFactory.getCurrentSession().merge(app);
			TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
			Date date = new Date();
			
			sessionFactory.getCurrentSession().createQuery("update Application set uppdated_date = :update"
					+ " where app_id = :id").setParameter("update", date).setParameter("id", savedApp.getAppID()).executeUpdate();
			
			return savedApp;
		}catch(HibernateException exc){
			return null;
		}
	}

	@Override
	public List<Application> getAllApps() {
		return this.sessionFactory.getCurrentSession().createQuery("from Application order by app_name", Application.class).getResultList();
	}

	@Override
	public Application getAppById(int id) {
		Application app;
		
		try{
		app = this.sessionFactory.getCurrentSession()
				.createQuery("from Application where app_id = :applicationidentity", Application.class)
				.setParameter("applicationidentity", id)
				.getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
		
		return app;
	}

	@Override
	public boolean removeById(int id) {
		sessionFactory.getCurrentSession().createQuery("DELETE from Application where app_id = :givenid")
		.setParameter("givenid", id).executeUpdate();
		
		return true;
	}
	
}
