package com.team4.healthmonitor;

import java.util.ArrayList;

import com.team4.database.MedSchedule;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

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
       Log.w("PHMS", "ListAdapter" + position);
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.medschedule_item, null);
       }
       // Lookup view for data population
       TextView medName = (TextView) convertView.findViewById(R.id.medName_temp);
       TextView medDosage = (TextView) convertView.findViewById(R.id.medDosage_temp);
       TextView medTime = (TextView)convertView.findViewById(R.id.medTime_temp);
       CheckBox taken = (CheckBox)convertView.findViewById(R.id.medStatus_temp);
       Button edit = (Button)convertView.findViewById(R.id.medEditBtn);

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
       // Return the completed view to render on screen
       return convertView;
   }

}