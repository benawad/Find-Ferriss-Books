package com.benawad;

import com.benawad.models.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by benawad on 7/27/15.
 */
public class ItuneSearch {
    public static final String API_URL = "https://itunes.apple.com/search?term=";
    public static final String AUDIOBOOK = "Audiobook";
    public void checkIfAudiobook(List<Book> books) {
        for(Book book : books){
            System.out.println("Itunes API searching: "+ book.getTitle());
            String json = searchBook(book);
            checkBook(json, book);
        }
    }

    private void checkBook(String json, Book book) {
        JSONObject top = new JSONObject(json);
        if(top.getInt("resultCount") != 0){
            JSONArray results = top.getJSONArray("results");
            JSONObject result1 = results.getJSONObject(0);
            if(result1.getString("wrapperType").equals("audiobook")){
                book.setAudiobook(true);
                book.setAudiobookDesc(result1.getString("description"));
            } else {
                book.setAudiobookDesc("NA");
            }
            if(result1.has("collectionViewUrl")) {
                book.setApple(result1.getString("collectionViewUrl"));
            } else {
                book.setApple("Link not available");
            }
        } else {
            book.setAudiobook(false);
            book.setApple("Link not available");
            book.setAudiobookDesc("NA");
        }

    }

    private String searchBook(Book book) {
        String json = "";
        try {
            //makes sure the book does not have any characters that would mess up the url
            String title = URLEncoder.encode(book.getTitle(), "UTF-8");
            URL url = new URL(API_URL + title );
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                json += line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
