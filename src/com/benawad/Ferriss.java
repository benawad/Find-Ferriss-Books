package com.benawad;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class Ferriss {

    public final static String LINKS = "http://fourhourworkweek.com/podcast/";
    public final static String SEPARATOR = "::";

    public List<String[]> downloadAllBooks() {
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

//        List<String> title_author = new ArrayList<>();
//        for(String[] array : books){
//            title_author.add(array[0] + " by " + array[1]);
//        }
//        Collections.sort(title_author, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareToIgnoreCase(o2);
//            }
//        });
//        for(String s : title_author){
//            System.out.println(s);
//        }

        return books;
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
                        System.out.println("Found the link for " + a.text());
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
                                if(href.contains("amazon.com") || href.contains("audible.com")) {
                                    String title = li.getElementsByTag("a").get(0).text().trim();
                                    if (unique(title, href, books)) {
                                        books.add(new String[]{title, href});
                                        System.out.println("Book added to the list: " + title);
                                    }
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

    private boolean unique(String title, String url, List<String[]> books) {
        boolean unique = true;
        for(String[] array: books){
            if(title.equals(array[0]) || url.equals(array[1])){
                unique = false;
            }
        }
        return unique;
    }

}
