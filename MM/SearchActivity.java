package com.coen268.moviemate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = MoviesActivity.class.getName();

    private static final String API_KEY = "24e5ade166fa5bca1990279da2746ba3";

    private static String query = "";

    /** URL for searching */
    private static final String SEARCH_MOVIE_URL =
            "https://api.themoviedb.org/3/search/movie?include_adult=false&query=" + query + "&language=en-US&api_key=" + API_KEY;

    List<Movie> movieList = new ArrayList<>();
    List<Long> movieIDs = new ArrayList<>();
    List<String> litImages = new ArrayList<>();

    Button nav;
    Intent intent;

    EditText searchBar;
    Button searchButton;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        intent = new Intent(this, NarBar.class);

        searchBar = (EditText) findViewById(R.id.search);
        searchButton = (Button) findViewById(R.id.btn_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new searchMovies().execute();
            }

        });


        nav = (Button) findViewById(R.id.nav_button);

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }

        });

    }

    //AsyncTask
    public class searchMovies extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... voids) {

            movieList = new ArrayList<>();

            if(networkStatus(SearchActivity.this)){
                query = searchBar.getText().toString();
                String url = "https://api.themoviedb.org/3/search/movie?api_key="+ API_KEY + "&language=en-US&query="+ query + "&page=1&include_adult=false";
                /** Get popular movies*/
                movieList = QueryUtils.fetchPopularMovieDate(url);
                /**Get all the posters from the movie list*/
                getPosterList(movieList);

            }else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(SearchActivity.this);
                dialog.setTitle("No network connection");
                dialog.setMessage("Haha");
                dialog.setCancelable(false);
                dialog.show();
            }
            return null;
        }

        private void getPosterList(List<Movie> movieList) {
            String  IMAGE_BASE_URI = "https://image.tmdb.org/t/p/w500/";
            for (Movie m : movieList) {
                litImages.add( IMAGE_BASE_URI + m.posterPath);
            }
        }

        public  Boolean networkStatus(Context context){
            ConnectivityManager manager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                return true;
            }
            return false;
        }


        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            //mProgressBar.setVisibility(View.INVISIBLE);
            //Load popular movies by default
            HorizontalInfiniteCycleViewPager pager = findViewById(R.id.search_cycle);
            MovieAdapter adapter = new MovieAdapter(litImages, movieList, getBaseContext(), user);
            pager.setAdapter(adapter);
        }
    }
}

