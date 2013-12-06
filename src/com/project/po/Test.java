package com.project.po;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * Created with MyEclipse
 * User : Dawei Jia
 * Date : 11/22/2013 
 * @author Dawei Jia
 * Test locally, no use in Irrigation tool
 */
public class Test {
	
	public static final Logger logger = Logger.getLogger(Test.class.getCanonicalName());
	
	public static String ETdataServerURL = "http://test.fawn.ifas.ufl.edu/controller.php/lastWeekET/json/";
	
	public ArrayList<Double> ET0 = new ArrayList<Double>();
	public ArrayList<Double> ET1 = new ArrayList<Double>();
	
	public Calendar startDate;
	public Calendar endDate;
	
	public Test(){
		
		this.setEndDate();
		this.setStartDate();
		
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
	
	
	public void requestETData(String stnID) throws Exception{
		
		this.postETRequest2ExternalServer(stnID);
		
	}
	
	public void postETRequest2ExternalServer(String stnID) throws Exception{
		
		URL url = new URL(ETdataServerURL+stnID);
		logger.log(Level.INFO,ETdataServerURL+stnID);
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
		//System.out.println(connection.getInputStream());
		//BufferedReader in1 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		//System.out.println(in1.readLine());
		//BufferedReader in =new BufferedReader(new InputStreamReader(connection.getInputStream()));  //for GAE
		BufferedReader in =new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));    //for local test
		String str = in.readLine();
		JSONArray jsonarray = new JSONArray(str);
		logger.log(Level.INFO, String.valueOf(jsonarray.length()));
		
		//this.startDate.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		
		for(int i = 0; i < 169; i++){
			
			
			ET1.add(-1.0);
			
		}
		this.startDate.set(Calendar.HOUR_OF_DAY, 23);
		this.startDate.set(Calendar.MINUTE, 0);
		this.startDate.set(Calendar.SECOND, 0);
		this.startDate.set(Calendar.MILLISECOND, 0);
		Date one = this.startDate.getTime();
		System.out.println(one);
		long time1 = one.getTime();
		for(int i = 0; i<jsonarray.length();i++){
			
			Date two = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonarray.getJSONObject(i).getString("dt_hr"));
			
			
			long time2 = two.getTime();
			long diff = time2 - time1;
			
			long index = (diff / (60 * 60 * 1000)) ;
			
