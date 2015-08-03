package com.benawad;

import com.benawad.models.Book;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by benawad on 8/3/15.
 */
public class DownloadRunner implements Runnable {

    JButton downloadButton;
    String apiKey;

    public DownloadRunner(JButton button, String apiKey) {
        super();
        downloadButton = button;
        this.apiKey = apiKey;
    }

    @Override
    public void run() {
        Ferriss ferriss = new Ferriss();
        GoogleBooks googleBooks = new GoogleBooks();
        java.util.List<Book> bookList = googleBooks.createBooks(ferriss.downloadAllBooks(), apiKey);
        ItuneSearch ituneSearch = new ItuneSearch();
        ituneSearch.checkIfAudiobook(bookList);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Main.BOOK_OB_FILE);
            ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
            for (Book book : bookList) {
                os.writeObject(book);
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        downloadButton.setText("Done!");
    }
}
