package com.project.model;

import java.util.ArrayList;
import java.util.HashMap;

public class timeBasedSoilSensorModel extends timeBasedModel{
	
	
	private double soilThreshold;
	private ArrayList<Double> Ihrsoil;		//WB = Rhr +Ihrsoil
	public timeBasedSoilSensorModel(String soilType, double area,
			double rootDepth, String zipcode, String unit, double soilthreshold) {
			
		super(soilType,area,rootDepth,zipcode,unit);
		this.soilThreshold = soilthreshold;
		Ihrsoil = new ArrayList<Double>();
		// TODO Auto-generated constructor stub
	}
	
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
			}
			double wb=this.b.Rhr.get(i-1)+this.Ihrsoil.get(i-1);
			this.getWB().add(wb);
			super.calculation(i);
			
		}
		
	}
	
	

}
