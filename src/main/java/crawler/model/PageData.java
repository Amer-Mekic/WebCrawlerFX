package main.java.crawler.model;

import java.util.ArrayList;
import java.util.List;

public class PageData {
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
