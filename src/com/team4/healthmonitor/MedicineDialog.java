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


public class MedicineDialog extends DialogFragment implements OnClickListener

{

	 private AutoCompleteTextView medName;
	 private EditText dosage;
	 private RadioGroup priority;
	 private RadioButton high;
	 private RadioButton low;
	 private TimePicker time;
	 private EditText perDay;

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
	 DatabaseHandler db = new DatabaseHandler(this.getActivity());

	 List<String> suggestions = new ArrayList<String>();
	 ArrayList<Medication> medications = db.getMedications();
	 for (Medication m : medications)
	 {
		 suggestions.add(m.getName());
	 }
	 //String[] suggestions2 = (String[])suggestions.toArray();
	 ArrayAdapter<Medication> adapter = new ArrayAdapter<Medication>(this.getActivity(), android.R.layout.simple_list_item_1, medications);

     View view = inflater.inflate(R.layout.dialog_medicine, container);
     medName = (AutoCompleteTextView) view.findViewById(R.id.MedName);
     
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
     button.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
             // When button is clicked, call up to owning activity.
             dismiss();
         }
     });

     return view;
     
 }
 

 public void onClick(View v) {
     // When button is clicked, call up to owning activity.
     getActivity();
 }
private class AutoCompleteSelected implements OnItemClickListener, OnItemSelectedListener, OnClickListener
{

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.w("PHMS", medName.getText().toString());
		Log.w("PHMS", ""+parent.getSelectedItemPosition());
		Medication foo = (Medication)parent.getSelectedItem();
		if (foo != null)
			Log.w("PHMS", foo.toString());
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Medication foo = (Medication)parent.getSelectedItem();
		if (foo != null)
			Log.w("PHMS", foo.toString());
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