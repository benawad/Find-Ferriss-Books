package com.benawad;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //takes a little bit to download the list of books
        //make sure they want to update it
        File bookList = new File(Ferriss.BOOK_FILE);
        if(bookList.exists()){
            Scanner scan = new Scanner(System.in);
            while(true) {
                System.out.println("You have already downloaded the list of books. Do you want to update the list (y/n):");
                String answer = scan.nextLine();
                if(answer.equals("y")){
                    Ferriss ferriss = new Ferriss();
                    ferriss.downloadAllBooks();
                    break;
                } else if (answer.equals("n")){
                    break;
                }
            }

        }

    }
}
