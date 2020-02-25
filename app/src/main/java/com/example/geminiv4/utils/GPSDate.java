package com.example.geminiv4.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class GPSDate {

	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public String gps2greg(int expirationweek, int expirationsecondofweek){
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR,1980);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		c1.set(Calendar.DAY_OF_MONTH,6);
		c1.set(Calendar.HOUR_OF_DAY,0);
		c1.set(Calendar.MINUTE,0);
		c1.set(Calendar.SECOND,0);
		int weekselapsed = (int)((1024*2+expirationweek) * 604800)/(24*3600);
		c1.add(Calendar.DAY_OF_YEAR, weekselapsed);
        c1.add(Calendar.DAY_OF_YEAR, 1);
		int millisecselapsed = (expirationsecondofweek*1000);
		c1.add(Calendar.MILLISECOND, millisecselapsed);

		return (df.format(c1.getTime()));
	}

	
	
	public HashMap<String, Integer> getGPSTime(String strDate){
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		try {
			Date givendate = df.parse(strDate);
			Calendar c = Calendar.getInstance();
			c.setTime(givendate);
			int year = c.get(Calendar.YEAR);
			int mont = c.get(Calendar.MONTH);
			int day  = c.get(Calendar.DAY_OF_MONTH);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minu = c.get(Calendar.MINUTE);
			int secs = c.get(Calendar.SECOND);
			int noofyears  = year - 1980;
			int dayspassed = noofyears * 365;
			dayspassed = dayspassed + getNoofLeapYears(1980, year) + getNoofDaysPassed(year, mont, day);
			int secspassed = dayspassed * 86400;
			double totalweeks = (double)secspassed / 604800;
			int weekspassed = ((int)totalweeks)%1024;
			int noofdays = (int) ((totalweeks % 1)*7);
			int noofsecs = noofdays * 86400;
			noofsecs = (int) (noofsecs + (getHourspassed(hour, minu, secs) * 3600));
			ret.put("expirationweek",weekspassed);
			ret.put("expirationsecondofweek",noofsecs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	
	private int getNoofLeapYears(int startyear, int endyear) {
		int count = 0;
		for(int i=startyear ; i <= endyear; i++) {
			if(i%4==0) {
				count++;
			}
		}
		return count;
	}
	
	private int getNoofDaysPassed(int year, int month, int day) {
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR,year);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		c1.set(Calendar.DAY_OF_MONTH,6);
		c1.set(Calendar.HOUR_OF_DAY,0);
		c1.set(Calendar.MINUTE,0);
		c1.set(Calendar.SECOND,0);
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.YEAR,year);
		c2.set(Calendar.MONTH,month);
		c2.set(Calendar.DAY_OF_MONTH,day);
		c2.set(Calendar.HOUR_OF_DAY,0);
		c2.set(Calendar.MINUTE,0);
		c2.set(Calendar.SECOND,0);
		Date date1 = c1.getTime();
	    Date date2 = c2.getTime();
	    long diff = date2.getTime() - date1.getTime();
	    long diffdays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		return (int) diffdays;
	}
	
	private double getHourspassed(int hour, int minute, int sec) {
		return ((double)hour + ((double)minute/60) + ((double)sec/3600));
	}

}
