package main.java.crawler.model;

import java.util.ArrayList;
import java.util.List;

public class CrawlNode {
    private String url;
    private List<CrawlNode> children = new ArrayList<>();
    private CrawlNode parent; // NEW

    public CrawlNode(String url) {
        this.url = url;
    }

    public void setParent(CrawlNode parent) {
        this.parent = parent;
    }

    public boolean hasInAncestors(String targetUrl) {
        CrawlNode current = this;
        while (current != null) {
            if (current.url.equals(targetUrl)) return true;
            current = current.parent;
        }
        return false;
    }

    public String getUrl() {
        return url;
    }

    public List<CrawlNode> getChildren() {
        return children;
    }

    public void addChild(CrawlNode child) {
        children.add(child);
    }
}
