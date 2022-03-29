package com.project.po;

import java.util.Comparator;
/**
 * Created with MyEclipse
 * User : Dawei Jia
 * Date :09/19/2013
 * @author Dawei Jia
 * Location to store geographical information for user location and nearest fawn station location
 */
public class Location implements Comparator<Location>{
	
	private String city;
	private String zip;
	private float lat;
	private float lng;
	private String zone;
	private String fawnStnID;
	private String fawnStnName;
	private float fawnStnLat;
	private float fawnStnLng;
	public float distance = -1f;
	private static float north2Central = 29.1f;
	private static float central2South = 27.4f;
	
	
	
	
	/**
	 * Constructor method
	 * @param zipcode
	 * @param lat
	 * @param lng
	 * @param city
	 */
	public Location(String zipcode, float lat,float lng,String city){
		
		this.zip = zipcode;
		this.lat = lat;
		this.lng = lng;
		this.city = city;
		this.setZone();
	}

	/**
	 * Compare distance between two objects
	 */

	public int compare(Location o1, Location o2) {
		// TODO Auto-generated method stub
		if(o1.distance == o2.distance){
			
			return 0;
			
		}else if(o1.distance > o2.distance){
			
			return 1;
			
		}else{
			
			return -1;
		}
	}



	/**
	 * overload compare function
	 * @param other
	 * @return boolean flag
	 */
	public int compare(Location other) {
		// TODO Auto-generated method stub
		if(this.distance == other.distance){
			
			return 0;
			
		}else if(this.distance >other.distance){
			
			return 1;
			
		}else{
			
			return -1;
		}
		
	}




	public String getCity() {
		return city;
	}




	public void setCity(String city) {
		this.city = city;
	}




	public String getZip() {
		return zip;
	}




	public void setZip(String zip) {
		this.zip = zip;
	}




	public float getLat() {
		return lat;
	}




	public void setLat(float lat) {
		this.lat = lat;
	}




	public float getLng() {
		return lng;
	}




	public void setLng(float lng) {
		this.lng = lng;
	}




	public String getZone() {
		return zone;
	}




	public void setZone(String zone) {
		this.zone = zone;
	}
	/**
	 * set the district of location
	 */
	public void setZone(){
		
		if(this.lat > north2Central){
			
			this.setZone("North Florida");
		}
		if(this.lat > central2South && this.lat < north2Central){
			
			this.setZone("Central Florida");
			
		}
		if(this.lat <north2Central){
			
			this.setZone("South Florida");
			
		}
		
	}


	public String getFawnStnID() {
		return fawnStnID;
	}




	public void setFawnStnID(String fawnStnID) {
		this.fawnStnID = fawnStnID;
	}




	public String getFawnStnName() {
		return fawnStnName;
	}




	public void setFawnStnName(String fawnStnName) {
		this.fawnStnName = fawnStnName;
	}




	public float getDistance() {
		return distance;
	}




	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	
	
	public float getFawnStnLat() {
		return fawnStnLat;
	}

	public void setFawnStnLat(float fawnStnLat) {
		this.fawnStnLat = fawnStnLat;
	}

	public float getFawnStnLng() {
		return fawnStnLng;
	}

	public void setFawnStnLng(float fawnStnLng) {
		this.fawnStnLng = fawnStnLng;
	}

	public void print(){
		
		System.out.println("zipcode: "+this.zip+",city: "+this.city+",lat: "+this.lat+",lng: "+this.lng+",zone: "+this.zone);
		System.out.println("fawn id: "+this.fawnStnID+",fawnStnName: "+this.fawnStnName+",fawnStnLat: "+this.fawnStnLat+",fawnStnLng: "+this.fawnStnLng+",distance: "+this.distance);
		
		
		
	}
	
	

}
