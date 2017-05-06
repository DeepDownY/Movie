package com.example.y1247.movie.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.y1247.movie.data.source.local.MoviesPersistenceContract;

/**
 * Created by y1247 on 2017/3/7.
 */

public class Movie implements Parcelable{
    public static String beginUrl = "http://image.tmdb.org/t/p/w342";
    public static String big_beginUrl = "http://image.tmdb.org/t/p/w500";

    private int id;

    private String title;

    private double vote_average;

    private String poster_path;

    private String backdrop_path;

    private String overview;

    private double popularity;

    private String release_date;

    private int save_flag = 0;

    private int runtime;


    public Movie(){}

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        vote_average = in.readDouble();
        poster_path = in.readString();
        backdrop_path = in.readString();
        overview = in.readString();
        popularity = in.readDouble();
        release_date = in.readString();
        save_flag = in.readInt();
        runtime = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static Movie from(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_TITLE));
        double vote_average = cursor.getDouble(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE));
        String poster_path = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_POSTER_PATH));
        String backdrop_path = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH));
        String overview = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_OVERVIEW));
        double popularity = cursor.getDouble(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_POSTER_PATH));
        String release_date = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_RELEASE_DATE));
        int save_flag = cursor.getInt(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_SAVE_FLAG));
        int runtime = cursor.getInt(cursor.getColumnIndexOrThrow(
                MoviesPersistenceContract.MovieEntry.COLUMN_NAME_RUNTIME));
//        Log.i("DSF", "from: " + runtime);
        return new Movie(id,title,vote_average,poster_path,backdrop_path,
                overview,popularity,release_date,save_flag,runtime);
    }


    public Movie(int id, String title, double vote_average, String poster_path, String backdrop_path, String overview, double popularity, String release_date, int save_flag,int runtime) {
        this.id = id;
        this.title = title;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.popularity = popularity;
        this.release_date = release_date;
        this.save_flag = save_flag;
        this.runtime = runtime;
    }




    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getSave_flag() {
        return save_flag;
    }

    public void setSave_flag(int save_flag) {
        this.save_flag = save_flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(vote_average);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeDouble(popularity);
        dest.writeString(release_date);
        dest.writeInt(save_flag);
        dest.writeInt(runtime);
    }
}
