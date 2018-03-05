package com.example.y1247.movie.movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.y1247.movie.Injection;
import com.example.y1247.movie.R;
import com.example.y1247.movie.data.source.LoadSourceType;
import com.example.y1247.movie.data.source.LoaderProvider;
import com.example.y1247.movie.util.ActivityUtils;

public class MoviesActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private MoviesPresenter moviesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();


        MoviesFragment moviesFragment = (MoviesFragment) getFragmentManager().findFragmentById(R.id.content_Frame);
        if (moviesFragment == null) {
            moviesFragment = MoviesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getFragmentManager(),moviesFragment,R.id.content_Frame
            );
        }

        LoaderProvider loaderProvider = new LoaderProvider(this);

        MovieFilter movieFilter = MovieFilter.from(MoviesFilterType.ALL);
        if (savedInstanceState != null) {
            MoviesFilterType filterType = (MoviesFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            movieFilter = MovieFilter.from(filterType);
        }


        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        SortFilter sortFilter;
        if (setting != null) {

            int temp = setting.getInt("SortType",SortType.RATE.ordinal());

            if (temp == SortType.RATE.ordinal()) {
                sortFilter = SortFilter.from(SortType.RATE);
                moviesFragment.setLoadflag(LoadSourceType.RATE.ordinal());
            } else {
                sortFilter = SortFilter.from(SortType.POP);
                moviesFragment.setLoadflag(LoadSourceType.POP.ordinal());
            }
        } else {
            sortFilter = SortFilter.from(SortType.POP);
            moviesFragment.setLoadflag(LoadSourceType.POP.ordinal());
        }


        moviesPresenter = new MoviesPresenter(
                moviesFragment,
                Injection.provideMoviesRepository(getApplicationContext()),
                getLoaderManager(),
                loaderProvider,
                movieFilter,
                sortFilter
        );

        moviesPresenter.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY,moviesPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }
}
