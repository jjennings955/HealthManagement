package com.team4.database;

public class MedSchedule 
{
	public String name;
	public String dosage;
	public String time;
	public String day;
	private String date;
	public boolean status;
	public int id;
	
	public MedSchedule(int id, String name, String dosage, String day, String time, boolean status)
	{
		this.id = id;
		this.name = name;
		this.day = day;
		this.dosage = dosage;
		this.time = time;
		this.status = status;
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




}
