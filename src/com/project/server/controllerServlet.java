package com.project.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.model.ETControllerModel;
import com.project.model.timeBasedModel;
import com.project.model.timeBasedRainSensorModel;
import com.project.model.timeBasedSoilSensorModel;
import com.project.po.Data;
import com.project.po.DataBase;
import com.project.po.Util;

public class controllerServlet extends HttpServlet{
	
	private static final Logger logger = Logger.getLogger(controllerServlet.class.getCanonicalName());
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		
		PrintWriter out = resp.getWriter();
		String path = req.getServletPath();
		Hashtable<String,Data> records = new Hashtable<String,Data>();
		logger.log(Level.INFO, path);
		if(path.contains("/weeklyReport")){
			
			DataBase db = new DataBase("User");
			records = db.fecthAll();
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
						
						logger.log(Level.INFO, "Calculating " + email +" 's water use");
						//StringBuilder sb = new StringBuilder();
						Hashtable<String,String> results = new Hashtable<String,String>();
						//sb.append("Technology , waterLoss(gallons) , waterLossPercentgae , waterStressDays , rainfall(inch)\r\n");
						String[] systemSelection = data.getSystemSelection();
						for(String system : systemSelection){
							
							if(system.equals("Time-based")){
								
								logger.log(Level.INFO, "Time-based");
								try{
									
									timeBasedModel tbm = new timeBasedModel(data.getSoilType(),data.getArea(),data.getRootDepth(),data.getZipcode(),data.getUnit(),data.getDays(),data.getHours(),data.getIrriDepth());
									tbm.calculation();
									String startDate = DateFormat.getDateInstance().format(tbm.getB().startDate.getTime());
									String endDate = DateFormat.getDateInstance().format(tbm.getB().endDate.getTime());
									Double waterLoss = tbm.getwLostWeek();
									Double iLoss = tbm.getiLostWeek();
									//Double rainfall = tbm.getB().getRainFallPerWeek();
									int wStressDays = tbm.getwStressDays();
									String fawnName = tbm.getLocation().getFawnStnName();
									double fawnDistance = tbm.getLocation().distance;
									
									
									
									//sb.append("Time-based , "+String.valueOf(waterLoss)+" , "+String.valueOf(iLoss)+"% , "+String.valueOf(wStressDays)+" , "+String.valueOf(rainfall)+"\r\n");
									results.put("Time-based", startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+String.valueOf(wStressDays)+","+fawnName+","+String.valueOf(fawnDistance));
									
								}catch(Exception e){
									
									logger.log(Level.WARNING, e.getMessage());
								}
								
								
							}else if(system.equals("Time-based with rain sensor")){
								logger.log(Level.INFO, "Time-based with rain sensor");
								
								try{
									
									timeBasedRainSensorModel tbrsm = new timeBasedRainSensorModel(data.getSoilType(),data.getArea(),data.getRootDepth(),data.getZipcode(),data.getUnit(),data.getRainsettings(),data.getDays(),data.getHours(),data.getIrriDepth());
									tbrsm.calculation();
									String startDate = DateFormat.getDateInstance().format(tbrsm.getB().startDate.getTime());
									String endDate = DateFormat.getDateInstance().format(tbrsm.getB().endDate.getTime());
									Double waterLoss = tbrsm.getwLostWeek();
									Double iLoss = tbrsm.getiLostWeek();
									//Double rainfall = tbm.getB().getRainFallPerWeek();
									int wStressDays = tbrsm.getwStressDays();
									String fawnName = tbrsm.getLocation().getFawnStnName();
									double fawnDistance = tbrsm.getLocation().distance;
									
									
									
									//sb.append("Time-based , "+String.valueOf(waterLoss)+" , "+String.valueOf(iLoss)+"% , "+String.valueOf(wStressDays)+" , "+String.valueOf(rainfall)+"\r\n");
									results.put("Time-based",startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+String.valueOf(wStressDays)+","+fawnName+","+String.valueOf(fawnDistance));
								}catch(Exception e){
									
									logger.log(Level.WARNING, e.getMessage());
									
									
								}
								
								
							}else if(system.equals("Time-based with soil moisture sensor")){
								
								logger.log(Level.INFO, "Time-based with soil moisture sensor");
								try{
									
									timeBasedSoilSensorModel tbssm = new timeBasedSoilSensorModel(data.getSoilType(),data.getArea(),data.getRootDepth(),data.getZipcode(),data.getUnit(),data.getSoilthreshold(),data.getDays(),data.getHours(),data.getIrriDepth());
									tbssm.calculation();
									String startDate = DateFormat.getDateInstance().format(tbssm.getB().startDate.getTime());
									String endDate = DateFormat.getDateInstance().format(tbssm.getB().endDate.getTime());
									Double waterLoss = tbssm.getwLostWeek();
									Double iLoss = tbssm.getiLostWeek();
									//Double rainfall = tbm.getB().getRainFallPerWeek();
									int wStressDays = tbssm.getwStressDays();
									String fawnName = tbssm.getLocation().getFawnStnName();
									double fawnDistance = tbssm.getLocation().distance;
									
									
									
									//sb.append("Time-based , "+String.valueOf(waterLoss)+" , "+String.valueOf(iLoss)+"% , "+String.valueOf(wStressDays)+" , "+String.valueOf(rainfall)+"\r\n");
									results.put("Time-based",startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+String.valueOf(wStressDays)+","+fawnName+","+String.valueOf(fawnDistance));
									
								}catch(Exception e){
									
									logger.log(Level.WARNING, e.getMessage());
									
								}
								
							}else{
								
								logger.log(Level.INFO, "Evaprtranspiration Controller");
								
								try{
									
									ETControllerModel etcm = new ETControllerModel(data.getSoilType(),data.getArea(),data.getRootDepth(),data.getZipcode(),data.getUnit(),data.getDays(),data.getHours(),data.getIrriDepth());
									etcm.calculation();
									String startDate = DateFormat.getDateInstance().format(etcm.getB().startDate.getTime());
									String endDate = DateFormat.getDateInstance().format(etcm.getB().endDate.getTime());
									Double waterLoss = etcm.getwLostWeek();
									Double iLoss = etcm.getiLostWeek();
									//Double rainfall = tbm.getB().getRainFallPerWeek();
									int wStressDays = etcm.getwStressDays();
									String fawnName = etcm.getLocation().getFawnStnName();
									double fawnDistance = etcm.getLocation().distance;
									
									
									
									//sb.append("Time-based , "+String.valueOf(waterLoss)+" , "+String.valueOf(iLoss)+"% , "+String.valueOf(wStressDays)+" , "+String.valueOf(rainfall)+"\r\n");
									results.put("Time-based",startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+String.valueOf(wStressDays)+","+fawnName+","+String.valueOf(fawnDistance));
									
								}catch(Exception e){
									
									logger.log(Level.WARNING, e.getMessage());
								}
								
							}//end_if
														
						}//end_for
						try {
							
							String sent = Util.requestWeeklyReport(results, email);
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.log(Level.WARNING , e.getMessage());
						}
						logger.log(Level.INFO, "Sending email to "+ email);
						
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
					
					DataBase db = new DataBase("user");
					Data data = db.fetch(email);
					if(data !=null){
						
						data.setChoice("no");
						db.insertIntoDataBase(data);
						out.println("You have unsubsribed successfully");
						
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
			
			
			
		}else if(path.contains("/changesecret")){
			
			// http://fawnapps.appspot.com/changesecret?old=xxxx&new=xxxx
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
				
				
				out.println("update failed");
				
			}
			
			
			
		}//end_if
		
	}
	
	
	

}
