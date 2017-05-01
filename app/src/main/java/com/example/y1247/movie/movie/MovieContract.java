package com.example.y1247.movie.movie;

import com.example.y1247.movie.BasePresenter;
import com.example.y1247.movie.BaseView;
import com.example.y1247.movie.data.Movie;

/**
 * Created by y1247 on 2017/4/29.
 */

public class MovieContract {
    interface View extends BaseView<Presenter>{

        void showMovie(Movie movie);

        void showMore();
    }

    interface Presenter extends BasePresenter{

        void collectMovie(Movie movie);

        void uncollectMovie(Movie movie);

        void loadMore(String id);
    }
}
