package com.tas.healthcheck.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tas.healthcheck.dao.DownScheduleDao;
import com.tas.healthcheck.dao.TASApplicationDao;
import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.models.DownSchedule;
import com.tas.healthcheck.models.HealthcheckPayload;
import com.tas.healthcheck.models.Connection;

public class TASApplicationService {

	private static final Logger logger = LoggerFactory.getLogger(TASApplicationService.class);
	
	private static final String APP_VERSION = "appVersion";
	private static final String CONN_CHECKS = "connChecks";
	private static final String CONN_NAME = "name";
	private static final String CONN_DESC = "description";
	private static final String CONN_FUNC = "functional";
	
	@Autowired
	TASApplicationDao tasApplicationDao;
	
	@Autowired
	DownScheduleDao downScheduleDao;
	
	public void saveApplication(Application application) {
		tasApplicationDao.saveApplication(application);
	}

	public List<Application> getAllApplications() {
		return tasApplicationDao.getAllApps();
	}

	public Application getApplicationById(int id) {
		return tasApplicationDao.getAppById(id);
	}

	public boolean removeApplicationById(int id) {
		return tasApplicationDao.removeById(id);
	}

	
	private static int STATUS_ERROR = -1;
	private static int STATUS_OFF = 0;
	private static int STATUS_UP = 1;
	private static int STATUS_SOME = 2;
	private static int STATUS_DOWN = 3;
	/*
	 * Return health state as int
	 * -1 error retrieving or parsing healthcheck
	 * 0 is known disabled
	 * 1 is healthy
	 * 2 is some healthy, some down
	 * 3 is all down
	 */
	public HealthcheckPayload determineHealthOfApp(Application app) {
		logger.info("Checking health of app " + app);
		
		HealthcheckPayload payload = new HealthcheckPayload(app);
		
		//String healthCheckURL = app.getUrl();
		
		//App is manually disabled
		if(!app.isActiveState()){
			logger.info("Application health checks manually turned off");
			payload.setResultValue(STATUS_OFF);
			return payload;
		}
		
		if(this.checkForScheduledDown(app)){
			logger.info("Application has scheduled down time");
			payload.setResultValue(STATUS_OFF);
			return payload;
		}
		
		String jsonContent = null;
						
		try {			
			//Mocking data
			if(app.getAppName().equals("alltrue") || app.getAppName().equals("missingconnections")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("alltrue.json").toURI())));
			}else if(app.getAppName().equals("allfalse")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("allfalse.json").toURI())));
			}else if(app.getAppName().equals("sometrue")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("sometrue.json").toURI())));
			}else if(app.getAppName().equals("noconnections")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("noconnections.json").toURI())));
				//End of mock
			}else{
				URL urlToParse = new URL(app.getUrl());
				
				HttpURLConnection connection = (HttpURLConnection) urlToParse.openConnection();
		        connection.setRequestMethod("GET");
		        connection.setDoOutput(true);
		        connection.connect();
				
		        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				jsonContent = readAllChars(rd);
				
				connection.disconnect();
				
				if(jsonContent == null){
					payload.setResultValue(STATUS_ERROR);
					payload.setErrorMessage("Unable to read JSON content from " + app.getUrl());
					return payload;
				}
				
			}

			
			logger.info("Parsing the json content " + jsonContent);
			
			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = null;
			
			try{
				rootNode = mapper.readTree(jsonContent);
			}catch(JsonParseException e){
				logger.error("Error parsing json " + jsonContent);
				payload.setResultValue(STATUS_ERROR);
				payload.setErrorMessage("Unable to read JSON content from " + app.getUrl());
				return payload;
			}
			
			if(rootNode == null){
				payload.setResultValue(STATUS_ERROR);
				payload.setErrorMessage("No content found in " + app.getUrl());
				return payload;
			}
			
			//save the application version name
			if(rootNode.has(APP_VERSION) && !rootNode.get(APP_VERSION).asText().equals(app.getVersionNum())){
				String newVersion = rootNode.get(APP_VERSION).asText();
				logger.info("Saving version of app {} as {}", app.getAppName(), newVersion);
				app.setVersionNum(newVersion);
			
				this.saveApplication(app);
			}
			
			if(!rootNode.has(CONN_CHECKS)){
				if(app.getConnections() != null){
					String[] connections = null;
					List<String> appConnections = null;
					if(app.getConnections() != null){
						connections = app.getConnections().split(",");
						for(int x = 0; x < connections.length; x++){
							connections[x] = connections[x].toLowerCase();
						}
					
						appConnections = new ArrayList<String>(Arrays.asList(connections));
					}
					
					Map<String, Connection> connectionsMap = new HashMap<String, Connection>();
					
					for(String connection: appConnections){
						connectionsMap.put(connection, new Connection(null, "Expected connection but was not in JSON", true));
					}
					
					payload.setResultValue(STATUS_SOME);
					payload.setConnections(connectionsMap);
					return payload;
				}else{
					//if there are no connections then app is healthy
					payload.setResultValue(STATUS_UP);
					return payload;
				}
			}
			JsonNode connectionsNode = rootNode.get(CONN_CHECKS);
			
			/*if(app.getConnections() == null){
				//if there are no connections on application object side then also healthy
				payload.setResultValue(1);
				return payload;
			}*/
			
			String[] connections = null;
			List<String> appConnections = null;
			if(app.getConnections() != null){
				connections = app.getConnections().split(",");
				for(int x = 0; x < connections.length; x++){
					connections[x] = connections[x].toLowerCase();
				}
			
				appConnections = new ArrayList<String>(Arrays.asList(connections));
			}
			
			Map<String, Connection> connectionsMap = new HashMap<String, Connection>();
			
			boolean allFalse = true;
			boolean allTrue = true;
			
			if(!connectionsNode.isArray()){
				payload.setResultValue(STATUS_ERROR);
				payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName() + ", JSON connChecks is not a list");
				return payload;
			}
			
			for(JsonNode objNode : connectionsNode){
				System.out.println(objNode.toString());
				
				if(!objNode.has(CONN_NAME) || !objNode.has(CONN_FUNC) || !objNode.has(CONN_DESC)){
					logger.error("Connection json string {} does not contain expected values", objNode.toString());
				}else{
				
					String connName = objNode.get(CONN_NAME).asText().toLowerCase();
					boolean connValue = objNode.get(CONN_FUNC).asBoolean();
					String connDetails = objNode.get(CONN_DESC).asText();
				
					if(connValue){
						allFalse = false;
					}
					if(!connValue){
						allTrue = false;
					}
					
					if(appConnections != null && appConnections.contains(connName)){
						connectionsMap.put(connName, new Connection(connValue, connDetails, true));
					}else{
						connectionsMap.put(connName, new Connection(connValue, connDetails, false));
					}
				}
			}
			
			boolean doesNotContainConnection = false;
			if(appConnections != null){
				for(String connection: appConnections){
					if(!connectionsMap.containsKey(connection.toLowerCase())){
						connectionsMap.put(connection, new Connection(null, "Expected connection but was not in JSON", true));
					
						doesNotContainConnection = true;
					}
				}
			}
						
			payload.setConnections(connectionsMap);
			
			if(doesNotContainConnection){
				if(allFalse){
					payload.setResultValue(STATUS_DOWN);
				}else{
					payload.setResultValue(STATUS_SOME);
				}
			}else if(allFalse){
				payload.setResultValue(STATUS_DOWN);
			}else if(allTrue){
				payload.setResultValue(STATUS_UP);
			}else{
				payload.setResultValue(STATUS_SOME);
			}
			
			return payload;
		}catch (JsonParseException e){
			payload.setResultValue(STATUS_ERROR);
			payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName() + ", application url may be wrong or json may be formatted incorrectly");
			return payload;
		}catch (IOException e) {
			e.printStackTrace();
			payload.setResultValue(STATUS_ERROR);
			payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName() + ", please confirm application url");
			return payload;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			payload.setResultValue(STATUS_ERROR);
			payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName());
			return payload;
		}
	}

	private boolean checkForScheduledDown(Application app) {

		List<DownSchedule> dScheds = downScheduleDao.getAllDownSchedulesByAppId(app.getAppID());
		
		Date currentDateTime = new Date();
		
		boolean appScheduledDown = false;
		//remove schedules in past
		//check to see if current time falls in one schedule and return true
		for(DownSchedule sched: dScheds){
			if(sched.getEndDate().before(currentDateTime)){
				downScheduleDao.removeById(sched.getSchedID());
			}
			if(sched.getStartDate().before(currentDateTime) && sched.getEndDate().after(currentDateTime)){
				appScheduledDown = true;
			}
			
		}
		
		return appScheduledDown;
	}
	
	  private String readAllChars(Reader rd){
		    StringBuilder sb = new StringBuilder();
		    int cc;
		    try {
				while ((cc = rd.read()) != -1) {
				  sb.append((char) cc);
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Error reading json from reader" + rd);
				return null;
			}
		    return sb.toString();
	  }

}
