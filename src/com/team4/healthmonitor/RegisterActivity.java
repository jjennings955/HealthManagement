package com.team4.healthmonitor;


import android.app.Activity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.team4.database.DatabaseHandler;
import com.team4.database.User;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	 private EditText  newUser=null;
	 private EditText  newPassword=null;
	 private Pattern pattern;
	 private Matcher matcher;

	 private static final String PASSWORD_PATTERN = 
             "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		newUser=(EditText)findViewById(R.id.reg_username);
		newPassword=(EditText)findViewById(R.id.reg_password);
		

	}

	public void btnRegister (View view)
	{
	
	  if(newUser.getText().toString().matches(PASSWORD_PATTERN) )
	  {
		  Toast.makeText(getApplicationContext(), "Matched", 
			      Toast.LENGTH_SHORT).show();
	  }
	  
	  
	  User user = new User();
	  DatabaseHandler db = new DatabaseHandler(this);
	  user.setUserName(newUser.getText().toString());
	  user.setPassword(newPassword.getText().toString());
	  db.store(user);
	  
	  
	 
	 
	}
	
	public void addContact(View view)
	{
		
	}
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	


}

