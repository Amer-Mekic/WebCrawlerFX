package main.java.crawler.logic;
import java.io.IOException;
import java.util.*;

import main.java.crawler.model.CrawlNode;
import main.java.crawler.util.Normalize;
import main.java.crawler.util.UrlValidator;
import org.jsoup.*;
import org.jsoup.nodes.*;

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

    public CrawlNode crawl(String startUrl, int maxLinksPerPage, int maxDepth, ProgressCallback callback) {
        visited = new HashSet<>();
        links = new LinkedList<>();

        CrawlNode root = new CrawlNode(startUrl);
        links.add(startUrl);

        Map<String, CrawlNode> nodeMap = new HashMap<>();
        nodeMap.put(startUrl, root);

        int pagesVisited = 0;

        while (!links.isEmpty() && pagesVisited < maxDepth) {
            String currentUrl = links.poll();
            currentUrl = Normalize.normalize(currentUrl);

            if (!UrlValidator.isValidURL(currentUrl) || visited.contains(currentUrl)) continue;

            visited.add(currentUrl);
            System.out.println("Crawling: " + currentUrl);

            try {
                Document doc = Jsoup.connect(currentUrl).get();
                var pageInfo = pageProcessor.process(doc);

                List<String> newLinks = pageInfo.getLinks();
                CrawlNode currentNode = nodeMap.get(currentUrl);

                int count = 0;
                for (String link : newLinks) {
                    if (count++ >= maxLinksPerPage) break;
                    link = Normalize.normalize(link);

                    if (!visited.contains(link) && !links.contains(link)) {
                        links.add(link);
                    }

                    if (currentNode.hasInAncestors(link)) {
                        System.out.println("Cycle detected: skipping " + link);
                    }
                    // Avoid duplicate nodes for same URL
                    if (!currentNode.hasInAncestors(link)) {
                        CrawlNode childNode = nodeMap.getOrDefault(link, new CrawlNode(link));
                        nodeMap.putIfAbsent(link, childNode);
                        childNode.setParent(currentNode); // set parent
                        currentNode.addChild(childNode);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            pagesVisited++;
            if (callback != null) {
                callback.update((double) pagesVisited / maxDepth);
            }
        }

        return root;
    }
}
