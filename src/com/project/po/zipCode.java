package com.project.po;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Create with MyEclipse
 * User : Dawei Jia
 * Date : 09/19/2013
 * @author Dawei Jia
 * Read zipcode and fawn_zips from .txt file
 *
 */
public class zipCode {
	
	
	public static ArrayList<String> zipcode = new ArrayList<String>();
	public static ArrayList<Float> lats = new ArrayList<Float>();
	public static ArrayList<Float> lngs = new ArrayList<Float>();
	public static ArrayList<String> city = new ArrayList<String>();
	public static ArrayList<String> fawnStnIDs = new ArrayList<String>();
	public static ArrayList<Float> fawnStnLats = new ArrayList<Float>();
	public static ArrayList<Float> fawnStnLngs = new ArrayList<Float>();
	public static ArrayList<String> fawnStnNames = new ArrayList<String>();
	
	
	public zipCode(){
		
		try{
			//File file =new File(zippath);
			//System.out.println(file);
			FileInputStream fstream = new FileInputStream("./zips.txt");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			
			while(br.ready()){
			
				String line = br.readLine();
				String item[] = line.split(",");
				zipcode.add(item[0]);
				lats.add(Float.parseFloat(item[1]));
				lngs.add(Float.parseFloat(item[2]));
				city.add(item[3]);
				
			}
			br.close();
			
		}catch (FileNotFoundException e) { 
		    System.out.println("no file");  
			e.printStackTrace(); 
		} catch (IOException e) { 
		      e.printStackTrace(); 
		} 
		
		try{
			
			FileInputStream fstream = new FileInputStream("./fawn_zips.txt");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			while(br.ready()){
				
				String line = br.readLine();
				String item[] = line.split(",");
				fawnStnIDs.add(item[0]);
				fawnStnLats.add(Float.parseFloat(item[1]));
				fawnStnLngs.add(Float.parseFloat(item[2]));
				fawnStnNames.add(item[3]);
				
			}
			br.close();
			
		}catch (FileNotFoundException e) { 
		      e.printStackTrace(); 
		} catch (IOException e) { 
		      e.printStackTrace(); 
		}
	
	}
	public static void main(String[] args){
		
		//zipCode zip = new zipCode();
		//for(String c:zipCode.city){
			
			//System.out.println(c);
			
		//}
		
		
	}
	

}
