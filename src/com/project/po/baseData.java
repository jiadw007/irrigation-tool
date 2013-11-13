package com.project.po;




import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import com.google.appengine.labs.repackaged.org.json.JSONArray;

public class baseData {
	
	public static String mailServerURL = "http://fawn.ifas.ufl.edu/mail/send.php";
	public static String dataServerURL = "http://fawn.ifas.ufl.edu/data/reports/?res";
	public static String ETdataServerURL = "http://test.fawn.ifas.ufl.edu/controller.php/lastWeekET/json/";
	public static HashMap<String,HashMap<String, Double>> soil=new HashMap<String,HashMap<String, Double>>();
	public static HashMap<String,HashMap<String, Double>> Kc=new HashMap<String,HashMap<String, Double>>();
	public static zipCode zipcodes;
	
	public ArrayList<Double> Ihr;
	public ArrayList<Double> ET0;
	public ArrayList<Double> Rhr;
	public ArrayList<String> Month;
	public ArrayList<String> Hour;
	public ArrayList<String> Date;
	public ArrayList<String> Year;
	public ArrayList<Integer> Ihrschedule;
	//public ArrayList<Integer> Ihrschedule1;
	public Calendar startDate;
	public Calendar endDate;
	public String stnID;
	/**
	public ArrayList<String> Date;			//from csv.file
	public ArrayList<String> Year;			//from csv.file
	public ArrayList<String> Month;			//from csv.file
	public ArrayList<String> Hour;			//from csv.file
	//public ArrayList<Double> WB;			//from csv.file
	public ArrayList<Double> Rhr;			//from csv.file
	public ArrayList<Double> Ihr;			//from csv.file
	public ArrayList<Double> ET0;          //from csv.file
	public ArrayList<Integer> Ihrschedule;   //from csv.file
	public String Filename;
	 * @throws Exception 
	**/
	public baseData(String zipcode,String[]days, String[] hours, Double irriDepth) throws Exception{
		
		zipcodes = new zipCode();
		
		Date=new ArrayList<String>();			
		Year=new ArrayList<String>();			
		Month=new ArrayList<String>();			
		Hour=new ArrayList<String>();			
		//WB=new ArrayList<Double>();			
		Rhr=new ArrayList<Double>();			
		Ihr=new ArrayList<Double>();			
		ET0=new ArrayList<Double>();         
		Ihrschedule=new ArrayList<Integer>();
		//Ihrschedule1=new ArrayList<Integer>();
		/*edit data for the soil type table */
		HashMap<String,Double> sand=new HashMap<String, Double>(8);
		sand.put("MAD", 0.3);
		sand.put("Porosity", 0.44);
		sand.put("Bulk Density", 1.48);
		sand.put("FC", 0.08);
		sand.put("WP", 0.02);
		sand.put("theta", 0.42);
		sand.put("psi", 4.95);
		sand.put("K", 11.78);
		soil.put("Sand",sand);
		HashMap<String,Double> sandyLoam=new HashMap<String, Double>(8);
		sandyLoam.put("MAD", 0.5);
		sandyLoam.put("Porosity", 0.45);
		sandyLoam.put("Bulk Density", 1.45);
		sandyLoam.put("FC", 0.16);
		sandyLoam.put("WP", 0.06);
		sandyLoam.put("theta", 0.41);
		sandyLoam.put("psi", 11.01);
		sandyLoam.put("K", 1.09);
		soil.put("Sandy loam",sandyLoam);
		HashMap<String,Double> loam=new HashMap<String, Double>(8);
		loam.put("MAD", 0.5);
		loam.put("Porosity", 0.46);
		loam.put("Bulk Density", 1.43);
		loam.put("FC", 0.26);
		loam.put("WP", 0.08);
		loam.put("theta", 0.43);
		loam.put("psi", 8.89);
		loam.put("K", 0.34);
		soil.put("Loam",loam);
		HashMap<String,Double> siltLoam=new HashMap<String, Double>(8);
		siltLoam.put("MAD", 0.5);
		siltLoam.put("Porosity", 0.5);
		siltLoam.put("Bulk Density", 1.32);
		siltLoam.put("FC", 0.31);
		siltLoam.put("WP", 1.10);
		siltLoam.put("theta", 0.49);
		siltLoam.put("psi", 16.68);
		siltLoam.put("K", 0.65);
		soil.put("Silt loam",siltLoam);
		HashMap<String,Double> clayLoam=new HashMap<String, Double>(8);
		clayLoam.put("MAD", 0.4);
		clayLoam.put("Porosity", 0.46);
		clayLoam.put("Bulk Density", 1.43);
		clayLoam.put("FC", 0.34);
		clayLoam.put("WP", 0.14);
		clayLoam.put("theta", 0.31);
		clayLoam.put("psi", 20.88);
		clayLoam.put("K", 0.10);
		soil.put("Clay loam",clayLoam);
		HashMap<String,Double> clay=new HashMap<String, Double>(8);
		clay.put("MAD", 0.3);
		clay.put("Porosity", 0.48);
		clay.put("Bulk Density", 1.37);
		clay.put("FC", 0.37);
		clay.put("WP", 0.16);
		clay.put("theta", 0.39);
		clay.put("psi", 31.63);
		clay.put("K", 0.03);
		soil.put("Clay",clay);
		
		/*edit data for the Kc value table*/
		HashMap<String,Double> northFlorida=new HashMap<String,Double>(12);
		northFlorida.put("1", 0.35);
		northFlorida.put("2", 0.35);
		northFlorida.put("3", 0.55);		
		northFlorida.put("4", 0.80);
		northFlorida.put("5", 0.90);
		northFlorida.put("6", 0.75);
		northFlorida.put("7", 0.70);
		northFlorida.put("8", 0.70);
		northFlorida.put("9", 0.75);
		northFlorida.put("10", 0.70);
		northFlorida.put("11", 0.60);
		northFlorida.put("12", 0.45);
		Kc.put("North Florida",northFlorida);
		HashMap<String,Double> centerFlorida=new HashMap<String,Double>(12);
		centerFlorida.put("1", 0.45);
		centerFlorida.put("2", 0.45);
		centerFlorida.put("3", 0.65);		
		centerFlorida.put("4", 0.80);
		centerFlorida.put("5", 0.90);
		centerFlorida.put("6", 0.75);
		centerFlorida.put("7", 0.70);
		centerFlorida.put("8", 0.70);
		centerFlorida.put("9", 0.75);
		centerFlorida.put("10", 0.70);
		centerFlorida.put("11", 0.60);
		centerFlorida.put("12", 0.45);
		Kc.put("Central Florida",centerFlorida);
		HashMap<String,Double> southFlorida=new HashMap<String,Double>(12);
		southFlorida.put("1", 0.71);
		southFlorida.put("2", 0.79);
		southFlorida.put("3", 0.78);		
		southFlorida.put("4", 0.86);
		southFlorida.put("5", 0.99);
		southFlorida.put("6", 0.86);
		southFlorida.put("7", 0.86);
		southFlorida.put("8", 0.90);
		southFlorida.put("9", 0.87);
		southFlorida.put("10", 0.86);
		southFlorida.put("11", 0.84);
		southFlorida.put("12", 0.71);
		Kc.put("South Florida",southFlorida);
		
		this.setStartDate();
		this.setEndDate();
		this.stnID = this.getNearByFawnStnID(this.getLocationByzipCode(zipcode));
		System.out.println("stnID : " +this.stnID);
		this.requestRainData(startDate, endDate, stnID);
		this.requestETData(this.stnID);
		//ET0.add(0.0);
		//Ihrschedule1.add(0);
		//set the value of Ihrschedule
		
		for(int i =0;i<169;i++){
					
			Ihrschedule.add(0);
			Ihr.add(0.0);
		}
		for(int i =0 ;i<days.length;i++){
			
			int day = Integer.parseInt(days[i])-1;
			int hour = Integer.parseInt(hours[i])+1;
			int index = 24 * day + hour;
			Ihrschedule.set(index, 1);
			Ihr.set(index, irriDepth);
		}
		//get et value
		
		/*
		try{
			
			File csv = new File("time-base-trial.csv");
			BufferedReader br =new BufferedReader(new FileReader(csv));
			br.readLine();
			int i =1;
			while(br.ready()&&i<=168){
				
				String line = br.readLine();
				String item[] = line.split(",");
				double ihr = Double.parseDouble(item[5]);
				//Ihr.add(ihr);
				ET0.add(Double.parseDouble(item[7]));
				//Ihrschedule1.add(Integer.parseInt(item[10]));
				
				i++;
			}
			br.close();
			
			
		}catch (FileNotFoundException e) { 
		      e.printStackTrace(); 
		} catch (IOException e) { 
		      e.printStackTrace(); 
		}
		*/
		System.out.println("finish read file");
		//for(int i =0;i<169;i++){
			
			//System.out.println(i+","+Ihrschedule.get(i)+","+Ihrschedule1.get(i));
			
		//}
			
	}
	
