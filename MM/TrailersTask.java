//package com.coen268.moviemate;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TrailersTask extends AsyncTask<Long, Void, List<Movie>> {
//
//
//    private static final String API_KEY = "24e5ade166fa5bca1990279da2746ba3";
//
//
//    public static String LOG_TAG = TrailersTask.class.getSimpleName();
//
//    private final Listener mListener;
//
//    public TrailersTask(Listener mListener) {
//        this.mListener = mListener;
//    }
//
//    interface Listener {
//        void onLoadFinished(List<Movie> movies);
//    }
//
//    @Override
//    protected List<Movie> doInBackground(Long... params) {
//        List<Movie> trailers = new ArrayList<>();
//        // If there's no movie id, there's nothing to look up.
//        if (params.length == 0) {
//            return null;
//        }
//        long movie_id = params[0];
//
//        try {
//
//            /** URL for one movie data */
//            final String MOVIE_TRAILER_URL =
//                    "https://api.themoviedb.org/3/movie/" + movie_id + "?api_key=" + API_KEY + "&append_to_response=videos";
//
//            URL url;
//            url = new URL(MOVIE_TRAILER_URL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Opening a http connection  to the remote object
//            connection.connect();
//
//            InputStream inputStream = connection.getInputStream(); //reading from the object
//            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
//            parseJson(results,trailers);
//            inputStream.close();
//            return trailers;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return null;
//    }
//
//    public static void parseJson(String data, List<Movie> list){
//        Movie movie = new Movie();
//
//        try {
//            JSONObject mainObject = new JSONObject(data);
//            //Log.v(LOG_TAG,mainObject.toString());
//            JSONArray resArray = mainObject.getJSONArray("results"); //Getting the results object
//            for (int i = 0; i < resArray.length(); i++) {
//                JSONObject jsonObject = resArray.getJSONObject(i);
//                Movie.setmId(jsonObject.getString("id"));
//                Movie.setmKey(jsonObject.getString("key"));
//                trailers.setmName(jsonObject.getString("name"));
//                trailers.setmSite(jsonObject.getString("site"));
//                trailers.setmSize(jsonObject.getString("size"));
//                //Log.e(LOG_TAG, jsonObject.toString());
//                list.add(trailers);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e(LOG_TAG, "Error occurred during JSON Parsing");
//        }
//
//    }
//
//    @Override
//    protected void onPostExecute(List<Movie> trailers) {
//        if (trailers != null) {
//            mListener.onLoadFinished(trailers);
//        } else {
//            mListener.onLoadFinished(new ArrayList<Movie>());
//        }
//    }
//
//
//    private String readStream(InputStream in) {
//        BufferedReader reader = null;
//        StringBuffer response = new StringBuffer();
//        try {
//            reader = new BufferedReader(new InputStreamReader(in));
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return response.toString();
//    }
//}
