package com.project.model;




import java.util.HashMap;

import com.project.po.Data;
import com.project.po.BaseData;
/***
 * Created with Eclipse
 * User: Dawei Jia
 * Date: 09/13/2013
 * @author Dawei Jia
 * This is the implementation of time based model for irrigation system in FAWN
 */
public class TimeBasedModel extends Hydrology{
	
	/**
	 * Constructor Method
	 * @param data
	 * @param bd
	 * @throws Exception
	 */
	public TimeBasedModel(Data data, BaseData bd) throws Exception{
		
		super(data,bd);
		
	}
	/**
	 * implementation time based function 
	 *  
	 */
	public void calculation(){
		
		HashMap<String, Double> SOIL=b.soil.get(soilType);
		System.out.println("Ihr size: " + this.getB().Ihr.size());	
		for(int i=this.startIrrigationHour;i<=this.lastIrrigationHour;i++){
			
			this.getB().irriWeek  +=this.getB().Ihr.get(i-1);
			double wb = b.Rhr.get(i-1) + b.Ihr.get(i-1);
			this.WB.add(wb);
			//calculate the ET
			double kc=b.Kc.get(this.location.getZone()).get(b.Month.get(i-1));
			double et=b.ET0.get(i-1);
			this.ET.add(et*kc);
			this.calculation(i);
		}
		System.out.println("finish calculation!");
		this.calculateWaterLoss();
			
	}
	
}