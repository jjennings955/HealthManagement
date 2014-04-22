package com.team4.healthmonitor;


import java.util.ArrayList;
import java.util.List;

import com.team4.database.DatabaseHandler;
import com.team4.database.Medication;
import com.team4.database.MedicationEvent;

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
 public MedicineDialog() 
 {
     // Empty constructor required for DialogFragment
 }
 
 //@Override
 public void onAddButtonClicked(View view)
 {
	 
 }
 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
 {
	 Bundle args = getArguments();
	 userId = args.getInt("id");
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

     
     medName.setAdapter(adapter);
     
     
     
     AutoCompleteSelected foo = new AutoCompleteSelected();
     medName.setOnItemClickListener(foo);
     medName.setOnClickListener(foo);
     medName.setOnItemSelectedListener(foo);
    	


     Button button = (Button)view.findViewById(R.id.btnSubmit);
     button.setOnClickListener(this);
   /*8  button.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
        	 
             // When button is clicked, call up to owning activity.
             dismiss();
         }
     });
*/
     return view;
     
 }
 

 public void onClick(View v) {
	 DatabaseHandler db = new DatabaseHandler(getActivity());
     String enteredMedName = "";
     String enteredDosage = "";
     int hours = -1;
     int mins = -1;
     float dosage = -1.0f;
     String interval = "";
     
     String num_times = "";
     boolean valid = true;
     
     enteredMedName = medName.getText().toString();
     enteredDosage = this.dosage.getText().toString();
     try
     {
    	 dosage = Float.parseFloat(enteredDosage);
     }
     catch (NumberFormatException e)
     {
    	 dosage = -1.0f;
     }
     hours = time.getCurrentHour();
     mins = time.getCurrentMinute();
     num_times = perDay.getText().toString();
     interval = this.interval.getText().toString();
     
     if (db.findMedication(enteredMedName) == null)
     {
    	 valid = false;
    	 medName.setError("Medication name not found in database");
     }
     if (enteredDosage.equals("") && dosage != -1.0f)
     {
    	 valid = false;
    	 this.dosage.setError("Please enter a dosage");
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
    	 
    	 for (int i = 0; i < numTimes; i++)
    	 {
    		 MedicationEvent med = new MedicationEvent();
    		 //med.setUserId(
        	 med.setMedication_id(medId);
        	 med.setUserId(userId);
        	 med.setTime_hours(hours + i*inter);
        	 med.setTime_mins(mins);
        	 med.setDosage(dosage);
        	 med.setDay("sunday"); // FIX ME!
        	 db.store(med);
    	 }
		Intent mshg = new Intent("my-event");
		mshg.putExtra("message", "data");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);

		dismiss();

     }
     //time.get
     //getActivity();
 }
private class AutoCompleteSelected implements OnItemClickListener, OnItemSelectedListener, OnClickListener
{

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DatabaseHandler db = new DatabaseHandler(getActivity());
		Log.w("PHMS", "ONE! " + medName.getText().toString());
		//Log.w("PHMS", "TWO! "+parent.getSelectedItemPosition());
		Medication foo = db.findMedication(medName.getText().toString());
		Log.w("PHMS", "Found: " + foo);
		if (foo != null)
		{
			Toast.makeText(getActivity(), "You have chosen " + " " + foo.toString(), Toast.LENGTH_LONG).show();
			Log.w("PHMS", foo.toString());
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Medication foo = (Medication)parent.getSelectedItem();
		if (foo != null)
		{
			Log.w("PHMS", "THREE! " + foo.toString());
			Toast.makeText(getActivity(), "You have chosen " + " " + foo.toString(), Toast.LENGTH_LONG).show();
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
		
	}

	@Override
	public void onClick(View v) {
		Log.w("PHMS", v.toString());
		
	}
	
}



}