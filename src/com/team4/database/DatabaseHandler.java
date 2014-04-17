package com.team4.database;



import java.util.ArrayList;
import java.util.Date;

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
    private static final int DATABASE_VERSION = 11;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		//SQLiteDatabase foo = this.getWritableDatabase();
		String[] tables = { "user", "vitalsign", "article", "food", "food_journal", "medication", "medication_schedule", "medication_tracking" };
		String create_statement = "" + 
				"create table user(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, first_name TEXT, last_name TEXT, height_feet TINYINT, height_inches TINYINT, weight INTEGER, age INTEGER, doctor_name TEXT, doctor_number TEXT, doctor_email TEXT, contact_name TEXT, contact_number TEXT, contact_email TEXT);\n" +
				"create table contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, name TEXT, phone TEXT, email TEXT, FOREIGN KEY(user_id) REFERENCES user(id) );\n" +
				"create table sessions(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, timestamp INTEGER, FOREIGN KEY(user_id) REFERENCES user(id));\n" +
				"create table vitalsign(id INTEGER PRIMARY KEY AUTOINCREMENT, type TINYINT, value1 INTEGER, value2 INTEGER, datetime INTEGER, user INTEGER, FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create table article(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, url TEXT, title TEXT, description TEXT, user INTEGER, FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create table food(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, calories REAL, fat REAL, protein REAL, carbs REAL, fiber REAL, sugar REAL);\n" +
				"create table food_journal(id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, user INTEGER, food INTEGER, FOREIGN KEY(user) REFERENCES user(id), FOREIGN KEY(food) references food(id));\n" +
				"create table medication(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, priority INTEGER);\n" +
				"create table medication_schedule(id INTEGER PRIMARY KEY AUTOINCREMENT, time_hours INTEGER,  time_mins INTEGER, day TEXT, dosage REAL, medication INTEGER, user INTEGER, FOREIGN KEY(medication) REFERENCES medication(id), FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create table medication_tracking(id INTEGER PRIMARY KEY AUTOINCREMENT, medication_schedule_id INTEGER, date INTEGER, FOREIGN KEY(medication_schedule_id) REFERENCES medication_schedule(id));";
		
		String statements[] = create_statement.split("\n");
		for (int i = 0; i < statements.length; i++)
		{
			Log.w("PHMS", statements[i]);
			db.execSQL(statements[i]);
		}
		User jason = new User("admin", "admin", "jason", "jennings", 6, 1, 200, 26, 'M');
		ArrayList<Medication> meds = new ArrayList<Medication>();
		
		meds.add(new Medication("Tylenol", 1));
		meds.add(new Medication("Viagra", 1));
		meds.add(new Medication("Aspirin", 1));
		meds.add(new Medication("Viox", 1));
		meds.add(new Medication("Heroin", 1));
		meds.add(new Medication("Caffeine", 1));
		//MedicationEvent mv = new MedicationEvent(5, 24, "wednesday", 5, 1, 1);
		
		
		for (Medication m : meds)
		{
			this.store(m, db);
		}
		
		this.store(jason, db);
		//this.store(mv, db);
		//mv.setMedication_id(2);
		//this.store(mv, db);
	}
	
	public ArrayList<String[]> getUserMedicationSchedule(User u)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<String[]> results = new ArrayList<String[]>();
		String query = "select M.name, S.dosage, S.day, S.time_hours, S.time_mins from medication_schedule as S, user as U, medication as M where S.user = U.id and U.id = M.id and U.id = ?";
		Cursor cursor = db.rawQuery(query, new String[] { "" + u.getId() } );
        if (cursor.moveToFirst()) {
            do {
    			String row[] = { cursor.getString(0), ""+cursor.getFloat(1), "" + cursor.getString(2), "" + cursor.getInt(3), "" + cursor.getInt(4) }; 
    			results.add(row);
            } while (cursor.moveToNext());
        }
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
			medEvent.setDay(cursor.getString(3));
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
    			medEvent.setDay(cursor.getString(3));
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
	    
	    ContentValues values = new ContentValues();
		values.put("time_hours", existingEvent.getTime_hours());
		values.put("time_mins", existingEvent.getTime_mins());
		values.put("dosage", existingEvent.getDosage());
		values.put("medication", existingEvent.getMedication_id());
		values.put("user", existingEvent.getUserId());
		values.put("day", existingEvent.getDay());
		db.update("user", values, "id = ?", new String[] { ""+existingEvent.getId() });
	}
	public void delete(MedicationEvent target)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("medication_schedule", "id = ?", new String[] { ""+target.getId() });
	}
	public void store(Medication newMedication)
	{
		  SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put("name", newMedication.getName());
		  values.put("priority", newMedication.getPriority());
		  db.insert("medication", null, values);
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
			Medication med = new Medication();
			med.setId(cursor.getInt(0));
			med.setName(cursor.getString(1));
			med.setPriority(cursor.getInt(2));
			return med;
		}
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
		  values.put("height_feet", newUser.getHeight_feet());
		  values.put("height_inches", newUser.getHeight_inches());
		  values.put("weight", newUser.getWeight());
		  values.put("age", newUser.getAge());
		  db.insert("user", null, values);
		  
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
	public User login(String uname, String pass)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from user where username = ? and password = ?;";
		//Helper.
		Cursor cursor = db.rawQuery(query, new String[] { uname, Helper.hashPassword(pass) });
		if (cursor.moveToFirst())
		{
			User contact = new User();
            contact.setId(cursor.getInt(0));
            contact.setUserName(cursor.getString(1));
            contact.setPassword(cursor.getString(2));
            contact.setFirstName(cursor.getString(3));
            contact.setLastName(cursor.getString(4));
            contact.setHeight_feet(cursor.getInt(5));
            contact.setHeight_inches(cursor.getInt(6));
            contact.setWeight(cursor.getFloat(7));
            contact.setAge(cursor.getInt(8));
            return contact;
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
	public ArrayList<User> getUsers()
	{
		ArrayList<User> results = new ArrayList<User>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from user;";
		Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User contact = new User();
                contact.setId(cursor.getInt(0));
                contact.setUserName(cursor.getString(1));
                contact.setPassword(cursor.getString(2));
                contact.setFirstName(cursor.getString(3));
                contact.setLastName(cursor.getString(4));
                contact.setHeight_feet(cursor.getInt(5));
                contact.setHeight_inches(cursor.getInt(6));
                contact.setWeight(cursor.getFloat(7));
                contact.setAge(cursor.getInt(8));
                
                //Date foo = new Date(cursor.getLong(8)/1000);
                //Log.w("PHMS", "Date = " + foo);
                //contact.setDate_of_birth(foo);
                //contact.setAge(cursor.get)
                
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
	    values.put("userId", article.getUserId()); 
	    
	    db.insert("article", null, values);
	    //db.close(); 
	}
	
	/*
	 * Get single article entry from Id
	 * */
	public Article getArticle(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    
	    //Cursor c = db.rawQuery("select * from article where userId= ?", new String[]{String.valueOf(id)});
	 
	    Cursor cursor = db.query("article", new String[] {"id", "type",
	            "url", "title", "description","userId" }, "userId =?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    Article article = new Article(Integer.parseInt(cursor.getString(0)),
	            cursor.getString(1), cursor.getString(2),cursor.getString(3),
	            cursor.getString(4),Integer.parseInt(cursor.getString(5)));
	    // return contact
	    
	    
	    
	    return article;
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
	
	/*
	 * Add food_journal
	 * */
	public void store(FoodJournal food_j) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put("userId", food_j.getUserId()); 
	    values.put("foodId", food_j.getFoodId());  
	    
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
		result.setId(cursor.getInt(0));
		result.setUser_id(cursor.getInt(1));
		result.setName(cursor.getString(2));
		result.setPhone(cursor.getString(3));
		result.setEmail(cursor.getString(4));
		return result;		
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
		Session result = new Session();
		result.setSession_id(cursor.getInt(0));
		result.setUser_id(cursor.getInt(1));
		result.setTimestamp(cursor.getLong(2));
		return result;
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
	    
	    Cursor cursor = db.query("food_journal", new String[] {"id", "userId",
	            "foodId"}, "id = ?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    
	    if (cursor != null)
	        cursor.moveToFirst();
	    
	    FoodJournal food_j = new FoodJournal(Integer.parseInt(cursor.getString(0)),
	    		Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)));
	    
	    
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
	    values.put("userId", vt.getUser_Id());  
	    
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
	            "value1","value2","userId"}, "userId =?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	    VitalSign vt = new VitalSign(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(0)),
	    		Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(0)),
	    		Integer.parseInt(cursor.getString(2)));
	       
	    
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
    			v.setUser_Id(cursor.getInt(4));

    			results.add(v);
            } while (cursor.moveToNext());
        }
		return results;	
	}
	
	
		
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String[] tables = { "user", "vitalsign", "article", "food", "food_journal", "medication", "medication_schedule", "medication_tracking", "contacts", "sessions" };
		for (String t : tables)
		{
			db.execSQL("DROP TABLE IF EXISTS " + t);
		}
		onCreate(db);
	}
}

