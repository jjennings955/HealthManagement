package com.team4.healthmonitor;

import com.team4.database.MedSchedule;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ScheduleBinder implements ViewBinder {
	private MedicineFragment parent;
	public ScheduleBinder(MedicineFragment parent)
	{
		this.parent = parent;
	}
	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		int viewId = view.getId();
		switch (viewId)
		{
		case R.id.medName_temp:
			TextView tv = (TextView)view;
			tv.setText(cursor.getString(2));
			return true;
		case R.id.medDosage_temp:
			TextView tv2 = (TextView)view;
			tv2.setText(cursor.getString(3));
			return true;
		case R.id.medStatus_temp:
			RadioButton rb = (RadioButton)view;
			rb.setChecked(cursor.getString(7) == null ? false : true);
			return true;

		case R.id.medTime_temp:
			TextView tb = (TextView)view;
			tb.setText("" + cursor.getInt(5) + ":" + cursor.getInt(6));
			return true;

		case R.id.medEditBtn:
			Button edit = (Button)view;
			final int id = cursor.getInt(1);
			edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					parent.showEditMedicineDialog(id);
				}
			});
			return true;

			
		}
		return false;
	}

}
