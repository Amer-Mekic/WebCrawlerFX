package main.java.crawler.logic;

import main.java.crawler.model.PageData;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

public class PageProcessor {
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
            links.add(nextUrl);
        }
        return new PageData(title, links);
    }
}