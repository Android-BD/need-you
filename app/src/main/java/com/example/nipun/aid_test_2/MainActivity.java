package com.example.nipun.aid_test_2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadSavedPreferences();
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean userCredentials = sharedPreferences.getBoolean("hasLoggedIn", false);
        if (userCredentials) {
	    //Launch background serive to capture triggers
            Intent launchBackgroundServiceObj = new Intent( this, ListenUserInBackground.class );
            startService( launchBackgroundServiceObj );
            this.finish();
        }
    }

    private void savePreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("hasLoggedIn", true);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRegister( View view ) {
        EditText userNumber = ( EditText )findViewById(R.id.userNumber );
        EditText password = ( EditText )findViewById(R.id.password);

        if (userNumber.getText().toString().equals("9968211049") && password.getText().toString().equals("Nipun")) {

	    //save preferences for registeration of the user
            savePreferences();

	    //Launch background serive to capture triggers
            Intent launchBackgroundServiceObj = new Intent( this, ListenUserInBackground.class );
            startService( launchBackgroundServiceObj );
            this.finish();
        }
    }
}


