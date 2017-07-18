package com.tas.healthcheck.web;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tas.healthcheck.models.AppConnection;
import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.models.DownSchedule;
import com.tas.healthcheck.service.AppConnectionService;
import com.tas.healthcheck.service.DownScheduleService;
import com.tas.healthcheck.service.TASApplicationService;

@Controller
public class AdminController {
	
	@Autowired
	TASApplicationService tasApplicationService;
	
	@Autowired
	DownScheduleService downScheduleService;
	
	@Autowired
	AppConnectionService appConnectionService;
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	
	@RequestMapping(value = "/applications", method = RequestMethod.GET)
	public String viewApps(Model model) {
		List<Application> apps = tasApplicationService.getAllApplications();
		
		model.addAttribute("appData", apps);
		
		return "applicationlist";
	}
	
	@RequestMapping(value = "/createapplication", method = RequestMethod.GET)
	public String createApp(Model model, Application application) {
		return "createapplication";
	}
	
	@RequestMapping(value = "/createapplication", method = RequestMethod.POST)
	public String saveApp(Model model, @Valid Application application, BindingResult bindingResult, 
			@RequestParam(name="connection", required=false) String[] connectionValues, 
			@RequestParam(name="core", required=false) Integer[] priorityValues,
			@RequestParam(name="submit") String submit, RedirectAttributes redirectAttributes) {
		
		if(submit.equals("cancel")){
			return "redirect:./applications";
		}
		
		logger.info("Posting to create application");
		
		if(bindingResult.hasErrors()){
			logger.info("Binding errors on application " + application);
			if(connectionValues != null){
				logger.info("Preserving connectionValues " + connectionValues.toString() + ", size " + connectionValues.length);
			}
			
	        redirectAttributes.addFlashAttribute("errors", bindingResult);
			redirectAttributes.addFlashAttribute("application", application);
			model.addAttribute("connectionsAdded", connectionValues);
			model.addAttribute("priorityValues", priorityValues);
			
			return "createapplication";
		}
		
		List<AppConnection> connList = new LinkedList<AppConnection>();

		if(connectionValues != null){
			logger.info("Parsing through connection values {}", connectionValues.toString());
			for(int x = 0; x < connectionValues.length; x++){
				
				if(connectionValues[x].isEmpty()){
					logger.info("Empty connection component");
					
					redirectAttributes.addFlashAttribute("application", application);
					model.addAttribute("connectionsAdded", connectionValues);
					model.addAttribute("connectionInvalid", "Connection values cannot be empty");
					model.addAttribute("priorityValues", priorityValues);
					
					return "createapplication";
				}
				
				//for now make all priorities false
				connList.add(new AppConnection(connectionValues[x], false));
			}
		}
		
		if(priorityValues != null){
			for(int index : priorityValues){
				if(index < connList.size() && index > -1){
					connList.get(index).setPriority(true);
				}
			}
		}
		
		logger.info("Creating: " + application);
		application.setActiveState(true);
		Application app = tasApplicationService.saveApplication(application);
		
		if(app != null){
			for(AppConnection conn : connList){
				conn.setAppID(app.getAppID());
				appConnectionService.saveAppConnection(conn);
			}
		}
		
		return "redirect:./applications";
	}
	
	@RequestMapping(value = "/editapplication/{id}", method = RequestMethod.GET)
	public String editAppGet(@PathVariable("id") int id, Model model, Application application, RedirectAttributes redirect) {
		application = tasApplicationService.getApplicationById(id);
		
		if(application == null){
			redirect.addFlashAttribute("errorMessage", "Could not find application to edit");
			return "redirect:../error";
		}
		
		logger.info("Editing Application {} is {} GET", id, application);
		
		model.addAttribute("application", application);
		
		List<AppConnection> connections = appConnectionService.getConnectionsByAppId(application.getAppID());
		List<String> connectionNames = new LinkedList<String>();
		for(AppConnection conn : connections){
			connectionNames.add(conn.getConnName());
		}
		model.addAttribute("connections", connections);
		
		return "editapplication";
	}
	
