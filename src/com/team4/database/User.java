package com.team4.database;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.telephony.PhoneNumberUtils;

public class User {
	
	private int _id;
	
	private String userName;
	private String passwordHash;
	private String firstName;
	private String lastName;
	private int height_feet;
	private int height_inches;
	private float weight;
	//private Date date_of_birth;
	private char gender;
	private int age;
	
	private String doctor_name;
	private String doctor_phone;
	private String doctor_email;
	
	private String contact_name;
	private String contact_phone;
	private String contact_email;
	
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
	public String toString()
	{
		return "userName: " + userName + " " + "name: " + firstName + " " + lastName + "\n" +
				"     " + "height: " + height_feet + "'" + height_inches + "\"" + "weight: " + weight + "lbs" + "\n" +
				"     " + "gender: " + gender + " age: " + getAge();
	}
	public static HashMap<String, Boolean> validateUserFields(String user, String pass, String fName, String lName,
			int height_feet, int height_inches, float weight, int age, char gender)
			{
				HashMap<String, Boolean> resultMap = new HashMap<String, Boolean>();
				resultMap.put("userName", validateUserName(user));
				resultMap.put("password", validatePassword(pass));
				resultMap.put("first_name", validFirstName(fName));
				resultMap.put("last_name", validLastName(lName));
				resultMap.put("height_feet", validHeightFeet(height_feet));
				resultMap.put("height_inches", validHeightInch(height_inches));
				resultMap.put("weight", validateWeight(weight));
				resultMap.put("age", validateAge(age));
				resultMap.put("gender", validateGender(gender));
				ArrayList<Boolean> resultVals = (ArrayList<Boolean>)resultMap.values();
				boolean valid = !resultVals.contains(Boolean.FALSE);
				resultMap.put("valid", valid);
				
				return resultMap;
				
			}
	
	public User(String user, String pass, String fName, String lName,
			int height_feet, int height_inches, float weight, int age, char gender) 
	{
		
		setFirstName(fName);
		setLastName(lName);
		setUserName(user);
		setPassword(pass);
		setHeight_feet(height_feet);
		setHeight_inches(height_inches);
		setWeight(weight);
		setAge(age);
		setGender(gender);
	}

	
	public void setPassword(String pass) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			passwordHash = pass;
			return;
		}
		byte[] hash = null;
		try {
			hash = digest.digest(pass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			
		}
		passwordHash = Helper.getHex(hash);
	}
	
	public User()
	{
		
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public static boolean validateUserName(String userName)
	{

		return userName != null && Helper.stringMatchesPattern(userName, "[a-zA-Z0-9_]{4,16}");
	}

	public static boolean validatePassword(String password)
	{
		return password != null && Helper.stringMatchesPattern(password, "[a-zA-Z0-9_]{4,16}");
	}
	public static boolean validFirstName(String firstName)
	{
		return firstName != null && Helper.stringMatchesPattern(firstName, "[a-zA-Z0-9_]{1,40}");
	}
	public static boolean validLastName(String lastName)
	{
		return lastName != null && Helper.stringMatchesPattern(lastName, "[a-zA-Z0-9_]{1,40}");
	}
	public static boolean validHeightInch(int inch)
	{
		return inch >= 0 && inch < 12;
	}
	public static boolean validHeightFeet(int feet)
	{
		return feet >= 2 && feet <= 9;
	}
	public static boolean validateWeight(float weight)
	{
		return weight > 0.0f && weight < 2000.0f;
	}
	public static boolean validateGender(char gender)
	{
		return (gender == 'M' || gender == 'F' || gender == 'U');
	}
	public static boolean validateAge(int age)
	{
		return age > 0 && age <= 1000;
	}
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getHeight_feet() {
		return height_feet;
	}

	public void setHeight_feet(int height_feet) {
		this.height_feet = height_feet;
	}

	public int getHeight_inches() {
		return height_inches;
	}

	public void setHeight_inches(int height_inches) {
		this.height_inches = height_inches;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}


	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getDoctor_name() {
		return doctor_name;
	}
	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}
	public String getDoctor_phone() {
		return doctor_phone;
	}
	public void setDoctor_phone(String doctor_phone) {
		this.doctor_phone = doctor_phone;
	}
	public String getDoctor_email() {
		return doctor_email;
	}
	public void setDoctor_email(String doctor_email) {
		this.doctor_email = doctor_email;
	}
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getContact_phone() {
		return contact_phone;
	}
	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}
	public String getContact_email() {
		return contact_email;
	}
	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

}
