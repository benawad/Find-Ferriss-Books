package com.benawad;

import com.benawad.models.Book;

import java.io.File;
import java.util.List;

/**
 * Created by benawad on 7/28/15.
 */
public class BookSorter {

    public static final String AUDIOBOOK_FILE = "audiobook.txt";
    public static final String EBOOK_FILE = "ebooks.txt";

    //Audiobook or ebook
    public void sortByType(List<Book> bookList){
        String audiobooks = "";
        String ebooks = "";
        for(Book book : bookList){
            if (book.isAudiobook()){
                audiobooks += book.toString();
            }
            if(book.isEbook()){
                ebooks += book.toString();
            }
        }
        FileManager fileManager = new FileManager();
        fileManager.saveFile(audiobooks, new File(AUDIOBOOK_FILE));
        fileManager.saveFile(ebooks, new File(EBOOK_FILE));
    }
    public void sortByGenre(List<Book> bookList){

    }
}
