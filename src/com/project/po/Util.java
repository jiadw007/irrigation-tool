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
import javax.servlet.http.Cookie;
/**
 * Created with MyEclipse
 * Date : 11/22/2013
 * User : Dawei Jia
 * @author jiadw_000
 * Util function for weeklyreport
 */
public class Util{
	
	private static String mailServerURL = "http://fawn.ifas.ufl.edu/mail/send.php";
	public static TimeZone timeZoneUsed = TimeZone.getTimeZone("America/New_York");
	public static int EMAIL_INTRO = 0;
	public static int EMAIL_WEEKLY_REPORT = 1;
	private static final Logger logger = Logger.getLogger(calculateServlet.class.getCanonicalName());
	
	/**
	 * create security token for email server
	 * @param timestamp
	 * @param type
	 * @return token
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws ServletException
	 */
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
	/**
	 * Invoke function for weekly report 
	 * @param results
	 * @param email
	 * @return reponse from mail server
	 * @throws Exception
	 */
	public static String requestWeeklyReport(Hashtable<String,String> results, String email) throws Exception{
		
		String urlParameters = buildWeeklyReportParameters(results, email);
		return  postRequest2ExternalServer(mailServerURL, urlParameters);
		
	}
	/**
	 * build weekly report parameters
	 * @param results
	 * @param email
	 * @return url parameters
	 * @throws Exception
	 */
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
		String rainfall = attrs[7];
		String irriDepth = attrs[8];
		urlParameters.append("dates=" + startDate + " to " + endDate);
		
		Enumeration<String> keys = results.keys();
		while(keys.hasMoreElements()){
			
			String technology = keys.nextElement();
			String[] result = results.get(technology).split(",");
			String waterLoss = result[2];
			String percentage = result[3];
			//String wstressDays = result[4];
			if(technology.equals("Time_based")){
				
				urlParameters.append("&percentage_water_not_used1=" + percentage +"&gallon_water_not_used1=" + waterLoss);
				
			}else if(technology.equals("Time_based with rain sensor")){
				
				urlParameters.append("&percentage_water_not_used2=" + percentage +"&gallon_water_not_used2=" + waterLoss);
				
			}else if(technology.equals("Time_based with soil moisture sensor")){
				
				urlParameters.append("&percentage_water_not_used3=" + percentage +"&gallon_water_not_used3=" + waterLoss);
				
			}else{
				
				urlParameters.append("&percentage_water_not_used4=" + percentage +"&gallon_water_not_used4=" + waterLoss);
				
			}
			
		}
		urlParameters.append("&fawn_station_name=" + fawnName + "&miles_to_fawn_station=" + fawnDistance + "&to="+email +"&subject=Urban Irrigation Weekly Report"
								+ "&template_name=" + app + "&email_token=" + email_token + "&unsubscribe_token=" + unsubscribe_token + "&timestamp=" + timestamp 
								+ "&app=" + app+"&rainfall="+rainfall+"&irriDepth="+irriDepth);
		
		return urlParameters.toString();
		
	}
	/**
	 * post request to mail server to get weekly report
	 * @param serverURL
	 * @param postParas
	 * @return reponse information from mail server
	 * @throws IOException
	 */
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
	
	
	public static Cookie[] createCookies(String name, String waterLoss, String iLoss, String wstressDays,String rainfall, String stnID,String startDate, String endDate){
		
		Cookie[] results = new Cookie[7];
		System.out.println("Create Cookies !");
		results[0] = new Cookie(name+"_waterLoss",waterLoss);
		results[0].setMaxAge(60*60);
		results[0].setPath("/");
		results[1] = new Cookie(name+"_iLoss",iLoss);
		results[1].setMaxAge(60*60);
		results[1].setPath("/");
		results[2] = new Cookie(name+"_wStressDays", wstressDays);
		results[2].setMaxAge(60*60);
		results[2].setPath("/");
		results[3] = new Cookie("rainfall",rainfall);
		results[3].setMaxAge(60*60);
		results[3].setPath("/");
		results[4] = new Cookie("stnId", stnID);
		results[4].setMaxAge(60*60);
		results[4].setPath("/");
		results[5] = new Cookie("startDate",startDate);
		results[5].setMaxAge(60*60);
		results[5].setPath("/");
		results[6] = new Cookie("endDate",endDate);
		results[6].setMaxAge(60*60);
		results[6].setPath("/");
		return results;
	}
	
	
	
	public static void main(String arghhs[]) throws NoSuchAlgorithmException, UnsupportedEncodingException, ServletException{
		
		Util util = new Util();
		String timestamp = Long.toString(System.nanoTime());
		System.out.println(util.createToken(timestamp, "email"));
		
		
		
		
	}
	
}