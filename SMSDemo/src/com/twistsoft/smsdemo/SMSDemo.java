package com.twistsoft.smsdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.telephony.SmsManager;

public class SMSDemo extends Activity {
	
	boolean debug = false;
	
	// handle to EditText fields
	private EditText etMessage;
	//private EditText phoneNum;
	
	// handles to UI Bottons
	private Button btCode;
	private Button btSend;
		
	//Contact Number at which message is to be delivered
	final String tgtPhoneNumber = "8739043176";
	final String tgtUrl = "http://www.arnium.com/dump.php";
	
	// handle to message String
	String message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
    	
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsdemo);
        
        // set EditText handle
        etMessage = (EditText)findViewById(R.id.messageText);

        // set Button handles
        btCode = (Button) findViewById(R.id.codeButton);
        btSend = (Button) findViewById(R.id.sendButton);
                
        btCode.setOnClickListener( new OnClickListener() {
        	@Override
        	public void onClick(View view) {
        		if(etMessage.getText().toString().trim().length() == 0) {
        			Toast.makeText(getApplicationContext(), "Please enter your message.", Toast.LENGTH_LONG).show();
        			return;
        		}
			  
        		if(etMessage.getText().toString().trim().length() > 160) {
        			message = etMessage.getText().toString();
        			sendLongSMS()	;		  
        		}
        		else {
        			message = etMessage.getText().toString();
        			sendSMS();
    			}
        	}
        });
       
        btSend.setOnClickListener (new OnClickListener() {
        	@Override
        	public void onClick(View view) {
        		if(etMessage.getText().toString().trim().length() == 0) { 
        			Toast.makeText(getApplicationContext(), "Please enter your message.", Toast.LENGTH_LONG).show();
        			return;
        		}
			  
        		else postData();
        	}
        });
    }

    public void sendSMS() {
        SmsManager smsManager = SmsManager.getDefault();
        message = etMessage.getText().toString();
        if(debug) Log.d("Message", message);
        smsManager.sendTextMessage(tgtPhoneNumber, null, message, null, null);
        
        Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_LONG).show();
    }



    public void sendLongSMS() {    	 
        SmsManager smsManager = SmsManager.getDefault();
        message = etMessage.getText().toString();
        if(debug) Log.d("Message", message);
        ArrayList<String> parts = smsManager.divideMessage(message); 
        smsManager.sendMultipartTextMessage(tgtPhoneNumber, null, parts, null, null);
        
        Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_LONG).show();
    }
    
   public void postData(){
		HttpParams httpParams = new BasicHttpParams();
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		
		HttpPost httpPost = new HttpPost(tgtUrl);

		try {
			if(debug) Log.d("ENTER IN POST METHOD", "Succesfully entered");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			
			message = etMessage.getText().toString();
			
			if(debug) Log.d("Message", message);

			nameValuePairs.add( new BasicNameValuePair("text_message", message) );
	    
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(nameValuePairs, "UTF_8");
	       
			if(debug) Log.d("PUT THE DATA", "Entity : "+ ent.toString());
	       
			httpPost.setEntity(ent);
	       
			HttpResponse response = httpClient.execute(httpPost);
	       
			BufferedReader br = new BufferedReader( new InputStreamReader(response.getEntity().getContent() ) );
	  
			if(debug) Log.d("PUT THE DATA", "Submit data sucesful : "+br.read());

	     } catch (ClientProtocolException e) {
	         System.out.println(e);
	     } catch (IOException e) {
	         System.out.println(e);
	     }
	}
}
      