package edu.cs.fsu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class sessionForm extends Activity{
	String questions[];
	String question;
	String jsonresults;
	String sessionID;
	String fname, lname;
	public int numOfQuestions;
	public String sendResult = "";
	JSONArray json;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sessionform);

		final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

		sessionID = app_preferences.getString("sessionID", "");
		fname = app_preferences.getString("fname", "");
		lname = app_preferences.getString("lname", "");


		try{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("survey_questions", "1"));
			nameValuePairs.add(new BasicNameValuePair("sessionID", sessionID));

			jsonresults = serveUtilities.getStringFromUrl(nameValuePairs);		}
		catch(Exception e)
		{
			Log.d("tagger","fauked ti getstrubfrom url");
		}

		if (!jsonresults.equals("[]")) {

			setContentView(R.layout.sessionformview);

			LinearLayout myLayout = (LinearLayout) findViewById(R.id.linearLayout1);

			Log.d("SessionForm", "Inside question array work");
			try {
				json = new JSONArray(jsonresults);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				question = json.getJSONObject(0).getString("questions");

				Log.d("blah", question);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			numOfQuestions = question.split(",").length;

			questions = question.split(",");

			Log.d("TestingQuestion", questions[0]);

			final TextView[] myTextViews = new TextView[numOfQuestions]; // create an empty array;
			final EditText[] myEditTexts = new EditText[numOfQuestions];

			for (int i = 0; i < numOfQuestions; i++) {
				// create a new textview
				final TextView rowTextView = new TextView(this);
				final EditText rowEditText = new EditText(this);

				// set some properties of rowTextView or something
				rowTextView.setText(questions[i]);

				// add the textview to the linearlayout
				myLayout.addView(rowTextView);
				myLayout.addView(rowEditText);

				// save a reference to the textview for later
				myTextViews[i] = rowTextView;
				myEditTexts[i] = rowEditText;
			}

			Button btn = new Button(this);
			btn.setText("Submit Answers");
			myLayout.addView(btn);

			btn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub

					for (int i = 0; i < numOfQuestions; i++) {
						if (i == 0)
							sendResult += myEditTexts[i].getText().toString();
						else {
							sendResult += ",";
							sendResult += myEditTexts[i].getText().toString();
						}
					}

					String result = "";
					try {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
						nameValuePairs.add(new BasicNameValuePair("answer_survey", "1"));
						nameValuePairs.add(new BasicNameValuePair("sessionID", sessionID));
						nameValuePairs.add(new BasicNameValuePair("fname", fname));
						nameValuePairs.add(new BasicNameValuePair("lname", lname));
						nameValuePairs.add(new BasicNameValuePair("answers", sendResult));

						result = serveUtilities.getStringFromUrl(nameValuePairs);					
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (!result.equals("good")) {
						Log.d("AnsweringForm","Failed to answer form");
					}
					else {
						Log.d("AnsweringForm", "Answered form correctly!");

						// make alert dialog send back to sessionPicker.java

						AlertDialog.Builder builder = new AlertDialog.Builder(sessionForm.this);
						builder.setMessage("Survey submitted successfully!")
						.setCancelable(false)
						.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(sessionForm.this);
								final SharedPreferences.Editor editor = app_preferences.edit();

								editor.putString("sessionID", "");
								editor.commit();

								Intent i = new Intent(sessionForm.this,edu.cs.fsu.sessionPicker.class);
								startActivity(i);
							}
						});
						AlertDialog alert = builder.create();
						alert.show();

					}  
				}
			});

		}
		else {
			Log.d("SessionForm","There is no session survey!");
			// do a dialog box
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Attendance has been recorded!")
			.setCancelable(false)
			.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(sessionForm.this);
					final SharedPreferences.Editor editor = app_preferences.edit();

					editor.putString("sessionID", "");
					editor.commit();

					Intent i = new Intent(sessionForm.this,edu.cs.fsu.sessionPicker.class);
					startActivity(i);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
}
