package com.example.y1247.movie.data.source.remote;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.y1247.movie.BuildConfig;
import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.Review;
import com.example.y1247.movie.data.Video;
import com.example.y1247.movie.data.source.LoadSourceType;
import com.example.y1247.movie.data.source.MoviesDataSource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by y1247 on 2017/3/11.
 */

public class MoviesRemoteDataSource implements MoviesDataSource {
    private static final String API_KEY = BuildConfig.APIKEY;
    private static final String URL = "https://api.themoviedb.org/3/movie/";
    private static final String POP = "popular";
    private static final String RATE = "top_rated";

    private static final int TIME_OUT = 10;

    private final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT,TimeUnit.SECONDS)
                .readTimeout(TIME_OUT,TimeUnit.SECONDS)
                .build();

    private static MoviesRemoteDataSource INSTANCE;

    private MoviesRemoteDataSource() {
    }

    /**
     * 获取单例
     * @return 单例远程仓库
     */
    public static MoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(@NonNull GetMoviesCallback callback, LoadSourceType extras,int page) {
        String url = null;
        switch (extras) {
            case POP:
                url = URL + POP
                        + "?page=" + String.valueOf(page)
                        + "&api_key=" + API_KEY;
                break;
            case RATE:
                url = URL + RATE + "?page=" + String.valueOf(page) + "&api_key=" + API_KEY;
                break;
            case LOCAL:
                return;
        }
        getJsonFromServer(callback,url);
    }

    @Override
    public void getMovie(@NonNull String id, @NonNull final GetMovieCallback callback) {
        String url = URL + id + "?api_key=" + API_KEY;


        final Movie m = new Movie();
        Request request = new Request.Builder().url(url).build();
        MovieHandler handler = new MovieHandler(callback);

        final Message msg = handler.obtainMessage();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                msg.what = -1;
                msg.sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    m.setVote_average(jsonObject.getDouble("vote_average"));
                    m.setPopularity(jsonObject.getDouble("popularity"));
                    m.setRuntime(jsonObject.getInt("runtime"));

                    msg.obj = m;
                    msg.what = 1;
                    msg.sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void saveMovie(@NonNull Movie movie) {

    }

    @Override
    public void saveMovies(@NonNull List<Movie> movies) {

    }

    @Override
    public void collectMovie(@NonNull String id) {

    }

    @Override
    public void unCollectMovie(@NonNull String id) {

    }

    @Override
    public void deleteAllMovies() {

    }

    @Override
    public void refreshAll() {

    }

    @Override
    public void getReviews(@NonNull String id, @NonNull GetReviewCallback callback) {
        String url = URL + id + "/reviews?api_key=" + API_KEY;
        getJsonFromServer(callback,url);
    }

    @Override
    public void getVideos(@NonNull String id, @NonNull GetVideoCallback callback) {
        String url = URL + id + "/videos?api_key=" + API_KEY;
        getJsonFromServer(callback,url);
    }

    private void getJsonFromServer(final GetReviewCallback callback, String url) {
        Request request = new Request.Builder().url(url).build();

        ReviewHandler handler = new ReviewHandler(callback);

        final Message msg = handler.obtainMessage();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                msg.what = -1;
                msg.sendToTarget();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    msg.obj = JsonToReviewList(jsonObject);
                    msg.what = 1;
                    msg.sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getJsonFromServer(final GetMoviesCallback callback, String url) {
        Request request = new Request.Builder().url(url).build();

        MoviesHandler handler = new MoviesHandler(callback);

        final Message msg = handler.obtainMessage();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                msg.what = -1;
                msg.sendToTarget();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    msg.obj = JsonToList(jsonObject);
                    msg.what = 1;
                    msg.sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getJsonFromServer(final GetVideoCallback callback, String url) {
        Request request = new Request.Builder().url(url).build();

        VideoHandler handler = new VideoHandler(callback);

        final Message msg = handler.obtainMessage();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                msg.what = -1;
                msg.sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    msg.obj = JsonToVideoList(jsonObject);
                    msg.what = 1;
                    msg.sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private List<Movie> JsonToList(JSONObject json) {
        List<Movie> ls;
        JSONArray jsonArray;

        Gson gson = new Gson();
        Type it = new TypeToken<List<Movie>>(){}.getType();
        try {
            jsonArray = json.getJSONArray("results");
            ls = gson.fromJson(jsonArray.toString(),it);
            return ls;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private List<Review> JsonToReviewList(JSONObject json) {
        List<Review> ls;
        JSONArray jsonArray;

        Gson gson = new Gson();
        Type it = new TypeToken<List<Review>>(){}.getType();
        try {
            jsonArray = json.getJSONArray("results");
            ls = gson.fromJson(jsonArray.toString(),it);
            return ls;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Video> JsonToVideoList(JSONObject json) {
        List<Video> ls;
        JSONArray jsonArray;

        Gson gson = new Gson();
        Type it = new TypeToken<List<Video>>(){}.getType();
        try {
            jsonArray = json.getJSONArray("results");
            ls = gson.fromJson(jsonArray.toString(),it);
            return ls;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class VideoHandler extends Handler {

        SoftReference<GetVideoCallback> reference ;

        VideoHandler(GetVideoCallback callback) {
            this.reference = new SoftReference<>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    reference.get().onDataNotAvailable();
                    break;
                case 1:
                    reference.get().onVideoLoaded((List<Video>) msg.obj);
                    break;
            }
        }
    }

    private static class ReviewHandler extends Handler {

        SoftReference<GetReviewCallback> reference ;

        ReviewHandler(GetReviewCallback callback) {
            this.reference = new SoftReference<GetReviewCallback>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    reference.get().onDataNotAvailable();
                    break;
                case 1:
                    reference.get().onReviewLoaded((List<Review>) msg.obj);
                    break;
            }
        }
    }

    private static class MoviesHandler extends Handler {

        SoftReference<GetMoviesCallback> reference ;

        MoviesHandler(GetMoviesCallback callback) {
            this.reference = new SoftReference<GetMoviesCallback>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    reference.get().onDataNotAvailable();
                    break;
                case 1:
                    reference.get().onMoviesLoaded((List<Movie>) msg.obj);
                    break;
            }
        }
    }

    private static class MovieHandler extends Handler {

        GetMovieCallback callback ;

        MovieHandler(GetMovieCallback callback) {
            this.callback = callback;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    callback.onDataNotAvailable();
                    break;
                case 1:
                    callback.onMovieLoaded((Movie) msg.obj);
                    break;
            }
        }
    }

}
