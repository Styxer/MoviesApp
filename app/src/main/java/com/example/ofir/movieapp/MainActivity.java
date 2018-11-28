package com.example.ofir.movieapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ofir.movieapp.adapter.MoviesAdapter;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swiperContainer;

    //TODO: PLANT TIMBER


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        return null;
    }


    private void initViews() {
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching movies....");
        pd.setCancelable(false);
        pd.show();

        recyclerView = findViewById(R.id.recycler_view);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            //TODO : ADD ITEM DECORATION
            //recyclerView.addItemDecoration();
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.n

        loadJson();
    }

    private void loadJson() {
        String apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        try {
            if (apiKey.isEmpty()) {
                Toast.makeText(this, "missing api key from themoviedb.db.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }

            Client client = new Client();
            Service apiService = Client.getClient().create(Service.class);

            Call<MoviesResponse> call = apiService.getTopRatedMovies(apiKey);

            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swiperContainer.isRefreshing()) {
                        swiperContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    //TODO: ADD ERROR LOG   - t.getMessage();
                    Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            //TODO ADD ERROR LOG -   e.getMessage();
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
            case R.id.movie_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
