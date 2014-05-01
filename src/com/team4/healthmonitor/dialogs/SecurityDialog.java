package com.team4.healthmonitor.dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team4.database.DatabaseHandler;
import com.team4.database.User;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;


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