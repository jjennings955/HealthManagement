package com.example.healthmonitoring;

public class Food {
	
	
	
	/*
	 * 				food(	id INTEGER PRIMARY KEY AUTOINCREMENT, 
	 * 						name TEXT, 
	 * 						calories REAL, 
	 * 						fat REAL, 
	 * 						protein REAL, 
	 * 						carbs REAL, 
	 * 						fiber REAL, 
	 * 						sugar REAL);
	 * */
	
	
	private static int Id = 0;
	private String  name ;
	private float  calories ;
	private float  fat ;
	private float  protein ;
	private float  carbs ;
	private float fiber ;
	private float sugar ;
	
	
	
	public Food(String name, float calories, float fat, float protein,
			float carbs, float fiber, float sugar) {
		Id++;
		this.name = name;
		this.calories = calories;
		this.fat = fat;
		this.protein = protein;
		this.carbs = carbs;
		this.fiber = fiber;
		this.sugar = sugar;
	}
	
	
	public static int getId() {
		return Id;
	}
	public static void setId(int id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getCalories() {
		return calories;
	}
	public void setCalories(float calories) {
		this.calories = calories;
	}
	public float getFat() {
		return fat;
	}
	public void setFat(float fat) {
		this.fat = fat;
	}
	public float getProtein() {
		return protein;
	}
	public void setProtein(float protein) {
		this.protein = protein;
	}
	public float getCarbs() {
		return carbs;
	}
	public void setCarbs(float carbs) {
		this.carbs = carbs;
	}
	public float getFiber() {
		return fiber;
	}
	public void setFiber(float fiber) {
		this.fiber = fiber;
	}
	public float getSugar() {
		return sugar;
	}
	public void setSugar(float sugar) {
		this.sugar = sugar;
	}
	
	

}
