package com.example.healthmonitoring;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
		String create_statement = "create table user(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, first_name TEXT, last_name TEXT, height_feet TINYINT, height_inches TINYINT, weight INTEGER, date_of_birth INTEGER); create table vitalsign(id INTEGER PRIMARY KEY AUTOINCREMENT, type TINYINT, value1 INTEGER, value2 INTEGER, user INTEGER, FOREIGN KEY(user) REFERENCES user(id)); create table article(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, url TEXT, title TEXT, description TEXT, user INTEGER, FOREIGN KEY(user) REFERENCES user(id)); create table food(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, calories REAL, fat REAL, protein REAL, carbs REAL, fiber REAL, sugar REAL); create table food_journal(id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, user INTEGER, food INTEGER, FOREIGN KEY(user) REFERENCES user(id), FOREIGN KEY(food) references food(id)); create table medication(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, priority INTEGER); create table medication_schedule(id INTEGER PRIMARY KEY AUTOINCREMENT, time INTEGER, day TEXT, dosage REAL, medication INTEGER, user INTEGER, FOREIGN KEY(medication) REFERENCES medication(id), FOREIGN KEY(user) REFERENCES user(id)); create table medication_tracking(id INTEGER PRIMARY KEY AUTOINCREMENT, medication_schedule_id INTEGER, date INTEGER, FOREIGN KEY(medication_schedule_id) REFERENCES medication_schedule(id));";
		
		db.execSQL(create_statement);
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
		  //values.put("date_of_birth", newUser.getDate_of_birth().getTime());
		  values.put("date_of_birth", 111111);
		  db.insert("user", null, values);
		  db.close();
	}
	public ArrayList<User> getUsers() throws UnsupportedEncodingException
	{
		ArrayList<User> results = new ArrayList<User>();
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * from user;";
		Cursor cursor = db.rawQuery(query, null);
		Log.w("PHMS", ""+cursor.getCount());
		Log.w("PHMS", "" + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
                User contact = new User();
                contact.setUserName(cursor.getString(1));
                contact.setPassword(cursor.getString(2));
                contact.setFirstName(cursor.getString(3));
                contact.setLastName(cursor.getString(4));
                contact.setHeight_feet(cursor.getInt(5));
                contact.setHeight_inches(cursor.getInt(6));
                contact.setWeight(cursor.getFloat(7));
                contact.setDate_of_birth(new Date(cursor.getInt(8)));
                results.add(contact);
            } while (cursor.moveToNext());
        }
		return results;
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}

