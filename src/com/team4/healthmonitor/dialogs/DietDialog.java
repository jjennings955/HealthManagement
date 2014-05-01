package com.team4.healthmonitor.dialogs;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.team4.database.DatabaseHandler;
import com.team4.database.FoodJournal;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;

@SuppressLint("NewApi")
public class DietDialog extends DialogFragment  {

	
	private EditText foodIntake;
	private EditText amount;
	private int userId;
	private int offset = 0;
	private int itemId;
	private int mode = 0;
	public static int EDITING = 2;
	public static int CREATING = 1;
	public FoodJournal item;
	public DietDialog(){}
	
	@Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
 {
		
		 View view = inflater.inflate(R.layout.dialog_diet, container);
		 Button button = (Button)view.findViewById(R.id.diet_submitBtn);
		 Button delButton = (Button)view.findViewById(R.id.diet_deleteBtn);
		 foodIntake=(EditText) view.findViewById(R.id.editFood);
		 amount = (EditText) view.findViewById(R.id.diet_amount_edit);
 
		 final DatabaseHandler db = new DatabaseHandler(getActivity());
		 item = null;
		 Bundle arguments = this.getArguments();
		 mode = CREATING;
		 
		 if (arguments != null)
		 {
			 userId = arguments.getInt(Arguments.USERID, -1);
			 offset = arguments.getInt(Arguments.OFFSET, 0);
			 if (arguments.containsKey(Arguments.ITEMID))
			 {
				 mode = EDITING;
				 itemId = arguments.getInt(Arguments.ITEMID);
				 item = db.getFood_journal(itemId);
				 if (item == null)
					 dismiss();
				 delButton.setVisibility(View.VISIBLE);
			 }

		 }
		 else
		 {
			 userId = -1;
		 }
		 getDialog().setTitle("Track Food Intake");
		 
		 
		if (mode == EDITING)
		{
			foodIntake.setText(item.getName());
			amount.setText(""+((int)item.getAmount()));
		}
	     		    
		 delButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.w("PHMS", "Delete " + item.getId());
				db.delete(item);
				sendUpdate();
				dismiss();
			}
		});
	     button.setOnClickListener(new OnClickListener() {
	         public void onClick(View v) {
//<<<<<<< HEAD
	        	 boolean valid = true;
	        	 String foodName = foodIntake.getText().toString();
	        	 String foodAmount = amount.getText().toString();
	        	 if (foodName.equals(""))
	        	 {
	        		 valid = false;
	        		 foodIntake.setError("Invalid food. Type something!");
	        	 }
	        	 if (!foodAmount.matches("-?[0-9]+"))
	        	 {
	        		 valid = false;
	        		 amount.setError("Invalid food amount. Enter a whole number (no decimals)");
	        	 }
	        	 if (valid)
	        	 {
	        		 Log.w("PHMS", " userId = " + userId);
	        		 Log.w("PHMS", " amount = " + foodAmount);
	        		 if (mode == CREATING)
	        		 {
	     				Calendar foo = Calendar.getInstance();
	    				foo.setTimeInMillis(System.currentTimeMillis());
	    				foo.add(Calendar.DAY_OF_YEAR, offset);
	    				
		        		 FoodJournal food = new FoodJournal();
		        		 food.setDatetime(foo.getTimeInMillis());
		        		 food.setUserId(userId);
			        	 food.setName(foodName);
			        	 food.setAmount(Float.parseFloat(foodAmount));
			        	 db.store(food);
	        		 }
	        		 else if (mode == EDITING && item != null)
	        		 {
			        	 item.setName(foodName);
			        	 item.setAmount(Float.parseFloat(foodAmount));
			        	 db.delete(item);
			        	 db.store(item);
	        		 }
	        		 Log.w("PHMS", "MODE = " + mode);
		        	 sendUpdate();
		             dismiss();

	        	 }
	         }
	     });
  
		return view;
		
		
		
  }
	private void sendUpdate() {
		Intent mshg = new Intent("com.team4.healthmonitor.UPDATEDIET");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
	}

}
