package com.example.ofir.movieapp.Utilities;

import com.example.ofir.movieapp.model.Movie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Common {

    public static final String SELECTED_MOVIE_KEY  = "SELECTED_MOVIE";
    public static final String SELECTED_MOVIE_POSITION_KEY  = "SELECTED_MOVIE_POSITION";

    public static final String ASYNC_MAIN_CONTENT_KEY  = "ASYNC_MAIN_CONTENT";


    static List<Movie> movieList;

    public static String getSelectedMovieKey() {
        return SELECTED_MOVIE_KEY;
    }

    public static List<Movie> getMovieList() {
        return movieList;
    }

    public static void setMovieList(List<Movie> movieList) {
        Common.movieList = movieList;
    }

    public static String parseDateToddMMyyyy(String time) {

       // SimpleDateFormat date1 = new SimpleDateFormat().

        String inputPattern = "yyyy-MM-dd";
        //String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        DateFormat outputFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
