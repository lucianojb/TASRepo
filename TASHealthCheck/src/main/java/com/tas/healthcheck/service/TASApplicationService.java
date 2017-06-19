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

public class TASApplicationService {

	private static final Logger logger = LoggerFactory.getLogger(TASApplicationService.class);
	
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
			payload.setResultValue(0);
			return payload;
		}
		
		if(this.checkForScheduledDown(app)){
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
					payload.setErrorMessage("Unable to read JSON content");
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
			if(rootNode.has("ver") && !rootNode.get("ver").asText().equals(app.getVersionNum())){
				String newVersion = rootNode.get("ver").asText();
				logger.info("Saving version of app {} as {}", app.getAppName(), newVersion);
				app.setVersionNum(newVersion);
			
				this.saveApplication(app);
			}
			
			if(!rootNode.has("conns")){
				//if there are no connections then app is healthy
				payload.setResultValue(1);
				return payload;
			}
			JsonNode connectionsNode = rootNode.get("conns");
			
			if(app.getConnections() == null){
				//if there are no connections on application object side then also healthy
				payload.setResultValue(1);
				return payload;
			}
			
			String[] appConnections = app.getConnections().split(",");
			Map<String, Boolean> connectionsMap = new HashMap<String, Boolean>();
			
			boolean allFalse = true;
			boolean allTrue = true;
			for(int x = 0; x < appConnections.length; x++){
				if(!connectionsNode.has(appConnections[x])){
					//if healthcheck json does not have expected connection
					payload.setResultValue(-1);
					payload.setErrorMessage("Healthcheck JSON does not contain expected connection: " + appConnections[x]);
					return payload;
				}
				boolean connectionValue = connectionsNode.get(appConnections[x]).asBoolean();
				connectionsMap.put(appConnections[x], connectionValue);
				if(connectionValue){
					allFalse = false;
				}
				if(!connectionValue){
					allTrue = false;
				}
			}
			
			payload.setConnections(connectionsMap);
			if(allFalse){
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
