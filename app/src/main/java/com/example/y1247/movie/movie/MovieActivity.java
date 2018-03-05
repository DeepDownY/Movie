package com.example.y1247.movie.movie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.y1247.movie.Injection;
import com.example.y1247.movie.R;
import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.util.ActivityUtils;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    public static String INTENT_EXTRA = "IntentExtra";

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_movie);

        final Movie movie = getIntent().getParcelableExtra(INTENT_EXTRA);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(movie.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MovieFragment fragment = new MovieFragment();
        ActivityUtils.addFragmentToActivity(getFragmentManager(),fragment,R.id.movie_content);
        final MoviePresenter mPresenter = new MoviePresenter(movie,
                Injection.provideMoviesRepository(this),fragment);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (movie.getSave_flag() == 1) {
            fab.setImageResource(R.drawable.star);
        } else fab.setImageResource(R.drawable.star_outline);

        Log.i("DSF", "onCreate: " + movie.getSave_flag());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie.getSave_flag() != 1) {
                    fab.setImageResource(R.drawable.star);
                    mPresenter.collectMovie(movie);
                    movie.setSave_flag(1);

                } else {
                    fab.setImageResource(R.drawable.star_outline);
                    mPresenter.unCollectMovie(movie);
                    movie.setSave_flag(0);
                }
            }
        });

        imageView = (ImageView) findViewById(R.id.iv_movieFirst);


        Picasso.with(this)
                .load(Movie.big_beginUrl + movie.getBackdrop_path())
                .into(imageView);

    }

    public static void StarActivity(Context context, Movie movie) {
        Intent intent = new Intent(context,MovieActivity.class);
        String transitionName = context.getString(R.string.transition_album_cover);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
//                albumCoverImageView,   // The view which starts the transition
//                transitionName    // The transitionName of the view we’re transitioning to
//        );
        intent.putExtra(INTENT_EXTRA,movie);
        context.startActivity(intent);
    }
}
