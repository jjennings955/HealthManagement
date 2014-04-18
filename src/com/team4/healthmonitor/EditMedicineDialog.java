package com.team4.healthmonitor;

import com.team4.database.DatabaseHandler;
import com.team4.database.Medication;
import com.team4.database.MedicationEvent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class EditMedicineDialog extends DialogFragment {
	 private FragmentActivity myContext;
	 
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		 View view = inflater.inflate(R.layout.dialog_edit_medicine, container);
		 Bundle args = this.getArguments();
		 int id = args.getInt("id");
		 EditText dosage = (EditText)view.findViewById(R.id.edit_dosage);
		 AutoCompleteTextView name = (AutoCompleteTextView)view.findViewById(R.id.edit_medicine);

		 DatabaseHandler db = new DatabaseHandler(getActivity());
		 MedicationEvent medEvent = db.getMedicationEvent(id);
		 Medication med = db.getMedication(medEvent.getMedication_id());
		 dosage.setText("" + medEvent.getDosage());
		 name.setText(med.getName());
		 
		 getDialog().setTitle("Edit Medication Schedule");
		 Log.w("PHMS", "Opened edit medicine dialog with medicine schedule id " + id);
		 return view;
	 }
		@Override
		public void onAttach(Activity activity)
		{
		    myContext=(FragmentActivity) activity;
		    super.onAttach(activity);
		}
}
