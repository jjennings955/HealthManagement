package com.example.healthmonitoring;

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
    private static final int DATABASE_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		//SQLiteDatabase foo = this.getWritableDatabase();
		String create_statement = "create table user(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, first_name TEXT, last_name TEXT, height_feet TINYINT, height_inches TINYINT, weight INTEGER, date_of_birth INTEGER);\n" +
				"create table vitalsign(id INTEGER PRIMARY KEY AUTOINCREMENT, type TINYINT, value1 INTEGER, value2 INTEGER, user INTEGER, FOREIGN KEY(user) REFERENCES user(id));\n" +
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
	}
	public void store(MedicationEvent newEvent)
	{

		  SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put("time_hours", newEvent.getTime_hours());
		  values.put("time_mins", newEvent.getTime_mins());
		  values.put("dosage", newEvent.getDosage());
		  values.put("medication_id", newEvent.getMedication_id());
		  values.put("user_id", newEvent.getUser_id());
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
			medEvent.set_id(cursor.getInt(0));
			medEvent.setTime_hours(cursor.getInt(1));
			medEvent.setTime_mins(cursor.getInt(2));
			medEvent.setDay(cursor.getString(3));
			medEvent.setDosage(cursor.getFloat(4));
			medEvent.setMedication_id(cursor.getInt(5));
			medEvent.setUser_id(cursor.getInt(6));
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
    			medEvent.set_id(cursor.getInt(0));
    			medEvent.setTime_hours(cursor.getInt(1));
    			medEvent.setTime_mins(cursor.getInt(2));
    			medEvent.setDay(cursor.getString(3));
    			medEvent.setDosage(cursor.getFloat(4));
    			medEvent.setMedication_id(cursor.getInt(5));
    			medEvent.setUser_id(cursor.getInt(6));
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
		values.put("medication_id", existingEvent.getMedication_id());
		values.put("user_id", existingEvent.getUser_id());
		values.put("day", existingEvent.getDay());
		db.update("user", values, "id = ?", new String[] { ""+existingEvent.get_id() });
	}
	public void delete(MedicationEvent target)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("medication_schedule", "id = ?", new String[] { ""+target.get_id() });
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
		db.update("user", values, "id = ?", new String[] { ""+existingMedication.get_id() });		
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
			med.set_id(cursor.getInt(0));
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
    			med.set_id(cursor.getInt(0));
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
		db.delete("medication", "id = ?", new String[] { ""+target.get_id() });
	}
	public void store(User newUser)
	{
		  SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put("username", newUser.getUserName());
		  values.put("password", newUser.getPasswordHash());
		  values.put("first_name", newUser.getFirstName());
		  values.put("last_name", newUser.getLastName());
		  values.put("height_feet", newUser.getHeight_feet());
		  values.put("height_inches", newUser.getHeight_inches());
		  values.put("weight", newUser.getWeight());
		  values.put("date_of_birth", newUser.getDate_of_birth().getTime());
		  db.insert("user", null, values);
		  db.close();
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
                contact.set_id(cursor.getInt(0));
                contact.setUserName(cursor.getString(1));
                contact.setPassword(cursor.getString(2));
                contact.setFirstName(cursor.getString(3));
                contact.setLastName(cursor.getString(4));
                contact.setHeight_feet(cursor.getInt(5));
                contact.setHeight_inches(cursor.getInt(6));
                contact.setWeight(cursor.getFloat(7));
                Date foo = new Date(cursor.getLong(8)/1000);
                Log.w("PHMS", "Date = " + foo);
                contact.setDate_of_birth(foo);
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
		values.put("date_of_birth", target.getDate_of_birth().getTime());
		db.update("user", values, "id = ?", new String[] { ""+target.get_id() });
	}
	
	public void deleteUser(User target)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("user", "id = ?", new String[] { ""+target.get_id() });
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
		
	}
}

