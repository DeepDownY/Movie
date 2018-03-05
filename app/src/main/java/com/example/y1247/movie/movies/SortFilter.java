package com.example.y1247.movie.movies;

import android.os.Bundle;
import android.support.compat.BuildConfig;

/**
 * Created by y1247 on 2017/3/11.
 */

public class SortFilter {
    public final static String KEY_SORT_FILTER = BuildConfig.APPLICATION_ID + "SORT_FILTER";
    private SortType sortType = SortType.RATE;
    private Bundle filterExtras;

    protected SortFilter(Bundle extras) {
        this.filterExtras = extras;
        this.sortType = (SortType) extras.getSerializable(KEY_SORT_FILTER);
    }

    public static SortFilter from(SortType sortType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SORT_FILTER,sortType);
        return  new SortFilter(bundle);
    }


    public SortType getSortType() {
        return sortType;
    }
}
