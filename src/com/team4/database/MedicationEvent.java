package com.team4.database;


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
	private float dosage;
	private int medication_id;
	private int day;
	private String priority;
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

	public MedicationEvent(int time_hours, int time_mins, int day, float dosage, int medication_id, int user_id, String priority)
	{
		this.time_hours = time_hours;
		this.time_mins = time_mins;
		this.dosage = dosage;
		this.day = day;
		this.medication_id = medication_id;
		this.user_id = user_id;
		this.setPriority(priority);
		
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
	public float getDosage() {
		return dosage;
	}
	public void setDosage(float dosage) {
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
}
