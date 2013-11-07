package com.project.po;

import java.util.Calendar;

public class Util {
	
	public static void main(String[] args){
		
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -14);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		System.out.println(cal.get(Calendar.DAY_OF_MONTH));
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DATE, -7);
		cal1.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		System.out.println(cal1.get(Calendar.DAY_OF_MONTH));
	}

}
