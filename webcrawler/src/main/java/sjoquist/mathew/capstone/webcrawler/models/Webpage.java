package sjoquist.mathew.capstone.webcrawler.models;

import java.io.Serializable;
import java.util.Set;

public class Webpage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String url;
    private String text;
    private Set<Webpage> linksTo;

    public Webpage() {}

    public Webpage(String url, String text, Set<Webpage> links) {
        setUrl(url);
        setText(text);
        setLinksTo(links);
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public Set<Webpage> getLinksTo() {
        return linksTo;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    private void setText(String text) {
        this.text = text;
    }

    private void setLinksTo(Set<Webpage> linksTo) {
        this.linksTo = linksTo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{url: ");
        sb.append(url);
        sb.append(", text: \"");
        sb.append(text);
        sb.append("\", linksTo: [");
        if (linksTo != null) {
            linksTo.forEach(link -> {
                sb.append(link.getUrl());
                sb.append(", ");
            });
        }
        sb.append("]}");
        return sb.toString();
    }
}
