package com.team4.healthmonitor.dialogs;

import android.app.Activity;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.team4.database.DatabaseHandler;
import com.team4.database.Medication;
import com.team4.database.MedicationEvent;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.fragments.MedicineFragment;


public class EditMedicineDialog extends DialogFragment implements OnClickListener {
	 private FragmentActivity myContext;
	 private AutoCompleteTextView name;
	 private EditText dosage;
	 private TimePicker time;
	 private Button update;
	 private Button delete;
	 private MedicationEvent event;
	 private DatabaseHandler db;
	 private MedicineFragment parent;
	 private EditText interval;
	 private int id;
	 public EditMedicineDialog()
	 {
		 
	 }
	 

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		 View view = inflater.inflate(R.layout.dialog_edit_medicine, container);
		 Bundle args = this.getArguments();
		 id = args.getInt(Arguments.USERID);
		 dosage = (EditText)view.findViewById(R.id.edit_dosage);
		 interval = (EditText)view.findViewById(R.id.edit_dosage_interval);
		 name = (AutoCompleteTextView)view.findViewById(R.id.edit_medicine);
		 time = (TimePicker)view.findViewById(R.id.timePicker1);
		 db = new DatabaseHandler(getActivity());
		 update = (Button)view.findViewById(R.id.medEditBtn);
		 delete = (Button)view.findViewById(R.id.medDeleteBtn);
		 event = db.getMedicationEvent(id);

		 time.setCurrentHour(event.getTime_hours());
		 time.setCurrentMinute(event.getTime_mins());

		 Medication med = db.getMedication(event.getMedication_id());
		 dosage.setText("" + event.getDosage());
		 name.setText(med.getName());
		 
		 getDialog().setTitle("Edit Medication Schedule");
		 Log.w("PHMS", "Opened edit medicine dialog with medicine schedule id " + id);
		 
		 update.setOnClickListener(this);
		 delete.setOnClickListener(this);
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

			if (arg0.getId() == R.id.medEditBtn)
			{
				handleUpdate();
			}
			else if (arg0.getId() == R.id.medDeleteBtn)
			{
				handleDelete();
			}
		}
		private void handleDelete() {
			MedicationEvent target = db.getMedicationEvent(id);
			if (target != null)
			{
				db.delete(target);
				Intent mshg = new Intent("my-event");
				mshg.putExtra("message", "data");
				LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
			}
			dismiss();
		}


		public void handleUpdate()
		{
			int time_hrs = time.getCurrentHour();
			int time_mins = time.getCurrentMinute();
			String dose = dosage.getText().toString();
			event.setTime_hours(time_hrs);
			event.setTime_mins(time_mins);
			event.setDosage(dose);
			db.updateMedicationEvent(event);
			Intent mshg = new Intent("my-event");
			mshg.putExtra("message", "data");
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);

			dismiss();
		}
}
