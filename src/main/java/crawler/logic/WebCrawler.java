package main.java.crawler.logic;
import java.io.IOException;
import java.util.*;

import main.java.crawler.util.Normalize;
import main.java.crawler.util.UrlValidator;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class WebCrawler {
    private static final int maxPages = 10;
    private Set<String> visited = null;
    private Queue<String> links = null;
    private final PageProcessor pageProcessor = PageProcessor.getInstance();

    // *** Singleton ***
    private static WebCrawler w = null;

    private WebCrawler(){
        visited = new HashSet<>();
        links = new LinkedList<>();
    }

    public static WebCrawler getInstance(){
        if(w==null)
            w=new WebCrawler();
        return w;
    }
    // ************************************

    public void crawl(String URL){
        int p = 0;
        links.add(URL);
        while(!links.isEmpty() && p<maxPages){
            String nLink = links.poll();
            String url = Normalize.normalize(nLink);
            if(!UrlValidator.isValidURL(url))
                continue;
            if (visited.contains(url)) {
                System.out.println("Already visited: " + url);
                continue;
            }
            visited.add(url);
            System.out.println("Crawling: " + url);
            try {
                Document doc = Jsoup.connect(url).get();
                var pageInfo = pageProcessor.process(doc);
                System.out.println("Title: " + pageInfo.getTitle());
                var newLinks = pageInfo.getLinks();
                System.out.println("Found " + newLinks.size() + " links on this page.");
                int count = 0;
                for (String link : newLinks) {
                    if (count++ >= 10) break;
                    System.out.println(" → " + link);
                }
                int c = 0;
                for (String link : newLinks) {
                    if (c++ >= 10) break;
                    if (!visited.contains(link) && !links.contains(link)) {
                        links.add(link);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            p++;
        }
        System.out.println("Crawling complete. Total pages visited: " + visited.size());
    }
}
