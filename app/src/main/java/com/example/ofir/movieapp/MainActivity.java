package com.example.ofir.movieapp;


import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.ofir.movieapp.adapter.MoviesAdapter;
import com.example.ofir.movieapp.model.Movie;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

                
            }
        });
    }
}
