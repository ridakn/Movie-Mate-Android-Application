package com.coen268.moviemate;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving movie data from TMDB.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the dataset and return a list of  popular {@link Movie} objects.
     */
    public static List<Movie> fetchPopularMovieDate(String requestUrl) {
        // Create URL object
        URL url = createPopMovieUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        List<Movie> movies = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Movie}s
        return movies;
    }

    public static Movie fetchMovieWithId(String requestUrl) {
        // Create URL object
        URL url = createPopMovieUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        Movie movie = extractMovieFromJson(jsonResponse);

        // Return the list of {@link Movie}s
        return movie;
    }
    /**
     * Query the dataset and return a movie {@link Movie} object.
     */
    public static List<Movie> fetchSimilarMovie(String start, String query, String end) {
        // Create URL object
        URL url = createMovieUrl(start, query, end);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        List<Movie> movies = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Movie}s
        return movies;
    }

    public static List<Movie> fetchMovieDetails(String start, String query) {
        // Create URL object
        URL url = createMovieDetailsUrl(start, query);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        List<Movie> movies = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Movie}s
        return movies;
    }


    private static URL createMovieDetailsUrl(String stringUrl, String query) {
        URL url = null;
        try {
            url = new URL(stringUrl+query);
            Log.i(LOG_TAG, url.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createPopMovieUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createMovieUrl(String stringUrlStart, String query, String stringUrlEnd) {
        URL url = null;
        try {
            url = new URL(stringUrlStart+query+stringUrlEnd);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Movie} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Movie> extractFeatureFromJson(String movieJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movies to
        List<Movie> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(movieJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or movies).
            JSONArray movieArray = baseJsonResponse.getJSONArray("results");

            // For each movie in the movieArray, create an {@link Movie} object
            for (int i = 0; i < movieArray.length(); i++) {

                // Get a single movie at position i within the list of movies
                JSONObject currentMovie = movieArray.getJSONObject(i);

                long id = currentMovie.getLong("id");
                String title = currentMovie.getString("title");
                String posterPath = currentMovie.getString("poster_path");
                String overview = currentMovie.getString("overview");
                String releaseDate = currentMovie.getString("release_date");
                String score = currentMovie.getString("vote_average");

                String trailerPath = null;
                List<String> subscribeAvailable = null;

                // Create a new {@link Movie} object with the magnitude, location, time,
                // and url from the JSON response.
                Movie movie = new Movie(id, title, overview, releaseDate, posterPath, trailerPath, score, subscribeAvailable);

                // Add the new {@link Movie} to the list of movies.
                movies.add(movie);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }
        return movies;
    }

    /**
     * Return a list of {@link Movie} objects that has been built up from
     * parsing the given JSON response.
     */
    private static Movie extractMovieFromJson(String movieJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        Movie movie;

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            long id = baseJsonResponse.getLong("id");
            String title = baseJsonResponse.getString("title");
            String posterPath = baseJsonResponse.getString("poster_path");
            String overview = baseJsonResponse.getString("overview");
            String releaseDate = baseJsonResponse.getString("release_date");
            String score = baseJsonResponse.getString("vote_average");
            String trailerPath = null;
            List<String> subscribeAvailable = null;



            movie = new Movie(id, title, overview, releaseDate, posterPath, trailerPath, score, subscribeAvailable);

            JSONObject videoTag = baseJsonResponse.getJSONObject("videos");
            JSONArray videos = videoTag.getJSONArray("results");

            // Get a single movie at position i within the list of movies
            for (int i = 0; i < videos.length(); i++) {
                JSONObject currentVideo= videos.getJSONObject(i);
                String type = currentVideo.getString("type");
                String site = currentVideo.getString("site");
                if (type.equals("Trailer") && site.equals("YouTube")) {
                    //String videoId =  currentVideo.getString("id");
                    String key = currentVideo.getString("key");
                    movie.trailerPath = key;
                    break;
                }
            }

            return movie;

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
            return null;
        }

    }



}