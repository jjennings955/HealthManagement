package com.example.healthmonitoring;

public class Medication {
	//"create table medication(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, priority INTEGER);
	private int _id;
	private String name;
	private int priority;
	public String getName() {
		return name;
	}
	public static boolean validateName(String name)
	{
		return (name != null && !name.equals(""));
	}
	public static boolean validatePriority(int priority)
	{
		return priority > 0 && priority < 5;
	}
	public Medication(String name, int priority)
	{
		this.name = name;
		this.priority = priority;
	}
	public Medication() {
		
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	
}
