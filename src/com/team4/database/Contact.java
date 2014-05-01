package com.team4.database;

import android.database.Cursor;
import android.telephony.PhoneNumberUtils;

public class Contact {
	private int id;
	private int user_id;
	private String name;
	private String phone;
	private String email;
	
	public Contact()
	{
		
	}
	public final static boolean validatePhone(String phone)
	{
		return phone != null && PhoneNumberUtils.isGlobalPhoneNumber(phone);
	}
	public final static boolean validateName(String name)
	{
		return name != null && !name.equals("");
	}
	public final static boolean validateEmail(String target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	public Contact(User u, String name, String phone, String email)
	{
		this.user_id = u.getId();
		this.name = name;
		this.phone = phone;
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public static Contact getContact(Cursor cursor)
	{
		Contact result = new Contact();
		result.setId(cursor.getInt(0));
		result.setUser_id(cursor.getInt(1));
		result.setName(cursor.getString(2));
		result.setPhone(cursor.getString(3));
		result.setEmail(cursor.getString(4));
		return result;
	}
}
