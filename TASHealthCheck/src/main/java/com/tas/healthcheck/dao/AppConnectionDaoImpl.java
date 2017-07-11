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

@Repository
@Transactional
public class AppConnectionDaoImpl implements AppConnectionDao{

	private static final Logger logger = LoggerFactory.getLogger(AppConnectionDaoImpl.class);
		
		private SessionFactory sessionFactory;
		
		public void setSessionFactory(SessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
		}

		@Override
		public AppConnection saveAppConnection(AppConnection conn) {
			logger.info("Adding {} to database", conn.toString());
			
			AppConnection savedConn = null;
			
			try{
				savedConn = (AppConnection) sessionFactory.getCurrentSession().merge(conn);
				
				Date date = new Date();
				
				sessionFactory.getCurrentSession().createQuery("update AppConnection set uppdated_date = :update"
						+ " where conn_id = :id").setParameter("update", date).setParameter("id", savedConn.getConnID()).executeUpdate();
				
				return savedConn;
			}catch(HibernateException exc){
				return null;
			}
		}

		@Override
		public List<AppConnection> getAllAppConnection() {
			return this.sessionFactory.getCurrentSession().createQuery("from AppConnection order by conn_id", AppConnection.class).getResultList();
		}

		@Override
		public AppConnection getAppConnectionById(int connId) {
			AppConnection conn;
			
			try{
			conn = this.sessionFactory.getCurrentSession()
					.createQuery("from AppConnection where conn_id = :connidentity", AppConnection.class)
					.setParameter("connidentity", connId)
					.getSingleResult();
			}catch(NoResultException nre){
				return null;
			}
			
			return conn;
		}

		@Override
		public List<AppConnection> getAllAppConnectionByAppId(int appId) {
			return this.sessionFactory.getCurrentSession().createQuery("from AppConnection "
					+ " where app_id = :id", AppConnection.class).setParameter("id", appId).getResultList();
		}

		@Override
		public boolean removeById(int connId) {
			sessionFactory.getCurrentSession().createQuery("DELETE from AppConnection where conn_id = :givenid")
			.setParameter("givenid", connId).executeUpdate();
			
			return true;
		}

		@Override
		public boolean removeAllByAppId(int appId) {
			sessionFactory.getCurrentSession().createQuery("DELETE from AppConnection where app_id = :givenid")
			.setParameter("givenid", appId).executeUpdate();
			
			return true;
		}
}
