package com.tas.healthcheck.web;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
		
		tasApplicationService.saveApplication(application);
		
		return "redirect:./";
	}
}
