package com.project.model;

import java.util.ArrayList;

import com.project.po.Location;
import com.project.po.baseData;

public class timeBasedModel {
	
	
	protected static baseData b = new baseData();
	private String soilType;  // get from user input
	private Double area;	  //get from user input
	private Double rootDepth; //get from user input
	private Location location;	//get from user input
	private String unit;	    //get from user input
	private ArrayList<Double> WB;
	private ArrayList<Double> SWC;			//from calculation function 
	private ArrayList<Double> ET;			//from calculation function
	private ArrayList<Double> delta;		//from calculation function 
	private ArrayList<Double> F;			//from calculation function 
	private ArrayList<Double> rateF;			//from calculation function 
	private ArrayList<Double> PERC;			//from calculation function 
	private ArrayList<Double> Q;			//from calculation function 
	private ArrayList<Double> InF;			//from calculation function 
	private ArrayList<Double> Loss;			//from calculation function 
	private ArrayList<Double> PerLoss;		//from calculation function 
	private ArrayList<Double> wLostHr;
	private ArrayList<Double> wLostDay;
	private ArrayList<Double> iLostHr;
	private ArrayList<Double> iLostDay;
	
	
	
	
	public String getSoilType() {
		return soilType;
	}



	public void setSoilType(String soilType) {
		this.soilType = soilType;
	}



	public Double getArea() {
		return area;
	}



	public void setArea(Double area) {
		this.area = area;
	}



	public Double getRootDepth() {
		return rootDepth;
	}



	public void setRootDepth(Double rootDepth) {
		this.rootDepth = rootDepth;
	}



	public String getUnit() {
		return unit;
	}



	public void setUnit(String unit) {
		this.unit = unit;
	}



	public Location getLocation() {
		return location;
	}



	public void setLocation(Location location) {
		this.location = location;
	}


	public timeBasedModel(String soiltype, Double area, Double rootDepth,String zipcode, String unit){
		
		this.soilType = soiltype;
		this.unit = unit;
		if(unit.equals("English")){
			
			this.area = (double) (Math.round(area*2.54*1000)/1000);
			this.rootDepth = (double) (Math.round(rootDepth*2.54*1000)/1000);
			
			
		}else{
			
			this.area = area;
			this.rootDepth = rootDepth;
			
		}
		this.location = b.getLocationByzipCode(zipcode);
		
		
		
		
		
	}
	
	
	

}
