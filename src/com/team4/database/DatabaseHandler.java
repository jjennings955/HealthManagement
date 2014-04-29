package com.team4.database;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import com.team4.healthmonitor.R;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PHMS";
    private static final int DATABASE_VERSION = 25;
    private Context myContext;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		String[] tables = { "user", "vitalsign", "article", "food", "food_journal", "medication", "medication_schedule", "medication_tracking" };
		String create_statement = "" + 
				"create table user(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, first_name TEXT, last_name TEXT, gender TEXT, height_feet TINYINT, height_inches TINYINT, weight INTEGER, age INTEGER, doctor_name TEXT, doctor_number TEXT, doctor_email TEXT, contact_name TEXT, contact_number TEXT, contact_email TEXT);\n" +
				"create table contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, name TEXT, phone TEXT, email TEXT, FOREIGN KEY(user_id) REFERENCES user(id) );\n" +
				"create table sessions(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, timestamp INTEGER, FOREIGN KEY(user_id) REFERENCES user(id));\n" +
				"create table vitalsign(id INTEGER PRIMARY KEY AUTOINCREMENT, type TINYINT, value1 INTEGER, value2 INTEGER, datetime INTEGER, user INTEGER, FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create table article(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, url TEXT, title TEXT, description TEXT, user INTEGER, FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create table food(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, calories REAL, fat REAL, protein REAL, carbs REAL, fiber REAL, sugar REAL);\n" +
				"create table food2(id INTEGER PRIMARY KEY, calories REAL, protein REAL, lipid REAL, carbs REAL, fiber REAL, sugar REAL, potassium REAL, sodium REAL, fat_sat REAL, fat_mono REAL, fat_poly REAL, cholesterol REAL, weight_serving1 REAL, desc_serving1 TEXT, weight_serving2 REAL, desc_serving2 TEXT, description TEXT);\n" +
				
				"create table food_journal(id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, user INTEGER, food INTEGER, datetime INTEGER, FOREIGN KEY(user) REFERENCES user(id), FOREIGN KEY(food) references food(id));\n" +
				"create virtual table food_search using fts3(id INTEGER, description TEXT);\n" +
				"create table medication(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, priority INTEGER);\n" +
				"create table medication_schedule(id INTEGER PRIMARY KEY AUTOINCREMENT, time_hours INTEGER,  time_mins INTEGER, day TINYINT, dosage REAL, medication INTEGER, user INTEGER, FOREIGN KEY(medication) REFERENCES medication(id), FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create table medication_tracking(medication_schedule_id INTEGER, date TEXT, FOREIGN KEY(medication_schedule_id) REFERENCES medication_schedule(id) ON DELETE CASCADE, primary key (medication_schedule_id, date));\n";
				//select count(*) from schedule where schedule_id = ? and 
				/*"create view schedule as " +
				"	select U.id as user_id, S.id schedule_id, M.name as medication_name, S.dosage as medication_dosage, S.day as day, S.time_hours as time_hours, S.time_mins as time_mins, MT.medication_schedule_id as taken_entry" +
				"	from " +
				"		(medication_schedule as S JOIN user as U JOIN medication as M ON " +
				"			S.user = U.id and S.medication = M.id) " +
				"		LEFT OUTER JOIN medication_tracking as MT ON " +
				"			MT.medication_schedule_id = S.id" +
				""*/; 
		
		String statements[] = create_statement.split("\n");
		//db.beginTransaction();
		for (int i = 0; i < statements.length; i++)
		{
			Log.w("PHMS", statements[i]);
			db.execSQL(statements[i]);
		}
		//db.endTransaction();
		User jason = new User("admin", "admin", "jason", "jennings", 6, 1, 200, 26, 'M');
		
		ArrayList<Medication> meds = new ArrayList<Medication>();
		
		meds.add(new Medication("Tylenol", 1));
		meds.add(new Medication("Viagra", 1));
		meds.add(new Medication("Aspirin", 1));
		meds.add(new Medication("Viox", 1));
		meds.add(new Medication("Heroin", 1));
		meds.add(new Medication("Caffeine", 1));
		MedicationEvent mv = new MedicationEvent(5, 24, Calendar.WEDNESDAY, 5, 1, 1);
		
		
		for (Medication m : meds)
		{
			this.store(m, db);
		}
		
		this.store(jason, db);
		this.store(mv, db);
		mv.setMedication_id(2);
		this.store(mv, db);
	}
	public void medicationTaken(int id, String date)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("date", date);
		cv.put("medication_schedule_id", id);
		db.insert("medication_tracking", null, cv);
	}
	public void medicationNotTaken(int id, String date)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete("medication_tracking", "date = ? and medication_schedule_id = ?", new String[] { "" + date, "" + id });
	}
	public Cursor getMedScheduleCursor(User u)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from schedule where user_id = ?", new String[] { "" + u.getId() });
		return cur;
	}
	public ArrayList<MedSchedule> getUserMedicationSchedule(User u, int day, String date)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<MedSchedule> results = new ArrayList<MedSchedule>();
		String query1 = "select S.id, M.name, S.dosage, S.day, S.time_hours, S.time_mins from medication as M, medication_schedule as S where M.id = S.medication and not exists (select * from medication_tracking where medication_schedule_id = S.id and date = ?) and user = ? and day = ? order by S.time_hours, S.time_mins, M.name;";
		String query2 = "select S.id, M.name, S.dosage, S.day, S.time_hours, S.time_mins from medication as M, medication_schedule as S where M.id = S.medication and exists (select * from medication_tracking where medication_schedule_id = S.id and date = ?) and user = ? and day = ? order by S.time_hours, S.time_mins, M.name;";
		
		Cursor cursor = db.rawQuery(query1, new String[] { "" + date, "" + u.getId(), "" + day });
		if (cursor.moveToFirst()) {
            do {
    			String minutesAdjusted = cursor.getInt(5)+"";
            	
        		if(cursor.getInt(5) < 10)
        		{
        			minutesAdjusted = "0"+cursor.getInt(5);
        		}

            	MedSchedule result = new MedSchedule(cursor.getInt(0), // ID
            			cursor.getString(1), // Name
            			cursor.getString(2), // dosage
            			cursor.getShort(3), // day
            			"" + cursor.getInt(4) + ":" + minutesAdjusted, //hours, mins
            			""+date,
            			false); // taken?
    			//String row[] = { cursor.getString(0), ""+cursor.getFloat(1), "" + cursor.getString(2), "" + cursor.getInt(3), "" + cursor.getInt(4) }; 
    			//results.add(row);
    			results.add(result);
            } while (cursor.moveToNext());
		}
       cursor = db.rawQuery(query2, new String[] { "" + date, "" + u.getId(), ""+day });
		if (cursor.moveToFirst()) {
            do {
    			String minutesAdjusted = cursor.getInt(5)+"";
            	
        		if(cursor.getInt(5) < 10)
        		{
        			minutesAdjusted = "0"+cursor.getInt(5);
        		}

            	MedSchedule result = new MedSchedule(cursor.getInt(0), // ID
            			cursor.getString(1), // Name
            			cursor.getString(2), // dosage
            			cursor.getShort(3), // day
            			"" + cursor.getInt(4) + ":" + minutesAdjusted,  //hours, mins 
            			""+date,
            			true); // taken?
//    			String row[] = { cursor.getString(0), ""+cursor.getFloat(1), "" + cursor.getString(2), "" + cursor.getInt(3), "" + cursor.getInt(4) }; 
    			//results.add(row);
    			results.add(result);
            } while (cursor.moveToNext());
		}
		Collections.sort(results);
        return results;
		
	}
	
	public void store(MedicationEvent newEvent)
	{

		  SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put("time_hours", newEvent.getTime_hours());
		  values.put("time_mins", newEvent.getTime_mins());
		  values.put("dosage", newEvent.getDosage());
		  values.put("medication", newEvent.getMedication_id());
		  values.put("user", newEvent.getUserId());
		  values.put("day", newEvent.getDay());
		  db.insert("medication_schedule", null, values);
		  
	}

	public MedicationEvent getMedicationEvent(int _id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from medication_schedule where id = ?;";
		Cursor cursor = db.rawQuery(query, new String[] { "" + _id } );
		if (cursor.getCount() == 0)
		{
			return null;
		}
		else
		{
			cursor.moveToFirst();
			MedicationEvent medEvent = new MedicationEvent();
			medEvent.setId(cursor.getInt(0));
			medEvent.setTime_hours(cursor.getInt(1));
			medEvent.setTime_mins(cursor.getInt(2));
			medEvent.setDay(cursor.getShort(3));
			medEvent.setDosage(cursor.getFloat(4));
			medEvent.setMedication_id(cursor.getInt(5));
			medEvent.setUserId(cursor.getInt(6));
			return medEvent;
		}	
	}
	public ArrayList<MedicationEvent> getMedicationEvents(int _userId)
	{
		ArrayList<MedicationEvent> results = new ArrayList<MedicationEvent>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from medication_schedule where user = ?;";
		Cursor cursor = db.rawQuery(query, new String[] { "" + _userId });
				
        if (cursor.moveToFirst()) {
            do {
    			MedicationEvent medEvent = new MedicationEvent();
    			medEvent.setId(cursor.getInt(0));
    			medEvent.setTime_hours(cursor.getInt(1));
    			medEvent.setTime_mins(cursor.getInt(2));
    			medEvent.setDay(cursor.getShort(3));
    			medEvent.setDosage(cursor.getFloat(4));
    			medEvent.setMedication_id(cursor.getInt(5));
    			medEvent.setUserId(cursor.getInt(6));
                results.add(medEvent);
            } while (cursor.moveToNext());
        }
		return results;
	}
	public void updateMedicationEvent(MedicationEvent existingEvent)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    ///"create table medication_schedule(id INTEGER PRIMARY KEY AUTOINCREMENT, time_hours INTEGER,  time_mins INTEGER, day TEXT, dosage REAL, medication INTEGER, user INTEGER, FOREIGN KEY(medication) REFERENCES medication(id), FOREIGN KEY(user) REFERENCES user(id));\n" +
	    ContentValues values = new ContentValues();
		values.put("time_hours", existingEvent.getTime_hours());
		values.put("time_mins", existingEvent.getTime_mins());
		values.put("dosage", existingEvent.getDosage());
		values.put("medication", existingEvent.getMedication_id());
		values.put("user", existingEvent.getUserId());
		values.put("day", existingEvent.getDay());
		db.update("medication_schedule", values, "id = ?", new String[] { ""+existingEvent.getId() });
	}
	public void delete(MedicationEvent target)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("medication_tracking", "medication_schedule_id = ?", new String[] { "" + target.getId() } );
		db.delete("medication_schedule", "id = ?", new String[] { ""+target.getId() });
	}
	public void store(Medication newMedication)
	{
		  SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put("name", newMedication.getName());
		  values.put("priority", newMedication.getPriority());
		  long id = db.insert("medication", null, values);
		  newMedication.setId((int)id);
	}
	public Medication findMedication(String medName)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "select * from medication where name = ?;";
		Cursor cursor = db.rawQuery(query, new String[]{ medName } );
		if (cursor.getCount() == 0)
		{
			return null;
		}
		else
		{
			cursor.moveToFirst();
			Medication med = new Medication();
			med.setId(cursor.getInt(0));
			med.setName(cursor.getString(1));
			med.setPriority(cursor.getInt(2));
			return med;
		}
		
	}
	public void update(Medication existingMedication)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
		values.put("name", existingMedication.getName());
		values.put("priority", existingMedication.getPriority());
		db.update("user", values, "id = ?", new String[] { ""+existingMedication.getId() });		
	}
	public Medication getMedication(int medicationId)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from medication where id = ?;";
		Cursor cursor = db.rawQuery(query, new String[] { "" + medicationId } );
		if (cursor.getCount() == 0)
		{
			return null;
		}
		else
		{
			cursor.moveToFirst();
			Medication med = getMedication(cursor);
			return med;
		}
	}
	private Medication getMedication(Cursor cursor)
	{
		Medication med = new Medication();
		med.setId(cursor.getInt(0));
		med.setName(cursor.getString(1));
		med.setPriority(cursor.getInt(2));
		return med;		
	}
	public ArrayList<Medication> getMedications()
	{
		ArrayList<Medication> results = new ArrayList<Medication>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from medication;";
		Cursor cursor = db.rawQuery(query, null);
		Log.w("PHMS", ""+cursor.getCount());
		Log.w("PHMS", "" + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
    			Medication med = new Medication();
    			med.setId(cursor.getInt(0));
    			med.setName(cursor.getString(1));
    			med.setPriority(cursor.getInt(2));
                results.add(med);
            } while (cursor.moveToNext());
        }
		return results;
	}
	public void deleteMedication(Medication target)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("medication", "id = ?", new String[] { ""+target.getId() });
	}
	public void store(User newUser, SQLiteDatabase db)
	{
		  ContentValues values = new ContentValues();
		  values.put("username", newUser.getUserName());
		  values.put("password", newUser.getPasswordHash());
		  values.put("first_name", newUser.getFirstName());
		  values.put("last_name", newUser.getLastName());
		  values.put("gender", ""+newUser.getGender());
		  values.put("height_feet", newUser.getHeight_feet());
		  values.put("height_inches", newUser.getHeight_inches());
		  values.put("weight", newUser.getWeight());
		  values.put("age", newUser.getAge());
		  long id = db.insert("user", null, values);
		  newUser.setId((int)id);
		  
	}
	public void store(MedicationEvent newEvent, SQLiteDatabase db)
	{
		//"create table medication_schedule(id INTEGER PRIMARY KEY AUTOINCREMENT, time_hours INTEGER,  time_mins INTEGER, day TEXT, dosage REAL, medication INTEGER, user INTEGER, FOREIGN KEY(medication) REFERENCES medication(id), FOREIGN KEY(user) REFERENCES user(id));\n" +
		  ContentValues values = new ContentValues();
		  values.put("time_hours", newEvent.getTime_hours());
		  values.put("time_mins", newEvent.getTime_mins());
		  values.put("day", newEvent.getDay());
		  values.put("dosage", newEvent.getDosage());
		  values.put("medication", newEvent.getMedication_id());
		  values.put("user", newEvent.getUserId());
		  db.insert("medication_schedule", null, values);
		  //db.close();
	}
	public void store(Medication newMedication, SQLiteDatabase db)
	{
		  ContentValues values = new ContentValues();
		  values.put("name", newMedication.getName());
		  values.put("priority", newMedication.getPriority());
		  db.insert("medication", null, values);
		  //db.close();
	}
	public void store(User newUser)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		this.store(newUser, db);
		//db.close();
	}
	public boolean checkLoginInfo(String uname, String pass)
	{
		Log.w("PHMS", "" + (login(uname, pass) == null));
		return login(uname, pass) != null;
	}
	public User getUser(int id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from user where id = ?;";
		//Helper.
		Cursor cursor = db.rawQuery(query, new String[] { "" + id });
		if (cursor.moveToFirst())
		{
			User userObj = getUser(cursor);
			return userObj;
		}
		return null;
	}
	public User login(String uname, String pass)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from user where username = ? and password = ?;";
		//Helper.
		Cursor cursor = db.rawQuery(query, new String[] { uname, Helper.hashPassword(pass) });
		if (cursor.moveToFirst())
		{
			User userObj = new User();
            userObj.setId(cursor.getInt(0));
            userObj.setUserName(cursor.getString(1));
            userObj.setPassword(cursor.getString(2));
            userObj.setFirstName(cursor.getString(3));
            userObj.setLastName(cursor.getString(4));
            userObj.setHeight_feet(cursor.getInt(5));
            userObj.setHeight_inches(cursor.getInt(6));
            userObj.setWeight(cursor.getFloat(7));
            userObj.setAge(cursor.getInt(8));
            return userObj;
		}
		else
		{
			return null;
		}
	}
	public boolean usernameAvailable(String uname)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from user where username = ?;";
		Cursor cursor = db.rawQuery(query, new String[] { uname });
		if (cursor.moveToFirst())
			return false;
		else
			return true;
		
	}
	private User getUser(Cursor cursor)
	{
		User userObj = new User();
        userObj.setId(cursor.getInt(0));
        userObj.setUserName(cursor.getString(1));
        userObj.setPassword(cursor.getString(2));
        userObj.setFirstName(cursor.getString(3));
        userObj.setLastName(cursor.getString(4));
        userObj.setGender(cursor.getString(5));
        userObj.setHeight_feet(cursor.getInt(6));
        userObj.setHeight_inches(cursor.getInt(7));
        userObj.setWeight(cursor.getFloat(8));
        userObj.setAge(cursor.getInt(9));
        return userObj;
	}
	public ArrayList<User> getUsers()
	{
		ArrayList<User> results = new ArrayList<User>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from user;";
		Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User contact = getUser(cursor);
                results.add(contact);
            } while (cursor.moveToNext());
        }
		return results;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onConfigure(SQLiteDatabase db)
	{
		db.setForeignKeyConstraintsEnabled(true);
	}
	
	public void updateUser(User target)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
		values.put("username", target.getUserName());
		values.put("password", target.getPasswordHash());
		values.put("first_name", target.getFirstName());
		values.put("last_name", target.getLastName());
		values.put("height_feet", target.getHeight_feet());
		values.put("height_inches", target.getHeight_inches());
		values.put("weight", target.getWeight());
		values.put("age", target.getAge());
		db.update("user", values, "id = ?", new String[] { ""+target.getId() });
	}
	
	public void deleteUser(User target)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("user", "id = ?", new String[] { ""+target.getId() });
	}
	
	
	/*
	 * Add article
	 * */
	public void store(Article article) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put("type", article.getType()); 
	    values.put("url", article.getUrl()); 
	    values.put("title", article.getTitle()); 
	    values.put("description", article.getDescription()); 
	    values.put("user", article.getUserId()); 
	    
	    db.insert("article", null, values);
	    //db.close(); 
	}
	
	/*
	 * Get single article entry from Id
	 * */
	public Article getArticle(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    Cursor cursor = db.query("article", new String[] {"id", "type",
	            "url", "title", "description","user" }, "id =?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    Article article = new Article(Integer.parseInt(cursor.getString(0)),
	            cursor.getString(1), cursor.getString(2),cursor.getString(3),
	            cursor.getString(4),Integer.parseInt(cursor.getString(5)));
	    
	    
	    	    
	    return article;
	}
	public ArrayList<Article> getUserArticle(int uid) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    ArrayList<Article> results = new ArrayList<Article>();
	    
	    Cursor cursor = db.query("article", new String[] {"id", "type",
	            "url", "title", "description","user" }, "user = ?",
	            new String[] { String.valueOf(uid) }, null, null, null, null);
	    if (cursor.moveToFirst())
	    {
	    	do
	    	{
	    		Article a = new Article();
	    		a.setId(cursor.getInt(0));
	    		a.setType(cursor.getString(1));
	    		a.setUrl(cursor.getString(2));
	    		a.setTitle(cursor.getString(3));
	    		a.setDescription(cursor.getString(4));
	    		a.setUserId(cursor.getInt(5));
	    		results.add(a);
	    	} while (cursor.moveToNext());
	    }
	 
	    return results;
	}
	
	/*
	 * Add food
	 * */
	public void store(Food food) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put("name", food.getName()); 
	    values.put("calories", food.getCalories()); 
	    values.put("fat", food.getFat()); 
	    values.put("protein", food.getProtein()); 
	    values.put("carb", food.getCarbs()); 
	    values.put("fiber", food.getFiber()); 
	    values.put("sugar", food.getSugar()); 
	    
	    db.insert("food", null, values);
	    //db.close(); 
	}
	public int getFoodCount()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery("select * from food2", null);
		return result.getCount();
	}
	public Food2 getFood(int id)
	{

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Cursor cur = db.rawQuery("select * from food2 where id = ?", new String[] { "" + id });
		if (cur.moveToFirst())
		{
			Food2 result = new Food2();
			result.setId(cur.getInt(0));
			result.setCalories(cur.getFloat(1));
			result.setProtein(cur.getFloat(2));
			result.setLipid(cur.getFloat(3));
			result.setCarbs(cur.getFloat(4));
			result.setFiber(cur.getFloat(5));
			result.setSugar(cur.getFloat(6));
			result.setPotassium(cur.getFloat(7));
			result.setSodium(cur.getFloat(8));
			result.setFat_sat(cur.getFloat(9));
			result.setFat_mono(cur.getFloat(10));
			result.setFat_poly(cur.getFloat(11));
			result.setCholesterol(cur.getFloat(12));
			result.setWeight_serving1(cur.getFloat(13));
			result.setDesc_serving1(cur.getString(14));
			result.setWeight_serving2(cur.getFloat(15));
			result.setDesc_serving2(cur.getString(16));
			result.setDescription(cur.getString(17));
			return result;
		}
		else
		{
			return null;
		}
		
	}
	
	/*
	 * Add food_journal
	 * */
	public void store(FoodJournal food_j) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    //"create table food_journal(id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, user INTEGER, food INTEGER, datetime INTEGER, FOREIGN KEY(user) REFERENCES user(id), FOREIGN KEY(food) references food(id));\n" +
	    ContentValues values = new ContentValues();
	    values.put("amount", food_j.getAmount());
	    values.put("datetime", food_j.getDatetime());
	    values.put("user", food_j.getUserId()); 
	    values.put("food", food_j.getFoodId());  
	    
	    db.insert("food_journal", null, values);
	    //db.close(); 
	}
	
	public void store(Contact c)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_id", c.getUser_id());
		values.put("phone", c.getPhone());
		values.put("name", c.getName());
		values.put("email", c.getEmail());
		long id = db.insert("contacts", null, values);
		c.setId((int)id);
	}
	public Contact getContact(int id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from contacts where id = ?";
		Cursor cursor = db.rawQuery(query, new String[] { ""+id });
		Contact result = new Contact();
		if (cursor.moveToFirst())
		{
		result.setId(cursor.getInt(0));
		result.setUser_id(cursor.getInt(1));
		result.setName(cursor.getString(2));
		result.setPhone(cursor.getString(3));
		result.setEmail(cursor.getString(4));
		return result;
		}
		else
			return null;
	}
	public ArrayList<Contact> getContacts(int userid)
	{
		return null;
	}
	public void store(Session s)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_id", s.getUser_id());
		values.put("timestamp", s.getTimestamp());
		db.insert("sessions", null, values);
	}
	public Session getSession(int id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from sessions where id = ?";
		Cursor cursor = db.rawQuery(query, new String[] { ""+id });
		if (cursor.moveToFirst())
		{
			Session result = new Session();
			result.setSession_id(cursor.getInt(0));
			result.setUser_id(cursor.getInt(1));
			result.setTimestamp(cursor.getLong(2));
			return result;
		}
		else
		{
			return null;
		}
	}
	public ArrayList<Session> userSessions(int userId)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from sessions where user_id = ?";
		Cursor cursor = db.rawQuery(query, new String[] { ""+ userId });
		ArrayList<Session> results = new ArrayList<Session>();
        if (cursor.moveToFirst()) {
            do {
            	Session result = new Session();
        		result.setSession_id(cursor.getInt(0));
        		result.setUser_id(cursor.getInt(1));
        		result.setTimestamp(cursor.getLong(2));
        		
    			results.add(result);
            } while (cursor.moveToNext());
        }

        return results;
	}
	/*
	 * Get single food_journal entry from Id
	 * */
	public FoodJournal getFood_journal(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    //"create table food_journal(id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, user INTEGER, food INTEGER, datetime INTEGER, FOREIGN KEY(user) REFERENCES user(id), FOREIGN KEY(food) references food(id));\n" +
	    Cursor cursor = db.query("food_journal", new String[] {"id", "amount", "user",
	            "food", "datetime"}, "id = ?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    
	    if (cursor != null)
	        cursor.moveToFirst();
	    
	    FoodJournal food_j = new FoodJournal();
	    food_j.setId(cursor.getInt(0));
	    food_j.setAmount(cursor.getFloat(1));

	    food_j.setUserId(cursor.getInt(2));
	    food_j.setFoodId(cursor.getInt(3));
	    food_j.setDatetime(cursor.getInt(4));
	   // FoodJournal food_j = new FoodJournal(Integer.parseInt(cursor.getString(0)),
	    //		Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)));
	    
	    
	    return food_j;
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * Add vitalSign
	 * */
	
	
	public void store(VitalSign vt) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put("type", vt.getType()); 
	    values.put("value1", vt.getValue1());  
	    values.put("value2", vt.getValue2()); 
	    values.put("user", vt.getUser_Id());  
	    values.put("datetime", vt.getDatetime());
	    db.insert("vitalsign", null, values);
	    //db.close(); 
	}
	
	/*
	 * Get single vital sign entry from Id
	 * */
	public VitalSign getVitalSign(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    
	    //Cursor c = db.rawQuery("select * from article where userId= ?", new String[]{String.valueOf(id)});
	 
	    Cursor cursor = db.query("vitalsign", new String[] {"id", "type",
	            "value1","value2","user"}, "user = ?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	    VitalSign vt = new VitalSign();
	    vt.setId(cursor.getInt(0));
	    vt.setType(cursor.getInt(1));
	    vt.setValue1(cursor.getInt(2));
	    vt.setValue2(cursor.getInt(3));
	    vt.setDatetime(cursor.getLong(4));
	    vt.setUser_Id(cursor.getInt(5));
	    
	    return vt;
	}
	
	public ArrayList<VitalSign> getVitalSigns(int userId)
	{
		
		ArrayList<VitalSign> results = new ArrayList<VitalSign>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from vitalsign where user = ?;";
		Cursor cursor = db.rawQuery(query, new String[] { "" + userId });
				
        if (cursor.moveToFirst()) {
            do {
    			VitalSign v = new VitalSign();
    			v.setId(cursor.getInt(0));
    			v.setType(cursor.getInt(1));
    			v.setValue1(cursor.getInt(2));
    			v.setValue2(cursor.getInt(3));
    		    v.setDatetime(cursor.getLong(4));
    		    v.setUser_Id(cursor.getInt(5));

    			results.add(v);
            } while (cursor.moveToNext());
        }
		return results;	
	}
	public void update(VitalSign sign)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put("type", sign.getType()); 
	    values.put("value1", sign.getValue1());  
	    values.put("value2", sign.getValue2()); 
	    values.put("user", sign.getUser_Id());  
	    values.put("datetime", sign.getDatetime());

	    db.update("vitalsign", values, "id = ?", new String[] { ""+sign.getId() });		
	}
	
	
		
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String[] tables = { "user", "vitalsign", "article", "food", "food_journal", "medication", "medication_schedule", "medication_tracking", "contacts", "sessions", "schedule" };
		for (String t : tables)
		{
			db.execSQL("DROP TABLE IF EXISTS " + t);
		}
		onCreate(db);

	}
	public void delete(Article a)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("article", "id = ?", new String[] { ""+a.getId() });
	}
	
	public void update(Article a) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put("type", a.getType());
	    values.put("url", a.getUrl());
	    values.put("title", a.getTitle());
	    values.put("description", a.getDescription());
	    values.put("user", a.getUserId());
		db.update("article", values, "id = ?", new String[] { ""+a.getId() });		
	}

	public ArrayList<VitalSign> getUserVitals(int userid) {
		ArrayList<VitalSign> results = new ArrayList<VitalSign>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from vitalsign where user = ?;";
		Cursor cursor = db.rawQuery(query, new String[] { "" + userid });
				
        if (cursor.moveToFirst()) {
            do {
        	    VitalSign vt = new VitalSign();
        	    vt.setId(cursor.getInt(0));
        	    vt.setType(cursor.getInt(1));
        	    vt.setValue1(cursor.getInt(2));
        	    vt.setValue2(cursor.getInt(3));
        	    vt.setDatetime(cursor.getLong(4));
        	    vt.setUser_Id(cursor.getInt(5));
        	   results.add(vt);
            } while (cursor.moveToNext());
        }
        return results;
	}
	public Cursor getFoodCursor(String args) {
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "select id as _id, description from food_search where description match ? UNION select id as _id, description from food_search where description like ? LIMIT 10";
		Cursor result = db.rawQuery(query, new String[] { args,  args + "%" });
		return result;
	}
	
	public ArrayList<VitalSign> getUserVitals(int userid, int type, int offset) {
		ArrayList<VitalSign> results = new ArrayList<VitalSign>();
		SQLiteDatabase db = this.getWritableDatabase();
		long now = System.currentTimeMillis();
		long day = 1000*60*60*24;
		long d1 = now + day*(offset - 1);
		long d2 = now + day*(offset);
		//String dateSection = String.format("date('now', '%d days') and date('now','%d days')", offset, offset+1); 
		String query = "SELECT * from vitalsign where user = ? and type = ? and datetime between " + d1 + " and " + d2;
		Cursor cursor = db.rawQuery(query, new String[] { "" + userid, ""+type });
				
        if (cursor.moveToFirst()) {
            do {
        	    VitalSign vt = new VitalSign();
        	    vt.setId(cursor.getInt(0));
        	    vt.setType(cursor.getInt(1));
        	    vt.setValue1(cursor.getInt(2));
        	    vt.setValue2(cursor.getInt(3));
        	    vt.setDatetime(cursor.getLong(4));
        	    vt.setUser_Id(cursor.getInt(5));
        	   results.add(vt);
            } while (cursor.moveToNext());
        }
        return results;
	}
	
	public static boolean importFoodDatabase(SQLiteDatabase db, InputStream infile, int num)
	{
		Scanner scan = new Scanner(infile);
		String line = "";
		String split[];
		String columns[] = "id,calories,protein,lipid,carbs,fiber,sugar,potassium,sodium,fat_sat,fat_mono,fat_poly,cholesterol,weight_serving1,desc_serving1,weight_serving2,desc_serving2,description".split(",");
		//db.beginTransaction();
		scan.nextLine();
		int cnt = 0;
		while (scan.hasNextLine() && cnt++ < num) {
			line = scan.nextLine();
			split = line.split("\t");
			//Log.w("PHMS", "Description = " + split[split.length-1]);
			ContentValues values = new ContentValues();
			for (int i = 0; i < columns.length; i++)
			{
				values.put(columns[i], split[i].replace("\"", ""));
			}
			db.insert("food2", null, values);
			ContentValues values2 = new ContentValues();
			values2.put("id", split[0]);
			values2.put("description", Helper.toDisplayCase(split[split.length-1]));
			db.insert("food_search", null, values2);
		}
		//db.endTransaction();
		scan.close();
		return true;
	}

	public ArrayList<FoodJournal> getUserFoods(int userid) {
			ArrayList<FoodJournal> results = new ArrayList<FoodJournal>();
			SQLiteDatabase db = this.getWritableDatabase();
			String query = "SELECT * from food_journal where user = ?;";
			Cursor cursor = db.rawQuery(query, new String[] { "" + userid });
					
	        if (cursor.moveToFirst()) {
	            do {
	        	    FoodJournal food_j = new FoodJournal();
	        	    food_j.setId(cursor.getInt(0));
	        	    food_j.setAmount(cursor.getFloat(1));
	        	    food_j.setUserId(cursor.getInt(2));
	        	    food_j.setFoodId(cursor.getInt(3));
	        	    food_j.setDatetime(cursor.getInt(4));
	        	   results.add(food_j);
	            } while (cursor.moveToNext());
	        }
	        return results;
		}
	
}

