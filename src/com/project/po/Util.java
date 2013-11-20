package com.project.po;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.project.server.calculateServlet;

import javax.servlet.ServletException;

public class Util{
	
	private static String mailServerURL = "http://fawn.ifas.ufl.edu/mail/send.php";
	public static TimeZone timeZoneUsed = TimeZone.getTimeZone("America/New_York");
	public static int EMAIL_INTRO = 0;
	public static int EMAIL_WEEKLY_REPORT = 1;
	private static final Logger logger = Logger.getLogger(calculateServlet.class.getCanonicalName());
	
	public static String createToken(String timestamp, String type) throws NoSuchAlgorithmException, UnsupportedEncodingException, ServletException{
		
		DataBase db = new DataBase("secret");
		String secret = db.fetch("secret", "secret");
		String originalToken = "{" + secret + "}-{" + timestamp + "}-{" + type + "}";
		MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		digest.update(originalToken.getBytes("UTF8"));
		byte[] hash = digest.digest();
		StringBuffer hexString = new StringBuffer();
		for(int i = 0; i < hash.length; i++){
			
			String hex = Integer.toHexString(0xFF & hash[i]);
			if(hex.length() ==1){
				
				hexString.append('0');
				
				
			}
			hexString.append(hex);
			
		}
		return hexString.toString();

	}
	
	public static String requestWeeklyReport(Hashtable<String,String> results, String email) throws Exception{
		
		String urlParameters = buildWeeklyReportParameters(results, email);
		return  postRequest2ExternalServer(mailServerURL, urlParameters);
		
	}
	
	public static String buildWeeklyReportParameters(Hashtable<String,String> results,String email) throws Exception{
		
		
		//parameters not used : ranking links techonology 
		StringBuilder urlParameters = new StringBuilder();
		String timestamp = Long.toString(System.nanoTime());
		String app = "UrbanIrrigation";
		String email_token = createToken(timestamp, app);
		String unsubscribe_token = createToken(timestamp, email);
		//get common result attributes
		String[] attrs = results.elements().nextElement().split(",");
		String startDate = attrs[0];
		String endDate = attrs[1];
		String fawnName = attrs[5];
		String fawnDistance = attrs[6];
		urlParameters.append("dates=" + startDate + " to " + endDate);
		
		Enumeration<String> keys = results.keys();
		while(keys.hasMoreElements()){
			
			String technology = keys.nextElement();
			String[] result = results.get(technology).split(",");
			String waterLoss = result[2];
			String percentage = result[3];
			String wstressDays = result[4];
			urlParameters.append("&percentage_water_not_used=" + percentage +"&gallon_water_not_used=" +
			                     "&gallon_water_not_used=" + waterLoss +"&water_stress_day="+ wstressDays);
		}
		urlParameters.append("&fawn_station_name=" + fawnName + "&miles_to_fawn_station=" + fawnDistance + "&to="+email +"&subject=Urban Irrigation Weekly Report"
								+ "&template_name=" + app + "&email_token=" + email_token + "&unsubscribe_token=" + unsubscribe_token + "&timestamp=" + timestamp 
								+ "&app=" + app);
		return urlParameters.toString();
		
	}
	public static String postRequest2ExternalServer(String serverURL,
			String postParas) throws IOException {
		URL url = new URL(serverURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length",
				"" + Integer.toString(postParas.getBytes().length));
		connection.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(postParas);
		wr.flush();
		wr.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		while (in.ready()) {

			response.append(in.readLine()+"\n");
		}
		in.close();
		connection.disconnect();
		if (response.length() == 0) {
			// fail
			logger.log(Level.WARNING, "Fail to get response from request "
					+ serverURL);
			return null;
		}
		return response.toString();
	}
	
	
	public static void main(String arghhs[]) throws NoSuchAlgorithmException, UnsupportedEncodingException, ServletException{
		
		Util util = new Util();
		String timestamp = Long.toString(System.nanoTime());
		System.out.println(util.createToken(timestamp, "email"));
		
		
		
		
	}
	
}