package main.java.crawler.logic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;

public class PageProcessor {

    public static class PageData{
        private String title = "";
        private List<String> pageLinks = new ArrayList<>();

        public PageData(String title, List<String> pageLinks){
            this.title = title;
            this.pageLinks = pageLinks;
        }

        public String getTitle(){
            return title;
        }
        public List<String> getLinks(){
            return pageLinks;
        }
    }

    private static PageProcessor pageProcessor = null;

    public static PageProcessor getInstance(){
        if(pageProcessor==null)
            pageProcessor = new PageProcessor();
        return pageProcessor;
    }

    public PageData process(Document page){
        List<String> links = new ArrayList<>();
        String title = page.title();
        Elements newLinks = page.select("a[href]");
        for (Element link : newLinks) {
            String nextUrl = link.absUrl("href");
            //Document nextDoc = Jsoup.connect(nextUrl).get();
            links.add(nextUrl);
        }
        return new PageData(title, links);
    }
}