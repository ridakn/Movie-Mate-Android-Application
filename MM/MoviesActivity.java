package com.coen268.moviemate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.coen268.moviemate.data.MateProvider;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.List;

public class MoviesActivity extends AppCompatActivity {

    public static boolean isUserSignedIn;

    private static final String LOG_TAG = MoviesActivity.class.getName();

    private static final String API_KEY = "24e5ade166fa5bca1990279da2746ba3";

    private static Long movie_id;
    private static String query;

    /** URL for popular movies data */
    private static final String POPULAR_MOIVES_URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;

    /** URL for similar movies data */
    private static final String SIMILAR_MOIVES_URL_START = "https://api.themoviedb.org/3/movie/";

    private static final String SIMILAR_MOIVES_URL_END = "/similar?api_key=" + API_KEY + "&language=en-US&page=1";

    private static final String SEARCH_DETAILS_URL = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=";

    public static final String MY_PREFS_NAME = "MyPrefsFile";


    List<Movie> queryMovie = new ArrayList<>();
    List<Movie> movieList = new ArrayList<>();
    List<Long> moviesID = new ArrayList<>();
    List<String> litImages = new ArrayList<>();

    Button nav;
    Intent intent, secondIntent;
    String username;
    MateProvider mate;
    Movie movieInstance;
    String movieQuery;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isUserSignedIn = true;

        mate = new MateProvider();
        mate.open(this);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        secondIntent = getIntent();
        username = prefs.getString("username", secondIntent.getStringExtra("user_name"));
        intent = new Intent(this, NarBar.class);
        intent.putExtra("user_name", username);

        nav = (Button) findViewById(R.id.nav_button);

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }

        });

        new FetchMovies().execute();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }



    //AsyncTask
    public class FetchMovies extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... voids) {

            movieList = new ArrayList<>();

            if(networkStatus(MoviesActivity.this)){

                movieQuery = mate.getUserMovieName(username);
                if (movieQuery != null) {
                    query = movieQuery;
                    queryMovie = QueryUtils.fetchMovieDetails(SEARCH_DETAILS_URL, java.net.URLEncoder.encode(query));
                    movie_id = queryMovie.get(0).id;
                    movieList = QueryUtils.fetchSimilarMovie(SIMILAR_MOIVES_URL_START, movie_id.toString(), SIMILAR_MOIVES_URL_END);
                    getPosterList(movieList);
                    Log.i(LOG_TAG, SIMILAR_MOIVES_URL_START+movie_id+SIMILAR_MOIVES_URL_END);
                }
                else {
                    /** Get popular movies*/
                    movieList = QueryUtils.fetchPopularMovieDate(POPULAR_MOIVES_URL);
                    /**Get all the posters from the movie list*/
                    getPosterList(movieList);
                }

            }else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(MoviesActivity.this);
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
            HorizontalInfiniteCycleViewPager pager = findViewById(R.id.horiontal_cycle);
            MovieAdapter adapter = new MovieAdapter(litImages, movieList, getBaseContext(), username);
            pager.setAdapter(adapter);
        }
    }

}

