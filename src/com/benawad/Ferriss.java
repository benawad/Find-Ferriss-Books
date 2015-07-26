package com.benawad;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ferriss {

    public final static String BOOK_FILE = "unCheckedBooks.txt";
    public final static String LINKS = "http://fourhourworkweek.com/podcast/";

    public void downloadAllBooks() {
        //get all the links to every tim ferriss podcast
        List<String> links = fetchLinks();
        //get every book reccomended in each episode
        //[nameOfBook, author]
        List<String[]> books = fetchBooks(links);

        //some of the the author's names are followed by a dash and some extra info we don't need
        for(int i = 0; i < books.size(); i++){
            String author = books.get(i)[1];
            if(author.contains("–")){
                int loc = author.indexOf("–");
                books.get(i)[1] = author.substring(0, loc-1);
            }
        }

        //change string array to string with format = book||author
        List<String> sBooks = new ArrayList<>();
        for(String[] array : books){
            sBooks.add(array[0].trim()+"||"+array[1].trim());
        }

        //there may be duplicates because 2 people can like the same book
        //we want to remove duplicates
        Set<String> temp = new HashSet<>();
        temp.addAll(sBooks);
        sBooks.clear();
        sBooks.addAll(temp);

        //save the list to a text file
        Saver saver = new Saver();
        //the list of books has some non-books in it
        //we will get rid of those later
        saver.saveList(sBooks, BOOK_FILE);
    }

    private List<String> fetchLinks() {
        List<String> links = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(LINKS).get();
            org.jsoup.nodes.Element body = doc.body();
            Elements all = body.getAllElements();
            //loop through every tag in the html document
            for (Element el : all) {
                //find the div that has the classname entry-content
                if (el.hasAttr("class") && el.className().equals("entry-content")) {
                    //each link has type-post as one of the classes, so search for that
                    Elements eles = el.getElementsByAttributeValueContaining("class", "type-post");
                    for (Element e : eles) {
                        Element a = e.getElementsByTag("a").get(0);
                        String url = a.attr("href");
//                        System.out.println("Found the link for " + a.text());
                        links.add(url);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }

    //return number of times a word is found in array
    public int wordCount(String word, String[] list){
        int count = 0;
        for(String item : list){
            if (item.equals(word)){
                count++;
            }
        }
        return count;
    }

    //go to each link and get all the books mentioned in the description
    private List<String[]> fetchBooks(List<String> links) {
        List<String[]> books = new ArrayList<>();
        for (String link : links) {
            try {
                Document doc = Jsoup.connect(link).get();
                Element body = doc.body();
                Elements all = body.getAllElements();
                for (Element el : all) {
                    if (el.hasAttr("class") && el.className().equals("entry-content")) {
                        Elements lis = el.getElementsByTag("li");
                        for(Element li : lis){
                            //tim puts the books in <li> tags
                            //the books match the pattern of '<a some text>bookName</a> by authorName'
                            //this regex works, but could be made better
                            String regex = "<a.+>.+</a>.*by.\\w+.+";
                            if(li.html().matches(regex)){
//                                System.out.println("Found the book " + li.text());
                                if(wordCount("by", li.text().split("\\s+")) > 1){
//                                    books.add(customSplit(li.text()));
                                    //^does not work needs better implementation
                                }else {
                                    books.add(li.text().split("by"));
                                }
                            }
                        }
                        //once we find the entry-content element we don't need to continue looping
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    //we want to split on the word 'by'
    //but if the word 'by' is in the name of the book it messes up the split
    //this method is flaky
    //works for this : Bird by Bird by Anne Lamott
    //fails for this : Atlas Shrugged by Ayn Rand – recommended by Gabby
    //not sure how to properly split some of the strings
    //only 5 or so books have the word 'by' in them
    //for now leaving them off the list
    private String[] customSplit(String text) {
        String[] words = text.split("\\s+");
        for(int i = 0; i < words.length; i++){
           if(noMoreComing("by", words, i)){
               break;
           } else if (words[i].equals("by")){
               words[i] = "~~";
           }
        }

        text = String.join(" ", words);
        System.out.println(text);
        String[] bookInfo = text.split("\\s+by\\s+");
        bookInfo[0] = bookInfo[0].replaceAll("~~", "by");
        return bookInfo;
    }

    private boolean noMoreComing(String word, String[] list, int start){
        boolean noMore = true;
        for(int i = start+1; i < list.length; i++){
            if(list[i].equals(word)){
                noMore = false;
            }
        }
        return noMore;
    }

}