	@RequestMapping(value = "/editapplication/{id}", method = RequestMethod.POST)
	public String editAppPost(@PathVariable("id") int id, Model model, @Valid Application application, 
			BindingResult bindingResult, RedirectAttributes redirectAttributes, 
			@RequestParam(name="connection", required=false) String[] connectionValues, 
			@RequestParam(name="core", required=false) Integer[] priorityValues,
			@RequestParam("submit")String submit){
		logger.info("Editing Application {} is {} POST", id, application);
		
		if(submit.equals("cancel")){
			return "redirect:../applications";
		}
		
		List<AppConnection> appConns = appConnectionService.getConnectionsByAppId(application.getAppID());

		
		if(bindingResult.hasErrors()){
			logger.info("Binding errors on application " + application);
			if(connectionValues != null){
				logger.info("Preserving connectionValues " + connectionValues.toString() + ", size " + connectionValues.length);
			}
			
			application.setAppName(tasApplicationService.getApplicationById(id).getAppName());
			application.setUrl((tasApplicationService.getApplicationById(id).getUrl()));
			
	        redirectAttributes.addFlashAttribute("errors", bindingResult);
			redirectAttributes.addFlashAttribute("application", application);
			model.addAttribute("connections", appConns);
			
			return "editapplication";
		}
		
		
		List<AppConnection> conns = new LinkedList<AppConnection>();
		if(connectionValues != null){
			for(int x = 0; x < connectionValues.length; x++){
				
				if(connectionValues[x].isEmpty()){
					logger.info("Empty connection component");
					
					redirectAttributes.addFlashAttribute("application", application);
					model.addAttribute("connections", appConns);
					model.addAttribute("connectionInvalid", "Connection values cannot be empty");
					
					return "editapplication";
				}
				
				//for now make them all false
				conns.add(new AppConnection(connectionValues[x], false, application.getAppID()));
			}
		}
		
		if(priorityValues != null){
			for(int index : priorityValues){
				if(index < conns.size() && index > -1){
					conns.get(index).setPriority(true);
				}
			}
		}
		
		appConnectionService.removeApplicationConnections(application.getAppID());
		for(AppConnection conn : conns){
			appConnectionService.saveAppConnection(conn);
		}
		
		tasApplicationService.saveApplication(application);
		
		return "redirect:../applications";
	}
	
	@RequestMapping(value = "/deleteapplication/{id}", method = RequestMethod.GET)
	public String deleteApplicationGet(Model model, @PathVariable("id") int id, RedirectAttributes redirect) {
		logger.info("Getting page to delete application {}", id);
		
		Application app = tasApplicationService.getApplicationById(id);
		if(app == null){
			redirect.addFlashAttribute("errorMessage", "Could not find application to delete");
			return "redirect:../error";
		}
		
		model.addAttribute("application", app);
						
		return "deleteapplication";
	}
	
	@RequestMapping(value = "/deleteapplication/{id}", method = RequestMethod.POST)
	public String deleteApplication(Model model,  @PathVariable("id") int id, @RequestParam(name="submit", required=true)String submit, RedirectAttributes redirect) {		
		if(submit.equals("delete")){
			logger.info("Deleting application {}!", id);
			
			Application app = tasApplicationService.getApplicationById(id);
			if(app == null){
				redirect.addFlashAttribute("errorMessage", "Could not find application to delete");
				return "redirect:../error";
			}

			appConnectionService.removeApplicationConnections(id);
			downScheduleService.removeSchedulesByAppId(id);
			tasApplicationService.removeApplicationById(id);
		}
		
						
		return "redirect:../applications";
	}
	
