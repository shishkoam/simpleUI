package com.yo.shishkoam.simpleui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yo.shishkoam.simpleui.R;
import com.yo.shishkoam.simpleui.helpers.Consts;
import com.yo.shishkoam.simpleui.helpers.Utils;
import com.yo.shishkoam.simpleui.managers.MovieManager;
import com.yo.shishkoam.simpleui.model.Movie;
import com.yo.shishkoam.simpleui.types.Language;

import java.util.Calendar;

/**
 * Created by User on 08.04.2017
 */

public class EditMovieActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, Consts {

    private Activity activity = this;
    private EditText nameEditText, descriptionEditText;
    private TextView createTimeTextView, textViewFilePath;
    private Button setDateButton, controlButton;
    private Switch adultSwitch;
    private ImageView imageView;
    private Spinner langSpinner;
    private RatingBar defaultRatingBar;
    private Calendar calendar;
    private long createTime;
    private long movieID;
    private Uri imageUri;
    private boolean isEditMode = false;
    private View formView, progressView;
    private ImageButton attachButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_movie_form);

        imageView = (ImageView) findViewById(R.id.image);
        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(IMAGE_URI);
            imageView.setImageURI(imageUri);
            movieID = savedInstanceState.getLong(CURR_MOVIE, 0);
            isEditMode = savedInstanceState.getBoolean(IS_EDIT, false);
        } else {
            Intent intent = getIntent();
            movieID = intent.getLongExtra(MOVIE_ID, 0);
            isEditMode = intent.getBooleanExtra(IS_EDIT, false);
        }
        attachButton = (ImageButton) findViewById(R.id.attach);
        calendar = Calendar.getInstance();
        formView = findViewById(R.id.main_form);
        progressView = findViewById(R.id.progress);
        nameEditText = (EditText) findViewById(R.id.editText_name);
        createTimeTextView = (TextView) findViewById(R.id.textView_createTime);
        createTime = System.currentTimeMillis();
        createTimeTextView.setText(Utils.formatDate(createTime));
        langSpinner = (Spinner) findViewById(R.id.spinner_lang);

        adultSwitch = (Switch) findViewById(R.id.adult);
        imageView.setOnClickListener((v) -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PHOTO_REQUEST);
        });
        descriptionEditText = (EditText) findViewById(R.id.editText_description);
        setDateButton = (Button) findViewById(R.id.button_deadline);
        textViewFilePath = (TextView) findViewById(R.id.file_path);
        initSpinnerLang();
        defaultRatingBar = (RatingBar) findViewById(R.id.ratingBar_default);
        restoreCurrentStatus(movieID);

        controlButton = (Button) findViewById(R.id.save);
        if (isEditMode) {
            turnOnEditing();
        } else {
            turnOfEditing();
        }

        setDateButton.setOnClickListener(view -> {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.setOnDateSetListener(EditMovieActivity.this);
            Bundle args = new Bundle();
            args.putSerializable("calendar", calendar);
            newFragment.setArguments(args);
            newFragment.show(activity.getFragmentManager(), "datePicker");
        });


        attachButton.setOnClickListener((v) -> {
            Intent fileSelectIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileSelectIntent.setType("*/*");
            startActivityForResult(fileSelectIntent, FILE_REQUEST);
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void turnOnEditing() {
        controlButton.setText(R.string.save);
        isEditMode = true;
        setupEditMode(attachButton);
        controlButton.setOnClickListener(v -> {
            saveMovie();
            turnOfEditing();
        });
    }

    private void turnOfEditing() {
        isEditMode = false;
        setupEditMode(attachButton);
        controlButton.setText(R.string.to_edit);
        controlButton.setOnClickListener(view -> turnOnEditing());
    }

    private void setupEditMode(ImageButton attachButton) {
        defaultRatingBar.setEnabled(isEditMode);
        defaultRatingBar.setFocusable(isEditMode);
        langSpinner.setEnabled(isEditMode);
        langSpinner.setFocusable(isEditMode);
        nameEditText.setEnabled(isEditMode);
        nameEditText.setFocusable(isEditMode);
        descriptionEditText.setEnabled(isEditMode);
        descriptionEditText.setFocusable(isEditMode);
        setDateButton.setEnabled(isEditMode);
        setDateButton.setFocusable(isEditMode);
        imageView.setEnabled(isEditMode);
        imageView.setFocusable(isEditMode);
        adultSwitch.setEnabled(isEditMode);
        adultSwitch.setFocusable(isEditMode);
        attachButton.setEnabled(isEditMode);
        attachButton.setFocusable(isEditMode);
    }

    private void saveMovie() {
        SaveMovie saveMovie = new SaveMovie();
        saveMovie.execute();
    }

    private void initSpinnerLang() {
        ArrayAdapter<Language> adapter = new ArrayAdapter<>(activity, R.layout.simple_spinner_item, Language.values());
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(adapter);
    }

    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener onDateSetListener;

        public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
            this.onDateSetListener = onDateSetListener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = (Calendar) getArguments().getSerializable("calendar");
            @SuppressWarnings("ConstantConditions")
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(year, month, day);
        setDateButton.setText(Utils.formatDate(calendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        if (isEditMode && movieID != 0) {
            turnOfEditing();
        } else {
            super.onBackPressed();
            saveCurrentStatus();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CURR_MOVIE, movieID);
        outState.putParcelable(IMAGE_URI, imageUri);
        outState.putBoolean(IS_EDIT, isEditMode);
    }

    private void saveCurrentStatus() {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(TITLE_KEY, nameEditText.getText().toString());
        ed.putLong(RELEASE, calendar.getTimeInMillis());
        ed.putString(OVERVIEW, descriptionEditText.getText().toString());
        ed.putString(FILE_PATH, textViewFilePath.getText().toString());
        ed.putBoolean(ADULT, adultSwitch.isChecked());
        ed.putInt(LANG, langSpinner.getSelectedItemPosition());
        ed.putFloat(VOTE_AVERAGE, defaultRatingBar.getRating());
        ed.commit();
    }

    private void restoreCurrentStatus(long task_id) {
        if (task_id == 0) {
            SharedPreferences sPref = getPreferences(MODE_PRIVATE);
            nameEditText.setText(sPref.getString(TITLE_KEY, ""));
            descriptionEditText.setText(sPref.getString(OVERVIEW, ""));
            textViewFilePath.setText(sPref.getString(FILE_PATH, ""));
            adultSwitch.setChecked(sPref.getBoolean(ADULT, false));
            langSpinner.setSelection(sPref.getInt(LANG, 0));
            defaultRatingBar.setRating(sPref.getFloat(VOTE_AVERAGE, 0));
            calendar.setTimeInMillis(sPref.getLong(RELEASE, 0));
            setDateButton.setText(Utils.formatDate(calendar.getTime()));
        } else {
            Movie movie = MovieManager.INSTANCE.getMovie(task_id);
            if (movie == null) {
                return;
            }
            createTimeTextView.setText(Utils.formatDate(createTime));
            nameEditText.setText(movie.getTitle());
            descriptionEditText.setText(movie.getOverview());
            textViewFilePath.setText(movie.getFilePath());
            adultSwitch.setChecked(movie.isAdult());
            langSpinner.setSelection(movie.getOriginalLanguage().ordinal());
            defaultRatingBar.setRating(movie.getVoteAverage());
            calendar.setTimeInMillis(movie.getReleaseDate());
            setDateButton.setText(Utils.formatDate(calendar.getTime()));
            imageView.setImageDrawable(movie.getImage());
        }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent fileReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, fileReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    imageUri = fileReturnedIntent.getData();
                    imageView.setImageURI(imageUri);
                }
                break;
            case PHOTO_REQUEST:
                if (resultCode == RESULT_OK) {
                    imageUri = fileReturnedIntent.getData();
                    imageView.setImageURI(imageUri);
                }
                break;
            case FILE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedFile = fileReturnedIntent.getData();
                    textViewFilePath.setText(selectedFile.getLastPathSegment());
                }
                break;
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        formView.setVisibility(show ? View.GONE : View.VISIBLE);

        formView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                formView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private class SaveMovie extends AsyncTask<Void, Integer, Void> {
        private Movie movie;
        private boolean change = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            movie = MovieManager.INSTANCE.getMovie(movieID);
            String title = nameEditText.getText().toString();
            long releaseDate = calendar.getTimeInMillis();
            String overview = descriptionEditText.getText().toString();
            boolean adult = adultSwitch.isChecked();
            Language originalLanguage = (Language) langSpinner.getSelectedItem();
            Drawable image = imageView.getDrawable();
            float voteAvg = defaultRatingBar.getRating();
            String filePath = textViewFilePath.getText().toString();
            if (movie == null) {
                movieID = System.currentTimeMillis();
                movie = new Movie(adult, overview, releaseDate, movieID,
                        originalLanguage, title, image, voteAvg, filePath);
                change = false;
            } else {
                movie = new Movie(adult, overview, releaseDate, movie.getId(),
                        originalLanguage, title, image, voteAvg, filePath);
                change = true;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (change) {
                MovieManager.INSTANCE.changeMovie(movie);
            } else {
                MovieManager.INSTANCE.addMovie(movie);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(activity, R.string.element_saved, Toast.LENGTH_SHORT).show();
            showProgress(false);
        }
    }

}
