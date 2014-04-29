package com.team4.database;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.ParseException;

public class Helper {
	public static String getHex( byte [] raw ) {
		String hexes = "0123456789ABCDEF";
	    if ( raw == null ) {
	        return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    for ( final byte b : raw ) {
	        hex.append(hexes.charAt((b & 0xF0) >> 4))
	            .append(hexes.charAt((b & 0x0F)));
	    }
	    return hex.toString();
	}
	public static int getDay()
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}
	public static boolean stringMatchesPattern(String s, String patt)
	{
		Pattern compiledPatt = Pattern.compile(patt);
		Matcher match = compiledPatt.matcher(s);
		return match.matches();
	}
	public static String hashPassword(String p)
	{
		MessageDigest digest;
		String out;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			out = p;
			return out;
		}
		byte[] hash = null;
		try {
			hash = digest.digest(p.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			
		}
		return Helper.getHex(hash);
	}
	public static String getDate()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(cal.getTime());
		return formatted;
	}
	public static String getDayThisWeek(int which)
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.DAY_OF_WEEK, which);
		return format1.format(cal);
	}
	public static long getDateLongFromString(String date)
	{
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try
		{
			d = format1.parse(date);
		}
		catch (java.text.ParseException e)
		{
			return -1;
		}
		return d.getTime();
	}
	public static String formatTime(long millis)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		SimpleDateFormat format1 = new SimpleDateFormat("h:mmaa");
		//System.out.println(cal.getTime());
		String formatted = format1.format(cal.getTime());
		return formatted;

	}
	public static String getDateWithOffset(int offset)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.DAY_OF_MONTH, offset);
		SimpleDateFormat format1 = new SimpleDateFormat("M-d-yyyy");

		String formatted = format1.format(cal.getTime());
		return formatted;

	}
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); 
        return cal.getTime();
    }
	public static String toDisplayCase(String s) {
		s = s.replace("\"", "");
	    final String ACTIONABLE_DELIMITERS = " '-/,"; // these cause the character following
	                                                 // to be capitalized

	    StringBuilder sb = new StringBuilder();
	    boolean capNext = true;

	    for (char c : s.toCharArray()) {
	        c = (capNext)
	                ? Character.toUpperCase(c)
	                : Character.toLowerCase(c);
	        sb.append(c);
	        capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
	    }
	    return sb.toString();
	}
}
