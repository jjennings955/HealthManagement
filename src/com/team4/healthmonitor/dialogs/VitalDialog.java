package com.team4.healthmonitor.dialogs;
import java.util.ArrayList;
import java.util.HashMap;

import com.team4.database.DatabaseHandler;
import com.team4.database.VitalSign;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class VitalDialog extends DialogFragment implements OnItemSelectedListener, OnClickListener  {
	
	
	private TextView label1;
	private TextView label2;
	private EditText vital1;
	private EditText vital2;
	private Button savebutton;
	private Spinner vitalPicker;
	private int userId;
	private String mode;

	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	 {
			 View view2 = inflater.inflate(R.layout.dialog_vitals2, container);
			 Bundle arguments = this.getArguments();
			 userId = arguments.getInt(Arguments.USERID, -1);
			 getDialog().setTitle("Add Vital Signs");
			label1 = (TextView)view2.findViewById(R.id.vital_label1);
			label2 = (TextView)view2.findViewById(R.id.vital_label2);
			vital1 = (EditText)view2.findViewById(R.id.vital_edit1);
			vital2 = (EditText)view2.findViewById(R.id.vital_edit2);
			savebutton = (Button)view2.findViewById(R.id.vitalsaveBtn);
			 Spinner spinner = (Spinner)view2.findViewById(R.id.spinner_vitals);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.vital_types, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setOnItemSelectedListener(this);
			spinner.setAdapter(adapter);
			savebutton.setOnClickListener(this);
			return view2;
	 }


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		Adapter foo = arg0.getAdapter();
		String current = (String) foo.getItem(arg2);
		mode = current;
		if (current.equals("Blood Pressure"))
		{
			vital1.setVisibility(View.VISIBLE);
			vital2.setVisibility(View.VISIBLE);
			label1.setVisibility(View.VISIBLE);
			label2.setVisibility(View.VISIBLE);
			label1.setText("Systolic");
			label2.setText("Diastolic");
			vital1.setText("");
			vital2.setText("");
		}
		if (current.equals("Blood Sugar"))
		{
			vital1.setVisibility(View.VISIBLE);
			vital2.setVisibility(View.INVISIBLE);
			label1.setVisibility(View.VISIBLE);
			label2.setVisibility(View.INVISIBLE);
			label1.setText("Blood Glucose");
			
			vital1.setText("");
			vital2.setText("");
			
			
		}
		if (current.equals("Cholesterol"))
		{
			vital1.setVisibility(View.VISIBLE);
			vital2.setVisibility(View.VISIBLE);
			label1.setVisibility(View.VISIBLE);
			label2.setVisibility(View.VISIBLE);
			label1.setText("HDL");
			label2.setText("LDL");
		}
		if (current.equals("Weight"))
		{
			vital1.setVisibility(View.VISIBLE);
			vital2.setVisibility(View.INVISIBLE);
			label1.setVisibility(View.VISIBLE);
			label2.setVisibility(View.INVISIBLE);
			
			label1.setText("Weight");
			
			vital1.setText("");
			vital2.setText("");
					
		}
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	private void sendUpdate(int type) {
		Intent mshg = new Intent("com.team4.healthmonitor.UPDATEVITALS");
		mshg.putExtra("type", type);
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
	}
	@Override
	public void onClick(View view) {
		DatabaseHandler db = new DatabaseHandler(getActivity());
		int type;
		HashMap<String, Integer> modeMap = new HashMap<String, Integer>();
		modeMap.put("Weight", VitalSign.WEIGHT);
		modeMap.put("Blood Pressure", VitalSign.BLOOD_PRESSURE);
		modeMap.put("Blood Sugar", VitalSign.BLOOD_SUGAR);
		modeMap.put("Cholesterol", VitalSign.CHOLESTEROL);
		
		if (modeMap.containsKey(mode))
		{
			type = modeMap.get(mode);
		}
		else
		{
			return;
		}
		
		if (type == VitalSign.BLOOD_SUGAR || type == VitalSign.WEIGHT)
		{
			String val1 = vital1.getText().toString();
			if (!val1.matches("[0-9]+"))
			{
				vital1.setError("Value must be numeric");
			}
			else
			{
				VitalSign v = new VitalSign();
				v.setUser_Id(userId);
				v.setDatetime(System.currentTimeMillis());
				v.setValue1(Integer.parseInt(val1));
				v.setType(type);
				db.store(v);
				sendUpdate(type);
				dismiss();
			}
				
		}
		else
		{
			String val1 = vital1.getText().toString();
			String val2 = vital2.getText().toString();
			boolean valid = true;
			if (!val1.matches("[0-9]+"))
			{
				valid = false;
				vital1.setError("Value must be numeric");
			}
			if (!val2.matches("[0-9]+"))
			{
				valid = false;
				vital2.setError("Value must be numeric");
			}
			if (valid)
			{
				VitalSign v = new VitalSign();
				v.setUser_Id(userId);
				v.setDatetime(System.currentTimeMillis());
				v.setValue1(Integer.parseInt(val1));
				v.setValue2(Integer.parseInt(val2));
				v.setType(type);
				
				db.store(v);
				sendUpdate(type);
				dismiss();
			}
			
		}
		
	}
	
}
