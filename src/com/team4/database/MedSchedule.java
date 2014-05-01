package com.team4.database;

public class MedSchedule implements Comparable<MedSchedule>
{
	public String name;
	public String dosage;
	public String time;
	public int day;
	private String date;
	public boolean status;
	public String priority;
	public int id;
	
	public MedSchedule(int id, String name, String dosage, int day, String time, String date, boolean status, String priority)
	{
		this.id = id;
		this.name = name;
		this.day = day;
		this.dosage = dosage;
		this.time = time;
		this.date = date;
		this.status = status;
		this.priority = priority;
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

	public boolean getStatus()
	{
		return status;
	}
	
	public String getPriority()
	{
		return priority;
	}
	
	public int getID()
	{
		return id;
	}


}
