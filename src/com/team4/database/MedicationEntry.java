package com.team4.database;


public class MedicationEntry {
		/*"create table mesdication_tracking(id INTEGER PRIMARY KEY AUTOINCREMENT," +
		" medication_schedule_id INTEGER, date INTEGER, " +
		"FOREIGN KEY(medication_schedule_id) REFERENCES medication_schedule(id));";*/
	private int _id;
	private int med_event_id;
	private int date;
	
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
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
		this.med_event_id = event.getId();
		this.date = date;
	}
	
}
