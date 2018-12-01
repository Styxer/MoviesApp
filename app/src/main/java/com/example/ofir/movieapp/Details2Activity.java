package com.example.ofir.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.ofir.movieapp.adapter.DetailsFragmentCollectionAdapter;

public class Details2Activity extends AppCompatActivity {

    private ViewPager viewPager;
    private DetailsFragmentCollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);

        viewPager  = findViewById(R.id.pager);
        adapter = new DetailsFragmentCollectionAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }
}
