package edu.cs.fsu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class main extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView txt = (TextView) findViewById(R.id.app_name);  
		Typeface font = Typeface.createFromAsset(getAssets(), "Amorino_beta.ttf");  
		txt.setTypeface(font); 
	}
	
	public void createNewAccountClick(View target)
	{	
		Intent i = new Intent(this, edu.cs.fsu.createAccount.class);
		startActivity(i);
	}
	
	public void signInClick(View target)
	{	
		Intent i = new Intent(this, edu.cs.fsu.signIn.class);
		startActivity(i);
	}
}