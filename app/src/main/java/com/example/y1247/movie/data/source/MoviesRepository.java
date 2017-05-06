package com.example.y1247.movie.data.source;

import android.support.annotation.NonNull;

import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.Review;
import com.example.y1247.movie.data.Video;

import java.util.List;

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by y1247 on 2017/3/12.
 */

public class MoviesRepository implements MoviesDataSource{
    private static MoviesRepository INSTANCE = null;

    private final MoviesDataSource mMoviesLocalDataSource;

    private final MoviesDataSource mMoviesRemoteDataSource;

    private MoviesRepository(@NonNull MoviesDataSource mMoviesRemoteDataSource,
                            @NonNull MoviesDataSource mMoviesLocalDataSource){
        this.mMoviesLocalDataSource = mMoviesLocalDataSource;
        this.mMoviesRemoteDataSource = mMoviesRemoteDataSource;
    }

    public static MoviesRepository getInstance(MoviesDataSource mMoviesRemoteDataSource,
                                                MoviesDataSource mMoviesLocalDataSource){
        if(INSTANCE == null){
            INSTANCE = new MoviesRepository(mMoviesRemoteDataSource,mMoviesLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getMovies(@NonNull final GetMoviesCallback callback, LoadSourceType extras, int page) {

        switch (extras){
            case LOCAL:
                break;
            case POP:
            case RATE:
                mMoviesRemoteDataSource.getMovies(new GetMoviesCallback() {
                    @Override
                    public void onMoviesLoaded(final List<Movie> movies) {
                        new Thread(){
                            @Override
                            public void run() {
                                refreshLocalDataSource(movies);
                            }
                        }.run();
                        callback.onMoviesLoaded(movies);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                },extras,page);
        }
    }

    private void refreshLocalDataSource(List<Movie> movies) {

        mMoviesLocalDataSource.saveMovies(movies);
    }

    @Override
    public void getMovie(@NonNull String id, @NonNull final GetMovieCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);

        mMoviesLocalDataSource.getMovie(id, new GetMovieCallback() {
            @Override
            public void onMovieLoaded(Movie movie) {
                callback.onMovieLoaded(movie);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveMovie(@NonNull Movie movie) {
        checkNotNull(movie);
        mMoviesLocalDataSource.saveMovie(movie);
    }

    @Override
    public void saveMovies(@NonNull List<Movie> movies) {
        checkNotNull(movies);
        mMoviesLocalDataSource.saveMovies(movies);
    }

    @Override
    public void collectMovie(@NonNull String id) {
        checkNotNull(id);
        mMoviesLocalDataSource.collectMovie(id);
    }

    @Override
    public void unCollectMovie(@NonNull String id) {
        checkNotNull(id);
        mMoviesLocalDataSource.unCollectMovie(id);
    }

    @Override
    public void deleteAllMovies() {
        mMoviesLocalDataSource.deleteAllMovies();
    }

    @Override
    public void refreshAll() {
        new Thread(){
            @Override
            public void run() {
//                Log.i("DSF", "run: ");
                mMoviesLocalDataSource.getMovies(new GetMoviesCallback() {
                    @Override
                    public void onMoviesLoaded(List<Movie> movies) {
                        for (final Movie m: movies) {
//                            Log.i("DSF", "onMoviesLoaded: " + m.getTitle());
                            mMoviesRemoteDataSource.getMovie(String.valueOf(m.getId()), new GetMovieCallback() {
                                @Override
                                public void onMovieLoaded(Movie movie) {
                                    m.setPopularity(movie.getPopularity());
                                    m.setVote_average(movie.getVote_average());

                                    m.setRuntime(movie.getRuntime());
//                                    Log.i("DSF", "onMovieLoaded: "+m.getRuntime());
                                    mMoviesLocalDataSource.saveMovie(m);
                                }

                                @Override
                                public void onDataNotAvailable() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                },null,0);
            }
        }.run();
    }

    @Override
    public void getReviews(@NonNull String id, @NonNull final GetReviewCallback callback) {
        mMoviesRemoteDataSource.getReviews(id, new GetReviewCallback() {
            @Override
            public void onReviewLoaded(List<Review> reviews) {
                callback.onReviewLoaded(reviews);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void getVideos(@NonNull String id, @NonNull final GetVideoCallback callback) {
        mMoviesRemoteDataSource.getVideos(id, new GetVideoCallback() {
            @Override
            public void onVideoLoaded(List<Video> videos) {
                callback.onVideoLoaded(videos);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    public interface LoadDataCallback {
        void onDataLoaded(List<Movie> data);

        void onDataEmpty();

        void onDataNotAvailable();

        void onDataReset();
    }
}
