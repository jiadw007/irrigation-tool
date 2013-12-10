package com.project.model;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.project.po.Location;
import com.project.po.BaseData;
/***
 * Created with Eclipse
 * User: Dawei Jia
 * Date: 09/13/2013
 * @author Dawei Jia
 * This is the implementation of time based model for irrigation system in FAWN
 */
public class TimeBasedModel {
	
	
	protected static BaseData b;  
	private int startIrrigationHour = 1;
	private int lastIrrigationHour = 168;
	private String soilType;  // get from user input
	private Double area;	  //get from user input
	private Double rootDepth; //get from user input
	private Location location;	//get from user input
	private String unit;	    //get from user input
	private ArrayList<Double> WB = new ArrayList<Double>();   		
	private ArrayList<Double> SWC = new ArrayList<Double>();			//from calculation function 
	private ArrayList<Double> ET = new ArrayList<Double>();			//from calculation function
	private ArrayList<Double> delta = new ArrayList<Double>();		//from calculation function 
	private ArrayList<Double> F = new ArrayList<Double>();			//from calculation function 
	private ArrayList<Double> rateF = new ArrayList<Double>();		//from calculation function 
	private ArrayList<Double> PERC = new ArrayList<Double>();			//from calculation function 
	private ArrayList<Double> Q = new ArrayList<Double>();			//from calculation function 
	private ArrayList<Double> InF = new ArrayList<Double>();			//from calculation function 
	private ArrayList<Double> Loss = new ArrayList<Double>();			//from calculation function 
	private ArrayList<Double> PerLoss = new ArrayList<Double>();		//from calculation function 
	private ArrayList<Double> wLostHr = new ArrayList<Double>();		//from calculation function 	
	private ArrayList<Double> wLostDay = new ArrayList<Double>();		//from calculation function 
	private ArrayList<Double> iLostHr = new ArrayList<Double>();		//from calculation function 
	private ArrayList<Double> iLostDay = new ArrayList<Double>();		//from calculation function 
	protected Double wLostWeek = 0.0;		// water not used for one week, output in the result web page
	protected Double iLostWeek = 0.0;		// water not used percentage for one week, output in the result web page
	protected int wStressDays = 0;			// water stress day for one week, output in the result webpage
	protected double averW = 0.0;			// water stress day criteria
	
	public int getStartIrrigationHour() {
		return startIrrigationHour;
	}



	public void setStartIrrigationHour(int startIrrigationHour) {
		this.startIrrigationHour = startIrrigationHour;
	}



	public int getLastIrrigationHour() {
		return lastIrrigationHour;
	}



	public void setLastIrrigationHour(int lastIrrigationHour) {
		this.lastIrrigationHour = lastIrrigationHour;
	}



	public ArrayList<Double> getWB() {
		return WB;
	}



	public void setWB(ArrayList<Double> wB) {
		WB = wB;
	}



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

	
	public ArrayList<Double> getET() {
		return ET;
	}



	public void setET(ArrayList<Double> eT) {
		ET = eT;
	}

	

	public ArrayList<Double> getSWC() {
		return SWC;
	}



	public void setSWC(ArrayList<Double> sWC) {
		SWC = sWC;
	}



	public ArrayList<Double> getPERC() {
		return PERC;
	}



	public void setPERC(ArrayList<Double> pERC) {
		PERC = pERC;
	}



	public ArrayList<Double> getQ() {
		return Q;
	}



	public void setQ(ArrayList<Double> q) {
		Q = q;
	}


	
	public static BaseData getB() {
		return b;
	}



	public double getwLostWeek() {
		return wLostWeek;
	}



	public double getiLostWeek() {
		return iLostWeek;
	}



	



	public int getwStressDays() {
		return wStressDays;
	}



	public void setwStressDays(int wStressDays) {
		this.wStressDays = wStressDays;
	}



	public ArrayList<Double> getInF() {
		return InF;
	}



	public void setInF(ArrayList<Double> inF) {
		InF = inF;
	}

	

	public ArrayList<Double> getF() {
		return F;
	}



	public ArrayList<Double> getRateF() {
		return rateF;
	}



	public ArrayList<Double> getLoss() {
		return Loss;
	}



	public ArrayList<Double> getPerLoss() {
		return PerLoss;
	}



	public ArrayList<Double> getwLostHr() {
		return wLostHr;
	}



