package com.team4.database;

import android.database.Cursor;

public class MedSchedule implements Comparable<MedSchedule>
{
	public String name;
	public String dosage;
	public String time;
	public int day;
	public String date;
	public boolean status;
	public int id;
	
	public MedSchedule(int id, String name, String dosage, int day, String time, String date, boolean status)
	{
		this.id = id;
		this.name = name;
		this.day = day;
		this.dosage = dosage;
		this.time = time;
		this.date = date;
		this.status = status;
	}

	public MedSchedule() {
	}
	public String getTime()
	{
		return time;
	}
	
	public int getHour()
	{
		int colon = time.indexOf(":");
		String hourStr = time.substring(0, colon);
		
		return Integer.parseInt(hourStr);
	}
	
	public int getMinutes()
	{
		int colon = time.indexOf(":");
		String minStr = time.substring(colon+1, time.length());
		
		return Integer.parseInt(minStr);
	}
	
	public String getName()
	{
		return name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int compareTo(MedSchedule another) {
		return (getHour() * 60 + getMinutes()) - (another.getHour() * 60 + another.getMinutes());
		
	}
	public static MedSchedule getMedSchedule(Cursor cursor)
	{
		int hours = cursor.getInt(4);
		int minutes = cursor.getInt(5);
		String mins = "";
		if (minutes < 10)
			mins = "0" + minutes;
		else
			mins = ""+minutes;
		String timeString = hours + ":" + mins;
		MedSchedule result = new MedSchedule();
		result.id = cursor.getInt(0);
		result.name = cursor.getString(1);
		result.dosage = cursor.getString(2);
		result.day = cursor.getShort(3);
		result.time = timeString;
		return result;
		
	}
}
