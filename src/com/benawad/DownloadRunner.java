package com.benawad;

import com.benawad.database.BookDatabaseCreator;
import com.benawad.gui.Main;
import com.benawad.models.Book;

import javax.swing.*;
import java.util.List;

/**
 * Created by benawad on 8/3/15.
 */
public class DownloadRunner implements Runnable {

    Main main;
    BookDatabaseCreator creator;

    public DownloadRunner(Main m, BookDatabaseCreator bookDatabaseCreator) {
        super();
        main = m;
        creator = bookDatabaseCreator;
    }

    @Override
    public void run() {
        main.isDownloading = true;

        try {
            Ferriss ferriss = new Ferriss();
            GoogleBooks googleBooks = new GoogleBooks();
            List<Book> bookList = googleBooks.createBooks(ferriss.downloadAllBooks());
            if(bookList == null){
                JOptionPane.showMessageDialog(null, "Invalid API Key entered", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                ItuneSearch ituneSearch = new ItuneSearch();
                ituneSearch.checkIfAudiobook(bookList);

                for (Book book : bookList) {
                    creator.addBookToDatabase(book);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            main.isDownloading = false;
        }

        main.refreshScreen();

    }
}
