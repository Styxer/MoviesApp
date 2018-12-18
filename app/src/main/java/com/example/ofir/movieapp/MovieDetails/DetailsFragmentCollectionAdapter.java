package com.example.ofir.movieapp.MovieDetails;

import android.os.Bundle;

import com.example.ofir.movieapp.MovieDetails.DetailsFragment;
import com.example.ofir.movieapp.Utilities.Common;
import com.example.ofir.movieapp.model.Movie;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DetailsFragmentCollectionAdapter extends FragmentStatePagerAdapter {
    public DetailsFragmentCollectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        Movie selectedMovie = Common.getMovieList().get(position);
        bundle.putParcelable(Common.SELECTED_MOVIE_KEY, selectedMovie);
       // bundle.putInt("id", selectedMovie);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public int getCount() {
        return Common.getMovieList().size();
    }
}
