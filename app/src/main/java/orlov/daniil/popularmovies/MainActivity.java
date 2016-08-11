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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPopularMovies();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        //FetchMoviesTask popularMovies = new FetchMoviesTask();
        //popularMovies.execute("popular");

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String picUrl = ImageAdapter.imagesURLsAdapter[position];
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, picUrl);
                startActivity(intent);
            }
        });
    }





    private void getPopularMovies() {
        FetchMoviesTask popularMovies = new FetchMoviesTask();
        try {
            popularMovies.execute("popular").get();
            //popularMovies.execute("popular");
        } catch (InterruptedException e) {
            Log.v("error", "fail interrupted");
        } catch (ExecutionException ee) {
            Log.v("error", "fail execution");
        }

    }


}
