package com.example.y1247.movie.movie;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.y1247.movie.R;
import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.Review;
import com.example.y1247.movie.data.Video;
import com.example.y1247.movie.util.ReviewView;
import com.example.y1247.movie.util.VideoView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by y1247 on 2017/4/30.
 */

public class MovieFragment extends Fragment implements MovieContract.View {

    MovieContract.Presenter presenter;

    String temp = "/10";

    Movie movie;
    ImageView iv_moviePic;
    TextView tv_movieDate;
    TextView tv_movieRate;
    TextView tv_introduce;
    TextView tv_runtime;

    Button btn_ShowMore;

    LinearLayout ll_review,ll_videos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movie_fragment,container,false);

        iv_moviePic = (ImageView) root.findViewById(R.id.iv_movieInfo);
        tv_introduce = (TextView) root.findViewById(R.id.tv_movieIntroduce);
        tv_movieDate = (TextView) root.findViewById(R.id.tv_movieDate);
        tv_movieRate = (TextView) root.findViewById(R.id.tv_movieRate);
        tv_runtime = (TextView) root.findViewById(R.id.tv_movieRuntime);

        ll_review = (LinearLayout) root.findViewById(R.id.ll_reviews);
        ll_videos = (LinearLayout) root.findViewById(R.id.ll_videos);

        btn_ShowMore = (Button) root.findViewById(R.id.btn_showMore);

        btn_ShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadMoreReviews(movie);
                presenter.loadMoreVideos(movie);
            }
        });

        presenter.start();

        return root;
    }

    @Override
    public void setPresenter(MovieContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showMovie(Movie movie) {

        tv_introduce.setText(movie.getOverview());
        tv_movieDate.setText(movie.getRelease_date());
        tv_movieRate.setText(movie.getVote_average() + temp);
        if (movie.getRuntime() != 0) {
            tv_runtime.setText(String.valueOf(movie.getRuntime())+ " minute");
        } else tv_runtime.setText(" ");

        Picasso.with(getActivity())
                .load(Movie.beginUrl + movie.getPoster_path())
                .into(iv_moviePic);
    }

    @Override
    public void showMoreVideos(List<Video> videos) {
        for (Video v:videos) {
            VideoView v_temp = new VideoView(getActivity());
            ll_videos.addView(v_temp);
        }
    }

    @Override
    public void showMoreReviews(List<Review> reviews) {
        if (getActivity() == null) {
            return;
        }
        for (Review re:reviews) {
            ReviewView r = new ReviewView(getActivity());
            r.setReview(re);
            ll_review.addView(r);
            ll_review.setZ(5);
        }
    }

    @Override
    public void hideButton() {
        btn_ShowMore.setVisibility(View.INVISIBLE);
    }


}
