package com.benawad;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static final String API_KEY_FOLDER = System.getProperty("user.home")+"/Find-Ferriss-Books-API-key";
    public static final String API_KEY_FILE = API_KEY_FOLDER + "/key.txt";

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        //program needs to access Google Books api which requires an api key
        //if this is the user's first time running the app they need
        //to go to https://developers.google.com/console/help/new/#generatingdevkeys and make an api key
        File keyFile = new File(API_KEY_FILE);
        File keyFolder = new File(API_KEY_FOLDER);
        if(!keyFolder.exists()){
            keyFolder.mkdir();
            System.out.println("made");
        }
        if (!keyFile.exists()) {
            System.out.println("Please visit https://developers.google.com/console/help/new/#generatingdevkeys and get an API key (make sure to enable the Google Books api)");
            System.out.print("Enter Google API key >");
            String apiKey = scan.nextLine();
            FileManager fileManager = new FileManager();
            fileManager.saveWord(apiKey, keyFile);
        }

        //takes a little bit to download the list of books
        //make sure they want to update it
        File bookList = new File(Ferriss.BOOK_FILE);
        if (bookList.exists()) {
            while (true) {
                System.out.println("You have already downloaded the list of books.");
                System.out.println("Do you want to update the list (y/n) >");
                String answer = scan.nextLine();
                if (answer.equals("y")) {
                    Ferriss ferriss = new Ferriss();
                    ferriss.downloadAllBooks();
                    break;
                } else if (answer.equals("n")) {
                    break;
                }
            }

        }

    }
}
