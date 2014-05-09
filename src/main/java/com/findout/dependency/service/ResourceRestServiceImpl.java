package com.findout.dependency.service;

import com.findout.dependency.domain.Resource;
import com.findout.dependency.event.resource.*;
import com.findout.dependency.rest.domain.RestResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by magnusdep on 10/03/14.
 */
@Service
public class ResourceRestServiceImpl implements ResourceRestService {
    @Autowired
    ResourceService resourceService;

    @Override
    public ResourceDetailsEvent requestResourceDetails(RequestResourceDetailsEvent requestResourceDetailsEvent) {

        Resource resource = resourceService.getResource((Long)requestResourceDetailsEvent.getId());

        if (resource == null) {
            return ResourceDetailsEvent.notFound(requestResourceDetailsEvent.getId());
        }

        return new ResourceDetailsEvent(resource.getId(), resource.toResourceDetails());

    }

    @Override
    public ResourceDetailsEvent findByName(FindResourceByNameEvent findResourceByNameEvent) {
        Resource resource = resourceService.findResourceByName(findResourceByNameEvent.getName());

        if (resource == null) {
            return ResourceDetailsEvent.notFound(findResourceByNameEvent.getName());
        }

        return new ResourceDetailsEvent(resource.getName(), resource.toResourceDetails());
    }

    @Override
    public AllResourcesEvent findAll(RequestAllResourcesEvent requestAllResourcesEvent) {
        Collection<ResourceDetails> result = new ArrayList<>();

        for (Resource resource : resourceService.getAllResources()) {
            result.add(resource.toResourceDetails());
        }

        return new AllResourcesEvent(result);
    }

    @Override
    public ResourceCreatedEvent addResource(CreateResourceEvent createResourceEvent) {
        ResourceDetails details = createResourceEvent.getDetails();
        Resource resource = resourceService.addResource(details.getName(), details.getDescription());
        return new ResourceCreatedEvent(resource.getId(), resource.toResourceDetails());
    }

    @Override
    public ResourceDeletedEvent deleteResource(DeleteResourceEvent deleteResourceEvent) {
        Resource resource = resourceService.getResource(deleteResourceEvent.getId());

        if (resource == null) {
            return ResourceDeletedEvent.notFound(deleteResourceEvent.getId());
        }

        ResourceDetails details = resource.toResourceDetails();

        try {
            resourceService.deleteResource(resource);
        }
        catch (Exception e) {
            return ResourceDeletedEvent.deletionForbidden(deleteResourceEvent.getId(),
                    details);
        }

        return new ResourceDeletedEvent(deleteResourceEvent.getId(), details);
    }

    @Override
    public ResourceUpdatedEvent updateResource(UpdateResourceEvent updateResourceEvent) {
        Resource resource = resourceService.getResource(updateResourceEvent.getId());

        if (resource == null) {
            return ResourceUpdatedEvent.notFound(updateResourceEvent.getId());
        }

        resource = resourceService.updateResource(updateResourceEvent.getDetails().toResource());

        return new ResourceUpdatedEvent(resource.getId(), resource.toResourceDetails());
    }

    @Override
    public ResourcesConnectedEvent connectResources(ConnectResourcesEvent connectResourcesEvent) {
        Resource source = resourceService.getResource(connectResourcesEvent.getResourceId());
        Resource dependsOn = resourceService.getResource(connectResourcesEvent.getDependsOnId());

        if (source == null || dependsOn == null) {
            return ResourcesConnectedEvent.notFound(connectResourcesEvent.getResourceId(),
                    connectResourcesEvent.getDependsOnId());
        }

        source = resourceService.connectResources(source, dependsOn, connectResourcesEvent.getType());

        return new ResourcesConnectedEvent(source.getId(), source.toResourceDetails(), dependsOn.getId());

    }

    @Override
    public RelationDeletedEvent deleteRelation(DeleteRelationEvent event) {
        Resource source = resourceService.getResource(event.getResourceId());
        Resource dependsOn = resourceService.getResource(event.getDependsOnId());

        if (source == null || dependsOn == null) {
            return RelationDeletedEvent.notFound(event.getResourceId(), event.getDependsOnId());
        }

        ResourceDetails details = source.toResourceDetails();

        try {
            resourceService.deleteRelation(source, dependsOn, event.getType());
        }
        catch (Exception e) {
            return RelationDeletedEvent.deletionForbidden(source.getId(), dependsOn.getId(),
                    details);
        }

        return new RelationDeletedEvent(source.getId(), dependsOn.getId(), details);
    }
}
