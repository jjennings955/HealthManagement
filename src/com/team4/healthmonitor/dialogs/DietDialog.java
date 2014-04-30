package com.team4.healthmonitor.dialogs;
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
import android.widget.AutoCompleteTextView;
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
	public DietDialog(){}
	
	@Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
 {
		
		 View view = inflater.inflate(R.layout.dialog_diet, container);
		 final DatabaseHandler db = new DatabaseHandler(getActivity());
		 Bundle arguments = this.getArguments();
		 if (arguments != null)
			 userId = arguments.getInt(Arguments.USERID, -1);
		 else
			 userId = -1;
		 
		foodIntake=(EditText) view.findViewById(R.id.editFood);
		amount = (EditText) view.findViewById(R.id.diet_amount_edit);
		 getDialog().setTitle("Track Food Intake");
		 
		
	     		    
		 Button button = (Button)view.findViewById(R.id.diet_submitBtn);
	     button.setOnClickListener(new OnClickListener() {
	         public void onClick(View v) {
	        	 boolean valid = true;
	        	 String foodName = foodIntake.getText().toString();
	        	 String foodAmount = amount.getText().toString();
	        	 if (foodName.equals(""))
	        	 {
	        		 valid = false;
	        		 foodIntake.setError("Invalid food. Type something!");
	        	 }
	        	 if (!foodAmount.matches("[0-9]+"))
	        	 {
	        		 valid = false;
	        		 amount.setText("Invalid food amount. Enter a whole number (no decimals)");
	        	 }
	        	 if (valid)
	        	 {
	        		 Log.w("PHMS", " userId = " + userId);
	        		 Log.w("PHMS", " amount = " + foodAmount);
	        		 
	        		 FoodJournal food = new FoodJournal();
	        		 food.setDatetime(System.currentTimeMillis());
	        		 food.setUserId(userId);
		        	 food.setName(foodName);
		        	 food.setAmount(Float.parseFloat(foodAmount));
		        	 db.store(food);
	        	 }
	        	 sendUpdate();
	             dismiss();
	         }
	     });
  
		return view;
		
		
		
  }
	private void sendUpdate() {
		Intent mshg = new Intent("com.team4.healthmonitor.UPDATEDIET");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
	}

}
