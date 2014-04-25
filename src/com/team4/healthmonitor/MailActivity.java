package com.team4.healthmonitor;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast; 

public class MailActivity extends Activity {
   
	
	


   @SuppressLint("NewApi")
@Override 
public void onCreate(Bundle icicle) { 
  super.onCreate(icicle); 
 // setContentView(R.layout.dialog_diet);  
	
  Button addImage = (Button) findViewById(R.id.btnSubmitDiet); 
  addImage.setOnClickListener(new View.OnClickListener() { 
    public void onClick(View view) { 
      Mail m = new Mail("saad.subhani@gmail.com", "screwed2"); 
 
      String[] toArr = {"saad.subhani@gmail.com", "saad.subhani@mavs.uta.edu"}; 
      
      
      m.setTo(toArr); 
      m.setFrom("saad.subhani@gmail.com"); 
      m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device."); 
      m.setBody("Email body."); 
 
      try { 
      //  m.addAttachment("/sdcard/filelocation"); 
 
        if(m.send()) { 
          Toast.makeText(MailActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
        } else { 
          Toast.makeText(MailActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
        } 
      } catch(Exception e) { 
        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
        Log.e("MailApp", "Could not send email", e); 
      } 
    } 
  }); 
} 
}









