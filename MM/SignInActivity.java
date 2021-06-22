package com.coen268.moviemate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.coen268.moviemate.data.MateContract.MateEntry;



public class SignInActivity extends AppCompatActivity {

    public static boolean isAppRunning;

    EditText username;
    EditText password;
    Button logIn;
    Button signUp;
    SharedPreferences.Editor editor;
    SharedPreferences settings;
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        logIn = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.login_signup);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        settings = getSharedPreferences("mySharedPref", 0);
        if (settings.getBoolean("connected", false)) {
            /* The user has already login, so start the dashboard */
            startActivity(new Intent(getApplicationContext(), MoviesActivity.class));
        }


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExist = checkUserExist(username.getText().toString(), password.getText().toString());

                if(isExist){
                    SharedPreferences.Editor editor2 = settings.edit();
                    editor.putBoolean("connected", true);
                    editor.commit();
                    editor.putString("username", username.getText().toString());
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MoviesActivity.class);
                    intent.putExtra("user_name", username.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    MoviesActivity.isUserSignedIn = true;
                    startActivity(intent);
                } else {
                    password.setText(null);
                    Toast.makeText(getApplicationContext(), "Login failed. Invalid username or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    //intent.putExtra("username", username.getText().toString());
                    startActivity(intent);
            }

        });
    }

    public boolean checkUserExist(String username, String password) {
        String[] projection = {
                MateEntry.COLUMN_MATE_NAME,
                MateEntry.COLUMN_MATE_PASSWORD,
        };

        String selection = "name=? and password = ?";
        String[] selectionArgs = {username, password};

        // Perform a query on the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to access the pet data.
        Cursor cursor = getContentResolver().query(
                MateEntry.CONTENT_URI,   // The content URI of the words table
                projection,             // The columns to return for each row
                selection,                   // Selection criteria
                selectionArgs,                   // Selection criteria
                null);                  // The sort order for the returned rows
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppRunning = false;
    }

}
