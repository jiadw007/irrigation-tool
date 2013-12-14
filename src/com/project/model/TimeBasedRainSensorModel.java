package com.project.model;

import java.util.ArrayList;

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
public class TimeBasedRainSensorModel extends Hydrology{
	
	
	private double rainsettings;
	private ArrayList<Double> rainSum = new ArrayList<Double>();    //rainSum for last 24hours
	private ArrayList<Double> IhrRain = new ArrayList<Double>();	  //WB = Rhr +IhrRain
	/**
	 * 
	 * @param data
	 * @param bd
	 * @throws Exception
	 */
	public TimeBasedRainSensorModel(Data data, BaseData bd) throws Exception{
		
		super(data,bd);
		this.rainsettings = data.getRainsettings();
		rainSum = new ArrayList<Double>();
		IhrRain = new ArrayList<Double>();
		this.getB().irriWeek = 0.0;
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

	/**
	 * calculation method in time based rain sensor model
	 * override method in time based model
	 */
	public void calculation(){
		
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
				this.IhrRain.add(this.b.Ihr.get(i-1));
				this.getB().irriWeek +=b.Ihr.get(i-1);
			}
			double wb=this.b.Rhr.get(i-1)+this.IhrRain.get(i-1);
			this.getWB().add(wb);
			super.calculation(i);
			
		}
		System.out.println("finish calculation!");
		this.calculateWaterLoss();
		
	}
	
}
