package com.example.ofir.movieapp.DBData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ofir.movieapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";

    private static final int DATABASE_VERSION = 2;


    private SQLiteDatabase db;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void DBOpen() {
        Timber.d("Database Opened");
        if (!db.isOpen()) {
            db = getWritableDatabase();
        }

    }

    public void DBClose() {
        Timber.i("Database Closed");
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID + " INTEGER, " +
                FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_IS_FAVORITE + " INTEGER  NOT NULL DEFAULT 0" +

                "); ";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }

    public boolean addFavorite(Movie movie) {

        boolean result = false;

        if (!isItemExitsInDB(movie.getId())) {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, movie.getId());
            values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movie.getOriginalTitle());
            values.put(FavoriteContract.FavoriteEntry.COLUMN_USERRATING, movie.getVoteAverage());
            values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, movie.getOverview());
            values.put(FavoriteContract.FavoriteEntry.COLUMN_IS_FAVORITE, true);
            db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values);
            db.close();

            result = true;
        }

        return result;

    }

    public void deleteFavorite(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, FavoriteContract.FavoriteEntry.COLUMN_MOVIEID + "=" + id, null);
        db.close();
    }

    public boolean isItemExitsInDB(int id) {
        SQLiteDatabase db = getReadableDatabase();
        //DBOpen();
        String selectString = "SELECT * FROM " + FavoriteContract.FavoriteEntry.TABLE_NAME + " WHERE " + FavoriteContract.FavoriteEntry.COLUMN_MOVIEID + " =?";
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[]{String.valueOf(id)});

        boolean isExits = false;
        if (cursor.moveToFirst()) {
            isExits = true;

            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }

            Timber.d(String.format("%d records found", count));
        }

        cursor.close();
        db.close();
        return isExits;
    }

    public List<Movie> getAllFavorite() {


        String[] columns = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,
                FavoriteContract.FavoriteEntry.COLUMN_TITLE,
                FavoriteContract.FavoriteEntry.COLUMN_USERRATING,
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH,
                FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS,
                FavoriteContract.FavoriteEntry.COLUMN_IS_FAVORITE

        };
        String sortOrder =
                FavoriteContract.FavoriteEntry._ID + " ASC";
        List<Movie> favoriteList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID))));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_IS_FAVORITE)));

                favoriteList.add(movie);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return favoriteList;
    }
}
