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
	private void sendUpdate() {
		Intent mshg = new Intent("com.team4.healthmonitor.UPDATESTORAGE");
		mshg.putExtra("message", "data");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	     View view = inflater.inflate(R.layout.dialog_storage, container);
	     Bundle args = getArguments();
	     Button storeButton = (Button)view.findViewById(R.id.articleSubmitBtn);
	     title = (EditText)view.findViewById(R.id.artTitle_edit);
	     desc = (EditText)view.findViewById(R.id.articleDesc_edit);
	     url = (EditText)view.findViewById(R.id.articleUrl_edit);
	     userId = args.getInt(Arguments.USERID, -1);
	     getDialog().setTitle("Store an Article");
	     Log.w("PHMS", "StoreButton? = " + storeButton);
	     storeButton.setOnClickListener(this);
	     return view;
	}

	@Override
	public void onClick(View arg0) {
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
			db.store(a);
			sendUpdate();
			dismiss();
		}
		
	}
	
}
