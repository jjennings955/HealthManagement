package com.team4.healthmonitor.adapters;

import java.util.ArrayList;
import java.util.List;

import com.team4.database.DatabaseHandler;
import com.team4.database.Food2;
import com.team4.database.Helper;
import com.team4.database.VitalSign;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.fragments.VitalsFragment;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;



public class CompleteFoodAdapter extends ArrayAdapter<Food2> {
	private VitalsFragment parentFragment = null;
	
	public CompleteFoodAdapter(Context context, int resource, List<Food2> objects) {
		super(context, resource, objects);
	}

	public CompleteFoodAdapter(VitalsFragment parentFragment,
			Context context, int id, ArrayList<Food2> foods) {
		super(context, id, foods);
		this.parentFragment = parentFragment;
 
	}
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       Food2 entry = getItem(position);

       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.vital_item, null);
       }
       String rowString = "";
       

       final int id = entry.getId();
  /*     edit.setOnClickListener(new View.OnClickListener() {
	   @Override
		public void onClick(View v) {
			
		}
       });
    */   
       // Return the completed view to render on screen
       return convertView;
   }
}