package com.team4.healthmonitor.adapters;

import java.util.List;

import com.team4.database.Article;
import com.team4.database.DatabaseHandler;
import com.team4.database.Food2;
import com.team4.database.FoodJournal;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.fragments.DietFragment;
import com.team4.healthmonitor.fragments.StorageFragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class DietAdapter extends ArrayAdapter<FoodJournal> {
	public DietFragment parentFragment;
	public DietAdapter(Context context, int resource, List<FoodJournal> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}
	public DietAdapter(DietFragment parentFragment, Context context, int resource, List<FoodJournal> objects)
	{
		super(context, resource, objects);
		this.parentFragment = parentFragment;
	}
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
	   DatabaseHandler db = new DatabaseHandler(parentFragment.getActivity());
       FoodJournal entry = getItem(position);  
       Log.w("PHMS", "ListAdapter" + position);
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.foodjournal_item, null);
       }
       // Lookup view for data population
       TextView desc = (TextView) convertView.findViewById(R.id.fj_desc);
       TextView amt = (TextView) convertView.findViewById(R.id.fj_amt);
       TextView cals = (TextView)convertView.findViewById(R.id.fj_cals);
       TextView fat = (TextView)convertView.findViewById(R.id.fj_fat);
       TextView protein = (TextView)convertView.findViewById(R.id.fj_protein);
       TextView carbs = (TextView)convertView.findViewById(R.id.fj_carb);
       
       //CheckBox taken = (CheckBox)convertView.findViewById(R.id.medStatus_temp);
       ImageButton edit = (ImageButton)convertView.findViewById(R.id.fj_edit);
       
       // Populate the data into the template view using the data object
       Food2 food = db.getFood(entry.getFoodId());
       desc.setText(food.getDescription());
       amt.setText("" + entry.getAmount());
       Log.w("PHMS", "Fat for " + food.getDescription() + " " + food.getLipid());
       cals.setText("" + String.format("%1.0f", (food.getCalories()/food.getWeight_serving1() * entry.getAmount())));
       
       carbs.setText("" + String.format("%1.0f", (food.getCarbs()/food.getWeight_serving1() * entry.getAmount())));
       protein.setText("" + String.format("%1.0f", (food.getProtein()/food.getWeight_serving1() * entry.getAmount())));
       fat.setText("" + String.format("%1.0f", (food.getLipid()/food.getWeight_serving1() * entry.getAmount())));
       
       
       final int id = entry.getId();
       edit.setOnClickListener(new View.OnClickListener() {
	   @Override
		public void onClick(View v) {
			
			//parentFragment.showEditArticleDialog(id);
		}
       });
       
       // Return the completed view to render on screen
       return convertView;
   }

}
