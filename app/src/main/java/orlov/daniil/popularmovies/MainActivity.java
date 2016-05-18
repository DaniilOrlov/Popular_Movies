package orlov.daniil.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    //private final String LOG_TAG = MainActivity.class.getSimpleName();
    public String popular = "test";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {



                Log.v("popular ", getPopularMovies());

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id_of_pic", ImageAdapter.mThumbIds[position]);
                startActivity(intent);
            }
        });
    }

    private String getPopularMovies() {
        FetchMoviesTask popularMovies = new FetchMoviesTask();
        try {
            return popularMovies.execute("popular").get();
        } catch (InterruptedException e) {
            return "fail interrupted";
        } catch (ExecutionException ee) {
            return "fail execution";
        }

    }


}
