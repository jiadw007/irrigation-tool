package com.project.model;

import java.util.ArrayList;

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
public void calculation(){
		
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
		
		//super.calculation();
		
	}
	
}
