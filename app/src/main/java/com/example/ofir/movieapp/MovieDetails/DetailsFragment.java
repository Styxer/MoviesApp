package com.example.ofir.movieapp.MovieDetails;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofir.movieapp.GlideApp;
import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Utilities.Common;
import com.example.ofir.movieapp.model.Movie;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private TextView nameOfMovie, plotSynopsis, userRating, releaseDate;
    private ImageView imageView;

    private Toolbar toolbar;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        try {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

       initCollapsingToolbar(view);



        imageView = view.findViewById(R.id.thumbnail_image_header);
        nameOfMovie = view.findViewById(R.id.title);
        plotSynopsis = view.findViewById(R.id.plotsynopsis);
        userRating = view.findViewById(R.id.userrating);
        releaseDate = view.findViewById(R.id.releasedate);

        Bundle bundle = getArguments();
        //  if (intent.hasExtra(Common.SELECTED_MOVIE_KEY)) {
        //Bundle bundle = intent.getExtras();

        if (bundle != null && bundle.containsKey(Common.SELECTED_MOVIE_KEY)) {
            Movie selectedMovie = getArguments().getParcelable(Common.SELECTED_MOVIE_KEY);
            String thumbnail = selectedMovie.getPosterPath();
            String movieName = selectedMovie.getOriginalTitle();
            String synopsis = selectedMovie.getOverview();
            String rating = Double.toString(selectedMovie.getVoteAverage());
            String dateOfRelease = selectedMovie.getReleaseDate();
            dateOfRelease = Common.parseDateToddMMyyyy(dateOfRelease);


            GlideApp.with(this)
                    .load(thumbnail)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
        } else {
            Toast.makeText(getContext(), "missing data", Toast.LENGTH_SHORT).show();
            Timber.e("missing data");
        }


        return view;
    }

    private void initCollapsingToolbar(View view) {
        final CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {

            boolean isShown = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShown = true;
                } else if (isShown) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShown = false;
                }


            }

        });


    }

}
