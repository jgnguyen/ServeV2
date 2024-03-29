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
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class createAccount extends Activity{


	EditText username;
	EditText password;
	EditText firstname;
	EditText lastname;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createaccount);
	}

	public boolean validateForm()
	{
		Boolean un = true, pass = true, fn = true, ln = true;

		username = (EditText) findViewById(R.id.et_username);
		password = (EditText) findViewById(R.id.et_password);
		firstname = (EditText) findViewById(R.id.et_firstname);
		lastname = (EditText) findViewById(R.id.et_lastname);

		if (username.getText().toString().isEmpty())
			un = false;
		if (password.getText().toString().isEmpty()) 
			pass = false;
		if (firstname.getText().toString().isEmpty()) 
			fn = false;
		if (lastname.getText().toString().isEmpty()) 
			ln = false;


		return un && pass && fn && ln;
	}

	public void submitNewAccountClick(View v) throws NoSuchAlgorithmException, ClientProtocolException, IOException
	{
		username = (EditText) findViewById(R.id.et_username);
		password = (EditText) findViewById(R.id.et_password);
		firstname = (EditText) findViewById(R.id.et_firstname);
		lastname = (EditText) findViewById(R.id.et_lastname);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("new_user", "1"));
		nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("password", serveUtilities.SHA1(password.getText().toString())));
		nameValuePairs.add(new BasicNameValuePair("fname", firstname.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("lname", lastname.getText().toString()));

		String result = serveUtilities.getStringFromUrl(nameValuePairs);

		if (validateForm()) {
			if (result.equals("good")) {
				// issue intent to the session picker.
				Intent i = new Intent(this,edu.cs.fsu.signIn.class);
				startActivity(i);
			} else {
				Toast.makeText(this, "Could not create account: Username already taken", Toast.LENGTH_SHORT).show();
				username = (EditText) findViewById(R.id.et_username);
				username.setTextColor(Color.RED);
			}
		} else {
			Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
		}
	}
}
