package com.findout.dependency.service;

import com.findout.dependency.domain.Dependency;
import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.domain.Resource;
import com.findout.dependency.repository.ResourceRepository;
import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class ResourceServiceImpl implements ResourceService {


    @Autowired
    ResourceRepository resourceRepository;

    @Transactional
    public Resource addResource(String name, String description){
        Resource system = new Resource(name);
        system.setDescription(description);
        return resourceRepository.save(system);
    }

    @Transactional
    public Resource connectResources(Resource main, Resource dependsOn, DependencyType type) {
        resourceRepository.createRelationshipBetween(main, dependsOn, Dependency.class, type.name());
        return main;
    }

    @Override
    public Resource getResource(Long id) {
        return resourceRepository.findOne(id);
    }

    @Override
    public Resource findResourceByName(String name) {
        return resourceRepository.findByName(name);
    }

    @Override
    public Collection<Resource> getAllResources() {
        Iterable<Resource> result = resourceRepository.findAll();
        return IteratorUtil.asCollection(result);
    }

    @Override
    @Transactional
    public void deleteResource(Resource resource) {
        resourceRepository.delete(resource);
    }

    @Override
    @Transactional
    public void deleteRelation(Resource main, Resource dependsOn, DependencyType type) {
        resourceRepository.deleteRelationshipBetween(main, dependsOn, type.name());
    }

    @Override
    @Transactional
    public Resource updateResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    /**
     * Only used for testing
     * @param resourceRepository
     */
    protected void setResourceRepository(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    private class ObjectNotFoundException extends Exception {
    }
}
