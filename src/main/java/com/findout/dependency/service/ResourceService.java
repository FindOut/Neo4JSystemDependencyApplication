package com.findout.dependency.service;

import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.domain.Resource;

import java.util.Collection;
import java.util.List;

/**
 * Created by magnusdep on 27/02/14.
 */
public interface ResourceService {
    public Resource addResource(String name, String description);
    public Resource connectResources(Resource main, Resource dependsOn, DependencyType type);
    public Resource getResource(Long id);
    public Collection<Resource> getAllResources();
    public Resource findResourceByName(String name);
    public void deleteResource(Resource resource);
    public void deleteRelation(Resource main, Resource dependsOn, DependencyType type);
    public Resource updateResource(Resource resource);
}
