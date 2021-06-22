package com.coen268.moviemate;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.coen268.moviemate.data.MateContract;
import com.coen268.moviemate.data.MateProvider;
//import com.twitter.sdk.android.tweetui.SearchTimeline;
//import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_MOVIE_MATE_NAME;
import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_MOVIE_NAME;
import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_WATCH_MOVIE_MATE_NAME;
import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_WATCH_MOVIE_NAME;

public class MovieInfoActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener{

    public static final String LOG_TAG = MovieInfoActivity.class.getSimpleName();

    private static final String API_KEY = "24e5ade166fa5bca1990279da2746ba3";

    private static  final String YOUTUBE_API_KEY = "AIzaSyD7-0B_gZtm6E0365fm9_9tb-chkIdkubo";

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;

    private static String movie_id = "";

    Movie movie;
    Intent intent, secondIntent;
    ImageButton addWatchLater, addLikes, dislike;
    Button nav, watchNow;
    MateProvider mateProvider;
    TextView movieName, score, movieOverview;
    String name, user;
    ListView tweetsList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs, sharedPreferences;

    //Tweets t;
    //SearchTimeline searchTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_info);

        //t = new Tweets();

        intent = new Intent(this, NarBar.class);
        secondIntent = getIntent();

        name = secondIntent.getStringExtra("Movie_name");
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        secondIntent = getIntent();
        user = prefs.getString("username", secondIntent.getStringExtra("user_name"));

        mateProvider = new MateProvider();
        mateProvider.open(this);

        nav = (Button) findViewById(R.id.nav_button);

        addWatchLater = (ImageButton) findViewById(R.id.button_watch);

        if(!mateProvider.dubUpCheck(user, name)) {
            addLikes = (ImageButton) findViewById(R.id.button_heart);
        }
        else {
            addLikes = (ImageButton) findViewById(R.id.button_heart);
            addLikes.setBackgroundResource(R.drawable.fullheart);
        }

        dislike = (ImageButton) findViewById(R.id.button_dislike);
        //tweetsList = (ListView) findViewById(R.id.tweets_list);
        movieName = (TextView) findViewById(R.id.movie_name);
        movieName.setText(name);
        movieOverview = (TextView) findViewById(R.id.movie_info);

        //watchNow = (Button) findViewById(R.id.watch_now);

        score = (TextView) findViewById(R.id.score);
        sharedPreferences = getSharedPreferences("MOVIE_ID", MODE_PRIVATE);
        movie_id = sharedPreferences.getString("id", "1");

        /*
        searchTweets = t.SearchTweets(this, name);
        TweetTimelineListAdapter adapter =
                new TweetTimelineListAdapter.Builder(this)
                        .setTimeline(searchTweets)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                        .build();

        tweetsList.setAdapter(adapter);
        */

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }

        });

        addLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mateProvider.dubUpCheck(user, name)) {
                    addLikes.setBackgroundResource(R.drawable.heart);
                    String[] args = { user, name };
                    getContentResolver().delete(MateContract.MateEntry.CONTENT_URI2, COLUMN_MOVIE_MATE_NAME+ "=? AND " + COLUMN_MOVIE_NAME+ "=?", args);
                    Toast.makeText(getApplicationContext(), "Removed from likes!", Toast.LENGTH_SHORT).show();
                }
                else {
                    addLikes.setBackgroundResource(R.drawable.fullheart);
                    ContentValues values = new ContentValues();
                    values.put(MateContract.MateEntry.COLUMN_MOVIE_NAME, name);
                    values.put(MateContract.MateEntry.COLUMN_MOVIE_MATE_NAME, user);
                    Uri newUri = getContentResolver().insert(MateContract.MateEntry.CONTENT_URI2, values);
                    Toast.makeText(MovieInfoActivity.this, "Liked!",Toast.LENGTH_SHORT).show();
                }
            }

        });

        addWatchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mateProvider.dubUpWatchCheck(user, name)) {
                    String[] args = { user, name };
                    getContentResolver().delete(MateContract.MateEntry.CONTENT_URI3, COLUMN_WATCH_MOVIE_MATE_NAME+ "=? AND " + COLUMN_WATCH_MOVIE_NAME+ "=?", args);
                    Toast.makeText(getApplicationContext(), "Removed from Watch Later!", Toast.LENGTH_SHORT).show();
                }
                else {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_WATCH_MOVIE_NAME, name);
                    values.put(COLUMN_WATCH_MOVIE_MATE_NAME, user);
                    Uri newUri = getContentResolver().insert(MateContract.MateEntry.CONTENT_URI3, values);
                    Toast.makeText(MovieInfoActivity.this, "Added!",Toast.LENGTH_SHORT).show();
                }
            }

        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLikes.setBackgroundResource(R.drawable.heart);
                String[] args = { user, name };
                getContentResolver().delete(MateContract.MateEntry.CONTENT_URI2, COLUMN_MOVIE_MATE_NAME+ "=? AND " + COLUMN_MOVIE_NAME+ "=?", args);
                Toast.makeText(MovieInfoActivity.this, "Disliked!",Toast.LENGTH_SHORT).show();
            }

        });

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(YOUTUBE_API_KEY, this);

        /*
        watchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Movie isn't available right now!", Toast.LENGTH_SHORT).show();
            }
        });
        */

        new FetchTrailer().execute();

    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            //Log.i(LOG_TAG, "movie trailer is " + movie.trailerPath);
            if(movie != null) {
                player.cueVideo(movie.trailerPath);
            }
            else {
                player.cueVideo("https://www.youtube.com/watch?v=fhWaJi1Hsfo");
            }
             // Plays
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    //AsyncTask
    public class FetchTrailer extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(networkStatus(MovieInfoActivity.this)){
                /** URL for one movie data */
                String MOVIE_TRAILER_URL =
                        "https://api.themoviedb.org/3/movie/" + movie_id + "?api_key=" + API_KEY + "&append_to_response=videos";

                movie = QueryUtils.fetchMovieWithId(MOVIE_TRAILER_URL);
                Log.i(LOG_TAG, "The movie's info " + movie.id + " " + movie.title);

            }else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(MovieInfoActivity.this);
                dialog.setTitle("No network connection");
                dialog.setMessage("Haha");
                dialog.setCancelable(false);
                dialog.show();
            }
            return null;
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
            movieOverview.setText(movie.overview);
            movieName.setText(movie.title);
            score.setText(movie.score);
        }
    }

}



