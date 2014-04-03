package com.example.healthmonitoring;

public class MedicationEntry {
		/*"create table medication_tracking(id INTEGER PRIMARY KEY AUTOINCREMENT," +
		" medication_schedule_id INTEGER, date INTEGER, " +
		"FOREIGN KEY(medication_schedule_id) REFERENCES medication_schedule(id));";*/
	private int _id;
	private int med_event_id;
	private int date;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getEvent() {
		return med_event_id;
	}
	public void setEvent(int med_event_id) {
		this.med_event_id = med_event_id;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public MedicationEntry()
	{
		
	}
	public MedicationEntry(MedicationEvent event, int date)
	{
		this.med_event_id = event.get_id();
		this.date = date;
	}
	
}
