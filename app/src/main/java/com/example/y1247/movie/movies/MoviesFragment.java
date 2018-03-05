package com.example.y1247.movie.movies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.y1247.movie.R;
import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.source.LoadSourceType;
import com.example.y1247.movie.movie.MovieActivity;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by y1247 on 2017/3/15.
 */

public class MoviesFragment extends Fragment implements MoviesContract.View{

    private MoviesContract.Presenter mPresenter;

    private RecyclerView rv_Movies;
    private LinearLayout mNoMovieView;
    SwipeRefreshLayout sl_Movies;

    private MoviesAdapter adapter;


    private SharedPreferences.Editor editor;

    int loadflag;

    @Override
    public void onResume() {
        super.onResume();
    }

    ItemListener itemListener = new ItemListener() {
        @Override
        public void onClick(Movie movie) {
            mPresenter.openMovieDetails(movie);
        }

        @Override
        public void onCollectClick(Movie movie) {
            mPresenter.collectMovie(movie);
        }

        @Override
        public void onUnCollectClick(Movie movie) {
            mPresenter.unCollectMovie(movie);
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.menu_filter:
               showFilteringPopUpMenu();
               break;
           case R.id.menu_POP:
               mPresenter.setSortType(SortFilter.from(SortType.POP));
               editor.putInt("SortType",SortType.POP.ordinal());
               changeLoadFlag(LoadSourceType.POP);
               break;
           case R.id.menu_RATE:
               mPresenter.setSortType(SortFilter.from(SortType.RATE));
               editor.putInt("SortType",SortType.RATE.ordinal());
               changeLoadFlag(LoadSourceType.POP);
               break;
       }
       editor.commit();
       return true;
    }

    public MoviesFragment(){

    }

    public static MoviesFragment newInstance(){
        return new MoviesFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movies_frag,container,false);

        rv_Movies = (RecyclerView) root.findViewById(R.id.movies_list);
        mNoMovieView = (LinearLayout) root.findViewById(R.id.noMovies);

        adapter = new MoviesAdapter(getActivity(),itemListener);

        rv_Movies.setLayoutManager(new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false));

        rv_Movies.setAdapter(adapter);

        final Picasso picasso = Picasso.with(getActivity());

//        adapter = new MovieCursorAdapter(getActivity(),itemListener);

//        lv_Movies.setAdapter(adapter);

        sl_Movies = (SwipeRefreshLayout) root.findViewById(R.id.sl_movies);
        sl_Movies.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        sl_Movies.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loadflag==LoadSourceType.POP.ordinal())
                    mPresenter.loadMovies(LoadSourceType.POP);
                else
                    mPresenter.loadMovies(LoadSourceType.RATE);
            }
        });

        mPresenter.start();

        setHasOptionsMenu(true);

        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = setting.edit();
        editor.apply();

        return root;
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.sl_movies);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMovies(List<Movie> data) {
        adapter.setData(data);
//        if(movies!=null) {
//            if (movies.moveToFirst()) {
////                adapter.swapCursor(movies);
////                adapter.notifyDataSetChanged();
//                lv_Movies.setVisibility(View.VISIBLE);
//                mNoMovieView.setVisibility(View.GONE);
//            } else {
//                lv_Movies.setVisibility(View.GONE);
//                mNoMovieView.setVisibility(View.VISIBLE);
//            }
//        }else{
//            lv_Movies.setVisibility(View.GONE);
//            mNoMovieView.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void showMoviesDetail(Movie movie) {
        MovieActivity.StarActivity(getActivity(),movie);
    }

    @Override
    public void showNoMovies() {
        mNoMovieView.setVisibility(View.VISIBLE);
        rv_Movies.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingMoviesError() {
        showMessage("Load Failed");
    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getActivity(),getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.menu_filter,popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                adapter.swapCursor(null);
                switch (item.getItemId()) {
                    case R.id.filter_all:
                        mPresenter.setFiltering(MovieFilter.from(MoviesFilterType.ALL));
                        break;
                    case R.id.filter_collect:
                        mPresenter.setFiltering(MovieFilter.from(MoviesFilterType.COLLECTED));
                        break;
                }
                mPresenter.loadMovies(LoadSourceType.LOCAL);
                return true;
            }
        });

        popup.show();
    }

    public void setLoadflag(int flag){
        this.loadflag = flag;
    }

    @Override
    public void changeLoadFlag(LoadSourceType type) {
        this.loadflag = type.ordinal();
        sl_Movies.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loadflag==LoadSourceType.POP.ordinal())
                    mPresenter.loadMovies(LoadSourceType.POP);
                else if (loadflag==LoadSourceType.RATE.ordinal())
                    mPresenter.loadMovies(LoadSourceType.RATE);
            }
        });
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

