package com.project.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.model.ETControllerModel;
import com.project.model.TimeBasedModel;
import com.project.model.TimeBasedRainSensorModel;
import com.project.model.TimeBasedSoilSensorModel;
import com.project.po.Data;
import com.project.po.DataBase;
import com.project.po.Util;

/**
 * Created with MyEclipse
 * User : Dawei Jia
 * Date : 11/19/2013
 * @author Dawei Jia
 * controller for unsubscribe weeklyreport and changesecret createsecret
 */
public class ControllerServlet extends HttpServlet{
	
	private static final Logger logger = Logger.getLogger(ControllerServlet.class.getCanonicalName());
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
		
		PrintWriter out = resp.getWriter();
		String path = req.getServletPath();
		Hashtable<String,Data> records = new Hashtable<String,Data>();
		DateFormat df = new SimpleDateFormat("MMM dd yyyy", Locale.US);
		logger.log(Level.INFO, path);
		
		if(path.contains("/weeklyReport")){
			
			DataBase db = new DataBase("User");
			records = db.fetchAll();
			//System.out.println(records.size());
			if(records != null){
				
				Enumeration<String> enumeration = records.keys();
				while(enumeration.hasMoreElements()){
					
					String email = (String) enumeration.nextElement();
					//logger.log(Level.INFO, "Calculating "+ email + "'s water use");
					Data data = db.fetch(email);
					String choice = data.getChoice();
					if(choice.equals("no")){
						
						logger.log(Level.INFO, email + " is unsubscribed");
						continue;	
					}else{
						try{
							
							logger.log(Level.INFO, "Calculating " + email +" 's water use");
							//StringBuilder sb = new StringBuilder();
							Hashtable<String,String> results = new Hashtable<String,String>();
							//sb.append("Technology , waterLoss(gallons) , waterLossPercentgae , waterStressDays , rainfall(inch)\r\n");
							String[] systemSelection = data.getSystemSelection();
							for(String system : systemSelection){
							
								if(system.equals("Time-based")){
								
									logger.log(Level.INFO, "Time-based");
									TimeBasedModel tbm = new TimeBasedModel(data);
									results.put("Time-based", Util.buildWeeklyReportResult(df, tbm));
			
								}else if(system.equals("Time-based with rain sensor")){
									
									logger.log(Level.INFO, "Time-based with rain sensor");
						
									TimeBasedRainSensorModel tbrsm = new TimeBasedRainSensorModel(data);
									
									results.put("Time-based with rain sensor", Util.buildWeeklyReportResult(df, tbrsm));
								
								}else if(system.equals("Time-based with soil moisture sensor")){
								
									logger.log(Level.INFO, "Time-based with soil moisture sensor");

									TimeBasedSoilSensorModel tbssm = new TimeBasedSoilSensorModel(data);
			
									results.put("Time-based with soil moisture sensor",Util.buildWeeklyReportResult(df, tbssm));
									
								
								}else{
								
									logger.log(Level.INFO, "Evaprtranspiration Controller");
									
									ETControllerModel etcm = new ETControllerModel(data);		
									
									results.put("ET_Controller",Util.buildWeeklyReportResult(df, etcm));
								
								}//end_if
														
							}//end_for
							
							String sent = Util.requestWeeklyReport(results, email);
							logger.log(Level.INFO,sent);
							logger.log(Level.INFO, "Sending email to "+ email);
						}catch(Exception e){
							
							logger.log(Level.WARNING, e.getMessage());
						}
						
					}//end_if
					
				}//end_while
				
				
			}//end_if
			
		}else if(path.contains("/unsubscribe")){
			
			String email = req.getParameter("email").trim();
			String userToken = req.getParameter("token").trim();
			String timestamp = req.getParameter("timestamp").trim();
			
			try{
				
				String token = Util.createToken(timestamp, email);
				if(userToken.equals(token)){
					
					DataBase db = new DataBase("User");
					Data data = db.fetch(email);
					if(data !=null){
						
						data.setChoice("no");
						db.insertIntoDataBase(data);
						out.println("You have unsubscribed successfully");
						
					}else{
						
						logger.log(Level.INFO, email+": Sorry. We can not find your record. Please contact webmaster@fawn.ifas.ufl.edu");
						out.println("Sorry. We can not find your record. Please contact webmaster@fawn.ifas.ufl.edu");
						
					}
					
					
					
				}else{
					
					logger.log(Level.INFO, "User token doesn't match: "+ email + "," + token +"," + timestamp);
					out.println("Sorry. Please contact webmaster@fawn.ifas.ufl.edu to complete request.");
				}
				
				
			}catch(NoSuchAlgorithmException e){
				
				e.printStackTrace();
				
			}
			
			
			
		}else if(path.contains("/changeSecret")){
			
			// http://1.irrigation-tool.appspot.com/changeSecret?old=xxxx&new=xxxx
			String oldSecret = req.getParameter("old");
			String newSecret = req.getParameter("new");
			
			DataBase db = new DataBase("Secret");
			if(oldSecret == null || newSecret == null){
				
				out.println("missing parameter");
			}else{
				
				oldSecret = oldSecret.trim();
				newSecret = newSecret.trim();
				
			}
			try{
				
				String dbOldSecret = db.fetch("secret","secret");
				
				if(dbOldSecret.equals(oldSecret)){
					
					db.replace("secret", "secret", newSecret.trim());
					out.println("true "+db.fetch("secret", "secret"));
					
					
				}else{
					out.println("invalid secret! ");
					
				}
				
			}catch(Exception e){
				
				e.printStackTrace();
				out.println("update failed");
				
			}
			
			
			
		}else if(path.contains("createSecret")){
			
			DataBase db = new DataBase("Secret");
			db.replace("secret", "secret", "jiadw007");
			
			logger.log(Level.INFO, "create secret !");
			out.println("finish create secret ! ");
			//System.out.println("finish create secret !");
			
			
			
		}else if(path.contains("emailtest")){
			
			DataBase db = new DataBase("User");
			Data data = db.fetch("jiadw007@gmail.com");
			//System.out.println(records.size());

			String choice = data.getChoice();
			if(choice.equals("no")){
						
				logger.log(Level.INFO, "jiadw007@gmail.com" + " is unsubscribed");
					
			}else{
				try{
							
					logger.log(Level.INFO, "Calculating " + "jiadw007@gmail.com" +" 's water use");
					//StringBuilder sb = new StringBuilder();
					Hashtable<String,String> results = new Hashtable<String,String>();
					//sb.append("Technology , waterLoss(gallons) , waterLossPercentgae , waterStressDays , rainfall(inch)\r\n");
					String[] systemSelection = data.getSystemSelection();
					for(String system : systemSelection){
						
						if(system.equals("Time-based")){
							
							logger.log(Level.INFO, "Time-based");
							TimeBasedModel tbm = new TimeBasedModel(data);
							System.out.println("email_1");		
							results.put("Time-based", Util.buildWeeklyReportResult(df, tbm));
			
						}else if(system.equals("Time-based with rain sensor")){
									
							logger.log(Level.INFO, "Time-based with rain sensor");
						
							TimeBasedRainSensorModel tbrsm = new TimeBasedRainSensorModel(data);	
							System.out.println("email_2");
							results.put("Time-based with rain sensor", Util.buildWeeklyReportResult(df, tbrsm));
								
						}else if(system.equals("Time-based with soil moisture sensor")){
								
							logger.log(Level.INFO, "Time-based with soil moisture sensor");

							TimeBasedSoilSensorModel tbssm = new TimeBasedSoilSensorModel(data);	
							System.out.println("email_3");	
							results.put("Time-based with soil moisture sensor",Util.buildWeeklyReportResult(df, tbssm));
									
								
						}else{
								
							logger.log(Level.INFO, "Evaprtranspiration Controller");

							ETControllerModel etcm = new ETControllerModel(data);
	
							results.put("ET_Controller",Util.buildWeeklyReportResult(df, etcm));						
						}//end_if
														
					}//end_for
							
					String sent = Util.requestWeeklyReport(results, "jiadw007@gmail.com");
					out.println(sent);
					logger.log(Level.INFO,sent);
					logger.log(Level.INFO, "Sending email to "+ "jiadw007@gmail.com");
				}catch(Exception e){
					e.printStackTrace();		
					logger.log(Level.WARNING, e.getMessage());
				}
			}
		}//end_if
		
	}

}
