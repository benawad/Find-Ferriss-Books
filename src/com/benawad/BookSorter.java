package com.benawad;

import com.benawad.models.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benawad on 7/28/15.
 */
public class BookSorter {

   public static List<Book> sort(boolean ebooks, boolean audiobooks, String category, List<Book> bookList) {
      List<Book> rightBooks = new ArrayList<>();

      for(Book book : bookList){
         boolean matchesCategory = contains(category, book.getCategories());
         if(book.isEbook() == ebooks && book.isAudiobook() == audiobooks && matchesCategory == true){
            rightBooks.add(book);
         }
      }

      return  rightBooks;
   }

   public static List<Book> sort(boolean ebooks, boolean audiobooks, List<Book> bookList) {
      List<Book> rightBooks = new ArrayList<>();

      for(Book book : bookList){
         if(book.isEbook() == ebooks && book.isAudiobook() == audiobooks){
            rightBooks.add(book);
         }
      }

      return  rightBooks;
   }

   private static boolean contains(String word, List<String> words){
      boolean contains = false;
      for(String w : words){
         if(word.equals(w)){
            contains = true;
         }
      }
      return contains;
   }

}
