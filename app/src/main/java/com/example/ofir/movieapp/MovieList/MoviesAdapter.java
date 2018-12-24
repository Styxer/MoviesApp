package com.example.ofir.movieapp.MovieList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ofir.movieapp.DBData.FavoriteDbHelper;
import com.example.ofir.movieapp.GlideApp;
import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Utilities.MovieUtils;
import com.example.ofir.movieapp.model.Movie;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {


    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener movieClickListener;


    private SharedPreferences sharedPreferences;


    public MoviesAdapter(Context context, List<Movie> movieList, OnMovieClickListener movieClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.movieClickListener = movieClickListener;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    @NonNull
    @Override
    public MoviesAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MovieHolder holder, int position) {

        final boolean[] saveFavorite = {false};
        final boolean[] mFavorite = {false};

        Movie selectedMovie = movieList.get(position);
        holder.bindMovies(selectedMovie);

        selectedMovie.setIsFavorite(setFavButton(selectedMovie.getId(), holder));

        holder.favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {

            SharedPreferences.Editor editor = context.getSharedPreferences("movieapp.MovieAdapter", MODE_PRIVATE).edit();

            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                selectedMovie.setIsFavorite(setFavButton(selectedMovie.getId(), holder));

                if (selectedMovie.getIsFavorite()) {

                    saveFavorite[0] = MovieUtils.saveFavorite(context, selectedMovie.getId(), selectedMovie.getTitle(), selectedMovie.getPosterPath(), selectedMovie.getVoteAverage(), selectedMovie.getOverview());

                    if (saveFavorite[0]) {
                        editor.putBoolean("Favorite Added", true);
                        editor.apply();
                        Snackbar.make(buttonView, "Added to Favorite",
                                Snackbar.LENGTH_SHORT).show();

                    } else {
                        Snackbar.make(buttonView, "Item already in Favorite list",
                                Snackbar.LENGTH_SHORT).show();


                    }
                } else {

                    MovieUtils.removeFromFavorite(context, selectedMovie.getId());
                    editor.putBoolean("Favorite Removed", true);
                    editor.apply();

                    Snackbar.make(buttonView, "Removed from Favorite",
                            Snackbar.LENGTH_SHORT).show();
                }
                //  mFavorite[0] = !mFavorite[0];
                //    mFavorite[0] = setFavButton(selectedMovie.getId(), holder);
            }
        });


        // force button image resource
        holder.favoriteButton.setOnFavoriteAnimationEndListener(new MaterialFavoriteButton.OnFavoriteAnimationEndListener() {
            @Override
            public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean favorite) {
                if (!saveFavorite[0] || !selectedMovie.getIsFavorite()) {
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                } else {
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite);
                }
            }
        });


    }

    private boolean setFavButton(int movieId, MoviesAdapter.MovieHolder holder) {
        boolean result;
        if (MovieUtils.isMovieFavourited(context, movieId)) {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite);
            result = false;
        } else {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_border);
            result = true;
        }

        return result;
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title, userRating;
        private ImageView thumbnail;
        private MaterialFavoriteButton favoriteButton;

        private MovieHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            userRating = itemView.findViewById(R.id.userrating);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
            itemView.setOnClickListener(this);
        }

        public void bindMovies(Movie movie) {
            title.setText(movie.getOriginalTitle());
            String vote = Double.toString(movie.getVoteAverage());
            userRating.setText(vote);

            String fullPostPath = movie.getBaseImageUrl() + movie.getPosterPath();

            GlideApp.with(context)
                    .load(fullPostPath)
                    .placeholder(R.drawable.load)
                    .into(thumbnail);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_content:
                    if (movieClickListener != null) {
                        movieClickListener.onMovieClicked(getAdapterPosition());
                    }
                    break;

            }

        }

    }


}
