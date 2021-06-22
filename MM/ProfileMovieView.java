package com.coen268.moviemate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

import com.coen268.moviemate.data.MateContract;
import com.coen268.moviemate.data.MateDbHelper;
import com.coen268.moviemate.data.MateProvider;

public class ProfileMovieView extends Activity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private MateProvider mate;
    private ListView list;
    private SimpleCursorAdapter adapter;
    private String name;
    Intent secondIntent;
    SharedPreferences prefs;

    final String[] from = new String[]{
            MateContract.MateEntry.COLUMN_MOVIE_NAME
    };

    final int[] to = new int[]{
            R.id.movie_row
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_user_list);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        secondIntent = getIntent();
        name = prefs.getString("username", secondIntent.getStringExtra("user_name"));
        mate = new MateProvider();
        mate.open(this);
        Cursor cursor = mate.getUserMovies(name);

        list = (ListView) findViewById(R.id.liked_movie_list);

        adapter = new SimpleCursorAdapter(this, R.layout.user_list_row, cursor,from, to, 0);

        list.setAdapter(adapter);

    }
}