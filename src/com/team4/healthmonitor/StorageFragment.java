package com.team4.healthmonitor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.Activity;
import android.content.Intent;
import android.webkit.WebSettings;
import android.widget.Toast;


public class StorageFragment extends Fragment{
	private FragmentActivity myContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = (View) inflater.inflate(R.layout.fragment_storage, container, false);
        WebView webView = (WebView) mainView.findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);



        webView.setWebViewClient(new WebViewClient());
        //webView.loadUrl("http://www.cleankutz.appointy.com");
        webView.loadUrl("https://cleankutz.appointy.com/");

        return mainView;

        
        
   

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.schedule_layout, container, false);

    }

    public void onAttach(Activity activity)
   	{
   	    myContext=(FragmentActivity) activity;
   	    super.onAttach(activity);
   	}
   	
   	
   	@Override
   	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
   	{
   	   inflater.inflate(R.menu.storage_menu, menu);
   	}
   	
   	public boolean onOptionsItemSelected(MenuItem item) 
   	{
   	    // Handle item selection
   	    switch (item.getItemId()) 
   	    {
   	        case R.id.add_item_storage:
   	        	showStorageDialog();
   	            return true;
   	        case R.id.settings_item:
   	        	Toast.makeText(getActivity(), "Storage",
   	        		      Toast.LENGTH_SHORT).show();
   	        	return true;
   	        default:
   	            return super.onOptionsItemSelected(item);
   	    }
   	}
   	
       private void showStorageDialog()
       {
           FragmentManager fm = myContext.getSupportFragmentManager();
           DietDialog dd = new DietDialog();
           dd.show(fm, "fragment_edit_name");
       }

}