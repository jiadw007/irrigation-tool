package com.project.server;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.project.model.timeBasedModel;
import com.project.po.Data;

public class calculateServlet extends HttpServlet{
	
	

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//Cookie[] cookies = req.getCookies();
		//System.out.println(cookies[1]);
		
		String information = req.getParameter("json");
		//System.out.println(information);
		try {
			JSONObject jsonObject = new JSONObject(information);
			System.out.println(jsonObject);
			String email = jsonObject.getString("email");
			String password = jsonObject.getString("password");
			String soilType = jsonObject.getString("soilType");
			String zipcode = jsonObject.getString("zipcode");
			String unit = jsonObject.getString("unit");
			double rootDepth = jsonObject.getDouble("rd");
			double area = jsonObject.getDouble("area");
			String[] systemSelection = jsonObject.getString("systemSelection").split(",");
			double rainsettings = 0.0;
			double soilthreshold = 0.0;
			double irriDepth = 0.0;
			String isystem = "";
			int irriDuration = 0;
			if(jsonObject.has("rainsettings")){
				
			    rainsettings = jsonObject.getDouble("rainsettings");
			
			}
			if(jsonObject.has("soilthreshold")){
				
				soilthreshold = jsonObject.getDouble("soilthreshold");
				
			}
			if(jsonObject.has("irriDepth")){
				
				irriDepth = jsonObject.getDouble("irriDepth");
				
			}
			if(jsonObject.has("isystem")){
				
				isystem = jsonObject.getString("isystem");
				irriDuration = jsonObject.getInt("irriDuration");
				
				
				
			}
			String[] days = jsonObject.getString("days").split(",");
			String[] hours = jsonObject.getString("hours").split(",");
			//String[] minutes = jsonObject.getString("minutes").split(",");
			String choice = jsonObject.getString("choice");
			Data data = new Data(email,password,unit,zipcode,
					soilType, rootDepth, area,
					systemSelection, days, hours,
					choice, rainsettings,
					soilthreshold, irriDepth, isystem,
					irriDuration);
		    
			for (String system : systemSelection){
				
				if (system.equals("Time-based")){
					
					System.out.println("Time-based");
					
					timeBasedModel tbm = new timeBasedModel(soilType,area,rootDepth,zipcode,unit);
					System.out.println(tbm.getRootDepth());
					System.out.println(tbm.getUnit());
					System.out.println(tbm.getArea());
					System.out.println(tbm.getSoilType());
					tbm.getLocation().print();
					
				}else if(system.equals("Time-based with rain sensor")){
					
					System.out.println("Time-based with rain sensor");
					
				}else if(system.equals("Time-based with soil moisture sensor")){
					
					System.out.println("Time-based with soil moisture sensor");
					
				}else{
					
					System.out.println("Evapotranspiration Controller");
					
				}
				
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//resp.setContentType("text/plain");
		//resp.getWriter().println("Hello, world");
		//resp.sendRedirect("/results.html");
		
		
	}
	
	

}
