package com.team4.database;


public class VitalSign {
	
	/*
	 * vitalsign(id INTEGER PRIMARY KEY AUTOINCREMENT, 
	 * 			 type TINYINT, 
	 * 			 value1 INTEGER, 
	 * 			 value2 INTEGER, 
	 * 			 userId INTEGER, 
	 * 			 FOREIGN KEY(userId) REFERENCES user(id));
	 * */
	
	
		
	private int _id = 0;
	private int type;
	private int value1;
	private int value2;
	private int user_Id;
	private int datetime;
	
	public VitalSign()
	{
		
	}
	public VitalSign(int id, int type, int value1, int value2, int user_Id) {
		_id = id;
		this.type = type;
		this.value1 = value1;
		this.value2 = value2;
		this.user_Id = user_Id;
	}
	
	/*
	public VitalSign(User user) {
		super();
		Id = id;
		this.type = type;
		this.value1 = value1;
		this.value2 = value2;
		this.user_Id = user.get_id();
	}
	*/
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getValue1() {
		return value1;
	}
	public void setValue1(int value1) {
		this.value1 = value1;
	}
	public int getValue2() {
		return value2;
	}
	public void setValue2(int value2) {
		this.value2 = value2;
	}
	public int getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(int user_Id) {
		this.user_Id = user_Id;
	}
	public int getDatetime() {
		return datetime;
	}
	public void setDatetime(int datetime) {
		this.datetime = datetime;
	}
	

}
