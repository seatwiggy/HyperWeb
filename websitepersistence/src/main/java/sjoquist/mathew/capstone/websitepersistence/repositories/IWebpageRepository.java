package sjoquist.mathew.capstone.websitepersistence.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import sjoquist.mathew.capstone.websitepersistence.models.Webpage;

public interface IWebpageRepository extends Neo4jRepository<Webpage, String> {

}
