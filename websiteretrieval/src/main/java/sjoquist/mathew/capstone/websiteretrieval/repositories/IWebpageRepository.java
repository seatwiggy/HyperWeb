package sjoquist.mathew.capstone.websiteretrieval.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import sjoquist.mathew.capstone.websiteretrieval.models.Webpage;

public interface IWebpageRepository extends Neo4jRepository<Webpage, String> {
    
}
