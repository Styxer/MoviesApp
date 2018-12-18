package com.example.ofir.movieapp.MovieTrailer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofir.movieapp.GlideApp;
import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Utilities.Common;
import com.example.ofir.movieapp.model.Trailer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    Context context;
    List<Trailer> trailerList;

    public TrailerAdapter(Context context, List<Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_card, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder holder, int position) {
        Trailer selectedTrailer = trailerList.get(position);
        holder.trailerTitle.setText(selectedTrailer.getName());
        String videoId = selectedTrailer.getKey();
        String img_url = "http://img.youtube.com/vi/" + videoId + "/0.jpg";

        GlideApp.with(context)
                .load(img_url)
                .placeholder(R.drawable.youtube)
                .into(holder.trailerThumbnail);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        private TextView trailerTitle;
        private ImageView trailerThumbnail;


        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            trailerTitle = itemView.findViewById(R.id.trailer_title);
            trailerThumbnail = itemView.findViewById(R.id.trailer_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Trailer selectedTrailer = trailerList.get(pos);
                        String videoId = trailerList.get(pos).getKey();
                        Intent intent = new Intent(context, YouTubePlayerFragmentActivity.class);
                        intent.putExtra(Common.MOVE_ID_KEY, videoId);
                     //   intent.putExtra("force_fullscreen",true);
                        context.startActivity(intent);
                        Timber.d("trailer clicked " + selectedTrailer.getName());
                       // Toast.makeText(v.getContext(), "You clicked " + selectedTrailer.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
