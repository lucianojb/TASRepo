package com.tas.healthcheck.web;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.models.HealthcheckPayload;
import com.tas.healthcheck.service.TASApplicationService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	TASApplicationService tasApplicationService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		logger.info("Accessing the tas healthcheck dashboard homepage");
		
		List<Application> apps = tasApplicationService.getAllApplications();
		List<HealthcheckPayload> payloads = new LinkedList<HealthcheckPayload>();
		
		for(Application app : apps){
			payloads.add(tasApplicationService.determineHealthOfApp(app));
		}
		
		model.addAttribute("applications", apps);
		model.addAttribute("payloads", payloads);
		
		return "home";
	}
	
	@RequestMapping(value = "/application/{id}", method = RequestMethod.GET)
	public String individualAppView(Model model, @PathVariable("id") int id){
		
		Application app = tasApplicationService.getApplicationById(id);
		if(app == null){
			model.addAttribute("errorMessage", "Could not find app with id " + id);
			return "redirect:../error";
		}
		
		HealthcheckPayload payload = tasApplicationService.determineHealthOfApp(app);
		
		logger.info(payload.toString());
		
		model.addAttribute("app", app);
		model.addAttribute("healthPayload", payload);
		
		return "application";
	}
	
}
