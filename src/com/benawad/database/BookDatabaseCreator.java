package com.benawad.database;

import com.benawad.models.Book;
import org.json.JSONArray;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Created by benawad on 8/5/15.
 */
public class BookDatabaseCreator {

    private Connection connection;
    public static final String BOOKS_TABLE = "books";

    public BookDatabaseCreator() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("database.properties"));

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String dburl = properties.getProperty("dburl");
        String dname = properties.getProperty("dname");

        try {
            connection = DriverManager.getConnection(dburl, user, password);
            Statement statement = connection.createStatement();

            // create the database
            String sql = "CREATE DATABASE " + dname;
            statement.executeUpdate(sql);
            System.out.println(dname + " database created successfully");

            // connect to the newly created database
            connection.setCatalog(dname);
            // create a new statement
            // setCataglog() does not change statement object
            statement = connection.createStatement();

            // create table
            String createTable = "CREATE TABLE " + BOOKS_TABLE + "(" +
                    "id INT  AUTO_INCREMENT," +
                    "title TEXT ," +
                    "authors TEXT ," +
                    "amazon TEXT ," +
                    "subtitle TEXT ," +
                    "description TEXT ," +
                    "pageCount INT ," +
                    "genres TEXT ," +
                    "google TEXT ," +
                    "apple TEXT ," +
                    "audiobookDesc TEXT ," +
                    "audiobook BOOLEAN ," +
                    "ebook BOOLEAN ," +
                    "PRIMARY KEY ( id )" +
                    ")";
            statement.executeUpdate(createTable);
            System.out.println(BOOKS_TABLE + " table successfully created.");
        } catch (Exception ex){
            // database already exists, so connect to that.
            connection = DriverManager.getConnection(dburl+dname, user, password);
        }
    }

    public void addBookToDatabase(Book book) throws SQLException {
        String sql = "INSERT IGNORE INTO " + BOOKS_TABLE +
                "(title, authors, amazon, subtitle, description, pageCount, genres, google, apple, audiobookDesc, audiobook, ebook)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, book.getTitle());
        preparedStatement.setString(2, new JSONArray(book.getAuthors().toArray()).toString());
        preparedStatement.setString(3, book.getAmazon());
        preparedStatement.setString(4, book.getSubtitle());
        preparedStatement.setString(5, book.getDescription());
        preparedStatement.setInt(6, book.getPageCount());
        preparedStatement.setString(7, new JSONArray(book.getCategories().toArray()).toString());
        preparedStatement.setString(8, book.getGoogle());
        preparedStatement.setString(9, book.getApple());
        preparedStatement.setString(10, book.getAudiobookDesc());
        preparedStatement.setBoolean(11, book.isAudiobook());
        preparedStatement.setBoolean(12, book.isEbook());
        preparedStatement.execute();
        preparedStatement.close();
        System.out.println(book.getTitle() + " has been successfully entered into the " +BOOKS_TABLE+ " table");
    }

}
