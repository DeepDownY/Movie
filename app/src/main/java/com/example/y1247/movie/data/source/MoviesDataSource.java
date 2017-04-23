package com.example.y1247.movie.data.source;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.y1247.movie.data.Movie;

import java.util.List;

/**
 * Created by y1247 on 2017/3/7.
 */

public interface MoviesDataSource {
    interface GetMoviesCallback{

        void onMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    interface GetMovieCallback{

        void onMovieLoaded(Movie movie);

        void onDataNotAvailable();
    }

    void getMovies(@NonNull GetMoviesCallback callback, LoadSourceType extras,int page);

    void getMovie(@NonNull String id,@NonNull GetMovieCallback callback);

    void saveMovie(@NonNull Movie movie);

    void saveMovies(@NonNull List<Movie> movies);

    void collectMovie(@NonNull String id);

    void unCollectMovie(@NonNull String id);

    void deleteAllMovies();

    void refreshAll();
}
