package com.project.po;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.project.server.calculateServlet;


public class Util{
	
	private static String mailServerURL = "http://fawn.ifas.ufl.edu/mail/send.php";
	public static TimeZone timeZoneUsed = TimeZone.getTimeZone("America/New_York");
	public static int EMAIL_INTRO = 0;
	public static int EMAIL_WEEKLY_REPORT = 1;
	private static final Logger logger = Logger.getLogger(calculateServlet.class.getCanonicalName());
	
	public static String requestWeeklyReport(Hashtable<String,String> results, String email) throws Exception{
		
		String urlParameters = buildWeeklyReportParameters(results, email);
		return  postRequest2ExternalServer(mailServerURL, urlParameters);
		
	}
	
	public static String buildWeeklyReportParameters(Hashtable<String,String> results,String email) throws Exception{
		
		String timestamp = Long.toString(System.nanoTime());
		
		
		
		
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
	
	
}