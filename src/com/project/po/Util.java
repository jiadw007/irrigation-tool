package com.project.po;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;


class Util{
	
	private String ETdataServerURL = "http://test.fawn.ifas.ufl.edu/controller.php/lastWeekET/json/";
	
	public void requestETData(String stnID) throws Exception{
		
		this.postETRequest2ExternalServer(stnID);
		
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
		
			
			while(in.ready()){
				
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
				System.out.println("jsonarray length: "+jsonarray.length());
				for(int i =0;i<jsonarray.length();i++){
					
					System.out.println(jsonarray.getJSONObject(i).getDouble("et_FAO56_mm"));
					
				}
			}
			in.close();
		
		
		
	}
	public static void main(String[] args) throws Exception{
		
		String[] days = {"1","2"};
		String str = days.toString();
		System.out.println(str);
		
		
	}
	
	
	
}