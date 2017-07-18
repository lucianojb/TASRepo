package com.tas.healthcheck.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tas.healthcheck.dao.AppConnectionDao;
import com.tas.healthcheck.dao.DownScheduleDao;
import com.tas.healthcheck.dao.TASApplicationDao;
import com.tas.healthcheck.models.AppConnection;
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
	
	@Autowired
	AppConnectionDao appConnectionDao;
	
	@Autowired
	ConnectionService connectionService;
	
	@Autowired
	HealthcheckPayloadService healthcheckPayloadService;
	
	public Application saveApplication(Application application) {
		return tasApplicationDao.saveApplication(application);
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

	
	private static int STATUS_OFF = -1;
	private static int STATUS_UP = 0;
	private static int STATUS_ERROR = 1;
	private static int STATUS_SOME = 2;
	private static int STATUS_DOWN = 3;
	/*
	 * Return health state as int
	 * -1 is known disabled
	 * 0 is healthy
	 * 1 error retrieving or parsing healthcheck
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
			if(app.getAppName().equals("LEOFA") || app.getAppName().equals("TRIP")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("alltrue.json").toURI())));
			}else if(app.getAppName().equals("Amber Alerts")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("allfalse.json").toURI())));
			}else if(app.getAppName().equals("Parking")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("sometrue.json").toURI())));
			}else if(app.getAppName().equals("Job Swap")){
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
				List<AppConnection> connections = appConnectionDao.getAllAppConnectionByAppId(app.getAppID());
				if(connections != null && connections.size() > 0){
					
					List<String> appConnections = new ArrayList<String>();
					
					boolean priorityConnectionDown = false;
					Map<String, Connection> connectionsMap = new HashMap<String, Connection>();

					for(AppConnection conn : connections){
						appConnections.add(conn.getConnName().toLowerCase());
						if(conn.getPriority()){
							priorityConnectionDown = true;
							connectionsMap.put(conn.getConnName().toLowerCase(), new Connection(null, "Expected connection but was not in JSON", true, true));
						}else{
							connectionsMap.put(conn.getConnName().toLowerCase(), new Connection(null, "Expected connection but was not in JSON", true, false));
						}
					}
					
					if(priorityConnectionDown){
						payload.setResultValue(STATUS_DOWN);
					}else{
						payload.setResultValue(STATUS_SOME);
					}
					payload.setConnections(connectionsMap);
					return payload;
				}else{
					//if there are no connections then app is healthy
					payload.setResultValue(STATUS_UP);
					return payload;
				}
			}
			JsonNode connectionsNode = rootNode.get(CONN_CHECKS);
			

			List<AppConnection> connections = appConnectionDao.getAllAppConnectionByAppId(app.getAppID());
			List<String> appConnections = null;
			List<String> priorityConnections = new ArrayList<String>();
			if(connections != null && connections.size() > 0){
				appConnections = new ArrayList<String>();
			
				for(AppConnection con : connections){
					appConnections.add(con.getConnName().toLowerCase());
					if(con.getPriority()){
						priorityConnections.add(con.getConnName().toLowerCase());
					}
				}
			}
			
			
			Map<String, Connection> connectionsMap = new HashMap<String, Connection>();
			
			boolean allFalse = true;
			boolean allTrue = true;
			boolean incorrectJSON = false;
			boolean priorityConnectionDown = false;
			
			if(!connectionsNode.isArray()){
				payload.setResultValue(STATUS_ERROR);
				payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName() + ", JSON connChecks is not a list");
				return payload;
			}
			
			for(JsonNode objNode : connectionsNode){
				System.out.println(objNode.toString());
				
				if(!objNode.has(CONN_NAME) || !objNode.has(CONN_FUNC) || !objNode.has(CONN_DESC)){
					logger.error("Connection json string {} does not contain expected values", objNode.toString());
					incorrectJSON = true;
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
						if(priorityConnections.contains(connName)){
							connectionsMap.put(connName, new Connection(connValue, connDetails, true, true));
							if(!connValue){
								priorityConnectionDown = true;
							}
						}else{
							connectionsMap.put(connName, new Connection(connValue, connDetails, true, false));
						}
					}else{
						connectionsMap.put(connName, new Connection(connValue, connDetails, false, false));
					}
				}
			}
			
			boolean doesNotContainConnection = false;
			if(appConnections != null){
				for(String connection: appConnections){
					if(!connectionsMap.containsKey(connection.toLowerCase())){
						if(priorityConnections.contains(connection)){
							priorityConnectionDown = true;
							connectionsMap.put(connection, new Connection(null, "Expected connection but was not in JSON", true, true));
						}else{
							connectionsMap.put(connection, new Connection(null, "Expected connection but was not in JSON", true, false));
						}
					
						doesNotContainConnection = true;
					}
				}
			}
						
			payload.setConnections(connectionsMap);
			
			if(incorrectJSON && connectionsMap.size() == 0){
					payload.setResultValue(STATUS_ERROR);
					payload.setErrorMessage("Connection values incorrectly formatted in JSON");
					return payload;
			}
			
			if(priorityConnectionDown){
				payload.setResultValue(STATUS_DOWN);
			}
			else if(doesNotContainConnection){
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
		TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
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
	  
	  public void runHealthChecksOnApps(List<Application> apps){
		  List<HealthcheckPayload> payloads = new LinkedList<HealthcheckPayload>();

			for (Application app : apps) {
				payloads.add(this.determineHealthOfApp(app));
			}
			
			for(HealthcheckPayload payload : payloads){
				HealthcheckPayload existingPayload = healthcheckPayloadService.getByAppId(payload.getApp().getAppID());
				
				HealthcheckPayload saveP = new HealthcheckPayload();
				
				if(existingPayload != null){
					saveP.setHealthId(existingPayload.getHealthId());
				}
				
				saveP.setAppId(payload.getApp().getAppID());
				saveP.setErrorMessage(payload.getErrorMessage());
				saveP.setResultValue(payload.getResultValue());
				
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
					this.saveApplication(pl.getApp());
				} else if ((pl.getResultValue() == STATUS_UP || pl.getResultValue() == STATUS_SOME)
						&& pl.getApp().getupTime() == null) {
					TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
					Date date = new Date();
					pl.getApp().setupTime(date);
					this.saveApplication(pl.getApp());
				}
			}
	  }
}
