package com.team4.healthmonitor;


import android.app.Activity;
import android.content.Intent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.team4.database.DatabaseHandler;
import com.team4.database.User;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
     private EditText newUser = null;
	 private EditText newPassword = null;
	 private EditText verifyPassword = null;
	 private EditText fName = null;
	 private EditText lName = null;
	 private String gender = "";
	 private EditText age = null;
	 private EditText weight = null;
	 private EditText height_feet = null;
	 private EditText height_inch = null;
	 private EditText doc_name = null;
	 private EditText doc_phone = null;
	 private EditText doc_email = null;
	 private EditText relative_name = null;
	 private EditText relative_phone = null;
	 private EditText relative_email = null;
	 
	 
	 
/*	 private Pattern pattern;
	 private Matcher matcher;

	 private static final String PASSWORD_PATTERN = 
             "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
*/
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	 {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		newUser=(EditText)findViewById(R.id.reg_username);
		newPassword=(EditText)findViewById(R.id.reg_password);
		verifyPassword = (EditText)findViewById(R.id.reg_verifyPassword);
		fName = (EditText)findViewById(R.id.first_name);
		lName = (EditText)findViewById(R.id.last_name);
		age = (EditText)findViewById(R.id.Age);
		weight = (EditText)findViewById(R.id.Weight);
		height_feet = (EditText)findViewById(R.id.Height);
		height_inch = (EditText)findViewById(R.id.Inches);
		doc_name = (EditText)findViewById(R.id.DoctorName);
		doc_phone = (EditText)findViewById(R.id.DoctorPhone);
		doc_email = (EditText)findViewById(R.id.DoctorEmail);
		relative_name = (EditText)findViewById(R.id.personalName);
		relative_phone = (EditText)findViewById(R.id.personalPhone);
		relative_email = (EditText)findViewById(R.id.personalEmail);
		
		
		

	}

	public void btnRegister (View view)
	{
	
	/*  if(newUser.getText().toString().matches(PASSWORD_PATTERN) )
	  {
		  Toast.makeText(getApplicationContext(), "Matched", 
			      Toast.LENGTH_SHORT).show();
	  }
	  */
		
		if(newPassword.getText().toString().equals(verifyPassword.getText().toString()))
		{
			  User user = new User();
			  DatabaseHandler db = new DatabaseHandler(this);
			  user.setUserName(newUser.getText().toString());
			  user.setPassword(newPassword.getText().toString());
			  user.setFirstName(fName.getText().toString());
			  user.setLastName(lName.getText().toString());
			  user.setGender(gender.charAt(0));
			  user.setAge(Integer.parseInt(age.getText().toString()));
			  user.setWeight(Float.parseFloat(weight.getText().toString()));
			  user.setHeight_feet(Integer.parseInt(height_feet.getText().toString()));
			  user.setHeight_inches(Integer.parseInt(height_inch.getText().toString()));
			  db.store(user);
			  

			  finish();
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Passwords did not match", Toast.LENGTH_SHORT).show();
		}

	  

	  //returns user to login screen
	  Intent i = new Intent(this, MainActivity.class);
      startActivity(i);
	 
	 
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

	public void onRadioButtonClicked(View view) 
	{
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) 
	    {
	        case R.id.radio_male:
	            if (checked)
	            {
	            	gender = "male";
	            }
	            break;
	        case R.id.radio_female:
	            if (checked)
	            {
	            	gender = "female";
	            }
	            break;
	    }
	}
	


}

