package com.team4.database;


public class Article {
	
	
	/*
	 * article(		id INTEGER PRIMARY KEY AUTOINCREMENT, 
	 * 				type TEXT, 
	 * 				url TEXT, 
	 * 				title TEXT, 	
	 * 				description TEXT, 
	 * 				user INTEGER, 
	 * 				FOREIGN KEY(user) REFERENCES user(id))
				
	 * */

	
	private int Id = 0;
	private String type;
	private String url;
	private String title;
	private String description;
	private int userId;
	
	
	
	public Article(int id, String type, String url, String title, String description,
			int userId) {
		Id = id;
		this.type = type;
		this.url = url;
		this.title = title;
		this.description = description;
		this.userId = userId;
	}



	public int getId() {
		return Id;
	}



	public void setId(int id) {
		Id = id;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	
}
