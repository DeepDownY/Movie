package com.example.y1247.movie.data.source;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.example.y1247.movie.movies.MovieFilter;
import com.example.y1247.movie.data.source.local.MoviesPersistenceContract;
import com.example.y1247.movie.movies.SortFilter;
import com.example.y1247.movie.movies.SortType;

import java.io.Serializable;

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by y1247 on 2017/3/8.
 */

public class LoaderProvider implements Serializable {

    public static int flag = 0;



    @NonNull
    public Context getmContext() {
        return mContext;
    }

    @NonNull
    private final Context mContext;

    public LoaderProvider(@NonNull Context context) {
        this.mContext = checkNotNull(context, "context cannot be null");
    }

    public Loader<Cursor> createFilteredMoviesLoader(MovieFilter movieFilter, SortFilter sortFilter){
        String selection = null;
        String sortType = null;
        String[] selectionArgs = null;

        switch (sortFilter.getSortType()){
            case POP:
                sortType = MoviesPersistenceContract.MovieEntry.COLUMN_NAME_POPULARITY + "  DESC";
                break;
            case RATE:
                sortType = MoviesPersistenceContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + "  DESC";
                break;
        }

        switch (movieFilter.getMoviesFilterType()){
            case ALL:
                selection = null;
                selectionArgs = null;
                break;
            case COLLECTED:
                selection = MoviesPersistenceContract.MovieEntry.COLUMN_NAME_SAVE_FLAG + "= ? ";
                selectionArgs = new String[]{String.valueOf(1)};
                break;
        }

//        if(flag==0){
//            Looper.prepare();
//            flag = 1;
//        }
        return new CursorLoader(
                mContext,
                MoviesPersistenceContract.MovieEntry.buildMoviesUri(),
                MoviesPersistenceContract.MovieEntry.MOVIES_COLUMNS,selection,selectionArgs,sortType
        );
    }

    public Loader<Cursor> createMovieLoader(String movieId){
        return new CursorLoader(mContext, MoviesPersistenceContract.MovieEntry.buildMoviesUriWith(movieId),
                null,
                null,
                new String[]{String.valueOf(movieId)},null
        );
    }
}
