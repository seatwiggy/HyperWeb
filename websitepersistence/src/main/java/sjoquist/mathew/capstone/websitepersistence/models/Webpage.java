package sjoquist.mathew.capstone.websitepersistence.models;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Webpage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String url;

    @Property("text")
    private String text;

    @Relationship(type = "LINKS_TO", direction = Relationship.Direction.OUTGOING)
    private Set<Webpage> linksTo;

    @Version
    private Long version;

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

    public void setText(String text) {
        this.text = text;
    }

    public void setLinksTo(Set<Webpage> linksTo) {
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
