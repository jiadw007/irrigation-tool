package com.project.po;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;



import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

	public class DataBase {
	
		private Cache cache;
		private String dbName;
		private static final Logger logger = Logger.getLogger(DataBase.class.getCanonicalName());
	
		public DataBase(String dbName){
		
			this.dbName = dbName;
		
		
	}
	
	public void insertIntoDataBase(Data data) throws ServletException{
		
		logger.log(Level.INFO,"Inserting user "+ data.getEmail() + "'s settings into datastore");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key clientKey= KeyFactory.createKey(dbName,data.getEmail());
		Entity entity = new Entity(clientKey);
		entity.setProperty("unit", data.getUnit());
		entity.setProperty("zipcode", data.getZipcode());
		entity.setProperty("rootDepth", data.getRootDepth());
		entity.setProperty("soilType", data.getSoilType());
		entity.setProperty("area", data.getArea());
		entity.setProperty("systemSelection", this.processArray2String(data.getSystemSelection()));
		entity.setProperty("days", this.processArray2String(data.getDays()));
		entity.setProperty("hours", this.processArray2String(data.getHours()));
		entity.setProperty("rainsettings", data.getRainsettings());
		entity.setProperty("soilthreshold", data.getSoilthreshold());
		entity.setProperty("irriDepth", data.getIrriDepth());
		entity.setProperty("choice", data.getChoice());
		Key key = datastore.put(entity);
		logger.log(Level.INFO,"Putting user " + data.getEmail()+"'s settings into Cache key " +key);
		cache = createCache();
		cache.put(KeyFactory.keyToString(clientKey), entity);
		System.out.println("finish insert into database");
		
		
		
		
		
	}
	public Hashtable<String,Data> fecthAll(){
		
		Hashtable<String,Data> records = new Hashtable<String,Data>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		logger.log(Level.INFO,"Retrieving all the users' setting information from Datastore");
		Query q = new Query(this.dbName);
		PreparedQuery pq = datastore.prepare(q);
		for(Entity result: pq.asIterable()){
			
			String email = (String) result.getKey().getName();
			Data data = this.processEntity2Data(result);
			records.put(email,data);
			
		}
		if(records.isEmpty()){
			
			return null;
		}
		return records;
		
	}
	public Data fetch(String key) throws ServletException{
		
		logger.log(Level.INFO, "Retrieving user " + key +"'s settings from Cache");
		cache = createCache();
		Key clientKey = KeyFactory.createKey(this.dbName, key);
		Entity entity = (Entity) cache.get(KeyFactory.keyToString(clientKey));
		Data data = new Data();
		if(entity ==null){
			
			try{
				
				logger.log(Level.INFO,"Can not find in Cache,so retriveing user " + key + "'s settings from Datastore");
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				Entity result = datastore.get(clientKey);
				data = this.processEntity2Data(result);
				//data = (Data)result.getProperty(colName);
			
			}catch(Exception e){
				
				data = null;
				logger.log(Level.WARNING, e.getMessage());
				
			}
			
			
		}else{
			
			data = this.processEntity2Data(entity);
			
		}
		
		
		return data;
		
	}
	private Cache createCache() throws ServletException{
		// TODO Auto-generated method stub
		Map<String,Object> props = Collections.emptyMap();
		try{
			
			return CacheManager.getInstance().getCacheFactory().createCache(props);
		}catch(CacheException ex){
			logger.log(Level.WARNING, ex.getMessage());
			throw new ServletException("Could no initialize cahce:", ex);
			
		}
	}
	public void cleanUpCacheDB(String key) throws CacheException{
		
		Map<String, Object> props = Collections.emptyMap();
		cache = CacheManager.getInstance().getCacheFactory().createCache(props);
		Key clientKey = KeyFactory.createKey(dbName, key);
		Entity entity =(Entity) cache.get(KeyFactory.keyToString(clientKey));
		if(entity != null){
			
			//Data data = (Data)entity.getProperty("settings");
			logger.info("Removing cache of "+key);
			cache.remove(KeyFactory.keyToString(clientKey));
			
			
		}
		entity = (Entity) cache.get(KeyFactory.keyToString(clientKey));
		if(entity != null){
			
			logger.info("Cache of "+ key +" can not be removed");
					
		}else{
			
			logger.info("Cache of "+ key +"is removed");
		}
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.delete(clientKey);
		logger.info("data of "+ key +" is removed");
		
	}
	public Data processEntity2Data(Entity result){
		
		String email = result.getKey().getName();
		String unit = (String) result.getProperty("unit");
		String zipcode = (String) result.getProperty("zipcode");
		Double rootDepth = (Double) result.getProperty("rootDepth");
		String soilType = (String) result.getProperty("soilType");
		Double area = (Double) result.getProperty("area");
		String[] systemSelection = ((String) result.getProperty("systemSelection")).split(",");
		String[] days = ((String) result.getProperty("days")).split(",");
		String[] hours = ((String) result.getProperty("hours")).split(",");
		Double rainsettings = (Double) result.getProperty("rainsettings");
		Double soilthreshold = (Double) result.getProperty("soilthreshold");
		Double irriDepth = (Double) result.getProperty("irriDepth");
		String choice = (String ) result.getProperty("choice");
		
		Data data = new Data(email,unit,zipcode,soilType,rootDepth,area,systemSelection,days,hours,choice,rainsettings,soilthreshold,irriDepth);
		return data;
	}
	public String processArray2String(String[] str){
		
		StringBuilder sb = new StringBuilder();
		for(String string : str){
			
			sb.append(string+",");
			
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
		
	}
	public static void main(String agrs[]) throws ServletException{
		
		DataBase db= new DataBase("Users");
		
		String[] days={"1","5"};
		String[] hours = {"4","7"};
		
		
		
	}

}
