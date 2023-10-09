package sjoquist.mathew.capstone.webcrawler.models;

import java.util.HashMap;

public class Webpage {
    public final String url;
    public final HashMap<String, Integer> termMatrix;
    public final String[] links;

    public Webpage(String url, HashMap<String, Integer> termMatrix, String[] links) {
        this.url = url;
        this.termMatrix = termMatrix;
        this.links = links;
    }
}
