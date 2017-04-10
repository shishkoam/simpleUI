package com.yo.shishkoam.simpleui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.yo.shishkoam.simpleui.model.Movie;
import com.yo.shishkoam.simpleui.types.Language;

import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 08.04.2017
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private final static String ID = "id";
    private final static String ADULT = "adult";
    private final static String OVERVIEW = "overview";
    private final static String RELEASE = "release";
    private final static String TITLE = "title";
    private final static String LANG = "lang";
    private final static String IMAGE = "popularity";
    private final static String VOTE_AVG = "vote_count";
    private final static String VOTE_AVERAGE = "vote_avg";
    private final static String DB_NAME = "movies";
    private final static String FILE_PATH = "file_path";

    public MoviesDbHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(DB_NAME, db);
    }

    private void createDatabase(String databaseName, SQLiteDatabase db) {
        // create table
        db.execSQL("create table " + databaseName + " ("
                + ID + " long,"
                + ADULT + " integer,"
                + OVERVIEW + " string,"
                + RELEASE + " string,"
                + TITLE + " string,"
                + LANG + " string,"
                + IMAGE + " blob,"
                + VOTE_AVG + " integer,"
                + VOTE_AVERAGE + " double,"
                + FILE_PATH + "string"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addProductList(List<Movie> movieList) {
        SQLiteDatabase db = getWritableDatabase();
        for (Movie movie : movieList) {
            // create object for data
            ContentValues cv = new ContentValues();
            // prepare pairs to insert
            addMovieToCv(movie, cv);
            //insert object to db
            db.insert(DB_NAME, null, cv);
        }
        close();
    }

    private void addMovieToCv(Movie movie, ContentValues cv) {
        cv.put(ID, movie.getId());
        cv.put(ADULT, movie.isAdult() ? 1 : 0);
        cv.put(OVERVIEW, movie.getOverview());
        cv.put(RELEASE, movie.getReleaseDate());
        cv.put(TITLE, movie.getTitle());
        cv.put(LANG, movie.getOriginalLanguage().ordinal());
        cv.put(IMAGE, movie.getImageBytes());
        cv.put(VOTE_AVG, movie.getVoteAverage());
        cv.put(FILE_PATH, movie.getFilePath());
    }

    public void addMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        // create object for data
        ContentValues cv = new ContentValues();
        // prepare pairs to insert
        addMovieToCv(movie, cv);
        //insert object to db
        db.insert(DB_NAME, null, cv);
        close();
    }

    public HashMap<Long, Movie> readData() throws SQLiteException {
        HashMap<Long, Movie> movieHashMap = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        // request all data (cursor) from table
        Cursor c = db.query(DB_NAME, null, null, null, null, null, null);
        // check that table has data
        if (c.moveToFirst()) {
            // get column index by name
            int idColIndex = c.getColumnIndex(ID);
            int adultIdColIndex = c.getColumnIndex(ADULT);
            int overviewColIndex = c.getColumnIndex(OVERVIEW);
            int releaseColIndex = c.getColumnIndex(RELEASE);
            int titleColIndex = c.getColumnIndex(TITLE);
            int langColIndex = c.getColumnIndex(LANG);
            int imageColIndex = c.getColumnIndex(IMAGE);
            int voteAvgColIndex = c.getColumnIndex(VOTE_AVG);
            int filePathColIndex = c.getColumnIndex(FILE_PATH);
            do {
                // get data by column indexes
                long id = c.getLong(idColIndex);
                int adult = c.getInt(adultIdColIndex);
                String overview = c.getString(overviewColIndex);
                long releaseDate = c.getLong(releaseColIndex);
                String title = c.getString(titleColIndex);
                Language originalLanguage = Language.values()[c.getInt(langColIndex)];
                byte[] image = c.getBlob(imageColIndex);
                float voteAvg = c.getFloat(voteAvgColIndex);
                String filePath = c.getString(filePathColIndex);
                Movie movie = new Movie(adult == 1, overview, releaseDate, id,
                        originalLanguage, title, image, voteAvg, filePath);
                movieHashMap.put(id, movie);
            } while (c.moveToNext());
        }
        c.close();
        close();
        return movieHashMap;
    }

    public void remove(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + DB_NAME + " WHERE " + ID + " = '" + movie.getId() + "'");
    }

}
