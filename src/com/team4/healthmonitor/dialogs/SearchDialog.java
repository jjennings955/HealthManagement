package com.team4.healthmonitor.dialogs;
import java.util.Calendar;

import com.team4.database.Helper;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchDialog extends DialogFragment {
	 int userId;
	 int offset;
	 int type;
	 public static int UNSPECIFIED = 0;
	 public static int VITAL_DATE = 1;
	 public static int STORAGE = 2;
	 public static int DIET_DATE = 3;
	 private static final String SEARCH_DATE = "Enter a date to search for";
	 private static final String SEARCH_KEYWORD = "Enter a date to search for";
	 private EditText searchPhrase;
	 public SearchDialog()
	 {
		 
	 }
	 

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		 View view = inflater.inflate(R.layout.search_dialog, container);
		 Bundle arguments = getArguments();
		 searchPhrase = (EditText)view.findViewById(R.id.searchPhrase);
		 TextView searchLabel = (TextView)view.findViewById(R.id.searchLabel);
		 Button searchButton = (Button)view.findViewById(R.id.searchButton);
		 
		 if (arguments != null)
		 {
			 type = arguments.getInt(Arguments.DIALOGTYPE, UNSPECIFIED);
			 userId = arguments.getInt(Arguments.USERID, -1);
		 }
		 if (type == VITAL_DATE || type == DIET_DATE)
		 {
			 searchLabel.setText(SEARCH_DATE);
			 searchPhrase.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
		 }
		 else
		 {
			 searchLabel.setText(SEARCH_KEYWORD);
			 searchPhrase.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
		 }
		 searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Log.w("PHMS", "Searchstring = " + searchPhrase.getText().toString());
				long date = Helper.getDateFromUserString(searchPhrase.getText().toString());
				if (date == -1)
				{
					searchPhrase.setError("Enter a date in format MM-DD-YYY");
				}
				else
				{
					long ctime = System.currentTimeMillis();
					Calendar foo = Calendar.getInstance();
					Calendar foo2 = Calendar.getInstance();
					foo2.setTimeInMillis(date);
					if (foo2.after(foo))
					{
						searchPhrase.setError("You cannot see the future.");
						return;
					}
					
					foo.setTimeInMillis(ctime);
					foo.set(Calendar.HOUR, 0);
					foo.set(Calendar.MINUTE, 0);
					double offset = date - foo.getTimeInMillis();
					offset = offset/86400000D;
					int newOffset = (int)Math.ceil(offset);
					if (type == DIET_DATE)
					{
						sendUpdateDiet(newOffset);
						dismiss();
					}
					else
					{
						sendUpdateVitals(newOffset);
						dismiss();
					}
				}
				
			}
			
		});
		 getDialog().setTitle("Search");
		 return view;
	 }
		private void sendUpdateVitals(int offset) {
			Intent mshg = new Intent("com.team4.healthmonitor.UPDATEVITALS");
			mshg.putExtra("type", type);
			mshg.putExtra("offset", offset);
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
		}
		private void sendUpdateDiet(int offset) {
			Intent mshg = new Intent("com.team4.healthmonitor.UPDATEDIET");
			mshg.putExtra("offset", offset);
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
		}
}
