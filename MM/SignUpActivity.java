package com.coen268.moviemate;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coen268.moviemate.data.MateContract.MateEntry;
import com.coen268.moviemate.data.MateProvider;

public class SignUpActivity extends AppCompatActivity {

    public static final String LOG_TAG = SignUpActivity.class.getSimpleName();

    EditText email;
    EditText username;
    EditText password1;
    EditText password2;
    Button signUp;
    Button login;
    MateProvider mateProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        email = findViewById(R.id.input_email);
        password1 = findViewById(R.id.input_password1);
        password2 = findViewById(R.id.input_password2);
        username = findViewById(R.id.input_username);

        signUp = findViewById(R.id.btn_signup);
        login = findViewById(R.id.link_login);

        mateProvider = new MateProvider();
        mateProvider.open(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password1.getText().toString().equals(password2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords should match", Toast.LENGTH_SHORT).show();
                }
                else if(mateProvider.dubUserCheck(username.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Username is taken!", Toast.LENGTH_SHORT).show();
                }
                else if(mateProvider.dubEmailCheck(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Email is taken!", Toast.LENGTH_SHORT).show();
                }
                else if(password1.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    insertPet();
                    finish();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            }
        });
    }

    private void insertPet() {
        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(MateEntry.COLUMN_MATE_NAME, username.getText().toString());
        values.put(MateEntry.COLUMN_MATE_EMAIL, email.getText().toString() );
        Log.i(LOG_TAG,  "password is " + password2.getText().toString());
        values.put(MateEntry.COLUMN_MATE_PASSWORD, password2.getText().toString());



        // Insert a new row into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // Receive the new content URI that will allow us to access the data in the future.
        Uri newUri = getContentResolver().insert(MateEntry.CONTENT_URI, values);
    }
}
