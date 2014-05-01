package com.team4.healthmonitor.dialogs;
import com.team4.database.DatabaseHandler;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.adapters.CompleteFoodAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.TimePicker;

@SuppressLint("NewApi")
public class DietDialog extends DialogFragment  {
	public String[] TEST = new String[] {
		"Chicken", "Beef", "Pork", "CRACK", "HEROIN"
	};
	
	private AutoCompleteTextView foodIntake;
	private EditText quantity;
	SimpleCursorAdapter mAdapter;
	public DietDialog(){}
	
	@Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
 {
		
		 View view = inflater.inflate(R.layout.dialog_diet, container);
		 final DatabaseHandler db = new DatabaseHandler(getActivity());

		foodIntake=(AutoCompleteTextView) view.findViewById(R.id.autoCompleteFood);
		 getDialog().setTitle("Track Food Intake");
		 
		
	     ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, TEST);
		    mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null,
                    new String[] { "description" },
                    new int[] {android.R.id.text1}, 
                    0);
		    foodIntake.setAdapter(mAdapter);
		    
			mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence str) {
				String args;
				if (str == null)
					args = "a";
				else
					args = str.toString();
			return db.getFoodCursor(args);
			} });
			
			mAdapter.setCursorToStringConverter(new CursorToStringConverter() {
			public CharSequence convertToString(Cursor cur) {
				int index = 1;
				return cur.getString(index);
			}});

		 Button button = (Button)view.findViewById(R.id.diet_submitBtn);
	     button.setOnClickListener(new OnClickListener() {
	         public void onClick(View v) {
	             dismiss();
	         }
	     });
  
		return view;
		
		
		
  }


}
