package com.project.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.project.model.ETControllerModel;
import com.project.model.timeBasedModel;
import com.project.model.timeBasedRainSensorModel;
import com.project.model.timeBasedSoilSensorModel;
import com.project.po.Data;

public class calculateServlet extends HttpServlet{
	
	

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PrintWriter out = resp.getWriter();
		//String zippath = req.getRealPath("./zips.txt");
		//String fawnpath = req.getRealPath("./fawn_zips.txt");
		//Cookie[] cookies = req.getCookies();
		//System.out.println(cookies[1]);
		//System.out.println();
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
				
			}else if(jsonObject.has("isystem")){
				
				isystem = jsonObject.getString("isystem");
				irriDuration = jsonObject.getInt("irriDuration");
				//calculate the irriDepth for different system
				if(isystem =="micro-irrigation-head"){
					
					irriDepth = 0.25 * irriDuration/60;
					
				}else if(isystem =="fixed-irrigation-head"){
					
					irriDepth = 1.5 *irriDuration/60;
					
				}else if(isystem =="gear-driven-irrigation-head"){
					
					irriDepth = 0.5 * irriDuration/60;
					
				}else if(isystem =="impact-irrigation-head"){
					
					irriDepth = 0.5 * irriDuration/60;
					
				}
				
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
					JSONObject resultJSON = tbm.calculation();
					System.out.println(resultJSON.get("wLostDay"));
					System.out.println(resultJSON.get("Hour"));
					//resp.setContentType("text/csv");
					//String disposition = "attachment;filename=time-base-result.csv";
					//resp.setHeader("Content-Disposition", disposition);
					out = resp.getWriter();
					
					//ut.print("sdsdf");
					out.write(resultJSON.toString());
					//out.println(resultJSON.toString());
					
				}else if(system.equals("Time-based with rain sensor")){
					
					System.out.println("Time-based with rain sensor");
					timeBasedRainSensorModel tbrsm = new timeBasedRainSensorModel(soilType,area,rootDepth,zipcode,unit,rainsettings);
					System.out.println(tbrsm.getRootDepth());
					System.out.println(tbrsm.getUnit());
					System.out.println(tbrsm.getArea());
					System.out.println(tbrsm.getSoilType());
					tbrsm.getLocation().print();
					tbrsm.calculation();
					
				}else if(system.equals("Time-based with soil moisture sensor")){
					
					System.out.println("Time-based with soil moisture sensor");
					timeBasedSoilSensorModel tbssm = new timeBasedSoilSensorModel(soilType,area,rootDepth,zipcode,unit,soilthreshold);
					System.out.println(tbssm.getRootDepth());
					System.out.println(tbssm.getUnit());
					System.out.println(tbssm.getArea());
					System.out.println(tbssm.getSoilType());
					tbssm.getLocation().print();
					tbssm.calculation();
					
				}else{
					
					System.out.println("Evapotranspiration Controller");
					ETControllerModel etcm = new ETControllerModel(soilType,area,rootDepth,zipcode,unit);
					System.out.println(etcm.getRootDepth());
					System.out.println(etcm.getUnit());
					System.out.println(etcm.getArea());
					System.out.println(etcm.getSoilType());
					etcm.getLocation().print();
					etcm.calculation();
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

	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = resp.getWriter();
		Cookie[] cookie = req.getCookies();
		double rainsettings = 0.0;
		double soilthreshold = 0.0;
		double irriDepth = 0.0;
		String isystem = "";
		int irriDuration = 0;
		String email = "";
		String password = "";
		String soilType ="";
		String zipcode ="";
		String unit = "";
		double rootDepth =0.0;
		double area = 0.0;
		String[] systemSelection = null;
		String[] days = null;
		String[] hours = null;
		String choice = req.getParameter("correspondence");
		//System.out.println("choice: "+choice);
		for(int i = 0; i< cookie.length; i++){
			
			if(cookie[i].getName().equals("email")){
				
				email = URLDecoder.decode(cookie[i].getValue(),"UTF-8");
				//System.out.println(email);
			}else if(cookie[i].getName().equals("password")){
				
				password =URLDecoder.decode(cookie[i].getValue(),"UTF-8");
				//System.out.println(password);
			}else if (cookie[i].getName().equals("soilType")){
				
				soilType = URLDecoder.decode(cookie[i].getValue(),"UTF-8");
				//System.out.println(soilType);
			}else if(cookie[i].getName().equals("zipcode")){
				
			    zipcode = URLDecoder.decode(cookie[i].getValue(),"UTF-8");
			   // System.out.println(soilType);
			}else if(cookie[i].getName().equals("unit")){
				
				unit = URLDecoder.decode(cookie[i].getValue(),"UTF-8");
				
			}else if(cookie[i].getName().equals("rd")){
				
				rootDepth =Double.parseDouble(cookie[i].getValue());
				
			}else if(cookie[i].getName().equals("area")){
				
				area = Double.parseDouble(cookie[i].getValue());
				
			}else if(cookie[i].getName().equals("systemSelection")){
				
				systemSelection = URLDecoder.decode(cookie[i].getValue(),"UTF-8").split(",");
				
			}else if(cookie[i].getName().equals("rainsettings")){
				
				rainsettings = Double.parseDouble(cookie[i].getValue());
				
			}else if(cookie[i].getName().equals("soilthreshold")){
				
				soilthreshold = Double.parseDouble(cookie[i].getValue());
				
			}else if(cookie[i].getName().equals("irriDepth")){
				
				irriDepth = Double.parseDouble(cookie[i].getValue());
				
			}else if(cookie[i].getName().equals("isystem")){
				
				isystem = URLDecoder.decode(cookie[i].getValue(),"UTF-8");
				irriDuration = Integer.parseInt(cookie[i].getValue());
				if(isystem =="micro-irrigation-head"){
					
					irriDepth = 0.25 * irriDuration/60;
					
				}else if(isystem =="fixed-irrigation-head"){
					
					irriDepth = 1.5 *irriDuration/60;
					
				}else if(isystem =="gear-driven-irrigation-head"){
					
					irriDepth = 0.5 * irriDuration/60;
					
				}else if(isystem =="impact-irrigation-head"){
					
					irriDepth = 0.5 * irriDuration/60;
					
				}
				
			}else if(cookie[i].getName().equals("days")){
				
				days = URLDecoder.decode(cookie[i].getName(),"UTF-8").split(",");
				
			}else if(cookie[i].getName().equals("hours")){
				
				hours = URLDecoder.decode(cookie[i].getName(),"UTF-8").split(",");
				
			}/*else if(cookie[i].getName().equals("choice")){
				
				choice = URLDecoder.decode(cookie[i].getValue(),"UTF-8");
				
			}*/
		}
		
		for (String system : systemSelection){
			
			if (system.equals("Time-based")){
				
				System.out.println("Time-based");
				
				timeBasedModel tbm = new timeBasedModel(soilType,area,rootDepth,zipcode,unit);
				System.out.println(tbm.getRootDepth());
				System.out.println(tbm.getUnit());
				System.out.println(tbm.getArea());
				System.out.println(tbm.getSoilType());
				tbm.getLocation().print();
				
				try{
					JSONObject resultJSON = tbm.calculation();
					Cookie time_base_waterLoss = new Cookie("time_base_waterLoss",String.valueOf(tbm.getwLostWeek()));
					time_base_waterLoss.setMaxAge(60*60);
					time_base_waterLoss.setPath("/");
					Cookie time_base_iLoss = new Cookie("time_base_iLoss",String.valueOf(tbm.getiLostWeek()));
					time_base_iLoss.setMaxAge(60*60);
					time_base_iLoss.setPath("/");
					resp.addCookie(time_base_waterLoss);
					resp.addCookie(time_base_iLoss);
					System.out.println(resultJSON.get("wLostDay"));
					System.out.println(resultJSON.get("Hour"));
					/*
					resp.setContentType("text/csv");
					
					String disposition = "attachment;fileName=time-base-result.csv";
					resp.setHeader("Content-Disposition", disposition);
					//resp.sendRedirect("/results.html");
					out = resp.getWriter();
					
					//ut.print("sdsdf");
					//out.write(resultJSON.toString());
					out.println("Hour,ET,WB,SWC,DELTA,F,rateF,Q,Inf,PERC,Loss,PerLoss,Rhr,wLostHr,wLostDay,iLostHr,iLostDay");
					for(int i =0;i<tbm.getB().Hour.size();i++){
						
						out.println(tbm.getB().Hour.get(i)+","+tbm.getET().get(i)+","+tbm.getWB().get(i)+","+tbm.getSWC().get(i+1)+","+tbm.getDelta().get(i)+","
						        +tbm.getF().get(i)+","+tbm.getRateF().get(i)+","+tbm.getQ().get(i)+","
						        +tbm.getInF().get(i)+","+tbm.getPERC().get(i)+","+tbm.getLoss().get(i)+","+tbm.getPerLoss().get(i)+","+tbm.getB().Rhr.get(i)+","+tbm.getwLostHr().get(i)+","
						        +tbm.getwLostDay().get(i)+","+tbm.getiLostHr().get(i)+","+tbm.getiLostDay().get(i));
						
					}
					out.flush();
					out.close();
					*/
				}catch(JSONException e){
					e.printStackTrace();
				}
				
				//resp.sendRedirect("/result.html");
			}else if(system.equals("Time-based with rain sensor")){
				
				System.out.println("Time-based with rain sensor");
				timeBasedRainSensorModel tbrsm = new timeBasedRainSensorModel(soilType,area,rootDepth,zipcode,unit,rainsettings);
				System.out.println(tbrsm.getRootDepth());
				System.out.println(tbrsm.getUnit());
				System.out.println(tbrsm.getArea());
				System.out.println(tbrsm.getSoilType());
				tbrsm.getLocation().print();
				try{
					JSONObject resultJSON = tbrsm.calculation();
					Cookie rain_sensor_waterLoss = new Cookie("rain_sensor_waterLoss",String.valueOf(tbrsm.getwLostWeek()));
					rain_sensor_waterLoss.setMaxAge(60*60);
					rain_sensor_waterLoss.setPath("/");
					Cookie rain_sensor_iLoss = new Cookie("rain_sensor_iLoss",String.valueOf(tbrsm.getiLostWeek()));
					rain_sensor_iLoss.setMaxAge(60*60);
					rain_sensor_iLoss.setPath("/");
					resp.addCookie(rain_sensor_waterLoss);
					resp.addCookie(rain_sensor_iLoss);
					System.out.println(resultJSON.get("wLostDay"));
					System.out.println(resultJSON.get("Hour"));
					/*
					resp.setContentType("text/csv");
					String disposition = "attachment;fileName=rain-sensor-result.csv";
					resp.setHeader("Content-Disposition", disposition);
					out = resp.getWriter();
					
					//ut.print("sdsdf");
					//out.write(resultJSON.toString());
					out.println("Hour,Rhr,RainSum,IhrRain,WB,SWC,DELTA,F,rateF,Q,Inf,PERC,Loss,PerLoss,wLostHr,wLostDay,iLostHr,iLostDay");
					for(int i =0;i<tbrsm.getB().Hour.size();i++){
						
						out.println(tbrsm.getB().Hour.get(i)+","+tbrsm.getB().Rhr.get(i)+","+tbrsm.getRainSum()+","+tbrsm.getIhrRain()+","+tbrsm.getET().get(i)+","+tbrsm.getWB().get(i)+","+tbrsm.getSWC().get(i+1)+","+tbrsm.getDelta().get(i)+","
						        +tbrsm.getF().get(i)+","+tbrsm.getRateF().get(i)+","+tbrsm.getQ().get(i)+","
						        +tbrsm.getInF().get(i)+","+tbrsm.getPERC().get(i)+","+tbrsm.getLoss().get(i)+","+tbrsm.getPerLoss().get(i)+","+tbrsm.getwLostHr().get(i)+","
						        +tbrsm.getwLostDay().get(i)+","+tbrsm.getiLostHr().get(i)+","+tbrsm.getiLostDay().get(i));
						
					}
					*/
				}catch(JSONException e){
					e.printStackTrace();
				}
				
			}else if(system.equals("Time-based with soil moisture sensor")){
				
				System.out.println("Time-based with soil moisture sensor");
				timeBasedSoilSensorModel tbssm = new timeBasedSoilSensorModel(soilType,area,rootDepth,zipcode,unit,soilthreshold);
				System.out.println(tbssm.getRootDepth());
				System.out.println(tbssm.getUnit());
				System.out.println(tbssm.getArea());
				System.out.println(tbssm.getSoilType());
				tbssm.getLocation().print();
				//tbssm.calculation();
				try{
					JSONObject resultJSON = tbssm.calculation();
					Cookie soil_sensor_waterLoss = new Cookie("soil_sensor_waterLoss",String.valueOf(tbssm.getwLostWeek()));
					soil_sensor_waterLoss.setMaxAge(60*60);
					soil_sensor_waterLoss.setPath("/");
					Cookie soil_sensor_iLoss = new Cookie("soil_sensor_iLoss",String.valueOf(tbssm.getiLostWeek()));
					soil_sensor_iLoss.setMaxAge(60*60);
					soil_sensor_iLoss.setPath("/");
					resp.addCookie(soil_sensor_waterLoss);
					resp.addCookie(soil_sensor_iLoss);
					System.out.println(resultJSON.get("wLostDay"));
					System.out.println(resultJSON.get("Hour"));
					/*
					resp.setContentType("text/csv");
					String disposition = "attachment;fileName=soil-sensor-result.csv";
					resp.setHeader("Content-Disposition", disposition);
					out = resp.getWriter();
					
					//ut.print("sdsdf");
					//out.write(resultJSON.toString());
					out.println("Hour,Rhr,IhrSoil,ET,WB,SWC,DELTA,F,rateF,Q,Inf,PERC,Loss,PerLoss,wLostHr,wLostDay,iLostHr,iLostDay");
					for(int i =0;i<tbssm.getB().Hour.size();i++){
						
						out.println(tbssm.getB().Hour.get(i)+","+tbssm.getB().Rhr.get(i)+","+tbssm.getIhrsoil()+","+tbssm.getET().get(i)+","+tbssm.getWB().get(i)+","+tbssm.getSWC().get(i+1)+","+tbssm.getDelta().get(i)+","
						        +tbssm.getF().get(i)+","+tbssm.getRateF().get(i)+","+tbssm.getQ().get(i)+","
						        +tbssm.getInF().get(i)+","+tbssm.getPERC().get(i)+","+tbssm.getLoss().get(i)+","+tbssm.getPerLoss().get(i)+","+tbssm.getwLostHr().get(i)+","
						        +tbssm.getwLostDay().get(i)+","+tbssm.getiLostHr().get(i)+","+tbssm.getiLostDay().get(i));
						
					}
					*/
				}catch(JSONException e){
					e.printStackTrace();
				}
				
			}else{
				
				System.out.println("Evapotranspiration Controller");
				ETControllerModel etcm = new ETControllerModel(soilType,area,rootDepth,zipcode,unit);
				System.out.println(etcm.getRootDepth());
				System.out.println(etcm.getUnit());
				System.out.println(etcm.getArea());
				System.out.println(etcm.getSoilType());
				etcm.getLocation().print();
				try{
					JSONObject resultJSON = etcm.calculation();
					Cookie et_controller_waterLoss = new Cookie("et_controller_waterLoss",String.valueOf(etcm.getwLostWeek()));
					et_controller_waterLoss.setMaxAge(60*60);
					et_controller_waterLoss.setPath("/");
					Cookie et_controller_iLoss = new Cookie("et_controller_iLoss",String.valueOf(etcm.getiLostWeek()));
					et_controller_iLoss.setMaxAge(60*60);
					et_controller_iLoss.setPath("/");
					resp.addCookie(et_controller_waterLoss);
					resp.addCookie(et_controller_iLoss);
					System.out.println(resultJSON.get("wLostDay"));
					System.out.println(resultJSON.get("Hour"));
					/*
					resp.setContentType("text/csv");
					String disposition = "attachment;fileName=ET-controller-result.csv";
					resp.setHeader("Content-Disposition", disposition);
					out = resp.getWriter();
					
					//ut.print("sdsdf");
					//out.write(resultJSON.toString());
					out.println("Hour,Rhr,Re,Ihrshcdule,ET,Ick1,Ick2,AWRSTEP1,AWRSTEP2,AWR,Ihret,WB,SWC,DELTA,F,rateF,Q,Inf,PERC,Loss,PerLoss,wLostHr,wLostDay,iLostHr,iLostDay");
					for(int i =0;i<etcm.getB().Hour.size()-1;i++){
						
						out.println(etcm.getB().Hour.get(i)+","+etcm.getB().Rhr.get(i)+","+etcm.getRe()+","+etcm.getB().Ihrschedule+","+etcm.getET().get(i)+","+etcm.getIck1()+","+etcm.getIck2()+","+etcm.getAWRstep1()+","+etcm.getAWRstep2()+","+etcm.getAWR()+","+etcm.getIhret()+","+etcm.getWB().get(i)+","+etcm.getSWC().get(i+1)+","+etcm.getDelta().get(i)+","
						        +etcm.getF().get(i)+","+etcm.getRateF().get(i)+","+etcm.getQ().get(i)+","
						        +etcm.getInF().get(i)+","+etcm.getPERC().get(i)+","+etcm.getLoss().get(i)+","+etcm.getPerLoss().get(i)+","+etcm.getwLostHr().get(i)+","
						        +etcm.getwLostDay().get(i)+","+etcm.getiLostHr().get(i)+","+etcm.getiLostDay().get(i));
						
					}
					*/
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
			
		}
		resp.sendRedirect("/results.html");
		
	}
	
	
	

}
