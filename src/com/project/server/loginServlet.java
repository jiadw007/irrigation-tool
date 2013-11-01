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

public class loginServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if(user != null){
			
			Cookie email = new Cookie("email", user.getEmail());
			email.setMaxAge(7*24*60*60);
			email.setPath("/");
			resp.addCookie(email);
			resp.sendRedirect("/");
			
		}else{
			
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
			
		}
	}
	
	
	

}
