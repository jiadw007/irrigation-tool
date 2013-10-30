package com.project.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class timeBasedSoilSensorModel extends timeBasedModel{
	
	
	private double soilThreshold;
	private ArrayList<Double> Ihrsoil;		//WB = Rhr +Ihrsoil
	
	
	public double getSoilThreshold() {
		return soilThreshold;
	}

	public ArrayList<Double> getIhrsoil() {
		return Ihrsoil;
	}

	public timeBasedSoilSensorModel(String soilType, double area,
			double rootDepth, String zipcode, String unit, double soilthreshold) {
			
		super(soilType,area,rootDepth,zipcode,unit);
		this.soilThreshold = soilthreshold;
		Ihrsoil = new ArrayList<Double>();
		// TODO Auto-generated constructor stub
	}
	
	public JSONObject calculation(){
		
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
		this.wLostWeek = (double) (Math.round(this.wLostWeek*1000)/1000.0);
		
		this.iLostWeek = (double) (Math.round((this.iLostWeek/7)*1000)/10.0);
		System.out.println(this.wLostWeek);
		System.out.println(this.iLostWeek);
		JSONObject resultJSON = new JSONObject();
		try{
			resultJSON.append("Hour", b.Hour).append("Rhr", b.Rhr).append("Ihrsoil",this.Ihrsoil)
			.append("ET", this.getET()).append("WB", this.getWB()).append("SWC", this.getSWC()).append("DELTA",this.getDelta())
			.append("F", this.getF()).append("rateF", this.getRateF()).append("Q", this.getQ()).append("InF",this.getInF()).append("PERC",this.getPERC())
			.append("Loss",this.getLoss()).append("PerLoss",this.getPerLoss()).append("wLostHr",this.getwLostHr()).append("wLostDay",this.getwLostDay())
			.append("iLostHr",this.getiLostHr()).append("iLostDay",this.getiLostDay());			
			
		}catch(JSONException e){
			
			e.printStackTrace();
		}
		
		return resultJSON ; 
	}
	
	

}
