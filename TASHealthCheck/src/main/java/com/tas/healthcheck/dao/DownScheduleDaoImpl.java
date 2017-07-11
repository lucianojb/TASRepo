package com.tas.healthcheck.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.tas.healthcheck.models.DownSchedule;

@Repository
@Transactional
public class DownScheduleDaoImpl implements DownScheduleDao{

private static final Logger logger = LoggerFactory.getLogger(DownScheduleDaoImpl.class);
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public boolean removeById(int id) {
		sessionFactory.getCurrentSession().createQuery("DELETE from DownSchedule where sched_id = :givenid")
		.setParameter("givenid", id).executeUpdate();
		
		return true;
	}

	@Override
	public DownSchedule saveDownSchedule(DownSchedule sched) {
		logger.info("Adding {} to database", sched.toString());
		
		DownSchedule savedSched = null;
		
		try{
			savedSched = (DownSchedule) sessionFactory.getCurrentSession().merge(sched);
			
			Date date = new Date();
			
			sessionFactory.getCurrentSession().createQuery("update DownSchedule set uppdated_date = :update"
					+ " where sched_id = :id").setParameter("update", date).setParameter("id", savedSched.getSchedID()).executeUpdate();
			
			return savedSched;
		}catch(HibernateException exc){
			return null;
		}
	}

	@Override
	public List<DownSchedule> getAllDownSchedule() {
		return this.sessionFactory.getCurrentSession().createQuery("from DownSchedule order by sched_id", DownSchedule.class).getResultList();
	}

	@Override
	public DownSchedule getDownScheduleById(int id) {
		DownSchedule sched;
		
		try{
		sched = this.sessionFactory.getCurrentSession()
				.createQuery("from DownSchedule where sched_id = :scheduleidentity", DownSchedule.class)
				.setParameter("scheduleidentity", id)
				.getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
		
		return sched;
	}

	@Override
	public List<DownSchedule> getAllDownSchedulesByAppId(int id) {
		return this.sessionFactory.getCurrentSession().createQuery("from DownSchedule "
				+ " where app_id = :id", DownSchedule.class).setParameter("id", id).getResultList();
	}

	@Override
	public void removeByAppId(int id) {
		sessionFactory.getCurrentSession().createQuery("DELETE from DownSchedule where app_id = :givenid")
		.setParameter("givenid", id).executeUpdate();
	}
	
}
