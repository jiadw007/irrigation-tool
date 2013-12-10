package com.project.po;

import java.util.HashMap;

public class EnviromentData {
	
	public static HashMap<String,HashMap<String, Double>> soil=new HashMap<String,HashMap<String, Double>>();
	public static HashMap<String,HashMap<String, Double>> Kc=new HashMap<String,HashMap<String, Double>>();
	public EnviromentData(){
		
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
		
		
		
	}

}
