package com.benawad;

import com.benawad.database.BookDatabaseHelper;
import com.benawad.models.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by benawad on 7/27/15.
 */
public class GoogleBooks {

    public static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    String apiKey = "";

    public List<Book> createBooks(List<String[]> bookList) {

        try {
            BookDatabaseHelper helper = new BookDatabaseHelper();
            apiKey = helper.getApiKey();
            if (!apiKey.isEmpty()) {
                apiKey = "&key=" + apiKey;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Book> books = new ArrayList<>();

        for (String[] book : bookList) {
            String json = "";
            for (int n = 0; n < 5; ++n) {
                try {
                    System.out.println("Google Books API searching: " + book[0]);
                    json = searchBook(book[0]);
                    break;
                } catch (IOException e) {
                    //403 error = rate limit exceeded
                    //recommendation = https://developers.google.com/drive/web/handle-errors
                    //wait 1 + random_number_milliseconds
                    if (e.getMessage().contains("403")) {
                        try {
                            Random randomGenerator = new Random();
                            Thread.sleep((1 << n) * 1000 + randomGenerator.nextInt(1001));
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    } else if (e.getMessage().contains("400")) {
                        // invalid api key
                        return null;
                    } else {
                        //if the error is not a 403 then not a rate limit error
                        e.printStackTrace();
                    }
                }
            }
            if (!json.equals("")) {
                Book newBook = makeBook(json);
                newBook.setAmazon(book[1]);
                books.add(newBook);
            } else {
                new Exception("Google Books API - rate limit exceeded");
            }
        }
        return books;
    }

    public static ArrayList<String> toStringArrayList(JSONArray jsonArray) {
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
        if (volumeInfo.has("authors")) {
            book.setAuthors(toStringArrayList(volumeInfo.getJSONArray("authors")));
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
        if (volumeInfo.has("categories")) {
            book.setCategories(toStringArrayList(volumeInfo.getJSONArray("categories")));
        } else {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Uncategorized");
            book.setCategories(arrayList);
        }
        if (volumeInfo.has("industryIdentifiers")){
            JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
            for (int i = 0; i < industryIdentifiers.length(); i++){
                String type = industryIdentifiers.getJSONObject(i).getString("type");
                if(type.equals("ISBN_10")){
                    book.setIsbn10(industryIdentifiers.getJSONObject(i).getString("identifier"));
                }
                if(type.equals("ISBN_13")){
                    book.setIsbn13(industryIdentifiers.getJSONObject(i).getString("identifier"));
                }
            }
        }
        if (jsonBook.getJSONObject("accessInfo").has("webReaderLink")) {
            book.setGoogle(jsonBook.getJSONObject("accessInfo").getString("webReaderLink"));
        } else {
            book.setGoogle("Link not available");
        }
        //assuming if the book is in Google's repository it must be an ebook
        book.setEbook(true);
        return book;
    }

    private String searchBook(String bookTitle) throws IOException {
        String json = "";

        URL url = new URL(API_URL + URLEncoder.encode(bookTitle, "UTF-8") + apiKey);

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = null;
        while ((line = br.readLine()) != null) {
            json += line;
        }
        br.close();
        return json;
    }
}
