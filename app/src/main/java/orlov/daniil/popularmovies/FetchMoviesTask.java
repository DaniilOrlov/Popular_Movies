package orlov.daniil.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchMoviesTask extends AsyncTask<String, Void, String> {

    private final String BASE_URL = "https://api.themoviedb.org/3/movie/",
            POPULAR_PATH = "popular",
            TOP_RATED_PATH = "top_rated",
            IMAGES_PATH = "images",
            API_KEY_PARAM = "api_key",
            API_KEY = "",
            LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String popularMovies = null;

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

            popularMovies = buffer.toString();

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

        /*try {
            return popularMovies;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }*/

        // This will only happen if there was an error getting or parsing the forecast.
        if(popularMovies.length() > 0) return popularMovies;
        else return null;


    }
    /*@Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Log.v("fetchPopular", result);
        }
    }*/
}
