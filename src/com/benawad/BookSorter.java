package com.benawad;

import com.benawad.models.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benawad on 7/28/15.
 */
public class BookSorter {

   public static List<Book> sort(boolean ebooks, boolean audiobooks, boolean all, String category, List<Book> bookList) {
      List<Book> rightBooks = new ArrayList<>();

      if(ebooks){
         for(Book book : bookList){
            boolean matchesCategory = contains(category, book.getCategories());
            if(book.isAudiobook() != true && matchesCategory == true){
               rightBooks.add(book);
            }
         }
      } else if (audiobooks){
           for(Book book : bookList){
            boolean matchesCategory = contains(category, book.getCategories());
            if(book.isAudiobook() == audiobooks && matchesCategory == true){
               rightBooks.add(book);
            }
         }
      } else if (all){
         for(Book book : bookList){
            boolean matchesCategory = contains(category, book.getCategories());
            if(matchesCategory){
               rightBooks.add(book);
            }
         }
      }
      return  rightBooks;
   }

   public static List<Book> sort(boolean ebooks, boolean audiobooks, boolean all, List<Book> bookList) {
      List<Book> rightBooks = new ArrayList<>();

      if(ebooks){
         for(Book book : bookList){
            if(book.isAudiobook() != true){
               rightBooks.add(book);
            }
         }
      } else if (audiobooks){
         for(Book book : bookList){
            if(book.isAudiobook() == audiobooks){
               rightBooks.add(book);
            }
         }
      } else if (all){
         rightBooks = bookList;
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
