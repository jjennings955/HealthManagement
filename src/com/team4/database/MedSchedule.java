package com.team4.database;

public class MedSchedule {
	public String name;
	public String dosage;
	public String time;
	public String day;
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


}
