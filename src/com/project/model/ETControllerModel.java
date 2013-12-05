package com.project.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
/**
 * Created with MyEcplise
 * User : Dawei Jia
 * Date: 10/19/2013
 * @author Dawei Jia
 *
 */
public class ETControllerModel extends timeBasedModel{
	
	
	private ArrayList<Double> Ihret;
	private ArrayList<Double> Re;
	private ArrayList<Integer> Ick1;
	private ArrayList<Integer> Ick2;
	private ArrayList<Double> AWR;
	private ArrayList<Double> AWRstep1;
	private ArrayList<Integer> AWRstep2;
	//private ArrayList<Double> AWRstep3;
	/**
	 * Constructor Method
	 * @param soilType
	 * @param area
	 * @param rootDepth
	 * @param zipcode
	 * @param unit
	 * @param days
	 * @param hours
	 * @param irriDepth
	 * @throws Exception
	 * set initial value in ET0 ET AWR AWRstep1 AWRstep2 Ick1 Ick2 Ihret
	 * 
	 */
	public ETControllerModel(String soilType, double area, double rootDepth,
			String zipcode, String unit, String[] days, String[] hours, Double irriDepth) throws Exception {
		// TODO Auto-generated constructor stub
		super(soilType, area, rootDepth, zipcode, unit, days, hours, irriDepth);
		
			
		
		Ihret=new ArrayList();
		Re=new ArrayList();
		Ick1=new ArrayList();
		Ick2=new ArrayList();
		AWR=new ArrayList();
		AWRstep1=new ArrayList();
		AWRstep2=new ArrayList();
		double kc = b.Kc.get(this.getLocation().getZone()).get(b.Month.get(0));
		double et0 = b.ET0.get(0);
		this.getET().add(kc*et0);
		this.AWR.add(0.0);  //initial value for AWR
		this.AWRstep1.add(0.0);
		this.AWRstep2.add(0);
		this.Ick1.add(0);
		this.Ick2.add(0);
		
		HashMap<String,Double> SOIL = b.soil.get(this.getSoilType());
		if(b.Rhr.get(0)>(this.getRootDepth()*SOIL.get("FC")-this.getSWC().get(0))){
			
			double re = this.getRootDepth()*SOIL.get("FC")-this.getSWC().get(0);
			this.Re.add(re);
			
			
		}else{
			
			this.Re.add(b.Rhr.get(0));
			
		}
		this.Ihret.add(0.0);
		
	}
	public ArrayList<Double> getIhret() {
		return Ihret;
	}
	public ArrayList<Double> getRe() {
		return Re;
	}
	public ArrayList<Integer> getIck1() {
		return Ick1;
	}
	public ArrayList<Integer> getIck2() {
		return Ick2;
	}
	public ArrayList<Double> getAWR() {
		return AWR;
	}
	public ArrayList<Double> getAWRstep1() {
		return AWRstep1;
	}
	public ArrayList<Integer> getAWRstep2() {
		return AWRstep2;
	}
	/**
	 * intital value: SWC AWR Rhr Re and ET	AWRstep1 AWRstep2 when i=i , output i
	 * for the value in based model ,when i =i ,output i-1 
	 * */
	public void calculation(){
		
		HashMap<String,Double> SOIL = b.soil.get(this.getSoilType());
		b.IrriWeek = 0.0;
		
		for(int i =this.getStartIrrigationHour();i<=this.getLastIrrigationHour();i++){
			
			
			//calculate ETi 
			double kc = b.Kc.get(this.getLocation().getZone()).get(b.Month.get(i));
			double et0 = b.ET0.get(i);
			this.getET().add(kc*et0);
			
			//calculate Re
			
			if(b.Rhr.get(i)>(this.getRootDepth()*SOIL.get("FC")-this.getSWC().get(i-1))){
				
				double re = this.getRootDepth()*SOIL.get("FC") - this.getSWC().get(i-1);
				this.Re.add(re);
				
				
			}else{
				this.Re.add(b.Rhr.get(i));
				
			}
			
			//Check if irrigation is scheduled
			if (b.Ihrschedule.get(i)==1){
				
				this.Ick1.add(1);
				
				
			}else{
				
				this.Ick1.add(0);
			}
			//Check if irrigation occurs
			if(this.AWR.get(i-1)>(SOIL.get("FC")-SOIL.get("WP"))*this.getRootDepth()*SOIL.get("MAD")){
				
				this.Ick2.add(1);
				
			}else{
				
				this.Ick2.add(0);
			}
			//Check if itrrigation occurs
			if(Ick1.get(i)+Ick2.get(i)==2){
				
				this.Ihret.add(this.AWR.get(i-1));
				b.IrriWeek += this.AWR.get(i-1);
			}else{
				
				this.Ihret.add(0.0);
				
			}
			//Calculate AWR for this hour, like the process of SWC, use above value to calcualte
			//AWRstep1
			if(this.Ick1.get(i)+this.Ick2.get(i)==2){
				 
				double awrstep1=this.getET().get(i-1)-this.Re.get(i-1);    //et and re initial value
				this.AWRstep1.add(awrstep1);
				
			}else{
				
				double awrstep1 = this.getET().get(i-1)-this.Re.get(i-1)+this.AWR.get(i-1);
				this.AWRstep1.add(awrstep1);
			}
			
			//AWRstep2
			if(b.Rhr.get(i-1) > 0 &&this.Re.get(i-1)==0){
				
				this.AWRstep2.add(0);
				
			}else{
				
				this.AWRstep2.add(1);
			}
			//AWR
			if(this.AWRstep1.get(i) < 0 || this.AWRstep2.get(i) == 0){
				
				this.AWR.add(0.0);
				
				
			}else{
				
				this.AWR.add(this.AWRstep1.get(i));
				
			}
			
			double wb = b.Rhr.get(i)+this.Ihret.get(i);			
			this.getWB().add(wb);
			super.calculationET(i);
			
		}
		System.out.println("finish !");
		//remove initial value
		b.removeInitialValue();
		this.AWR.remove(0);
		this.AWRstep1.remove(0);
		this.AWRstep2.remove(0);
		this.Ihret.remove(0);
		this.Ick1.remove(0);
		this.Ick2.remove(0);
		//this.getiLostHr().remove(0);
		//this.getiLostDay().remove(0);
		for(int i =this.getStartIrrigationHour();i<=this.getLastIrrigationHour();i++){
			
			//calculate the water loss
			double wloss=(this.getQ().get(i-1)+this.getPERC().get(i-1)-b.Rhr.get(i-1))*this.getArea()*Math.pow(10.0, 4.0);
			//this.getLoss().add(i-1,Math.abs(wloss));
			double iloss=(this.getQ().get(i-1)+this.getPERC().get(i-1)-this.b.Rhr.get(i-1))/this.b.Ihr.get(i-1);
			//this.getPerLoss().add(i-1,Math.abs(iloss));
			
			//caculate the wLostHr
			if(wloss>0){
				this.getwLostHr().add(wloss);
				this.getLoss().add(i-1,wloss);
			}else{
				this.getwLostHr().add(0.0);
				this.getLoss().add(0.0);
			}
			if(iloss > 0 ){
				
				this.getPerLoss().add(i-1,iloss);
				
			}else{
				
				this.getPerLoss().add(i-1, 0.0);
				
			}
			
			
			//calculate the iLostHr
			if(this.b.Ihr.get(i-1)>0){
				
				if(iloss>0){
					
					this.getiLostHr().add(iloss);
				}else{
					
					this.getiLostHr().add(0.0);
				}
			}else{
				this.getiLostHr().add(0.0);
			}
			
			//calculate the wLostDay and iLostDay
			if(i>24){
						
				if(Integer.parseInt(this.b.Hour.get(i-1))==0){
					this.getwLostDay().add(0.0);
					this.getiLostDay().add(0.0);
							
				}else if(Integer.parseInt(this.b.Hour.get(i-1))%23==0){
							
					double wsum=0;
					double isum=0;
					for(int j =i-23;j<=i;j++){
								
						wsum+=this.getwLostHr().get(j-1);
						isum+=this.getiLostHr().get(j-1);
					}
					this.getwLostDay().add(wsum);
					this.getiLostDay().add(isum);
				}else{
					this.getwLostDay().add(0.0);
					this.getiLostDay().add(0.0);
							
				}
						
			}else{
				this.getwLostDay().add(0.0);
				this.getiLostDay().add(0.0);
			}
			
		}
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
		System.out.println("iLostWeek: " +this.iLostWeek);
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
		
		/*
		JSONObject resultJSON = new JSONObject();
		try{
			resultJSON.append("Hour", b.Hour).append("Rhr", b.Rhr).append("Re", this.Re).append("Ihrschedule", b.Ihrschedule).append("Ick1",this.Ick1).append("Ick2",this.Ick2)
			.append("ET", this.getET()).append("AWRStep1",this.AWRstep1).append("AWRStep2", this.AWRstep2).append("AWR", this.AWR).append("Ihret",this.Ihret).append("WB", this.getWB()).append("SWC", this.getSWC()).append("DELTA",this.getDelta())
			.append("F", this.getF()).append("rateF", this.getRateF()).append("Q", this.getQ()).append("InF",this.getInF()).append("PERC",this.getPERC())
			.append("Loss",this.getLoss()).append("PerLoss",this.getPerLoss()).append("wLostHr",this.getwLostHr()).append("wLostDay",this.getwLostDay())
			.append("iLostHr",this.getiLostHr()).append("iLostDay",this.getiLostDay());			
			
		}catch(JSONException e){
			
			e.printStackTrace();
		}
		*/
		//return resultJSON;
		
		
	}
	
	

}
