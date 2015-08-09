package com.benawad;

import com.benawad.database.BookDatabaseCreator;
import com.benawad.gui.Main;
import com.benawad.models.Book;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by benawad on 8/3/15.
 */
public class DownloadRunner implements Runnable {

    Main main;

    public DownloadRunner(Main m){
        super();
        main = m;
    }

    @Override
    public void run() {
        Ferriss ferriss = new Ferriss();
        GoogleBooks googleBooks = new GoogleBooks();
        List<Book> bookList = googleBooks.createBooks(ferriss.downloadAllBooks());
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

        main.refreshScreen();

    }
}
