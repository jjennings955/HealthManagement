package com.team4.healthmonitor;


import java.util.List;

import com.team4.database.*;
import com.team4.healthmonitor.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VitalsFragment extends Fragment 
{
	
	TextView vhisto[][] = null; 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_vitals, container, false);
		setHasOptionsMenu(true);
		
		LinearLayout l1 = (LinearLayout)rootView.findViewById(R.id.fragment_vs);
		//TextView tv = new TextView(getActivity());
        //tv.setText("Dynamic Text!");
        //l.addView(tv);
       
        
        
		TextView[] tv = new TextView[4];
	       
	       //tv.setText("Testing...");
	      // l.addView(tv);
		DatabaseHandler db = new DatabaseHandler(getActivity());
				
		LinearLayout l =null;
	       List<VitalSign> vt = db.getVitalSigns(0);       
		     for (VitalSign cn1 : vt) {
		    	  for(int j1=0; j1<4; j1++){
		    		   l = new LinearLayout(getActivity().getBaseContext());
		    		   l.setLayoutParams(new ViewGroup.LayoutParams(
		    			        ViewGroup.LayoutParams.MATCH_PARENT,
		    			        ViewGroup.LayoutParams.WRAP_CONTENT));
		    		   //l.setBackgroundResource(R.id.myshape_s);

		    		  tv[j1] = new TextView(getActivity());
		    		  tv[j1].setLayoutParams(new ViewGroup.LayoutParams(
		    			        ViewGroup.LayoutParams.WRAP_CONTENT,
		    			        ViewGroup.LayoutParams.WRAP_CONTENT));
		    		  tv[j1].setWidth(150);
		    		  //tv[j1].setText("Dynamic Text!");
		    	  	 //l.addView(tv[j1]);
		    	  }
		    	  tv[0].setText(""+cn1.getId());
		    	  tv[1].setText(""+cn1.getType());
		    	  tv[2].setText(""+cn1.getValue1());
		    	  tv[3].setText(""+cn1.getUser_Id());
		          
		    	  for(int j1=0; j1<4; j1++){
		    		 // tv[j1] = new TextView(container.getContext());
		    	  	  l.addView(tv[j1]);
		    	  }
		    	  l1.addView(l);
		          }
		
		
		
		
		//(R.layout.dialog_vitals);
		//)rootView.findViewById(Integer.parseInt("R.id."+i+""+j));
		/*
		for(int i1=0; i1<25; i1++)
			for(int j1=0; j1<4; j1++)
				vhisto[i1][j1]= new TextView(null);
		vhisto[0][0]= (TextView)rootView.findViewById(R.id._11);
		vhisto[0][1]= (TextView)rootView.findViewById(R.id._12);
		vhisto[0][2]= (TextView)rootView.findViewById(R.id._13);
		vhisto[0][3]= (TextView)rootView.findViewById(R.id._14);
		vhisto[1][0]= (TextView)rootView.findViewById(R.id._21);
		vhisto[1][1]= (TextView)rootView.findViewById(R.id._22);
		vhisto[1][2]= (TextView)rootView.findViewById(R.id._23);
		vhisto[1][3]= (TextView)rootView.findViewById(R.id._24);
		vhisto[2][0]= (TextView)rootView.findViewById(R.id._31);
		vhisto[2][1]= (TextView)rootView.findViewById(R.id._32);
		vhisto[2][2]= (TextView)rootView.findViewById(R.id._33);
		vhisto[2][3]= (TextView)rootView.findViewById(R.id._34);
		vhisto[3][0]= (TextView)rootView.findViewById(R.id._41);
		vhisto[3][1]= (TextView)rootView.findViewById(R.id._42);
		vhisto[3][2]= (TextView)rootView.findViewById(R.id._43);
		
		
		
		List<VitalSign> vt = MainActivity.dh.getVitalSigns();       
	      int i = 0, j;
	      for (VitalSign cn1 : vt) {
	    	  j=0;
	    	  vhisto[i][j++].setText(cn1.getId());
	    	  vhisto[i][j++].setText(cn1.getType());
	    	  vhisto[i][j++].setText(cn1.getValue1());
	    	  vhisto[i][j++].setText(cn1.getUser_Id());
	          i++;
	          }
		 
		*/
			
		//((MainAppActivity) getActivity()).setActionBarTitle("Vitals");
        //return new SampleView(this);
       // rootView = l;
		return rootView;
		//return l;
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.vitals_menu, menu);
	}
}
