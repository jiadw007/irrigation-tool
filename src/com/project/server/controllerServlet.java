package com.project.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.project.model.timeBasedModel;
import com.project.model.timeBasedRainSensorModel;
import com.project.model.timeBasedSoilSensorModel;
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
		DateFormat df = new SimpleDateFormat("MMM dd yyyy", Locale.US);
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
						try{
							
							logger.log(Level.INFO, "Calculating " + email +" 's water use");
							//StringBuilder sb = new StringBuilder();
							Hashtable<String,String> results = new Hashtable<String,String>();
							//sb.append("Technology , waterLoss(gallons) , waterLossPercentgae , waterStressDays , rainfall(inch)\r\n");
							String[] systemSelection = data.getSystemSelection();
							for(String system : systemSelection){
							
								if(system.equals("Time-based")){
								
									logger.log(Level.INFO, "Time-based");
									timeBasedModel tbm = new timeBasedModel(data.getSoilType(),data.getArea(),data.getRootDepth(),data.getZipcode(),data.getUnit(),data.getDays(),data.getHours(),data.getIrriDepth());
									tbm.calculation();
									String startDate = df.format(tbm.getB().startDate.getTime());
									String endDate = df.format(tbm.getB().endDate.getTime());
									Double waterLoss = tbm.getwLostWeek();
									Double iLoss = tbm.getiLostWeek();
									Double rainfall = tbm.getB().getRainFallPerWeek();
									//int wStressDays = tbm.getwStressDays();
									String fawnName = tbm.getLocation().getFawnStnName();
									//double fawnDistance = tbm.getLocation().distance;
									double irriDepth = 0.0;
									if(data.getUnit().equals("Metric")){
										
										BigDecimal dividend = new BigDecimal(data.getIrriDepth());
										BigDecimal divisor = new BigDecimal(2.54);
										irriDepth = dividend.divide(divisor, 2).doubleValue();
										
									}else{
										
										irriDepth = data.getIrriDepth();
										
									}
									
									//sb.append("Time-based , "+String.valueOf(waterLoss)+" , "+String.valueOf(iLoss)+"% , "+String.valueOf(wStressDays)+" , "+String.valueOf(rainfall)+"\r\n");
									//results.put("Time-based", startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+String.valueOf(wStressDays)+","+fawnName+","+String.valueOf(fawnDistance)+","+String.valueOf(rainfall)+","+String.valueOf(irriDepth));
									results.put("Time-based", startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+fawnName+","+String.valueOf(rainfall)+","+String.valueOf(irriDepth));
			
								}else if(system.equals("Time-based with rain sensor")){
									
									logger.log(Level.INFO, "Time-based with rain sensor");
						
									timeBasedRainSensorModel tbrsm = new timeBasedRainSensorModel(data.getSoilType(),data.getArea(),data.getRootDepth(),data.getZipcode(),data.getUnit(),data.getRainsettings(),data.getDays(),data.getHours(),data.getIrriDepth());
									tbrsm.calculation();
									String startDate = df.format(tbrsm.getB().startDate.getTime());
									String endDate = df.format(tbrsm.getB().endDate.getTime());
									Double waterLoss = tbrsm.getwLostWeek();
									Double iLoss = tbrsm.getiLostWeek();
									Double rainfall = tbrsm.getB().getRainFallPerWeek();
									//int wStressDays = tbrsm.getwStressDays();
									String fawnName = tbrsm.getLocation().getFawnStnName();
									//double fawnDistance = tbrsm.getLocation().distance;
									double irriDepth = data.getIrriDepth()/2.54;
									
									
									//sb.append("Time-based , "+String.valueOf(waterLoss)+" , "+String.valueOf(iLoss)+"% , "+String.valueOf(wStressDays)+" , "+String.valueOf(rainfall)+"\r\n");
									//results.put("Time-based with rain sensor",startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+String.valueOf(wStressDays)+","+fawnName+","+String.valueOf(fawnDistance)+","+String.valueOf(rainfall)+","+String.valueOf(irriDepth));
									results.put("Time-based with rain sensor", startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+fawnName+","+String.valueOf(rainfall)+","+String.valueOf(irriDepth));
								
								}else if(system.equals("Time-based with soil moisture sensor")){
								
									logger.log(Level.INFO, "Time-based with soil moisture sensor");

									timeBasedSoilSensorModel tbssm = new timeBasedSoilSensorModel(data.getSoilType(),data.getArea(),data.getRootDepth(),data.getZipcode(),data.getUnit(),data.getSoilthreshold(),data.getDays(),data.getHours(),data.getIrriDepth());
									tbssm.calculation();
									String startDate = df.format(tbssm.getB().startDate.getTime());
									String endDate = df.format(tbssm.getB().endDate.getTime());
									Double waterLoss = tbssm.getwLostWeek();
									Double iLoss = tbssm.getiLostWeek();
									Double rainfall = tbssm.getB().getRainFallPerWeek();
									//int wStressDays = tbssm.getwStressDays();
									String fawnName = tbssm.getLocation().getFawnStnName();
									//double fawnDistance = tbssm.getLocation().distance;
									double irriDepth = data.getIrriDepth()/2.54;
									
									
									//sb.append("Time-based , "+String.valueOf(waterLoss)+" , "+String.valueOf(iLoss)+"% , "+String.valueOf(wStressDays)+" , "+String.valueOf(rainfall)+"\r\n");
									//results.put("Time-based with soil moisture sensor",startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+String.valueOf(wStressDays)+","+fawnName+","+String.valueOf(fawnDistance)+","+String.valueOf(rainfall)+","+String.valueOf(irriDepth));
									results.put("Time-based with soil moisture sensor",startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+fawnName+","+String.valueOf(rainfall)+","+String.valueOf(irriDepth));
									
								
								}else{
								
									logger.log(Level.INFO, "Evaprtranspiration Controller");

									ETControllerModel etcm = new ETControllerModel(data.getSoilType(),data.getArea(),data.getRootDepth(),data.getZipcode(),data.getUnit(),data.getDays(),data.getHours(),data.getIrriDepth());
									etcm.calculation();
									String startDate = df.format(etcm.getB().startDate.getTime());
									String endDate = df.format(etcm.getB().endDate.getTime());
									Double waterLoss = etcm.getwLostWeek();
									Double iLoss = etcm.getiLostWeek();
									Double rainfall = etcm.getB().getRainFallPerWeek();
									//int wStressDays = etcm.getwStressDays();
									String fawnName = etcm.getLocation().getFawnStnName();
									//double fawnDistance = etcm.getLocation().distance;
									double irriDepth = data.getIrriDepth()/2.54;
									
									
									//sb.append("Time-based , "+String.valueOf(waterLoss)+" , "+String.valueOf(iLoss)+"% , "+String.valueOf(wStressDays)+" , "+String.valueOf(rainfall)+"\r\n");
									//results.put("ET_Controller",startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+String.valueOf(wStressDays)+","+fawnName+","+String.valueOf(fawnDistance)+","+String.valueOf(rainfall)+","+String.valueOf(irriDepth));
									results.put("ET_Controller",startDate+","+endDate+","+String.valueOf(waterLoss)+","+String.valueOf(iLoss)+"%,"+fawnName+","+String.valueOf(rainfall)+","+String.valueOf(irriDepth));
								
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
			
			
			
		}else if(path.contains("/changeSecret")){
			
			// http://1.irrigation.appspot.com/changesecret?old=xxxx&new=xxxx
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
			
			
			
		}else if(path.contains("createSecret")){
			
			DataBase db = new DataBase("Secret");
			db.replace("secret", "secret", "jiadw007");
			
			logger.log(Level.INFO, "create secret !");
			out.println("finish create secret ! ");
			//System.out.println("finish create secret !");
			
			
			
		}//end_if
		
	}
	
	
	

}
