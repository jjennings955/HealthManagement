package com.team4.healthmonitor.dialogs;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.team4.database.DatabaseHandler;
import com.team4.database.Medication;
import com.team4.database.MedicationEvent;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;

import android.content.DialogInterface;
import android.view.View.OnClickListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

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


public class MedicineDialog extends DialogFragment implements OnClickListener

{

	 private AutoCompleteTextView medName;
	 private EditText dosage;
	 private RadioGroup priority;
	 private RadioButton high;
	 private RadioButton low;
	 private TimePicker time;
	 private EditText perDay;
	 private EditText interval;
	 private int userId;
	 private HashMap<Integer, CheckBox> checkMap;
 public MedicineDialog() 
 {
 }
 
 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
 {
	 Bundle args = getArguments();
	 userId = args.getInt(Arguments.USERID);
	 DatabaseHandler db = new DatabaseHandler(this.getActivity());
	 //Intent intent = this.getActivity().getIntent().getIntExtra("userid", defaultValue);
	 List<String> suggestions = new ArrayList<String>();
	 ArrayList<Medication> medications = db.getMedications();
	 for (Medication m : medications)
	 {
		 suggestions.add(m.getName());
	 }
	 //String[] suggestions2 = (String[])suggestions.toArray();
	 final ArrayAdapter<Medication> adapter = new ArrayAdapter<Medication>(this.getActivity(), android.R.layout.simple_list_item_1, medications);
	 //SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, layout, c, from, to)
     View view = inflater.inflate(R.layout.dialog_medicine, container);
     medName = (AutoCompleteTextView) view.findViewById(R.id.MedName);
     interval = (EditText)view.findViewById(R.id.edit_dosage_interval);
     dosage = (EditText) view.findViewById(R.id.Dosage);
     priority = (RadioGroup) view.findViewById(R.id.Priority);
     time = (TimePicker) view.findViewById(R.id.MedTime);
     perDay = (EditText) view.findViewById(R.id.NumberOfTimes);
     getDialog().setTitle("Add a Medication");

	
	 checkMap = new HashMap<Integer, CheckBox>();
	 checkMap.put(Calendar.SUNDAY, (CheckBox)view.findViewById(R.id.medicine_takenSun));
	 checkMap.put(Calendar.MONDAY, (CheckBox)view.findViewById(R.id.medicine_takenMon));
	 checkMap.put(Calendar.TUESDAY, (CheckBox)view.findViewById(R.id.medicine_takenTue));
	 checkMap.put(Calendar.WEDNESDAY, (CheckBox)view.findViewById(R.id.medicine_takenWed));
	 checkMap.put(Calendar.THURSDAY, (CheckBox)view.findViewById(R.id.medicine_takenThu));
	 checkMap.put(Calendar.FRIDAY, (CheckBox)view.findViewById(R.id.medicine_takenFri));
	 checkMap.put(Calendar.SATURDAY, (CheckBox)view.findViewById(R.id.medicine_takenSat));
	 
     medName.setAdapter(adapter);
     
     Button button = (Button)view.findViewById(R.id.btnSubmit);
     button.setOnClickListener(this);
     return view;
     
 }
 

 public void onClick(View v) {
	 DatabaseHandler db = new DatabaseHandler(getActivity());
     String enteredMedName = "";
     String enteredDosage = "";
     int hours = -1;
     int mins = -1;
     String dosage = "";
     String interval = "";
     
     String num_times = "";
     boolean valid = true;
     
     enteredMedName = medName.getText().toString();
     enteredDosage = this.dosage.getText().toString();
     /*try
     {
    	 dosage = Float.parseFloat(enteredDosage);
     }
     catch (NumberFormatException e)
     {
    	 dosage = -1.0f;
     }*/
     
     hours = time.getCurrentHour();
     mins = time.getCurrentMinute();
     num_times = perDay.getText().toString();
     interval = this.interval.getText().toString();
     
     if (db.findMedication(enteredMedName) == null)
     {
    	 valid = false;
    	 medName.setError("Medication name not found in database");
     }
     Medication medcheck = db.findMedication(enteredMedName);
     if (medcheck != null && db.willConflict(userId, medcheck.getId()))
     {
    	 valid = false;
    	 medName.setError("This medication conflicts with a scheduled medication!");
     }
     String testDosage = enteredDosage.toLowerCase();
     if (enteredDosage.equals("") || !testDosage.matches("[0-9]+(ml|mg|capsules|pills|tabs|g|mcg)?"))
     {
    	 valid = false;
    	 this.dosage.setError("Please enter a valid dosage (number followed by units)");
     }
     if (!num_times.matches("[1-9][0-9]*?"))
     {
    	 valid = false;
    	 perDay.setError("Please enter a number (greater than zero)");
     }
     if (!interval.matches("[0-9]+"))
     {
    	 valid = false;
    	 this.interval.setError("Please enter the time between medications");
     }
     if (valid)
     {
    	 int numTimes = Integer.parseInt(num_times);
    	 ArrayList<MedicationEvent> schedule = new ArrayList<MedicationEvent>();
    	 int medId = db.findMedication(enteredMedName).getId();
    	 int inter = Integer.parseInt(interval);

    	 for (Entry<Integer, CheckBox> e : checkMap.entrySet())
    	 {
    		 if (e.getValue().isChecked())
    		 {
		    	 for (int i = 0; i < numTimes; i++)
		    	 {
		    		 MedicationEvent med = new MedicationEvent();
		        	 med.setMedication_id(medId);
		        	 med.setUserId(userId);
		        	 med.setTime_hours(hours + i*inter);
		        	 med.setTime_mins(mins);
		        	 med.setDosage(enteredDosage);
		        	 med.setDay(e.getKey()); // FIX ME!
		        	 db.store(med);
		    	 }
    		 }
    	 }
		sendUpdate();

		dismiss();

     }
 }

private void sendUpdate() {
	Intent mshg = new Intent("my-event");
	mshg.putExtra("message", "data");
	LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
}



}