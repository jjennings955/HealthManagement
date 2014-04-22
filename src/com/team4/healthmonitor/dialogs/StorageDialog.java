package com.team4.healthmonitor.dialogs;

import com.team4.database.Article;
import com.team4.database.DatabaseHandler;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StorageDialog extends android.support.v4.app.DialogFragment implements android.view.View.OnClickListener {
	private int userId;
	private Button store;
	private EditText title;
	private EditText desc;
	private EditText url;
	private boolean type;
	public static final boolean CREATE = true;
	public static final boolean UPDATE = false;
	private int id = -1;
	private DatabaseHandler db;
	public StorageDialog()
	{
		this.type = CREATE;
	}
	private void sendUpdate() {
		Intent mshg = new Intent("com.team4.healthmonitor.UPDATESTORAGE");
		mshg.putExtra("message", "data");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		 db = new DatabaseHandler(getActivity());
	     View view = inflater.inflate(R.layout.dialog_storage, container);
	     Bundle args = getArguments();
	     Button storeButton = (Button)view.findViewById(R.id.articleSubmitBtn);
	     Button deleteButton = (Button)view.findViewById(R.id.articleDeleteBtn);
	     
	     title = (EditText)view.findViewById(R.id.artTitle_edit);
	     desc = (EditText)view.findViewById(R.id.articleDesc_edit);
	     url = (EditText)view.findViewById(R.id.articleUrl_edit);
	     userId = args.getInt(Arguments.USERID, -1);
	     id = args.getInt(Arguments.ITEMID, -1);
	     this.type = args.getBoolean(Arguments.DIALOGTYPE, CREATE);
	     if (type == CREATE)
	     {
	    	 getDialog().setTitle("Create an Article");
	    	 storeButton.setText("Add");
	    	 deleteButton.setVisibility(View.GONE);
	     }
	     if (type == UPDATE)
	     {
	    	 Article editArticle = db.getArticle(id);
	    	 getDialog().setTitle("Modify an Article");
	    	 title.setText(editArticle.getTitle());
	    	 desc.setText(editArticle.getDescription());
	    	 url.setText(editArticle.getUrl());
	    	 storeButton.setText("Update");
	    	 deleteButton.setVisibility(View.VISIBLE);
	     }
	     storeButton.setOnClickListener(this);
	     deleteButton.setOnClickListener(this);
	     return view;
	}

	@Override
	public void onClick(View buttonClicked) {
		if (buttonClicked.getId() == R.id.articleSubmitBtn)
		{
			String title = "", desc = "", url = "";
			boolean valid = true;
			title = this.title.getText().toString();
			desc = this.desc.getText().toString();
			url = this.url.getText().toString();
			Log.w("PHMS", "title = " + title + " desc = " + desc + " url = " + url);
			if (title.equals(""))
			{
				valid = false;
				this.title.setError("Title must not be empty.");
			}
			if (!URLUtil.isNetworkUrl(url))
			{
				Log.w("PHMS", url);
				valid = false;
				this.url.setError("Invalid URL");
			}
			
			if (valid)
			{
				DatabaseHandler db = new DatabaseHandler(getActivity());
				Article a = new Article();
				a.setTitle(title);
				a.setDescription(desc);
				a.setUrl(url);
				a.setUserId(userId);
				if (this.type == CREATE)
				{
					Log.w("PHMS", a.toString());
					db.store(a);
					sendUpdate();
				}
				else // (type == UPDATE)
				{
					a.setId(id);
					Log.w("PHMS", a.toString());
					db.update(a);
					sendUpdate();
				}

			}
			
		}
		if (buttonClicked.getId() == R.id.articleDeleteBtn)
		{
			Article dummy = new Article();
			dummy.setId(id);
			db.delete(dummy);
			sendUpdate();
		}
		dismiss();	
	}
	
}
	