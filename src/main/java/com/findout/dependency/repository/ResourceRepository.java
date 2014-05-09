package com.findout.dependency.repository;

import com.findout.dependency.domain.Resource;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;
import org.springframework.stereotype.Component;

/**
 * Created by magnusdep on 27/02/14.
 */
public interface ResourceRepository extends GraphRepository<Resource>, RelationshipOperationsRepository<Resource> {
    Resource findByName(String name);
}
