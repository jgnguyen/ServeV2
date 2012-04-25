package edu.cs.fsu;

import java.io.IOException;
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
import android.widget.EditText;
import android.widget.Toast;

public class sessionRejoin extends Activity{
	EditText et_sessionkey, et_password;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sessionrejoin);
	}
	public void submitCodeClick(View v) throws ClientProtocolException, IOException, NoSuchAlgorithmException
	{
		final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = app_preferences.edit();

		String sessionID, password;
		et_sessionkey = (EditText) findViewById(R.id.et_sessionkey);
		et_password = (EditText) findViewById(R.id.et_sessionpassword);
		
		sessionID = et_sessionkey.getText().toString();
		password = serveUtilities.SHA1(et_password.getText().toString());

		if (sessionID.isEmpty()) {
			Toast.makeText(this, "Enter a sessionID", Toast.LENGTH_SHORT).show();
		} else {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("rejoin", "1"));
			nameValuePairs.add(new BasicNameValuePair("sessionID", sessionID));
			nameValuePairs.add(new BasicNameValuePair("password", password));

			String result = serveUtilities.getStringFromUrl(nameValuePairs);

			String good = result.substring(0, 1);			
			if (!good.equals("1")) {
				Log.e("JoiningSession","Failed to join session");
			}
			else {
				String sessName = result.substring(2);
				Toast.makeText(getApplicationContext(), "You have successfully joined session "+sessionID, Toast.LENGTH_SHORT).show();
				Intent i = new Intent(this,edu.cs.fsu.sessionResults.class);
				editor.putString("sessionID", sessionID);
				editor.commit();
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				i.putExtra("sessionName", sessName);
				startActivity(i);
			}
		}
	}

}
