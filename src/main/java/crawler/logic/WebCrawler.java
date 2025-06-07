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
    Map<String, List<String>> crawlGraph = new HashMap<>();
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

    public interface ProgressCallback {
        void update(double progress); // 0.0 to 1.0
    }

    public Map<String, List<String>> crawl(String startUrl, int maxLinksPerPage, int maxDepth, ProgressCallback callback) {
        visited = new HashSet<>();
        links = new LinkedList<>();
        crawlGraph = new HashMap<>();

        int pagesVisited = 0;
        links.add(startUrl);

        while (!links.isEmpty() && pagesVisited < maxDepth) {
            String nLink = links.poll();
            String url = Normalize.normalize(nLink);

            if (!UrlValidator.isValidURL(url)) continue;
            if (visited.contains(url)) continue;

            visited.add(url);
            System.out.println("Crawling: " + url);

            try {
                Document doc = Jsoup.connect(url).get();
                var pageInfo = pageProcessor.process(doc);
                System.out.println("Title: " + pageInfo.getTitle());

                var newLinks = pageInfo.getLinks();
                crawlGraph.put(url, newLinks);

                int count = 0;
                for (String link : newLinks) {
                    if (count++ >= maxLinksPerPage) break;
                    if (!visited.contains(link) && !links.contains(link)) {
                        links.add(link);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // or log better
            }

            pagesVisited++;

            if (callback != null) {
                callback.update((double) pagesVisited / maxDepth); // update progress
            }
        }

        System.out.println("Crawling complete. Total pages visited: " + visited.size());
        return crawlGraph;
    }
}