//    private static class MovieCursorAdapter extends CursorAdapter{
//
//        private final ItemListener itemListener;
//
//        public MovieCursorAdapter(Context context,ItemListener itemListener) {
//            super(context, null, 0);
//            this.itemListener = itemListener;
//        }
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup parent) {
//            View view = LayoutInflater.from(context)
//                    .inflate(R.layout.item_movies,parent,false);
//            ViewHolder viewHolder = new ViewHolder(view);
//            view.setTag(viewHolder);
//
//            return view;
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            ViewHolder viewHolder = (ViewHolder) view.getTag();
//
//            final Movie movie = Movie.from(cursor);
//
//            Picasso.with(view.getContext())
//                    .load(Movie.beginUrl + movie.getBackdrop_path())
//                    .placeholder(R.mipmap.ic_launcher)
//                    .into(viewHolder.iv_moviePic);
//
//            String temp = String.valueOf(movie.getVote_average()) + "/ 10";
//
//            viewHolder.tv_MovieName.setText(movie.getTitle());
//            viewHolder.tv_movieVote.setText(temp);
//
//            if(movie.getSave_flag()==1){
//                viewHolder.imageButton.setActivated(true);
//            } else viewHolder.imageButton.setActivated(false);
//
//            viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i("DSF", "onClick: " + movie.getSave_flag());
//                    if(movie.getSave_flag()==1){
//                        v.setActivated(false);
//                        itemListener.onUnCollectClick(movie);
//                    }else {
//                        v.setActivated(true);
//                        itemListener.onCollectClick(movie);
//                    }
//                }
//            });
//
//
//
//            viewHolder.rowView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemListener.onClick(movie);
//                }
//            });
//
//
//
//
//        }
//
//        public static class ViewHolder {
//            public final View rowView;
//            public final TextView tv_MovieName;
//            public final TextView tv_movieVote;
//            public final ImageView iv_moviePic;
//            public final ImageView imageButton;
//
//            public ViewHolder(View view) {
//                rowView = view;
//                iv_moviePic = (ImageView) view.findViewById(R.id.iv_movieImg);
//                tv_MovieName = (TextView) view.findViewById(R.id.tv_movieName);
//                tv_movieVote = (TextView) view.findViewById(R.id.tv_movieVote);
//                imageButton = (ImageView) view.findViewById(R.id.iv_collection);
//            }
//        }
//    }

    class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.Holder>{

        List<Movie> ls;
        Context context;
        private ItemListener itemListener;

        public MoviesAdapter(Context context,ItemListener itemListener) {
            this.itemListener = itemListener;
            this.context = context;
            ls = new ArrayList<>();
        }

        public void setData(List<Movie> data){
            this.ls = data;
//            Log.i("DSF", "setData: " + data.size());
            this.notifyDataSetChanged();
        }


        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_movies, parent,
                    false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            final Movie movie = ls.get(position);
//            Handler handle = new Handler(){
//                @Override
//                public void handleMessage(Message msg) {
//                    switch (msg.what){
//                        case 1:
//                            Bitmap bitmap = (Bitmap) msg.obj;
//                            Palette.Builder builder = Palette.from(bitmap);
//                            holder.iv_moviePic.setImageBitmap(bitmap);
//                            builder.generate(new Palette.PaletteAsyncListener() {
//                                @Override
//                                public void onGenerated(Palette palette) {
//                                    Palette.Swatch vibrant = palette.getVibrantSwatch();
//                                    holder.rowView.setBackgroundColor(vibrant.getRgb());
//                                    holder.tv_movieVote.setTextColor(vibrant.getTitleTextColor());
//                                    holder.tv_movieVote.setTextColor(vibrant.getTitleTextColor());
//                                }
//                            });
//                    }
//                }
//            };
//
//            final Message msg = handle.obtainMessage();
//            new Thread(){
//                @Override
//                public void run() {
//                    try {
//                        Bitmap b = Picasso.with(context)
//                                .load(Movie.beginUrl + movie.getPoster_path()).get();
//                        msg.obj = b;
//                        msg.what = 1;
//                        msg.sendToTarget();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.run();

            Picasso.with(context)
                    .load(Movie.beginUrl + movie.getPoster_path())
                    .tag("Touch Listener")
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.iv_moviePic);

//            Bitmap obmp = Bitmap.createBitmap(holder.iv_moviePic.getDrawingCache());
//
//            Palette.Builder builder = Palette.from(obmp);
//                            builder.generate(new Palette.PaletteAsyncListener() {
//                                @Override
//                                public void onGenerated(Palette palette) {
//                                    Palette.Swatch vibrant = palette.getVibrantSwatch();
//                                    holder.rowView.setBackgroundColor(vibrant.getRgb());
//                                    holder.tv_movieVote.setTextColor(vibrant.getTitleTextColor());
//                                    holder.tv_movieVote.setTextColor(vibrant.getTitleTextColor());
//                                }
//                            });

//            RandomColor randomColor = new RandomColor();
//            int color = randomColor.randomColor();
//            Log.i("DSF", "onBindViewHolder: " + color);

//            holder.rowView.setBackgroundColor(Color.parseColor(String.valueOf(color)));

            String temp = String.valueOf(movie.getVote_average()) + "/ 10";
            holder.tv_MovieName.setText(movie.getTitle());
            holder.tv_movieVote.setText(temp);
            if (movie.getSave_flag() == 1) {
                holder.imageButton.setActivated(true);
            } else {
                holder.imageButton.setActivated(false);
            }
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(movie.getSave_flag() == 1) {
                        v.setActivated(false);
                        itemListener.onUnCollectClick(movie);
                    }else {
                        v.setActivated(true);
                        itemListener.onCollectClick(movie);
                    }
                }
            });
            holder.rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,MovieActivity.class);
                    String transitionName = context.getString(R.string.transition_album_cover);

                    ActivityOptionsCompat compat = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(),v.findViewById(R.id.iv_movieImg),transitionName);

                    intent.putExtra(MovieActivity.INTENT_EXTRA,movie);
                    ActivityCompat.startActivity(getActivity(),intent,compat.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            if(ls == null) return 0;
            return ls.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public LinearLayout rowView;
            public TextView tv_MovieName;
            public TextView tv_movieVote;
            public ImageView iv_moviePic;
            public ImageView imageButton;

            public Holder(View view) {
                super(view);
                rowView = (LinearLayout) view.findViewById(R.id.ll_itemLayout);
                iv_moviePic = (ImageView) view.findViewById(R.id.iv_movieImg);
                tv_MovieName = (TextView) view.findViewById(R.id.tv_movieName);
                tv_movieVote = (TextView) view.findViewById(R.id.tv_movieVote);
                imageButton = (ImageView) view.findViewById(R.id.iv_collection);
            }
        }

    }

    public interface ItemListener{

        void onClick(Movie movie);

        void onCollectClick(Movie movie);

        void onUnCollectClick(Movie movie);
    }

}
