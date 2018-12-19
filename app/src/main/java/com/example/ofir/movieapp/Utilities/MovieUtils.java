package com.example.ofir.movieapp.Utilities;

import android.app.Activity;
import android.content.Context;

import com.example.ofir.movieapp.DBData.FavoriteDbHelper;
import com.example.ofir.movieapp.model.Movie;

public class MovieUtils {

    static FavoriteDbHelper favoriteDbHelper;
    static private Movie favorite;

    public static void saveFavorite(Context context , int movie_id, String movieName, String thumbnail, double rate, String synopsis) {
        favoriteDbHelper = new FavoriteDbHelper(context);
        favorite = new Movie();

        favorite.setId(movie_id);
        favorite.setOriginalTitle(movieName);
        favorite.setPosterPath(thumbnail);
        favorite.setVoteAverage(rate);
        favorite.setOverview(synopsis);

        favoriteDbHelper.addFavorite(favorite);
    }
}
