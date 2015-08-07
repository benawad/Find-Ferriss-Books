package com.benawad;

import com.benawad.models.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by benawad on 7/27/15.
 */
public class GoogleBooks {
    public static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    public List<Book> createBooks(List<String[]> bookList) {

        List<Book> books = new ArrayList<>();

        for (String[] book : bookList) {
            String json = "";
            for(int n = 0; n < 5; ++n){
                try {
                    System.out.println("Google Books API searching: " + book[0]);
                    json = searchBook(book[0]);
                    break;
                } catch (IOException e) {
                    //403 error = rate limit exceeded
                    //recommendation = https://developers.google.com/drive/web/handle-errors
                    //wait 1 + random_number_milliseconds
                    if(e.getMessage().contains("403")){
                        try {
                            Random randomGenerator = new Random();
                            Thread.sleep((1 << n) * 1000 + randomGenerator.nextInt(1001));
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        //if the error is not a 403 then not a rate limit error
                        e.printStackTrace();
                    }
                }
            }
            if(!json.equals("")) {
                Book newBook = makeBook(json);
                newBook.setAmazon(book[1]);
                books.add(newBook);
            } else {
                new Exception("Google Books API - rate limit exceeded");
            }
        }
        return books;
    }

    public static ArrayList<String> toArrayList(JSONArray jsonArray) {
        ArrayList<String> arraylist = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            arraylist.add((String) jsonArray.get(i));
        }
        return arraylist;
    }

    private Book makeBook(String json) {
        Book book = new Book();
        JSONObject top = new JSONObject(json);
        JSONArray items = top.getJSONArray("items");
        //get book with highest relevance
        JSONObject jsonBook = items.getJSONObject(0);
        JSONObject volumeInfo = jsonBook.getJSONObject("volumeInfo");
        book.setTitle(volumeInfo.getString("title"));
        if (volumeInfo.has("subtitle")) {
            book.setSubtitle(volumeInfo.getString("subtitle"));
        } else {
            book.setSubtitle("No subtitle");
        }
        if(volumeInfo.has("authors")) {
            book.setAuthors(toArrayList(volumeInfo.getJSONArray("authors")));
        } else {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Unknown author");
            book.setAuthors(arrayList);
        }
        if (volumeInfo.has("description")) {
            book.setDescription(volumeInfo.getString("description"));
        } else {
            book.setDescription("No description available");
        }
        if (volumeInfo.has("pageCount")) {
            book.setPageCount(volumeInfo.getInt("pageCount"));
        } else {
            book.setPageCount(0);
        }
        if(volumeInfo.has("categories")) {
            book.setGenres(toArrayList(volumeInfo.getJSONArray("categories")));
        } else {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Uncategorized");
            book.setGenres(arrayList);
        }
        if(jsonBook.getJSONObject("accessInfo").has("webReaderLink")) {
            book.setGoogle(jsonBook.getJSONObject("accessInfo").getString("webReaderLink"));
        } else {
            book.setGoogle("Link not available");
        }
        //assuming if the book is in Google's repository it must be an ebook
        book.getGenres().add("eBook");
        return book;
    }

    private String searchBook(String bookTitle) throws IOException {
        String json = "";
        URL url = new URL(API_URL + bookTitle);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = null;
        while ((line = br.readLine()) != null) {
            json += line;
        }
        br.close();
        return json;
    }
}
