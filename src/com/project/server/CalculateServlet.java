package com.project.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.po.Util;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.project.model.ETControllerModel;
import com.project.model.TimeBasedModel;
import com.project.model.TimeBasedRainSensorModel;
import com.project.model.TimeBasedSoilSensorModel;
import com.project.po.Data;
import com.project.po.DataBase;
/**
 * Created with MyEclipse
 * User : Dawei Jia
 * Date : 10/19/2013
 * @author jiadw_000
 *
 */
public class CalculateServlet extends HttpServlet{
	
	
	private static final Logger logger = Logger.getLogger(CalculateServlet.class.getCanonicalName());
	@SuppressWarnings("deprecation")
	@Override
	/**
	 * post method function
	 * two reuslt mode : csv and result in web page
	 * 
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = resp.getWriter();
		Cookie[] cookie = req.getCookies();
		String choice = req.getParameter("correspondence");
		boolean adjustETflag = false;
		boolean adjustRainflag = false;
		Cookie cookie1 = new Cookie("choice",choice);
		cookie1.setMaxAge(7*24*60*60);
		cookie1.setPath("/");
		resp.addCookie(cookie1);
		/*
		 * store user setting in the database
		 */
		try{
			
			Data data = new Data(cookie,choice);
			DataBase db = new DataBase("User");
			db.insertIntoDataBase(data);
			/*
			 * unit conversion
			 */
			if(data.getUnit().equals("English")){
				
				data.setIrriDepth(data.getIrriDepth() * 2.54);
				
			}
			System.out.println(data.getIrriDepth());
			/*
			 * compute for all models 
			 */
			for (String system : data.getSystemSelection()){
				
				if (system.equals("Time-based")){
					
					System.out.println("Time-based");
					TimeBasedModel tbm = new TimeBasedModel(data);
					if(!adjustETflag&&tbm.getB().adjustET){
						
						adjustETflag = true;
					}
					if(!adjustRainflag&&tbm.getB().adjustRain){
						
						adjustRainflag = true;
					}
					
					Cookie[] results = Util.calculateLossProcess("time_base",tbm);
					
					for(Cookie result : results){
						
						resp.addCookie(result);
						
					}		
				}else if(system.equals("Time-based with rain sensor")){
					
					System.out.println("Time-based with rain sensor");
						
					TimeBasedRainSensorModel tbrsm = new TimeBasedRainSensorModel(data);
					if(!adjustETflag&&tbrsm.getB().adjustET){
						
						adjustETflag = true;
					}
					if(!adjustRainflag&&tbrsm.getB().adjustRain){
						
						adjustRainflag = true;
					}
					Cookie[] results = Util.calculateLossProcess("rain_sensor",tbrsm);
					for(Cookie result : results){			
						resp.addCookie(result);				
					}	
					
				}else if(system.equals("Time-based with soil moisture sensor")){
					
					System.out.println("Time-based with soil moisture sensor");
					
					TimeBasedSoilSensorModel tbssm = new TimeBasedSoilSensorModel(data);
					
					if(!adjustETflag&&tbssm.getB().adjustET){
						
						adjustETflag = true;
					}
					if(!adjustRainflag&&tbssm.getB().adjustRain){
						
						adjustRainflag = true;
					}
					
					Cookie[] results = Util.calculateLossProcess("soil_sensor",tbssm);
					
					for(Cookie result : results){
						resp.addCookie(result);			
					}
	
				}else{
					
					System.out.println("Evapotranspiration Controller");
					
					ETControllerModel etcm = new ETControllerModel(data);
					
					if(!adjustETflag&&etcm.getB().adjustET){
						
						adjustETflag = true;
					}
					if(!adjustRainflag&&etcm.getB().adjustRain){
						
						adjustRainflag = true;
					}	
					Cookie[] results = Util.calculateLossProcess("et_controller",etcm);			
					for(Cookie result : results){		
						resp.addCookie(result);			
					}							
				}
				
			}
			if(adjustRainflag){
				
				throw new IOException(" Estimated FAWN Rain Data. This is the adjusted result ! ");
				
			}
			if(adjustETflag){
				
				throw new IOException(" Estimated FAWN ET Data. This is the adjusted result ! ");
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.log(Level.SEVERE, e.getMessage());
			Cookie errorflag = new Cookie("errorflag",e.getMessage());
			errorflag.setMaxAge(60*60);
            errorflag.setPath("/");
            resp.addCookie(errorflag);
		}
		
		resp.sendRedirect("/results.html");
		
		
	}
	
}

