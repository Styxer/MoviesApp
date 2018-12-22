package com.example.ofir.movieapp.Utilities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.example.ofir.movieapp.DBData.FavoriteDbHelper;
import com.example.ofir.movieapp.MovieList.MoviesAdapter;
import com.example.ofir.movieapp.model.Movie;

import java.util.List;

public class MovieUtils {

    static private FavoriteDbHelper favoriteDbHelper;
    static private Movie favorite;



    public static boolean saveFavorite(Context context , int movie_id, String movieName, String thumbnail, double rate, String synopsis) {
        favoriteDbHelper = new FavoriteDbHelper(context);
        favorite = new Movie();

        favorite.setId(movie_id);
        favorite.setOriginalTitle(movieName);
        favorite.setPosterPath(thumbnail);
        favorite.setVoteAverage(rate);
        favorite.setOverview(synopsis);

        return favoriteDbHelper.addFavorite(favorite);
    }

    public  static  void removeFromFavorite(Context context , int movieId){
        favoriteDbHelper = new FavoriteDbHelper(context);
        favoriteDbHelper.deleteFavorite(movieId);
    }

    public  static  boolean isMovieFavourited(Context context,  int movieId){
        favoriteDbHelper = new FavoriteDbHelper(context);
        return favoriteDbHelper.isItemExitsInDB(movieId);
    }

    public static void getAllFavorite(Context context,   List<Movie> movieList, MoviesAdapter adapter){
        favoriteDbHelper = new FavoriteDbHelper(context);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params){
                movieList.clear();
                movieList.addAll(favoriteDbHelper.getAllFavorite());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