	public Location getLocationByzipCode(String zipcode){
		
		int index = this.zipcodes.zipcode.indexOf(zipcode);
		if(index != -1){
			
			Float lat = this.zipcodes.lats.get(index);
			Float lng = this.zipcodes.lngs.get(index);
			String city = this.zipcodes.city.get(index);
			Location currentLocation = new Location(zipcode,lat,lng,city);
			this.getNearByFawnStnID(currentLocation);
			//currentLocation.setDistance(fawnStnLocation.getDistance());
			//currentLocation.setFawnStnID(fawnStnLocation.getFawnStnID());
			//currentLocation.setFawnStnName(fawnStnLocation.getFawnStnName());
			return currentLocation;
		}else{
			
			System.out.println("zipcode out of range !");
			return null;
			
		}
		
		
		
		
	}
	
	public String getNearByFawnStnID(Location loc){
		
		float lat = loc.getLat();
		float lng = loc.getLng();
		int length = this.zipcodes.fawnStnIDs.size();
		int index = 0;
		double distance = Math.pow((lat - this.zipcodes.fawnStnLats.get(0)),2.0)+Math.pow((lng - this.zipcodes.fawnStnLngs.get(0)), 2.0);
		for(int i =0; i < length; i++){
			
			double dist = Math.pow((lat - this.zipcodes.fawnStnLats.get(i)),2.0)+Math.pow((lng - this.zipcodes.fawnStnLngs.get(i)), 2.0);
			//System.out.println(dist);
			if(dist < distance){
				
				index = i;
				distance = dist;
				//System.out.println(index);
			}
			
		}
		loc.setFawnStnID(this.zipcodes.fawnStnIDs.get(index));
		loc.setFawnStnName(this.zipcodes.fawnStnNames.get(index));
		loc.setFawnStnLat(this.zipcodes.fawnStnLats.get(index));
		loc.setFawnStnLng(this.zipcodes.fawnStnLngs.get(index));
		double distanceMiles = this.distanceCalculation(lat, lng, this.zipcodes.fawnStnLats.get(index),this.zipcodes.fawnStnLngs.get(index));
		loc.setDistance((float)distanceMiles);
		return loc.getFawnStnID();
		
	}
	
