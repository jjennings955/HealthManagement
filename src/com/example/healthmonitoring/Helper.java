package com.example.healthmonitoring;

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
}
