package com.example.ofir.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ofir.movieapp.DetailActivity;
import com.example.ofir.movieapp.GlideApp;
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

        GlideApp.with(context)
                .load(selectedMovie.getPosterPath())
                .placeholder(R.drawable.ic_launcher_background)
                .listener(new RequestListener<Drawable>(){

                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {

        private TextView title, userRating;
        private ImageView thumbnail;

        private MovieHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            userRating = itemView.findViewById(R.id.userrating);
            thumbnail = itemView.findViewById(R.id.thumbnail);

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
