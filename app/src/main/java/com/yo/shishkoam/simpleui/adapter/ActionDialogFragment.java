package com.yo.shishkoam.simpleui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.yo.shishkoam.simpleui.R;
import com.yo.shishkoam.simpleui.activity.DetailsActivity;
import com.yo.shishkoam.simpleui.activity.EditMovieActivity;
import com.yo.shishkoam.simpleui.activity.MainActivity;
import com.yo.shishkoam.simpleui.helpers.Consts;
import com.yo.shishkoam.simpleui.managers.MovieManager;
import com.yo.shishkoam.simpleui.model.Movie;

/**
 * Created by User on 13.02.2017 to save dialog during rotation
 */

public class ActionDialogFragment extends DialogFragment implements Consts {
    private Context context;
    private Movie movie;
    private int position;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public Dialog onCreateDialog(Bundle id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.dialog_view, null);
        View editButton = view.findViewById(R.id.edit);
        editButton.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), EditMovieActivity.class);
            intent.putExtra(MOVIE_ID, movie.getId());
            startActivity(intent);
        });
        View moreButton = view.findViewById(R.id.more);
        moreButton.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra(MOVIE_ID, movie.getId());
            startActivity(intent);
        });

        View deleteButton = view.findViewById(R.id.delete);
        deleteButton.setOnClickListener((v) -> showConfirmDeleteDialog());
        View cancelButton = view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener((v) -> dismiss());
        builder.setView(view);
        return builder.create();
    }

    private void showConfirmDeleteDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle(R.string.deleting)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                    intent.putExtra(MOVIE_ID, movie.getId());
                    getContext().sendBroadcast(intent);
                    dismiss();
                })
                .setNegativeButton(R.string.cancel, null);
        builder1.create().show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CURR_MOVIE, movie.getId());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            int movieID = savedInstanceState.getInt(CURR_MOVIE);
            movie = MovieManager.INSTANCE.getMovie(movieID);
        }
    }
}
