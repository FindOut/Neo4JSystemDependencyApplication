package com.findout.dependency.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * Created by magnusdep on 27/02/14.
 */
@RelationshipEntity(type = "DEPENDS_ON")
public class Dependency {
    @GraphId
    Long id;

    @StartNode
    Resource startNode;

    @EndNode
    Resource endNode;

    DependencyType dependencyType;

    public Dependency() {
    }

    public Dependency(Resource startNode, Resource endNode, DependencyType dependencyType) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.dependencyType = dependencyType;
    }

    public DependencyType getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    public Resource getStartNode() {
        return startNode;
    }

    public Resource getEndNode() {
        return endNode;
    }
}
