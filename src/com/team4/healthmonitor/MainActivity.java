package com.team4.healthmonitor;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.team4.database.*;

public class MainActivity extends Activity {
   
   public final static String USERNAME = "com.team4.healthmonitor.USERNAME";	

   public final static String PASSWORD = "com.team4.healthmonitor.PASSWORD";	
   public final static String USERID = "com.team4.healthmonitor.USERID";
   private EditText username=null;
   private EditText password=null;
   private TextView attempts;
   private Button login;
   int counter = 3;
   @Override 
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      DatabaseHandler db = new DatabaseHandler(this);

      username = (EditText)findViewById(R.id.main_edit_login);
      password = (EditText)findViewById(R.id.main_edit_password);
      attempts = (TextView)findViewById(R.id.main_attempts_text);
      attempts.setText(Integer.toString(counter));
      login = (Button)findViewById(R.id.main_login_btn);
   }

   public void login(View view)
   {
	  DatabaseHandler db = new DatabaseHandler(this);
	  String uname = username.getText().toString();
	  String pass = password.getText().toString();
	  User u = db.login(uname, pass);
	  if(u != null)
      {
	      Toast.makeText(getApplicationContext(), "Redirecting...",
	      Toast.LENGTH_SHORT).show();
	      Intent i = new Intent(this, MainAppActivity.class);
	      i.putExtra(USERNAME, uname);
	      i.putExtra(PASSWORD, pass);
	      i.putExtra(USERID, u.getId());
	      Session sess = Session.create_session(u);
	      db.store(sess);
	      
	      startActivity(i);
      }	
   else
   {

      Toast.makeText(getApplicationContext(), "Wrong Credentials",
      Toast.LENGTH_SHORT).show();
      attempts.setBackgroundColor(Color.RED);	
      counter--;
      attempts.setText(Integer.toString(counter));
      if(counter==0)
      {
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
