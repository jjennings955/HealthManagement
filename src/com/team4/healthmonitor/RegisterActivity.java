package com.team4.healthmonitor;


import android.app.Activity;
import android.content.Intent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.team4.database.Contact;
import com.team4.database.DatabaseHandler;
import com.team4.database.User;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
     private EditText newUser = null;
	 private EditText newPassword = null;
	 private EditText verifyPassword = null;
	 private EditText fName = null;
	 private EditText lName = null;
	 private String gender = "X";
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
		gender = "X";
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
	
		DatabaseHandler db = new DatabaseHandler(this);
		User user = new User();
		String userName = newUser.getText().toString();
		String password = newPassword.getText().toString();
		String password2 = verifyPassword.getText().toString();
		String firstName = fName.getText().toString();
		String lastName = lName.getText().toString();
		char userGender = gender.charAt(0);
		String doctorName = doc_name.getText().toString();
		String doctorEmail = doc_email.getText().toString();
		String doctorPhone = doc_phone.getText().toString();
		
		String contactName = relative_name.getText().toString();
		String contactEmail = relative_email.getText().toString();
		String contactPhone = relative_phone.getText().toString();
		
		
		
		
		
		
		
		int userAge = 0;
		if (!age.getText().toString().equals(""))
			userAge = Integer.parseInt(age.getText().toString());
		float userWeight = 0.0f;
		if (!weight.getText().toString().equals(""))
			userWeight = Float.parseFloat(weight.getText().toString());
		int hf = -1, hi = -1;
		
		if (!height_feet.getText().toString().equals(""))
			hf = Integer.parseInt(height_feet.getText().toString());
		if (!height_inch.getText().toString().equals(""))
			hi = Integer.parseInt(height_inch.getText().toString());
		
		boolean valid = true;
		
		if (!User.validateUserName(userName))
		{
			valid = false;
			newUser.setError("Invalid username");
		}
		if (!db.usernameAvailable(userName))
		{
			valid = false;
			newUser.setError("Username already exists");
		}
		if (password.equals(password2))
		{
			if (!User.validatePassword(password))
			{
				valid = false;
				newPassword.setError("Invalid password");
			}
		}
		else
		{
			valid = false;
			verifyPassword.setError("Passwords don't match");
		}
		
		if (!User.validFirstName(firstName))
		{
			valid = false;
			fName.setError("Invalid first name");
		}
		if (!User.validLastName(lastName))
		{
			valid = false;
			lName.setError("Invalid last name");
		}
		if (!User.validateGender(userGender))
		{
			valid = false;
			RadioButton foo = (RadioButton)findViewById(R.id.radio_male);
			foo.setError("Please choose a gender");
		}
		if (!User.validateAge(userAge))
		{
			valid = false;
			age.setError("Invalid age");
		}
		if (!User.validateWeight(userWeight))
		{
			valid = false;
			weight.setError("Invalid weight");
		}
		if (!User.validHeightFeet(hf))
		{
			valid = false;
			height_feet.setError("Invalid Height");
		}
		if (!User.validHeightInch(hi))
		{
			valid = false;
			height_inch.setError("Invalid Height");
		}
		if (!Contact.validateEmail(doctorEmail))
		{
			valid = false;
			doc_email.setError("Invalid email address");
		}
		if (!Contact.validatePhone(doctorPhone))
		{
			valid = false;
			doc_phone.setError("Invalid Phone Number");
		}
		if (!Contact.validateName(doctorName))
		{
			valid = false;
			doc_name.setError("Invalid Name");
		}
		if (!Contact.validateEmail(contactEmail))
		{
			valid = false;
			relative_email.setError("Invalid email address");
		}
		if (!Contact.validatePhone(contactPhone))
		{
			valid = false;
			relative_phone.setError("Invalid Phone Number");
		}
		if (!Contact.validateName(contactName))
		{
			valid = false;
			relative_name.setError("Invalid email address");
		}
		
		if(valid)
		{
		
			  user.setUserName(userName);
			  user.setPassword(password);
			  user.setFirstName(firstName);
			  user.setLastName(lastName);
			  user.setGender(userGender);
			  user.setAge(userAge);
			  user.setWeight(userWeight);
			  user.setHeight_feet(hf);
			  user.setHeight_inches(hi);
			  db.store(user);
			  Contact doctor = new Contact(user, doctorName, doctorPhone, doctorEmail);
			  Contact relative = new Contact(user, contactName, contactPhone, contactEmail);
			  db.store(doctor);
			  db.store(relative);

			  finish();
			  Intent i = new Intent(this, MainActivity.class);
		      startActivity(i);
			 
		}

	  

	  //returns user to login screen

	 
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
	    RadioButton male = (RadioButton)findViewById(R.id.radio_male);
	    // Check which radio button was clicked
	    switch(view.getId()) 
	    {
	        case R.id.radio_male:
	            if (checked)
	            {
	            	gender = "MALE";
	            	male.setError(null);
	            }
	            break;
	        case R.id.radio_female:
	            if (checked)
	            {
	            	gender = "FEMALE";
	            	male.setError(null);
	            }
	            break;
	    }
	}
	


}

