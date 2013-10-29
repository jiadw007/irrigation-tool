package com.project.model;

import java.util.ArrayList;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class timeBasedRainSensorModel extends timeBasedModel{
	
	
	private double rainsettings;
	private ArrayList<Double> rainSum;    //rainSum for last 24hours
	private ArrayList<Double> IhrRain;	  //WB = Rhr +IhrRain
	
	public timeBasedRainSensorModel(String soilType, double area, double rootDepth, String zipcode, String unit, double rainsettings){
		
		super(soilType,area,rootDepth,zipcode,unit);
		this.rainsettings = rainsettings;
		rainSum = new ArrayList<Double>();
		IhrRain = new ArrayList<Double>();
		
		
		
	}
	
	public double getRainsettings() {
		return rainsettings;
	}

	public ArrayList<Double> getRainSum() {
		return rainSum;
	}

	public ArrayList<Double> getIhrRain() {
		return IhrRain;
	}

	public JSONObject calculation(){
		
		//this.setWB(new ArrayList<Double>());
		
		for(int i =this.getStartIrrigationHour();i<=this.getLastIrrigationHour();i++){
			
			//calculate the ET
			double kc=b.Kc.get(this.getLocation().getZone()).get(b.Month.get(i-1));
			double et=b.ET0.get(i-1);
			this.getET().add(et*kc);
			
			
			if(i<24){
				
				this.rainSum.add(super.b.Rhr.get(i-1));
				
			}else{
				
				double sum=0;
				for(int j=i-24;j<i;j++){
					
					sum+=super.b.Rhr.get(j);
										
				}
				this.rainSum.add(sum);
				
			}
			if(this.rainSum.get(i-1)>this.rainsettings){
				
				
				this.IhrRain.add(0.0);
				
			}
			else{
				this.IhrRain.add(super.b.Ihr.get(i-1));
				
			}
			double wb=this.b.Rhr.get(i-1)+this.IhrRain.get(i-1);
			this.getWB().add(wb);
			super.calculation(i);
			
		}
		System.out.println("finish !");
		JSONObject resultJSON = new JSONObject();
		try{
			resultJSON.append("Hour", b.Hour).append("Rhr", b.Rhr).append("rainSum", this.rainSum).append("IhrRain", this.IhrRain)
			.append("ET", this.getET()).append("WB", this.getWB()).append("SWC", this.getSWC()).append("DELTA",this.getDelta())
			.append("F", this.getF()).append("rateF", this.getRateF()).append("Q", this.getQ()).append("InF",this.getInF()).append("PERC",this.getPERC())
			.append("Loss",this.getLoss()).append("PerLoss",this.getPerLoss()).append("wLostHr",this.getwLostHr()).append("wLostDay",this.getwLostDay())
			.append("iLostHr",this.getiLostHr()).append("iLostDay",this.getiLostDay());			
			
		}catch(JSONException e){
			
			e.printStackTrace();
		}
		
		return resultJSON;
		//super.calculation();
		
	}
	
}
