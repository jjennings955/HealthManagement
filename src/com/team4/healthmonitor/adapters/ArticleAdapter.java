package com.team4.healthmonitor.adapters;

import java.util.ArrayList;
import java.util.List;

import com.team4.database.Article;
import com.team4.database.MedSchedule;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


public class ArticleAdapter extends ArrayAdapter<Article> {

	public ArticleAdapter(Context context, int resource, List<Article> objects) {
		super(context, resource, objects);
		
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       Article entry = getItem(position);  
       Log.w("PHMS", "ListAdapter" + position);
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_item, null);
       }
       // Lookup view for data population
       TextView title = (TextView) convertView.findViewById(R.id.artTitle);
       TextView url = (TextView) convertView.findViewById(R.id.artUrl);
       TextView description = (TextView)convertView.findViewById(R.id.artDesc);
       //CheckBox taken = (CheckBox)convertView.findViewById(R.id.medStatus_temp);
       //Button edit = (Button)convertView.findViewById(R.id.medEditBtn);
       int height = title.getMeasuredHeight() + url.getMeasuredHeight() + description.getMeasuredHeight();
       // Populate the data into the template view using the data object
       title.setText(entry.getTitle());
       url.setText(entry.getUrl());
       description.setText(entry.getDescription());
       description.setMinHeight(0);
       final int id = entry.getId();
       /*edit.setOnClickListener(new View.OnClickListener() {
	   @Override
		public void onClick(View v) {
			Log.w("PHMS", "Clicked edit on row with entry " + id);
			parentFragment.showEditMedicineDialog(id);
		}
       });*/
       
       // Return the completed view to render on screen
       return convertView;
   }


}
