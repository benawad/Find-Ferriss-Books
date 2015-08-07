package com.benawad.database;

import com.benawad.models.Book;
import org.apache.commons.lang3.StringEscapeUtils;
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
        String title = "'"+ StringEscapeUtils.escapeEcmaScript(book.getTitle())+"', ";
        // converting array to string to be stored in database
        String authors = "'"+StringEscapeUtils.escapeEcmaScript(new JSONArray(book.getAuthors().toArray()).toString())+"', ";
        String amazon = "'"+StringEscapeUtils.escapeEcmaScript(book.getAmazon())+"', ";
        String subtitle = "'"+StringEscapeUtils.escapeEcmaScript(book.getSubtitle())+"', ";
        String description = "'"+StringEscapeUtils.escapeEcmaScript(book.getDescription())+"', ";
        String pageCount = book.getPageCount()+", ";
        // converting array to string to be stored in database
        String genres = "'"+StringEscapeUtils.escapeEcmaScript(new JSONArray(book.getGenres().toArray()).toString())+"', ";
        String google = "'"+StringEscapeUtils.escapeEcmaScript(book.getGoogle())+"', ";
        String apple = "'"+StringEscapeUtils.escapeEcmaScript(book.getApple())+"', ";
        String audiobookDesc = "'"+StringEscapeUtils.escapeEcmaScript(book.getAudiobookDesc())+"'";

        String sql = "INSERT IGNORE INTO " + BOOKS_TABLE +
                "(title, authors, amazon, subtitle, description, pageCount, genres, google, apple, audiobookDesc)" +
                " VALUES" +
                " (" + title + authors + amazon + subtitle + description + pageCount + genres + google + apple + audiobookDesc + ")";
        System.out.println(sql);
        System.out.println(new JSONArray(book.getAuthors().toArray()).toString());
        System.out.println(StringEscapeUtils.escapeEcmaScript(new JSONArray(book.getAuthors().toArray()).toString()));
        System.out.println(authors);
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
