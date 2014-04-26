package com.team4.database;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		System.out.println(cal.getTime());
		String formatted = format1.format(cal.getTime());
		return formatted;
	}
}
