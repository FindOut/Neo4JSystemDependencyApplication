package com.findout.dependency.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

/**
 * Created by magnusdep on 26/03/14.
 *
 */
@Configuration
@PropertySource("classpath:${spring.profiles.active}.properties")
@EnableNeo4jRepositories("com.findout.dependency.repository")
@EnableTransactionManagement
@Profile("test")
public class Neo4jTestConfig extends Neo4jConfiguration {

    public Neo4jTestConfig() {
        setBasePackage("com.findout.dependency.domain");
    }

    @Resource
    private Environment environment;

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase(environment.getProperty("neo4j.host"));
    }

}
