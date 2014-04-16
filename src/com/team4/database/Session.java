package com.team4.database;

public class Session {
	private int session_id;
	private int user_id;
	private long timestamp;
	
	public Session()
	{
		
	}
	
	public Session(int user_id, long timestamp)
	{
		this.user_id = user_id;
		this.timestamp = timestamp;
	}
	
	public static Session create_session(User u)
	{
		return new Session(u.getId(), System.currentTimeMillis());
	}
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getSession_id() {
		return session_id;
	}

	public void setSession_id(int session_id) {
		this.session_id = session_id;
	}


		
}
