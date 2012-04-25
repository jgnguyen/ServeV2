package edu.cs.fsu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class sessionExisting extends Activity{
	EditText et_sessionkey;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sessionexisting);
	}
	public void submitCodeClick(View v) throws ClientProtocolException, IOException
	{
		final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = app_preferences.edit();

		String sessionID;
		String fname = app_preferences.getString("fname", "");
		String lname = app_preferences.getString("lname", "");
		et_sessionkey = (EditText) findViewById(R.id.et_sessionkey);

		sessionID = et_sessionkey.getText().toString();

		if (sessionID.isEmpty()) {
			Toast.makeText(this, "Enter a sessionID", Toast.LENGTH_SHORT).show();
		} else {
			editor.putString("sessionID", sessionID);
			editor.commit();
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("add_attendee", "1"));
			nameValuePairs.add(new BasicNameValuePair("sessionID", sessionID));
			nameValuePairs.add(new BasicNameValuePair("fname", fname));
			nameValuePairs.add(new BasicNameValuePair("lname", lname));

			String result = serveUtilities.getStringFromUrl(nameValuePairs);

			if (!result.equals("good")) {
				Log.e("JoiningSession","Failed to join session");
			}
			else {				
				Intent i = new Intent(this,edu.cs.fsu.sessionForm.class);
				startActivity(i);
			}
		}
	}

}