	@RequestMapping(value = "/disableapplication/{id}", method = RequestMethod.GET)
	public String disableAppGet(Model model, @PathVariable("id") int id, DownSchedule downSchedule, RedirectAttributes redirect) {
		logger.info("Getting page to disable application {}", id);
		
		Application app = tasApplicationService.getApplicationById(id);
		if(app == null){
			redirect.addFlashAttribute("errorMessage", "Could not find application to disable");
			return "redirect:../error";
		}
		
		List<DownSchedule> dScheds = downScheduleService.getAllScheduledDownByAppId(id);

		model.addAttribute("application", app);
		model.addAttribute("scheduledTimes", dScheds);
						
		return "disableapplication";
	}
	
	@RequestMapping(value = "/disableapplication/{id}", method = RequestMethod.POST)
	public String disableApp(Model model,  @PathVariable("id") int id, @Valid DownSchedule downSchedule, BindingResult bindingResult,
			@RequestParam(name="submit", required=true)String submit, RedirectAttributes redirectAttributes) {
		logger.info("Disabling application {}!", id);
		
		Application app = tasApplicationService.getApplicationById(id);
		if(app == null){
			redirectAttributes.addFlashAttribute("errorMessage", "Could not find application to disable");
			return "redirect:../error";
		}
		
		List<DownSchedule> dScheds = downScheduleService.getAllScheduledDownByAppId(id);
		
		if(submit.equals("schedule")){
			
			if(bindingResult.hasErrors()){
				logger.info("Binding errors on schedule " + downSchedule);
		        redirectAttributes.addFlashAttribute("errors", bindingResult);
				redirectAttributes.addFlashAttribute("downSchedule", downSchedule);
				model.addAttribute("application", app);
				model.addAttribute("scheduledTimes", dScheds);
				
				return "disableapplication";
			}
			
			logger.info("Binding schedule " + downSchedule);
			
			if(!downSchedule.getEndDate().after(downSchedule.getStartDate())){
				redirectAttributes.addFlashAttribute("downSchedule", downSchedule);
				model.addAttribute("dateError", "End date must be after start date");
				model.addAttribute("application", app);
				model.addAttribute("scheduledTimes", dScheds);
				
				return "disableapplication";
			}
			
			logger.info(downSchedule.toString());
			downScheduleService.saveSchedule(downSchedule);
			
			return "redirect:../disableapplication/" + app.getAppID();
			
		}else if(submit.equals("continue")){
			logger.info("Toggling state of application to {}", !app.isActiveState());
			app.setActiveState(!app.isActiveState());
			tasApplicationService.saveApplication(app);
			
			return "redirect:../disableapplication/" + app.getAppID();
		}
		
						
		return "redirect:../applications";
	}
	
	@RequestMapping(value = "/deleteschedule/{id}", method = RequestMethod.GET)
	public String deleteScheduleGet(Model model, @PathVariable("id") int id, RedirectAttributes redirect) {
		logger.info("Getting page to delete application {}", id);
		
		DownSchedule sched = downScheduleService.getScheduleBySchedId(id);
		if(sched == null){
			redirect.addFlashAttribute("errorMessage", "Could not find schedule to delete");
			return "redirect:../error";
		}
		Application app = tasApplicationService.getApplicationById(sched.getAppID());
		if(app == null){
			redirect.addFlashAttribute("errorMessage", "Could not find app for specified schedule with id " + id);
			return "redirect:../error";
		}
		
		model.addAttribute("schedule", sched);
		model.addAttribute("application", app);
						
		return "deleteschedule";
	}
	
	@RequestMapping(value = "/deleteschedule/{id}", method = RequestMethod.POST)
	public String deleteSchedule(Model model,  @PathVariable("id") int id, @RequestParam(name="submit", required=false)String submit, RedirectAttributes redirect) {
		logger.info("Deleting application {}!", id);
		
		DownSchedule sched = downScheduleService.getScheduleBySchedId(id);
		if(sched == null){
			redirect.addFlashAttribute("errorMessage", "Could not find schedule to delete");
			return "redirect:../error";
		}
		
		int appId = sched.getAppID();
		
		if(submit.equals("delete")){
			
			downScheduleService.removeScheduleById(id);
			
		}
		
		return "redirect:../disableapplication/" + appId;
		
	}
}
