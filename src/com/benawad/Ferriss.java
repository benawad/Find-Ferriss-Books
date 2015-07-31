package com.benawad;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ferriss {

    public final static String BOOK_FILE = "book_titles.txt";
    public final static String LINKS = "http://fourhourworkweek.com/podcast/";
    public final static String SEPARATOR = "::";

    public void downloadAllBooks() {
        //get all the links to every tim ferriss podcast
        List<String> links = fetchLinks();
        //get every book recommended in each episode
        List<String[]> books = fetchBooks(links);

        //there may be duplicates because 2 people can like the same book
        //we want to remove duplicates
        Set<String[]> temp = new HashSet<>();
        temp.addAll(books);
        books.clear();
        books.addAll(temp);

        List<String> sBooks = new ArrayList<>();
        for(String[] book : books){
           sBooks.add(book[0].trim() + SEPARATOR + book[1].trim());
        }

        //save the list to a text file
        FileManager fileManager = new FileManager();
        fileManager.saveList(sBooks, new File(BOOK_FILE));
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
                                //tim links all the books to amazon or audible
                                //so we can check if the <a>'s href is linking to amazon or audible
                                //if it is not, it is probably not a book and we don't want it
                                String href = li.getElementsByTag("a").get(0).attr("href");
                                if(href.contains("amazon.com")){
                                    //getting book title from amazon link
                                    //example link: http://www.amazon.com/The-Checklist-Manifesto-Things-Right/dp/0312430000/?tag=offsitoftimfe-20
                                    //we just want The-Checklist-Manifesto-Things-Right
                                    String end = href.substring(href.indexOf("com/"));
                                    int loc1 = end.indexOf("/")+1;
                                    String newEnd = end.replaceFirst("/", ":");
                                    int loc2 = newEnd.indexOf("/");
                                    String title = newEnd.substring(loc1, loc2);

                                    //the link is different if tim uses an affiliate link
                                    //for now just ignoring those
                                    if(!title.equals("gp")){
                                        books.add(new String[]{title, href});
                                    }
                                } else if(href.contains("audible.com")){
                                    //example link: http://www.audible.com/pd/Teens/The-Graveyard-Book-Audiobook/B002V8DEKC/?tag=offsitoftimfe-20
                                    //we want The-Graveyard-Book-Audiobook
                                    //the url format is audible.com/pd/category/bookname
                                    String com = href.substring(href.indexOf("com"));
                                    com = com.replaceFirst("/", ":");
                                    com = com.replaceFirst("/", ":");
                                    int loc1 = com.indexOf("/")+1;
                                    com = com.replaceFirst("/", ":");
                                    int loc2 = com.indexOf("/");
                                    String title = com.substring(loc1, loc2);
                                    books.add(new String[]{title, href});
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

}
