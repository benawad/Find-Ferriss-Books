package com.benawad.database;

import com.benawad.GoogleBooks;
import com.benawad.models.Book;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.benawad.database.BookDatabaseCreator.*;

/**
 * Created by benawad on 8/5/15.
 */
public class BookDatabaseHelper {
    private Connection connection;

    private static final String joinTables = "SELECT * "
            + "FROM " + BOOKS_TABLE + "," + TITLE_TABLE + "," + SUBTITLE_TABLE + "," + AUTHORS_TABLE + "," + PAGE_COUNT_TABLE + "," + CATEGORIES_TABLE + "," + DESCRIPTION_TABLE + "," + AMAZON_TABLE + "," + GOOGLE_TABLE + "," + APPLE_TABLE + "," + AUDIOBOOK_DESC_TABLE + "," + AUDIOBOOK_TABLE + "," + EBOOK_TABLE +
            " WHERE " + TITLE_TABLE + ".id=title_id and " + SUBTITLE_TABLE + ".id=subtitle_id and " + AUTHORS_TABLE + ".id=authors_id and " + PAGE_COUNT_TABLE + ".id=pageCount_id and " + CATEGORIES_TABLE + ".id=categories_id and " + DESCRIPTION_TABLE + ".id=description_id and " + AMAZON_TABLE + ".id=amazon_id and " + GOOGLE_TABLE + ".id=google_id and " + APPLE_TABLE + ".id=apple_id and " + AUDIOBOOK_DESC_TABLE + ".id=audiobookDesc_id and " + AUDIOBOOK_TABLE + ".id=audiobook_id and " + EBOOK_TABLE + ".id=ebook_id";

    public BookDatabaseHelper() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("database.properties"));

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String dburl = properties.getProperty("dburl") + properties.getProperty("dname");

        connection = DriverManager.getConnection(dburl, user, password);

        System.out.println("Database connection successful to: " + dburl);
    }

    public List<Book> getAllBooks() throws Exception {
        List<Book> bookList = new ArrayList<>();

        Statement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(joinTables);

            while(resultSet.next()){
                Book book = convertRowToBook(resultSet);
                bookList.add(book);
            }
            return bookList;
        } finally {
            close(statement, resultSet);
        }
    }

    private static void close(Connection connection, Statement statement, ResultSet resultSet)
            throws SQLException {

        if (resultSet != null) {
            resultSet.close();
        }

        if (statement != null) {
            statement.close();
        }

        if (connection != null) {
            connection.close();
        }
    }

    private void close(Statement statement, ResultSet resultSet) throws SQLException {
        close(null, statement, resultSet);
    }

    private Book convertRowToBook(ResultSet resultSet) throws SQLException{
        ArrayList<String> authors = GoogleBooks.toStringArrayList(new JSONArray(resultSet.getString("authors")));
        boolean audiobook = false;
        boolean ebook = false;
        if(resultSet.getBoolean("audiobook")){
            audiobook = true;
        }
        if(resultSet.getBoolean("ebook")){
            ebook = true;
        }
        ArrayList<String> genres = GoogleBooks.toStringArrayList(new JSONArray(resultSet.getString("categories")));
        Book book = new Book(resultSet.getString("title"), authors, resultSet.getString("amazon"), resultSet.getString("subtitle"), resultSet.getString("description"), resultSet.getInt("pageCount"), genres, resultSet.getString("google"), resultSet.getString("apple"), resultSet.getString("audiobookDesc"), audiobook, ebook);
        return book;
    }

    public String getApiKey() throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;

        statement = connection.createStatement();
        String sql = "SELECT * FROM " + BookDatabaseCreator.API_TABLE;
        resultSet = statement.executeQuery(sql);

        String apiKey = "";
        if(resultSet.next()) {
            apiKey = resultSet.getString("apiKey");
        }

        return apiKey;
    }

    public Book getBook(String title) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;

        String encodedTitle = "'"+StringEscapeUtils.escapeEcmaScript(title) + "'";
        statement = connection.createStatement();
        String sql = joinTables + " and titles.title=" + encodedTitle;
        resultSet = statement.executeQuery(sql);

        Book book = null;
        if(resultSet.next()) {
            book = convertRowToBook(resultSet);
        }

        return book;

    }

}
