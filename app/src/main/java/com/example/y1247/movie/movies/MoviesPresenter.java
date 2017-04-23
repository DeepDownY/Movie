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

import java.util.List;

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by y1247 on 2017/3/13.
 */

public class MoviesPresenter implements MoviesContract.Presenter,
        MoviesDataSource.GetMoviesCallback,
        LoaderManager.LoaderCallbacks<Cursor>,MoviesRepository.LoadDataCallback{

    public static String TAG = "DSF";

    public final static int MOVIES_LOADER = 1;

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
        Log.i(TAG, "MoviesPresenter: "+ sortFilter.getSortType());
        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loaderManager.initLoader(MOVIES_LOADER,null,this);
//        loadMovies(LoadSourceType.LOCAL);

    }

    @Override
    public void loadMovies(LoadSourceType type) {
        mMoviesView.setLoadingIndicator(true);
        switch (type){
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

    }

    @Override
    public void setFiltering(MovieFilter movieFilter) {
        this.movieFilter = movieFilter;
        if(loaderManager.getLoader(MOVIES_LOADER)!=null){
            loaderManager.restartLoader(MOVIES_LOADER,null,this);
        }else loaderManager.initLoader(MOVIES_LOADER,null,this);

//        loadMovies(LoadSourceType.LOCAL);
    }

    @Override
    public MoviesFilterType getFiltering() {
        return movieFilter.getMoviesFilterType();
    }

    @Override
    public void setSortType(SortFilter sortType) {
        this.sortFilter = sortType;
        Log.i(TAG, "setSortType: " + sortFilter.getSortType());
        loaderManager.restartLoader(MOVIES_LOADER,null,this);
    }

    @Override
    public void collectMovie(Movie movie) {
        moviesRepository.collectMovie(String.valueOf(movie.getId()));
    }

    @Override
    public SortFilter getSortType() {
        return this.sortFilter;
    }

    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        if(loaderManager.getLoader(MOVIES_LOADER)==null){
            loaderManager.initLoader(MOVIES_LOADER,null,this);
        }else{
            loaderManager.restartLoader(MOVIES_LOADER,null,this);
        }
    }

    @Override
    public void onDataLoaded(Cursor data) {
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
        Log.i(TAG, "onCreateLoader: " + movieFilter.getFilterExtras());
        return loaderProvider.createFilteredMoviesLoader(movieFilter,sortFilter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("DSF", "onLoadFinished: ");
        if(data != null){
            if(data.moveToLast()){
                onDataLoaded(data);
            }else {
                onDataEmpty();
            }
        }else {
            onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        onDataReset();
    }

}
