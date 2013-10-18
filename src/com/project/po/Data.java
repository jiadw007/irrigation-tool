package com.project.po;

public class Data {
	
	private String email;
	private String password;
	private String unit;
	private String zipcode;
	private String soilType;
	private double rootDepth;
	private double area;
	private String[] systemSelection;
	private String[] days;
	private String[] hours;
	//private String[] minutes;
	private String choice;
	private double rainsettings;
	private double soilthreshold;
	private double irriDepth;
	private String isystem;
	private int irriDuration;
	
	
	public Data(String email, String password, String unit, String zipcode,
			String soilType, double rootDepth, double area,
			String[] systemSelection, String[] days, String[] hours,
			String choice, double rainsettings,
			double soilthreshold, double irriDepth, String isystem,
			int irriDuration) {
		super();
		this.email = email;
		this.password = password;
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
		this.isystem = isystem;
		this.irriDuration = irriDuration;
		
	}
	
	
	
	
	

}
