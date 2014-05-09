package com.findout.dependency.domain;

import com.findout.dependency.event.resource.ResourceDetails;
import com.findout.dependency.repository.ResourceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.neo4j.annotation.*;
import org.springframework.data.neo4j.template.Neo4jOperations;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by magnusdep on 26/02/14.
 */
@NodeEntity
public class Resource {
    @GraphId
    Long id;

    @Indexed(unique = true)
    String name;
    String description;

    //depends on
    @Fetch //Look into lazy fetching of this
    @RelatedToVia(type = "DEPENDS_ON")
    Set<Dependency> dependencies = new HashSet<>();

    public Resource() {
    }

    public Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public Dependency dependsOn(ResourceRepository resourceRepository, Resource dependencyResource, DependencyType type){
        Dependency dependency = new Dependency(this, dependencyResource, type);
        return resourceRepository.save(dependency);
    }*/

    @Override
    public String toString() {
        return name;
    }

    public ResourceDetails toResourceDetails(){
        ResourceDetails details = new ResourceDetails();

        BeanUtils.copyProperties(this, details);

        for(Dependency d: dependencies){
            details.addDependency(d.getEndNode().getId(), d.getDependencyType());
        }

        return details;
    }
}
