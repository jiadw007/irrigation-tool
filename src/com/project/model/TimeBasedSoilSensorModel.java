package com.project.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.project.po.BaseData;
import com.project.po.Data;

/**
 * Created with MyEclipse
 * User : Dawei Jia
 * Date : 10/19/2013
 * @author Dawei Jia
 *
 */
public class TimeBasedSoilSensorModel extends Hydrology{
	
	
	private double soilThreshold;
	private ArrayList<Double> Ihrsoil = new ArrayList<Double>();		//WB = Rhr +Ihrsoil
	
	
	public double getSoilThreshold() {
		return soilThreshold;
	}

	public ArrayList<Double> getIhrsoil() {
		return Ihrsoil;
	}
	

	/**
	 * 
	 * @param data
	 * @param b
	 * @throws Exception
	 */
	public TimeBasedSoilSensorModel(Data data, BaseData b) throws Exception {
			
		super(data, b);
		this.soilThreshold = data.getSoilthreshold();
		this.getB().irriWeek = 0.0;
		// TODO Auto-generated constructor stub
	}
	/**
	 * calculation in time based soil sensor model
	 * override method in time based model
	 */
	public void calculation(){
		
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
				this.getB().irriWeek +=this.b.Ihr.get(i-1);
			}
			double wb=this.b.Rhr.get(i-1)+this.Ihrsoil.get(i-1);
			this.getWB().add(wb);
			super.calculation(i);
			
		}
		
		System.out.println("finish calculation!");
		this.calculateWaterLoss();
	}
	
	

}
