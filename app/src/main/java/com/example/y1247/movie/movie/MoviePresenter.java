package com.example.y1247.movie.movie;

import android.util.Log;

import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.source.MoviesDataSource;
import com.example.y1247.movie.data.source.MoviesRepository;

/**
 * Created by y1247 on 2017/4/30.
 */

public class MoviePresenter implements MovieContract.Presenter {

    private static final String TAG = "MoviePresenter";

    Movie movie;

    MoviesDataSource dataSource ;

    MovieContract.View mMovieView;

    public MoviePresenter(Movie movie,MoviesDataSource dataSource, MovieContract.View mMovieView) {
        this.movie = movie;
        this.dataSource = dataSource;
        this.mMovieView = mMovieView;
    }

    @Override
    public void start() {

    }

    @Override
    public void collectMovie(Movie movie) {
        Log.i(TAG, "collectMovie: ");
    }

    @Override
    public void uncollectMovie(Movie movie) {
        Log.i(TAG, "uncollectMovie: ");
    }

    @Override
    public void loadMore(String id) {

    }
}
