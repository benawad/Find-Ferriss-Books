package com.benawad;

import com.benawad.database.BookDatabaseCreator;
import com.benawad.models.Book;

import java.sql.SQLException;

/**
 * Created by benawad on 8/3/15.
 */
public class DownloadRunner implements Runnable {

    @Override
    public void run() {
        Ferriss ferriss = new Ferriss();
        GoogleBooks googleBooks = new GoogleBooks();
        java.util.List<Book> bookList = googleBooks.createBooks(ferriss.downloadAllBooks());
        ItuneSearch ituneSearch = new ItuneSearch();
        ituneSearch.checkIfAudiobook(bookList);
        BookDatabaseCreator creator = null;
        try {
            creator = new BookDatabaseCreator();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            for(Book book : bookList){
                creator.addBookToDatabase(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
