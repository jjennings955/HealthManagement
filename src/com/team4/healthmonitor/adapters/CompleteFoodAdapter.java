package com.team4.healthmonitor.adapters;

import com.team4.database.DatabaseHandler;
import com.team4.healthmonitor.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;



public class CompleteFoodAdapter extends CursorAdapter {
	private DatabaseHandler db;
	public CompleteFoodAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		db = new DatabaseHandler(context);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		((TextView)arg0).setText(arg2.getString(1));
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final TextView view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		return null;
	}
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
    {
        Cursor currentCursor = null;
        
        if (getFilterQueryProvider() != null)
        {
            return getFilterQueryProvider().runQuery(constraint);
        }
        
        String args = "";
        
        if (constraint != null)
        {
            args = constraint.toString();      
        }
 
        currentCursor = db.getFoodCursor(args);
 
        return currentCursor;
    }



}
