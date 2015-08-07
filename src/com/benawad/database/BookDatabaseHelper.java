package com.benawad.database;

import com.benawad.GoogleBooks;
import com.benawad.models.Book;
import org.json.JSONArray;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by benawad on 8/5/15.
 */
public class BookDatabaseHelper {
    private Connection connection;

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
            resultSet = statement.executeQuery("SELECT * FROM books");

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
        ArrayList<String> authors = GoogleBooks.toArrayList(new JSONArray(resultSet.getString("authors")));
        ArrayList<String> genres = GoogleBooks.toArrayList(new JSONArray(resultSet.getString("genres")));
        Book book = new Book(resultSet.getString("title"), authors, resultSet.getString("amazon"), resultSet.getString("subtitle"), resultSet.getString("description"), resultSet.getInt("pageCount"), genres, resultSet.getString("google"), resultSet.getString("apple"), resultSet.getString("audiobookDesc"));
        return book;
    }

}
