package edu.cs.fsu;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
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
			
			String url = String.format("http://www.fsurugby.org/serve/request.php?add_attendee=1&sessionID=%s&fname=%s&lname=%s", sessionID, fname, lname);
			String result = "";
			result = serveUtilities.getStringFromUrl(url);

			if (!result.equals("good")) {
				Log.e("JoiningSession","Failed to join session");
			}
			else {
				Toast.makeText(getApplicationContext(), "You have successfully joined session "+sessionID, Toast.LENGTH_SHORT).show();
				
				Intent i = new Intent(this,edu.cs.fsu.sessionForm.class);
				startActivity(i);
			}
		}
	}

}
