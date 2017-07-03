package com.tas.healthcheck.web;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Payload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.models.DownSchedule;
import com.tas.healthcheck.models.HealthcheckPayload;
import com.tas.healthcheck.service.DownScheduleService;
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
        
        Boolean[] myValues = {true, true, true, true, true};
        
        model.addAttribute("checkboxValues", myValues);
        model.addAttribute("applications", apps);
        model.addAttribute("payloads", payloads);
        
        return "home";
    }
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
    public String homePost(Model model, @RequestParam(name="filterCheckBox", required=false) Integer[] checkboxValues) {
        logger.info("Accessing the tas healthcheck dashboard homepage");
        
        List<Application> apps = tasApplicationService.getAllApplications();
        List<HealthcheckPayload> payloads = new LinkedList<HealthcheckPayload>();
        List<HealthcheckPayload> filteredPayloads = new LinkedList<HealthcheckPayload>();
        
        
        for(Application app : apps){
            payloads.add(tasApplicationService.determineHealthOfApp(app));
        }
        
    	Collections.sort(payloads, new PayloadComparator());
        
        Boolean[] myValues = {false, false, false, false, false};
        if(checkboxValues != null){
            for(Integer checkbox : checkboxValues){
                logger.info("Here is some stuff values is {}", checkbox);
                myValues[checkbox + 1] = true;
                
                for(HealthcheckPayload payload : payloads){
                	if(payload.getResultValue() == checkbox){
                		filteredPayloads.add(payload);
                	}
                }
                
            }
        }
        
        model.addAttribute("checkboxValues", myValues);
        model.addAttribute("applications", apps);
        model.addAttribute("payloads", filteredPayloads);
        
        return "home";
    }
	

	
	@RequestMapping(value = "/homeinner", method = RequestMethod.GET)
	public String homeInner(Model model, @RequestParam(name="check", required=false) String[] checkboxValues) {
	logger.info("Accessing the tas healthcheck dashboard homepage");
		
		List<Application> apps = tasApplicationService.getAllApplications();
		List<HealthcheckPayload> payloads = new LinkedList<HealthcheckPayload>();
		
		for(Application app : apps){
			payloads.add(tasApplicationService.determineHealthOfApp(app));
		}
		Collections.sort(payloads, new PayloadComparator());
		
		boolean[] preserveCheckboxes = {false,false, false, false, false};
		
		if (checkboxValues != null){
		for (String x: checkboxValues)
		{
			preserveCheckboxes[Integer.parseInt(x)] = true;			
			logger.info(x);
		}}
		
		model.addAttribute("checkboxValues", preserveCheckboxes);
		model.addAttribute("applications", apps);
		model.addAttribute("payloads", payloads);
		return "homeinner";
	}
	
	@RequestMapping(value = "/homeinner", method = RequestMethod.POST)
	public String innerPost(Model model, @RequestParam(name="check", required=false) String[] checkboxValues) {
		logger.info("Accessing the tas healthcheck dashboard homepage");
		
		List<Application> apps = tasApplicationService.getAllApplications();
		List<HealthcheckPayload> payloads = new LinkedList<HealthcheckPayload>();
		
		for(Application app : apps){
			payloads.add(tasApplicationService.determineHealthOfApp(app));
		}
		
		Collections.sort(payloads, new PayloadComparator());
		
		boolean[] preserveCheckboxes = {false,false, false, false, false};
		
		if (checkboxValues != null){
		for (String x: checkboxValues)
		{
			preserveCheckboxes[Integer.parseInt(x)] = true;			
			logger.info(x);
		}}
		
		
		model.addAttribute("checkboxValues", preserveCheckboxes);
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
		
		HealthcheckPayload payload = tasApplicationService.determineHealthOfApp(app);
		List<DownSchedule> dScheds = downScheduleService.getAllScheduledDownByAppId(id);
		
		logger.info(payload.toString());
		
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
		
		HealthcheckPayload payload = tasApplicationService.determineHealthOfApp(app);
		List<DownSchedule> dScheds = downScheduleService.getAllScheduledDownByAppId(id);
		
		logger.info(payload.toString());
		
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
}
