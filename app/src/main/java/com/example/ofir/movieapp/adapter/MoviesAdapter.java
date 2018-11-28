package com.example.ofir.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ofir.movieapp.DetailActivity;
import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.model.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {


    private Context context;
    private List<Movie> movieList;

    public MoviesAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;


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
        holder.title.setText(selectedMovie.getOriginalTitle());
        String vote = Double.toString(selectedMovie.getVoteAverage());
        holder.userRating.setText(vote);

        //TODO: CHANGE TO  GlideApp.With(...)

        Glide.with(context)
                .load(selectedMovie.getPosterPath())
                .into(holder.thumbnail);
        // .preload(R.drawable.ic_launcher_background)


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {

        public TextView title, userRating;
        public ImageView thumbnail;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.text_view_movie_title);
            userRating = itemView.findViewById(R.id.text_view_user_rating);
            thumbnail = itemView.findViewById(R.id.image_view_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Movie selectedMovie = movieList.get(pos);
                        //TODO: CHANGE TO PARCABLE
                        Intent intent = new Intent(context, DetailActivity.class);
                        //TODO:CREATE COMMON KEYS
                        intent.putExtra("original_title", selectedMovie.getOriginalTitle());
                        intent.putExtra("poster_path", selectedMovie.getPosterPath());
                        intent.putExtra("overview", selectedMovie.getOverview());
                        intent.putExtra("vote_average", selectedMovie.getVoteAverage());
                        intent.putExtra("release_date", selectedMovie.getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        //TODO:ADD LOG
                        Toast.makeText(v.getContext(), "clicked on " + selectedMovie.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}
