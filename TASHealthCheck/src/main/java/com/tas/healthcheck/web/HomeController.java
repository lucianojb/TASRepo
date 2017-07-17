package com.tas.healthcheck.web;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.models.Connection;
import com.tas.healthcheck.models.DownSchedule;
import com.tas.healthcheck.models.HealthcheckPayload;
import com.tas.healthcheck.service.AppConnectionService;
import com.tas.healthcheck.service.ConnectionService;
import com.tas.healthcheck.service.DownScheduleService;
import com.tas.healthcheck.service.HealthcheckPayloadService;
import com.tas.healthcheck.service.PayloadComparator;
import com.tas.healthcheck.service.TASApplicationService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	TASApplicationService tasApplicationService;
	
	@Autowired
	DownScheduleService downScheduleService;
	
	@Autowired
	AppConnectionService appConnectionService;
	
	@Autowired
	HealthcheckPayloadService healthcheckPayloadService;
	
	@Autowired
	ConnectionService connectionService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		logger.info("Accessing the tas healthcheck dashboard homepage");
		
		List<Application> apps = tasApplicationService.getAllApplications();
		List<HealthcheckPayload> payloads = new LinkedList<HealthcheckPayload>();
		
		for(Application app : apps){
			payloads.add(createPayloadFromApp(app));
		}
		
		Collections.sort(payloads, new PayloadComparator());
		
		model.addAttribute("applications", apps);
		model.addAttribute("payloads", payloads);
		
		return "home";
	}

	@RequestMapping(value = "/homeinner", method = RequestMethod.GET)
	public String homeInner(Model model) {
		logger.info("Accessing the tas healthcheck dashboard homepage");
		
		List<Application> apps = tasApplicationService.getAllApplications();
		List<HealthcheckPayload> payloads = new LinkedList<HealthcheckPayload>();
		
		for(Application app : apps){
			payloads.add(createPayloadFromApp(app));
		}
		
		Collections.sort(payloads, new PayloadComparator());
		
		model.addAttribute("applications", apps);
		model.addAttribute("payloads", payloads);
		
		return "homeinner";
	}
	
	@RequestMapping(value = {"/application/{id}", "/application/{id}/*"}, method = RequestMethod.GET)
	public String individualAppView(Model model, @PathVariable("id") int id){
		
		Application app = tasApplicationService.getApplicationById(id);
		if(app == null){
			model.addAttribute("errorMessage", "Could not find app with id " + id);
			return "redirect:../error";
		}
		
		HealthcheckPayload payload = createPayloadFromApp(app);
		List<DownSchedule> dScheds = downScheduleService.getAllScheduledDownByAppId(id);
		
		logger.info(payload.toString());
		
		if (app.getupTime() != null) {
			Date date = new Date();
			long diff = date.getTime() - app.getupTime().getTime();

			long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
			long hours = TimeUnit.MILLISECONDS.toHours(diff);

			model.addAttribute("upHours", hours);
			model.addAttribute("upMinutes", minutes);
		}
		model.addAttribute("app", app);
		model.addAttribute("healthPayload", payload);
		model.addAttribute("scheduledTimes", dScheds);
		
		return "application";
	}
	
	@RequestMapping(value = {"/appinner/{id}", "/appinner/{id}/*"}, method = RequestMethod.GET)
	public String appInner(Model model, @PathVariable("id") int id, RedirectAttributes redirect) {
		
		Application app = tasApplicationService.getApplicationById(id);
		if(app == null){
			redirect.addFlashAttribute("errorMessage", "Could not find app with id " + id);
			return "redirect:../error";
		}
		
		HealthcheckPayload payload = createPayloadFromApp(app);
		List<DownSchedule> dScheds = downScheduleService.getAllScheduledDownByAppId(id);
		
		logger.info(payload.toString());
		
		if (app.getupTime() != null) {
			Date date = new Date();
			long diff = date.getTime() - app.getupTime().getTime();

			long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
			long hours = TimeUnit.MILLISECONDS.toHours(diff);

			model.addAttribute("upHours", hours);
			model.addAttribute("upMinutes", minutes);
		}
		
		model.addAttribute("app", app);
		model.addAttribute("healthPayload", payload);
		model.addAttribute("scheduledTimes", dScheds);

		return "appinner";
	}
	
	
	@RequestMapping(value = {"/jsontest"}, method = RequestMethod.GET)
	public String jsonEndpointTest(Model model){
		return "jsontest";
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String error(Model model) {
		return "error";
	}
	
	private HealthcheckPayload createPayloadFromApp(Application app) {
		HealthcheckPayload payload = new HealthcheckPayload(app);
		
		HealthcheckPayload storedP = healthcheckPayloadService.getByAppId(app.getAppID());
		List<Connection> storedConns = connectionService.getAllByAppId(app.getAppID());
		
		payload.setAppId(storedP.getAppId());
		payload.setErrorMessage(storedP.getErrorMessage());
		payload.setResultValue(storedP.getResultValue());
		
		Map<String, Connection> connections = new HashMap<String, Connection>();
		for(Connection conn : storedConns){
			connections.put(conn.getConnName(), conn);
		}

		payload.setConnections(connections);
		
		return payload;
	}
}