	public ArrayList<Double> getwLostDay() {
		return wLostDay;
	}



	public ArrayList<Double> getiLostHr() {
		return iLostHr;
	}



	public ArrayList<Double> getiLostDay() {
		return iLostDay;
	}

	

	public ArrayList<Double> getDelta() {
		return delta;
	}


	/**
	 * Constructor methods 
	 * @param soiltype 
	 * @param area
	 * @param rootDepth
	 * @param zipcode
	 * @param unit
	 * @param days
	 * @param hours
	 * @param irriDepth
	 * @throws Exception
	 * All parameters got from calculateServlet
	 */
	public TimeBasedModel(String soiltype, Double area, Double rootDepth,String zipcode, String unit,String days[],String hours[], double irriDepth) throws Exception{
		
		b = new BaseData(zipcode,days,hours,irriDepth);
		this.soilType = soiltype;
		this.unit = unit;
		/*
		 * unit conversion
		 */
		if(unit.equals("English")){
			
			this.area = (double) (Math.round(area*4046.85*100000)/100000);
			this.rootDepth = (double) rootDepth *2.54;
			
			
		}else{
			
			this.area = area;
			this.rootDepth = rootDepth;
			
		}
		//get the fawn station information
		this.location = b.getLocationByzipCode(zipcode);
		//for(int i =0;i<b.Rhr.size();i++){
			
			//double wb=b.Rhr.get(i)+b.Ihr.get(i);
			//this.WB.add(wb);
			
		//}
		HashMap<String, Double> SOIL=b.soil.get(this.soilType);		//get the properties for the designated soil
		//System.out.println(SOIL);
		Double swc0=0.75*SOIL.get("FC")*this.rootDepth;
		this.SWC.add(swc0);			//get the SWC0 value initialize SWC
		this.averW = SOIL.get("FC")*this.rootDepth - SOIL.get("WP")*this.rootDepth;
		
		
	}
	/**
	 * implementation time based function 
	 *  
	 */
	public void calculation(){
		
		b.removeInitialValue();
		HashMap<String, Double> SOIL=b.soil.get(soilType);
					
		for(int i=this.startIrrigationHour;i<=this.lastIrrigationHour;i++){
			
			this.getB().IrriWeek +=this.getB().Ihr.get(i-1);
			double wb = b.Rhr.get(i-1) + b.Ihr.get(i-1);
			this.WB.add(wb);
			if(this.WB.get(i-1)>0){  //calculate the rate(f),Q and PERC
				//System.out.println(i);
				double delta=SOIL.get("theta")-this.SWC.get(i-1)/this.rootDepth;  //get the value of delta for equation 2
				this.delta.add(i-1,delta);
				double psi=SOIL.get("psi");		//get the psi property of the soil
				double k=SOIL.get("K");			//get the K property of the soil
				NewtonMethod nm=new NewtonMethod(psi,delta,k);	
				//double F=0.0;
				if(nm.calculationMethod()){
					
					double F=nm.getResult();			//get the value of F for equation 1
					//System.out.println("F: "+F);
					this.F.add(i-1,F);
					
				}else{
					
					System.out.println("error calculation for F");
					
				}
				//calculation for the rate(f)
				
				double f=k*(1+(psi*delta/this.F.get(i-1)));
				this.rateF.add(i-1,f);
				
				//calculation for the Q
				if(this.WB.get(i-1)>f){
					double Q=this.WB.get(i-1)-f*1;
					this.Q.add(i-1,Q);
					this.InF.add(i-1,f*1);	
				}else{
					
					this.Q.add(i-1,0.0);
					this.InF.add(this.WB.get(i-1));
					
				}
				//calculation for the PERC
				double perc=this.SWC.get(i-1)+this.InF.get(i-1)-SOIL.get("FC")*this.rootDepth;
				if(perc>0){
					
					this.PERC.add(perc);
					
				}else{
					
					this.PERC.add(0.0);
				}
				
				
				
			}else{
				this.delta.add(-1.0);		//we have no result for this property
				this.F.add(-1.0);		//we have no result for this property
				this.rateF.add(-1.0);		//we have no result for this property
				this.Q.add(0.0);
				this.PERC.add(0.0);
				this.InF.add(0.0);
				
				
			}
			//calculate the ET
			double kc=b.Kc.get(this.location.getZone()).get(b.Month.get(i-1));
			double et=b.ET0.get(i-1);
			this.ET.add(et*kc);
			
			//calculate SWC
			
			if(this.PERC.get(i-1)>0){
				
				this.SWC.add(i,SOIL.get("FC")*this.rootDepth);
				
			}else if(this.SWC.get(i-1)-this.ET.get(i-1)+this.InF.get(i-1)<SOIL.get("WP")*this.rootDepth*0.1){
				
				this.SWC.add(i,SOIL.get("WP")*this.rootDepth*0.1);
				
				
			}else if(this.SWC.get(i-1)-this.ET.get(i-1)+this.InF.get(i-1)>SOIL.get("FC")*this.rootDepth){
				
				this.SWC.add(i,SOIL.get("FC")*this.rootDepth);
				
			}else{
				
				this.SWC.add(i,this.SWC.get(i-1)+this.InF.get(i-1)-this.ET.get(i-1));
				
			}
			
			
			//calculate the water loss
			double wloss=(this.Q.get(i-1)+this.PERC.get(i-1)-b.Rhr.get(i-1))*this.area*Math.pow(10.0, 4.0);
			double iloss=(this.Q.get(i-1)+this.PERC.get(i-1)-this.b.Rhr.get(i-1))/this.b.Ihr.get(i-1);
			
			//caculate the wLostHr and water loss
			if(wloss>0){
				this.wLostHr.add(wloss);
				this.Loss.add(wloss);
			}else{
				this.wLostHr.add(0.0);
				this.Loss.add(0.0);
			}
			if(iloss>0){
				
				this.PerLoss.add(iloss);
				
			}else{
				
				this.PerLoss.add(0.0);
			}
			
			
			
			//calculate the iLostHr
			if(this.b.Ihr.get(i-1)>0){
				
				if(iloss>0){
					
					this.iLostHr.add(iloss);
				}else{
					
					this.iLostHr.add(0.0);
				}
			}else{
				this.iLostHr.add(0.0);
			}
			
			//calculate the wLostDay and iLostDay
			if(i>24){
						
				if(Integer.parseInt(this.b.Hour.get(i-1))==0){
					this.wLostDay.add(0.0);
					this.iLostDay.add(0.0);
							
				}else if(Integer.parseInt(this.b.Hour.get(i-1))%23==0){
					//System.out.println(i);		
					double wsum=0;
					double isum=0;
					for(int j =i-23;j<=i;j++){
								
						wsum+=this.wLostHr.get(j-1);
						isum+=this.iLostHr.get(j-1);
					}
					this.wLostDay.add(wsum);
					this.iLostDay.add(isum);
				}else{
					this.wLostDay.add(0.0);
					this.iLostDay.add(0.0);
							
				}
						
			}else{
				this.wLostDay.add(0.0);
				this.iLostDay.add(0.0);
			}
			
		}
		
		System.out.println("finish!");
		//calculate the water loss per week
		int i =0;
		for(String hour: b.Hour){
			
			if(hour.equals("23")){
				
				
				//System.out.println(i);
				//System.out.println(this.wLostDay.get(i)+","+this.iLostDay.get(i));
				this.wLostWeek +=this.wLostDay.get(i);
				this.iLostWeek +=this.iLostDay.get(i);
				i++;
				
			}else{
				i++;
			}
			
		}
		//System.out.println(this.wLostWeek);
		//System.out.println(this.iLostWeek);
		
		
		this.wLostWeek = (double) (Math.round(this.wLostWeek*1000/378.54)/1000.0);
		
		this.iLostWeek = (double) (Math.round((this.iLostWeek/7)*1000)/10.0);
		System.out.println("wLostWeek: "+this.wLostWeek);
		System.out.println("iLostWeek: " +this.iLostWeek);
		System.out.println("averW :"+this.averW);
		//calculate the water stress day
		double swcSum = 0.0;
		for(int j =1;j<this.SWC.size();j++){
			if(j%24 != 0){
				
				swcSum +=this.SWC.get(j);
				
				
			}else{
				swcSum +=this.SWC.get(j);
				swcSum /=24.0;
				//System.out.println("swc : "+swcSum);
				if(swcSum < this.averW){
					
					this.wStressDays++;
				}
				swcSum = 0.0;
				
			}
			
			
		}
		
		
		/*
		JSONObject resultJSON = new JSONObject();
		try{
			resultJSON.append("Hour", b.Hour).append("ET", this.ET).append("WB", this.WB).append("SWC", this.SWC)
			.append("F", this.F).append("rateF", this.rateF).append("Q", this.Q).append("InF",this.InF).append("PERC",this.PERC)
			.append("Loss",this.Loss).append("PerLoss",this.PerLoss).append("wLostHr",this.wLostHr).append("wLostDay",this.wLostDay)
			.append("iLostHr",this.iLostHr).append("iLostDay",this.iLostDay);			
			
		}catch(JSONException e){
			
			e.printStackTrace();
		}
		*/
		//return resultJSON ;
		
		
	}

