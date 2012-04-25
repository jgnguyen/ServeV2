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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class sessionResults extends Activity {

	// var associated with ListView
	private ListView results;
	private ListView results2;
	private String[] values;
	private String[] values2;
	private String[] values3;
	private String[] values4;
	private String[] annonymousValues;

	// var associated with JSON parsing
	private String nameList;
	private String jsonresults;
	private JSONArray jObject;

	//vars associated with TVs
	private String sessionID;
	private String sessionName;
	private String sessionType;
	private String sessionPermType;
	private TextView id;
	private TextView name;
	private TextView type;

	private TextView fname;
	private TextView lname;

	private String sendResult;
	private EditText et_question1, et_question2, et_question3, et_question4, et_question5, et_question6, et_question7, et_question8, et_question9, et_question10;

	static final int NUM_QUESTION_ID = 0;
	private int numOfQuestions;	
	private Handler handler;
	private Runnable refresh;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sessionresults);

		results = (ListView) findViewById(R.id.sessionResultsListView); 

		name = (TextView) findViewById(R.id.tv_results_sessionName);
		id = (TextView) findViewById(R.id.tv_results_sessionID);
		type = (TextView) findViewById(R.id.tv_results_sessionType);

		final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		sessionID = app_preferences.getString("sessionID", "");
		sessionType = app_preferences.getString("type", "");
		sessionName = getIntent().getStringExtra("sessionName");
		sessionPermType = sessionType;
		Log.e("tagger ssid",sessionID);
		name.setText("Session Name: "+ sessionName);
		id.setText("ID: " +sessionID);
		type.setText("Type: " +sessionType);
		
		if((sessionType.equals("survey")) || (sessionType.equals("surveyannony")))
		{
			Log.d("sessionResult","go into survey");
			setContentView(R.layout.sessionformview);
			
			/*
			 TableLayout.LayoutParams params;
             TableLayout layout = new TableLayout(this);
             layout.setPadding(6, 6, 6, 6);


             TableRow row_header = new TableRow(this);

             mPickInteger = new NumberPicker(this);
             mPickInteger.setRange(1, 10);

             TableRow row_one = new TableRow(this);
             row_one.setGravity(Gravity.CENTER);
             row_one.addView(mPickInteger);

             layout.addView(row_header);

             TableLayout table_main = new TableLayout(this);
             table_main.addView(row_one);

             TableRow row_main = new TableRow(this);
             row_main.setGravity(Gravity.CENTER_HORIZONTAL);
             row_main.addView(table_main);

             layout.addView(row_main);

             try
             {
            	 mPickInteger.setCurrent(0);
             }
             catch (Exception ex)
             {

             }
			 */           

			final CharSequence[] items = {"1 Question", "2 Questions", "3 Questions", "4 Questions", "5 Questions", "6 Questions", "7 Questions", "8 Questions", "9 Questions", "10 Questions"};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("How many questions?");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					numOfQuestions = item+1;
					Log.d("NumOfQuestions", Integer.toString(numOfQuestions));

					switch (numOfQuestions) {
					case 1: setContentView(R.layout.sessionresultsurvey1);
					break;
					case 2: setContentView(R.layout.sessionresultsurvey2);
					break;
					case 3: setContentView(R.layout.sessionresultsurvey3);
					break;
					case 4: setContentView(R.layout.sessionresultsurvey4);
					break;
					case 5: setContentView(R.layout.sessionresultsurvey5);
					break;
					case 6: setContentView(R.layout.sessionresultsurvey6);
					break;
					case 7: setContentView(R.layout.sessionresultsurvey7);
					break;
					case 8: setContentView(R.layout.sessionresultsurvey8);
					break;
					case 9: setContentView(R.layout.sessionresultsurvey9);
					break;
					case 10: setContentView(R.layout.sessionresultsurvey10);
					break;
					}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		handler = new Handler();
		refresh = new Runnable() {

			@Override
			public void run() {
				updateResults();
				handler.postDelayed(this, 5000);
			}
			
		};
		handler.post(refresh);
	}
	
	@Override
	public void onPause()
	{
		handler.removeCallbacks(refresh);
		super.onPause();
	}
	
	@Override 
	public void onDestroy()
	{
		handler.removeCallbacks(refresh);
		super.onDestroy();
	}

	public void onClickBack(View v)
	{
		updateResults();
		handler.post(refresh);
	}
	
	public void updateResults()
	{
		Log.d("sessionResult","go into updateResults");

		if(sessionType.equals("attendance"))
		{
			Log.d("sessionResult","go into attendance");
			getSessionResults();    
			
		}
		else if(sessionType.equals("submitSurvey") )
		{
			Log.d("sessionResult","go into submitSurvey");
			setContentView(R.layout.sessionresults);
			results = (ListView) findViewById(R.id.sessionResultsListView); 

			name = (TextView) findViewById(R.id.tv_results_sessionName);
			id = (TextView) findViewById(R.id.tv_results_sessionID);
			type = (TextView) findViewById(R.id.tv_results_sessionType);
			final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
			sessionID = app_preferences.getString("sessionID", "");
			sessionType = "submitSurvey";

			Log.d("tagger ssid",sessionID);
			name.setText("Session Name: "+ sessionName);
			id.setText("ID: " +sessionID);
			type.setText("Type: Survey Answers");
			getSessionResults();
			results.setOnItemClickListener (listener);
			results.setItemsCanFocus(true);

		}
		else if(sessionType.equals("file") )
		{
			Log.d("sessionResult","go into file");

		}
		Log.d("sessionResult","Done Update Results");
	}
	public void getSessionResults()
	{
		Log.d("sessionResult","called getSessionResults");
		Log.d("sessionResult sessionID",sessionID);

		nameList = "Waiting for results... (empty)";

		try{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("attendees", "1"));
			nameValuePairs.add(new BasicNameValuePair("sessionID", sessionID));

			jsonresults = serveUtilities.getStringFromUrl(nameValuePairs);		}
		catch(Exception e)
		{
			Log.d("sessionResult","Failed to get from URL");

		}


		try {
			jObject = new JSONArray(jsonresults);
			Log.d("sessionResult","got jsonArrayL");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		Log.d("sessionResult jObject",jObject.toString());


		try {
			nameList = jObject.getJSONObject(0).getString("attendees");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("sessionResult namelist",nameList.toString());

		if (sessionPermType.equals("surveyannony")) {
			Log.d("Testing", "inside surveyannony");
			int count = nameList.length() - nameList.replace(",", "").length();
			annonymousValues = new String[count+1];
			for (int i = 0; i <= count; i++) {
				annonymousValues[i] = "Annonymous User " + (i+1);
			}
		}

		values = nameList.split(",");

		Log.d("sessionResult values",values.toString());

		if (sessionPermType.equals("surveyannony")) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, annonymousValues);
			results.setAdapter(adapter);
		}
		else {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, values);
			results.setAdapter(adapter);
		}

		Log.d("sessionResult","set adpater");

		Log.d("sessionResult","finished updating info");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sessionresultsmenu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d("sessionResult","in option bool");
		switch (item.getItemId()) {
		case R.id.menu_item_endsession:
		    handler.removeCallbacks(refresh);    
			Intent i = new Intent(this,edu.cs.fsu.sessionPicker.class); Log.e("sessionResult","end session");
			startActivity(i);
			break;
		case R.id.menu_item_email:     
			break;  
		case R.id.menu_item_save: 
			break;
		case R.id.menu_item_refresh: 
			updateResults();	Log.e("sessionResult","refresh");
			break;
		}

		Log.d("sessionResult","out option bool");
		return true;
	}

	public void createSurveyClick(View v)
	{
		String question1, question2, question3, question4, question5, question6, question7, question8, question9, question10;
		sendResult = "";

		switch (numOfQuestions) {
		case 1:
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			question1 = et_question1.getText().toString();
			sendResult += question1;
			break;
		case 2:
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			et_question2 = (EditText) findViewById(R.id.editText_question2);
			question1 = et_question1.getText().toString();
			question2 = et_question2.getText().toString();
			sendResult += question1 + "," + question2;
			break;
		case 3:
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			et_question2 = (EditText) findViewById(R.id.editText_question2);
			et_question3 = (EditText) findViewById(R.id.editText_question3);
			question1 = et_question1.getText().toString();
			question2 = et_question2.getText().toString();
			question3 = et_question3.getText().toString();
			sendResult += question1 + "," + question2 + "," + question3;
			break;
		case 4:
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			et_question2 = (EditText) findViewById(R.id.editText_question2);
			et_question3 = (EditText) findViewById(R.id.editText_question3);
			et_question4 = (EditText) findViewById(R.id.editText_question4);
			question1 = et_question1.getText().toString();
			question2 = et_question2.getText().toString();
			question3 = et_question3.getText().toString();
			question4 = et_question4.getText().toString();
			sendResult += question1 + "," + question2 + "," + question3 + "," + question4;
			break;
		case 5:
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			et_question2 = (EditText) findViewById(R.id.editText_question2);
			et_question3 = (EditText) findViewById(R.id.editText_question3);
			et_question4 = (EditText) findViewById(R.id.editText_question4);
			et_question5 = (EditText) findViewById(R.id.editText_question5);
			question1 = et_question1.getText().toString();
			question2 = et_question2.getText().toString();
			question3 = et_question3.getText().toString();
			question4 = et_question4.getText().toString();
			question5 = et_question5.getText().toString();
			sendResult += question1 + "," + question2 + "," + question3 + "," + question4 + "," + question5;
			break;
		case 6: 
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			et_question2 = (EditText) findViewById(R.id.editText_question2);
			et_question3 = (EditText) findViewById(R.id.editText_question3);
			et_question4 = (EditText) findViewById(R.id.editText_question4);
			et_question5 = (EditText) findViewById(R.id.editText_question5);
			et_question6 = (EditText) findViewById(R.id.editText_question6);
			question1 = et_question1.getText().toString();
			question2 = et_question2.getText().toString();
			question3 = et_question3.getText().toString();
			question4 = et_question4.getText().toString();
			question5 = et_question5.getText().toString();
			question6 = et_question6.getText().toString();
			sendResult += question1 + "," + question2 + "," + question3 + "," + question4 + "," + question5 + "," + question6;
			break;
		case 7:
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			et_question2 = (EditText) findViewById(R.id.editText_question2);
			et_question3 = (EditText) findViewById(R.id.editText_question3);
			et_question4 = (EditText) findViewById(R.id.editText_question4);
			et_question5 = (EditText) findViewById(R.id.editText_question5);
			et_question6 = (EditText) findViewById(R.id.editText_question6);
			et_question7 = (EditText) findViewById(R.id.editText_question7);
			question1 = et_question1.getText().toString();
			question2 = et_question2.getText().toString();
			question3 = et_question3.getText().toString();
			question4 = et_question4.getText().toString();
			question5 = et_question5.getText().toString();
			question6 = et_question6.getText().toString();
			question7 = et_question7.getText().toString();
			sendResult += question1 + "," + question2 + "," + question3 + "," + question4 + "," + question5 + "," + question6
					+ "," + question7;
			break;
		case 8:
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			et_question2 = (EditText) findViewById(R.id.editText_question2);
			et_question3 = (EditText) findViewById(R.id.editText_question3);
			et_question4 = (EditText) findViewById(R.id.editText_question4);
			et_question5 = (EditText) findViewById(R.id.editText_question5);
			et_question6 = (EditText) findViewById(R.id.editText_question6);
			et_question7 = (EditText) findViewById(R.id.editText_question7);
			et_question8 = (EditText) findViewById(R.id.editText_question8);
			question1 = et_question1.getText().toString();
			question2 = et_question2.getText().toString();
			question3 = et_question3.getText().toString();
			question4 = et_question4.getText().toString();
			question5 = et_question5.getText().toString();
			question6 = et_question6.getText().toString();
			question7 = et_question7.getText().toString();
			question8 = et_question8.getText().toString();
			sendResult += question1 + "," + question2 + "," + question3 + "," + question4 + "," + question5 + "," + question6
					+ "," + question7 + "," + question8;
			break;
		case 9:
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			et_question2 = (EditText) findViewById(R.id.editText_question2);
			et_question3 = (EditText) findViewById(R.id.editText_question3);
			et_question4 = (EditText) findViewById(R.id.editText_question4);
			et_question5 = (EditText) findViewById(R.id.editText_question5);
			et_question6 = (EditText) findViewById(R.id.editText_question6);
			et_question7 = (EditText) findViewById(R.id.editText_question7);
			et_question8 = (EditText) findViewById(R.id.editText_question8);
			et_question9 = (EditText) findViewById(R.id.editText_question9);
			question1 = et_question1.getText().toString();
			question2 = et_question2.getText().toString();
			question3 = et_question3.getText().toString();
			question4 = et_question4.getText().toString();
			question5 = et_question5.getText().toString();
			question6 = et_question6.getText().toString();
			question7 = et_question7.getText().toString();
			question8 = et_question8.getText().toString();
			question9 = et_question9.getText().toString();
			sendResult += question1 + "," + question2 + "," + question3 + "," + question4 + "," + question5 + "," + question6
					+ "," + question7 + "," + question8 + "," + question9;
			break;
		case 10:
			et_question1 = (EditText) findViewById(R.id.editText_question1);
			et_question2 = (EditText) findViewById(R.id.editText_question2);
			et_question3 = (EditText) findViewById(R.id.editText_question3);
			et_question4 = (EditText) findViewById(R.id.editText_question4);
			et_question5 = (EditText) findViewById(R.id.editText_question5);
			et_question6 = (EditText) findViewById(R.id.editText_question6);
			et_question7 = (EditText) findViewById(R.id.editText_question7);
			et_question8 = (EditText) findViewById(R.id.editText_question8);
			et_question9 = (EditText) findViewById(R.id.editText_question9);
			et_question10 = (EditText) findViewById(R.id.editText_question10);
			question1 = et_question1.getText().toString();
			question2 = et_question2.getText().toString();
			question3 = et_question3.getText().toString();
			question4 = et_question4.getText().toString();
			question5 = et_question5.getText().toString();
			question6 = et_question6.getText().toString();
			question7 = et_question7.getText().toString();
			question8 = et_question8.getText().toString();
			question9 = et_question9.getText().toString();
			question10 = et_question10.getText().toString();
			sendResult += question1 + "," + question2 + "," + question3 + "," + question4 + "," + question5 + "," + question6
					+ "," + question7 + "," + question8 + "," + question9 + "," + question10;
			break;
		}

		Log.d("SessionResult", sendResult);

		String result = "";
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("new_survey", "1"));
			nameValuePairs.add(new BasicNameValuePair("sessionID", sessionID));
			nameValuePairs.add(new BasicNameValuePair("questions", sendResult));

			result = serveUtilities.getStringFromUrl(nameValuePairs);	
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!result.equals("good")) {
			Log.d("JoiningSession","Failed to join session");
		}
		else {
			Log.d("CreatedSurvey", "Created it correctly!");

			sessionType="submitSurvey";
			updateResults();
		}  
	}
	public void getSessionQuestions()
	{
		Log.d("SessionResult", "in get indvidual result");

		try{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("survey_questions", "1"));
			nameValuePairs.add(new BasicNameValuePair("sessionID", sessionID));

			jsonresults = serveUtilities.getStringFromUrl(nameValuePairs);		}
		catch(Exception e)
		{


		}
		try {
			jObject = new JSONArray(jsonresults);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Log.d("SessionResult", jObject.toString());
		try {
			nameList = jObject.getJSONObject(0).getString("questions");
			Log.e("tagger", nameList.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		values3 = nameList.split(",");
		Log.d("SessionResult", values3.toString());
		Log.d("SessionResult", values3[0].toString());

	}
	public void getIndvSurveyResult(String fn, String ln, String sID)
	{

		Log.d("SessionResult", "in get indvidual result");

		try{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("get_answers", "1"));
			nameValuePairs.add(new BasicNameValuePair("sessionID", sID));
			nameValuePairs.add(new BasicNameValuePair("lname", ln));
			nameValuePairs.add(new BasicNameValuePair("fname", fn));

			jsonresults = serveUtilities.getStringFromUrl(nameValuePairs);		}
		catch(Exception e)
		{


		}
		try {
			jObject = new JSONArray(jsonresults);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Log.d("SessionResult", jObject.toString());
		try {
			nameList = jObject.getJSONObject(0).getString("answers");
			Log.d("tagger", nameList.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		values2 = nameList.split(",");
		Log.d("SessionResult", values2.toString());
		Log.d("SessionResult", values2[0].toString());
		//Log.e("SessionResult", values2[1].toString());

		getSessionQuestions();

		//Log.e("SessionResult", "1");
		values4 = new String[values2.length];
		//Log.e("SessionResult", "2");
		for(int x = 0; x < values2.length ; x++)
		{
			values4[x] = values3[x].toString() + "\n " + values2[x].toString();
			Log.d("SessionResult values4 ", values4[x].toString());
		}

		//Log.e("SessionResult", "3");
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values4 );
		//Log.e("SessionResult", "4");

		results2.setAdapter(adapter2);
		results2.setItemsCanFocus(true);


		Log.d("SessionResult", "out of indv results");

	}
	OnItemClickListener listener = new OnItemClickListener () {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Log.d("sessionResult","clicked on a a person to view there survey results: "+values[arg2].toString());

			String names[];
			names = (values[arg2].toString()).split(" ");

			setContentView(R.layout.sessionresultsindsurvey);
			results2 = (ListView) findViewById(R.id.sessionResultsIndListView); 
			fname =(TextView) findViewById(R.id.tv_sessionResultsInd_fname);
			lname =(TextView) findViewById(R.id.tv_sessionResultsInd_lname);
			if (sessionPermType.equals("surveyannony")) {
				fname.setText("First Name: Annonymous");
				lname.setText("Last Name: User");
			}
			else {
				fname.setText("First Name: "+names[1].toString());
				lname.setText("Last Name: "+names[2].toString());
			}

			Log.d("sessionResult","names "+names[0].toString() +" . "+ names[1].toString()+ " . "+ names[2].toString()+ " "+sessionID.toString());
			getIndvSurveyResult(names[1],names[2],sessionID);
			
			handler.removeCallbacks(refresh);
		}
	};

}
