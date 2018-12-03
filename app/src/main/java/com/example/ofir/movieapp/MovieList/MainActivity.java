package com.example.ofir.movieapp.MovieList;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ofir.movieapp.BuildConfig;
import com.example.ofir.movieapp.MovieDetails.Details2Activity;
import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Utilities.Common;
import com.example.ofir.movieapp.Utilities.GridSpacingItemDecoration;
import com.example.ofir.movieapp.Utilities.Logging;
import com.example.ofir.movieapp.api.Client;
import com.example.ofir.movieapp.api.Service;
import com.example.ofir.movieapp.model.Movie;
import com.example.ofir.movieapp.model.MoviesResponse;

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

public class MainActivity extends AppCompatActivity implements OnMovieClickListener {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swiperContainer;
    private OnMovieClickListener movieClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Logging.InitLogging();
        Timber.d("QuestionActivity started");
        initViews();

        swiperContainer = findViewById(R.id.main_content);
        swiperContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swiperContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(MainActivity.this, "Movies refreshed", Toast.LENGTH_SHORT).show();

            }
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

        loadJson();

        //adapter.setOnItemClickLitsner(this\\);
    }

    private void loadJson() {
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

            Call<MoviesResponse> call = apiService.getTopRatedMovies(apiKey);

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
}