	public double distanceCalculation(float lat1, float lng1, float lat2, float lng2){
		
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2)+
				   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				   Math.sin(dLng/2) *Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = earthRadius *c;
		return dist;
		
		
	}
	
	
	public void setStartDate() {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -14);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		
		this.startDate = cal;
	}

	public void setEndDate() {
		
		Calendar cal = Calendar.getInstance();
		 
		 cal.add(Calendar.DATE,-7);
		 cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		 this.endDate = cal;
	}	
	
	
	
	public void  requestRainData(Calendar startDate, Calendar endDate,String stnID) throws Exception{
		
		String urlParameter = this.buildRainRequest(startDate, endDate, stnID);
		postRainRequest2ExternalServer(dataServerURL, urlParameter);
		/*
		for(int i =0;i<169;i++){
			
			System.out.println(this.Date.get(i)+","+this.Year.get(i)+","+this.Month.get(i)+","+this.Hour.get(i)+","+this.Rhr.get(i));
				
		}
		*/
	}
	
	public static void requestETData(String stnID) throws Exception{
		
		Util.postETRequest2ExternalServer(stnID);
		
	}
	
	public void postETRequest2ExternalServer(String stnID) throws Exception{
		
		URL url = new URL(ETdataServerURL+stnID);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		//System.out.println(connection.getDoInput());
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "text/plain");
		connection.setRequestProperty("charset", "utf-8");
		//connection.setRequestProperty("Content-Encoding", "gzip");
		//connection.setRequestProperty("Content-Length", "541");
		connection.setUseCaches(false);
		
		BufferedReader in =new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
		
		/*
		while(in.ready()){
			try{
				//System.out.print(in.readLine());
				JSONArray jsonarray = new JSONArray(in.readLine());
				for(int i =0; i<jsonarray.length();i++){
					
					System.out.println(jsonarray.getJSONObject(i));//.get("et_FAO56_mm"));
					count++;
					
				}
				
			}catch(Exception e){
				
				e.printStackTrace();
				
			}
			System.out.println(count);
			
			
			
		}
		*/
		JSONArray jsonarray = new JSONArray(in.readLine());
		System.out.println("jsonarray length: "+jsonarray.length());
		for(int i =0;i<jsonarray.length();i++){
			
			ET0.add(jsonarray.getJSONObject(i).getDouble("et_FAO56_mm"));
			
		}
		
	}
	public String buildRainRequest(Calendar fromDate,Calendar toDate, String stnID) {
	
		int fromMonth = fromDate.get(Calendar.MONTH)+1;
		int fromDay = fromDate.get(Calendar.DAY_OF_MONTH);
		int fromYear = fromDate.get(Calendar.YEAR);
		int toMonth = toDate.get(Calendar.MONTH)+1;
		int toDay = toDate.get(Calendar.DAY_OF_MONTH);
		int toYear = toDate.get(Calendar.YEAR);
		
		String urlParameters = "locs__" + stnID.trim() + "=on&fromDate_m=" + fromMonth +"&fromDate_d="+ fromDay +"&fromDate_y=" + fromYear +
				"&toDate_m=" + toMonth +"&toDate_d=" + toDay +"&toDate_y=" + toYear +"&reportType=hourly&presetRange=dates&vars__Rainfall=on&format=.CSV+%28Excel%29";
		return urlParameters;
		
		
	}
	
	
	public void postRainRequest2ExternalServer(String serverUrl, String postParams) throws Exception{
		
		URL url = new URL(serverUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length", ""+ Integer.toString(postParams.getBytes().length));
		connection.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		//System.out.println(connection.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
		BufferedReader in =new BufferedReader(new InputStreamReader(connection.getInputStream()));
		for(int i= 1;i<=24;i++){
			
			in.readLine();
			
		}
		while(in.ready()){
		
			String[] inputs = in.readLine().split(",");
			
		    this.Date.add(inputs[1].replace("\"", ""));
		    Date date = new Date(inputs[1].replace("\"", ""));
		    this.Year.add(String.valueOf(date.getYear()+1900));
		    this.Month.add(String.valueOf(date.getMonth()+1));
		    this.Hour.add(String.valueOf(date.getHours()));
		    this.Rhr.add(Double.parseDouble(inputs[2].equals("N/A") ? "0" : inputs[2].replace("\"", ""))*2.54);
		}
		in.close();
		
		
	}
	
	
	public Double getRainFallPerWeek(){
		Double rainsum = 0.0;
		for(int i =0;i<168;i++){
			
			rainsum += this.Rhr.get(i)/2.54;
			
		}
		//rainsum = (double) (Math.round(rainsum*2.54*1000)/1000);
		return rainsum;
		
	}
	
	public void removeInitialValue(){
		
		Date.remove(0);
		Year.remove(0);
		Month.remove(0);
		Hour.remove(0);
		Rhr.remove(0);
		Ihr.remove(0);
		ET0.remove(0);
		Ihrschedule.remove(0);
		
		
	}
	

}
