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
			payload.setResultValue(0);
			return payload;
		}
		
		if(this.checkForScheduledDown(app)){
			logger.info("Application has scheduled down time");
			payload.setResultValue(0);
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
					payload.setResultValue(-1);
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
				payload.setResultValue(-1);
				payload.setErrorMessage("Unable to read JSON content from " + app.getUrl());
				return payload;
			}
			
			if(rootNode == null){
				payload.setResultValue(-1);
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
				//if there are no connections then app is healthy
				payload.setResultValue(1);
				return payload;
			}
			JsonNode connectionsNode = rootNode.get(CONN_CHECKS);
			
			if(app.getConnections() == null){
				//if there are no connections on application object side then also healthy
				payload.setResultValue(1);
				return payload;
			}
			
			String[] connections = app.getConnections().split(",");
			for(int x = 0; x < connections.length; x++){
				connections[x] = connections[x].toLowerCase();
			}
			
			List<String> appConnections = new ArrayList<String>(Arrays.asList(connections));
			
			Map<String, Connection> connectionsMap = new HashMap<String, Connection>();
			
			boolean allFalse = true;
			boolean allTrue = true;
			
			if(!connectionsNode.isArray()){
				payload.setResultValue(-1);
				payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName() + ", JSON connChecks is not a list");
				return payload;
			}
			
			for(JsonNode objNode : connectionsNode){
				System.out.println(objNode.toString());
				
				if(!objNode.has("name") || !objNode.has("functional") || !objNode.has("description")){
					logger.error("Connection json string does not contain expected values");
				}else{
				
					String connName = objNode.get("name").asText().toLowerCase();
					boolean connValue = objNode.get("functional").asBoolean();
					String connDetails = objNode.get("description").asText();
				
					if(connValue){
						allFalse = false;
					}
					if(!connValue){
						allTrue = false;
					}
					
					if(appConnections.contains(connName)){
						connectionsMap.put(connName, new Connection(connValue, connDetails, true));
					}else{
						connectionsMap.put(connName, new Connection(connValue, connDetails, false));
					}
				}
			}
			
			boolean doesNotContainConnection = false;
			for(String connection: appConnections){
				if(!connectionsMap.containsKey(connection.toLowerCase())){
					connectionsMap.put(connection, new Connection(null, "Expected connection but was not in JSON", true));
					
					doesNotContainConnection = true;
				}
			}
						
			payload.setConnections(connectionsMap);
			
			if(doesNotContainConnection){
				if(allFalse){
					payload.setResultValue(3);
				}else{
					payload.setResultValue(2);
				}
			}else if(allFalse){
				payload.setResultValue(3);
			}else if(allTrue){
				payload.setResultValue(1);
			}else{
				payload.setResultValue(2);
			}
			
			return payload;
		}catch (JsonParseException e){
			payload.setResultValue(-1);
			payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName() + ", application url may be wrong or json may be formatted incorrectly");
			return payload;
		}catch (IOException e) {
			e.printStackTrace();
			payload.setResultValue(-1);
			payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName() + ", please confirm application url");
			return payload;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			payload.setResultValue(-1);
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
