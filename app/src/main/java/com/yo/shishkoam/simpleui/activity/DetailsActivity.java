package com.yo.shishkoam.simpleui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.yo.shishkoam.simpleui.BR;
import com.yo.shishkoam.simpleui.R;
import com.yo.shishkoam.simpleui.helpers.Consts;
import com.yo.shishkoam.simpleui.managers.MovieManager;
import com.yo.shishkoam.simpleui.model.Movie;

/**
 * Created by User on 08.04.2017
 */

public class DetailsActivity extends AppCompatActivity implements Consts {

    private static final int LAYOUT_ACTIVITY = R.layout.activity_details;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, LAYOUT_ACTIVITY);
        if (savedInstanceState == null) {
            movie = MovieManager.INSTANCE.getMovie(getIntent().getLongExtra(MOVIE_ID, 0));
        } else {
            movie = MovieManager.INSTANCE.getMovie(savedInstanceState.getLong(MOVIE_ID, 0));
        }
        binding.setVariable(BR.movie, movie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(MOVIE_ID, movie.getId());
    }
}
