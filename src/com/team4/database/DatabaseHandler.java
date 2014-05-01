package com.team4.database;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
		String[] tables = { "user", "contacts", "sessions", "vitalsign", "article", "food_journal", "food_search", "medication", "medication_schedule", "medication_tracking" };
		String create_statement = "" + 
				"create table user(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, first_name TEXT, last_name TEXT, gender TEXT, height_feet TINYINT, height_inches TINYINT, weight INTEGER, age INTEGER, doctor_name TEXT, doctor_number TEXT, doctor_email TEXT, contact_name TEXT, contact_number TEXT, contact_email TEXT);\n" +
				"create table contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, name TEXT, phone TEXT, email TEXT, FOREIGN KEY(user_id) REFERENCES user(id) );\n" +
				"create table sessions(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, timestamp INTEGER, FOREIGN KEY(user_id) REFERENCES user(id));\n" +
				"create table vitalsign(id INTEGER PRIMARY KEY AUTOINCREMENT, type TINYINT, value1 INTEGER, value2 INTEGER, datetime INTEGER, user INTEGER, FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create table article(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, url TEXT, title TEXT, description TEXT, user INTEGER, FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create virtual table article_search using fts3(id, url, title, description, user);\n" +
				"create table food_journal(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, amount REAL, user INTEGER, datetime INTEGER, FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create virtual table food_search using fts3(id INTEGER, description TEXT, FOREIGN KEY(id) REFERENCES food_journal(id) ON DELETE CASCADE);\n" +
				"create table medication(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, priority INTEGER);\n" +
				"create table medication_schedule(id INTEGER PRIMARY KEY AUTOINCREMENT, time_hours INTEGER,  time_mins INTEGER, day TINYINT, dosage TEXT, medication INTEGER, user INTEGER, priority TEXT, FOREIGN KEY(medication) REFERENCES medication(id), FOREIGN KEY(user) REFERENCES user(id));\n" +
				"create table medication_tracking(medication_schedule_id INTEGER, date TEXT, FOREIGN KEY(medication_schedule_id) REFERENCES medication_schedule(id) ON DELETE CASCADE, primary key (medication_schedule_id, date));\n" +
				"create table medication_conflict(medication_one INTEGER, medication_two INTEGER, PRIMARY KEY(medication_one, medication_two));\n";
		
		String statements[] = create_statement.split("\n");
		for (int i = 0; i < statements.length; i++)
		{
			db.execSQL(statements[i]);
		}
		User jason = new User("admin", "admin", "jason", "jennings", 6, 1, 200, 26, 'M');
		
		store(jason, db);
		importMedications(db);
	}
	public boolean willConflict(int userId, int med)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "select count(*) from medication_conflict where medication_two in (select medication from medication_schedule where user = ?) and medication_one = ?";
		Cursor result = db.rawQuery(query, new String[] { ""+userId, ""+med });
		if (result.moveToFirst())
		{
			return result.getInt(0) > 0;
		}
			else
			{
				return false;
			}
	}
	private void makeConflict(SQLiteDatabase db, int id1, int id2)
	{
		ContentValues cv = new ContentValues();
		cv.put("medication_one", id1);
		cv.put("medication_two", id2);
		
		ContentValues cv2 = new ContentValues();
		cv.put("medication_one", id2);
		cv.put("medication_two", id1);
		
		db.insert("medication_conflict", null, cv);
		db.insert("medication_conflict", null, cv2);
	}
	
	private boolean checkConflict(int id1, int id2)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "select * from medication_conflict where (medication_one = ? and medication_two = ?) or (medication_one = ? and medication_two = ?);";
		Cursor result = db.rawQuery(query, new String[]{""+id1, ""+id2, ""+id2, ""+id1});
		return result.getCount() == 1;
	}
	
	/*
	 * Import some hard coded medications for the user to choose from.
	 */
	private void importMedications(SQLiteDatabase db)
	{
		ArrayList<Medication> meds = new ArrayList<Medication>();
		Medication tylenol = new Medication("Tylenol", 1);
		Medication aspirin = new Medication("Aspirin", 1);
		
		meds.add(tylenol);
		meds.add(new Medication("Viagra", 1));
		meds.add(aspirin);
		meds.add(new Medication("Viox", 1));
		meds.add(new Medication("Caffeine", 1));
		meds.add(new Medication("Zoloft", 1));
		meds.add(new Medication("Vicodin", 1));
		meds.add(new Medication("Amoxicillin", 1));
		meds.add(new Medication("Tamiflu", 1));
		meds.add(new Medication("Sudafed", 1));
		meds.add(new Medication("Benadryl", 1));
		meds.add(new Medication("Levothyroxine", 1));
		for (Medication med : meds)
		{
			this.store(med, db);
		}
		if (tylenol != null && aspirin != null)
		{
			try
			{
				this.makeConflict(db, tylenol.getId(), aspirin.getId());
				this.makeConflict(db, aspirin.getId(), tylenol.getId());
			} catch (SQLiteException e)
			{
				
			}
		}
	}
	/* Mark a medication as taken
	 * 
	 */
	public void medicationTaken(int id, String date)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("date", date);
		cv.put("medication_schedule_id", id);
		db.insert("medication_tracking", null, cv);
	}
	/* Mark a medication as not taken
	 * 
	 */
	public void medicationNotTaken(int id, String date)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete("medication_tracking", "date = ? and medication_schedule_id = ?", new String[] { "" + date, "" + id });
	}
	/* Get a user's medication schedule
	 * 
	 */
	public ArrayList<MedSchedule> getUserMedicationSchedule(User u, int day, String date)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<MedSchedule> results = new ArrayList<MedSchedule>();

		String query1 = "select S.id, M.name, S.dosage, S.day, S.time_hours, S.time_mins, S.priority from medication as M, medication_schedule as S where M.id = S.medication and not exists (select * from medication_tracking where medication_schedule_id = S.id and date = ?) and user = ? and day = ? order by S.time_hours, S.time_mins, M.name;";
		String query2 = "select S.id, M.name, S.dosage, S.day, S.time_hours, S.time_mins, S.priority from medication as M, medication_schedule as S where M.id = S.medication and exists (select * from medication_tracking where medication_schedule_id = S.id and date = ?) and user = ? and day = ? order by S.time_hours, S.time_mins, M.name;";
		
		Cursor cursor = db.rawQuery(query1, new String[] { "" + date, "" + u.getId(), "" + day });
		if (cursor.moveToFirst()) {
            do {
        		MedSchedule result = MedSchedule.getMedSchedule(cursor);
        		result.date = "" + date;
        		result.status = false;
    			results.add(result);
            } while (cursor.moveToNext());
		}
       cursor = db.rawQuery(query2, new String[] { "" + date, "" + u.getId(), ""+day });
		if (cursor.moveToFirst()) {
            do {
        		MedSchedule result = MedSchedule.getMedSchedule(cursor);
        		result.date = "" + date;
        		result.status = true;
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
		  values.put("priority", newEvent.getPriority());
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
			return MedicationEvent.getMedicationEvent(cursor);
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
    			MedicationEvent medEvent = MedicationEvent.getMedicationEvent(cursor);
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
			Medication med = Medication.getMedication(cursor);
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
			Medication med = Medication.getMedication(cursor);
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
    			Medication med = Medication.getMedication(cursor);
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
		  long id = db.insert("medication", null, values);
		  newMedication.setId((int)id);
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
			User userObj = User.getUser(cursor);
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
			User userObj = User.getUser(cursor);
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
		return cursor.getCount() == 0;
	}

	public ArrayList<User> getUsers()
	{
		ArrayList<User> results = new ArrayList<User>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from user;";
		Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User contact = User.getUser(cursor);
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
	    if (cursor.moveToFirst())
	    {
	 
		    Article article = Article.getArticle(cursor);
		    return article;
	    }
	    	    
	    return null;
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
	    		Article a = Article.getArticle(cursor);
	    		results.add(a);
	    	} while (cursor.moveToNext());
	    }
	 
	    return results;
	}
	

	
	
	/*
	 * Add food_journal
	 * */
	public void store(FoodJournal food_j) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    if (food_j.getId() != -1)
	    	values.put("id", food_j.getId());
	    
	    values.put("name", food_j.getName());
	    values.put("amount", food_j.getAmount());
	    values.put("datetime", food_j.getDatetime());
	    values.put("user", food_j.getUserId()); 
	    
	    
	    long id = db.insert("food_journal", null, values);
	    ContentValues indexValues = new ContentValues();
	    indexValues.put("id", id);
	    indexValues.put("description", food_j.getName());
	    db.insert("food_search", null, indexValues);
	}
	public void delete(FoodJournal food_j)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("food_journal", "id = ?", new String[] { ""+food_j.getId() } );
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

		if (cursor.moveToFirst())
		{
			Contact result = Contact.getContact(cursor);
			return result;
		}
		else
		{
			return null;
		}
	}
	public ArrayList<Integer> getContactsList(int userid)
	{
		ArrayList<Integer> contacts = new ArrayList<Integer>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT id FROM contacts WHERE user_id = ?";
		Cursor cursor = db.rawQuery(query, new String[] {userid+""});
        if (cursor.moveToFirst()) 
        {
            do 
            {
            	int result = cursor.getInt(0);
            	contacts.add(result);
            } 
            while (cursor.moveToNext());
        }
        return contacts;
		
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
	public Session currentUserSession(int userId)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from sessions WHERE user_id = ? ORDER BY timestamp DESC LIMIT 1";
		Cursor cursor = db.rawQuery(query, new String[] { ""+ userId });
		
        if (cursor.moveToFirst()) {
            	Session result = new Session();
        		result.setSession_id(cursor.getInt(0));
        		result.setUser_id(cursor.getInt(1));
        		result.setTimestamp(cursor.getLong(2));
        		return result;
            } 
        return null;
        

        
	}
	/*
	 * Get single food_journal entry from Id
	 * */
	public FoodJournal getFood_journal(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    String query = "select * from food_journal where id = ?";
	    Cursor cursor = db.rawQuery(query, new String[] { "" + id });
	    if (cursor.moveToFirst())
	    {
	    
		    FoodJournal food_j = FoodJournal.getFoodJournal(cursor);
		    return food_j;
	    }
	    return null;
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
	
	
public ArrayList<Article> getArticle_search(String keyword, int userId)
{
ArrayList<Article> results = new ArrayList<Article>();
SQLiteDatabase db = this.getWritableDatabase();
String query = "SELECT * FROM article natural join (SELECT id FROM article_search WHERE article_search MATCH ?) where user = ?";//article_search
Cursor cursor = db.rawQuery(query, new String[] { keyword + "*", ""+userId });
Log.w("PHMS", ""+cursor.getCount());
Log.w("PHMS", "" + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
             Article art = new Article();
     art.setId(cursor.getInt(0));
     art.setType(cursor.getString(1));
     art.setUrl(cursor.getString(2));
     art.setTitle(cursor.getString(3));
     art.setDescription(cursor.getString(4));
     art.setUserId(cursor.getInt(5));//id,type, url,title, description, user
                results.add(art);
            } while (cursor.moveToNext());
        }
return results;
}
	
	/*
	 * Get single vital sign entry from Id
	 * */
	public VitalSign getVitalSign(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    Cursor cursor = db.rawQuery("select * from vitalsign where id = ?", new String[] { "" + id });
	    
	    if (cursor.moveToFirst())
	    {
		    
		    return VitalSign.getVitalSign(cursor);
	    }
	    return null;
	}
	
	public ArrayList<VitalSign> getVitalSigns(int userId)
	{
		
		ArrayList<VitalSign> results = new ArrayList<VitalSign>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from vitalsign where user = ?;";
		Cursor cursor = db.rawQuery(query, new String[] { "" + userId });
				
        if (cursor.moveToFirst()) {
            do {
    			VitalSign v = VitalSign.getVitalSign(cursor);

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
	    Log.w("PHMS", values.toString());
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


	public ArrayList<VitalSign> getUserVitals(int userid, int type) {
		ArrayList<VitalSign> results = new ArrayList<VitalSign>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from vitalsign where user = ? and type = ?;";
		Cursor cursor = db.rawQuery(query, new String[] { "" + userid, ""+type });
				
        if (cursor.moveToFirst()) {
            do {
        	    VitalSign vt = VitalSign.getVitalSign(cursor);
        	   results.add(vt);
            } while (cursor.moveToNext());
        }
        return results;
	}
	

	public ArrayList<VitalSign> getUserVitals(int userid) {
		ArrayList<VitalSign> results = new ArrayList<VitalSign>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from vitalsign where user = ?;";
		Cursor cursor = db.rawQuery(query, new String[] { "" + userid });
				
        if (cursor.moveToFirst()) {
            do {
        	    VitalSign vt = VitalSign.getVitalSign(cursor);
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
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(now);
		cal.add(Calendar.DAY_OF_YEAR, offset-1);
		long d1 = cal.getTimeInMillis();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		long d2 = cal.getTimeInMillis();
		//long day = 1000*60*60*24;
		//long d1 = now + day*(offset - 1);
		//long d2 = now + day*(offset);
		//String dateSection = String.format("date('now', '%d days') and date('now','%d days')", offset, offset+1); 
		String query = "SELECT * from vitalsign where user = ? and type = ? and datetime between " + d1 + " and " + d2;
		Cursor cursor = db.rawQuery(query, new String[] { "" + userid, ""+type });
				
        if (cursor.moveToFirst()) {
            do {
        	    VitalSign vt = VitalSign.getVitalSign(cursor);
        	   results.add(vt);
            } while (cursor.moveToNext());
        }
        return results;
	}
	
	public ArrayList<FoodJournal> getUserFoods(int userId, int offset)
	{
		long now = System.currentTimeMillis();
		long day = 1000*60*60*24;
		long d1 = now + day*(offset - 1);
		long d2 = now + day*(offset);
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT * from food_journal where user = ? and datetime between " + d1 + " and " + d2;
		Cursor cursor = db.rawQuery(query, new String[] { "" + userId });
		ArrayList<FoodJournal> results = new ArrayList<FoodJournal>();
        if (cursor.moveToFirst()) {
            do {
        	    FoodJournal food_j = FoodJournal.getFoodJournal(cursor);
        	   results.add(food_j);
            } while (cursor.moveToNext());
        }
        return results;

	}
	public ArrayList<FoodJournal> getUserFoods(int userid) {
			ArrayList<FoodJournal> results = new ArrayList<FoodJournal>();
			SQLiteDatabase db = this.getWritableDatabase();
			String query = "SELECT * from food_journal where user = ?;";
			Cursor cursor = db.rawQuery(query, new String[] { "" + userid });
			
	        if (cursor.moveToFirst()) {
	            do {
	        	    FoodJournal food_j = FoodJournal.getFoodJournal(cursor);
	        	    results.add(food_j);
	            } while (cursor.moveToNext());
	        }
	        return results;
		}

	public void update(FoodJournal item) {
		SQLiteDatabase db = this.getWritableDatabase();
		delete(item);
		store(item);
		
	}
	
}

