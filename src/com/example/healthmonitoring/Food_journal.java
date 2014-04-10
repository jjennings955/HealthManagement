package com.example.healthmonitoring;

public class Food_journal {
	
	
	/*
	 * 				food_journal(	id INTEGER PRIMARY KEY AUTOINCREMENT, 
	 * 								amount REAL, 
	 * 								userId INTEGER, 
	 * 								foodId INTEGER, 
	 * 								FOREIGN KEY(user) REFERENCES user(id), 
	 * 								FOREIGN KEY(food) references food(id));
				
	 * */
	
	private static int Id = 0;
	private int userId = 0;
	private int foodId = 0;
	
	
	public Food_journal(int id, int userId, int foodId) {
		Id = id;
		this.userId = userId;
		this.foodId = foodId;
	}


	public static int getId() {
		return Id;
	}


	public static void setId(int id) {
		Id = id;
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
