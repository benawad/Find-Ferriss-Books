package com.benawad.tests;

import com.benawad.models.Book;

/**
 * Created by benawad on 7/28/15.
 */
public class BookToStringTest {
    public static void test() {
        Book book = new Book();
        book.setAmazon("amazon.com");
        book.setApple("apple.com");
        book.setAudiobook(true);
        book.setAuthors(new String[]{"Brian Thomas, James Conthar"});
        book.setDescription("I am a description of a book");
        book.setEbook(true);
        book.setGenres(new String[]{"Comedy", "Romantic", "Action"});
        book.setGoogle("google.com");
        book.setPageCount(204);
        book.setSubtitle("A very serious subtitle");
        book.setTitle("A very real Title");
        System.out.println(book.toString());
        Book book2 = new Book();
        book2.setAmazon("amazon.com");
        book2.setApple("apple.com");
        book2.setAudiobook(true);
        book2.setAuthors(new String[]{"Brian Thomas, James Conthar"});
        book2.setDescription("I am a description of a book");
        book2.setEbook(true);
        book2.setGenres(new String[]{"Comedy", "Romantic", "Action"});
        book2.setGoogle("google.com");
        book2.setPageCount(204);
        book2.setSubtitle("A very serious subtitle");
        book2.setTitle("A very real Title");
        System.out.println(book2);
    }
}
