package com.team4.healthmonitor;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

public class VitalsDialog extends DialogFragment 
{

	 private EditText medName;
	 private EditText dosage;
	 private RadioGroup priority;
	 private RadioButton high;
	 private RadioButton low;
	 private TimePicker time;
	 private EditText perDay;

 public VitalsDialog() 
 {
     // Empty constructor required for DialogFragment
 }

 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
 {
     View view = inflater.inflate(R.layout.dialog_medicine, container);
     medName = (EditText) view.findViewById(R.id.MedName);
     dosage = (EditText) view.findViewById(R.id.Dosage);
     priority = (RadioGroup) view.findViewById(R.id.Priority);
     time = (TimePicker) view.findViewById(R.id.MedTime);
     perDay = (EditText) view.findViewById(R.id.NumberOfTimes);
     getDialog().setTitle("Add a Medication");

     return view;
 }
}