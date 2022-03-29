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
import com.project.model.Hydrology;
import com.project.model.SystemGeneratorFactory;
import com.project.po.BaseData;
import com.project.po.Data;
import com.project.po.DataBase;
import com.project.po.Util;

/**
 * Created with MyEclipse
 * User : Dawei Jia
 * Date : 11/19/2013
 * @author Dawei Jia
 * controller for unsubscribe weeklyreport and changesecret createsecret
 * irrigation-tool.appspot.com/weeklyReport
 * irrigation-tool.appspot.com/unsubscribe
 * irrigation-tool.appspot.com/emailtest
 * irrigation-tool.appspot.com/changeSecret
 * irrigation-tool.appspot.com/createSecret
 * irrigatoin-tool.appspot.com/tooltest
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
							BaseData b = new BaseData(data.getZipcode(),data.getDays(),data.getHours(),data.getIrriDepth());
							logger.log(Level.INFO, "Calculating " + email +" 's water use");
							//StringBuilder sb = new StringBuilder();
							Hashtable<String,String> results = new Hashtable<String,String>();
							//sb.append("Technology , waterLoss(gallons) , waterLossPercentgae , waterStressDays , rainfall(inch)\r\n");
							String[] systemSelection = data.getSystemSelection();
							for(String system : systemSelection){
								
								logger.log(Level.INFO, system);
								Hydrology hydrology = SystemGeneratorFactory.createModel(system, data, b);
								results.put(system, Util.buildWeeklyReportResult(df, hydrology));
														
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
			db.replace("secret", "secret", "Aa12345678");
			
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
					BaseData b = new BaseData(data.getZipcode(),data.getDays(),data.getHours(),data.getIrriDepth());		
					logger.log(Level.INFO, "Calculating " + "jiadw007@gmail.com" +" 's water use");
					//StringBuilder sb = new StringBuilder();
					Hashtable<String,String> results = new Hashtable<String,String>();
					//sb.append("Technology , waterLoss(gallons) , waterLossPercentgae , waterStressDays , rainfall(inch)\r\n");
					String[] systemSelection = data.getSystemSelection();
					for(String system : systemSelection){
						
						logger.log(Level.INFO, system);
						Hydrology hydrology = SystemGeneratorFactory.createModel(system, data, b);
						results.put(system, Util.buildWeeklyReportResult(df, hydrology));
														
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
		}else if(path.contains("tooltest")){
			
			DataBase db = new DataBase("User");
			Data data = db.fetch("jiadw007@gmail.com");
			out.println("Getting user's settings information from database... Example: jiadw007@gmail.com<br/>");
			try{
				
				BaseData b = new BaseData(data.getZipcode(),data.getDays(),data.getHours(),data.getIrriDepth());
				out.println("Calculating " + "jiadw007@gmail.com" +" 's water use...<br/>");
				//Hashtable<String,String> results = new Hashtable<String,String>();
				//StringBuilder sb = new StringBuilder();
				//sb.append("Technology , waterLoss(gallons) , waterLossPercentgae , waterStressDays , rainfall(inch)\r\n");
				String[] systemSelection = data.getSystemSelection();
				for(String system : systemSelection){		
					out.println("Calculate " + system + " model... <br />");
					String[] result = {};
					Hydrology hydrology = SystemGeneratorFactory.createModel(system, data, b);
					out.println("Finish calculation in Time-based model ! <br />");
					//System.out.println("email_time_based");		
					result = Util.buildWeeklyReportResult(df, hydrology).split(",");
					out.println("<b>"+system+" model:</b><br/>");
					out.println("Start Date: " + result[0] + "<br />End Date: " + result[1] + "<br />");
					out.println("Water Loss: "+ result[2] + "<br />Percentage Loss: " + result[3] + "<br />");
					out.println("Fawn station name: " + result[4] + "<br />");
					out.println("Rain fall weekly: " + result[5] + "<br />");
					out.println("Irrigatoin applied: " + result[6] + "<br />");													
				}//end_for
				
			}catch(Exception e){
				out.println("Error message: "+e.getMessage()+"<br />");		
				e.printStackTrace();
				logger.log(Level.WARNING, e.getMessage());
			}
			
			
		}//end_if
		
}

}
