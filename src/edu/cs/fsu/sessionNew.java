package edu.cs.fsu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class sessionNew extends Activity {
	String fname, lname, sessName, sID, sessionPassword;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sessionnew);

		Spinner s = (Spinner) findViewById(R.id.spinner_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.sessionTypes, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);
	}

	public void submit(View v) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = app_preferences.edit();

		Spinner s = (Spinner) findViewById(R.id.spinner_type);
		String selected = s.getSelectedItem().toString();
		if (selected.equals("Attendance")) {
			editor.putString("type", "attendance");
			editor.commit();	
		}
		else if (selected.equals("Survey")) {
			editor.putString("type", "survey");
			editor.commit();
		}
		else {
			editor.putString("type", "surveyannony");
			editor.commit();
		}

		EditText editText_sessionName, editText_sessionID, editText_sessionPassword;

		editText_sessionName = (EditText)findViewById(R.id.editText_sessionName);
		editText_sessionID = (EditText)findViewById(R.id.editText_sessionID);
		editText_sessionPassword = (EditText) findViewById(R.id.editText_sessionPassword);
		
		fname = app_preferences.getString("fname", "");
		lname = app_preferences.getString("lname", "");
		sessName = editText_sessionName.getText().toString();
		sessionPassword = serveUtilities.SHA1(editText_sessionPassword.getText().toString());
		sID = editText_sessionID.getText().toString();
		
		editor.putString("sessionID", sID);
		editor.commit();

		String result = "";
		
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
			nameValuePairs.add(new BasicNameValuePair("new_session", "1"));
			nameValuePairs.add(new BasicNameValuePair("sessionID", sID));
			nameValuePairs.add(new BasicNameValuePair("sessionName", sessName));
			nameValuePairs.add(new BasicNameValuePair("lname", lname));
			nameValuePairs.add(new BasicNameValuePair("fname", fname));
			nameValuePairs.add(new BasicNameValuePair("password", sessionPassword));

			result = serveUtilities.getStringFromUrl(nameValuePairs);			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!result.equals("good")) {
			Log.e("StartingSession","Failed to create session");
			Toast.makeText(this, "Cannot create session", Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(this,edu.cs.fsu.sessionResults.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			i.putExtra("sessionName", sessName);
			//add information to the intent
			startActivity(i);
		}
	}
}