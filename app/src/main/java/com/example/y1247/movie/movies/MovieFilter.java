package com.example.y1247.movie.movies;

import android.os.Bundle;
import android.support.compat.BuildConfig;

/**
 * Created by y1247 on 2017/3/8.
 */

public class MovieFilter {
    public final static String KEY_MOVIE_FILTER = BuildConfig.APPLICATION_ID + "MOVIE_FILTER";
    private MoviesFilterType moviesFilterType = MoviesFilterType.ALL;
    private Bundle filterExtras;

    protected MovieFilter(Bundle extras){
        this.filterExtras = extras;
        this.moviesFilterType = (MoviesFilterType) extras.getSerializable(KEY_MOVIE_FILTER);
    }

    public static MovieFilter from(MoviesFilterType moviesFilterType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MOVIE_FILTER,moviesFilterType);
        return  new MovieFilter(bundle);
    }


    public MoviesFilterType getMoviesFilterType() {
        return moviesFilterType;
    }

    public Bundle getFilterExtras(){
        return filterExtras;
    }
}
