package com.team4.healthmonitor;


import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.team4.database.*;

public class MainActivity extends Activity {
 
   private EditText username=null;
   private EditText password=null;
   private TextView attempts;
   private Button login;
   int counter = 3;
   
   
   public static DatabaseHandler dh;
 	
   @Override 
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      username = (EditText)findViewById(R.id.editText1);
      password = (EditText)findViewById(R.id.editText2);
      attempts = (TextView)findViewById(R.id.textView5);
      attempts.setText(Integer.toString(counter));
      login = (Button)findViewById(R.id.button1);
      
      
      dh = new DatabaseHandler(this);
      
      int t = 4;
      
      User jason = new User("admin"+t++, "admin", "jason", "jennings", 6, 1, 200, 26, 'M');
		//dh.store(jason, dh.getWritableDatabase());
      Medication m = new Medication("Aspirin",6);
      Medication m1 = new Medication("Talanoy",4);
      Medication m2 = new Medication("dayquil",2);
      Medication m3 = new Medication("desagruin",0);
      dh.store(m);
      dh.store(m1);
      dh.store(m2);
      dh.store(m3);
      
      
      Log.d("Reading: ", "Reading all contacts.."); 
      List<User> u = dh.getUsers();       
       
      for (User cn : u) {
          String log = "Id: "+cn.getId()+ " ,userName: " + cn.getUserName() + " ,Name: " + cn.getFirstName() + " ,age: " + cn.getAge();
              // Writing Contacts to log
      Log.d("Name: ", log);}
      
      
      List<VitalSign> vt = dh.getVitalSigns();       
      
      for (VitalSign cn1 : vt) {
          String log1 = "Id: "+cn1.getId()+ " ,type: " + cn1.getType() + " ,value1: " + cn1.getValue2() + " " +
          		",value2: " + cn1.getValue2() + " ,user_id: " + cn1.getUser_Id();
              // Writing Contacts to log
      Log.d("Name: ", log1);}
      
      
   }

   public void login(View view){
	  DatabaseHandler db = new DatabaseHandler(this);
	  String uname = username.getText().toString();
	  String pass = password.getText().toString();
      if(db.checkLoginInfo(uname, pass)){
      Toast.makeText(getApplicationContext(), "Redirecting...",
      Toast.LENGTH_SHORT).show();
      Intent i = new Intent(this, MainAppActivity.class);
      startActivity(i);
   }	
   else{
      Toast.makeText(getApplicationContext(), "Wrong Credentials",
      Toast.LENGTH_SHORT).show();
      attempts.setBackgroundColor(Color.RED);	
      counter--;
      attempts.setText(Integer.toString(counter));
      if(counter==0){
         login.setEnabled(false);
      }

   }

}
   
   public void register(View view)
   {
	  Intent intent = new Intent(this, RegisterActivity.class);
	  startActivity(intent);
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
