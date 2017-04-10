package com.yo.shishkoam.simpleui.managers;


import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.yo.shishkoam.simpleui.db.MoviesDbHelper;
import com.yo.shishkoam.simpleui.model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 08.04.2017
 */

public enum MovieManager {
    INSTANCE;
    private HashMap<Long, Movie> movieMap;
    private MoviesDbHelper dbHelper;

    public void init(Context context, boolean firsTime) {
        dbHelper = new MoviesDbHelper(context);
        try {
            movieMap = dbHelper.readData();
        } catch (SQLiteException ex) {
            movieMap = new HashMap<>();
        }

    }

    public void addMovie(Movie movie) {
        dbHelper.addMovie(movie);
        movieMap.put(movie.getId(), movie);
    }

    public void changeMovie(Movie movie) {
        movieMap.put(movie.getId(), movie);
        dbHelper.remove(movie);
        dbHelper.addMovie(movie);
    }

    public void deleteMovie(long movieID) {
        if (movieMap.containsKey(movieID)) {
            dbHelper.remove(movieMap.get(movieID));
            movieMap.remove(movieID);
        }
    }

    public List<Movie> getMovieList() {
        return new ArrayList<>(movieMap.values());
    }

    public Movie getMovie(long id) {
        return movieMap.get(id);
    }
}
