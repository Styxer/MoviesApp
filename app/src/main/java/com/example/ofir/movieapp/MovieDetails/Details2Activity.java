package com.example.ofir.movieapp.MovieDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import timber.log.Timber;

import android.os.Bundle;

import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Utilities.Common;

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

        if(getIntent().hasExtra(Common.SELECTED_MOVIE_POSITION_KEY)) {
            int startPosition = getIntent().getIntExtra(Common.SELECTED_MOVIE_POSITION_KEY, 0);
            viewPager.setCurrentItem(startPosition);
        }else{
            Timber.d("getIntent() missing position item");
        }


    }
}
