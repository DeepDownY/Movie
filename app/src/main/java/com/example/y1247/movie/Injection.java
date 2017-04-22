package com.example.y1247.movie;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.y1247.movie.data.source.MoviesDataSource;
import com.example.y1247.movie.data.source.MoviesRepository;
import com.example.y1247.movie.data.source.local.MoviesLocalDataSource;
import com.example.y1247.movie.data.source.remote.MoviesRemoteDataSource;

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by y1247 on 2017/3/12.
 */

public class Injection {
    public static MoviesRepository provideMoviesRepository(@NonNull Context context){
        checkNotNull(context);
        return MoviesRepository.getInstance(
                provideRemoteDataSource(),
                provideLocalDataSource(context)
        );
    }

    public static MoviesDataSource provideRemoteDataSource(){
        return MoviesRemoteDataSource.getInstance();
    }

    public static MoviesLocalDataSource provideLocalDataSource(@NonNull Context context){
        checkNotNull(context);
        return MoviesLocalDataSource.getInstance(context.getContentResolver());
    }
}
