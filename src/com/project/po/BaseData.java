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
import java.util.Locale;
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
public class BaseData extends EnviromentData{
	
	public static String mailServerURL = "http://fawn.ifas.ufl.edu/mail/send.php";
	public static String dataServerURL = "http://fawn.ifas.ufl.edu/data/reports/?res";
	public static String ETdataServerURL = "http://test.fawn.ifas.ufl.edu/controller.php/lastWeekET/json/";
	public static ZipCode zipcodes = new ZipCode();
	public Double Ihr_et = 0.0;
	public Double ET0_et = 0.0;
	public Double Rhr_et = 0.0;
	public String Month_et = "";
	public String Hour_et = "";
	public String Date_et = "";
	public String Year_et = "";
	public Integer Ihrschedule_et = 0;
	public ArrayList<Double> Ihr = new ArrayList<Double>();
	public ArrayList<Double> ET0 = new ArrayList<Double>();
	public ArrayList<Double> Rhr = new ArrayList<Double>();
	
	public ArrayList<String> Month = new ArrayList<String>();   
	public ArrayList<String> Hour = new ArrayList<String>();
	public ArrayList<String> Date = new ArrayList<String>();
	public ArrayList<String> Year = new ArrayList<String>();
	public ArrayList<Integer> Ihrschedule = new ArrayList<Integer>();
	//public ArrayList<Integer> Ihrschedule1;
	public Calendar startDate;
	public Calendar endDate;
	public String stnID;
	
	private static final Logger logger = Logger.getLogger(BaseData.class.getName());
	public Double irriWeek;
	
	/**
	 * Constructor method 
	 * @param zipcode
	 * @param days
	 * @param hours
	 * @param irriDepth
	 * @throws Exception
	 * Initialize Soil District fawnID Ihr Ischedule hourlyRainData hourlyETData
	 */
	public BaseData(String zipcode,String[]days, String[] hours, Double irriDepth) throws Exception{
		
		//Ihrschedule1=new ArrayList<Integer>();
		/*edit data for the soil type table */
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
			this.removeInitialValue();
			
		}catch(IOException e){
			
			throw new IOException(e.getMessage());
			
		}	
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
		
