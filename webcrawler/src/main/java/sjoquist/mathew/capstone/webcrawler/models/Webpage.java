package sjoquist.mathew.capstone.webcrawler.models;

import java.util.Map;
import java.util.Set;

public class Webpage {
    public final String url;
    public final Map<String, Integer> termMatrix;
    public final Set<Webpage> linksTo;

    public Webpage(String url, Map<String, Integer> termMatrix, Set<Webpage> links) {
        this.url = url;
        this.termMatrix = termMatrix;
        this.linksTo = links;
    }

    public Webpage(String url) {
        this.url = url;
        this.termMatrix = null;
        this.linksTo = null;
    }
}
