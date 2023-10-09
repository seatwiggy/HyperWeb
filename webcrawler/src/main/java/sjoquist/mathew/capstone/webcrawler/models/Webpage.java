package sjoquist.mathew.capstone.webcrawler.models;

import java.util.HashMap;
import java.util.Set;

public class Webpage {
    public final String url;
    public final HashMap<String, Integer> termMatrix;
    public final Set<Webpage> links;

    public Webpage(String url, HashMap<String, Integer> termMatrix, Set<Webpage> links) {
        this.url = url;
        this.termMatrix = termMatrix;
        this.links = links;
    }

    public Webpage(String url) {
        this.url = url;
        this.termMatrix = new HashMap<String, Integer>();
        this.links = null;
    }
}
