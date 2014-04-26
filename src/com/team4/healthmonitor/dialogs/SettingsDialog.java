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
		
		User user = db.getUser(id);
		
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
		
		ArrayList<Integer> contacts = new ArrayList<Integer>();
		contacts = db.getContactsList(id);
		
		if(contacts.size() == 2)
		{
			Contact one = db.getContact(contacts.get(0));
			//Contact two = db.getContact(contacts.get(1));
			
			doc_name.setText(one.getName()+"");
			doc_phone.setText(one.getPhone()+"");
			doc_email.setText(one.getEmail()+"");
		//	relative_name.setText(two.getName()+"");
	//		relative_phone.setText(two.getPhone()+"");
	//		relative_email.setText(two.getEmail()+"");
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
			/*
			int time_hrs = time.getCurrentHour();
			int time_mins = time.getCurrentMinute();
			float dose = Float.parseFloat(dosage.getText().toString());
			event.setTime_hours(time_hrs);
			event.setTime_mins(time_mins);
			event.setDosage(dose);
			db.updateMedicationEvent(event);
			Intent mshg = new Intent("my-event");
			mshg.putExtra("message", "data");
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
*/
			dismiss();
		}
}
