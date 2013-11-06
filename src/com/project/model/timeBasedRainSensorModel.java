package com.project.model;

import java.util.ArrayList;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class timeBasedRainSensorModel extends timeBasedModel{
	
	
	private double rainsettings;
	private ArrayList<Double> rainSum;    //rainSum for last 24hours
	private ArrayList<Double> IhrRain;	  //WB = Rhr +IhrRain
	
	public timeBasedRainSensorModel(String soilType, double area, double rootDepth, String zipcode, String unit, double rainsettings,String[] days, String[] hours) throws Exception{
		
		super(soilType,area,rootDepth,zipcode,unit,days, hours);
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
		b.Date.remove(0);
		b.Year.remove(0);
		b.Month.remove(0);
		b.Hour.remove(0);
		b.Rhr.remove(0);
		b.Ihr.remove(0);
		b.ET0.remove(0);
		b.Ihrschedule.remove(0);
		
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
		this.wLostWeek = (double) (Math.round(this.wLostWeek*1000/3785.4)/1000.0);
		
		this.iLostWeek = (double) (Math.round((this.iLostWeek/7)*1000)/10.0);
		System.out.println("wLostWeek: "+this.wLostWeek);
		System.out.println("iLostWeek " +this.iLostWeek);
		
		//calculate the water stress day
		double swcSum = 0.0;
		for(int j =1;j<this.getSWC().size();j++){
			if(j%24 != 0){
						
				swcSum +=this.getSWC().get(j);
						
						
			}else{
				swcSum +=this.getSWC().get(j);
				swcSum /=24.0;
				System.out.println("swc : "+swcSum);
				if(swcSum < this.averW){
							
					this.wStressDays++;
				}
				swcSum = 0.0;
						
			}
					
					
		}
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
