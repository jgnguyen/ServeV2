package edu.cs.fsu;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
//import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

		String url = String.format("http://fsurugby.org/serve/request.php?survey_questions=1&sessionID=%s", sessionID);

		try{
			jsonresults = serveUtilities.getStringFromUrl(url);
		}
		catch(Exception e)
		{
			Log.e("tagger","fauked ti getstrubfrom url");
		}
		
		if (!jsonresults.equals("[]")) {
			
			setContentView(R.layout.sessionformview);
			
			//ScrollView scroll = (ScrollView) findViewById(R.id.scrollView1);
			LinearLayout myLayout = (LinearLayout) findViewById(R.id.linearLayout1);
			
			Log.e("SessionForm", "Inside question array work");
			try {
				json = new JSONArray(jsonresults);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				question = json.getJSONObject(0).getString("questions");
				
				Log.e("blah", question);
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
							sendResult += myEditTexts[i].getText().toString().replace(" ", "%20");
						else {
							sendResult += ",";
							sendResult += myEditTexts[i].getText().toString().replace(" ", "%20");
						}
					}
					
					String url = String.format("http://www.fsurugby.org/serve/request.php?answer_survey=1&sessionID=%s&fname=%s&lname=%s&answers=%s", 
							sessionID, fname, lname, sendResult);
					
					String result = "";
					try {
						result = serveUtilities.getStringFromUrl(url);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (!result.equals("good")) {
						Log.e("AnsweringForm","Failed to answer form");
					}
					else {
						Log.d("AnsweringForm", "Answered form correctly!");
						Toast.makeText(getApplicationContext(), "You have successfully completed the Form", Toast.LENGTH_SHORT).show();
					}  
				}
			});

		}
		else {
			Log.e("SessionForm","There is no session survey!");
		}
	}
	public void submitAttendenceClick(View v)
	{

	}
	public void downloadFileClick(View v)
	{

	}
	public void exitSession(View v)
	{
		Intent i = new Intent(this,edu.cs.fsu.sessionPicker.class);
		startActivity(i);
	}
}
