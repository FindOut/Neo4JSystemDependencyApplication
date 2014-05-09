package com.findout.dependency.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created by magnusdep on 28/03/14.
 *
 */
@Configuration
@EnableNeo4jRepositories("com.findout.dependency.repository")
@EnableTransactionManagement
@Profile("dev")
public class Neo4JDevConfig extends Neo4jConfiguration {

    public Neo4JDevConfig() {
        setBasePackage("com.findout.dependency.domain");
    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase("data/graph.db");
    }
}
