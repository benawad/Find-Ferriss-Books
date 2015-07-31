package com.benawad;

import com.benawad.models.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    //we want to make the API folder outside the project structure so we don't accidentally share our API key with git
    public static final String API_KEY_FOLDER = System.getProperty("user.home")+"/Find-Ferriss-Books-API-key";
    public static final String API_KEY_FILE = API_KEY_FOLDER + "/key.txt";
    public static final String BOOK_OB_FILE = "books.ser";

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        //takes a little bit to download the list of books
        //make sure they want to update it
        File bookFile = new File(Ferriss.BOOK_FILE);
        Ferriss ferriss = new Ferriss();
        if (bookFile.exists()) {
            while (true) {
                System.out.println("You have already downloaded the list of books.");
                System.out.println("Do you want to update the list (y/n) >");
                String answer = scan.nextLine();
                if (answer.equals("y")) {
                    ferriss.downloadAllBooks();
                    break;
                } else if (answer.equals("n")) {
                    break;
                }
            }

        } else {
            ferriss.downloadAllBooks();
        }

        //program needs to access Google Books api
        //if this is the user's first time running the app they need
        //to go to https://developers.google.com/console/help/new/#generatingdevkeys and make an api key
        File keyFile = new File(API_KEY_FILE);
        File keyFolder = new File(API_KEY_FOLDER);
        if(!keyFolder.exists()){
            keyFolder.mkdir();
        }
        if (!keyFile.exists()) {
            System.out.println("Please visit https://developers.google.com/console/help/new/#generatingdevkeys and get an API key (make sure to enable the Google Books api)");
            System.out.println("An api key is not required to access the Google Books api, but it is recommended. I tried running the program without a key and had errors with exceeding the rate limit. If you don't want to get an API key leave the line below blank.");
            System.out.print("Enter Google API key >");
            String apiKey = scan.nextLine();
            FileManager fileManager = new FileManager();
            fileManager.saveFile(apiKey, keyFile);
        }

        File bookObjects = new File(BOOK_OB_FILE);
        boolean updateBookObs = true;
        if(bookObjects.exists()){
            while(true) {
                System.out.println("Already collected data from the Google API for the books");
                System.out.print("Would you like to update the data (y/n) >");
                String answer = scan.nextLine();
                if(answer.equals("y")){
                    break;
                } else if (answer.equals("n")){
                    updateBookObs = false;
                    break;
                }
            }
        }
        if(updateBookObs) {
            FileManager fm = new FileManager();
            List<String[]> bookList = fm.readList(new File(Ferriss.BOOK_FILE));
            GoogleBooks googleBooks = new GoogleBooks();
            List<Book> books = googleBooks.createBooks(bookList);
            ItuneSearch ituneSearch = new ItuneSearch();
            ituneSearch.checkIfAudiobook(books);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(BOOK_OB_FILE);
                ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
                for (Book book : books) {
                    os.writeObject(book);
                }
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Book> bookList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(BOOK_OB_FILE);
            ObjectInputStream os = new ObjectInputStream(fileInputStream);
            Book book;
            while((book = (Book)os.readObject()) != null){
                bookList.add(book);
            }
            os.close();
        } catch (IOException | ClassNotFoundException e) {
            //EOFException exception is expected
            e.printStackTrace();
        }

        //sort books into text files
        BookSorter sorter = new BookSorter();
        sorter.sortByType(bookList);

    }
}
