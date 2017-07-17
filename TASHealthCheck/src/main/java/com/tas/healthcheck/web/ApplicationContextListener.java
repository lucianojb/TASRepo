package com.tas.healthcheck.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.models.Connection;
import com.tas.healthcheck.models.HealthcheckPayload;
import com.tas.healthcheck.service.ConnectionService;
import com.tas.healthcheck.service.HealthcheckPayloadService;
import com.tas.healthcheck.service.TASApplicationService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);

	Executor executor = null;

	@Autowired
	TASApplicationService tasApplicationService;
	
	@Autowired
	HealthcheckPayloadService healthcheckPayloadService;
	
	@Autowired
	ConnectionService connectionService;

	private static int STATUS_OFF = -1;
	private static int STATUS_UP = 0;
	private static int STATUS_ERROR = 1;
	private static int STATUS_SOME = 2;
	private static int STATUS_DOWN = 3;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info(
				"ApplicationContext was initialized or refreshed: " + event.getApplicationContext().getDisplayName());
		String events = event.getApplicationContext().getDisplayName();

		if (executor == null && events.contains("Root")) {
			executor = Executors.newSingleThreadExecutor();
			executor.execute(new Runnable() {
				public void run() {
					while (true) {
						if (tasApplicationService != null) {
							List<Application> apps = tasApplicationService.getAllApplications();
							List<HealthcheckPayload> payloads = new LinkedList<HealthcheckPayload>();

							for (Application app : apps) {
								payloads.add(tasApplicationService.determineHealthOfApp(app));
							}
							
							for(HealthcheckPayload payload : payloads){
								HealthcheckPayload saveP = new HealthcheckPayload();
								
								saveP.setAppId(payload.getApp().getAppID());
								saveP.setErrorMessage(payload.getErrorMessage());
								saveP.setResultValue(payload.getResultValue());
								
								healthcheckPayloadService.removeAllByAppId(saveP.getAppId());
								connectionService.removeAllByAppId(saveP.getAppId());
								
								healthcheckPayloadService.saveHealthCheckPayload(saveP);
								
								List<Connection> listOfNewConnections = new LinkedList<Connection>();
								if(payload.getConnections() != null){
									for (Map.Entry<String, Connection> entry : payload.getConnections().entrySet()){
										Connection oldC = entry.getValue();
									
										Connection saveC = new Connection();
										saveC.setConnName(entry.getKey());
										saveC.setDetails(oldC.getDetails());
										saveC.setAppId(saveP.getAppId());
										saveC.setFunctional(oldC.getFunctional());
										saveC.setExpected(oldC.getExpected());
										saveC.setPriority(oldC.getPriority());
									
										listOfNewConnections.add(saveC);
									}
								
									for(Connection conn : listOfNewConnections){
										connectionService.saveNewConnection(conn);
									}
								}
							}
							
							for (HealthcheckPayload pl : payloads) {
								if (pl.getResultValue() == STATUS_DOWN || pl.getResultValue() == STATUS_ERROR
										|| pl.getResultValue() == STATUS_OFF) {
									pl.getApp().setupTime(null);
									tasApplicationService.saveApplication(pl.getApp());
								} else if ((pl.getResultValue() == STATUS_UP || pl.getResultValue() == STATUS_SOME)
										&& pl.getApp().getupTime() == null) {
									Date date = new Date();
									pl.getApp().setupTime(date);
									tasApplicationService.saveApplication(pl.getApp());
								}
							}
						} else {
							logger.info("tasApplicationService is null");
						}
						try {
							logger.info("Sleeping for health checks");
							Thread.sleep(60000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
			});
		}

	}
}
