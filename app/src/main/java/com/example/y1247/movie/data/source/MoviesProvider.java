package com.example.y1247.movie.data.source;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.y1247.movie.data.source.local.MoviesDBHelper;
import com.example.y1247.movie.data.source.local.MoviesPersistenceContract;

/**
 * Created by y1247 on 2017/3/9.
 */

public class MoviesProvider extends ContentProvider {
    private static final int MOVIE = 100;
    private static final int MOVIE_ITEM = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper moviesDBHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesPersistenceContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesPersistenceContract.MovieEntry.TABLE_NAME,MOVIE);
        matcher.addURI(authority, MoviesPersistenceContract.MovieEntry.TABLE_NAME + "/*", MOVIE_ITEM);

        Log.i("DSF", "getBaseMovieUri: "+  authority);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        moviesDBHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor mCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                mCursor = moviesDBHelper.getReadableDatabase().query(
                        MoviesPersistenceContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_ITEM:
                String[] where = {uri.getLastPathSegment()};
                mCursor = moviesDBHelper.getReadableDatabase().query(
                        MoviesPersistenceContract.MovieEntry.TABLE_NAME,
                        projection,
                        MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID + " = ?",
                        where,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        mCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                return MoviesPersistenceContract.CONTENT_MOVIE_TYPE;
            case MOVIE_ITEM:
                return MoviesPersistenceContract.CONTENT_MOVIE_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIE:
                Cursor exists = db.query(
                        MoviesPersistenceContract.MovieEntry.TABLE_NAME,
                        new String[]{MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID},
                        MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID + " = ?",
                        new String[]{values.getAsString(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID)},
                        null,
                        null,
                        null
                );

                if (exists.moveToLast()){
                    long _id = db.update(
                            MoviesPersistenceContract.MovieEntry.TABLE_NAME,
                            values,
                            MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID + " = ?",
                            new String[]{values.getAsString(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID)}
                    );
                    if(_id > 0){
                        returnUri = MoviesPersistenceContract.MovieEntry.buildMoviesUriWith(_id);
                    }else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                } else {
                    long _id = db.insert(MoviesPersistenceContract.MovieEntry.TABLE_NAME,null,values);
                    if(_id > 0){
                        returnUri = MoviesPersistenceContract.MovieEntry.buildMoviesUriWith(_id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                exists.close();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case MOVIE:
                rowsDeleted = db.delete(
                        MoviesPersistenceContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MoviesPersistenceContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
