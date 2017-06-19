package com.tas.healthcheck.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tas.healthcheck.dao.TASApplicationDao;
import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.models.HealthcheckPayload;

public class TASApplicationService {

	private static final Logger logger = LoggerFactory.getLogger(TASApplicationService.class);
	
	@Autowired
	TASApplicationDao tasApplicationDao;
	
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
	 * 0 is known outage
	 * 1 is healthy
	 * 2 is some healthy, some down
	 * 3 is all down
	 */
	public HealthcheckPayload determineHealthOfApp(Application app) {
		HealthcheckPayload payload = new HealthcheckPayload(app);
		
		//String healthCheckURL = app.getUrl();
		
		//TO-DO: add logic here for known outage
		//return 0
		
		String jsonContent;
						
		try {
			//TO-DO: retrieve JSON from url instead of resource
			
			//Mocking data
			if(app.getAppName().equals("alltrue")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("alltrue.json").toURI())));
			}else if(app.getAppName().equals("allfalse")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("allfalse.json").toURI())));
			}else if(app.getAppName().equals("sometrue")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("sometrue.json").toURI())));
			}else if(app.getAppName().equals("noconnections")){
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("noconnections.json").toURI())));
			}else{
				jsonContent = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("sometrue.json").toURI())));
			}
			//End of mock
			
			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readTree(jsonContent);
			
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
					payload.setErrorMessage("Healthcheck JSON does not contain expected connection " + appConnections[x]);
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
		} catch (IOException e) {
			e.printStackTrace();
			payload.setResultValue(-1);
			payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName());
			return payload;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			payload.setResultValue(-1);
			payload.setErrorMessage("Could not read Healthcheck payload from application " + app.getAppName());
			return payload;
		}
	}

}
