package com.example.y1247.movie.data.source;

import android.support.annotation.NonNull;

import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.Review;
import com.example.y1247.movie.data.Video;

import java.util.List;

/**
 * Created by y1247 on 2017/3/7.
 */

public interface MoviesDataSource {
    interface GetMoviesCallback {

        void onMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    interface GetMovieCallback {

        void onMovieLoaded(Movie movie);

        void onDataNotAvailable();
    }

    interface GetReviewCallback {

        void onReviewLoaded(List<Review> reviews);

        void onDataNotAvailable();
    }

    interface GetVideoCallback {

        void onVideoLoaded(List<Video> videos);

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

    void getReviews(@NonNull String id,@NonNull GetReviewCallback callback);

    void getVideos(@NonNull String id,@NonNull GetVideoCallback callback);
}
