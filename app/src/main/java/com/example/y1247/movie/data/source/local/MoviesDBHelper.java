package com.example.y1247.movie.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by y1247 on 2017/3/7.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movies.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String DOUBLE_TYPE = " DOUBlE";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MoviesPersistenceContract.MovieEntry.TABLE_NAME + " (" +
                    MoviesPersistenceContract.MovieEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + DOUBLE_TYPE + COMMA_SEP +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_POSTER_PATH + TEXT_TYPE + COMMA_SEP +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH + TEXT_TYPE + COMMA_SEP +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_POPULARITY + DOUBLE_TYPE + COMMA_SEP +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_RELEASE_DATE + TEXT_TYPE + COMMA_SEP +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_SAVE_FLAG + INTEGER_TYPE + COMMA_SEP +
                    MoviesPersistenceContract.MovieEntry.COLUMN_NAME_RUNTIME + INTEGER_TYPE +
                    " )";


    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DSF", "onCreate: " + SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
