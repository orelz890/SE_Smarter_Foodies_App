package com.example.plainoldjava;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import org.jsoup.Connection;
import java.sql.Array;
import java.util.ArrayList;


public class scraper {
    private static final int LEVELS_TO_SCRAPE = 3;
    private static final String URL = "https://www.10dakot.co.il/";

    public static void main(String[] args) {
        crawl(1, URL, new ArrayList<String>());
    }

    private static void crawl(int level, String url, ArrayList<String> visited) {
        if (level <= LEVELS_TO_SCRAPE){
            Document doc = request(url, visited);

            if (doc != null){
                // Finding all the links in the website and loops..
                for (Element link : doc.select("a[href]")){
                    // Get rid of the href
                    String next_link = link.absUrl("href");
                    if (!visited.contains(next_link)){

//                        ================== main code here ================


                        crawl(level++, next_link, visited);
                    }
                }
            }
        }
    }

    private static Document request(String url, ArrayList<String> v){
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            // Check if its ok to visit this website
            if (con.response().statusCode() == 200){
                System.out.println("Link: " + url);
                System.out.println(doc.title());
                v.add(url);
                return doc;
            }
            return null;
        }
        catch (IOException e){
            return null;
        }
    }

}
