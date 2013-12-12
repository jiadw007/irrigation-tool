package com.project.po;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import com.project.model.TimeBasedModel;
import com.project.server.CalculateServlet;

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
	private static final Logger logger = Logger.getLogger(CalculateServlet.class.getCanonicalName());
	
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
		
		DataBase db = new DataBase("Secret");
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
		String app = "UrbanIrrigationVersion2";
		String email_token = createToken(timestamp, app);
		String unsubscribe_token = createToken(timestamp, email);
		//get common result attributes
		String[] attrs = results.elements().nextElement().split(",");
		String startDate = attrs[0];
		String endDate = attrs[1];
		String fawnName = attrs[4];
		//String fawnDistance = attrs[6];
		String rainfall = attrs[5];
		//String irriWeek = attrs[6];
		urlParameters.append("start_date=" + startDate + "&end_date=" + endDate);
		
		Enumeration<String> keys = results.keys();
		while(keys.hasMoreElements()){
			
			String technology = keys.nextElement();
			logger.log(Level.INFO, technology);
			String[] result = results.get(technology).split(",");
			String waterLoss = result[2];
			String percentage = result[3];
			String irriDepth = result[6];
			//String wstressDays = result[4];
			if(technology.equals("Time-based")){
				
				urlParameters.append("&percentage_water_not_used_time_based=" + percentage +"&gallon_water_not_used_time_based=" + waterLoss + "&irriDepth_time_based=" + irriDepth);
				
			}else if(technology.equals("Time-based with rain sensor")){
				
				urlParameters.append("&percentage_water_not_used_rain_sensor=" + percentage +"&gallon_water_not_used_rain_sensor=" + waterLoss +"&irriDepth_rain_sensor=" + irriDepth);
				
			}else if(technology.equals("Time-based with soil moisture sensor")){
				
				urlParameters.append("&percentage_water_not_used_soil_sensor=" + percentage +"&gallon_water_not_used_soil_sensor=" + waterLoss +"&irriDepth_soil_sensor=" + irriDepth);
				
			}else if(technology.equals("ET_Controller")){
				System.out.println("parameter: "+irriDepth);
				urlParameters.append("&percentage_water_not_used_et=" + percentage +"&gallon_water_not_used_et=" + waterLoss+"&irriDepth_et=" + irriDepth);
				
			}
			
		}
		urlParameters.append("&fawn_station_name=" + fawnName + "&to=" + email +"&subject=UrbanIrrigation Weekly Report"
								+ "&template_name=" + app + "&email_token=" + email_token + "&unsubscribe_token=" + unsubscribe_token + "&timestamp=" + timestamp 
								+ "&app=" + app+"&rainfall=" + rainfall );
		logger.log(Level.INFO,urlParameters.toString());
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
		
		HttpURLConnection connection = Util.createUrlConnection(serverURL, "application/x-www-form-urlencoded", postParas, "POST");
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
	
	
	public static Cookie[] createCookies(String name, TimeBasedModel tbm){
		
		Cookie[] results = new Cookie[8];
		System.out.println("Create Cookies !");
		results[0] = new Cookie(name+"_waterLoss",String.valueOf(tbm.getwLostWeek()));
		results[0].setMaxAge(60*60);
		results[0].setPath("/");
		results[1] = new Cookie(name+"_iLoss",String.valueOf(tbm.getiLostWeek()));
		results[1].setMaxAge(60*60);
		results[1].setPath("/");
		results[2] = new Cookie(name+"_wStressDays", String.valueOf(tbm.getwStressDays()));
		results[2].setMaxAge(60*60);
		results[2].setPath("/");
		results[3] = new Cookie("rainfall",String.valueOf(tbm.getB().getRainFallPerWeek()));
		results[3].setMaxAge(60*60);
		results[3].setPath("/");
		results[4] = new Cookie("fawnName", tbm.getLocation().getFawnStnName());
		results[4].setMaxAge(60*60);
		results[4].setPath("/");
		results[5] = new Cookie("startDate",DateFormat.getDateInstance().format(tbm.getB().startDate.getTime()));
		results[5].setMaxAge(60*60);
		results[5].setPath("/");
		results[6] = new Cookie("endDate",DateFormat.getDateInstance().format(tbm.getB().endDate.getTime()));
		results[6].setMaxAge(60*60);
		results[6].setPath("/");
		System.out.println("irri: "+String.valueOf(tbm.getB().IrriWeek));
		results[7] = new Cookie(name+"_irriWeek",String.valueOf(tbm.getB().IrriWeek));
		results[7].setMaxAge(60*60);
		results[7].setPath("/");
		return results;
	}
	
	public static Double processCm2Inch(Double value){
		
		BigDecimal dividend = new BigDecimal(value);
		BigDecimal divisor = new BigDecimal(2.54);
		
		return dividend.divide(divisor, 2).doubleValue();
	} 
	
	public static Cookie[] calculateLossProcess(String name, TimeBasedModel tbm){
		
		System.out.println(tbm.getRootDepth());
		System.out.println(tbm.getUnit());
		System.out.println(tbm.getArea());
		System.out.println(tbm.getSoilType());
		System.out.println(tbm.getB().IrriWeek);
		tbm.getLocation().print();
		tbm.calculation();
		/*
		 * store results in cookies and response
		 */
		tbm.getB().startDate.add(Calendar.DATE, 1);
		if(tbm.getUnit().equals("Metric")){
			
			
			tbm.getB().IrriWeek = Util.processCm2Inch(tbm.getB().IrriWeek);
			System.out.println("After: "+tbm.getB().IrriWeek);		
					
		}
		Cookie[] results = Util.createCookies(name, tbm);
		return results;
	}
	public static String buildWeeklyReportResult(DateFormat df, TimeBasedModel tbm){
		
		tbm.calculation();
		tbm.getB().startDate.add(Calendar.DATE, 1);
		String startDate = df.format(tbm.getB().startDate.getTime());
		String endDate = df.format(tbm.getB().endDate.getTime());
		String waterLoss = String.valueOf(tbm.getwLostWeek());
		String iLoss = String.valueOf(tbm.getiLostWeek());
		String rainfall = String.valueOf(tbm.getB().getRainFallPerWeek());
		//int wStressDays = tbm.getwStressDays();
		String fawnName = tbm.getLocation().getFawnStnName();
		//double fawnDistance = tbm.getLocation().distance;
		double irriDepth = 0.0;
		if(tbm.getUnit().equals("Metric")){

			irriDepth = Util.processCm2Inch(tbm.getB().IrriWeek);
			
		}else{
			
			irriDepth = tbm.getB().IrriWeek;
			
		}
		return startDate+","+endDate+","+waterLoss+","+iLoss+"%,"+fawnName+","+rainfall+","+String.valueOf(irriDepth);
		
		
	}
	
	public static HttpURLConnection createUrlConnection(String serverURL, String contentType, String postParas, String method) throws IOException{
		
		URL url = new URL(serverURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type",
				contentType);
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length",
				"" + Integer.toString(postParas.getBytes().length));
		connection.setUseCaches(false);
		
		return connection;
		
		
	}
	public static void main(String arghhs[]) throws NoSuchAlgorithmException, UnsupportedEncodingException, ServletException{
		
		Util util = new Util();
		String timestamp = Long.toString(System.nanoTime());
		System.out.println(util.createToken(timestamp, "email"));
		
		
		
		
	}
	
}