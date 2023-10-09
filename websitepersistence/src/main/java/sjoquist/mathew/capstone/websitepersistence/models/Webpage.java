package sjoquist.mathew.capstone.websitepersistence.models;

import java.util.HashMap;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Webpage {
    
    @Id
    public final String url;

    public final HashMap<String, Integer> termMatrix;

    @Relationship("LINKS_TO")
    public final Set<Webpage> links;

    public Webpage(String url, HashMap<String, Integer> termMatrix, Set<Webpage> links) {
        this.url = url;
        this.termMatrix = termMatrix;
        this.links = links;
    }
}
