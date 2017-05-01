package com.example.y1247.movie.movies;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.y1247.movie.BasePresenter;
import com.example.y1247.movie.BaseView;
import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.source.LoadSourceType;

import java.util.List;

/**
 * Created by y1247 on 2017/3/13.
 */

public interface MoviesContract {
    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);

        void showMovies(List<Movie> data);

        void showMoviesDetail(Movie movie);

        void showNoMovies();

        void showLoadingMoviesError();

        void showSuccessfullySavedMessage();

        void showFilteringPopUpMenu();

        void changeLoadFlag(LoadSourceType type);

        void showMessage(String msg);
    }
    interface Presenter extends BasePresenter{

        void loadMovies(LoadSourceType type);

        void openMovieDetails(@NonNull Movie requestedMovie);

        void setFiltering(MovieFilter movieFilter);

        MoviesFilterType getFiltering();

        void setSortType(SortFilter sortType);

        void collectMovie(Movie movie);

        void unCollectMovie(Movie movie);

        SortFilter getSortType();

    }
}
