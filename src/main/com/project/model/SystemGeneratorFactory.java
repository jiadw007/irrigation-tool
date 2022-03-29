package com.project.model;

import com.project.po.BaseData;
import com.project.po.Data;



public class SystemGeneratorFactory {

	public static final String TIME_BASED = "Time-based";
	public static final String RAIN_SENSOR = "Time-based with rain sensor";
	public static final String SOIL_SENSOR = "Time-based with soil moisture sensor";
	public static final String ET_CONTROLLER = "Evapotranspiration Controller";
	
	public static Hydrology createModel(String system, Data data, BaseData bd) throws Exception{
		
		if(system.equals(TIME_BASED)){
			
			System.out.println("Time-based");
			return new TimeBasedModel(data,bd);
			
		}else if(system.equals(RAIN_SENSOR)){
			
			System.out.println("Time-based with rain sensor");
			return new TimeBasedRainSensorModel(data,bd);
			
		}else if(system.equals(SOIL_SENSOR)){
			
			System.out.println("Time-based with soil moisture sensor");
			return new TimeBasedSoilSensorModel(data,bd);
			
		}else{
			System.out.println("Evapotranspiration Controller");
			return new ETControllerModel(data,bd);
			
		}
	}
}
