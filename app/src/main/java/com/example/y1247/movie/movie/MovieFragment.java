package com.example.y1247.movie.movie;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.y1247.movie.data.Movie;

/**
 * Created by y1247 on 2017/4/30.
 */

public class MovieFragment extends Fragment implements MovieContract.View {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setPresenter(MovieContract.Presenter presenter) {

    }

    @Override
    public void showMovie(Movie movie) {

    }

    @Override
    public void showMore() {

    }
}
