package com.example.y1247.movie.movie;

import com.example.y1247.movie.BasePresenter;
import com.example.y1247.movie.BaseView;
import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.Review;
import com.example.y1247.movie.data.Video;

import java.util.List;

/**
 * Created by y1247 on 2017/4/29.
 */

public class MovieContract {
    interface View extends BaseView<Presenter> {

        void showMovie(Movie movie);

        void showMoreVideos(List<Video> videos);

        void showMoreReviews(List<Review> reviews);

        void hideButton();
    }

    interface Presenter extends BasePresenter {

        void collectMovie(Movie movie);

        void unCollectMovie(Movie movie);

        void loadMoreVideos(Movie movie);

        void loadMoreReviews(Movie movie);

    }
}