		this.postETRequest2ExternalServer(ETdataServerURL,stnID);
		
	}
	/**
	 * post request to server to get hourly ET data for last week
	 * @param stnID fawn station ID
	 * @throws Exception
	 */
	public void postETRequest2ExternalServer(String serverURL,String stnID) throws Exception{
		

		HttpURLConnection connection = Util.createUrlConnection(serverURL+stnID, "text/plain","", "GET");
		/*
		 * Important difference to get ET data
		 * One for GAE, the other for local test 
		 */
		BufferedReader in =new BufferedReader(new InputStreamReader(connection.getInputStream()));  //for GAE
		//BufferedReader in =new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));    //for local test
		this.setETData(in);
		in.close();
		connection.disconnect();
		
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
		

		HttpURLConnection connection = Util.createUrlConnection(serverUrl, "application/x-www-form-urlencoded",postParams, "POST");
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		//System.out.println(connection.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
		BufferedReader in =new BufferedReader(new InputStreamReader(connection.getInputStream()));
		this.setRainData(in);
		in.close();
		connection.disconnect();
		
	
	}
	/**
	 * rainsum for one week
	 * @return rainsum
	 */
	
	public Double getRainFallPerWeek(){
		
		BigDecimal rainsum = new BigDecimal(0.0);
		BigDecimal divisor = new BigDecimal("2.54");
		for(int i =0;i<168;i++){
			
			rainsum = rainsum.add(BigDecimal.valueOf(this.Rhr.get(i)));
			//System.out.println(this.Rhr.get(i)+","+rainsum);
		}
		//rainsum = (double) (Math.round(rainsum*100)/100);
		System.out.println("weekly rainfall: " + rainsum.divide(divisor,2).doubleValue());
		return rainsum.divide(divisor,2).doubleValue();
		
	}
	/**
	 * remove initial value for output convenience, since more attributes don't have initial vale 
	 */
	public void removeInitialValue(){
		
		
		this.Date_et = Date.remove(0);
		this.Year_et = Year.remove(0);
		this.Month_et = Month.remove(0);
		this.Hour_et = Hour.remove(0);
		this.Rhr_et = Rhr.remove(0);
		this.Ihr_et = Ihr.remove(0);
		this.ET0_et = ET0.remove(0);
		this.Ihrschedule_et = Ihrschedule.remove(0);	
	}
	
	public void setInitialValue(){
		
		this.Date.add(0, Date_et);
		this.Year.add(0, Year_et);
		this.Month.add(0, Month_et);
		this.Hour.add(0, Hour_et);
		this.Rhr.add(0, Rhr_et);
		this.Ihr.add(0, Ihr_et);
		this.ET0.add(0, ET0_et);
		this.Ihrschedule.add(0, Ihrschedule_et);
		
		
	}
	/**
	 * set last week rain data
	 * @param in
	 * @throws IOException
	 */
	public void setRainData(BufferedReader in) throws IOException{
		
	
		
		for(int i= 1;i<=24;i++){
			
			String str = in.readLine();
			//System.out.println(str);
			
		}
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Calendar startTime = Calendar.getInstance();
		 
		startTime.add(Calendar.DATE,-14);
		startTime.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		startTime.set(Calendar.HOUR_OF_DAY, 23);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);

		
		for(int i = 0; i < 169; i++){
		  
			this.Rhr.add(0.0);
			this.Date.add(df.format(startTime.getTime()));
			this.Year.add(String.valueOf(startTime.get(Calendar.YEAR)));
			this.Month.add(String.valueOf(startTime.get(Calendar.MONTH)+1));
			this.Hour.add(String.valueOf(startTime.get(Calendar.HOUR_OF_DAY)));
			startTime.add(Calendar.HOUR_OF_DAY, 1);
	    }
		  
		long startDateTime = this.startDate.getTime().getTime();
		
		BigDecimal multiplier = new BigDecimal("2.54");
		multiplier.setScale(5);
		int count = 0;
		while(in.ready()){
			
		 	String[] inputs = in.readLine().split(",");
		 	long time = new Date(inputs[1].replace("\"", "")).getTime();
		 	long diff = time-startDateTime;
		 	long index = (diff/(60*60*1000));
		 	this.Rhr.set((int)index,inputs[2].equals("N/A") ? 0.0 : BigDecimal.valueOf(Double.parseDouble(inputs[2].replace("\"",""))).multiply(multiplier).doubleValue());
		  	
		 	count++;
		  
		}
 
		logger.log(Level.INFO, "Total number of Rain Data is : " + count);
		
		
	}
	/**
	 * set last week ET data
	 * @param in
	 * @throws Exception
	 */
	public void setETData(BufferedReader in) throws Exception{
		
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
            /*Problems happen when EDT starts. Just for that week.
             * Fix bugs by checking diff
             */
            if(diff < 0)
                continue;
            else{
                long index = (diff / (60 * 60 * 1000)) ;
                ET0.set((int)index,jsonarray.getJSONObject(i).getDouble("et_FAO56_mm")/10.0);
            }
			
		}
		//check and modify hourly ET data
		if(jsonarray.length()<169){
			for(int i = 0; i<jsonarray.length();i++){		
				if(ET0.get(i) == -1.0){		
					if(i == 0){			
						ET0.set(i, 0.0);					
					}else{			
						ET0.set(i, ET0.get(i-1));					
					}				
				}					
			}	
			
		}	
		
		
	}
	public static void main(String args[]) throws Exception{
		String[] days = {"1","2","3"};
		String[] hours = {"0","1","2"};
		
		BaseData bd = new BaseData("32608", days, hours, 1.0);
		//bd.requestRainData(bd.startDate, bd.endDate, "32608");
		System.out.println(bd.Rhr.size());
		
		
	}
	

}
