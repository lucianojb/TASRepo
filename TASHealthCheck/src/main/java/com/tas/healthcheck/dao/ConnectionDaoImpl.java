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

import com.tas.healthcheck.models.AppConnection;
import com.tas.healthcheck.models.Connection;
import com.tas.healthcheck.models.DownSchedule;

@Repository
@Transactional
public class ConnectionDaoImpl implements ConnectionDao {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionDaoImpl.class);
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Connection saveConnection(Connection conn) {
		logger.info("Adding {} to database", conn.toString());
		
		Connection savedConn = null;
		
		try{
			savedConn = (Connection) sessionFactory.getCurrentSession().merge(conn);
			
			Date date = new Date();
			
			sessionFactory.getCurrentSession().createQuery("update Connection set uppdated_date = :update"
					+ " where conn_id = :id").setParameter("update", date).setParameter("id", savedConn.getConnId()).executeUpdate();
			
			return savedConn;
		}catch(HibernateException exc){
			return null;
		}
	}

	@Override
	public boolean removeAllByAppId(int appId) {
		sessionFactory.getCurrentSession().createQuery("DELETE from Connection where app_id = :givenid")
		.setParameter("givenid", appId).executeUpdate();
		
		return true;
	}

	@Override
	public List<Connection> getListByAppId(int appID) {
		return this.sessionFactory.getCurrentSession().createQuery("from Connection "
				+ " where app_id = :id", Connection.class).setParameter("id", appID).getResultList();
	}
}