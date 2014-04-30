package com.team4.healthmonitor.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team4.database.Article;
import com.team4.database.Helper;
import com.team4.database.VitalSign;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.dialogs.VitalDialog;
import com.team4.healthmonitor.fragments.StorageFragment;
import com.team4.healthmonitor.fragments.VitalsFragment;

public class VitalAdapter extends ArrayAdapter<VitalSign> {
	private VitalsFragment parentFragment = null;
	
	public VitalAdapter(Context context, int resource, List<VitalSign> objects) {
		super(context, resource, objects);
	}

	public VitalAdapter(VitalsFragment parentFragment,
			Context context, int id, ArrayList<VitalSign> userVitals) {
		super(context, id, userVitals);
		this.parentFragment = parentFragment;
 
	}
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       VitalSign entry = getItem(position);
       Log.w("PHMS", "" + entry.getType() + " " + entry.getValue1());
       String map[] = { "Blood Pressure", "Blood Sugar", "Weight", "Cholesterol" };
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.vital_item, null);
       }
       String rowString = "";
       
	   rowString = Helper.formatTime(entry.getDatetime());
	   rowString += "\t";
       if (entry.getType() == VitalSign.WEIGHT)
       {
    	    rowString += " " + entry.getValue1() + " lbs";
       }
       if (entry.getType() == VitalSign.BLOOD_PRESSURE)
       {
    	   rowString += " Systolic: " + entry.getValue1() + " Diastolic: " + entry.getValue2();
       }
       if (entry.getType() == VitalSign.BLOOD_SUGAR)
       {
    	   rowString += " " + entry.getValue1() + " mg/dL";
       }
       if (entry.getType() == VitalSign.CHOLESTEROL)
       {
    	   rowString += " LDL: " + entry.getValue1() + " mg/dL";
    	   rowString += " HDL: " + entry.getValue2() + " mg/dL";
    	   
       }
       TextView field1 = (TextView)convertView.findViewById(R.id.vitalField1);
       String val1 = ""+entry.getValue1();
       String val2 = ""+entry.getValue2();
       String type = map[entry.getType()];
       field1.setText(rowString);
       ImageButton edit = (ImageButton)convertView.findViewById(R.id.vitalEditBtn);

       final int id = entry.getId();
       final int userId = entry.getUser_Id();
    		   
       Log.w("PHMS", "Generated button for vitalsign with ID = " + id);
       edit.setOnClickListener(new View.OnClickListener() {
	   @Override
		public void onClick(View v) {
			showEditVitalDialog(id, userId);
			
		}
       });
       
       // Return the completed view to render on screen
       return convertView;
   }

    private void showEditVitalDialog(int id, int userId)
    {
        FragmentManager fm2 = parentFragment.getActivity().getSupportFragmentManager();
        VitalDialog vd = new VitalDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.ITEMID, id);
        args.putInt(Arguments.USERID, userId);
        //args.putInt(Arguments.USERID, userId);
        //args.putInt(Arguments.OFFSET, offset);
        vd.setArguments(args);
        vd.show(fm2, "fragment_edit_name");
    }
}