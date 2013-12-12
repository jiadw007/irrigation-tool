package com.project.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.project.po.Data;

/**
 * Created with MyEclipse
 * User : Dawei Jia
 * Date : 10/19/2013
 * @author Dawei Jia
 *
 */
public class TimeBasedSoilSensorModel extends TimeBasedModel{
	
	
	private double soilThreshold;
	private ArrayList<Double> Ihrsoil = new ArrayList<Double>();		//WB = Rhr +Ihrsoil
	
	
	public double getSoilThreshold() {
		return soilThreshold;
	}

	public ArrayList<Double> getIhrsoil() {
		return Ihrsoil;
	}
	/**
	 * Constructor Method
	 * @param soilType
	 * @param area
	 * @param rootDepth
	 * @param zipcode
	 * @param unit
	 * @param soilthreshold
	 * @param days
	 * @param hours
	 * @param irriDepth
	 * @throws Exception
	 */
	public TimeBasedSoilSensorModel(Data data) throws Exception {
			
		super(data);
		this.soilThreshold = data.getSoilthreshold();
		// TODO Auto-generated constructor stub
	}
	/**
	 * calculation in time based soil sensor model
	 * override method in time based model
	 */
	public void calculation(){
		
		b.removeInitialValue();
		
		HashMap<String, Double> SOIL=b.soil.get(this.getSoilType());
		//this.setWB(new ArrayList<Double>());
		
		for(int i =this.getStartIrrigationHour();i<=this.getLastIrrigationHour();i++){
			
			//calculate the ET
			double kc=b.Kc.get(this.getLocation().getZone()).get(b.Month.get(i-1));
			double et=b.ET0.get(i-1);
			this.getET().add(et*kc);
			
			
			//calculation the Ihrsoil
			if(this.getSWC().get(i-1)>this.soilThreshold*SOIL.get("FC")*this.getRootDepth()){
			
				this.Ihrsoil.add(0.0);
				
			}else{
				
				this.Ihrsoil.add(this.b.Ihr.get(i-1));
				this.getB().IrriWeek +=this.b.Ihr.get(i-1);
			}
			double wb=this.b.Rhr.get(i-1)+this.Ihrsoil.get(i-1);
			this.getWB().add(wb);
			super.calculation(i);
			
		}
		
		System.out.println("finish !");
		int i =0;
		for(String hour: b.Hour){
			
			if(hour.equals("23")){
				
				
				//System.out.println(i);
				//System.out.println(this.wLostDay.get(i)+","+this.iLostDay.get(i));
				this.wLostWeek +=this.getwLostDay().get(i);
				this.iLostWeek +=this.getiLostDay().get(i);
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
		System.out.println("iLostWeek " +this.iLostWeek);
		
		//calculate the water stress day
		//calculate the water stress day
		double swcSum = 0.0;
		for(int j =1;j<this.getSWC().size();j++){
			if(j%24 != 0){
								
				swcSum +=this.getSWC().get(j);
								
								
			}else{
				swcSum +=this.getSWC().get(j);
				swcSum /=24.0;
				//System.out.println("swc : "+swcSum);
				if(swcSum < this.averW){
									
					this.wStressDays++;
				}
				swcSum = 0.0;
								
			}
							
							
		}
	}
	
	

}
