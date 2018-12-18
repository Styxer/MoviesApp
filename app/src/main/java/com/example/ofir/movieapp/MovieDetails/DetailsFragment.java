package com.example.ofir.movieapp.MovieDetails;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofir.movieapp.BuildConfig;
import com.example.ofir.movieapp.GlideApp;
import com.example.ofir.movieapp.MovieTrailer.TrailerAdapter;
import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Utilities.Common;
import com.example.ofir.movieapp.api.Client;
import com.example.ofir.movieapp.api.Service;
import com.example.ofir.movieapp.model.Movie;
import com.example.ofir.movieapp.model.Trailer;
import com.example.ofir.movieapp.model.TrailerResponse;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private TextView nameOfMovie, plotSynopsis, userRating, releaseDate;
    private ImageView imageView;

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private TrailerAdapter adapter;
    private List<Trailer> trailerList;


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

        initViews(view);

        setArguments();

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

    private void initViews(View view) {
        trailerList = new ArrayList<>();
        adapter = new TrailerAdapter(getContext(), trailerList);

        imageView = view.findViewById(R.id.thumbnail_image_header);
        nameOfMovie = view.findViewById(R.id.title);
        plotSynopsis = view.findViewById(R.id.plotsynopsis);
        userRating = view.findViewById(R.id.userrating);
        releaseDate = view.findViewById(R.id.releasedate);


        recyclerView = view.findViewById(R.id.recycler_view_trailers);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void setArguments() {

        Bundle bundle = getArguments();

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
                    .placeholder(R.drawable.load)
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);

            loadJSON(selectedMovie.getId());

        } else {
            Toast.makeText(getContext(), "missing data", Toast.LENGTH_SHORT).show();
            Timber.e("missing data");
        }

    }

    private void loadJSON(int movie_id) {

        try {
            String apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN;
            if (apiKey.isEmpty()) {
                Toast.makeText(getContext(), "missing API key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }


            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            retrofit2.Call<TrailerResponse> call = apiService.getMovieTrailer(movie_id, apiKey);

            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(retrofit2.Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    List<Trailer> trailers = response.body().getResults();
                    recyclerView.setAdapter(new TrailerAdapter(getContext(), trailers));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(retrofit2.Call<TrailerResponse> call, Throwable t) {
                    Timber.d("Error " + t.getMessage());
                    Toast.makeText(getContext(), "Error fetching trailer data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Timber.d("Error" + e.getMessage());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }


    }
}
