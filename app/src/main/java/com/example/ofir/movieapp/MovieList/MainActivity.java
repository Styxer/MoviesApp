package com.example.ofir.movieapp.MovieList;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ofir.movieapp.BuildConfig;
import com.example.ofir.movieapp.DBData.FavoriteDbHelper;
import com.example.ofir.movieapp.MovieDetails.Details2Activity;
import com.example.ofir.movieapp.MoviesSettings.SettingsActivity;
import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Threads.AsyncTaskActivity;
import com.example.ofir.movieapp.Utilities.Common;
import com.example.ofir.movieapp.Utilities.GridSpacingItemDecoration;
import com.example.ofir.movieapp.Utilities.Logging;
import com.example.ofir.movieapp.api.Client;
import com.example.ofir.movieapp.api.Service;
import com.example.ofir.movieapp.model.Movie;
import com.example.ofir.movieapp.model.MoviesResponse;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements OnMovieClickListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swiperContainer;
    private OnMovieClickListener movieClickListener;

    public static final int SORT_BY_POPULAR = 1;
    public static final int SORT_BY_TOP_RATED = 2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       
        Logging.InitLogging();
        Timber.d("QuestionActivity started");
        initViews();

        swiperContainer = findViewById(R.id.main_content);
        swiperContainer.setColorSchemeResources(android.R.color.holo_orange_dark);




        swiperContainer.setOnRefreshListener( () -> {
            initViews();
            Toast.makeText(MainActivity.this, "Movies refreshed", Toast.LENGTH_SHORT).show();
        });

        movieClickListener = this;
    }

    private void initViews() {
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching movies....");
        pd.setCancelable(false);
        pd.show();

        recyclerView = findViewById(R.id.recycler_view);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList, movieClickListener);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            //recyclerView.addItemDecoration();
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, GridSpacingItemDecoration.dpToPx(10, this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



       // loadJson();
        checkSortOrder();

        //adapter.setOnItemClickLitsner(this\\);
    }

    private void loadJson(int sortOrder) {
        String apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        Timber.d("loadJson started");
        try {
            if (apiKey.isEmpty()) {
                Toast.makeText(this, "missing api key from themoviedb.db.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                Timber.e("loadJson found no API key");
                return;
            }

            // Client client = new Client();
            Service apiService = Client.getClient().create(Service.class);

            Call<MoviesResponse> call = null;
            if (sortOrder == SORT_BY_POPULAR){
                call = apiService.getTopRatedMovies(apiKey);
            }else if(sortOrder == SORT_BY_TOP_RATED)
                call = apiService.getPopularMovies(apiKey);



            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    movieList = response.body().getResults();
                    Common.setMovieList(movieList);
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movieList, movieClickListener));
                    recyclerView.smoothScrollToPosition(0);
                    if (swiperContainer.isRefreshing()) {
                        swiperContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                    Timber.d(t.getMessage());
                    Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Timber.d(e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.menu_open_async:
                startActivity(new Intent(MainActivity.this, AsyncTaskActivity.class));
                return  true;
            case R.id.menu_open_thread_handler:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onMovieClicked(int itemPosition) {
        if (itemPosition != RecyclerView.NO_POSITION) {
            Timber.d("movie item clicked");
            Intent intent = new Intent(this, Details2Activity.class);
            intent.putExtra(Common.SELECTED_MOVIE_POSITION_KEY, itemPosition);
            startActivity(intent);
        }
        else{
            Timber.d(String.format("movie item clicked on invalid position %d", itemPosition));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Timber.d("preferences updated");

        checkSortOrder();
    }

    private void  checkSortOrder(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );

        if (sortOrder.equals(this.getString(R.string.pref_most_popular))){
            Timber.d("Sorting by most popular");
            Toast.makeText(this, "sort by most popular selected", Toast.LENGTH_SHORT).show();
            loadJson(SORT_BY_POPULAR);
        }else if (sortOrder.equals(this.getString(R.string.pref_highest_rated))){
            Timber.d("Sorting by highest rated");
            Toast.makeText(this, "sort by highest rated selected", Toast.LENGTH_SHORT).show();
            loadJson(SORT_BY_TOP_RATED);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        if(movieList.isEmpty()){
            checkSortOrder();
        }else{
            //TODO
        }
    }

 /*   @Override
    protected void onPause() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }*/
}
