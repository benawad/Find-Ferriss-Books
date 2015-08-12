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
    public static final String TITLE_TABLE = "titles";
    public static final String AUTHORS_TABLE = "authors";
    public static final String AMAZON_TABLE = "amazons";
    public static final String SUBTITLE_TABLE = "subtitles";
    public static final String PAGE_COUNT_TABLE = "pageCounts";
    public static final String DESCRIPTION_TABLE = "descriptions";
    public static final String CATEGORIES_TABLE = "categories";
    public static final String GOOGLE_TABLE = "googles";
    public static final String APPLE_TABLE = "apples";
    public static final String AUDIOBOOK_DESC_TABLE = "audiobookDescs";
    public static final String AUDIOBOOK_TABLE = "audiobooks";
    public static final String EBOOK_TABLE = "ebooks";


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

            String titleTable = "CREATE TABLE " + TITLE_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "title TEXT," +
                    "PRIMARY KEY ( id ))";

            String subtitleTable = "CREATE TABLE " + SUBTITLE_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "subtitle TEXT," +
                    "PRIMARY KEY ( id ))";

            String authorsTable = "CREATE TABLE " + AUTHORS_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "authors TEXT," +
                    "PRIMARY KEY ( id ))";

            String pageCountTable = "CREATE TABLE " + PAGE_COUNT_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "pageCount INT," +
                    "PRIMARY KEY ( id ))";

            String categoriesTable = "CREATE TABLE " + CATEGORIES_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "categories TEXT," +
                    "PRIMARY KEY ( id ))";

            String amazonTable = "CREATE TABLE " + AMAZON_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "amazon TEXT," +
                    "PRIMARY KEY ( id ))";

            String appleTable = "CREATE TABLE " + APPLE_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "apple TEXT," +
                    "PRIMARY KEY ( id ))";

            String descriptionTable = "CREATE TABLE " + DESCRIPTION_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "description TEXT," +
                    "PRIMARY KEY ( id ))";

            String googleTable = "CREATE TABLE " + GOOGLE_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "google TEXT," +
                    "PRIMARY KEY ( id ))";

            String audiobookDescTable = "CREATE TABLE " + AUDIOBOOK_DESC_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "audiobookDesc TEXT," +
                    "PRIMARY KEY ( id ))";

            String audiobookTable = "CREATE TABLE " + AUDIOBOOK_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "audiobook BOOLEAN," +
                    "PRIMARY KEY ( id ))";

            String ebookTable = "CREATE TABLE " + EBOOK_TABLE + "(" +
                    "id INT AUTO_INCREMENT," +
                    "ebook BOOLEAN," +
                    "PRIMARY KEY ( id ))";

            String booksTable = "CREATE TABLE " + BOOKS_TABLE + "(" +
                    "id INT  AUTO_INCREMENT," +
                    "title_id BIGINT ," +
                    "authors_id BIGINT ," +
                    "amazon_id BIGINT ," +
                    "subtitle_id BIGINT ," +
                    "description_id BIGINT ," +
                    "pageCount_id BIGINT ," +
                    "categories_id BIGINT ," +
                    "google_id BIGINT ," +
                    "apple_id BIGINT ," +
                    "audiobookDesc_id BIGINT ," +
                    "audiobook_id BIGINT ," +
                    "ebook_id BIGINT ," +
                    "PRIMARY KEY ( id )" +
                    ")";

            statement.executeUpdate(amazonTable);
            statement.executeUpdate(apiTable);
            statement.executeUpdate(appleTable);
            statement.executeUpdate(audiobookDescTable);
            statement.executeUpdate(audiobookTable);
            statement.executeUpdate(authorsTable);
            statement.executeUpdate(booksTable);
            statement.executeUpdate(categoriesTable);
            statement.executeUpdate(descriptionTable);
            statement.executeUpdate(ebookTable);
            statement.executeUpdate(googleTable);
            statement.executeUpdate(pageCountTable);
            statement.executeUpdate(subtitleTable);
            statement.executeUpdate(titleTable);
        } catch (Exception ex){
            // database already exists, so connect to that.
            connection = DriverManager.getConnection(dburl+dname, user, password);
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public void addBookToDatabase(Book book) throws SQLException {

        long titleId = insertAndGetId("title", TITLE_TABLE, book.getTitle());
        long authorsId = insertAndGetId("authors", AUTHORS_TABLE, new JSONArray(book.getAuthors().toArray()).toString());
        long amazonId = insertAndGetId("amazon", AMAZON_TABLE, book.getAmazon());
        long subtitleId = insertAndGetId("subtitle", SUBTITLE_TABLE, book.getSubtitle());
        long descriptionId = insertAndGetId("description", DESCRIPTION_TABLE, book.getDescription());
        long pageCountId = insertAndGetId("pageCount", PAGE_COUNT_TABLE, book.getPageCount());
        long categoriesId = insertAndGetId("categories", CATEGORIES_TABLE, new JSONArray(book.getCategories().toArray()).toString());
        long googleId = insertAndGetId("google", GOOGLE_TABLE, book.getGoogle());
        long appleId = insertAndGetId("apple", APPLE_TABLE, book.getApple());
        long audiobookDescId = insertAndGetId("audiobookDesc", AUDIOBOOK_DESC_TABLE, book.getAudiobookDesc());
        long audiobookId = insertAndGetId("audiobook", AUDIOBOOK_TABLE, book.isAudiobook());
        long ebookId = insertAndGetId("ebook", EBOOK_TABLE, book.isEbook());

        String bookInsert = "INSERT INTO " + BOOKS_TABLE +
                "(title_id, authors_id, amazon_id, subtitle_id, description_id, pageCount_id, categories_id, google_id, apple_id, audiobookDesc_id, audiobook_id, ebook_id)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(bookInsert);
        preparedStatement.setLong(1, titleId);
        preparedStatement.setLong(2, authorsId);
        preparedStatement.setLong(3, amazonId);
        preparedStatement.setLong(4, subtitleId);
        preparedStatement.setLong(5, descriptionId);
        preparedStatement.setLong(6, pageCountId);
        preparedStatement.setLong(7, categoriesId);
        preparedStatement.setLong(8, googleId);
        preparedStatement.setLong(9, appleId);
        preparedStatement.setLong(10, audiobookDescId);
        preparedStatement.setLong(11, audiobookId);
        preparedStatement.setLong(12, ebookId);

        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    public long insertAndGetId(String columnName, String tableName, String value) throws SQLException {
        String insert = "INSERT INTO " + tableName + "(" + columnName + ") VALUES(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, value);
        int affectedRows = preparedStatement.executeUpdate();

        if(affectedRows == 0){
            throw new SQLException("No rows affected when inserting " + value + " into " + tableName);
        }

        long id = -1;

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if(generatedKeys.next()){
            id = generatedKeys.getLong(1);
        } else {
            throw new SQLException("No ID obtained when inserting " + value + " into " + tableName);
        }

        return id;
    }

    public long insertAndGetId(String columnName, String tableName, int value) throws SQLException {
        String insert = "INSERT INTO " + tableName + "(" + columnName + ") VALUES(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, value);
        int affectedRows = preparedStatement.executeUpdate();

        if(affectedRows == 0){
            throw new SQLException("No rows affected when inserting " + value + " into " + tableName);
        }

        long id = -1;

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if(generatedKeys.next()){
            id = generatedKeys.getLong(1);
        } else {
            throw new SQLException("No ID obtained when inserting " + value + " into " + tableName);
        }

        return id;
    }

    public long insertAndGetId(String columnName, String tableName, boolean value) throws SQLException {
        String insert = "INSERT INTO " + tableName + "(" + columnName + ") VALUES(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setBoolean(1, value);
        int affectedRows = preparedStatement.executeUpdate();

        if(affectedRows == 0){
            throw new SQLException("No rows affected when inserting " + value + " into " + tableName);
        }

        long id = -1;

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if(generatedKeys.next()){
            id = generatedKeys.getLong(1);
        } else {
            throw new SQLException("No ID obtained when inserting " + value + " into " + tableName);
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
