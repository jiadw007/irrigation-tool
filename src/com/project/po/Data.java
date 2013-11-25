package com.project.po;

import java.io.IOException;

/**
 * Create with MyEcplise
 * User : Dawei Jia
 * Date : 9/19/2013
 * @author Dawei Jia
 * po for storing user data in the GAE database
 */
public class Data {
	
	private String email;
	
	private String unit;
	private String zipcode;
	private String soilType;
	private double rootDepth;
	private double area;
	private String[] systemSelection;
	private String[] days;
	private String[] hours;
	//private String[] minutes;
	
	private double rainsettings;
	private double soilthreshold;
	private double irriDepth;
	private String choice;
	
	public Data(){
		
		
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
	
	public Data(String email, String unit, String zipcode,
			String soilType, double rootDepth, double area,
			String[] systemSelection, String[] days, String[] hours,
			String choice, double rainsettings,
			double soilthreshold, double irriDepth
			) throws IOException{
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
		//this.minutes = minutes;
		this.choice = choice;
		this.rainsettings = rainsettings;
		this.soilthreshold = soilthreshold;
		this.irriDepth = irriDepth;
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
	
	
	
	
	

}
