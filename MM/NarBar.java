package com.coen268.moviemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class NarBar extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    Intent secondIntent, goToHome, goSettings, goToProfile, goToSearch, loggedOut;
    String userName;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        secondIntent = getIntent();
        userName = prefs.getString("username", secondIntent.getStringExtra("user_name"));
        goToHome = new Intent(this, MoviesActivity.class);
        goSettings = new Intent(this, Settings.class);
        loggedOut = new Intent(this, SignInActivity.class);
        goToProfile = new Intent(this, ProfilePage.class);
        goToSearch = new Intent(this, SearchActivity.class);

        goToProfile.putExtra("user_name", userName);

        dl = (DrawerLayout)findViewById(R.id.drawer);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.search_option:
                        startActivity(goToSearch);
                        break;
                    case R.id.discover:
                        goToHome.putExtra("user_name", userName);
                        startActivity(goToHome);
                        break;
                    case R.id.account:
                        goToProfile.putExtra("user_name", userName);
                        startActivity(goToProfile);
                        //Toast.makeText(NarBar.this, "My Account",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(goSettings);
                        //Toast.makeText(NarBar.this, "Settings",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        loggedOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        MoviesActivity.isUserSignedIn = false;
                        startActivity(loggedOut);
                        break;
                    default:
                        return true;
                }

                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
