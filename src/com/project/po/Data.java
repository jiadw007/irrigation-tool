package com.project.po;

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
			) {
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
