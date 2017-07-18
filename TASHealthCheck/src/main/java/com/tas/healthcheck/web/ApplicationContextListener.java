package com.tas.healthcheck.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.service.TASApplicationService;

import java.util.List;
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
							
							tasApplicationService.runHealthChecksOnApps(apps);
							
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
