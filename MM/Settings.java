package com.coen268.moviemate;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coen268.moviemate.data.MateContract;
import com.coen268.moviemate.data.MateProvider;

public class Settings extends Activity {

    public static final String LOG_TAG = Settings.class.getSimpleName();

    EditText email;
    //EditText username;
    EditText password;
    Button update;
    Button nav;
    Intent intent;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    String user;
    MateProvider mateProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        email = (EditText) findViewById(R.id.update_email);
        //username = (EditText) findViewById(R.id.update_username);
        password = (EditText) findViewById(R.id.update_password);

        update = (Button) findViewById(R.id.btn_update);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user = prefs.getString("username", null);

        intent = new Intent(this, NarBar.class);

        mateProvider = new MateProvider();
        mateProvider.open(this);

        nav = (Button) findViewById(R.id.nav_button);

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }

        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    insertPet();
                    finish();
            }
        });
    }

    private void insertPet() {
        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(MateContract.MateEntry.COLUMN_MATE_EMAIL, email.getText().toString() );
        Log.i(LOG_TAG,  "password is " + password.getText().toString());
        values.put(MateContract.MateEntry.COLUMN_MATE_PASSWORD, password.getText().toString());
        getContentResolver().update(MateContract.MateEntry.CONTENT_URI, values, MateContract.MateEntry._ID, null);

        // Insert a new row into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // Receive the new content URI that will allow us to access the data in the future.
        //Uri newUri = getContentResolver().update(MateContract.MateEntry.CONTENT_URI, values, MateContract.MateEntry._ID, null);
    }
}
