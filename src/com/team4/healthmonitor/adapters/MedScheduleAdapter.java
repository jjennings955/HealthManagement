package com.team4.healthmonitor.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team4.database.DatabaseHandler;
import com.team4.database.Helper;
import com.team4.database.MedSchedule;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.fragments.MedicineFragment;

public class MedScheduleAdapter extends ArrayAdapter<MedSchedule> {
	private MedicineFragment parentFragment;
	
    public MedScheduleAdapter(MedicineFragment medicineFragment, Context context, ArrayList<MedSchedule> items) {
    
       super(context, R.layout.medschedule_item, items);
       parentFragment = medicineFragment;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       MedSchedule entry = getItem(position);
       final String date = entry.getDate();
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.medschedule_item, null);
       }
       // Lookup view for data population
       TextView medName = (TextView) convertView.findViewById(R.id.medName_1);
       TextView medDosage = (TextView) convertView.findViewById(R.id.medDosage_temp);
       TextView medTime = (TextView)convertView.findViewById(R.id.medTime_temp);
       CheckBox taken = (CheckBox)convertView.findViewById(R.id.medStatus_temp);
       ImageButton edit = (ImageButton)convertView.findViewById(R.id.medEditBtn2);

       // Populate the data into the template view using the data object
       medName.setText(entry.name);
       medDosage.setText(entry.dosage);
       medTime.setText(entry.time);
       taken.setChecked(entry.status);
       final int id = entry.id;
       
       edit.setOnClickListener(new View.OnClickListener() {
	   @Override
		public void onClick(View v) {
			Log.w("PHMS", "Clicked edit on row with entry " + id);
			parentFragment.showEditMedicineDialog(id);
		}
       });
       
       taken.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			DatabaseHandler db = new DatabaseHandler(parentFragment.getActivity());
			if (isChecked)
				db.medicationTaken(id, date);
			else
				db.medicationNotTaken(id, date);
		}
	});
       // Return the completed view to render on screen
       return convertView;
   }

}