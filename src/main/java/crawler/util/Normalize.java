package main.java.crawler.util;

import java.net.URI;

public class Normalize {
    public static String normalize(String url) {
        try {
            URI uri = new URI(url).normalize();
            String scheme = uri.getScheme().toLowerCase();
            String host = uri.getHost().toLowerCase();
            int port = uri.getPort();
            String path = uri.getPath().replaceAll("/$", ""); // remove trailing slash
            String query = uri.getQuery();

            if (port == 80 || port == 443 || port == -1)
                port = -1; // ignore default ports

            URI cleaned = new URI(scheme, null, host, port, path, query, null);
            return cleaned.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
