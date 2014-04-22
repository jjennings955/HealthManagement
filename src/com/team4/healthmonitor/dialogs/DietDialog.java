package com.team4.healthmonitor.dialogs;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

public class DietDialog extends DialogFragment  {
	public String[] TEST = new String[] {
		"Chicken", "Beef", "Pork", "CRACK", "HEROIN"
	};
	
	private AutoCompleteTextView foodIntake;
	private EditText quantity;
	
	public DietDialog(){}
	
	@Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
 {
		
		 View view = inflater.inflate(R.layout.dialog_diet, container);
		 

		foodIntake=(AutoCompleteTextView) view.findViewById(R.id.autoCompleteDiet);
		 quantity=(EditText) view.findViewById(R.id.quantity);
		
		 getDialog().setTitle("Add Diet");
		 
		
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, TEST);
			foodIntake.setAdapter(adapter);
		 
		 Button button = (Button)view.findViewById(R.id.btnSubmitDiet);
	     button.setOnClickListener(new OnClickListener() {
	         public void onClick(View v) {
	             // When button is clicked, call up to owning activity.
	             dismiss();
	         }
	     });
  
		return view;
		
		
		
  }


}
