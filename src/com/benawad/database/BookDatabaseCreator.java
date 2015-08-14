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
    public static final String API_TABLE = "api_key";
    public static final String AUTHORS_TABLE = "authors";
    public static final String CATEGORIES_TABLE = "categories";

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

            String apiTable = "CREATE TABLE " + API_TABLE + "(" +
                    "id INT," +
                    "apiKey TEXT,"+
                    "PRIMARY KEY ( id ))";

            String authorsTable = "CREATE TABLE " + AUTHORS_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "authors TEXT," +
                    "PRIMARY KEY ( id ))";

            String categoriesTable = "CREATE TABLE " + CATEGORIES_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "categories TEXT," +
                    "PRIMARY KEY ( id ))";

            String booksTable = "CREATE TABLE " + BOOKS_TABLE + "(" +
                    "id INT  AUTO_INCREMENT," +
                    "title TEXT ," +
                    "authors_id BIGINT ," +
                    "amazon TEXT ," +
                    "subtitle TEXT ," +
                    "description TEXT ," +
                    "pageCount INT ," +
                    "categories_id BIGINT ," +
                    "google TEXT ," +
                    "apple TEXT ," +
                    "audiobookDesc TEXT ," +
                    "audiobook BOOLEAN ," +
                    "ebook BOOLEAN ," +
                    "isbn_10 TEXT," +
                    "isbn_13 TEXT," +
                    "PRIMARY KEY ( id )" +
                    ")";

            statement.executeUpdate(apiTable);
            statement.executeUpdate(authorsTable);
            statement.executeUpdate(booksTable);
            statement.executeUpdate(categoriesTable);
        } catch (Exception ex){
            // database already exists, so connect to that.
            connection = DriverManager.getConnection(dburl+dname, user, password);
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public void addBookToDatabase(Book book) throws SQLException {

        String duplicateCheck = "SELECT * FROM " + BOOKS_TABLE + " WHERE isbn_10=? and isbn_13=?";
        PreparedStatement duplicateCheckStatement = connection.prepareStatement(duplicateCheck);
        duplicateCheckStatement.setString(1, book.getIsbn10());
        duplicateCheckStatement.setString(2, book.getIsbn13());
        ResultSet resultSet = duplicateCheckStatement.executeQuery();
        // if we find a match that means the isbn is already in the database and we don't want the book.
        if(!resultSet.next()) {
            long authorsId = insertAndGetId("authors", AUTHORS_TABLE, new JSONArray(book.getAuthors().toArray()).toString());
            long categoriesId = insertAndGetId("categories", CATEGORIES_TABLE, new JSONArray(book.getCategories().toArray()).toString());

            String bookInsert = "INSERT INTO " + BOOKS_TABLE +
                    "(title, authors_id, amazon, subtitle, description, pageCount, categories_id, google, apple, audiobookDesc, audiobook, ebook, isbn_10, isbn_13)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(bookInsert);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setLong(2, authorsId);
            preparedStatement.setString(3, book.getAmazon());
            preparedStatement.setString(4, book.getSubtitle());
            preparedStatement.setString(5, book.getDescription());
            preparedStatement.setInt(6, book.getPageCount());
            preparedStatement.setLong(7, categoriesId);
            preparedStatement.setString(8, book.getGoogle());
            preparedStatement.setString(9, book.getApple());
            preparedStatement.setString(10, book.getAudiobookDesc());
            preparedStatement.setBoolean(11, book.isAudiobook());
            preparedStatement.setBoolean(12, book.isEbook());
            preparedStatement.setString(13, book.getIsbn10());
            preparedStatement.setString(14, book.getIsbn13());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }

    }

    public long insertAndGetId(String columnName, String tableName, String value) throws SQLException {

        String findDuplicate = "SELECT * FROM " + tableName + " WHERE "+ tableName + "." + columnName + "=?";

        PreparedStatement statement = connection.prepareStatement(findDuplicate);

        statement.setString(1, value);

        ResultSet resultSet = statement.executeQuery();

        long id;

        if(resultSet.next()){
            id = resultSet.getLong("id");
        } else {
            String insert = "INSERT INTO " + tableName + "(" + columnName + ") VALUES(?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, value);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No rows affected when inserting " + value + " into " + tableName);
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            } else {
                throw new SQLException("No ID obtained when inserting " + value + " into " + tableName);
            }
        }

        return id;
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public void saveApiKey(String apikey) throws SQLException {
        String sql = "INSERT INTO " + API_TABLE + " (id, apiKey) VALUES(?,?) ON DUPLICATE KEY UPDATE apiKey=VALUES(apiKey)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 1);
        preparedStatement.setString(2, apikey);
        preparedStatement.execute();
        preparedStatement.close();
    }

}