	/**
	 * implementation for timebased soil sensor and rain sensor
	 * @param i index of results
	 */

	public void calculation(int i ){
		
		HashMap<String, Double> SOIL=b.soil.get(soilType);
		
		if(this.WB.get(i-1)>0){  //calculate the rate(f),Q and PERC
			//System.out.println(i);
			double delta=SOIL.get("theta")-this.SWC.get(i-1)/this.rootDepth;  //get the value of delta for equation 2
			this.delta.add(i-1,delta);
			double psi=SOIL.get("psi");		//get the psi property of the soil
			double k=SOIL.get("K");			//get the K property of the soil
			NewtonMethod nm=new NewtonMethod(psi,delta,k);	
			//double F=0.0;
			if(nm.calculationMethod()){
				
				double F=nm.getResult();			//get the value of F for equation 1
				//System.out.println("F: "+F);
				this.F.add(i-1,F);
				
			}else{
				
				System.out.println("error calculation for F");
				
			}
			//calculation for the rate(f)
			
			double f=k*(1+(psi*delta/this.F.get(i-1)));
			this.rateF.add(i-1,f);
			
			//calculation for the Q
			if(this.WB.get(i-1)>f){
				double Q=this.WB.get(i-1)-f*1;
				this.Q.add(i-1,Q);
				this.InF.add(i-1,f*1);	
			}else{
				
				this.Q.add(i-1,0.0);
				this.InF.add(this.WB.get(i-1));
				
			}
			//calculation for the PERC
			double perc=this.SWC.get(i-1)+this.InF.get(i-1)-SOIL.get("FC")*this.rootDepth;
			if(perc>0){
				
				this.PERC.add(perc);
				
			}else{
				
				this.PERC.add(0.0);
			}
			
			
			
		}else{
			this.delta.add(i-1,-1.0);		//we have no result for this property
			this.F.add(i-1,-1.0);		//we have no result for this property
			this.rateF.add(i-1,-1.0);		//we have no result for this property
			this.Q.add(i-1,0.0);
			this.PERC.add(i-1,0.0);
			this.InF.add(i-1,0.0);
			
			
		}
		//calculate the ET
		//double kc=b.Kc.get(this.district).get(b.Month.get(i-1));
		//double et=b.ET0.get(i-1);
		//this.ET.add(et*kc);
		
		//calculate SWC
		
		if(this.PERC.get(i-1)>0){
			
			this.SWC.add(i,SOIL.get("FC")*this.rootDepth);
		}else if(this.SWC.get(i-1)-this.ET.get(i-1)+this.InF.get(i-1)<SOIL.get("WP")*this.rootDepth*0.1){
			
			this.SWC.add(i,SOIL.get("WP")*this.rootDepth*0.1);
			
			
		}else if(this.SWC.get(i-1)-this.ET.get(i-1)+this.InF.get(i-1)>SOIL.get("FC")*this.rootDepth){
			
			this.SWC.add(i,SOIL.get("FC")*this.rootDepth);
			
		}else{
			
			this.SWC.add(i,this.SWC.get(i-1)+this.InF.get(i-1)-this.ET.get(i-1));
			
		}
		
		
		//calculate the water loss
		double wloss=(this.Q.get(i-1)+this.PERC.get(i-1)-b.Rhr.get(i-1))*this.area*Math.pow(10.0, 4.0);
		double iloss=(this.Q.get(i-1)+this.PERC.get(i-1)-this.b.Rhr.get(i-1))/this.b.Ihr.get(i-1);
		//caculate the wLostHr and water loss
		if(wloss>0){
			this.wLostHr.add(wloss);
			this.Loss.add(wloss);
		}else{
			this.wLostHr.add(0.0);
			this.Loss.add(0.0);
		}
		if(iloss>0){
			
			this.PerLoss.add(iloss);
			
		}else{
			
			this.PerLoss.add(0.0);
		}
		
		
		
		//calculate the iLostHr
		if(this.b.Ihr.get(i-1)>0){
			
			if(iloss>0){
				
				this.iLostHr.add(iloss);
			}else{
				
				this.iLostHr.add(0.0);
			}
		}else{
			this.iLostHr.add(0.0);
		}
		
		//calculate the wLostDay and iLostDay
		if(i>24){
					
			if(Integer.parseInt(this.b.Hour.get(i-1))==0){
				this.wLostDay.add(0.0);
				this.iLostDay.add(0.0);
						
			}else if(Integer.parseInt(this.b.Hour.get(i-1))%23==0){
						
				double wsum=0;
				double isum=0;
				for(int j =i-23;j<=i;j++){
							
					wsum+=this.wLostHr.get(j-1);
					isum+=this.iLostHr.get(j-1);
				}
				this.wLostDay.add(wsum);
				this.iLostDay.add(isum);
			}else{
				this.wLostDay.add(0.0);
				this.iLostDay.add(0.0);
						
			}
					
		}else{
			this.wLostDay.add(0.0);
			this.iLostDay.add(0.0);
		}
		
	}
	/**
	 * implementation for ET part 
	 * don't calculate the Loss, iLoss wLosshr iLosshr wLossday iLossDay here
	 * @param i
	 */
	public void calculationET(int i ){
		
		HashMap<String, Double> SOIL=b.soil.get(soilType);
		
		if(this.WB.get(i-1)>0){  //calculate the rate(f),Q and PERC
			//System.out.println(i);
			double delta=SOIL.get("theta")-this.SWC.get(i-1)/this.rootDepth;  //get the value of delta for equation 2
			this.delta.add(i-1,delta);
			double psi=SOIL.get("psi");		//get the psi property of the soil
			double k=SOIL.get("K");			//get the K property of the soil
			NewtonMethod nm=new NewtonMethod(psi,delta,k);	
			//double F=0.0;
			if(nm.calculationMethod()){
				
				double F=nm.getResult();			//get the value of F for equation 1
				//System.out.println("F: "+F);
				this.F.add(i-1,F);
				
			}else{
				
				System.out.println("error calculation for F");
				
			}
			//calculation for the rate(f)
			
			double f=k*(1+(psi*delta/this.F.get(i-1)));
			this.rateF.add(i-1,f);
			
			//calculation for the Q
			if(this.WB.get(i-1)>f){
				double Q=this.WB.get(i-1)-f*1;
				this.Q.add(i-1,Q);
				this.InF.add(i-1,f*1);	
			}else{
				
				this.Q.add(i-1,0.0);
				this.InF.add(this.WB.get(i-1));
				
			}
			//calculation for the PERC
			double perc=this.SWC.get(i-1)+this.InF.get(i-1)-SOIL.get("FC")*this.rootDepth;
			if(perc>0){
				
				this.PERC.add(perc);
				
			}else{
				
				this.PERC.add(0.0);
			}
			
			
			
		}else{
			this.delta.add(i-1,-1.0);		//we have no result for this property
			this.F.add(i-1,-1.0);		//we have no result for this property
			this.rateF.add(i-1,-1.0);		//we have no result for this property
			this.Q.add(i-1,0.0);
			this.PERC.add(i-1,0.0);
			this.InF.add(i-1,0.0);
			
			
		}
		//calculate the ET
		//double kc=b.Kc.get(this.district).get(b.Month.get(i-1));
		//double et=b.ET0.get(i-1);
		//this.ET.add(et*kc);
		
		//calculate SWC
		
		if(this.PERC.get(i-1)>0){
			
			this.SWC.add(i,SOIL.get("FC")*this.rootDepth);
		}else if(this.SWC.get(i-1)-this.ET.get(i-1)+this.InF.get(i-1)<SOIL.get("WP")*this.rootDepth*0.1){
			
			this.SWC.add(i,SOIL.get("WP")*this.rootDepth*0.1);
			
			
		}else if(this.SWC.get(i-1)-this.ET.get(i-1)+this.InF.get(i-1)>SOIL.get("FC")*this.rootDepth){
			
			this.SWC.add(i,SOIL.get("FC")*this.rootDepth);
			
		}else{
			
			this.SWC.add(i,this.SWC.get(i-1)+this.InF.get(i-1)-this.ET.get(i-1));
			
		}
		
		
	}
}