package com.project.po;

import java.util.HashMap;

public class EnviromentData {
	
	public static HashMap<String,HashMap<String, Double>> soil=new HashMap<String,HashMap<String, Double>>();
	public static HashMap<String,HashMap<String, Double>> Kc=new HashMap<String,HashMap<String, Double>>();
	public static final String MAD = "MAD";
	public static final String POROSITY = "Porosity";
	public static final String BULK_DENSITY = "Bulk Density";
	public static final String FC = "FC";
	public static final String WP = "WP";
	public static final String THETA = "theta";
	public static final String PSI = "psi";
	public static final String K = "K";
	public static final String SAND = "Sand";
	public static final String SANDY_LOAM = "Sandy loam";
	public static final String LOAM = "Loam";
	public static final String SILT_LOAM = "Silt loam";
	public static final String CLAY_LOAM = "Clay loam";
	public static final String CLAY = "Clay";
	public static final String JAN = "1";
	public static final String FEB = "2";
	public static final String MAR = "3";
	public static final String APR = "4";
	public static final String MAY = "5";
	public static final String JUN = "6";
	public static final String JUL = "7";
	public static final String AUG = "8";
	public static final String SEP = "9";
	public static final String OCT = "10";
	public static final String NOV = "11";
	public static final String DEC = "12";
	public EnviromentData(){
		
		HashMap<String,Double> sand=new HashMap<String, Double>(8);
		sand.put(MAD, 0.5);
		sand.put(POROSITY, 0.44);
		sand.put(BULK_DENSITY, 1.48);
		sand.put(FC, 0.08);
		sand.put(WP, 0.02);
		sand.put(THETA, 0.42);
		sand.put(PSI, 4.95);
		sand.put(K, 11.78);
		soil.put(SAND,sand);
		HashMap<String,Double> sandyLoam=new HashMap<String, Double>(8);
		sandyLoam.put(MAD, 0.5);
		sandyLoam.put(POROSITY, 0.45);
		sandyLoam.put(BULK_DENSITY, 1.45);
		sandyLoam.put(FC, 0.16);
		sandyLoam.put(WP, 0.06);
		sandyLoam.put(THETA, 0.41);
		sandyLoam.put(PSI, 11.01);
		sandyLoam.put(K, 1.09);
		soil.put(SANDY_LOAM,sandyLoam);
		HashMap<String,Double> loam=new HashMap<String, Double>(8);
		loam.put(MAD, 0.5);
		loam.put(POROSITY, 0.46);
		loam.put(BULK_DENSITY, 1.43);
		loam.put(FC, 0.26);
		loam.put(WP, 0.08);
		loam.put(THETA, 0.43);
		loam.put(PSI, 8.89);
		loam.put(K, 0.34);
		soil.put(LOAM,loam);
		HashMap<String,Double> siltLoam=new HashMap<String, Double>(8);
		siltLoam.put(MAD, 0.5);
		siltLoam.put(POROSITY, 0.5);
		siltLoam.put(BULK_DENSITY, 1.32);
		siltLoam.put(FC, 0.31);
		siltLoam.put(WP, 1.10);
		siltLoam.put(THETA, 0.49);
		siltLoam.put(PSI, 16.68);
		siltLoam.put(K, 0.65);
		soil.put(SILT_LOAM,siltLoam);
		HashMap<String,Double> clayLoam=new HashMap<String, Double>(8);
		clayLoam.put(MAD, 0.4);
		clayLoam.put(POROSITY, 0.46);
		clayLoam.put(BULK_DENSITY, 1.43);
		clayLoam.put(FC, 0.34);
		clayLoam.put(WP, 0.14);
		clayLoam.put(THETA, 0.31);
		clayLoam.put(PSI, 20.88);
		clayLoam.put(K, 0.10);
		soil.put(CLAY_LOAM,clayLoam);
		HashMap<String,Double> clay=new HashMap<String, Double>(8);
		clay.put(MAD, 0.3);
		clay.put(POROSITY, 0.48);
		clay.put(BULK_DENSITY, 1.37);
		clay.put(FC, 0.37);
		clay.put(WP, 0.16);
		clay.put(THETA, 0.39);
		clay.put(PSI, 31.63);
		clay.put(K, 0.03);
		soil.put(CLAY,clay);
		
		/*edit data for the Kc value table*/
		HashMap<String,Double> northFlorida=new HashMap<String,Double>(12);
		northFlorida.put(JAN, 0.35);
		northFlorida.put(FEB, 0.35);
		northFlorida.put(MAR, 0.55);		
		northFlorida.put(APR, 0.80);
		northFlorida.put(MAY, 0.90);
		northFlorida.put(JUN, 0.75);
		northFlorida.put(JUL, 0.70);
		northFlorida.put(AUG, 0.70);
		northFlorida.put(SEP, 0.75);
		northFlorida.put(OCT, 0.70);
		northFlorida.put(NOV, 0.60);
		northFlorida.put(DEC, 0.45);
		Kc.put("North Florida",northFlorida);
		HashMap<String,Double> centerFlorida=new HashMap<String,Double>(12);
		centerFlorida.put(JAN, 0.45);
		centerFlorida.put(FEB, 0.45);
		centerFlorida.put(MAR, 0.65);		
		centerFlorida.put(APR, 0.80);
		centerFlorida.put(MAY, 0.90);
		centerFlorida.put(JUN, 0.75);
		centerFlorida.put(JUL, 0.70);
		centerFlorida.put(AUG, 0.70);
		centerFlorida.put(SEP, 0.75);
		centerFlorida.put(OCT, 0.70);
		centerFlorida.put(NOV, 0.60);
		centerFlorida.put(DEC, 0.45);
		Kc.put("Central Florida",centerFlorida);
		HashMap<String,Double> southFlorida=new HashMap<String,Double>(12);
		southFlorida.put(JAN, 0.71);
		southFlorida.put(FEB, 0.79);
		southFlorida.put(MAR, 0.78);		
		southFlorida.put(APR, 0.86);
		southFlorida.put(MAY, 0.99);
		southFlorida.put(JUN, 0.86);
		southFlorida.put(JUL, 0.86);
		southFlorida.put(AUG, 0.90);
		southFlorida.put(SEP, 0.87);
		southFlorida.put(OCT, 0.86);
		southFlorida.put(NOV, 0.84);
		southFlorida.put(DEC, 0.71);
		Kc.put("South Florida",southFlorida);
		
		
		
	}

}
