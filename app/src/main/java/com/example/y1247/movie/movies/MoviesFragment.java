package com.example.y1247.movie.movies;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.y1247.movie.R;
import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.source.LoadSourceType;
import com.squareup.picasso.Picasso;

/**
 * Created by y1247 on 2017/3/15.
 */

public class MoviesFragment extends Fragment implements MoviesContract.View{

    private MoviesContract.Presenter mPresenter;

    private ListView lv_Movies;
    private LinearLayout mNoMovieView;
    SwipeRefreshLayout sl_Movies;

    private MovieCursorAdapter adapter;

    private SharedPreferences.Editor editor;

    int loadflag;

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
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


        lv_Movies = (ListView) root.findViewById(R.id.movies_list);
        mNoMovieView = (LinearLayout) root.findViewById(R.id.noMovies);
        adapter = new MovieCursorAdapter(getActivity(),itemListener);

        lv_Movies.setAdapter(adapter);

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
    public void showMovies(Cursor movies) {
        if(movies!=null) {
            if (movies.moveToFirst()) {
                adapter.swapCursor(movies);
                adapter.notifyDataSetChanged();
                lv_Movies.setVisibility(View.VISIBLE);
                mNoMovieView.setVisibility(View.GONE);
            } else {
                lv_Movies.setVisibility(View.GONE);
                mNoMovieView.setVisibility(View.VISIBLE);
            }
        }else{
            lv_Movies.setVisibility(View.GONE);
            mNoMovieView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMoviesDetail(Movie movie) {

    }

    @Override
    public void showNoMovies() {
        mNoMovieView.setVisibility(View.VISIBLE);
        lv_Movies.setVisibility(View.GONE);
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
                switch (item.getItemId()){
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

    private static class MovieCursorAdapter extends CursorAdapter{

        private final ItemListener itemListener;

        public MovieCursorAdapter(Context context,ItemListener itemListener) {
            super(context, null, 0);
            this.itemListener = itemListener;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_movie,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();

            final Movie movie = Movie.from(cursor);

            Picasso.with(view.getContext())
                    .load(Movie.beginUrl + movie.getBackdrop_path())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.iv_moviePic);

            String temp = String.valueOf(movie.getVote_average()) + "/ 10";

            viewHolder.tv_MovieName.setText(movie.getTitle());
            viewHolder.tv_movieVote.setText(temp);
            if(movie.getSave_flag()!=0){
                viewHolder.imageButton.setBackgroundResource(R.drawable.star_outline);
            }

            viewHolder.rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onClick(movie);
                }
            });

            viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onCollectClick(movie);
                }
            });


        }

        public static class ViewHolder {
            public final View rowView;
            public final TextView tv_MovieName;
            public final TextView tv_movieVote;
            public final ImageView iv_moviePic;
            public final ImageButton imageButton;

            public ViewHolder(View view) {
                rowView = view;
                iv_moviePic = (ImageView) view.findViewById(R.id.iv_movieImg);
                tv_MovieName = (TextView) view.findViewById(R.id.tv_movieName);
                tv_movieVote = (TextView) view.findViewById(R.id.tv_movieVote);
                imageButton = (ImageButton) view.findViewById(R.id.ib_collection);
            }
        }
    }

    public interface ItemListener{

        void onClick(Movie movie);

        void onCollectClick(Movie movie);
    }

}