			System.out.println(two.toString()+" : " + diff +" , "+ index);
			ET1.set((int) index, jsonarray.getJSONObject(i).getDouble("et_FAO56_mm")/10.0);
			
		}
		
		//modify the ET data
		for(int i = 0; i<jsonarray.length();i++){
			
			if(ET1.get(i) == -1.0){
				
				if(i == 0){
					
					ET1.set(i, 0.0);
					
				}else{
					
					ET1.set(i, ET1.get(i-1));
					
				}
				
				
			}
			
		}
		
		for(int i = 0 ; i<jsonarray.length();i++){
			
			ET0.add(jsonarray.getJSONObject(i).getDouble("et_FAO56_mm")/10.0);
			
		}
		in.close();
		connection.disconnect();
			/*
			String str = in.readLine();
			System.out.println(str);
			StringBuilder sb = new StringBuilder();
			sb.append("{\"ET\":");
			sb.append(str);
			sb.append("}");
			String json = sb.toString();
			System.out.println(json);
			JSONObject jsonobject = new JSONObject(json);
			JSONArray jsonarray = jsonobject.getJSONArray("ET");
			//System.out.println("jsonarray length: "+jsonarray.length());
			logger.log(Level.INFO, String.valueOf(jsonarray.length()));
			for(int i =0;i<jsonarray.length();i++){
				logger.log(Level.INFO,String.valueOf(jsonarray.getJSONObject(i).getDouble("et_FAO56_mm")));
				//System.out.println(jsonarray.getJSONObject(i).getDouble("et_FAO56_mm"));
				
			}   
		
		in.close();
		*/
		
		
	}
	
	public static void main(String args[]) throws Exception{
		/*
		String[] s1 = {"Time-based","Time-based with rain sensor","Time-based with soil moisture sensor","Evapotranspiration Controller"};
		StringBuilder system = new StringBuilder();
		for(String sys : s1){
			
			system.append(sys);
			
		}
		System.out.println();
		System.out.println(system.toString().contains("Time-based with soil moisture sensor"));
		
	}
	*/
	/*
	Cookie time_base_waterLoss = new Cookie("time_base_waterLoss",String.valueOf(tbm.getwLostWeek()));
	time_base_waterLoss.setMaxAge(60*60);
	time_base_waterLoss.setPath("/");
	Cookie time_base_iLoss = new Cookie("time_base_iLoss",String.valueOf(tbm.getiLostWeek()));
	time_base_iLoss.setMaxAge(60*60);
	time_base_iLoss.setPath("/");
	Cookie wStressDays = new Cookie("time_base_wStressDays", String.valueOf(tbm.getwStressDays()));
	wStressDays.setMaxAge(60*60);
	wStressDays.setPath("/");
	Cookie rainfall = new Cookie("rainfall",String.valueOf(tbm.getB().getRainFallPerWeek()));
	rainfall.setMaxAge(60*60);
	rainfall.setPath("/");
	Cookie stnId = new Cookie("stnId", tbm.getB().stnID);
	stnId.setMaxAge(60*60);
	stnId.setPath("/");
	Cookie startDate = new Cookie("startDate",DateFormat.getDateInstance().format(tbm.getB().startDate.getTime()));
	startDate.setMaxAge(60*60);
	startDate.setPath("/");
	Cookie endDate = new Cookie("endDate",DateFormat.getDateInstance().format(tbm.getB().endDate.getTime()));
	endDate.setMaxAge(60*60);
	endDate.setPath("/");
	*/
	/*
		String serverUrl = "http://test.fawn.ifas.ufl.edu/controller.php/stationsJson/";

		URL url = new URL(serverUrl);
		//logger.log(Level.INFO,ETdataServerURL+stnID);
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
		String str = in.readLine();
		JSONObject json = new JSONObject(str);
		Iterator iter =json.keys();
		while(iter.hasNext()){
			
			String key = (String)iter.next();
			JSONObject obj = (JSONObject) json.getJSONObject(key);
			String lat = obj.getString("Latitude");
			String lon = obj.getString("Longitude");
			String name = obj.getString("display_name");
			System.out.println(key+","+lat+","+lon+","+name);
			
			
		}
	*/
		ArrayList<String> Month = new ArrayList<String>();   
		ArrayList<String> Hour = new ArrayList<String>();
		ArrayList<String> Date = new ArrayList<String>();
		ArrayList<String> Year = new ArrayList<String>();
		Calendar cal1 = Calendar.getInstance();
		 
		cal1.add(Calendar.DATE,-14);
		cal1.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		cal1.set(Calendar.HOUR_OF_DAY, 23);
	    cal1.set(Calendar.MINUTE, 0);
	    cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.DATE, -7);
		cal2.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		cal2.set(Calendar.HOUR_OF_DAY, 23);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		for(int i =1; i<=169; i++){
			
			
			Date.add(df.format(cal1.getTime()));
			Year.add(String.valueOf(cal1.get(Calendar.YEAR)));
			Month.add(String.valueOf(cal1.get(Calendar.MONTH)+1));
			Hour.add(String.valueOf(cal1.get(Calendar.HOUR_OF_DAY)));
			cal1.add(Calendar.HOUR_OF_DAY, 1);
			
		}
		for(int i =0; i<=168;i++){
			
			System.out.println(Date.get(i)+","+Year.get(i)+","+Month.get(i)+","+Hour.get(i));
			
		}
	}

}
