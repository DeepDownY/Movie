package com.example.y1247.movie.movies;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.source.LoadSourceType;
import com.example.y1247.movie.data.source.LoaderProvider;
import com.example.y1247.movie.data.source.MoviesDataSource;
import com.example.y1247.movie.data.source.MoviesRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by y1247 on 2017/3/13.
 */

public class MoviesPresenter implements MoviesContract.Presenter,
        MoviesDataSource.GetMoviesCallback,
        LoaderManager.LoaderCallbacks<Cursor>,MoviesRepository.LoadDataCallback {

    public static String TAG = "DSF";

    public final static int MOVIES_LOADER = 1;

    private static boolean FIRSTLOAD = true;

    private int page = 1;

    private final MoviesContract.View mMoviesView;

    private final MoviesRepository moviesRepository;

    private final LoaderManager loaderManager;

    private final LoaderProvider loaderProvider;

    private MovieFilter movieFilter;

    private SortFilter sortFilter;

    public MoviesPresenter(MoviesContract.View mMoviesView, MoviesRepository moviesRepository,
                           LoaderManager loaderManager, LoaderProvider loaderProvider, MovieFilter movieFilter, SortFilter sortFilter) {
        this.mMoviesView = checkNotNull(mMoviesView);
        this.moviesRepository = checkNotNull(moviesRepository);
        this.loaderManager = checkNotNull(loaderManager);
        this.loaderProvider = checkNotNull(loaderProvider);
        this.movieFilter = checkNotNull(movieFilter);
        this.sortFilter = sortFilter;
        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        if (FIRSTLOAD) {
            //打开应用时进行内容更新
            moviesRepository.refreshAll();
            FIRSTLOAD = false;
        }
        loaderManager.initLoader(MOVIES_LOADER,null,this);
    }

    @Override
    public void loadMovies(LoadSourceType type) {
        mMoviesView.setLoadingIndicator(true);
        switch (type) {
            case POP:
                moviesRepository.getMovies(this,LoadSourceType.POP,page);
                break;
            case RATE:
                moviesRepository.getMovies(this,LoadSourceType.RATE,page);
                break;
            case LOCAL:
                loaderManager.initLoader(MOVIES_LOADER,null,this);
        }
    }

    @Override
    public void openMovieDetails(@NonNull Movie requestedMovie) {
        mMoviesView.showMoviesDetail(requestedMovie);
    }

    @Override
    public void setFiltering(MovieFilter movieFilter) {
        this.movieFilter = movieFilter;
        if (loaderManager.getLoader(MOVIES_LOADER) != null) {
            loaderManager.restartLoader(MOVIES_LOADER,null,this);
        } else {
            loaderManager.initLoader(MOVIES_LOADER,null,this);
        }

    }

    @Override
    public MoviesFilterType getFiltering() {
        return movieFilter.getMoviesFilterType();
    }

    @Override
    public void setSortType(SortFilter sortType) {
        this.sortFilter = sortType;
        loaderManager.restartLoader(MOVIES_LOADER,null,this);
    }

    @Override
    public void collectMovie(Movie movie) {
        Log.i(TAG, "collectMovie: ");
        moviesRepository.collectMovie(String.valueOf(movie.getId()));
    }

    @Override
    public void unCollectMovie(Movie movie) {
        Log.i(TAG, "unCollectMovie: ");
        moviesRepository.unCollectMovie(String.valueOf(movie.getId()));
    }

    @Override
    public SortFilter getSortType() {
        return this.sortFilter;
    }

    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        if (loaderManager.getLoader(MOVIES_LOADER) == null) {
            loaderManager.initLoader(MOVIES_LOADER,null,this);
        } else {
            loaderManager.restartLoader(MOVIES_LOADER,null,this);
        }
    }

    @Override
    public void onDataLoaded(List<Movie> data) {
        mMoviesView.setLoadingIndicator(false);
        mMoviesView.showMovies(data);
    }

    @Override
    public void onDataEmpty() {
        mMoviesView.setLoadingIndicator(false);
        mMoviesView.showMovies(null);
    }

    @Override
    public void onDataNotAvailable() {
        mMoviesView.setLoadingIndicator(false);
        mMoviesView.showLoadingMoviesError();
    }

    @Override
    public void onDataReset() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return loaderProvider.createFilteredMoviesLoader(movieFilter,sortFilter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            onDataLoaded(cursorToList(data));
        } else {
            onDataNotAvailable();
        }
    }

    private List<Movie> cursorToList(Cursor data) {
        List<Movie> ls = new ArrayList<>();
        while (data.moveToNext()) {
            Movie m = Movie.from(data);
            ls.add(m);
        }
        return ls;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        onDataReset();
    }

}
