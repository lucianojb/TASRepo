package com.tas.healthcheck.dao;

import java.util.List;

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
	public boolean saveApplication(Application app) {
		logger.info("Adding {} to database", app.toString());
		
		try{
			sessionFactory.getCurrentSession().merge(app);
			return true;
		}catch(HibernateException exc){
			return false;
		}
	}

	@Override
	public List<Application> getAllApps() {
		return this.sessionFactory.getCurrentSession().createQuery("from Application order by app_name", Application.class).getResultList();
	}
	
}
