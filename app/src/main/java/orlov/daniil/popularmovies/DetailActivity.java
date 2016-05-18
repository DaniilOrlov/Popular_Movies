package orlov.daniil.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageView = (ImageView) findViewById(R.id.detail_image_view);
        //Intent intent = getIntent();
        int imageRef = getIntent().getIntExtra("id_of_pic", R.drawable.oops);
        imageView.setImageResource(imageRef);
    }
}
