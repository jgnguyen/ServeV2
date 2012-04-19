package edu.cs.fsu;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class signIn extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
	}
	public void signInClick(View target) throws ClientProtocolException, JSONException, IOException, NoSuchAlgorithmException
	{
		EditText et_username = (EditText) findViewById(R.id.et_username);
		EditText et_password = (EditText) findViewById(R.id.et_password);
		String username = et_username.getText().toString();
		String password = et_password.getText().toString();
		if (username.isEmpty() || password.isEmpty()) {
			Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
		} else {
			String url = String.format("http://www.fsurugby.org/serve/request.php?login=1&username=%s&password=%s", username, serveUtilities.SHA1(password));

			JSONArray json = new JSONArray(serveUtilities.getStringFromUrl(url));
			String id = "";
			String fname = "";
			String lname = "";

			if (!json.isNull(0) && json.length() == 1) {
				id = json.getJSONObject(0).getString("id");
				fname = json.getJSONObject(0).getString("fname");
				lname = json.getJSONObject(0).getString("lname");

				if (id.isEmpty()) {
					Toast.makeText(this, "Incorrect Login", Toast.LENGTH_LONG).show();
				} else {
					final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
					final SharedPreferences.Editor editor = app_preferences.edit();

					editor.putString("fname", fname);
					editor.putString("lname", lname);
					editor.commit();

					Intent i = new Intent(this,edu.cs.fsu.sessionPicker.class);
					startActivity(i);
				}
			} else {
				Toast.makeText(this, "Incorrect Login", Toast.LENGTH_LONG).show();
			}
		}
	}
}
