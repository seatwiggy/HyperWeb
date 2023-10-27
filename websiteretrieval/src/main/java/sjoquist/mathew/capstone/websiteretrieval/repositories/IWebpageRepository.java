package sjoquist.mathew.capstone.websiteretrieval.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import sjoquist.mathew.capstone.websiteretrieval.models.Webpage;

public interface IWebpageRepository extends Neo4jRepository<Webpage, String> {
    List<Webpage> findByTextContainingIgnoreCase(String text);

    List<Webpage> findByUrlContainingIgnoreCase(String url);
}
