package com.example.y1247.movie.data.source;

import android.content.ContentValues;

import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.source.local.MoviesPersistenceContract;

/**
 * Created by y1247 on 2017/3/8.
 */

public class MovieValues {
    public static ContentValues from(Movie movie){
        ContentValues values = new ContentValues();
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID,movie.getId());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_TITLE,movie.getTitle());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE,movie.getVote_average());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_POSTER_PATH,movie.getPoster_path());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH,movie.getBackdrop_path());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_OVERVIEW,movie.getOverview());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_POPULARITY,movie.getPopularity());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_RELEASE_DATE,movie.getRelease_date());
        return values;
    }
}
