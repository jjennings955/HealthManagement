package com.team4.database;


public class FoodJournal {
	
	
	/*
	 * 				food_journal(	id INTEGER PRIMARY KEY AUTOINCREMENT, 
	 * 								amount REAL, 
	 * 								userId INTEGER, 
	 * 								foodId INTEGER, 
	 * 								FOREIGN KEY(user) REFERENCES user(id), 
	 * 								FOREIGN KEY(food) references food(id));
				
	 * */
	
	private int _id = 0;
	private int userId = 0;
	private int foodId = 0;
	
	
	public FoodJournal(int id, int userId, int foodId) {
		_id = id;
		this.userId = userId;
		this.foodId = foodId;
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
	
	
}
