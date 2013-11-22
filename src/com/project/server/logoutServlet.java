package com.project.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
/**
 * Create with MyEclipse
 * User : Dawei Jia
 * Date : 10/19/2013
 * @author Dawei Jia
 * log out function
 */
public class logoutServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Cookie[] cookies = req.getCookies();
		for(Cookie cookie :cookies){
			
			if(cookie.getName().equals("systemSelection")){
				
				
				cookie.setMaxAge(0);
				resp.addCookie(cookie);
				
			}
			if(cookie.getName().equals("email")){
				
				cookie.setMaxAge(0);
				resp.addCookie(cookie);
				
			}
			
			
		}
		resp.sendRedirect(userService.createLogoutURL("/"));
	}
	
	
	

}
