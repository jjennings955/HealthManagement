package com.team4.healthmonitor.dialogs;

import java.util.ArrayList;

import com.team4.database.Contact;
import com.team4.database.DatabaseHandler;
import com.team4.database.Medication;
import com.team4.database.MedicationEvent;
import com.team4.database.User;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.fragments.MedicineFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;


public class SettingsDialog extends DialogFragment implements OnClickListener 
{
	 private FragmentActivity myContext;
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
	 private Button update;
	 private RadioButton male;
	 private RadioButton female;
	 private User user;
	 private DatabaseHandler db;
	 private int id;
	 
	 public SettingsDialog()
	 {
		 
	 }
	 

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		 View view = inflater.inflate(R.layout.settings, container);
		 Bundle args = this.getArguments();
		 id = args.getInt(Arguments.USERID);
		 
		gender = "X";
		newUser=(EditText)view.findViewById(R.id.reg_username);
		newPassword=(EditText)view.findViewById(R.id.reg_password);
		verifyPassword = (EditText)view.findViewById(R.id.reg_verifyPassword);
		fName = (EditText)view.findViewById(R.id.first_name);
		lName = (EditText)view.findViewById(R.id.last_name);
		age = (EditText)view.findViewById(R.id.Age);
		weight = (EditText)view.findViewById(R.id.Weight);
		height_feet = (EditText)view.findViewById(R.id.Height);
		height_inch = (EditText)view.findViewById(R.id.Inches);
		doc_name = (EditText)view.findViewById(R.id.DoctorName);
		doc_phone = (EditText)view.findViewById(R.id.DoctorPhone);
		doc_email = (EditText)view.findViewById(R.id.DoctorEmail);
		relative_name = (EditText)view.findViewById(R.id.personalName);
		relative_phone = (EditText)view.findViewById(R.id.personalPhone);
		relative_email = (EditText)view.findViewById(R.id.personalEmail);
		update = (Button)view.findViewById(R.id.btnRegister);
		male = (RadioButton)view.findViewById(R.id.radio_male);
		female = (RadioButton)view.findViewById(R.id.radio_female);
		
		db = new DatabaseHandler(getActivity());
		
		user = db.getUser(id);
		
		if(user.getGender() == 'm' || user.getGender() == 'M')
		{
			male.setActivated(true);
		}
		else if(user.getGender() == 'f' || user.getGender() == 'F')
		{
			female.setActivated(true);
		}
		
		newUser.setText(user.getUserName()+"");
		fName.setText(user.getFirstName()+"");
		lName.setText(user.getLastName()+"");
		age.setText(user.getAge()+"");
		weight.setText(user.getWeight()+"");
		height_feet.setText(user.getHeight_feet()+"");
		height_inch.setText(user.getHeight_inches()+"");
		
		ArrayList<Integer> contactIDs = new ArrayList<Integer>();
		contactIDs = db.getContactsList(id);
		
		for(int z = 0; z < contactIDs.size(); z++)
		{
			Log.w("FORLOOP", ""+contactIDs.get(z));
		}
		
		if(contactIDs.size() == 2)
		{
			Log.w("CONTACTS.GET(0)", contactIDs.get(0)+"");
			Log.w("SECOND TEST", db.getContact(2).getName()+"");
			
			Contact one = db.getContact(contactIDs.get(0));
			Contact two = db.getContact(contactIDs.get(1));
			
			doc_name.setText(one.getName()+"");
			doc_phone.setText(one.getPhone()+"");
			doc_email.setText(one.getEmail()+"");
			relative_name.setText(two.getName()+"");
			relative_phone.setText(two.getPhone()+"");
			relative_email.setText(two.getEmail()+"");
		}
		
		 
		 getDialog().setTitle("Edit Registration");
		 
		 
		 update.setOnClickListener(this);
		 return view;
	 }
		@Override
		public void onAttach(Activity activity)
		{
		    myContext=(FragmentActivity) activity;
		    super.onAttach(activity);
		}
		@Override
		public void onClick(View arg0) {

			if (arg0.getId() == R.id.medEditBtn2)
			{
				handleUpdate();
			}
		}
		
		public void handleUpdate()
		{
			Log.w("TEST", "BUTTON CLICKED");
			
			String userName = newUser.getText().toString();
			String password = newPassword.getText().toString();
			String password2 = verifyPassword.getText().toString();
			String firstName = fName.getText().toString();
			String lastName = lName.getText().toString();
			
			
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
				  user.setAge(userAge);
				  user.setWeight(userWeight);
				  user.setHeight_feet(hf);
				  user.setHeight_inches(hi);
				  db.store(user);
				  Contact doctor = new Contact(user, doctorName, doctorPhone, doctorEmail);
				  Contact relative = new Contact(user, contactName, contactPhone, contactEmail);
				  db.store(doctor);
				  db.store(relative);
				  db.updateUser(user);
				 
			}
			
			Intent mshg = new Intent("my-event");
			mshg.putExtra("message", "data");
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
			
			Log.w("TEST", "test");
			dismiss();
		}
}
