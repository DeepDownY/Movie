package com.example.y1247.movie.movie;

import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.source.MoviesDataSource;
import com.example.y1247.movie.data.Review;
import com.example.y1247.movie.data.Video;

import java.util.List;

/**
 * Created by y1247 on 2017/4/30.
 */

public class MoviePresenter implements MovieContract.Presenter,MoviesDataSource.GetReviewCallback,MoviesDataSource.GetVideoCallback{

    private static final String TAG = "MoviePresenter";

    private Movie movie;

    String id;

    private MoviesDataSource movieRepository ;

    private MovieContract.View mMovieView;

    public MoviePresenter(Movie movie,MoviesDataSource dataSource, MovieContract.View mMovieView) {
        this.movie = movie;
        this.movieRepository = dataSource;
        this.mMovieView = mMovieView;
        this.id = String.valueOf(movie.getId());
        mMovieView.setPresenter(this);
    }

    @Override
    public void start() {

        mMovieView.showMovie(movie);
    }

    @Override
    public void collectMovie(Movie movie) {
        movieRepository.collectMovie(id);
    }

    @Override
    public void unCollectMovie(Movie movie) {
        movieRepository.unCollectMovie(id);
    }

    @Override
    public void loadMoreVideos(Movie movie) {
        movieRepository.getVideos(String.valueOf(this.movie.getId()),this);
    }

    @Override
    public void loadMoreReviews(Movie movie) {
        movieRepository.getReviews(String.valueOf(this.movie.getId()),this);
    }

    @Override
    public void onReviewLoaded(List<Review> reviews) {
        mMovieView.showMoreReviews(reviews);
        mMovieView.hideButton();
    }

    @Override
    public void onVideoLoaded(List<Video> videos) {
        mMovieView.showMoreVideos(videos);
        mMovieView.hideButton();
    }

    @Override
    public void onDataNotAvailable() {

    }
}
