package com.team4.database;


public class FoodJournal {
	
	
	/*
	 * 				food_journal(	id INTEGER PRIMARY KEY AUTOINCREMENT, 
	 * 								amount REAL, 
	 * 								userId INTEGER, 
	 * 								foodId INTEGER, 
	 * 								FOREIGN KEY(user) REFERENCES user(id), 
	 * 								FOREIGN KEY(food) references food2(id));
				
	 * */
	
	private int _id = 0;
	private int userId = 0;
	private int foodId = 0;
	private float amount;
	private long datetime;
	
	public FoodJournal(int id, int userId, int foodId, float amount, long datetime) {
		_id = id;
		this.userId = userId;
		this.foodId = foodId;
		this.amount = amount;
		this.datetime = datetime;
	}
	public FoodJournal()
	{
		
	}


	public int getId() {
		return _id;
	}


	public void setId(int id) {
		_id = id;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getFoodId() {
		return foodId;
	}


	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}


	public float getAmount() {
		return amount;
	}


	public void setAmount(float amount) {
		this.amount = amount;
	}


	public long getDatetime() {
		return datetime;
	}


	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}
	
	
}
