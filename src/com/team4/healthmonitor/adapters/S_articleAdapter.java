package com.team4.healthmonitor.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team4.database.Article;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.fragments.SearchFragment;

public class S_articleAdapter extends ArrayAdapter<Article> {

private SearchFragment sparentFragment = null;
	
	public S_articleAdapter(Context context, int resource, List<Article> objects) {
		super(context, resource, objects);
		
	}
	public S_articleAdapter(SearchFragment sparentFragment, Context context, int resource, List<Article> objects)
	{
		super(context, resource, objects);
		this.sparentFragment = sparentFragment;
	}
	
	
	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       Article entry = getItem(position);  
       Log.w("PHMS", "ListAdapter" + position);
       Log.w("PHMS", entry.getDescription() +"seetAWETwe");
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_article_item, null);
       }
       // Lookup view for data population
       TextView stitle = (TextView) convertView.findViewById(R.id.sartTitle);
       TextView surl = (TextView) convertView.findViewById(R.id.sartUrl);
       TextView sdescription = (TextView)convertView.findViewById(R.id.sartDesc);
       //CheckBox taken = (CheckBox)convertView.findViewById(R.id.medStatus_temp);
       ImageButton sedit = (ImageButton)convertView.findViewById(R.id.sstorageEditBtn);
       
       // Populate the data into the template view using the data object
       stitle.setText(entry.getTitle());
       surl.setText(entry.getUrl());
       sdescription.setText(entry.getDescription());
       sdescription.setMinHeight(0);
       final int id = entry.getId();
       sedit.setOnClickListener(new View.OnClickListener() {
	   @Override
		public void onClick(View v) {
			
			//sparentFragment.showEditArticleDialog(id);
		}
       });
       
       // Return the completed view to render on screen
       return convertView;
   }
	
	
	

}
