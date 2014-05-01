package com.team4.healthmonitor.dialogs;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.team4.database.DatabaseHandler;
import com.team4.database.Medication;
import com.team4.database.MedicationEvent;
import com.team4.database.Session;
import com.team4.database.User;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.MainAppActivity;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;

import android.content.DialogInterface;
import android.view.View.OnClickListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;


public class SecurityDialog extends DialogFragment implements OnClickListener
{

	 private EditText username;
	 private EditText password;
	 private int userId;
	 
	 
 public SecurityDialog() 
 {
 }
 
 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
 {
	 Bundle args = getArguments();
	 userId = args.getInt(Arguments.USERID);
	 
     View view = inflater.inflate(R.layout.dialog_security, container);
     
   
     username = (EditText)view.findViewById(R.id.username);
     password = (EditText) view.findViewById(R.id.password);
     


     Button button = (Button)view.findViewById(R.id.btnSubmitSecurity);
     button.setOnClickListener(this);
     
     getDialog().setTitle("Login In Again");
     
     
     
     return view;
     
 }
 

 public void onClick(View v) 
 {
	 DatabaseHandler db = new DatabaseHandler(getActivity());
	  
	  String uname = username.getText().toString();
	  String pass = password.getText().toString();
	  User u = db.login(uname, pass);
	  if(u != null)
     {
	      dismiss();
     }	
  else
  {

     Toast.makeText(getActivity(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

  }
 }
 






}