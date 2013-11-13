package com.project.po;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import net.sf.*;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class Util {
	
	public static String mailServerURL = "http://fawn.ifas.ufl.edu/mail/send.php";
	public static String dataServerURL = "http://test.fawn.ifas.ufl.edu/controller.php/lastWeekET/json/";
	public static TimeZone timeZoneUsed = TimeZone.getTimeZone("America/New_York");
	//private static final Logger logger = Logger.getLogger(Controller.class.getCanonicalName());
	
	
	
	public static void requestETData(String stnID) throws Exception{
		
		Util.postETRequest2ExternalServer(stnID);
		
	}
	
	public static void postETRequest2ExternalServer(String stnID) throws Exception{
		
		URL url = new URL(dataServerURL+stnID);
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
		int count =0;
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
		
	}


	
	
	
	
	public static void main(String args[]) throws Exception{
		
		Util.postETRequest2ExternalServer("230");
		
		
	}

}
