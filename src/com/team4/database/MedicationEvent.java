package com.team4.database;

import android.database.Cursor;


public class MedicationEvent {
/*"create table medication_schedule(id INTEGER PRIMARY KEY AUTOINCREMENT, 
	time INTEGER, 
	day TEXT, 
	dosage REAL, 
	medication INTEGER, 
	user INTEGER, 
	FOREIGN KEY(medication) REFERENCES medication(id), FOREIGN KEY(user) REFERENCES user(id));\n" +*/
	private int _id;
	private int time_hours;
	private int time_mins;
	private String dosage;
	private int medication_id;
	private int day;
	
	private int user_id;
	public static boolean validateHours(int hr)
	{
		return hr >= 0 && hr < 24;
	}
	public static boolean validateMins(int min)
	{
		return min >= 0 && min < 60;
	}
	
	private static boolean validateDosage(int min)
	{
		return min > 0;
	}
	private static boolean validateDay(String d)
	{
		d = d.toLowerCase();
		return d.equals("monday") || d.equals("tuesday") || d.equals("wednesday") || d.equals("thursday") || d.equals("friday") || d.equals("saturday") || d.equals("sunday"); 
	}
	
	public MedicationEvent()
	{
		
	}

	public MedicationEvent(int time_hours, int time_mins, int day, String dosage, int medication_id, int user_id)
	{
		this.time_hours = time_hours;
		this.time_mins = time_mins;
		this.dosage = dosage;
		this.day = day;
		this.medication_id = medication_id;
		this.user_id = user_id;
		
	}

	


	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public int getTime_hours() {
		return time_hours;
	}
	public void setTime_hours(int time_hours) {
		this.time_hours = time_hours;
	}
	public int getTime_mins() {
		return time_mins;
	}
	public void setTime_mins(int time_mins) {
		this.time_mins = time_mins;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public int getMedication_id() {
		return medication_id;
	}
	public void setMedication_id(int medication_id) {
		this.medication_id = medication_id;
	}
	public int getUserId() {
		return user_id;
	}
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public static MedicationEvent getMedicationEvent(Cursor cursor)
	{
		MedicationEvent medEvent = new MedicationEvent();
		medEvent.setId(cursor.getInt(0));
		medEvent.setTime_hours(cursor.getInt(1));
		medEvent.setTime_mins(cursor.getInt(2));
		medEvent.setDay(cursor.getShort(3));
		medEvent.setDosage(cursor.getString(4));
		medEvent.setMedication_id(cursor.getInt(5));
		medEvent.setUserId(cursor.getInt(6));
		return medEvent;
	}
	
}
