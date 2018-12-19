package com.example.ofir.movieapp.MovieList;

import android.content.Context;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {


    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener movieClickListener;

    private FavoriteDbHelper favoriteDbHelper;
    private Movie favorite;
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
        Movie selectedMovie = movieList.get(position);
        holder.bindMovies(selectedMovie);

        holder.favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {

            SharedPreferences.Editor editor = context.getSharedPreferences("movieapp.MovieAdapter", MODE_PRIVATE).edit();

            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite){
                    editor.putBoolean("Favorite Added", true);
                    editor.commit();
                    MovieUtils.saveFavorite(context, selectedMovie.getId(),selectedMovie.getTitle() ,selectedMovie.getPosterPath(), selectedMovie.getVoteAverage(), selectedMovie.getOverview());
                    Snackbar.make(buttonView, "Added to Favorite",
                            Snackbar.LENGTH_SHORT).show();
                }else{

                    favoriteDbHelper = new FavoriteDbHelper(context);
                    favoriteDbHelper.deleteFavorite(selectedMovie.getId());

                    editor.putBoolean("Favorite Removed", true);
                    editor.commit();

                    Snackbar.make(buttonView, "Removed from Favorite",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
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
            favoriteButton  = itemView.findViewById(R.id.favorite_button);
            itemView.setOnClickListener(this);
        }

        public  void bindMovies(Movie movie){
            title.setText(movie.getOriginalTitle());
            String vote = Double.toString(movie.getVoteAverage());
            userRating.setText(vote);


            GlideApp.with(context)
                    .load(movie.getPosterPath())
                    .placeholder(R.drawable.load)
                    .into(thumbnail);
        }

        @Override
        public void onClick(View v) {
           switch (v.getId()){
               case R.id.main_content:
                   if (movieClickListener != null) {
                       movieClickListener.onMovieClicked(getAdapterPosition());
                   }
                   break;

           }

        }
    }


}
