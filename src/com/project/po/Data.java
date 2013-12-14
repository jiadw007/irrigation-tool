package com.project.po;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;

/**
 * Create with MyEcplise
 * User : Dawei Jia
 * Date : 9/19/2013
 * @author Dawei Jia
 * po for storing user data in the GAE database
 */
public class Data {
	
	private String email = "";
	
	private String unit = "";
	private String zipcode = "";
	private String soilType = "";
	private double rootDepth = 0.0;
	private double area = 0.0;
	private String[] systemSelection = {};
	private String[] days = {};
	private String[] hours = {};
	//private String[] minutes;
	
	private double rainsettings = 0.0;
	private double soilthreshold = 0.0;
	private double irriDepth =0.0;
	private String choice;
	
	public Data(){
		
		
	}
	
	public Data(Cookie[] cookie, String choice) throws IOException{
		
		int irriDuration =0;
		String isystem = "";
		for(int i = 0; i< cookie.length; i++){
			
			if(cookie[i].getName().equals("email")){
				
				email = URLDecoder.decode(cookie[i].getValue(),"UTF-8");
				//System.out.println(email);
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
				
			}else if(cookie[i].getName().equals("irriDuration")){
				
				irriDuration = Integer.parseInt(cookie[i].getValue());
				
			}else if(cookie[i].getName().equals("isystem")){
				
				isystem = URLDecoder.decode(cookie[i].getValue(),"UTF-8");
				
				if(isystem.equals("micro-irrigation-head")){
					
					irriDepth = 0.25 * irriDuration/60;
					
				}else if(isystem.equals("fixed-irrigation-head")){
					
					irriDepth = 1.5 * irriDuration/60;
					
				}else if(isystem.equals("gear-driven-irrigation-head")){
					
					irriDepth = 0.5 * irriDuration/60;
					
				}else if(isystem.equals("impact-irrigation-head")){
					
					irriDepth = 0.5 * irriDuration/60;
					
				}
				
			}else if(cookie[i].getName().equals("days")){
				
				days = URLDecoder.decode(cookie[i].getValue(),"UTF-8").split(",");
				
			}else if(cookie[i].getName().equals("hours")){
				
				hours = URLDecoder.decode(cookie[i].getValue(),"UTF-8").split(",");
				
			}
		
		}
		/*
		 * unit conversion
		 */
		if(this.getUnit().equals("English")){
			
			this.setIrriDepth(this.getIrriDepth() * 2.54);
			
		}
		this.choice = choice;
		StringBuilder system = new StringBuilder();
		for(String sys : this.systemSelection){
			
			system.append(sys);
			
		}
		if(this.email.equals("")){
			
			throw new IOException("Sorry, you miss your email. Please Sign in !");
		}else if(this.unit.equals("")){
			
			throw new IOException("Sorry, you miss your unit setting. Please go back to set it ! ");
			
		}else if(this.zipcode.equals("")){
			
			throw new IOException("Sorry, you miss your zip code setting. Please go back to set it ! ");
			
		}else if(this.soilType.equals("")){
			
			throw new IOException("Sorry, you miss your soil type setting. Please go back to set it ! ");
			
		}else if(this.rootDepth == 0.0){
			
			throw new IOException("Sorry, you miss your root depth setting. Please go back to set it ! ");
			
		}else if(this.area ==0.0){
			
			throw new IOException("Sorry, you miss your area setting. Please go back to set it ! ");
			
		}else if(this.systemSelection.length == 0){
			
			throw new IOException("Sorry, you miss your system technology setting. Please go back to set it ! ");
			
		}else if(this.days.length == 0){
			
			throw new IOException("Sorry, you miss your irrigation schedule days setting. Please go back to set it ! ");
		
		}else if(this.hours.length == 0 ){
			
			throw new IOException("Sorry, you miss your irrigation schedule hours setting. Please go back to set it ! ");
			
		}else if(this.choice.equals("")){
			
			throw new IOException("Sorry, you miss your choice setting. Please go back to set it ! ");
			
		}else if(this.irriDepth == 0.0){
			
			throw new IOException("Sorry, you miss your irrigation system setting. Please go back to set it ! ");
			
		}else if(this.rainsettings == 0.0){
			
			if(system.toString().contains("Time-based with rain sensor")){
				
				throw new IOException("Sorry, you miss your rainsettings. Please go back to set it ! ");
				
			}
			
		}else if(this.soilthreshold == 0.0){
			
			if(system.toString().contains("Time-based with soil moisture sensor")){
				
				throw new IOException("Sorry, you miss your soil threshold. Please go back to set it ! ");
				
			}
			
			
		}
		
	}
	
	public Data(String email, String unit, String zipcode, String soilType,
			double rootDepth, double area, String[] systemSelection,
			String[] days, String[] hours, double rainsettings,
			double soilthreshold, double irriDepth, String choice) {
		super();
		this.email = email;
		this.unit = unit;
		this.zipcode = zipcode;
		this.soilType = soilType;
		this.rootDepth = rootDepth;
		this.area = area;
		this.systemSelection = systemSelection;
		this.days = days;
		this.hours = hours;
		this.rainsettings = rainsettings;
		this.soilthreshold = soilthreshold;
		this.irriDepth = irriDepth;
		this.choice = choice;
	}

	public Data(Data data){
		
		this.email = data.email;
		this.unit = data.unit;
		this.zipcode = data.zipcode;
		this.soilType = data.soilType;
		this.rootDepth = data.rootDepth;
		this.area = data.area;
		this.systemSelection = data.systemSelection;
		this.days = data.days;
		this.hours = data.hours;
		//this.minutes = minutes;
		this.choice = data.choice;
		this.rainsettings = data.rainsettings;
		this.soilthreshold = data.soilthreshold;
		this.irriDepth = data.irriDepth;
	}

	public String getEmail() {
		return email;
	}



	public String getUnit() {
		return unit;
	}



	public String getZipcode() {
		return zipcode;
	}



	public String getSoilType() {
		return soilType;
	}



	public double getRootDepth() {
		return rootDepth;
	}



	public double getArea() {
		return area;
	}



	public String[] getSystemSelection() {
		return systemSelection;
	}



	public String[] getDays() {
		return days;
	}



	public String[] getHours() {
		return hours;
	}



	public String getChoice() {
		return choice;
	}



	public double getRainsettings() {
		return rainsettings;
	}



	public double getSoilthreshold() {
		return soilthreshold;
	}



	public double getIrriDepth() {
		return irriDepth;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public void setSoilType(String soilType) {
		this.soilType = soilType;
	}

	public void setRootDepth(double rootDepth) {
		this.rootDepth = rootDepth;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public void setSystemSelection(String[] systemSelection) {
		this.systemSelection = systemSelection;
	}

	public void setDays(String[] days) {
		this.days = days;
	}

	public void setHours(String[] hours) {
		this.hours = hours;
	}

	public void setRainsettings(double rainsettings) {
		this.rainsettings = rainsettings;
	}

	public void setSoilthreshold(double soilthreshold) {
		this.soilthreshold = soilthreshold;
	}

	public void setIrriDepth(double irriDepth) {
		this.irriDepth = irriDepth;
	}
	
	
	
	
	

}
