package main.java.crawler.util;

import java.net.*;

public class UrlValidator {
    public static boolean isValidURL(String URL) {
        if (URL == null || URL.isEmpty()) return false;
        try {
            URI uri = URI.create(URL);
            URL u = uri.toURL();
            String scheme = u.getProtocol();
            return scheme.equals("http") || scheme.equals("https");
        } catch (IllegalArgumentException | MalformedURLException e) {
            return false;
        }
    }
}