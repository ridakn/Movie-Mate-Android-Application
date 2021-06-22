package com.coen268.moviemate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilePage extends Activity {

    Button watchLater, likedMovies;
    Button nav;
    Intent intent, viewLiked, viewWatchLater, secondIntent;
    TextView userNView;
    String name;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        secondIntent = getIntent();
        name = prefs.getString("username", secondIntent.getStringExtra("user_name"));

        userNView = (TextView) findViewById(R.id.username_view);
        userNView.setText(name);

        nav = (Button) findViewById(R.id.nav_button);

        watchLater = (Button) findViewById(R.id.watch_later);
        likedMovies = (Button) findViewById(R.id.liked_movies);

        viewLiked = new Intent(this, ProfileMovieView.class);
        viewWatchLater = new Intent(this, ProfileWatchView.class);
        viewWatchLater.putExtra("user_name", name);
        viewLiked.putExtra("user_name", name);

        intent = new Intent(this, NarBar.class);

        nav = (Button) findViewById(R.id.nav_button);

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }

        });

        watchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show user's watch later list
                //Toast.makeText(ProfilePage.this, "Watch Later",Toast.LENGTH_SHORT).show();
                startActivity(viewWatchLater);
            }
        });

        likedMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show user's likes list
                //Toast.makeText(ProfilePage.this, "Liked Movies",Toast.LENGTH_SHORT).show();
                startActivity(viewLiked);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        watchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show user's watch later list
                //Toast.makeText(ProfilePage.this, "Watch Later",Toast.LENGTH_SHORT).show();
                startActivity(viewWatchLater);
            }
        });

        likedMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show user's likes list
                //Toast.makeText(ProfilePage.this, "Liked Movies",Toast.LENGTH_SHORT).show();
                startActivity(viewLiked);
            }
        });

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }

        });
    }
}
