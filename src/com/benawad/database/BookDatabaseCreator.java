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
                "id INT NOT NULL AUTO_INCREMENT," +
                "title TEXT NOT NULL," +
                "authors TEXT NOT NULL," +
                "amazon TEXT NOT NULL," +
                "subtitle TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "pageCount INT NOT NULL," +
                "genres TEXT NOT NULL," +
                "google TEXT NOT NULL," +
                "apple TEXT NOT NULL," +
                "audiobookDesc TEXT NOT NULL," +
                "PRIMARY KEY ( id )" +
                ")";
        System.out.println(createTable);
        statement.executeUpdate(createTable);
        System.out.println(BOOKS_TABLE + " table successfully created.");

    }

    public void addBookToDatabase(Book book) throws SQLException {
        String title = "'"+book.getTitle()+"', ";
        // converting array to string to be stored in database
        String authors = "'"+new JSONArray(book.getAuthors()).toString()+"', ";
        String amazon = "'"+book.getAmazon()+"', ";
        String subtitle = "'"+book.getSubtitle()+"', ";
        String description = "'"+book.getDescription()+"', ";
        String pageCount = book.getPageCount()+", ";
        // converting array to string to be stored in database
        String genres = "'"+new JSONArray(book.getGenres()).toString()+"', ";
        String google = "'"+book.getGoogle()+"', ";
        String apple = "'"+book.getApple()+"', ";
        String audiobookDesc = "'"+book.getAudiobookDesc()+"'";

        String sql = "INSERT INTO " + BOOKS_TABLE +
                "(title, authors, amazon, subtitle, description, pageCount, genres, google, apple, audiobookDesc" +
                "VALUES" +
                "(" + title + authors + amazon + subtitle + description + pageCount + genres + google + apple + audiobookDesc + ")";
        Statement statement = connection.createStatement();
        // insert book into table
        statement.executeUpdate(sql);
        close(statement, null);
        System.out.println(book.getTitle() + " has been successfully entered into the " +BOOKS_TABLE+ " table");

    }

    private static void close(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
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

}
