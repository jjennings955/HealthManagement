package com.team4.healthmonitor;


import com.team4.database.VitalSign;
import com.team4.healthmonitor.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class StorageFragment extends Fragment 
{

	
	Button save = null;
	Button clear = null;
	EditText value1 = null;
	EditText value2 = null;
	
	VitalSign vs;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_storage, container, false);
		setHasOptionsMenu(true);
		//((MainAppActivity) getActivity()).setActionBarTitle("Storage");
		
		
		save = (Button)rootView.findViewById(R.id.save);
		clear = (Button)rootView.findViewById(R.id.clear);
		value1 = (EditText)rootView.findViewById(R.id.value1);
		value2 = (EditText)rootView.findViewById(R.id.value2);
		
		
		
		save.setOnClickListener(send);
		
		
		return rootView;
	}
	
	
	private OnClickListener send = new OnClickListener() {
		@Override
		public void onClick(View v) {
			vs = new VitalSign(1, 2,Integer.parseInt(""+ value1.getText()),Integer.parseInt(""+ value1.getText()), 1 );
			int k = Integer.parseInt(""+ value1.getText())+ Integer.parseInt(""+ value2.getText());
			value2.setText("" + k);
			//MainActivity.dh.store(vs);
			//User jason = new User("avvhdkjlkmin", "admin", "jason", "jennings", 6, 1, 200, 26, 'M');
			//MainActivity.dh.store(jason,MainActivity.dh.getWritableDatabase());
			MainActivity.dh.store(vs);
			
		}
	};
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.storage_menu, menu);
	}
}
