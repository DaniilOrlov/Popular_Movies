package orlov.daniil.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

    private final String BASE_URL = "https://api.themoviedb.org/3/movie/",
            POPULAR_PATH = "popular",
            TOP_RATED_PATH = "top_rated",
            IMAGES_PATH = "images",
            API_KEY_PARAM = "api_key",
            API_KEY = "",
            LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected String[] doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonString = null;

        try {
            //resulting API call should look like
            //https://api.themoviedb.org/3/movie/popular?api_key=API_KEY
            Uri builtUriToGetPopular = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();
            URL urlToGetPopular = new URL(builtUriToGetPopular.toString());

            // Create the request to get popular movies from mdb, and open the connection
            urlConnection = (HttpURLConnection) urlToGetPopular.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // adding a newline to make debugging easier
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            jsonString = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getUrlsFromJson(jsonString); //getting array of urls from json
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }
    private String[] getUrlsFromJson(String jsonString) throws JSONException{

        final String MDB_RESULTS = "results",
                     MDB_POSTER_PATH = "poster_path",
                     BASE_URL_PIC = "http://image.tmdb.org/t/p/w185";

        JSONObject moviesJson = new JSONObject(jsonString);
        JSONArray resultsArray = moviesJson.getJSONArray(MDB_RESULTS); //get JSONArray of movies data

        String[] urlPaths = new String[resultsArray.length()]; //this will store URLs of movie posters

        for(int i = 0; i < resultsArray.length(); i++) {
            JSONObject resultNumI = resultsArray.getJSONObject(i); //get info of particular movie
            //add URL of this movie poster to array of URLs
            //should look like this: http://image.tmdb.org/t/p/w185/cGOPbv9wA5gEejkUN892JrveARt.jpg
            urlPaths[i] = BASE_URL_PIC + resultNumI.getString(MDB_POSTER_PATH);
        }

        return urlPaths;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null) {
            /*mForecastAdapter.clear();
            for(String dayForecastStr : result) {
                mForecastAdapter.add(dayForecastStr);
            }*/
            //Log.v("popular ", Arrays.toString(result));
            /*for(String imgURL : result) {
                ImageAdapter.imagesURLs.add(imgURL);
            }
            Log.v("popular", ImageAdapter.imagesURLs.size() + " " + ImageAdapter.imagesURLs.toString());*/
            for(int i = 0; i < 20; i++) {
                MainActivity.imagesURLs[i] = result[i];
            }
            Log.v("popular ", Arrays.toString(MainActivity.imagesURLs));
        }
    }
}
