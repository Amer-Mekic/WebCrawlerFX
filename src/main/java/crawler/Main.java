package main.java.crawler;

import main.java.crawler.logic.WebCrawler;

public class Main {
    public static void main(String[] args){
        WebCrawler.getInstance().crawl("https://example.com");
    }
}
