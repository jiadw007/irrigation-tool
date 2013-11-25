package com.project.po;




import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
/**
 * Create with MyEclipse
 * User: Dawei Jia
 * Date:9/19/2013
 * @author Dawei Jia
 * basic data for computation model in Irrigation tool
 */
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
	public boolean adjust = false;
	private static final Logger logger = Logger.getLogger(baseData.class.getName());
	
	/**
	 * Constructor method 
	 * @param zipcode
	 * @param days
	 * @param hours
	 * @param irriDepth
	 * @throws Exception
	 * Initialize Soil District fawnID Ihr Ischedule hourlyRainData hourlyETData
	 */
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
		sand.put("MAD", 0.5);
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
		
		try{
			
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
			this.requestRainData(startDate, endDate, stnID);
			this.requestETData(this.stnID);
			
		}catch(IOException e){
			
			throw new IOException(e.getMessage());
			
		}
		
		//ET0.add(0.0);
		//Ihrschedule1.add(0);
		//set the value of Ihrschedule
		
		
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
		
		System.out.println("finish read file");
		//for(int i =0;i<169;i++){
			
			//System.out.println(i+","+Ihrschedule.get(i)+","+Ihrschedule1.get(i));
			
		//}
		*/	
	}
	
	/**
	 * get user Location by zipcode
	 * latitude longitude
	 * @param zipcode
	 * @return Location
	 */
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
	/**
	 * get Nearest fawn station ID
	 * @param loc
	 * @return fawn station ID
	 */
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
	
	/**
	 * calculate distance between user geographical coordinates and nearest fawn geographical coordinates
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return distance Miles
	 */
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
	
	/**
	 * set startDate: the day before last Sunday
	 */
	public void setStartDate() {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -14);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		this.startDate = cal;
	}
	/**
	 * set endDate: last Saturday
	 */
	public void setEndDate() {
		
		Calendar cal = Calendar.getInstance();
		 
		 cal.add(Calendar.DATE,-7);
		 cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		 cal.set(Calendar.HOUR_OF_DAY, 23);
	     cal.set(Calendar.MINUTE, 0);
	     cal.set(Calendar.SECOND, 0);
		 cal.set(Calendar.MILLISECOND, 0);
		 this.endDate = cal;
	}	
	
	
	/**
	 * Invoke function for hourly rain data
	 * @param startDate
	 * @param endDate
	 * @param stnID
	 * @throws Exception
	 */
	public void  requestRainData(Calendar startDate, Calendar endDate,String stnID) throws Exception{
		
		String urlParameter = this.buildRainRequest(startDate, endDate, stnID);
		postRainRequest2ExternalServer(dataServerURL, urlParameter);
		
		//for(int i =0;i<169;i++){
			
			//System.out.println(this.Date.get(i)+","+this.Year.get(i)+","+this.Month.get(i)+","+this.Hour.get(i)+","+this.Rhr.get(i));
				
		//}
		
	}
	/**
	 * Invoke function for hourly rain data
	 * @param stnID
	 * @throws Exception
	 */
	public void requestETData(String stnID) throws Exception{
		
		this.postETRequest2ExternalServer(stnID);
		
	}
	/**
	 * post request to server to get hourly ET data for last week
	 * @param stnID fawn station ID
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws Exception
	 */
	public void postETRequest2ExternalServer(String stnID) throws IOException, JSONException, ParseException{
		
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
		
		/*
		 * Important difference to get ET data
		 * One for GAE, the other for local test 
		 */
		//BufferedReader in =new BufferedReader(new InputStreamReader(connection.getInputStream()));  //for GAE
		BufferedReader in =new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));    //for local test
		
		String str = in.readLine();
		JSONArray jsonarray = new JSONArray(str);
		logger.log(Level.INFO, "Total number of ET Data is : " + String.valueOf(jsonarray.length()));
		
		/*
		 * store ET data according to index, check missing data
		 * Total number is 169 
		 */
		for(int i = 0; i < 169; i++){	
			ET0.add(-1.0);	
		}
		
		long time1  = this.startDate.getTime().getTime();
		
		for(int i = 0 ; i<jsonarray.length();i++){
			
			long time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonarray.getJSONObject(i).getString("dt_hr")).getTime();
			
			long diff = time2 - time1;
			
			long index = (diff / (60 * 60 * 1000)) ;
			
			ET0.set((int)index,jsonarray.getJSONObject(i).getDouble("et_FAO56_mm")/10.0);
			
		}
		in.close();
		connection.disconnect();
		if(jsonarray.length()<169){
			
			//check and modify hourly ET data
			for(int i = 0; i<jsonarray.length();i++){
						
				if(ET0.get(i) == -1.0){
							
					if(i == 0){
								
						ET0.set(i, 0.0);
								
					}else{
								
						ET0.set(i, ET0.get(i-1));
								
					}
							
							
				}
						
			}
			
			adjust = true;
		}
		
		
	
	}
	/**
	 * build rain request parameters
	 * @param fromDate start Date
	 * @param toDate	end Date
	 * @param stnID		fawn station ID
	 * @return urlParameters
	 */
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
	
	/**
	 * post request to server get hourly rain data for last week
	 * @param serverUrl 
	 * @param postParams 
	 * @throws Exception
	 */
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
		BigDecimal multiplicand = new BigDecimal(0.0);
		BigDecimal multiplier = new BigDecimal(2.54);
		while(in.ready()){
		
			String[] inputs = in.readLine().split(",");
			
		    this.Date.add(inputs[1].replace("\"", ""));
		    Date date = new Date(inputs[1].replace("\"", ""));
		    this.Year.add(String.valueOf(date.getYear()+1900));
		    this.Month.add(String.valueOf(date.getMonth()+1));
		    this.Hour.add(String.valueOf(date.getHours()));
		
		    this.Rhr.add(inputs[2].equals("N/A") ? 0.0 : multiplicand.valueOf(Double.parseDouble(inputs[2].replace("\"",""))).multiply(multiplier).doubleValue());
		    //System.out.println(Double.parseDouble(inputs[2].replace("\"", ""))*2.54);
		}
		
		logger.log(Level.INFO, "Total number of Rain Data is : " + this.Date.size());
		in.close();
		connection.disconnect();
		if(this.Date.size()<169){
			
			throw new IOException("Sorry, Error With FAWN Rainfall Data. Please Contact FAWN !");
		}
	}
	
	/**
	 * rainsum for one week
	 * @return rainsum
	 */
	
	public Double getRainFallPerWeek(){
		
		BigDecimal rainsum = new BigDecimal(0.0);
		BigDecimal divisor = new BigDecimal(2.54);
		BigDecimal dividend = new BigDecimal(0.0);
		for(int i =0;i<168;i++){
			
			rainsum = rainsum.add(divisor.valueOf(this.Rhr.get(i)));
			//System.out.println(this.Rhr.get(i)+","+rainsum);
		}
		//rainsum = (double) (Math.round(rainsum*100)/100);
		return rainsum.divide(divisor,2).doubleValue();
		
	}
	/**
	 * remove initial value for output convenience, since more attributes don't have initial vale 
	 */
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