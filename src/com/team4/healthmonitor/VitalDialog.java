package com.team4.healthmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

public class VitalDialog extends DialogFragment  {
	
	
	private EditText diastolic;
	private EditText systolic;
	private EditText bgl;
	private EditText hdl;
	private EditText ldl;
	
	 
	
	
	
	public VitalDialog(){}
	
		@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	 {
			 View view2 = inflater.inflate(R.layout.dialog_vitals, container);
			 
			 
			 systolic=(EditText) view2.findViewById(R.id.systolic);
			 diastolic=(EditText) view2.findViewById(R.id.diastolic);
			 bgl=(EditText) view2.findViewById(R.id.bgl);
			 hdl=(EditText) view2.findViewById(R.id.hdl);
			 ldl=(EditText) view2.findViewById(R.id.ldl);
			 getDialog().setTitle("Add Vital Signs");
	  
			return view2;
			
			
			
	 }
	

}
