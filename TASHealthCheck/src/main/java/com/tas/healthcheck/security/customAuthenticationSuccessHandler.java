package com.tas.healthcheck.security;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
 
public class customAuthenticationSuccessHandler implements
		AuthenticationSuccessHandler {
 
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		System.out.println("In CustomAuth");

		HttpSession session = request.getSession();
		
 		SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
 		
 		String redirectUri = "/applications";
 		
 		if(savedRequest != null){
 			String urlToRedirect = savedRequest.getRedirectUrl();
 		
 			String loginUrl = request.getRequestURL().toString();
 			loginUrl = loginUrl.substring(0, loginUrl.length() - 6);
 		
 			redirectUri = urlToRedirect.replace(loginUrl, "");
 			
 			if(redirectUri.equals("/login")){
 				redirectUri = "/applications";
 			}
 		}
 		
		/*Set some session variables*/
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
        session.setAttribute("uname", authUser.getUsername());  
        session.setAttribute("authorities", authentication.getAuthorities()); 
        
        /*Set target URL to redirect*/
		String targetUrl = determineTargetUrl(authentication, redirectUri); 
        redirectStrategy.sendRedirect(request, response, targetUrl);
	}
 
	protected String determineTargetUrl(Authentication authentication, String successfulRedirectUri) {
        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authorities.contains("ROLE_ADMIN")) {
        	return successfulRedirectUri;
        } else {
            throw new IllegalStateException();
        }
    }
 
	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
 
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
}