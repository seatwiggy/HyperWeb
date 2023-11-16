package sjoquist.mathew.capstone.websiteretrieval.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import sjoquist.mathew.capstone.websiteretrieval.models.Webpage;

public interface IWebpageRepository extends Neo4jRepository<Webpage, String> {
    @Query("MATCH (w:Webpage) WHERE ALL (text IN $text WHERE w.text CONTAINS text) RETURN w")
    List<Webpage> findByTextContainingAllIgnoreCaseList(List<String> text);

    List<Webpage> findByUrlContainingIgnoreCase(String url);

    List<Webpage> findByUrlContainingIgnoreCaseAndTextContainingAllIgnoreCase(String url, List<String> text);

    // TODO: implement filtering with multiple urls
}
