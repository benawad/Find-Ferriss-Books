package com.benawad.models;

import java.io.Serializable;

/**
 * Created by benawad on 7/27/15.
 */
public class Book implements Serializable {
    String title;
    String[] authors;
    String amazon;
    boolean audiobook;
    boolean ebook;
    String subtitle;
    String description;
    int pageCount;
    String[] genres;
    String google;
    String apple;
    String audiobookDesc;

    public String getApple() {
        return apple;
    }

    public void setApple(String apple) {
        this.apple = apple;
    }

    public String getAudiobookDesc() {
        return audiobookDesc;
    }

    public void setAudiobookDesc(String audiobookDesc) {
        this.audiobookDesc = audiobookDesc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getAmazon() {
        return amazon;
    }

    public void setAmazon(String amazon) {
        this.amazon = amazon;
    }

    public boolean isAudiobook() {
        return audiobook;
    }

    public void setAudiobook(boolean audiobook) {
        this.audiobook = audiobook;
    }

    public boolean isEbook() {
        return ebook;
    }

    public void setEbook(boolean ebook) {
        this.ebook = ebook;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }
}
