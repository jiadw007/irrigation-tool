package com.project.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.project.po.BaseData;
import com.project.po.Data;
/**
 * Created with MyEcplise
 * User : Dawei Jia
 * Date: 10/19/2013
 * @author Dawei Jia
 *
 */
public class ETControllerModel extends Hydrology{
	
	
	private ArrayList<Double> Ihret = new ArrayList<Double>();
	private ArrayList<Double> Re = new ArrayList<Double>();
	private ArrayList<Integer> Ick1 = new ArrayList<Integer>();
	private ArrayList<Integer> Ick2 = new ArrayList<Integer>();
	private ArrayList<Double> AWR = new ArrayList<Double>();
	private ArrayList<Double> AWRstep1 = new ArrayList<Double>();
	private ArrayList<Integer> AWRstep2 = new ArrayList<Integer>();

	//private ArrayList<Double> AWRstep3;
	/**
	 * 
	 * @param data
	 * @param b
	 * @throws Exception
	 */
	public ETControllerModel(Data data,BaseData b) throws Exception {
		// TODO Auto-generated constructor stub
		super(data,b);
		double kc = b.Kc.get(this.getLocation().getZone()).get(b.Month.get(0));
		double et0 = b.ET0.get(0);
		this.getET().add(kc*et0);
		this.AWR.add(0.0);  //initial value for AWR
		this.AWRstep1.add(0.0);
		this.AWRstep2.add(0);
		this.Ick1.add(0);
		this.Ick2.add(0);
		this.getB().setInitialValue();
		HashMap<String,Double> SOIL = b.soil.get(this.getSoilType());
		if(b.Rhr.get(0)>(this.getRootDepth()*SOIL.get("FC")-this.getSWC().get(0))){
			
			double re = this.getRootDepth()*SOIL.get("FC")-this.getSWC().get(0);
			this.Re.add(re);
			
			
		}else{
			
			this.Re.add(b.Rhr.get(0));
			
		}
		this.Ihret.add(0.0);
		this.getB().irriWeek = 0.0;
		
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
			if(Ick1.get(i) + Ick2.get(i) == 2){
				
				this.Ihret.add(this.AWR.get(i-1));
				this.getB().irriWeek += this.AWR.get(i-1);
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
		System.out.println("finish calculatoin!");
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
		this.calculateWaterLoss();
		
	}
	
	

}
