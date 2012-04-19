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
	
	// var associated with JSON parsing
	private String nameList;
	private String url;
	private String jsonresults;
	private JSONArray jObject;
	
	//vars associated with TVs
	private String sessionID;
	private String sessionName;
	private String sessionType;
	private TextView id;
	private TextView name;
	private TextView type;
	
	private TextView fname;
	private TextView lname;
	
	private String sendResult;
	private EditText et_question1, et_question2, et_question3, et_question4, et_question5, et_question6, et_question7, et_question8, et_question9, et_question10;
	
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
        Log.e("tagger ssid",sessionID);
        name.setText("Session Name: "+ sessionName);
        id.setText("ID: " +sessionID);
        type.setText("Type: " +sessionType);


        updateResults();
	}
	public void updateResults()
	{
		Log.e("sessionResult","go into updateResults");
        
        if(sessionType.equals("attendance"))
        {
        	Log.e("sessionResult","go into attendance");
        	getSessionResults();    
        }
        else if(sessionType.equals("survey") )
        {
        	Log.e("sessionResult","go into survey");
        	setContentView(R.layout.sessionresultsurvey);			

		}
        else if(sessionType.equals("submitSurvey") )
        {
        	Log.e("sessionResult","go into submitSurvey");
        	setContentView(R.layout.sessionresults);
        	results = (ListView) findViewById(R.id.sessionResultsListView); 
             
            name = (TextView) findViewById(R.id.tv_results_sessionName);
            id = (TextView) findViewById(R.id.tv_results_sessionID);
            type = (TextView) findViewById(R.id.tv_results_sessionType);
     		final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
    		sessionID = app_preferences.getString("sessionID", "");
    		sessionType = "submitSurvey";

            Log.e("tagger ssid",sessionID);
            name.setText("Session Name: "+ sessionName);
            id.setText("ID: " +sessionID);
            type.setText("Type: Survey Answers");
        	getSessionResults();   
            results.setOnItemClickListener (listener);
            results.setItemsCanFocus(true);

        }
        else if(sessionType.equals("file") )
        {
        	Log.e("sessionResult","go into file");
        	
        }
        Log.e("sessionResult","Done Update Results");
	}
	public void getFileResults()
	{
		
		
	}
	public void getAnonSurveyResults()
	{
		
	}
	public void getSurveyResults()
	{
		
		
	}
	public void getSessionResults()
	{
		Log.e("sessionResult","called getSessionResults");
		url = String.format("http://www.fsurugby.org/serve/request.php?attendees=1&sessionID=%s", sessionID);
		Log.e("sessionResult sessionID",sessionID);
		
		 nameList = "Waiting for results... (empty)";
		
        try{
        	jsonresults = serveUtilities.getStringFromUrl(url);
        }
        catch(Exception e)
        {
        	Log.e("sessionResult","Failed to get from URL");
        	
        }
        
        
        try {
			jObject = new JSONArray(jsonresults);
			Log.e("sessionResult","got jsonArrayL");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
        
        Log.e("sessionResult jObject",jObject.toString());
        
       
        try {
        	nameList = jObject.getJSONObject(0).getString("attendees");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		Log.e("sessionResult namelist",nameList.toString());

		values = nameList.split(",");
		
		Log.e("sessionResult values",values.toString());
		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    			android.R.layout.simple_list_item_1, android.R.id.text1, values);
        
        results.setAdapter(adapter);
        Log.e("sessionResult","set adpater");
        
        Log.e("sessionResult","finished updating info");
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
		Log.e("sessionResult","in option bool");
	    switch (item.getItemId()) {
	        case R.id.menu_item_endsession:    Intent i = new Intent(this,edu.cs.fsu.sessionPicker.class); Log.e("sessionResult","end session");
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
	    
	    Log.e("sessionResult","out option bool");
	    return true;
	}

	public void createSurveyClick(View v)
	{
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
		
			// TODO Auto-generated method stub

			String question1 = et_question1.getText().toString().replace(" ", "%20");
			String question2 = et_question2.getText().toString().replace(" ", "%20");
			String question3 = et_question3.getText().toString().replace(" ", "%20");
			String question4 = et_question4.getText().toString().replace(" ", "%20");
			String question5 = et_question5.getText().toString().replace(" ", "%20");
			String question6 = et_question6.getText().toString().replace(" ", "%20");
			String question7 = et_question7.getText().toString().replace(" ", "%20");
			String question8 = et_question8.getText().toString().replace(" ", "%20");
			String question9 = et_question9.getText().toString().replace(" ", "%20");
			String question10 = et_question10.getText().toString().replace(" ", "%20");

			sendResult = "";

			if (!question1.equals(""))
				sendResult += question1;
			if (!question2.equals("")) {
				sendResult += ",";
				sendResult += question2;
			}
			if (!question3.equals("")) {
				sendResult += ",";
				sendResult += question3;
			}
			if (!question4.equals("")) {
				sendResult += ",";
				sendResult += question4;
			}
			if (!question5.equals("")) {
				sendResult += ",";
				sendResult += question5;
			}
			if (!question6.equals("")) {
				sendResult += ",";
				sendResult += question6;
			}
			if (!question7.equals("")) {
				sendResult += ",";
				sendResult += question7;
			}
			if (!question8.equals("")) {
				sendResult += ",";
				sendResult += question8;
			}
			if (!question9.equals("")) {
				sendResult += ",";
				sendResult += question9;
			}
			if (!question10.equals("")){ 
				sendResult += ",";
				sendResult += question10;
			}


			Log.e("SessionResult", sendResult);

			url = String.format("http://www.fsurugby.org/serve/request.php?new_survey=1&sessionID=%s&questions=%s", sessionID, sendResult);
			

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
				Log.e("JoiningSession","Failed to join session");
			}
			else {
				Log.e("CreatedSurvey", "Created it correctly!");
				
				sessionType="submitSurvey";
				updateResults();
			}  
		}
	public void getSessionQuestions()
	{
		url = String.format("http://www.fsurugby.org/serve/request.php?survey_questions=1&sessionID=%s",sessionID);
		
		Log.e("SessionResult", "in get indvidual result");
		
				try{
					jsonresults = serveUtilities.getStringFromUrl(url);
				}
				catch(Exception e)
				{
				

				}
				try {
					jObject = new JSONArray(jsonresults);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				Log.e("SessionResult", jObject.toString());
				try {
					nameList = jObject.getJSONObject(0).getString("questions");
					Log.e("tagger", nameList.toString());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				values3 = nameList.split(",");
				Log.e("SessionResult", values3.toString());
				Log.e("SessionResult", values3[0].toString());
		
	}
	public void getIndvSurveyResult(String fn, String ln, String sID)
	{
		
		Log.e("SessionResult", "in get indvidual result");
		url = String.format("http://www.fsurugby.org/serve/request.php?get_answers=1&sessionID=%s&fname=%s&lname=%s", sID, fn, ln);
		
				try{
					jsonresults = serveUtilities.getStringFromUrl(url);
				}
				catch(Exception e)
				{
				

				}
				try {
					jObject = new JSONArray(jsonresults);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				Log.e("SessionResult", jObject.toString());
				try {
					nameList = jObject.getJSONObject(0).getString("answers");
					Log.e("tagger", nameList.toString());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				values2 = nameList.split(",");
				Log.e("SessionResult", values2.toString());
				Log.e("SessionResult", values2[0].toString());
				//Log.e("SessionResult", values2[1].toString());
				
				getSessionQuestions();
				
				//Log.e("SessionResult", "1");
				values4 = new String[values2.length];
				//Log.e("SessionResult", "2");
				for(int x = 0; x < values2.length ; x++)
				{
					values4[x] = values3[x].toString() + "\n " + values2[x].toString();
					Log.e("SessionResult values4 ", values4[x].toString());
				}
				
				//Log.e("SessionResult", "3");
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, android.R.id.text1, values4 );
				//Log.e("SessionResult", "4");
								
				results2.setAdapter(adapter2);
		        results2.setItemsCanFocus(true);
		        
		        
		        Log.e("SessionResult", "out of indv results");
		
	}
	OnItemClickListener listener = new OnItemClickListener () {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Log.e("sessionResult","clicked on a a person tov iew there survey results: "+values[arg2].toString());
			//String names[] = {"poop","mcGee"};
			//sessionID="23435";
			String names[];
			names = (values[arg2].toString()).split(" ");
			
			setContentView(R.layout.sessionresultsindsurvey);
			results2 = (ListView) findViewById(R.id.sessionResultsIndListView); 
			 fname =(TextView) findViewById(R.id.tv_sessionResultsInd_fname);
			 lname =(TextView) findViewById(R.id.tv_sessionResultsInd_lname);
			 fname.setText("First Name: "+names[1].toString());
			 lname.setText("Last Name: "+names[2].toString());
			
			Log.e("sessionResult","names "+names[0].toString() +" . "+ names[1].toString()+ " . "+ names[2].toString()+ " "+sessionID.toString());
			getIndvSurveyResult(names[1],names[2],sessionID);
			}
		};
	
}


