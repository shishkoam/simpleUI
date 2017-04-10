package com.yo.shishkoam.simpleui.model;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.yo.shishkoam.simpleui.helpers.Utils;
import com.yo.shishkoam.simpleui.types.Language;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by User on 08.04.2017
 */

public class Movie implements Serializable{
    private boolean adult;
    private String overview;
    private long releaseDate;
    private long id;
    private Language originalLanguage;
    private String title;
    private Drawable image;
    private Float voteAverage;
    private String filePath;

    public Movie(boolean adult, String overview, long releaseDate, long id, Language originalLanguage,
                 String title, Drawable image, Float voteAverage, String filePath) {
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.image = image;
        this.voteAverage = voteAverage;
        this.filePath = filePath;
    }

    public Movie(boolean adult, String overview, long releaseDate, long id, Language originalLanguage,
                 String title, byte[] image, Float voteAverage, String filePath) {
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.image = Utils.getImage(image);
        this.voteAverage = voteAverage;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDateString () {
        return Utils.formatDate(releaseDate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Language getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(Language originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImageBytes() {

        return Utils.getBytes(image);
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
    public Drawable getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = Utils.getImage(image);
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }
}
