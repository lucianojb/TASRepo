package com.tas.healthcheck.web;

import java.util.List;

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

import com.tas.healthcheck.models.Application;
import com.tas.healthcheck.service.TASApplicationService;

@Controller
public class AdminController {
	
	@Autowired
	TASApplicationService tasApplicationService;
	
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
			@RequestParam(name="connection", required=false) String[] connectionValues, @RequestParam(name="submit") String submit, RedirectAttributes redirectAttributes) {
		
		if(submit.equals("cancel")){
			return "redirect:./";
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
			
			return "createapplication";
		}
		
		if(connectionValues != null){
			StringBuilder strBuild = new StringBuilder();
			for(int x = 0; x < connectionValues.length; x++){
				
				if(connectionValues[x].isEmpty()){
					logger.info("Empty connection component");
					
					redirectAttributes.addFlashAttribute("application", application);
					model.addAttribute("connectionsAdded", connectionValues);
					model.addAttribute("connectionInvalid", "Connection values cannot be empty");
					
					return "createapplication";
				}
				
				strBuild.append(connectionValues[x] + ",");
			}
			strBuild.deleteCharAt(strBuild.length() - 1);
		
			application.setConnections(strBuild.toString());
		}
		
		logger.info("Creating: " + application);
		application.setActiveState(true);
		tasApplicationService.saveApplication(application);
		
		return "redirect:./applications";
	}
	
	@RequestMapping(value = "/editapplication/{id}", method = RequestMethod.GET)
	public String editAppGet(@PathVariable("id") int id, Model model, Application application) {
		application = tasApplicationService.getApplicationById(id);
		
		if(application == null){
			model.addAttribute("errorMessage", "Could not find application to edit");
			return "error";
		}
		
		logger.info("Editing Application {} is {} GET", id, application);
		
		model.addAttribute("application", application);
		
		if(application.getConnections() != null){
			String[] connections = application.getConnections().split(",");
			model.addAttribute("connections", connections);
		}
		
		return "editapplication";
	}
	
	@RequestMapping(value = "/editapplication/{id}", method = RequestMethod.POST)
	public String editAppPost(@PathVariable("id") int id, Model model, @Valid Application application, 
			BindingResult bindingResult, RedirectAttributes redirectAttributes, 
			@RequestParam(name="connection", required=false) String[] connectionValues, @RequestParam("submit")String submit){
		logger.info("Editing Application {} is {} POST", id, application);
		
		if(submit.equals("cancel")){
			return "redirect:../applications";
		}
		
		if(bindingResult.hasErrors()){
			logger.info("Binding errors on application " + application);
			if(connectionValues != null){
				logger.info("Preserving connectionValues " + connectionValues.toString() + ", size " + connectionValues.length);
			}
	        redirectAttributes.addFlashAttribute("errors", bindingResult);
			redirectAttributes.addFlashAttribute("application", application);
			model.addAttribute("connections", connectionValues);
			
			return "editapplication";
		}
		
		if(connectionValues != null){
			StringBuilder strBuild = new StringBuilder();
			for(int x = 0; x < connectionValues.length; x++){
				
				if(connectionValues[x].isEmpty()){
					logger.info("Empty connection component");
					
					redirectAttributes.addFlashAttribute("application", application);
					model.addAttribute("connections", connectionValues);
					model.addAttribute("connectionInvalid", "Connection values cannot be empty");
					
					return "editapplication";
				}
				
				strBuild.append(connectionValues[x] + ",");
			}
			strBuild.deleteCharAt(strBuild.length() - 1);
		
			application.setConnections(strBuild.toString());
		}
		
		tasApplicationService.saveApplication(application);
		
		return "redirect:../applications";
	}
	
	@RequestMapping(value = "/deleteapplication/{id}", method = RequestMethod.GET)
	public String deleteUserGet(Model model, @PathVariable("id") int id) {
		logger.info("Getting page to delete application {}", id);
		
		Application app = tasApplicationService.getApplicationById(id);
		if(app == null){
			model.addAttribute("errorMessage", "Could not find application to delete");
			return "error";
		}
		
		model.addAttribute("application", app);
						
		return "deleteapplication";
	}
	
	@RequestMapping(value = "/deleteapplication/{id}", method = RequestMethod.POST)
	public String deleteUser(Model model,  @PathVariable("id") int id, @RequestParam(name="submit", required=true)String submit) {
		logger.info("Deleting application {}!", id);
		
		if(submit.equals("delete")){
			Application app = tasApplicationService.getApplicationById(id);
			if(app == null){
				model.addAttribute("errorMessage", "Could not find application to delete");
				return "redirect:../error";
			}

			//remove app
			tasApplicationService.removeApplicationById(id);
		}
		
						
		return "redirect:../applications";
	}
}
