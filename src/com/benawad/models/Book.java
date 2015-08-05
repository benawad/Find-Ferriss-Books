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

    public Book(){}

    public Book(String title, String[] authors, String amazon, String subtitle, String description, int pageCount, String[] genres, String google, String apple, String audiobookDesc) {
        this.title = title;
        this.authors = authors;
        this.amazon = amazon;
        this.subtitle = subtitle;
        this.description = description;
        this.pageCount = pageCount;
        this.genres = genres;
        this.google = google;
        this.apple = apple;
        this.audiobookDesc = audiobookDesc;
    }

    public String toString(){
        String info = "";
        String fTitle = String.format("%-12s%s\n","Title:", title);
        String fSubtitle = String.format("%-12s%s\n","Subtitle:", subtitle);
        String sAuthors = "";
        for(int i = 0; i< authors.length; i++){
            sAuthors += authors[i];
            if(i != authors.length-1){
                sAuthors += ", ";
            }
        }
        String fAuthors = String.format("%-12s%s\n", "Authors:", sAuthors);
        String fDescription = String.format("%-12s%s\n", "Google's description: ", description);
        String fAmazon = String.format("%-12s%s\n", "Amazon:", amazon);
        String fApple = String.format("%-12s%s\n", "Itunes:", apple);
        String fGoogle = String.format("%-12s%s\n", "Google:", google);
        String fPageCount = String.format("%-12s%s\n", "Pages:", pageCount);
        String sGenres = "";
        for(int i = 0; i < genres.length; i++){
            sGenres += genres[i];
            if(i != genres.length-1){
                sGenres += ", ";
            }
        }
        String fGenres = String.format("%-12s%s\n", "Genres:", sGenres);
        String fAudiobookDesc = String.format("%-12s%s\n", "Itune's description: ", audiobookDesc);
        String formats = "";
        if(ebook){
            formats += "Ebook";
        }
        if(audiobook && ebook){
            formats += ", Audiobook";
        } else if (audiobook){
            formats += "Audiobook";
        }
        if(!audiobook && !ebook){
            formats += "Print";
        }

        String fFormat = String.format("%-12s%s\n", "Formats:", formats);
        info += fTitle + fSubtitle + fAuthors + fDescription + fAudiobookDesc + fPageCount + fGenres + fFormat + fAmazon + fApple + fGoogle;
        String seperator = "=============================================\n";
        info += seperator;
        return info;
    }

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
