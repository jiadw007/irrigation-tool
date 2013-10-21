package com.project.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ETControllerModel extends timeBasedModel{
	
	
	private ArrayList<Double> Ihret;
	private ArrayList<Double> Re;
	private ArrayList<Integer> Ick1;
	private ArrayList<Integer> Ick2;
	private ArrayList<Double> AWR;
	private ArrayList<Double> AWRstep1;
	private ArrayList<Integer> AWRstep2;
	//private ArrayList<Double> AWRstep3;
	
	public ETControllerModel(String soilType, double area, double rootDepth,
			String zipcode, String unit) {
		// TODO Auto-generated constructor stub
		super(soilType, area, rootDepth, zipcode, unit);
		Ihret=new ArrayList();
		Re=new ArrayList();
		Ick1=new ArrayList();
		Ick2=new ArrayList();
		AWR=new ArrayList();
		AWRstep1=new ArrayList();
		AWRstep2=new ArrayList();
		//AWRstep3=new ArrayList();
		double kc = b.Kc.get(this.getLocation().getZone()).get(b.Month.get(0));
		double et0 = b.ET0.get(0);
		this.getET().add(kc*et0);
		this.AWR.add(0.0);  //initial value for AWR
		this.AWRstep1.add(0.0);
		this.AWRstep2.add(0);
		//this.AWRstep3.add(0.0);
		this.Ick1.add(0);
		this.Ick2.add(0);
		//this.getWB().add(0.0);
		
		HashMap<String,Double> SOIL = b.soil.get(this.getSoilType());
		if(b.Rhr.get(0)>(this.getRootDepth()*SOIL.get("FC")-this.getSWC().get(0))){
			
			double re = this.getRootDepth()*SOIL.get("FC")-this.getSWC().get(0);
			this.Re.add(re);
			
			
		}else{
			
			this.Re.add(b.Rhr.get(0));
			
		}
		
		//this.Re.add(0.0);
		//this.getQ().add(0.0);
		//this.getInF().add(0.0);
		//this.getPERC().add(0.0);
		this.Ihret.add(0.0);
		
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
			if(Ick1.get(i)+Ick2.get(i)==2){
				
				this.Ihret.add(this.AWR.get(i-1));
				
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
			super.calculation(i);
			
		}
		
		
		
	}
	
	
	
	

}
