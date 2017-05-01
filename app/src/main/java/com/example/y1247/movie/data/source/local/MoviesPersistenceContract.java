package com.example.y1247.movie.data.source.local;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.compat.BuildConfig;
import android.util.Log;

/**
 * Created by y1247 on 2017/3/7.
 */

public class MoviesPersistenceContract {

    public static final String CONTENT_AUTHORITY = "com.example.y1247.movie";
    public static final String CONTENT_MOVIE_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + MovieEntry.TABLE_NAME;
    public static final String CONTENT_MOVIE_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + MovieEntry.TABLE_NAME;
    public static final String VND_ANDROID_CURSOR_ITEM_VND = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);
    private static final String VND_ANDROID_CURSOR_DIR_VND = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".";
    private static final String SEPARATOR = "/";

    private MoviesPersistenceContract() {}

    public static Uri getBaseMovieUri(String movieId) {


        return Uri.parse(CONTENT_SCHEME + CONTENT_MOVIE_ITEM_TYPE + SEPARATOR + movieId);
    }

    public static abstract class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_POPULARITY = "popularity";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_SAVE_FLAG = "save_flag";
        public static final String COLUMN_NAME_RUNTIME = "runtime";
        public static final Uri CONTENT_MOVIE_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static String[] MOVIES_COLUMNS = new String[]{
                MovieEntry._ID,
                MovieEntry.COLUMN_NAME_ID,
                MovieEntry.COLUMN_NAME_TITLE,
                MovieEntry.COLUMN_NAME_VOTE_AVERAGE,
                MovieEntry.COLUMN_NAME_POSTER_PATH,
                MovieEntry.COLUMN_NAME_BACKDROP_PATH,
                MovieEntry.COLUMN_NAME_OVERVIEW,
                MovieEntry.COLUMN_NAME_POPULARITY,
                MovieEntry.COLUMN_NAME_RELEASE_DATE,
                MovieEntry.COLUMN_NAME_SAVE_FLAG,
                MovieEntry.COLUMN_NAME_RUNTIME
        };

        public static  Uri buildMoviesUriWith(long id){
            return ContentUris.withAppendedId(CONTENT_MOVIE_URI,id);
        }

        public static Uri buildMoviesUriWith(String id){
            Uri uri = CONTENT_MOVIE_URI.buildUpon().appendPath(id).build();
            return uri;
        }

        public static Uri buildMoviesUri(){

            return CONTENT_MOVIE_URI.buildUpon().build();
        }
    }
}
